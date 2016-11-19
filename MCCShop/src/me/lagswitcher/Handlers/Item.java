package me.lagswitcher.Handlers;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.lagswitcher.Main.Main;

@SuppressWarnings("unused")
public class Item {
	static Main plugin;
	protected String name;
	protected Integer qty;
	protected Integer itemID;
	protected String sell;
	protected Integer buy;
	protected Integer slot;
	protected Short data;
	protected String line1;
	protected String line2;
	protected String line3;
	protected String line4;
	protected ItemStack item;
	protected boolean isSpawner;
	protected int mobid;

	public Item(Main main) {
		plugin = main;
	}

	@SuppressWarnings("deprecation")
	public void buildItem(String name1, Integer itemID1, Integer qty1, String sell1, Integer buy1, Integer slot1,
			String line11, String line21, String line31, String line41, Boolean isSpawner2, Integer mobid2) {
		this.item = new ItemStack(Material.getMaterial(this.itemID.intValue()), this.qty.intValue(),
				this.data.shortValue());
		this.name = name1;
		this.itemID = itemID1;
		this.qty = qty1;
		this.sell = sell1;
		this.buy = buy1;
		this.slot = slot1;
		this.line1 = line11;
		this.line2 = line21;
		this.line3 = line31;
		this.line4 = line41;
		this.mobid = mobid2.intValue();
		this.isSpawner = isSpawner2.booleanValue();
		if (!isInteger(this.sell)) {
			addPrice2(this.item, this.buy, Boolean.valueOf(this.isSpawner), Integer.valueOf(this.mobid));
		} else {
			addPrice(this.item, this.buy, Integer.valueOf(Integer.parseInt(this.sell)), Boolean.valueOf(this.isSpawner),
					Integer.valueOf(this.mobid));
		}
	}

	public void addPrice(ItemStack item2, Integer price, Integer sell, Boolean isSpawner, Integer mobid) {
		ItemMeta itm = item2.getItemMeta();
		List<String> itmlore = isSpawner.booleanValue()
				? Arrays.asList(new String[] {
						String.valueOf(
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("cost")))
								+ " �c$�0,�c"
								+ price,
						String.valueOf(
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("return")))
								+ " �a$�0.�a" + sell,
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line1")),
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line2")),
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line3")),
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line4")),
						"Mob ID: " + mobid })
				: Arrays.asList(new String[] {
						String.valueOf(
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("cost")))
								+ " �c$�0,�c" + price,
						String.valueOf(
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("return")))
								+ " �a$�0.�a" + sell,
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line1")),
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line2")),
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line3")),
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line4")) });
		itm.setLore(itmlore);
		item2.setItemMeta(itm);
		this.item = item2;
	}

	public void addPrice2(ItemStack item2, Integer price, Boolean isSpawner, Integer mobid) {
		ItemMeta itm = item2.getItemMeta();
		List<String> itmlore = isSpawner.booleanValue()
				? Arrays.asList(
						new String[] {
								String.valueOf(ChatColor.translateAlternateColorCodes('&',
										plugin.getConfig().getString("cost")))
										+ " �c$�0,�c"
										+ price,
								String.valueOf(ChatColor.translateAlternateColorCodes('&',
										plugin.getConfig().getString("return"))) + " �a$�0.�a" + this.sell,
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line1")),
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line2")),
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line3")),
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line4")),
								"Mob ID: " + mobid })
				: Arrays.asList(
						new String[] {
								String.valueOf(ChatColor.translateAlternateColorCodes('&',
										plugin.getConfig().getString("cost"))) + " �c$�0,�c" + price,
								"�7Cannot Resell",
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line1")),
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line2")),
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line3")),
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("line4")) });
		itm.setLore(itmlore);
		item2.setItemMeta(itm);
		this.item = item2;
	}

	public static boolean isInteger(String s) {
		boolean isInt = plugin.utils.isInteger(s, 10);
		return isInt;
	}

	public ItemStack getItem() {
		return this.item;
	}

	public int getSlot() {
		return this.slot.intValue();
	}
}
