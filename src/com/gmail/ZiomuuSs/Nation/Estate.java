package com.gmail.ZiomuuSs.Nation;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.World;

import com.gmail.ZiomuuSs.Utils.ConfigLoader;
import com.gmail.ZiomuuSs.Utils.DynmapUpdater;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

public class Estate {
  private static HashSet<Estate> estates = new HashSet<>(); //set of all estates
  private ConfigLoader config;
  private String name; //name of estate
  private Nation nation; //nation to which this estate belongs
  private ArrayList<String> lore = new ArrayList<>(); //lore of estate
  private ProtectedPolygonalRegion region; //region that is for this estate
  private World world; //world of region
  
  public Estate(String name, ProtectedPolygonalRegion region, World world, ConfigLoader config) {
    this.name = name;
    this.region = region;
    this.world = world;
    this.config = config;
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
  
  public static HashSet<Estate> getEstates() {
    return estates;
  }
  
  public ProtectedPolygonalRegion getRegion() {
    return region;
  }
  
  public World getRegionWorld() {
    return world;
  }
}
