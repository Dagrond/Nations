package com.gmail.ZiomuuSs.Nation;

import java.util.HashSet;

import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

public class City {
  private static HashSet<City> cities = new HashSet<>(); //set of all cities
  private String name; //name of city
  private ProtectedPolygonalRegion region; //region of that city
  private Estate estate; //estate which contains that city
  
  
  public City(String name, ProtectedPolygonalRegion region, Estate estate) {
    this.name = name;
    this.region = region;
    this.estate = estate;
    cities.add(this);
  }
  
  //setters
  
  
  
  //checkers
  
  
  
  //getters
  @Override
  public String toString() {
    return name;
  }
  
  public static HashSet<City> getCities() {
    return cities;
  }
}
