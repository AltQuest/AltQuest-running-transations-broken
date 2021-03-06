
package com.altquest.altquest;

import com.google.gson.JsonObject;
import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MixpanelAPI;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cristian on 11/27/15.
 */
public class InventoryEvents implements Listener {
    AltQuest altQuest;
    ArrayList<Trade> trades;
	
	String S1=(AltQuest.getExchangeRate("btc"));
	double Rate1 = Double.parseDouble(S1);

    public InventoryEvents(AltQuest plugin) {
        altQuest = plugin;
        trades=new ArrayList<Trade>();
        trades.add(new Trade(new ItemStack(Material.BED,1),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.CLAY_BALL,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.COMPASS,1),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.COOKED_BEEF,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.BOAT,1),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.EYE_OF_ENDER,1),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.FENCE,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.GLASS,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.HAY_BLOCK,32),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.LEATHER,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.OBSIDIAN,32),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.RAILS,64),USDtoBTC(0.25))); //we still need these to slow down, you know.
        trades.add(new Trade(new ItemStack(Material.SANDSTONE,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.RED_SANDSTONE,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.SMOOTH_BRICK,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.BOW,2),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.BLAZE_POWDER,16),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.REDSTONE,16),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.CHORUS_FLOWER,8),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.DIAMOND,32),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.ENDER_STONE,16),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.IRON_INGOT,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.NETHERRACK,16),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.QUARTZ,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.SOUL_SAND,16),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.SPONGE,8),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.WOOD,64),USDtoBTC(0.10)));
        trades.add(new Trade(new ItemStack(Material.WOOL,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.PAPER,32),USDtoBTC(0.25))); //needed
        trades.add(new Trade(new ItemStack(Material.PACKED_ICE,64),USDtoBTC(0.25)));
        trades.add(new Trade(new ItemStack(Material.BLAZE_ROD,16),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.GOLD_INGOT,64),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.GOLDEN_APPLE,6),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.ARROW,64),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.PRISMARINE,64),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.QUARTZ_BLOCK,64),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.SEA_LANTERN,64),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.GLOWSTONE,64),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.ANVIL, 1),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.ENDER_PEARL, 32),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.EMERALD_BLOCK,50),45000));
        trades.add(new Trade(new ItemStack(Material.NETHER_WARTS,16),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.LAPIS_ORE,16),USDtoBTC(0.40)));
        trades.add(new Trade(new ItemStack(Material.SADDLE,1),USDtoBTC(0.50))); 
        trades.add(new Trade(new ItemStack(Material.SLIME_BALL,32),USDtoBTC(0.50)));
        trades.add(new Trade(new ItemStack(Material.SHIELD,1),USDtoBTC(0.60))); //epic
        trades.add(new Trade(new ItemStack(Material.GOLDEN_APPLE, 6, (short)1),USDtoBTC(0.60))); //notch apples
        trades.add(new Trade(new ItemStack(Material.ELYTRA,1),USDtoBTC(1.00)));
	trades.add(new Trade(new ItemStack(Material.DIAMOND_SWORD,1),USDtoBTC(0.25)));
	//trades.add(new Trade(new ItemStack(Material.EXP_BOTTLE ,1),50000));
	trades.add(new Trade(new ItemStack(Material.ORANGE_SHULKER_BOX  ,1),USDtoBTC(0.40)));
	trades.add(new Trade(new ItemStack(Material.BLUE_SHULKER_BOX  ,1),USDtoBTC(0.40)));
	trades.add(new Trade(new ItemStack(Material.PURPLE_SHULKER_BOX  ,1),USDtoBTC(0.40)));
	trades.add(new Trade(new ItemStack(Material.ENDER_CHEST  ,1),USDtoBTC(0.40)));
	//trades.add(new Trade(new ItemStack(Material.ENDER_PORTAL_FRAME  ,12),500000));
	trades.add(new Trade(new ItemStack(Material.TRAPPED_CHEST  ,1),USDtoBTC(0.25)));
        //cool diamond sword


    }
	public int USDtoBTC(double usdvalue){
	
	double tempprice = ((usdvalue / Rate1)/0.00000001); 
	return (int) tempprice;		
	
	}
    @EventHandler
    void onInventoryClick(final InventoryClickEvent event) throws IOException, ParseException, org.json.simple.parser.ParseException {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getInventory();
        final User user=new User(player);
        user.setTotalExperience(user.experience());
        // Merchant inventory
        if(inventory.getName().equalsIgnoreCase("Market")) {
            if(event.getRawSlot() < event.getView().getTopInventory().getSize()) {
                // player buys
                final ItemStack clicked = event.getCurrentItem();
                if(clicked!=null && clicked.getType()!=Material.AIR) {
			if (AltQuest.REDIS.get("currency"+player.getUniqueId().toString()).equalsIgnoreCase("BTC")){
		    String BTC_RATE=altQuest.getExchangeRate("btc");	                    			    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    System.out.println("[purchase] "+player.getName()+" <- "+clicked.getType());
                    player.sendMessage(ChatColor.YELLOW + "Purchasing " + clicked.getType() + "...");

                    player.closeInventory();
                    event.setCancelled(true);
                    AltQuest.REDIS.expire("balance"+player.getUniqueId().toString(),5);

                    scheduler.runTaskAsynchronously(altQuest, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int sat = 0;
                                for (int i = 0; i < trades.size(); i++) {
                                    if (clicked.getType() == trades.get(i).itemStack.getType())
                                        sat = trades.get(i).price;

                                }
                                
                                boolean hasOpenSlots = false;
                                for (ItemStack item : player.getInventory().getContents()) {
                                    if (item == null || (item.getType() == clicked.getType() && item.getAmount() + clicked.getAmount() < item.getMaxStackSize())) {
                                        hasOpenSlots = true;
                                        break;
                                    }
                                }
                                boolean hasBalance=false;

                                if(user.wallet.balance<sat) {
                                    player.sendMessage(ChatColor.RED + "You don't have enough balance to purchase this item. EX-rate: 1BTC/"+BTC_RATE+"$US");

                                } else if (hasOpenSlots) {
                                    if(sat > 10000 && user.wallet.create_blockcypher_transaction(sat, altQuest.wallet.address) == true) {
                                        ItemStack item = event.getCurrentItem();
                                        ItemMeta meta = item.getItemMeta();
                                        ArrayList<String> Lore = new ArrayList<String>();
                                        meta.setLore(null);
                                        item.setItemMeta(meta);
                                        player.getInventory().addItem(item);
                                        player.sendMessage(ChatColor.GREEN + "" + clicked.getType() + " purchased");
                                        
                                        if (altQuest.messageBuilder != null) {
    
                                            // Create an event
                                            org.json.JSONObject sentEvent = altQuest.messageBuilder.event(player.getUniqueId().toString(), "Purchase", null);
    
    
                                            ClientDelivery delivery = new ClientDelivery();
                                            delivery.addMessage(sentEvent);
    
                                            MixpanelAPI mixpanel = new MixpanelAPI();
                                            mixpanel.deliver(delivery);
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 1)");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "You don't have space in your inventory");
                                }
                            } catch (org.json.simple.parser.ParseException e) {
		        	e.printStackTrace();
			    } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 2)");
                            } catch (IOException e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 3)");
                            }
                        }
                    });
                
            }//end btc buy start DOGE buy
		else if (AltQuest.REDIS.get("currency"+player.getUniqueId().toString()).equalsIgnoreCase("DOGE")){
		    String DOGE_RATE=altQuest.getExchangeRate("doge");
                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    System.out.println("[purchase] "+player.getName()+" <- "+clicked.getType());
                    player.sendMessage(ChatColor.YELLOW + "Purchasing " + clicked.getType() + "...");

                    player.closeInventory();
                    event.setCancelled(true);
                    AltQuest.REDIS.expire("DOGE_balance"+player.getUniqueId().toString(),5);

                    scheduler.runTaskAsynchronously(altQuest, new Runnable() {
                        @Override
                        public void run(){
                            try {
                                int sat = 0;
                                for (int i = 0; i < trades.size(); i++) {
                                    if (clicked.getType() == trades.get(i).itemStack.getType())
                                        sat = trades.get(i).price;

                                }
                                
                                boolean hasOpenSlots = false;
                                for (ItemStack item : player.getInventory().getContents()) {
                                    if (item == null || (item.getType() == clicked.getType() && item.getAmount() + clicked.getAmount() < item.getMaxStackSize())) {
                                        hasOpenSlots = true;
                                        break;
                                    }
                                }
                                boolean hasBalance=false;
                                boolean txtrue=false;
				if(user.DOGE_wallet.final_balance()<sat) {
                                    player.sendMessage(ChatColor.RED + "You don't have enough balance to purchase this item. EX-rate: 1DOGE/"+DOGE_RATE+"$US");

                                } else if (hasOpenSlots) {
					                                   
			if(sat > 10000 && user.DOGE_wallet.create_blockcypher_transaction(sat, altQuest.DOGE_wallet.address) == true) {
                                       ItemStack item = event.getCurrentItem();
                                        ItemMeta meta = item.getItemMeta();
                                        ArrayList<String> Lore = new ArrayList<String>();
                                        meta.setLore(null);
                                        item.setItemMeta(meta);
                                        player.getInventory().addItem(item);
                                        player.sendMessage(ChatColor.GREEN + "" + clicked.getType() + " purchased");
                                        
                                        if (altQuest.messageBuilder != null) {
    
                                            // Create an event
                                            org.json.JSONObject sentEvent = altQuest.messageBuilder.event(player.getUniqueId().toString(), "Purchase", null);
    
    
                                            ClientDelivery delivery = new ClientDelivery();
                                            delivery.addMessage(sentEvent);
    
                                            MixpanelAPI mixpanel = new MixpanelAPI();
                                            mixpanel.deliver(delivery);
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 1)");
                                    }

                                } else {
                                    player.sendMessage(ChatColor.RED + "You don't have space in your inventory");
                                }
                            } catch (org.json.simple.parser.ParseException e) {
        	e.printStackTrace();
        } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 2)");
                            } catch (IOException e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 3)");
                            }
                        }
                    });
                
            }//end DOGE buy start LTC buy
		else if (AltQuest.REDIS.get("currency"+player.getUniqueId().toString()).equalsIgnoreCase("LTC")){
		    String LTC_RATE=altQuest.getExchangeRate("ltc");
                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    System.out.println("[purchase] "+player.getName()+" <- "+clicked.getType());
                    player.sendMessage(ChatColor.YELLOW + "Purchasing " + clicked.getType() + "...");

                    player.closeInventory();
                    event.setCancelled(true);
                    AltQuest.REDIS.expire("LTC_balance"+player.getUniqueId().toString(),5);

                    scheduler.runTaskAsynchronously(altQuest, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int sat = 0;
                                for (int i = 0; i < trades.size(); i++) {
                                    if (clicked.getType() == trades.get(i).itemStack.getType())
                                        sat = trades.get(i).price;

                                }
                                
                                boolean hasOpenSlots = false;
                                for (ItemStack item : player.getInventory().getContents()) {
                                    if (item == null || (item.getType() == clicked.getType() && item.getAmount() + clicked.getAmount() < item.getMaxStackSize())) {
                                        hasOpenSlots = true;
                                        break;
                                    }
                                }
                                boolean hasBalance=false;
                                user.LTC_wallet.updateBalance();
                                if(user.LTC_wallet.final_balance()<sat) {
                                    player.sendMessage(ChatColor.RED + "You don't have enough balance to purchase this item. EX-rate: 1LTC/"+LTC_RATE+"$US");

                                } else if (hasOpenSlots) {
                                    if(sat > 10000 && user.LTC_wallet.transaction(sat, altQuest.LTC_wallet) == true) {
                                        ItemStack item = event.getCurrentItem();
                                        ItemMeta meta = item.getItemMeta();
                                        ArrayList<String> Lore = new ArrayList<String>();
                                        meta.setLore(null);
                                        item.setItemMeta(meta);
                                        player.getInventory().addItem(item);
                                        player.sendMessage(ChatColor.GREEN + "" + clicked.getType() + " purchased");
                                        
                                        if (altQuest.messageBuilder != null) {
    
                                            // Create an event
                                            org.json.JSONObject sentEvent = altQuest.messageBuilder.event(player.getUniqueId().toString(), "Purchase", null);
    
    
                                            ClientDelivery delivery = new ClientDelivery();
                                            delivery.addMessage(sentEvent);
    
                                            MixpanelAPI mixpanel = new MixpanelAPI();
                                            mixpanel.deliver(delivery);
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 1)");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "You don't have space in your inventory");
                                }
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 2)");
                            } catch (IOException e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 3)");
                            }
                        }
                    });
                
            }//end LTC buy start Emerald buy
		else if (AltQuest.REDIS.get("currency"+player.getUniqueId().toString()).equalsIgnoreCase("emerald")){
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    System.out.println("[purchase] "+player.getName()+" <- "+clicked.getType());
                    player.sendMessage(ChatColor.YELLOW + "Purchasing " + clicked.getType() + "...");

                    player.closeInventory();
                    event.setCancelled(true);
                    //AltQuest.REDIS.expire("balance"+player.getUniqueId().toString(),5);

                    scheduler.runTaskAsynchronously(altQuest, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int sat = 0;
                                for (int i = 0; i < trades.size(); i++) {
                                    if (clicked.getType() == trades.get(i).itemStack.getType())
                                        sat = trades.get(i).price;

                                }
                                
                                boolean hasOpenSlots = false;
                                for (ItemStack item : player.getInventory().getContents()) {
                                    if (item == null || (item.getType() == clicked.getType() && item.getAmount() + clicked.getAmount() < item.getMaxStackSize())) {
                                        hasOpenSlots = true;
                                        break;
                                    }
                                }
                                
                                if(altQuest.countEmeralds(player)<(sat/100)) {
                                    player.sendMessage(ChatColor.RED + "You don't have enough Emeralds to purchase this item.");

                                } else if (hasOpenSlots) {
                                    if(sat > 100 && altQuest.removeEmeralds(player,(sat/100)) == true){
                                        ItemStack item = event.getCurrentItem();
                                        ItemMeta meta = item.getItemMeta();
                                        ArrayList<String> Lore = new ArrayList<String>();
                                        meta.setLore(null);
                                        item.setItemMeta(meta);
                                        player.getInventory().addItem(item);
                                        player.sendMessage(ChatColor.GREEN + "" + clicked.getType() + " purchased");
                                        
                                        if (altQuest.messageBuilder != null) {
    
                                            // Create an event
                                            org.json.JSONObject sentEvent = altQuest.messageBuilder.event(player.getUniqueId().toString(), "Purchase", null);
    
    
                                            ClientDelivery delivery = new ClientDelivery();
                                            delivery.addMessage(sentEvent);
    
                                            MixpanelAPI mixpanel = new MixpanelAPI();
                                            mixpanel.deliver(delivery);
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "Sorry, Transation may be less than 1 Emerald...");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "You don't have space in your inventory");
                                }
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 2)");
                            } catch (IOException e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 3)");
                            }
                        }
                    });
                
            }//end emerald buy
		}} else {
                // player sells (experimental) for emerald blocks = to items sold @bitcoinjake09
               final ItemStack clicked = event.getCurrentItem();
                if(clicked!=null && clicked.getType()!=Material.AIR) {
                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    System.out.println("[sell] " + player.getName() + " <- " + clicked.getType());
                    player.sendMessage(ChatColor.YELLOW + "Selling " + clicked.getType() + "...");
                    player.closeInventory();
                    scheduler.runTaskAsynchronously(altQuest, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int sat = 0;
				
				int iStack = 0;
		
                                for (int i = 0; i < trades.size(); i++) {
                                    if (clicked.getType() == trades.get(i).itemStack.getType()){
                                        sat = trades.get(i).price;
					iStack=i;
					}
					
                                }
				
					//edited by bitcoinjake09 to make villagers buy items for emeralds
				if(clicked.getType()!=trades.get(iStack).itemStack.getType()) {
                                    player.sendMessage(ChatColor.RED + "I don't buy "+clicked.getType()+" silly!!!");

                                }                                    
				else{
			
    
                                        player.sendMessage(ChatColor.GREEN + "" + clicked.getType() + " sold");
					//receive emeralds based on price of items \/ \/ \/
					double StkPrice = 	(double)(trades.get(iStack).price);			
					StkPrice=StkPrice/100;
					double TraStk = 	(double)(trades.get(iStack).itemStack.getAmount());
					double satPerItem=(StkPrice/TraStk);
					double emPrice= ((double) 400/(double) 44); //price of em in sat (350/32)
					double satDub =  (satPerItem * ((double) clicked.getAmount()));
					
					double emDub =	((satDub)/(emPrice));	
					int tradeAmount=clicked.getAmount();
while (clicked.getAmount() > 0){ clicked.setAmount(clicked.getAmount() - 1);}

					int emInt=(int) emDub;// this takes the decimal off, whole #'s
					altQuest.addEmeralds(player, ((int)satDub));

					int tempSatDub = (int)(satDub *1000);
					satDub= (double) (tempSatDub /1000);
					int tempEmPrice = (int) (emPrice *1000);
					emPrice= (double) (tempEmPrice/1000);
					int tempSatPerItem = (int)(satPerItem*1000);
					satPerItem=(double)(tempSatPerItem/1000);
player.sendMessage(ChatColor.GREEN + "" + emInt + " Emerald Blocks received worth "+ (satDub) + " bits");
                                        player.sendMessage(ChatColor.GREEN + "44 Emerald / 400 bits = "+ emPrice+ " Bits Per Emerald Block " + " Traded "+tradeAmount+" "+ (trades.get(iStack).itemStack.getType())+" @ "+satPerItem+" bits each!!");					
					
					}
                                        if (altQuest.messageBuilder != null) {

                                            // Create an event
                                            org.json.JSONObject sentEvent = altQuest.messageBuilder.event(player.getUniqueId().toString(), "Sell", null);


                                            ClientDelivery delivery = new ClientDelivery();
                                            delivery.addMessage(sentEvent);

                                            MixpanelAPI mixpanel = new MixpanelAPI();
                                            mixpanel.deliver(delivery);
                                        }
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 2)");
                            } catch (IOException e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.RED + "Transaction failed. Please try again in a few moments (ERROR 3)");
                            }
                        }
                    });

                }
                event.setCancelled(true);
            }

        } else if (inventory.getName().equals("Compass") && !player.hasMetadata("teleporting")) {
            final User bp = new User(player);

            ItemStack clicked = event.getCurrentItem();
            // teleport to other part of the world
            boolean willTeleport = false;
            if (clicked.getItemMeta() != null && clicked.getItemMeta().getDisplayName() != null) {
                int x = 0;
                int z = 0;
                // TODO: Go to the actual destination selected on the inventory, not 0,0
                
                player.sendMessage(ChatColor.GREEN + "Teleporting to " + clicked.getItemMeta().getDisplayName() + "...");
                System.out.println("[teleport] " + player.getName() + " teleported to " + x + "," + z);
                player.closeInventory();

                player.setMetadata("teleporting", new FixedMetadataValue(altQuest, true));
                Chunk c = new Location(altQuest.getServer().getWorld("world"), x, 72, z).getChunk();
                if (!c.isLoaded()) {
                    c.load();
                }
                final int tx = x;
                final int tz = z;
                altQuest.getServer().getScheduler().scheduleSyncDelayedTask(altQuest, new Runnable() {

                    public void run() {
                        Location location = Bukkit
                                .getServer()
                                .getWorld("world")
                                .getHighestBlockAt(tx, tz).getLocation();
                        player.teleport(location);
                        player.removeMetadata("teleporting", altQuest);
                    }
                }, 60L);

            }

            event.setCancelled(true);
        } else {
            event.setCancelled(false);
        }

    }
    @EventHandler
    void onInteract(PlayerInteractEntityEvent event) {
        // VILLAGER
        if (event.getRightClicked().getType().equals(EntityType.VILLAGER)) {
            event.setCancelled(true);
            // compass

            // open menu
            Inventory marketInventory = Bukkit.getServer().createInventory(null,  54, "Market");
            for (int i = 0; i < trades.size(); i++) {
                ItemStack button = new ItemStack(trades.get(i).itemStack);
                ItemMeta meta = button.getItemMeta();
                ArrayList<String> lore = new ArrayList<String>();
                lore.add("Price: "+trades.get(i).price/100);
                meta.setLore(lore);
                button.setItemMeta(meta);
                marketInventory.setItem(i, button);
            }
            event.getPlayer().openInventory(marketInventory);
        } else {
            event.setCancelled(false);
        }

    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryOpen(InventoryOpenEvent event)
    {
        event.setCancelled(false);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryInteract(InventoryInteractEvent event) {
        event.setCancelled(false);
    }
}
