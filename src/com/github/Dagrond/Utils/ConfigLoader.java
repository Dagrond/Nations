package com.github.Dagrond.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.Dagrond.NationPlugin;
import com.github.Dagrond.Nation.Estate;
import com.github.Dagrond.Nation.Nation;
import com.github.Dagrond.Nation.NationMember;
import com.github.Dagrond.Nation.NationMember.NationPerm;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

public class ConfigLoader {
	private ConfigAccessor msgAccessor;
	private WorldGuardPlugin worldGuard;
	private NationPlugin plugin;
	private boolean isLoading = false; // describes if plugin is loading files to block saving during first loading
										// (well, thats complicated)
	// options variables

	public ConfigLoader(NationPlugin plugin, WorldGuardPlugin worldGuard) {
		this.plugin = plugin;
		this.worldGuard = worldGuard;
		msgAccessor = new ConfigAccessor(plugin, "Messages.yml");
		msgAccessor.saveDefaultConfig();
		Msg.set(msgAccessor.getConfig());
		loadAll();
	}

	// loading all files
	private void loadAll() {
		isLoading = true;
		int loadedNations = 0;
		int loadedEstates = 0;
		int loadedMembers = 0;

		// loading estates
		if (new File(plugin.getDataFolder() + String.valueOf(File.separatorChar) + "Estates").exists()) {
			for (File file : new File(plugin.getDataFolder() + String.valueOf(File.separatorChar) + "Estates").listFiles()) {
				FileConfiguration cs = YamlConfiguration.loadConfiguration(file);
				String name = file.getName();
				name = name.substring(0, name.length() - 4); // remove the .yml
				ProtectedPolygonalRegion rg;
				World world = Bukkit.getWorld(cs.getString("spawn.world"));
				if (world != null) {
					rg = (ProtectedPolygonalRegion) worldGuard.getRegionManager(world)
							.getRegion(cs.getString("region"));
					if (rg != null) {
						Estate e = new Estate(name,
								new Location(world, cs.getDouble("spawn.x"), cs.getDouble("spawn.y"),
										cs.getDouble("spawn.z"), (float) cs.getDouble("spawn.yaw"),
										(float) cs.getDouble("spawn.pitch")),
								rg, this);
						if (cs.isList("description"))
							for (String lore : cs.getStringList("description"))
								e.addLore(lore);
						++loadedEstates;
					} else {
						Bukkit.getLogger().info(Msg.get("error_console_no_region", true, name, cs.getString("region"),
								cs.getString("spawn.world")));
					}
				} else {
					Bukkit.getLogger().info(Msg.get("error_console_no_world", true, name, cs.getString("spawn.world")));
				}
			}
		}
		// loading nations
		if (new File(plugin.getDataFolder() + String.valueOf(File.separatorChar) + "Nations").exists()) {
			for (File file : new File(plugin.getDataFolder() + String.valueOf(File.separatorChar) + "Nations").listFiles()) {
				FileConfiguration cs = YamlConfiguration.loadConfiguration(file);
				String name = file.getName();
				name = name.substring(0, name.length() - 4); // remove the .yml
				Nation nation = new Nation(name, this);
				nation.setHEXColor(Integer.parseInt(cs.getString("hexcolor").substring(1), 16));
				nation.setMCcolor(ChatColor.valueOf(cs.getString("mcolor")));
				if (cs.isList("bannedplayers")) {
					for (String uuid : cs.getStringList("bannedplayers")) {
						nation.addBannedPlayer(UUID.fromString(uuid));
					}
				}
				if (cs.isList("estates")) {
					String capital = "";
					if (cs.isString("capital")) {
						capital = cs.getString("capital");
					} else {
						Bukkit.getLogger().info(Msg.get("error_console_nation_without_capital", true, name));
					}
					for (String estate : cs.getStringList("estates")) {
						Estate e = Estate.getEstate(estate);
						if (e != null) {
							nation.addEstate(e);
							e.setNation(nation);
							if (capital.equals(estate))
								nation.setCapital(e);
						} else {
							Bukkit.getLogger().info(Msg.get("error_console_estate_not_loaded", true, estate, name));
						}
					}
				}
				if (cs.isString("king"))
					nation.setKing(UUID.fromString(cs.getString("king")));
				else
					Bukkit.getLogger().info(Msg.get("error_console_nation_without_king", true, name));
				if (cs.isList("assistants"))
					for (String assistant : cs.getStringList("assistants"))
						nation.addAssistant(UUID.fromString(assistant));
				if (cs.isList("members"))
					for (String member : cs.getStringList("members"))
						nation.addMember(UUID.fromString(member));
				++loadedNations;
				// loading nation members
				for (Player player : Bukkit.getOnlinePlayers()) {
					NationMember.addOnlineMember(loadMember(player.getUniqueId()));
				}
			}
		}
		isLoading = false;
		new DynmapUpdater(plugin);
		Bukkit.getLogger().info(Msg.get("console_loaded", true, Integer.toString(loadedNations),
				Integer.toString(loadedEstates), Integer.toString(loadedMembers)));
	}

	public boolean isSavedMember(UUID uuid) {
	  return new File(plugin.getDataFolder() + String.valueOf(File.separatorChar) + "Players", uuid.toString() + ".yml").isFile();
	}
	
	public NationMember loadMember(UUID uuid) {
	  File memberfile = new File(plugin.getDataFolder() + String.valueOf(File.separatorChar) + "Players", uuid.toString() + ".yml");
		NationMember member = null;
		if (memberfile.isFile()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(memberfile);
			member = new NationMember(uuid);
			if (Bukkit.getOfflinePlayer(uuid).isOnline())
			  NationMember.addOnlineMember(member);
			if (cfg.isString("nation"))
				member.setNation(Nation.getNationByString(cfg.getString("nation")));
			if (cfg.isInt("priority"))
				member.setPriority(cfg.getInt("priority"));
			if (cfg.isList("permissions")) {
				for (String perm : cfg.getStringList("permissions")) {
					member.addPermission(NationPerm.valueOf(perm));
				}
			}
		}
		return member;
	}

	public void saveNation(Nation nation) {
		if (!isLoading) {
			new File(plugin.getDataFolder() + String.valueOf(File.separatorChar) + "Nations", nation.toString() + ".yml").delete(); // delete old data
			ConfigAccessor ca = new ConfigAccessor(plugin, nation.toString() + ".yml", "Nations");
			ConfigurationSection cs = ca.getConfig();
			if (nation.getKing() != null)
				cs.set("king", nation.getKing().toString());
			if (nation.getCapital() != null)
				cs.set("capital", nation.getCapital().toString());
			cs.set("hexcolor", "#" + Integer.toHexString(nation.getHEXcolor()));
			cs.set("mcolor", nation.getMCcolor().toString());
			if (!Estate.getEstates().isEmpty()) {
				ArrayList<String> estates = new ArrayList<>();
				for (Estate estate : nation.getEstates()) {
					estates.add(estate.toString());
				}
				cs.set("estates", estates);
			}
			if (!nation.getBannedPlayers().isEmpty()) {
				ArrayList<String> banned = new ArrayList<>();
				for (UUID uuid : nation.getBannedPlayers()) {
					banned.add(uuid.toString());
				}
				cs.set("bannedplayers", banned);
			}
			if (!nation.getMembers().isEmpty()) {
				ArrayList<String> members = new ArrayList<>();
				for (UUID uuid : nation.getMembers()) {
					members.add(uuid.toString());
				}
				cs.set("members", members);
			}
			if (!nation.getAssistants().isEmpty()) {
				ArrayList<String> assistants = new ArrayList<>();
				for (UUID uuid : nation.getAssistants()) {
					assistants.add(uuid.toString());
				}
				cs.set("members", assistants);
			}
			ca.saveConfig();
		}
	}

	public void saveNationMember(NationMember player) {
		if (!isLoading) {
		  UUID uuid = player.getUUID();
			new File(plugin.getDataFolder() + String.valueOf(File.separatorChar) + "Players", uuid + ".yml").delete();
			ConfigAccessor ca = new ConfigAccessor(plugin, uuid + ".yml", "Players");
			ConfigurationSection cs = ca.getConfig();
			if (player.getNation() != null)
				cs.set("nation", player.getNation().toString());
			if (!player.getPermissions().isEmpty()) {
				ArrayList<String> permissions = new ArrayList<>();
				for (NationPerm permission : player.getPermissions()) {
					permissions.add(permission.toString());
				}
				cs.set("permissions", permissions);
			}
			if (player.getPriority() != 0)
				cs.set("priority", player.getPriority());
			ca.saveConfig();
		}
	}
//done by now /\

	public void saveEstate(Estate estate) {
		if (!isLoading) {
			new File(plugin.getDataFolder() + String.valueOf(File.separatorChar) + "Estates", estate.toString() + ".yml").delete();
			ConfigAccessor ca = new ConfigAccessor(plugin, estate.toString() + ".yml", "Estates");
			ConfigurationSection cs = ca.getConfig();
			cs.set("region", estate.getRegion().getId());
			cs.set("spawn.world", estate.getSpawn().getWorld().getName());
			cs.set("spawn.x", estate.getSpawn().getX());
			cs.set("spawn.y", estate.getSpawn().getY());
			cs.set("spawn.z", estate.getSpawn().getZ());
			cs.set("spawn.yaw", estate.getSpawn().getYaw());
			cs.set("spawn.pitch", estate.getSpawn().getPitch());
			if (!estate.getDescription().isEmpty())
				cs.set("description", estate.getDescription());
			if (estate.getNation() != null)
				cs.set("nation", estate.getNation().toString());
			ca.saveConfig();
		}
	}

	public void delSavedEstate(Estate estate) {
		new File(plugin.getDataFolder() + String.valueOf(File.separatorChar) + "Estates", estate.toString() + ".yml")
				.delete();
	}

	public void delSavedNation(Nation nation) {
		new File(plugin.getDataFolder() + String.valueOf(File.separatorChar) + "Nations", nation.toString() + ".yml")
				.delete();
	}

	public WorldGuardPlugin getWorldGuard() {
		return worldGuard;
	}

	public NationPlugin getMain() {
		return plugin;
	}
}
