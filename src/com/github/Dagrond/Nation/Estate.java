package com.github.Dagrond.Nation;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;

import com.github.Dagrond.Utils.ConfigLoader;
import com.github.Dagrond.Utils.DynmapUpdater;
import com.github.Dagrond.Utils.msg;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.md_5.bungee.api.ChatColor;

public class Estate {
	private static HashSet<Estate> estates = new HashSet<>(); // set of all estates
	private ConfigLoader config;
	private String name; // name of estate
	private Nation nation; // nation to which this estate belongs
	private ArrayList<String> lore = new ArrayList<>(); // lore of estate
	private boolean inWar = false; // describes if there is currently battle for this estate
	private Location spawn; // spawn location of this estate
	private ProtectedPolygonalRegion region; // region that is for this estate

	public Estate(String name, Location spawn, ProtectedPolygonalRegion region, ConfigLoader config) {
		this.name = name;
		this.spawn = spawn;
		this.region = region;
		this.config = config;
		estates.add(this);
		save();
		new DynmapUpdater(config.getMain());
	}

	public void save() {
		config.saveEstate(this);
	}

	// setters
	public void setSpawn(Location location) {
		spawn = location;
		save();
	}

	public void setNation(Nation nation) {
		this.nation = nation;
		save();
	}

	public void toggleWar(boolean war) {
		inWar = war;
		save();
	}

	public void addLore(String lore) {
		this.lore.add(lore);
		save();
	}

	public void editLore(int index, String lore) {
		this.lore.add(index, lore);
		save();
	}

	public void delLore(int index) {
		lore.remove(index);
		save();
	}

	// Clear all relationships with current nation that owes this estate
	// including removing child regions & all other shit
	public void clearAffiliation() {
		// remove all child regions
		for (ProtectedRegion region : WGBukkit.getRegionManager(spawn.getWorld()).getApplicableRegions(region)
				.getRegions()) {
			if (!region.getId().startsWith("static_"))
				WGBukkit.getRegionManager(spawn.getWorld()).removeRegion(region.getId());
		}
		// clear owner & members of estate's region
		region.getOwners().removeAll();
		region.getMembers().removeAll();
		if (nation != null) {
			nation.delEstate(this);
			nation = null;
		}
	}

	public void delete() {
		clearAffiliation();
		estates.remove(this);
		config.delSavedEstate(this);
	}

	// booleans
	public boolean isInWar() {
		return inWar;
	}

	public boolean isOccupied() {
		return nation != null;
	}

	// getters
	@Override
	public String toString() {
		return name;
	}

	public ArrayList<String> getDescription() {
		return lore;
	}

	public Nation getNation() {
		return nation;
	}

	public Location getSpawn() {
		return spawn;
	}

	public World getWorld() {
		return spawn.getWorld();
	}

	public ProtectedPolygonalRegion getRegion() {
		return region;
	}

	// Static
	public static HashSet<Estate> getEstates() {
		return estates;
	}

	public static boolean isEstate(String estate) {
		return getEstate(estate) != null;
	}

	public static String getFullList() {
		String list = "";
		for (Estate estate : estates) {
			list += (estate.getNation() != null ? estate.getNation().getMCcolor() : ChatColor.GRAY) + estate.toString()
					+ ", ";
		}
		if (!list.equalsIgnoreCase(""))
			return list.substring(0, list.length() - 2);
		else
			return msg.get("none", false);
	}

	public static Estate getEstate(String estate) {
		for (Estate e : estates)
			if (e.toString().equalsIgnoreCase(estate))
				return e;
		return null;
	}
}
