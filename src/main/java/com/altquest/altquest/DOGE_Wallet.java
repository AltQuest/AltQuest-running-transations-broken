package com.altquest.altquest;

import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cristian on 12/15/15.
 */
public class DOGE_Wallet {
    public double balance;
    public double unconfirmedBalance;
    public String path;
    public String public_key;
    public DOGE_Wallet(String address, String path, String public_key) {
        this.address=address; this.path=path; this.public_key=public_key;
    }

    public DOGE_Wallet(String address,String privatekey) {
        this.address=address;
        this.privatekey=privatekey;
    }

    public DOGE_Wallet(String address) {
        this.address=address;
    }
    public String address=null;
    private String privatekey=null;

    double final_balance() throws IOException, ParseException {

        JSONObject blockcypher_balance=this.get_blockcypher_balance();
        int total_received=((Number)blockcypher_balance.get("total_received")).intValue();
        double final_balance=(double)total_received;
        int unconfirmed_balance=((Number)blockcypher_balance.get("unconfirmed_balance")).intValue();
        if(unconfirmed_balance>0) {
            final_balance=final_balance+(double)unconfirmed_balance;
        }
        final_balance=final_balance+this.payment_balance();

        AltQuest.REDIS.set("final_balance:"+this.address,String.valueOf(final_balance));
        return ((double)(final_balance*0.00000001));
    }
    double payment_balance() {
        if(AltQuest.REDIS.exists("payment_balance:"+this.address)) {
            return Integer.parseInt(AltQuest.REDIS.get("payment_balance:"+this.address));
        } else {
            return 0;
        }
    }
    
    public int getBlockchainHeight() {
        JSONObject jsonobj = this.makeBlockCypherCall("https://api.blockcypher.com/v1/doge/main");
        return ((Number) jsonobj.get("height")).intValue();
    }
    
    // @todo: make this just accept the endpoint name and (optional) parameters
    public JSONObject makeBlockCypherCall(String requestedURL) {
        JSONParser parser = new JSONParser();
        
        try {
            System.out.println("Making Blockcypher API call...");
            // @todo: add support for some extra params in this method (allow passing in an optional hash/dictionary/whatever Java calls it)?
            URL url;
            if(AltQuest.BLOCKCYPHER_API_KEY!=null) {
                url = new URL(requestedURL + "?token=" + AltQuest.BLOCKCYPHER_API_KEY);

            } else {
                url = new URL(requestedURL);

            }

            System.out.println(url.toString());
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/1.22 (compatible; MSIE 2.0; Windows 3.1)");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            return (JSONObject) parser.parse(response.toString());
        } catch (IOException e) {
            System.out.println("problem making API call");
            System.out.println(e);
            // Unable to call API?
        } catch (ParseException e) {
            // Bad JSON?
        }
        
        return new JSONObject(); // just give them an empty object
    }
	double balance()
	{
		balance=0;
	 try {
            
                URL url=new URL("https://dogechain.info/api/v1/address/balance/"+this.address);
                
                System.out.println(url.toString());
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Mozilla/1.22 (compatible; MSIE 2.0; Windows 3.1)");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                int responseCode = con.getResponseCode();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
		JSONParser parser = new JSONParser();
            	final JSONObject jsonobj,jsonobj2;
            try {
                jsonobj = (JSONObject) parser.parse(response.toString());
		//double val=Double.parseDouble(jsonobj2.get("price").toString());

		balance = Double.parseDouble(jsonobj.get("balance").toString());
		int successful=Integer.parseInt(jsonobj.get("success").toString());
                System.out.println(" "+successful);

            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println("[BALANCE] problem updating balance for "+this.address);
            System.out.println(e);
            // wallet might be new and it's not listed on the blockchain yet
	   } 
		return balance;
	}	
	
   
    JSONObject get_blockcypher_balance() throws IOException, ParseException {
        System.out.println("[balance] "+this.address);
        URL url;
        url=new URL("https://api.blockcypher.com/v1/doge/main/addrs/"+address+"/balance");
        System.out.println(url.toString());
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/1.22 (compatible; MSIE 2.0; Windows 3.1)");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(response.toString());
    }

	 boolean payment(double sat, String address) {
        try {
            if(this.final_balance()>=sat) {
                this.create_blockcypher_transaction(sat,  address);

                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    String sign_transaction(String tosign) throws InterruptedException, IOException {
        // Get runtime
        java.lang.Runtime rt = java.lang.Runtime.getRuntime();
        // Start a new process: UNIX command ls
        java.lang.Process p = rt.exec("/btcutils/signer/signer "+tosign+" "+AltQuest.DOGE_PRIVATE_KEY);
        // You can or maybe should wait for the process to complete
        p.waitFor();
        // System.out.println("Process exited with code = " + rt.exitValue());
        // Get process' output: its InputStream
        java.io.InputStream is = p.getInputStream();
        java.io.BufferedReader reader = new java.io.BufferedReader(new InputStreamReader(is));
        // And print each line
        String s = null;
        StringBuffer signature = new StringBuffer();
        while ((s = reader.readLine()) != null) {
            System.out.println(s);
            signature.append(s);
        }
        is.close();
        System.out.println(signature.toString());
        return signature.toString();
    }
    boolean send_blockcypher_transaction(String json) throws IOException {
        JSONParser parser = new JSONParser();

        final JSONObject jsonObject;
        try {
            // String access_token=(String) jsonobj.get("access_token");
            jsonObject= (JSONObject) parser.parse(json);
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            return false;
        }
        int fees=((Number)((JSONObject)jsonObject.get("tx")).get("fees")).intValue();
        System.out.println("fees: "+fees);
        System.out.println(jsonObject);
        JSONArray tosign=(JSONArray) jsonObject.get("tosign");
        System.out.println(tosign);
        JSONArray signatures=new JSONArray();
        JSONArray pubkeys=new JSONArray();

        try {
            for(int i=0;i<tosign.size();i++) {
                String signature=sign_transaction((String)tosign.get(i));
                signatures.add(signature);
                pubkeys.add(AltQuest.DOGE_PUBLIC_KEY);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();

            return false;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
        jsonObject.put("signatures",signatures);
        jsonObject.put("pubkeys",pubkeys);
        System.out.println(jsonObject);


        URL url = new URL("https://api.blockcypher.com/v1/doge/main/txs/send?token=" + AltQuest.BLOCKCYPHER_API_KEY);

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        String inputLine = "";

        try {
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Payload : " + jsonObject.toString());
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/1.22 (compatible; MSIE 2.0; Windows 3.1)");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(jsonObject.toString());
            out.close();
            int responseCode = con.getResponseCode();

            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (responseCode == 200||responseCode==201) {
               
                    System.out.println(AltQuest.REDIS.decrBy("payment_balance:"+AltQuest.DOGE_ADDRESS,fees));
                    System.out.println(AltQuest.REDIS.decrBy("final_balance:"+AltQuest.DOGE_ADDRESS,fees));
                

                return true;
            } else {
                return false;
            }
        } catch(IOException ioe) {
            System.err.println("IOException: " + ioe);

            InputStream error = con.getErrorStream();

            int data = error.read();
            while (data != -1) {
                //do something with data...
                inputLine = inputLine + (char)data;
                data = error.read();
            }
            error.close();


            System.out.println(inputLine);


            return false;
        }
    }
    boolean create_blockcypher_transaction(double sat, String address) throws IOException, ParseException {
        if(this.final_balance()>=sat) {


            // inputs
            JSONArray input_addresses = new JSONArray();
            input_addresses.add(AltQuest.DOGE_ADDRESS);
            JSONObject input = new JSONObject();
            input.put("addresses", input_addresses);
            JSONArray inputs = new JSONArray();
            inputs.add(input);
            // outputs
            JSONArray output_addresses = new JSONArray();
            output_addresses.add(address);
            JSONObject output = new JSONObject();
            output.put("addresses", output_addresses);
            output.put("value", ((int)(sat/10000000)));
            JSONArray outputs = new JSONArray();
            outputs.add(output);


            JSONObject payload = new JSONObject();
            payload.put("inputs", inputs);
            payload.put("outputs", outputs);
            System.out.println("Payload : " + payload.toString());

            URL url = new URL("https://api.blockcypher.com/v1/doge/main/txs/new");
            String inputLine = "";
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            try {
                System.out.println("\nSending 'POST' request to URL : " + url);
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/1.22 (compatible; MSIE 2.0; Windows 3.1)");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
                out.write(payload.toString());
                out.close();
                int responseCode = con.getResponseCode();

                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                if (responseCode == 200 || responseCode == 201) {
                    AltQuest.REDIS.set("transaction:" + this.address, response.toString());
                    System.out.println(AltQuest.REDIS.get("transaction:" + this.address));
                    if (this.send_blockcypher_transaction(AltQuest.REDIS.get("transaction:" + this.address)) == true) {
                        System.out.println(AltQuest.REDIS.decrBy("payment_balance:" + this.address, ((int)(sat/10000000))));
                        System.out.println(AltQuest.REDIS.decrBy("final_balance:" + this.address, ((int)(sat/10000000))));
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (IOException ioe) {
                System.err.println("IOException: " + ioe);

                InputStream error = con.getErrorStream();

                int data = error.read();
                while (data != -1) {
                    //do something with data...
                    inputLine = inputLine + (char) data;
                    data = error.read();
                }
                error.close();


                System.out.println(inputLine);


                return false;
            }
        } else {
            return false;
        }
    }

}
