package com.gmail.ZiomuuSs.Nation;

import java.util.HashSet;

import org.bukkit.World;

import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

public class Estate {
  private static HashSet<Estate> estates = new HashSet<>(); //set of all estates
  private String name; //name of estate
  private Nation nation; //nation to which this estate belongs
  private ProtectedPolygonalRegion region; //region that is for this estate
  private String world; //world of region
  
  public Estate(String name, ProtectedPolygonalRegion region, String world) {
    this.name = name;
    this.region = region;
    this.world = world;
    estates.add(this);
  }
  
  //setters
  public void setNation(Nation nation) {
    this.nation = nation;
  }
  
  public void setRegion(String region, World world) {
    //todo
  }
  
  //checkers
  
  
  //getters
  
  @Override
  public String toString() {
    return name;
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
  
  public String getRegionWorld() {
    return world;
  }
}
