package me.lagswitcher.Handlers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import me.lagswitcher.Main.Main;

public class NumberUtil {
	Main plugin;
	static DecimalFormat twoDPlaces = new DecimalFormat("#,###.##");
	static DecimalFormat currencyFormat = new DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(Locale.US));

	public NumberUtil(Main instance) {
		this.plugin = instance;
	}

	public static boolean isInt(String sInt) {
		try {
			Integer.parseInt(sInt);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
