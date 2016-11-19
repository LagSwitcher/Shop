package me.lagswitcher.Handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.lagswitcher.Main.Main;

public class Shop {
	static Main plugin;
	protected String itemString;
	protected HashMap<String, Double> price = new HashMap<String, Double>();
	protected HashMap<String, Double> sell = new HashMap<String, Double>();
	protected HashMap<String, Integer> slot = new HashMap<String, Integer>();
	protected HashMap<String, Integer> itemID = new HashMap<String, Integer>();
	protected HashMap<String, Integer> dataID = new HashMap<String, Integer>();
	protected HashMap<String, Integer> qty = new HashMap<String, Integer>();
	protected HashMap<String, String> enchants = new HashMap<String, String>();
	protected Inventory shop;
	ArrayList<String> sopen = new ArrayList<String>();
	HashMap<Integer, Inventory> shopinv = new HashMap<Integer, Inventory>();
	String[] enc;
	int lvl = 0;
	String ench = "";
	String shopn = "";

	public Shop(Main main) {
		plugin = main;
		this.itemString = "";
	}

	public void setShopName(String input) {
		this.shopn = input;
	}

	public String getShopName() {
		return this.shopn;
	}

	public boolean setName(String input) {
		this.itemString = input;
		return true;
	}

	public Boolean addItem(ItemStack item, Integer slot) {
		this.shop.setItem(slot.intValue(), item);
		return Boolean.valueOf(true);
	}

	@SuppressWarnings("deprecation")
	public void loadShop(Player plyr) {
		if (!this.sopen.contains(plyr.getName())) {
			this.sopen.add(plyr.getName());
		}
		this.shopinv.clear();
		int row = 9;
		int size = 5;
		if (plugin.menu.title.length() > 16) {
			plugin.menu.title.substring(0, 16);
		}
		Inventory shop = Bukkit.getServer().createInventory(plyr, row * size, getShopName().replace(".", ""));
		String saved = getShopName().replaceAll("[\\s.]", "");
		if (plugin.cache.isSaved(saved)) {
			if (plugin.utils.getVerbose().booleanValue()) {
				System.out.println("isSaved check passed! Attempting to open shop with the saved inventory: " + saved);
			}
			shop.setContents(plugin.cache.getShop(saved));
			plyr.openInventory(shop);
		} else {
			if (this.shopinv.isEmpty()) {
				for (int i = 0; i <= 43; i++) {
					String name = "null";
					String data = "0";
					String qty = "1";
					String slot = "0";
					String price = "0";
					String sell = "0";
					String item = "0";
					Boolean isSpawner = Boolean.valueOf(false);
					if (plugin.utils.getVerbose().booleanValue()) {
						System.out.println("TEST TEST: " + getShopName());
					}
					if (plugin.getCustomConfig().get(String.valueOf(getShopName()) + i) != null) {
						List<String> nodes = plugin.getCustomConfig().getStringList(String.valueOf(getShopName()) + i);
						if (plugin.utils.getVerbose().booleanValue()) {
							System.out.println("Final item built: " + nodes + " Active!");
						}
						if (nodes != null) {
							for (int nodeapi = 0; nodeapi < nodes.size(); nodeapi++) {
								if (plugin.utils.getVerbose().booleanValue()) {
									System.out.println("Scanning shops.yml");
								}
								if (((String) nodes.get(nodeapi)).contains("item:")) {
									item = ((String) nodes.get(nodeapi)).replace("item:", "");
									if (plugin.utils.getVerbose().booleanValue()) {
										System.out.println("Item ID found: " + item);
									}
								}
								if (((String) nodes.get(nodeapi)).contains("slot:")) {
									slot = ((String) nodes.get(nodeapi)).replace("slot:", "");
									if (plugin.utils.getVerbose().booleanValue()) {
										System.out.println("Slot found: " + slot);
									}
								}
								if (((String) nodes.get(nodeapi)).contains("name:")) {
									name = ((String) nodes.get(nodeapi)).replace("name:", "").replace("'", "");
									if (plugin.utils.getVerbose().booleanValue()) {
										System.out.println("Item name found: " + name);
									}
								}
								if (((String) nodes.get(nodeapi)).contains("price:")) {
									price = ((String) nodes.get(nodeapi)).replace("price:", "").replace("'", "");
									if (plugin.utils.getVerbose().booleanValue()) {
										System.out.println("Item price found: " + price);
									}
								}
								if (((String) nodes.get(nodeapi)).contains("data:")) {
									data = ((String) nodes.get(nodeapi)).replace("data:", "");
									if (plugin.utils.getVerbose().booleanValue()) {
										System.out.println("Data value found: " + data);
									}
									if (item.equalsIgnoreCase("52")) {
										isSpawner = Boolean.valueOf(true);
										if (plugin.utils.verbose) {
											System.out.println(
													"Item IS a mob spawner! Beginning alternate data organizing!");
										}
									} else if (plugin.utils.verbose) {
										System.out.println("Bypassed item ID code, Was not a spawner!");
									}
								}
								if (((String) nodes.get(nodeapi)).contains("enchantments:")) {
									this.ench = ((String) nodes.get(nodeapi)).replace("enchantments:", "").replace("'",
											"");
									this.enc = this.ench.split(":| ");
									if (plugin.utils.getVerbose().booleanValue()) {
										System.out.println("Optional enchants found!: " + this.ench);
									}
								}
								if (((String) nodes.get(nodeapi)).contains("qty:")) {
									qty = ((String) nodes.get(nodeapi)).replace("qty:", "");
									if (plugin.utils.getVerbose().booleanValue()) {
										System.out.println("Item quantity found: " + qty);
									}
								}
								if (((String) nodes.get(nodeapi)).contains("sell:")) {
									sell = ((String) nodes.get(nodeapi)).replace("sell:", "").replace("'", "");
									if (plugin.utils.getVerbose().booleanValue()) {
										System.out.println("Item sell price found: " + sell);
									}
									if (plugin.sellitems.contains(String.valueOf(item) + "$" + sell + ")" + qty)) {
										plugin.sellitems.add(String.valueOf(item) + "$" + sell + ")" + qty);
									}
								}
							}
						}
						String sellparse = item + ":" + data;
						plugin.sell.addSell(sellparse, sell);
						if (plugin.utils.getVerbose().booleanValue()) {
							System.out.println("Added ITEM TO SELLABLES: " + sellparse);
						}
						plugin.sell.sellqty.put(sellparse, Integer.valueOf(Integer.parseInt(qty)));
						if (Integer.parseInt(data) != 0) {
							if (plugin.utils.getVerbose().booleanValue()) {
								System.out.println("ItemWithDataSelected!!!!!");
							}
							ItemStack itemwithdata = new ItemStack(Material.getMaterial(Integer.parseInt(item)),
									Integer.parseInt(qty), (short) Integer.parseInt(data));
							if (plugin.utils.getVerbose().booleanValue()) {
								System.out.println("Adding item: " + nodes + " To inventory");
							}
							if (!isInteger(sell)) {
								if (isSpawner.booleanValue()) {
									plugin.item.addPrice2(itemwithdata, Integer.valueOf(Integer.parseInt(price)),
											Boolean.valueOf(true), Integer.valueOf(Integer.parseInt(data)));
								} else {
									plugin.item.addPrice2(itemwithdata, Integer.valueOf(Integer.parseInt(price)),
											Boolean.valueOf(false), Integer.valueOf(0));
								}
								itemwithdata = plugin.item.item;
							} else {
								if (isSpawner.booleanValue()) {
									plugin.item.addPrice(itemwithdata, Integer.valueOf(Integer.parseInt(price)),
											Integer.valueOf(Integer.parseInt(sell)), Boolean.valueOf(true),
											Integer.valueOf(Integer.parseInt(data)));
								} else {
									plugin.item.addPrice(itemwithdata, Integer.valueOf(Integer.parseInt(price)),
											Integer.valueOf(Integer.parseInt(sell)), Boolean.valueOf(false),
											Integer.valueOf(0));
								}
								itemwithdata = plugin.item.item;
							}
							if (plugin.utils.getVerbose().booleanValue()) {
								System.out.println("AddPrice Method Passed! ");
							}
							if (name != "null") {
								ItemMeta itemmeta = itemwithdata.getItemMeta();
								itemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
								itemwithdata.setItemMeta(itemmeta);
								if (plugin.utils.getVerbose().booleanValue()) {
									System.out.println("Item name found! Item meta added!");
								}
							} else if (plugin.utils.getVerbose().booleanValue()) {
								System.out.println("NO Item name found! Breaking!");
							}
							if (this.enc != null) {
								for (int e = -1; e < this.enc.length; e += 2) {
									if (e >= 0) {
										if (plugin.utils.getVerbose().booleanValue()) {
											System.out.println("Enchants split into values!: ");
										}
										if ((this.enc[(e - 1)] == null) && (this.enc[e] == null)) {
											if (plugin.utils.getVerbose().booleanValue()) {
												System.out.println("Enchantments are null!");
											}
										} else {
											this.lvl = Integer.parseInt(this.enc[e]);
											itemwithdata.addUnsafeEnchantment(Enchantments.getByName(this.enc[(e - 1)]),
													this.lvl);
											if (plugin.utils.getVerbose().booleanValue()) {
												System.out.println("Enchant values: Enchant name: " + this.enc[(e - 1)]
														+ " Enchant Level: " + this.lvl);
											}
											this.enc[e] = null;
											this.enc[(e - 1)] = null;
										}
									}
								}
							}
							if (Integer.parseInt(slot) != 44) {
								if (plugin.getConfig().getString("back-button-item").contains(":")) {
									shop.setItem(Integer.parseInt(slot), itemwithdata);
									String[] backi = plugin.getConfig().getString("back-button-item").split(":");
									int bid = Integer.parseInt(backi[0]);
									int bme = Integer.parseInt(backi[1]);
									ItemStack backbutton = new ItemStack(Material.getMaterial(bid), 1, (short) bme);
									ItemMeta itemmeta2 = backbutton.getItemMeta();
									itemmeta2.setDisplayName(ChatColor.translateAlternateColorCodes('&',
											plugin.getConfig().getString("back")));
									backbutton.setItemMeta(itemmeta2);
									shop.setItem(44, backbutton);
									if (plugin.utils.getVerbose().booleanValue()) {
										System.out.println("Item with no data: " + itemwithdata + " Added to shop!");
									}
								} else {
									int bid2 = Integer.parseInt("back-button-item");
									ItemStack backbutton2 = new ItemStack(Material.getMaterial(bid2), 1, (short) 0);
									ItemMeta itemmeta3 = backbutton2.getItemMeta();
									itemmeta3.setDisplayName(ChatColor.translateAlternateColorCodes('&',
											plugin.getConfig().getString("back")));
									backbutton2.setItemMeta(itemmeta3);
									shop.setItem(44, backbutton2);
									if (plugin.utils.getVerbose().booleanValue()) {
										System.out.println("Item with no data: " + itemwithdata + " Added to shop!");
									}
								}
							} else if (plugin.utils.getVerbose().booleanValue()) {
								System.out.println("ERROR: An Item tried to overwrite button slot!");
							}
						} else {
							if (plugin.utils.getVerbose().booleanValue()) {
								System.out.println("ITEM WITHOUT DATA!");
							}
							if (Material.getMaterial(i) == null) {
								System.out.print(i);
							}
							Integer.parseInt(qty);
							ItemStack itemnodata = new ItemStack(Material.getMaterial(Integer.parseInt(item)),
									Integer.parseInt(qty));
							if (!isInteger(sell)) {
								if (isSpawner.booleanValue()) {
									plugin.item.addPrice2(itemnodata, Integer.valueOf(Integer.parseInt(price)),
											Boolean.valueOf(true), Integer.valueOf(90));
								} else {
									plugin.item.addPrice2(itemnodata, Integer.valueOf(Integer.parseInt(price)),
											Boolean.valueOf(false), Integer.valueOf(90));
								}
								itemnodata = plugin.item.item;
							} else {
								if (isSpawner.booleanValue()) {
									plugin.item.addPrice(itemnodata, Integer.valueOf(Integer.parseInt(price)),
											Integer.valueOf(Integer.parseInt(sell)), Boolean.valueOf(true),
											Integer.valueOf(90));
								} else {
									plugin.item.addPrice(itemnodata, Integer.valueOf(Integer.parseInt(price)),
											Integer.valueOf(Integer.parseInt(sell)), Boolean.valueOf(false),
											Integer.valueOf(0));
								}
								itemnodata = plugin.item.item;
							}
							if (name != "null") {
								ItemMeta itemmeta = itemnodata.getItemMeta();
								itemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
								itemnodata.setItemMeta(itemmeta);
							}
							if (this.enc != null) {
								for (int e = -1; e < this.enc.length; e += 2) {
									if (e >= 0) {
										if (plugin.utils.getVerbose().booleanValue()) {
											System.out.println("Enchants split into values!: ");
										}
										if ((this.enc[(e - 1)] == null) && (this.enc[e] == null)) {
											if (plugin.utils.getVerbose().booleanValue()) {
												System.out.println("Enchantments are null!");
											}
										} else {
											this.lvl = Integer.parseInt(this.enc[e]);
											itemnodata.addUnsafeEnchantment(Enchantments.getByName(this.enc[(e - 1)]),
													this.lvl);
											if (plugin.utils.getVerbose().booleanValue()) {
												System.out.println("Enchant values: Enchant name: " + this.enc[(e - 1)]
														+ " Enchant Level: " + this.lvl);
											}
											this.enc[(e - 1)] = null;
											this.enc[e] = null;
										}
									}
								}
							}
							shop.setItem(Integer.parseInt(slot), itemnodata);
							if (plugin.getConfig().getString("back-button-item").contains(":")) {
								String[] backi = plugin.getConfig().getString("back-button-item").split(":");
								int bid = Integer.parseInt(backi[0]);
								int bme = Integer.parseInt(backi[1]);
								ItemStack backbutton = new ItemStack(Material.getMaterial(bid), 1, (short) bme);
								ItemMeta itemmeta2 = backbutton.getItemMeta();
								itemmeta2.setDisplayName(ChatColor.translateAlternateColorCodes('&',
										plugin.getConfig().getString("back")));
								backbutton.setItemMeta(itemmeta2);
								shop.setItem(44, backbutton);
								if (plugin.utils.getVerbose().booleanValue()) {
									System.out.println("Item with no data: " + itemnodata + " Added to shop!");
								}
							} else {
								int bid3 = Integer.parseInt("back-button-item");
								ItemStack backbutton3 = new ItemStack(Material.getMaterial(bid3), 1, (short) 0);
								ItemMeta itemmeta4 = backbutton3.getItemMeta();
								itemmeta4.setDisplayName(ChatColor.translateAlternateColorCodes('&',
										plugin.getConfig().getString("back")));
								backbutton3.setItemMeta(itemmeta4);
								shop.setItem(44, backbutton3);
								if (plugin.utils.getVerbose().booleanValue()) {
									System.out.println("Item with no data: " + itemnodata + " Added to shop!");
								}
							}
						}
						this.shopinv.put(Integer.valueOf(1), shop);
						plyr.openInventory(shop);
						plyr.updateInventory();
					}
				}
			}
			if (plugin.cache.saveShop(saved, shop)) {
				if (plugin.utils.getVerbose().booleanValue()) {
					System.out.println("Saved Shop: " + getShopName());
					System.out.println("SHOP CONTENTS: ");
					System.out.println(plugin.cache.getShop(plugin.menu.shopn));
				}
			} else if (plugin.utils.getVerbose().booleanValue()) {
				System.out.println("Shop already exists!");
			}
		}
	}

	public static boolean isInteger(String s) {
		boolean isInt = plugin.utils.isInteger(s, 10);
		return isInt;
	}
}
