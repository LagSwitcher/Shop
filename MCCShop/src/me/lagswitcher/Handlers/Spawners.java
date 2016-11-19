package me.lagswitcher.Handlers;

import java.util.HashMap;
import java.util.Map;

public class Spawners {
	private static final Map<Integer, String> ALIASNAMES = new HashMap<Integer, String>();

	static {
		ALIASNAMES.put(Integer.valueOf(50), "Creeper");
		ALIASNAMES.put(Integer.valueOf(51), "Skeleton");
		ALIASNAMES.put(Integer.valueOf(52), "Spider");
		ALIASNAMES.put(Integer.valueOf(53), "Giant Zombie");
		ALIASNAMES.put(Integer.valueOf(54), "Zombie");
		ALIASNAMES.put(Integer.valueOf(55), "Slime");
		ALIASNAMES.put(Integer.valueOf(56), "Ghast");
		ALIASNAMES.put(Integer.valueOf(57), "Zombie Pigman");
		ALIASNAMES.put(Integer.valueOf(58), "Enderman");
		ALIASNAMES.put(Integer.valueOf(59), "Cave Spider");
		ALIASNAMES.put(Integer.valueOf(60), "Silverfish");
		ALIASNAMES.put(Integer.valueOf(61), "Blaze");
		ALIASNAMES.put(Integer.valueOf(62), "Magma Cube");
		ALIASNAMES.put(Integer.valueOf(63), "Ender Dragon");
		ALIASNAMES.put(Integer.valueOf(64), "Wither");
		ALIASNAMES.put(Integer.valueOf(66), "Witch");
		ALIASNAMES.put(Integer.valueOf(67), "Endermite");
		ALIASNAMES.put(Integer.valueOf(68), "Guardian");
		ALIASNAMES.put(Integer.valueOf(69), "Shulker");
		ALIASNAMES.put(Integer.valueOf(101), "Killer Rabbit");
		ALIASNAMES.put(Integer.valueOf(65), "Bat");
		ALIASNAMES.put(Integer.valueOf(90), "Pig");
		ALIASNAMES.put(Integer.valueOf(91), "Sheep");
		ALIASNAMES.put(Integer.valueOf(92), "Cow");
		ALIASNAMES.put(Integer.valueOf(93), "Chicken");
		ALIASNAMES.put(Integer.valueOf(94), "Squid");
		ALIASNAMES.put(Integer.valueOf(95), "Wolf");
		ALIASNAMES.put(Integer.valueOf(96), "Mooshroom");
		ALIASNAMES.put(Integer.valueOf(97), "Snow Golem");
		ALIASNAMES.put(Integer.valueOf(98), "Ocelot");
		ALIASNAMES.put(Integer.valueOf(99), "Iron Golem");
		ALIASNAMES.put(Integer.valueOf(100), "Horse");
		ALIASNAMES.put(Integer.valueOf(101), "Rabbit");
		ALIASNAMES.put(Integer.valueOf(120), "Villager");
	}

	public static String getMobName(Integer name) {
		String mobName = null;
		mobName = (String) ALIASNAMES.get(name);
		return mobName;
	}
}
