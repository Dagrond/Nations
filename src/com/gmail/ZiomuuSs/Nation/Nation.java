package com.gmail.ZiomuuSs.Nation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

/*
 *  Nation class
 *  this stores all information about certain nation
 *  
 */
public class Nation {
  private String name; //name of nation
  private HashSet<ProtectedPolygonalRegion> estatates = new HashSet<>(); //estates of this nation
  private HashSet<Group> groups = new HashSet<>(); //all groups created by this faction
  private HashMap<UUID, NationMember> members = new HashMap<>(); //list of members of that nation, with groups of player
}
