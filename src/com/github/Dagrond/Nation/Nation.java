package com.github.Dagrond.Nation;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.Dagrond.Utils.ConfigLoader;
import com.github.Dagrond.Utils.msg;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

/*
 *  Nation class
 *  this stores all information about certain nation
 *  
 */
public class Nation {
	private static HashSet<Nation> nations = new HashSet<>(); // list of all nations
	private ConfigLoader config;
	private String name; // name of nation
	private UUID king; // king of nation
	private HashSet<UUID> assistants = new HashSet<>(); // assistants of king of nation
	private Estate capital; // capital of this nation
	private HashSet<Estate> estates = new HashSet<>(); // estates of this nation (including capital)
	private int HEXcolor = 0xFF0000; // HEX color of nation on dynmap
	private ChatColor MCcolor = ChatColor.GRAY; // minecraft color of nation
	private HashSet<UUID> bannedPlayers = new HashSet<>(); // players who are enemies of this nation
	private HashSet<UUID> members = new HashSet<>(); // list of members of current nation

	public Nation(String name, ConfigLoader config) {
		this.name = name;
		this.config = config;
		nations.add(this);
		save();
	}

	public void save() {
		config.saveNation(this);
	}

	// setters
	public void setKing(UUID king) {
		this.king = king;
		save();
	}

	public void setHEXColor(int HEXcolor) {
		this.HEXcolor = HEXcolor;
		save();
	}

	public void setMCcolor(ChatColor MCcolor) {
		this.MCcolor = MCcolor;
		save();
	}

	public void setCapital(Estate capital) {
		this.capital = capital;
		save();
	}

	public void addEstate(Estate estate) {
		estates.add(estate);
		save();
	}

	public void delEstate(Estate estate) {
		estates.remove(estate);
		save();
	}

	public void addBannedPlayer(UUID banned) {
		bannedPlayers.add(banned);
		save();
	}

	public void delBannedPlayer(UUID banned) {
		bannedPlayers.remove(banned);
		save();
	}

	public void addMember(UUID member) {
		members.add(member);
		save();
	}

	public void delMember(UUID member) {
		members.remove(member);
		assistants.remove(member);
		if (king.equals(member))
			king = null;
		save();
	}

	public void addAssistant(UUID assistant) {
		assistants.add(assistant);
		save();
	}

	public void delAssisstant(UUID assistant) {
		assistants.remove(assistant);
		save();
	}

	// booleans
	public boolean isAssistant(UUID player) {
		return assistants.contains(player);
	}

	public boolean isBanned(UUID player) {
		return bannedPlayers.contains(player);
	}

	public boolean isMember(UUID player) {
		return members.contains(player);
	}

	public boolean isKing(UUID player) {
		return king.equals(player);
	}

	public boolean isCapital(Estate estate) {
		return capital.equals(estate);
	}

	public boolean estateBelongs(Estate estate) {
		return estates.contains(estate);
	}

	public boolean isInNationTerrority(Player player) {
		for (Estate e : estates) {
			if (player.getWorld().equals(e.getWorld())
					&& WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation())
							.getRegions().contains((ProtectedRegion) e.getRegion())) {
				return true;
			}
		}
		return false;
	}

	// getters
	@Override
	public String toString() {
		return name;
	}

	public String getDisplayName() {
		return MCcolor + name;
	}

	public int getEstateAmount() {
		return estates.size();
	}

	public ChatColor getMCcolor() {
		return MCcolor;
	}

	public int getHEXcolor() {
		return HEXcolor;
	}

	public UUID getKing() {
		return king;
	}

	public int getMemberAmount() {
		return members.size();
	}

	public int getAssistantAmount() {
		return assistants.size();
	}

	public int getBannedAmount() {
		return bannedPlayers.size();
	}

	public Estate getCapital() {
		return capital;
	}

	public HashSet<Estate> getEstates() {
		return estates;
	}

	public HashSet<UUID> getBannedPlayers() {
		return bannedPlayers;
	}

	public HashSet<UUID> getMembers() {
		return members;
	}

	public HashSet<UUID> getAssistants() {
		return assistants;
	}

	public String getMemberList() {
		String list = "";
		for (UUID member : members) {
			list += Bukkit.getOfflinePlayer(member).getName() + ", ";
		}
		if (!list.equalsIgnoreCase(""))
			return list.substring(0, list.length() - 2);
		else
			return msg.get("none", false);
	}

	public String getBannedList() {
		String list = "";
		for (UUID banned : bannedPlayers) {
			list += Bukkit.getOfflinePlayer(banned).getName() + ", ";
		}
		if (!list.equalsIgnoreCase(""))
			return list.substring(0, list.length() - 2);
		else
			return msg.get("none", false);
	}

	public String getAssistantsList() {
		String list = "";
		for (UUID assistant : assistants) {
			list += Bukkit.getOfflinePlayer(assistant).getName() + ", ";
		}
		if (!list.equalsIgnoreCase(""))
			return list.substring(0, list.length() - 2);
		else
			return msg.get("none", false);
	}

	public String getEstatesList() {
		String list = "";
		for (Estate estate : estates) {
			list += estate.toString() + ", ";
		}
		if (!list.equalsIgnoreCase(""))
			return list.substring(0, list.length() - 2);
		else
			return msg.get("none", false);
	}

	// Static methods
	public static boolean isNation(String nation) {
		for (Nation n : nations) {
			if (n.toString().equalsIgnoreCase(nation))
				return true;
		}
		return false;
	}

	public static Nation getNationByString(String nation) {
		for (Nation n : nations) {
			if (n.toString().equalsIgnoreCase(nation))
				return n;
		}
		return null;
	}

	public static int getNationAmount() {
		return nations.size();
	}

	public static boolean isInOwn(Player player) {

		return false;
	}

	public static Nation getPlayerNation(Player player) {
		for (Nation n : nations) {
			if (n.isMember(player.getUniqueId())) {
				return n;
			}
		}
		return null;
	}

	public static String getNationsList() {
		String list = "";
		for (Nation nation : nations) {
			list += nation.getMCcolor() + nation.toString() + ", ";
		}
		if (!list.equalsIgnoreCase(""))
			return list.substring(0, list.length() - 2);
		else
			return msg.get("none", false);
	}

}
