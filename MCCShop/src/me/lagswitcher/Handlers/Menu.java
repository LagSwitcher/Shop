package me.lagswitcher.Handlers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.lagswitcher.Main.Main;

public class Menu {
	Main plugin;
	String title;
	String shopn;

	public Menu(Main main) {
		this.plugin = main;
	}

	@SuppressWarnings({ "deprecation" })
	public void loadMenu(Player p) {
		this.title = this.plugin.utils.getMenuName();
		this.shopn = "";
		if ((p.hasPermission("mccshop.use")) || (p.isOp())) {
			if (this.plugin.getConfig().getStringList("Disabled-Worlds").contains(p.getWorld().getName())) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						this.plugin.getConfig().getString("disabled-world")));
			} else {
				ArrayList<String> Ls = new ArrayList<String>();
				Inventory chest = p.getPlayer().getServer().createInventory(null,
						this.plugin.getConfig().getInt("Rows") * 9, this.title);
				int[] row1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
				int[] row2 = { 10, 11, 12, 13, 14, 15, 16, 17, 18 };
				int[] row3 = { 19, 20, 21, 23, 24, 25, 26, 27, 28 };
				int[] arrayOfInt1;
				int j = (arrayOfInt1 = row1).length;
				for (int i = 0; i < j; i++) {
					int slot2 = arrayOfInt1[i];
					if (this.plugin.getConfig().getString(String.valueOf(slot2) + ".Enabled") == "true") {
						Ls.clear();
						String Name = ChatColor.translateAlternateColorCodes('&',
								this.plugin.getConfig().getString(String.valueOf(slot2) + ".Name"));
						if (ChatColor.translateAlternateColorCodes('&',
								this.plugin.getConfig().getString(String.valueOf(slot2) + ".Desc")) != "null") {
							Ls.add(ChatColor.translateAlternateColorCodes('&',
									this.plugin.getConfig().getString(String.valueOf(slot2) + ".Desc")));
						}
						if ((p.hasPermission("mccshop.slot." + slot2)) || (p.isOp())) {
							chest.setItem(slot2 - 1,
									setName(new ItemStack(
											Material.getMaterial(
													this.plugin.getConfig().getString(String.valueOf(slot2) + ".Item")),
											1), Name, Ls));
						} else {
							Ls.add(ChatColor.translateAlternateColorCodes('&',
									this.plugin.getConfig().getString("no-permission")));
							chest.setItem(slot2 - 1, setName(new ItemStack(Material.getMaterial(36), 1), Name, Ls));
						}
					}
				}
				int i;
				if (this.plugin.getConfig().getInt("Rows") >= 2) {
					j = (arrayOfInt1 = row2).length;
					for (i = 0; i < j; i++) {
						int slot2 = arrayOfInt1[i];
						if (this.plugin.getConfig().getString(String.valueOf(slot2) + ".Enabled") == "true") {
							Ls.clear();
							String Name = ChatColor.translateAlternateColorCodes('&',
									this.plugin.getConfig().getString(String.valueOf(slot2) + ".Name"));
							if (this.plugin.getConfig().getString(String.valueOf(slot2) + ".Desc") != "null") {
								Ls.add(ChatColor.translateAlternateColorCodes('&',
										this.plugin.getConfig().getString(String.valueOf(slot2) + ".Desc")));
							}
							if ((p.hasPermission("guishop.slot." + slot2)) || (p.isOp())) {
								chest.setItem(slot2 - 1,
										setName(new ItemStack(Material.getMaterial(
												this.plugin.getConfig().getString(String.valueOf(slot2) + ".Item")), 1),
												Name, Ls));
							} else {
								Ls.add(ChatColor.translateAlternateColorCodes('&',
										this.plugin.getConfig().getString("no-permission")));
								chest.setItem(slot2 - 1, setName(new ItemStack(Material.getMaterial(36), 1), Name, Ls));
							}
						}
					}
				}
				if (this.plugin.getConfig().getInt("Rows") >= 3) {
					j = (arrayOfInt1 = row3).length;
					for (i = 0; i < j; i++) {
						int slot2 = arrayOfInt1[i];
						if (this.plugin.getConfig().getString(String.valueOf(slot2) + ".Enabled") == "true") {
							Ls.clear();
							String Name = ChatColor.translateAlternateColorCodes('&',
									this.plugin.getConfig().getString(String.valueOf(slot2) + ".Name"));
							if (this.plugin.getConfig().getString(String.valueOf(slot2) + ".Desc") != "null") {
								Ls.add(ChatColor.translateAlternateColorCodes('&',
										this.plugin.getConfig().getString(String.valueOf(slot2) + ".Desc")));
							}
							if ((p.hasPermission("mccshop.slot." + slot2)) || (p.isOp())) {
								chest.setItem(slot2 - 1,
										setName(new ItemStack(Material.getMaterial(
												this.plugin.getConfig().getString(String.valueOf(slot2) + ".Item")), 1),
												Name, Ls));
							} else {
								Ls.add(ChatColor.translateAlternateColorCodes('&',
										this.plugin.getConfig().getString("no-permission")));
								chest.setItem(slot2 - 1, setName(new ItemStack(Material.getMaterial(36), 1), Name, Ls));
							}
						}
					}
				}
				p.openInventory(chest);
			}
		} else {
			p.sendMessage(
					ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("no-permission")));
		}
	}

	private ItemStack setName(ItemStack is, String name, List<String> lore) {
		ItemMeta IM = is.getItemMeta();
		if (name != null) {
			IM.setDisplayName(name);
		}
		if (lore != null) {
			IM.setLore(lore);
		}
		is.setItemMeta(IM);
		return is;
	}
}
