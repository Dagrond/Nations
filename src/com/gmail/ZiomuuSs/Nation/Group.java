package com.gmail.ZiomuuSs.Nation;

import java.util.HashSet;

import org.bukkit.ChatColor;

import com.gmail.ZiomuuSs.Utils.ConfigLoader;

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
    KICK, ATTACK, INFO, GIVE_RANK, BYPASS_BUILD, BUILD_CITY, ALL; 
    /*
     * KICK - player can kick players out of nation
     * ATTACK - player can attack other nations buildings
     * INFO - player has view into specific nation's info
     * GIVE_RANK - player can give ranks to other players (that are in lower rank)
     * BYPASS_BUILD - player can build on every city of this nation (including member's plots)
     * BUILD_CITY - player can build in specific city
     * ALL - this permissions contains all other permissions (but still, king is higher than this perm)
     */
  }
  private ConfigLoader config;
  private Nation nation; //nation that groups belongs into
  private String name; //name of group
  private HashSet<City> buildCities = new HashSet<>(); //cities that this player is supposed to build
  private int priority = 0; //priority of this rank (higher numbers override lower)
  private String prefix = ""; //prefix of this group on chat
  private HashSet<NationPermission> permittedActions = new HashSet<>(); //all actions that are permitted for this group
  
  public Group(String name, Nation nation, ConfigLoader config) {
    this.name = name;
    this.nation = nation;
    groups.add(this);
    this.config = config;
    config.saveGroup(this);
  }
  
  
  
  //setters
  
  public void setPriority(int priority) {
    this.priority = priority;
    config.saveGroup(this);
  }
  
  public void setPrefix(String prefix) {
    this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
    config.saveGroup(this);
  }
  
  public void delPermission(String perm) {
    permittedActions.remove(NationPermission.valueOf(perm));
    config.saveGroup(this);
  }
  
  public void addPermission(String perm) {
    permittedActions.add(NationPermission.valueOf(perm));
    config.saveGroup(this);
  }
  
  public void delGroup() {
    for (NationMember member : NationMember.getMembers()) {
      if (member.getNation().equals(nation) && member.getPlayerGroups().contains(this))
        member.kickFromGroup(this);
    }
    groups.remove(this);
    config.delSavedGroup(this);
  }
  
  //checkers
  
  public boolean hasPermission(NationPermission perm) {
    return permittedActions.contains(perm);
  }
  
  //getters
  public static Group matchGroup(String name, Nation nation) {
    for(Group group : groups) {
      if (group.getNation().equals(nation) && group.toString().equals(name)) return group;
    }
    return null;
  }
  
  public HashSet<NationPermission> getPermissions() {
    return permittedActions;
  }
  
  public Nation getNation() {
    return nation;
  }
  
  public static HashSet<Group> getAllGroupsInNation(Nation nation) {
    HashSet<Group> all = new HashSet<>();
    for(Group g : groups) {
      if (g.getNation().equals(nation)) all.add(g);
    }
    return all;
  }
  
  @Override
  public String toString() {
    return name;
  }
  
  public static HashSet<Group> getGroups() {
    return groups;
  }
}
