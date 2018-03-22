package com.gmail.ZiomuuSs.Nation;

import java.util.HashSet;

import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

public class Estate {
  private static HashSet<Estate> estates = new HashSet<>(); //set of all estates
  private String name; //name of estate
  private ProtectedPolygonalRegion region; //region that is for this estate
  private HashSet<City> cities = new HashSet<>(); //set of cities in this estate
  
  public Estate(String name, ProtectedPolygonalRegion region) {
    this.name = name;
    this.region = region;
    estates.add(this);
  }
  
  //setters
  
  
  //checkers
  
  
  //getters
  
  @Override
  public String toString() {
    return name;
  }
  
  public static HashSet<Estate> getEstates() {
    return estates;
  }
  
  public HashSet<City> getCities() {
    return cities;
  }
  
  public ProtectedPolygonalRegion getRegion() {
    return region;
  }
}
