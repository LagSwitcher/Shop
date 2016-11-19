package me.lagswitcher.Handlers;

import java.util.ArrayList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.lagswitcher.Main.Main;

public class Cache {
	protected ArrayList<ItemStack[]> cached = new ArrayList<ItemStack[]>();
	protected ArrayList<String> shopn = new ArrayList<String>();
	Main plugin;

	public Cache(Main main) {
		this.plugin = main;
	}

	public boolean saveShop(String input, Inventory shop) {
		if (this.shopn.contains(input)) {
			this.shopn.clear();
			this.cached.clear();
			if (this.plugin.utils.getVerbose().booleanValue()) {
				System.out.println("ISSaved Failed! Flushing tables on saveShop to prevent Errors!");
			}
			return false;
		}
		this.cached.add(shop.getContents());
		this.shopn.add(input);
		if (this.plugin.utils.getVerbose().booleanValue()) {
			System.out.println("Shops saved without error! Shop: " + input);
		}
		return true;
	}

	public boolean isSaved(String input) {
		if (this.shopn.isEmpty()) {
			if (this.plugin.utils.getVerbose().booleanValue()) {
				System.out.println("HashMap keys didnt exist, Returning false! Shop: " + input);
			}
			return false;
		}
		if (this.shopn.contains(input)) {
			if (this.plugin.utils.getVerbose().booleanValue()) {
				System.out.println("Shop Existed in Array, Loading inventory: " + input);
			}
			return true;
		}
		if (this.plugin.utils.getVerbose().booleanValue()) {
			System.out.println("No Condition could be met, Skipping method to avoid error! Shop: " + input);
		}
		return false;
	}

	public ItemStack[] getShop(String input) {
		if (this.plugin.utils.getVerbose().booleanValue()) {
			System.out.println("Getting items for SAVED Shop!");
		}
		for (int i = 0; i < this.shopn.size(); i++) {
			String item = (String) this.shopn.get(i);
			if (this.plugin.utils.getVerbose().booleanValue()) {
				System.out.println("Comparing item: " + item + " to " + input);
			}
			if (input.equals(item)) {
				if (this.plugin.utils.getVerbose().booleanValue()) {
					System.out.println("Inventory found! Loading!");
				}
				return (ItemStack[]) this.cached.get(i);
			}
		}
		return null;
	}

	public boolean flushData() {
		if (this.shopn.isEmpty()) {
			return false;
		}
		this.cached.clear();
		this.shopn.clear();
		return true;
	}
}
