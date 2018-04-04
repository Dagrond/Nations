package com.github.Dagrond.Nation;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;

import com.github.Dagrond.Utils.ConfigLoader;
import com.github.Dagrond.Utils.DynmapUpdater;
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
  private World world; //world of region
  
  public Estate(String name, ProtectedPolygonalRegion region, World world, ConfigLoader config, Location spawn) {
    this.name = name;
    this.region = region;
    this.world = world;
    this.config = config;
    this.spawn = spawn;
    estates.add(this);
    config.saveEstate(this);
    new DynmapUpdater(config.getMain());
  }
  
  //setters
  public void setNation(Nation nation) {
    this.nation = nation;
    config.saveEstate(this);
    //new DynmapUpdater(config.getMain(), this);
  }
  
  public void del() {
    if (nation != null)
      nation.getEstates().remove(this);
    config.delSavedEstate(this);
    estates.remove(this);
  }
  
  public void setSpawn(Location spawn) {
    this.spawn = spawn;
    config.saveEstate(this);
  }
  
  //checkers
  
  
  //getters
  
  @Override
  public String toString() {
    return name;
  }
  
  public ArrayList<String> getDescription() {
    return lore;
  }
  
  public static Estate getEstateByName(String name) {
    for (Estate estate : estates) {
      if (estate.toString().equalsIgnoreCase(name))
        return estate;
    }
    return null;
  }
  
  public Nation getNation() {
    return nation;
  }
  
  public boolean isInWar() {
    return inWar;
  }
  
  public static HashSet<Estate> getEstates() {
    return estates;
  }
  
  public Location getSpawn() {
    return spawn;
  }
  
  public ProtectedPolygonalRegion getRegion() {
    return region;
  }
  
  public World getRegionWorld() {
    return world;
  }
  
  public static Location getNearestLocation(Location from, Nation nation) {
    if (nation == null) return null;
    if (from == null) {
      return nation.getCapital().getSpawn();
    }
    double distance = -1;
    Location nearest = null;
    for (Estate estate : estates) {
      if (estate.getNation() != null && estate.getNation().equals(nation) && !estate.isInWar()) {
        double newDist = from.distance(estate.getSpawn());
        if (newDist < distance || distance == -1) {
          distance = newDist;
          nearest = estate.getSpawn();
        }
      }
    }
    return nearest;
  }
}
