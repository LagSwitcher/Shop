package me.lagswitcher.Handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.lagswitcher.Main.Main;

public class Sell {
	protected Main plugin;
	protected String title;
	protected String shopn;
	protected ArrayList<String> slopen = new ArrayList<String>();
	protected Inventory shop;
	protected ArrayList<String> itemComp = new ArrayList<String>();
	protected ArrayList<Double> itemSell = new ArrayList<Double>();
	protected HashMap<String, String> sellable = new HashMap<String, String>();
	protected HashMap<String, Double> sellitems = new HashMap<String, Double>();
	protected Map<String, Integer> sellqty = new HashMap<String, Integer>();
	protected Double price;

	public Sell(Main main) {
		this.plugin = main;
		this.price = Double.valueOf(0.0D);
	}

	public void loadSell(Player p) {
		if (!this.slopen.contains(p.getName())) {
			this.slopen.add(p.getName());
		}
		int row = 9;
		int size = 5;
		if (this.plugin.utils.getSellTitle().length() > 16) {
			this.plugin.utils.getSellTitle().substring(0, 16);
		}
		this.shop = Bukkit.getServer().createInventory(p, row * size,
				ChatColor.translateAlternateColorCodes('&', this.plugin.utils.getSellTitle()));
		p.openInventory(this.shop);
	}

	public Inventory getSellInv() {
		return this.shop;
	}

	public void addSell(String item, String sell) {
		this.sellable.put(item, sell);
	}

	public Double getSell(String input, Integer qty) {
		if (this.plugin.utils.getVerbose().booleanValue()) {
			System.out.println("Sellables: " + this.sellable);
		}
		if (this.sellable.containsKey(input)) {
			if (this.plugin.utils.getVerbose().booleanValue()) {
				System.out.println("Sellables contains item");
				System.out.println("SELLABLE PRICE: " + (String) this.sellable.get(input));
			}
			Double price = Double.valueOf(Double.parseDouble((String) this.sellable.get(input)));
			return Double.valueOf(price.doubleValue() / qty.intValue());
		}
		return Double.valueOf(0.0D);
	}

	@SuppressWarnings("deprecation")
	public void trySell(Player p, Inventory inv) {
		int slot = -1;
		for (ItemStack is : inv) {
			slot++;
			if (is == null) {
				if (this.plugin.utils.getVerbose().booleanValue()) {
					System.out.println("Slot: " + slot + " Was null!");
				}
			} else {
				int itemID = is.getTypeId();
				short dataID = is.getDurability();
				String parsedItem = String.valueOf(Integer.toString(itemID)) + ":" + Integer.toString(dataID);
				if (this.plugin.utils.getVerbose().booleanValue()) {
					System.out.println("Slot: " + slot + " was NOT null! Parsed item: " + parsedItem);
				}
				double pricep = getSell(parsedItem, compareQty(parsedItem)).doubleValue();
				if (this.plugin.utils.getVerbose().booleanValue()) {
					System.out.println("SELL PER BLOCK: " + pricep);
				}
				this.price = Double.valueOf(this.price.doubleValue() + pricep * is.getAmount());
			}
		}
		if (this.plugin.utils.getVerbose().booleanValue()) {
			System.out.println("Total payout: " + this.price);
		}
		EconomyResponse r = this.plugin.econ.depositPlayer(p.getName(), this.price.doubleValue());
		if (r.transactionSuccess()) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.utils.getPrefix()) + " "
					+ ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("sold")) + " $"
					+ this.price
					+ ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("added")));
			this.price = Double.valueOf(0.0D);
		}
	}

	public Integer compareQty(String input) {
		if (this.sellqty.containsKey(input)) {
			return (Integer) this.sellqty.get(input);
		}
		return null;
	}
}
