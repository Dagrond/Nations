package com.gmail.ZiomuuSs.Nation;

import java.util.HashSet;

/*
 * Group class
 * this stores all information about player's created groups
 * its permissions
 * prefix
 * 
 */
public class Group {
  private static HashSet<Group> groups = new HashSet<>(); //set of all groups
  public static enum NationPermission { //some kind of permissions 
    //TODO
    KICK, ATTACK, INFO, GIVE_RANK, BYPASS_BUILD, BUILD_CITY, KING; 
    /*
     * KICK - player can kick players out of nation
     * ATTACK - player can attack other nations buildings
     * INFO - player has view into specific nation's info
     * GIVE_RANK - player can give ranks to other players (that are in lower rank)
     * BYPASS_BUILD - player can build on every city of this nation (including member's plots)
     * BUILD_CITY - player can build in specific city
     * KING - this permissions contains all other permissions
     */
  }
  private Nation nation; //nation that groups belongs into
  private String name; //name of group
  private HashSet<City> buildCities = new HashSet<>(); //cities that this player is supposed to build
  private int priority = 0; //priority of this rank (higher numbers override lower)
  private String prefix = ""; //prefix of this group on chat
  private HashSet<NationPermission> permittedActions = new HashSet<>(); //all actions that are permitted for this group
  
  public Group(String name, Nation nation) {
    this.name = name;
    this.nation = nation;
    groups.add(this);
  }
  
  
  
  
  
  
  //getters
  public HashSet<NationPermission> getPermissions() {
    return permittedActions;
  }
  
  @Override
  public String toString() {
    return name;
  }
  
  public static HashSet<Group> getGroups() {
    return groups;
  }
}
