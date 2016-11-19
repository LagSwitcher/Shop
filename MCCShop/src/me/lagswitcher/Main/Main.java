package me.lagswitcher.Main;

import me.lagswitcher.Handlers.Cache;
import me.lagswitcher.Handlers.Item;
import me.lagswitcher.Handlers.Menu;
import me.lagswitcher.Handlers.Sell;
import me.lagswitcher.Handlers.Shop;
import me.lagswitcher.Handlers.Utils;
import me.lagswitcher.Listeners.PlayerListener;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	private File defaultConfigFile = null;
	public Economy econ;
	public Item item;
	public Utils utils;
	public Cache cache;
	public Shop shop;
	public Menu menu;
	public Sell sell;
	public List<String> sellitems;

	public Main() {
		this.item = new Item(this);
		this.utils = new Utils(this);
		this.cache = new Cache(this);
		this.shop = new Shop(this);
		this.menu = new Menu(this);
		this.sell = new Sell(this);
		this.sellitems = new ArrayList<String>();
	}

	public void onEnable() {
		saveDefaultConfig();
		loadDefaults();

		if (!setupEconomy()) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	}

	public void onDisable() {
		this.cache.flushData();
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		this.econ = ((Economy) rsp.getProvider());
		if (this.econ != null) {
			return true;
		}
		return false;
	}

	public void loadDefaults() {
		this.utils.setCommand(getConfig().getString("Command"));
		this.utils.setMenuName(ChatColor.translateAlternateColorCodes('&', getConfig().getString("menuname")));
		this.utils.setPrefix(ChatColor.translateAlternateColorCodes('&', getConfig().getString("tag")));
		this.utils.setSignOnly(getConfig().getBoolean("sign-only"));
		this.utils.setSignTitle(ChatColor.translateAlternateColorCodes('&', getConfig().getString("sign-title")));
		this.utils.setVerbose(getConfig().getBoolean("Verbose"));
		this.utils.setSellCommand(getConfig().getString("sell-command"));
		this.utils.setSellTitle(getConfig().getString("sell-title"));
	}

	public void closeInventory(final Player p) {
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				p.closeInventory();
			}
		}, 1L);
	}

	public void delayMenu(final Player p) {
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				Main.this.menu.loadMenu(p);
			}
		}, 1L);
	}

	public void delayShop(final Player p) {
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				Main.this.shop.loadShop(p);
			}
		}, 1L);
	}

	@SuppressWarnings("deprecation")
	public void reloadCustomConfig() {
		if (this.customConfigFile == null) {
			this.customConfigFile = new File(getDataFolder(), "shops.yml");
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);
		InputStream defConfigStream = getResource("shops.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			this.customConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getCustomConfig() {
		if (this.customConfig == null) {
			reloadCustomConfig();
		}
		return this.customConfig;
	}

	public void saveDefaultConfig() {
		if (this.customConfigFile == null) {
			this.customConfigFile = new File(getDataFolder(), "shops.yml");
			this.defaultConfigFile = new File(getDataFolder(), "config.yml");
		}
		if (!this.customConfigFile.exists()) {
			saveResource("shops.yml", false);
		}
		if (!this.defaultConfigFile.exists()) {
			saveResource("config.yml", false);
		}
	}
}
