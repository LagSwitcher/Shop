package me.lagswitcher.Handlers;

import org.bukkit.ChatColor;

import me.lagswitcher.Main.Main;

public class Utils {
	Main plugin;
	protected boolean verbose;
	protected String prefix;
	protected String menuname;
	protected String command;
	protected String signtitle;
	protected String sellcommand;
	protected String selltitle;
	protected boolean signonly;

	public Utils(Main main) {
		this.plugin = main;
		this.verbose = false;
		this.prefix = "";
		this.menuname = "";
		this.command = "";
		this.signtitle = "";
		this.sellcommand = "";
		this.selltitle = "";
		this.signonly = false;
	}

	public Boolean getVerbose() {
		return Boolean.valueOf(this.verbose);
	}

	public void setVerbose(boolean input) {
		this.verbose = input;
	}

	public void setPrefix(String input) {
		this.prefix = ChatColor.translateAlternateColorCodes('&', input);
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setMenuName(String input) {
		this.menuname = ChatColor.translateAlternateColorCodes('&', input);
	}

	public String getMenuName() {
		return this.menuname;
	}

	public void setSellTitle(String input) {
		this.selltitle = input;
	}

	public String getSellTitle() {
		return this.selltitle;
	}

	public void setCommand(String input) {
		this.command = input;
	}

	public String getCommand() {
		return this.command;
	}

	public void setSellCommand(String input) {
		this.sellcommand = input;
	}

	public String getSellCommand() {
		return this.sellcommand;
	}

	public void setSignTitle(String input) {
		this.signtitle = ChatColor.translateAlternateColorCodes('&', input);
	}

	public String getSignTitle() {
		return this.signtitle;
	}

	public void setSignOnly(boolean input) {
		this.signonly = input;
	}

	public Boolean getSignOnly() {
		return Boolean.valueOf(this.signonly);
	}

	public boolean isInteger(String s, int radix) {
		if (s.isEmpty()) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			if ((i == 0) && (s.charAt(i) == '-') ? s.length() == 1 : Character.digit(s.charAt(i), radix) < 0) {
				return false;
			}
		}
		return true;
	}
}
