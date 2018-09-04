package com.github.Dagrond.Nation;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;

import com.github.Dagrond.Utils.ConfigLoader;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

public class Estate {
  private static HashSet<Estate> estates = new HashSet<>(); //set of all estates
  private ConfigLoader config;
  private String name; //name of estate
  private Nation nation; //nation to which this estate belongs
  private ArrayList<String> lore = new ArrayList<>(); //lore of estate
  private boolean inWar = false; //describes if there is currently battle for this estate
  private Location spawn; //spawn location of this estate
  private ProtectedPolygonalRegion region; //region that is for this estate
  
  public Estate(String name, Location spawn, ProtectedPolygonalRegion region, ConfigLoader config) {
    this.name = name;
    this.spawn = spawn;
    this.region = region;
    this.config = config;
    estates.add(this);
    save();
  }
  
  public void save() {
    config.saveEstate(this);
  }
  //setters
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
  //booleans
  public boolean isInWar() {
    return inWar;
  }
  public boolean isOccupied() {
    return nation!=null;
  }
  //getters
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
  //Static
  public static HashSet<Estate> getEstates() {
    return estates;
  }
  public static Estate getEstate(String estate) {
    for (Estate e : estates)
      if (e.toString().equalsIgnoreCase(estate))
        return e;
    return null;
  }
}
