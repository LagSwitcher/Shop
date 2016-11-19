package me.lagswitcher.Listeners;

import de.dustplanet.util.SilkUtil;
import me.lagswitcher.Main.Main;

import java.util.ArrayList;
import java.util.Arrays;

import net.milkbowl.vault.economy.EconomyResponse;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener {
	Main plugin;
	protected ArrayList<String> menuOpen = new ArrayList<String>();
	protected ArrayList<String> shopOpen = new ArrayList<String>();
	protected String title;
	protected boolean one;
	protected boolean close = false;
	protected String properName;
	private CraftPlayer player;

	public PlayerListener(Main main) {
		this.plugin = main;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String command = event.getMessage();
		if (command.equalsIgnoreCase("/" + this.plugin.utils.getCommand())) {
			if ((player.hasPermission("mccshop.use")) || (player.isOp())) {
				if (this.plugin.getConfig().getBoolean("sign-only")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&',
							this.plugin.getConfig().getString("sign-only-message")));
					event.setCancelled(true);
				} else {
					this.plugin.menu.loadMenu(player);
					event.setCancelled(true);
					if (this.shopOpen.contains(player.getName())) {
						this.shopOpen.remove(player.getName());
					}
					if (!this.menuOpen.contains(player.getName())) {
						this.menuOpen.add(player.getName());
					}
				}
			} else {
				player.sendMessage(String.valueOf(this.plugin.utils.getPrefix()) + " " + ChatColor
						.translateAlternateColorCodes('&', this.plugin.getConfig().getString("no-permission")));
			}
		}
		if (command.equalsIgnoreCase("/" + this.plugin.utils.getCommand() + " reload")) {
			if (player.isOp()) {
				if (this.plugin.cache.flushData()) {
					if (this.plugin.utils.getVerbose().booleanValue()) {
						System.out.println("Shop Data exists! Flushing data!");
					}
				} else if (this.plugin.utils.getVerbose().booleanValue()) {
					System.out.println("No Shop data existed! No Shops have been opened and cached yet!");
				}
				this.plugin.reloadConfig();
				this.plugin.reloadCustomConfig();
				event.setCancelled(true);
				if (this.plugin.utils.getVerbose().booleanValue()) {
					System.out.println("Reload Complete!");
				}
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						String.valueOf(this.plugin.utils.getPrefix()) + " &aReloaded!"));
			} else {
				player.sendMessage("No Permission! You must be OP!");
			}
		}
		if (command.equalsIgnoreCase("/" + this.plugin.utils.getSellCommand())) {
			if (this.plugin.utils.getVerbose().booleanValue()) {
				System.out.println("ELSE ELSE ELSE");
			}
			if ((player.isOp()) || (player.hasPermission("mccshop.use"))) {
				if ((player.hasPermission("mccshop.sell")) || (player.isOp())) {
					this.plugin.sell.loadSell(player);
					event.setCancelled(true);
				} else {
					event.setCancelled(true);
					player.sendMessage(String.valueOf(this.plugin.utils.getPrefix()) + " " + ChatColor
							.translateAlternateColorCodes('&', this.plugin.getConfig().getString("no-permission")));
				}
			} else {
				event.setCancelled(true);
				player.sendMessage(String.valueOf(this.plugin.utils.getPrefix()) + " " + ChatColor
						.translateAlternateColorCodes('&', this.plugin.getConfig().getString("no-permission")));
			}
		}
		if (command.equalsIgnoreCase("/" + this.plugin.utils.getCommand() + " cache flush")) {
			if (player.isOp()) {
				this.plugin.cache.flushData();
				event.setCancelled(true);
			} else {
				event.setCancelled(true);
				player.sendMessage(String.valueOf(this.plugin.utils.getPrefix()) + " " + ChatColor
						.translateAlternateColorCodes('&', this.plugin.getConfig().getString("no-permission")));
			}
		}
	}

	@SuppressWarnings({ "unused", "deprecation" })
	@EventHandler(priority = EventPriority.HIGH)
	public void onShopClick(InventoryClickEvent e) {
		if ((e.getWhoClicked() instanceof Player)) {
			Player p = (Player) e.getWhoClicked();
			if (e.getInventory() != null) {
				if (e.getInventory().getTitle().equalsIgnoreCase(this.plugin.utils.getSellTitle())) {
					e.setCancelled(false);
				} else if (!this.shopOpen.contains(p.getName())) {
					if (this.menuOpen.contains(p.getName())) {
						if (e.getClick().isKeyboardClick()) {
							e.setCancelled(true);
							p.closeInventory();
							if ((this.menuOpen.contains(p.getName())) || (this.shopOpen.contains(p.getName()))) {
								this.menuOpen.remove(p.getName());
								this.shopOpen.remove(p.getName());
							}
						}
						if (e.getCurrentItem().getType() == Material.AIR) {
							e.setCancelled(true);

							this.plugin.closeInventory(p);
							if ((this.menuOpen.contains(p.getName())) || (this.shopOpen.contains(p.getName()))) {
								this.menuOpen.remove(p.getName());
								this.shopOpen.remove(p.getName());
							}
						}
						if (e.isShiftClick()) {
							e.setCancelled(true);
							p.closeInventory();
							if ((this.menuOpen.contains(p.getName())) || (this.shopOpen.contains(p.getName()))) {
								this.menuOpen.remove(p.getName());
								this.shopOpen.remove(p.getName());
							}
						}
						if (e.getInventory().getType() == InventoryType.PLAYER) {
							e.setCancelled(true);
							this.close = false;
							if ((this.menuOpen.contains(p.getName())) || (this.shopOpen.contains(p.getName()))) {
								this.menuOpen.remove(p.getName());
								this.shopOpen.remove(p.getName());
							}
						}
						this.close = true;
						if (this.plugin.utils.getVerbose().booleanValue()) {
							System.out.println("MenuOpen passed. Player removed");
						}
						if (e.getInventory().getTitle().contains(this.plugin.utils.getMenuName())) {
							if (this.plugin.utils.getVerbose().booleanValue()) {
								System.out.println("Title contains menu name");
							}
							if (e.getSlotType() == InventoryType.SlotType.CONTAINER) {
								if (e.getInventory().getType() == e.getView().getType()) {
									if ((e.isLeftClick()) || (e.isShiftClick())
											|| ((e.isRightClick()) && (!e.getClick().isKeyboardClick()))) {
										int[] arrayOfInt1;
										int j = (arrayOfInt1 = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
												14, 15, 16, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 28 }).length;
										for (int i = 0; i < j; i++) {
											int slot = arrayOfInt1[i];
											if (e.getRawSlot() == slot - 1) {
												if (this.plugin.getConfig()
														.getString(String.valueOf(slot) + ".Enabled") == "true") {
													if ((!p.hasPermission("mccshop.slot." + slot)) && (!p.isOp())) {
														break;
													}
													e.setCancelled(true);
													String shop = this.plugin.getConfig()
															.getString(String.valueOf(slot) + ".Shop");
													this.title = "";
													this.title = this.plugin.getConfig()
															.getString(String.valueOf(slot) + ".Shop");
													String shopn = String.valueOf(shop) + ".";
													this.plugin.shop.setShopName(shopn);
													if (this.menuOpen.contains(p.getName())) {
														this.menuOpen.remove(p.getName());
													}
													this.shopOpen.add(p.getName());
													this.plugin.delayShop(p);

													break;
												}
												this.plugin.closeInventory(p);

												break;
											}
										}
									} else {
										e.getClick().isKeyboardClick();
									}
								} else {
									this.plugin.closeInventory(p);
								}
							} else {
								this.plugin.closeInventory(p);
							}
						}
					}
				} else {
					this.properName = this.plugin.shop.getShopName().replace(".", "");
					if ((e.getInventory().getTitle().contains(this.properName))
							&& (!e.getInventory().getTitle().contains(this.plugin.utils.getMenuName()))) {
						if (this.plugin.utils.getVerbose().booleanValue()) {
							System.out.println("Shop name is shop name and not menu name");
							System.out.println("Menu name: " + this.plugin.utils.getMenuName() + " Compared to: "
									+ e.getInventory().getTitle());
						}
						if (e.getSlot() != 64537) {
							ItemStack item = e.getCurrentItem();
							if ((item.hasItemMeta()) && (item.getItemMeta().hasDisplayName())) {
								if (!item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes(
										'&', this.plugin.getConfig().getString("back")))) {
									if (e.getInventory().getItem(e.getSlot()) != null) {
										if (item.getItemMeta().hasLore()) {
											e.setCancelled(true);
											item.getItemMeta().getLore().toString()
													.contains(ChatColor.translateAlternateColorCodes('&',
															this.plugin.getConfig().getString("cost")));
											int price = Integer.MAX_VALUE;
											String lorestring2 = ChatColor
													.stripColor(item.getItemMeta().getLore().toString());

											Object items = Arrays.asList(lorestring2.split("\\s*,\\s*"));
											String lorestring = ChatColor.stripColor(item.getItemMeta().getLore()
													.toString().replace("[", "").replace("]", "").replace(",", "")
													.replace("To sell, click the item in your inv.", "")
													.replace("Must be the same quantity!", "")
													.replace("Shift+Click to buy 1 item", ""));
											lorestring = StringUtils.substringBefore(lorestring, ".");
											if (this.plugin.utils.getVerbose().booleanValue()) {
												System.out.println("LORE LORE LORE LORE!!!!!!!! ");
											}
											if ((e.isLeftClick()) && (!e.isShiftClick())
													&& (!e.getClick().isKeyboardClick())) {
												if (this.one) {
													e.setCancelled(true);
												} else if (this.plugin.econ.getBalance(p.getName()) >= price) {
													EconomyResponse r = this.plugin.econ.withdrawPlayer(p.getName(),
															price);
													if (r.transactionSuccess()) {
														ItemStack dupeitem = item.clone();
														if (dupeitem.getType() == Material.MOB_SPAWNER) {
															if (this.plugin.getServer().getPluginManager()
																	.getPlugin("SilkSpawners") == null) {
																if (this.plugin.utils.getVerbose().booleanValue()) {
																	System.out.println(
																			"ERROR: You are trying to purchase a MobSpawner without SilkSpawners installed!");
																}
															} else {
																if (this.plugin.utils.getVerbose().booleanValue()) {
																	System.out.println("Item IS a MOB_SPAWNER");
																}
																SilkUtil.hookIntoSilkSpanwers();

																stripMeta(dupeitem,
																		Integer.valueOf(dupeitem.getAmount()));
															}
														} else {
															if (this.plugin.utils.getVerbose().booleanValue()) {
																System.out.println("No Mob Spawner here...");
															}
															ItemStack dupeitem2 = stripMeta(dupeitem,
																	Integer.valueOf(dupeitem.getAmount()));
															p.getInventory().addItem(new ItemStack[] { dupeitem2 });
														}
														p.sendMessage(String
																.valueOf(ChatColor.translateAlternateColorCodes('&',
																		this.plugin.utils.getPrefix()))
																+ " "
																+ ChatColor.translateAlternateColorCodes('&',
																		this.plugin.getConfig().getString("purchased"))
																+ item.getAmount() + " "
																+ item.getType().toString().toLowerCase() + "!");
														p.sendMessage(String
																.valueOf(ChatColor.translateAlternateColorCodes('&',
																		this.plugin.utils.getPrefix()))
																+ " " + "$" + price + " "
																+ ChatColor.translateAlternateColorCodes('&',
																		this.plugin.getConfig().getString("taken")));
														PacketPlayOutTitle buytitle = new PacketPlayOutTitle(
																EnumTitleAction.TITLE,
																ChatSerializer
																		.a("{\"text\":\"You bought a item\",\"color\":\"gold\",\"bold\":true}"),
																20, 40, 30);

														((CraftPlayer) player).getHandle().playerConnection
																.sendPacket(buytitle);
													}
												} else {
													double dif = price - this.plugin.econ.getBalance(p.getName());
													p.sendMessage(String
															.valueOf(ChatColor.translateAlternateColorCodes('&',
																	this.plugin.utils.getPrefix()))
															+ " "
															+ ChatColor.translateAlternateColorCodes('&',
																	this.plugin.getConfig().getString("not-enough-pre"))
															+ dif
															+ ChatColor.translateAlternateColorCodes('&', this.plugin
																	.getConfig().getString("not-enough-post")));
													p.setItemOnCursor(new ItemStack(Material.AIR));
												}
											}
										} else {
											e.setCancelled(true);
										}
									}
								} else {
									e.setCancelled(true);
									this.plugin.closeInventory(p);
									if ((this.menuOpen.contains(p.getName()))
											|| (this.shopOpen.contains(p.getName()))) {
										this.shopOpen.remove(p.getName());
										this.menuOpen.add(p.getName());
									}
									this.plugin.delayMenu(p);
								}
							}
						}
					} else {
						if ((!e.getInventory().getTitle().contains(this.properName))
								&& (!e.getInventory().getTitle().contains(this.plugin.utils.getMenuName()))
								&& (this.shopOpen.contains(p.getName()))) {
							this.shopOpen.remove(p.getName());
						}
						e.setCancelled(true);
					}
					if (e.getClick().isKeyboardClick()) {
						e.setCancelled(true);
						p.closeInventory();
						if ((this.menuOpen.contains(p.getName())) || (this.shopOpen.contains(p.getName()))) {
							this.menuOpen.remove(p.getName());
							this.shopOpen.remove(p.getName());
						}
					}
					if (e.getCurrentItem().getType() == Material.AIR) {
						e.setCancelled(true);
						this.plugin.closeInventory(p);
						if ((this.menuOpen.contains(p.getName())) || (this.shopOpen.contains(p.getName()))) {
							this.menuOpen.remove(p.getName());
							this.shopOpen.remove(p.getName());
						}
					}
					if (e.isShiftClick()) {
						e.setCancelled(true);
						p.closeInventory();
						if ((this.menuOpen.contains(p.getName())) || (this.shopOpen.contains(p.getName()))) {
							this.menuOpen.remove(p.getName());
							this.shopOpen.remove(p.getName());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (e.getInventory().getTitle().equalsIgnoreCase(this.plugin.utils.getSellTitle())) {
			this.plugin.sell.trySell(p, this.plugin.sell.getSellInv());
		} else if ((e.getInventory().getTitle().equalsIgnoreCase(this.properName))
				&& (this.shopOpen.contains(p.getName()))) {
			this.shopOpen.remove(p.getName());
		} else if ((e.getInventory().getTitle().equalsIgnoreCase(this.plugin.utils.getMenuName()))
				&& (this.menuOpen.contains(p.getName()))) {
			this.menuOpen.remove(p.getName());
		}
	}

	public ItemStack stripMeta(ItemStack item, Integer amount) {
		ItemMeta itm = item.getItemMeta();
		itm.setLore(null);
		itm.setDisplayName(null);
		item.setItemMeta(itm);
		item.setAmount(amount.intValue());
		return item;
	}

	@EventHandler
	public void eventSignChanged(SignChangeEvent event) {
		String title = event.getLine(0);
		Player p = event.getPlayer();
		if (title.equalsIgnoreCase(
				ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("sign-title")))) {
			if ((p.hasPermission("mccshop.sign.place")) || (p.isOp())) {
				event.setCancelled(false);
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						this.plugin.getConfig().getString("no-permission")));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		if ((block != null) && (block.getState() != null) && ((block.getState() instanceof Sign))) {
			Sign sign = (Sign) block.getState();
			String line1 = ChatColor.translateAlternateColorCodes('&', sign.getLine(0));
			if (this.plugin.utils.getVerbose().booleanValue()) {
				System.out.println("Player Clicked sign with line1: " + line1 + " compared to "
						+ ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("sign-title")));
			}
			if (line1.equalsIgnoreCase(
					ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("sign-title")))) {
				if (((player.hasPermission("mccshop.use")) && (player.hasPermission("mccshop.sign.use")))
						|| (player.isOp())) {
					this.plugin.menu.loadMenu(player);
					e.setCancelled(true);
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&',
							this.plugin.getConfig().getString("no-permission")));
					e.setCancelled(true);
				}
			}
		}
	}
}
