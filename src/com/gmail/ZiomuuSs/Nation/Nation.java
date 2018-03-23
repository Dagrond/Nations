package com.gmail.ZiomuuSs.Nation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Utils.ConfigLoader;
import com.gmail.ZiomuuSs.Utils.msg;

/*
 *  Nation class
 *  this stores all information about certain nation
 *  
 */
public class Nation {
  private static HashSet<Nation> nations = new HashSet<>(); //list of all nations
  private ConfigLoader config;
  private String name; //name of nation
  private UUID king; //king of nation
  private HashSet<Estate> estates = new HashSet<>(); //estates of this nation
  private HashSet<Group> groups = new HashSet<>(); //all groups created by this faction
  private Language language; //language of this nation
  private HashMap<UUID, NationMember> members = new HashMap<>(); //list of members of that nation, with groups of player
  
  @SuppressWarnings("deprecation")
  public Nation(ConfigLoader config, String name, String player) {
    this.name = name;
    nations.add(this);
    this.config = config;
    king = Bukkit.getOfflinePlayer(player).getUniqueId();
  }
  
  public void broadcastToOnlineMembers(String message) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (isMember(player.getUniqueId()))
        player.sendMessage(message);
    }
  }
  
  //setters
  
  public void addMember(Player player) {
    broadcastToOnlineMembers(msg.get("nation_member_added", true, player.getDisplayName()));
    members.put(player.getUniqueId(), new NationMember(player.getUniqueId(), this));
    player.sendMessage(msg.get("nation_joined", true, name));
  }
  
  //checkers
  
  public boolean isMember(UUID uuid) {
    return members.containsKey(uuid);
  }
  
  public boolean hasEstate(String name ) {
    for (Estate e : estates) {
      if (e.toString().equalsIgnoreCase(name))
        return true;
    }
    return false;
  }
  
  //getters
  
  @Override
  public String toString() {
    return name;
  }
  
  public static Nation getPlayerNation(Player player) {
    for (Nation nation : nations) {
      if (nation.isMember(player.getUniqueId())) return nation;
    }
    return null;
  }
  
  public NationMember getMember(UUID uuid) {
    return members.get(uuid);
  }
  
  public Estate getEstate(String name) {
    for (Estate e : estates) {
      if (e.toString().equalsIgnoreCase(name))
        return e;
    }
    return null;
  }
  
  public HashMap<UUID, NationMember> getMembers() {
    return members;
  }
  
  public static HashSet<Nation> getNations() {
    return nations;
  }
}
