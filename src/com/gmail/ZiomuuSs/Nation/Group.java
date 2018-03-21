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
  public static enum NationPermission { //some kind of permissions 
    //TODO
    KICK, ATTACK, INFO, GIVE_RANK, BYPASS_BUILD; 
    /*
     * kick - player can kick players out of nation
     * ATTACK - player can attack other nations buildings
     * INFO - player has view into specific nation's info
     * GIVE_RANK - player can give ranks to other players (that are in lower rank)
     * BYPASS_BUILD - player can build on every city of this nation (including member's plots)
     */
  }
  private String name; //name of group
  private int priority = 0; //priority of this rank (higher numbers override lower)
  private String prefix = ""; //prefix of this group
  private HashSet<NationPermission> permittedActions = new HashSet<>(); //all actions that are permitted for this group
  
  public Group(String name) {
    this.name = name;
  }
  
  public HashSet<NationPermission> getPermissions() {
    return permittedActions;
  }
}
