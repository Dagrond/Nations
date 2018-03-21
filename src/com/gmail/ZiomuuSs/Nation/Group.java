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
  public static enum ActionType { //some kind of permissions 
    //TODO
  }
  private String name; //name of group
  private String prefix = ""; //prefix of this group
  private HashSet<ActionType> permittedActions = new HashSet<>(); //all actions that are permitted for this group
}
