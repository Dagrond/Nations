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
  private Estate capital; //capital of this nation 
  private HashSet<Estate> estates = new HashSet<>(); //estates of this nation
  private HashSet<Group> groups = new HashSet<>(); //all groups created by this faction
  private HashSet<UUID> bannedPlayers = new HashSet<>(); //players who cannot join to this nation
  private HashMap<UUID, NationMember> members = new HashMap<>(); //list of members of that nation, with groups of player
  
  @SuppressWarnings("deprecation")
  public Nation(ConfigLoader config, String name, String player, Estate capital) {
    this.name = name;
    nations.add(this);
    this.config = config;
    this.capital = capital;
    king = Bukkit.getOfflinePlayer(player).getUniqueId();
  }
  
  public Nation(ConfigLoader config, String name, UUID uuid, Estate capital) {
    this.name = name;
    nations.add(this);
    this.config = config;
    this.capital = capital;
    king = uuid;
  }
  
  public Nation(ConfigLoader config, String name, UUID uuid) {
    this.name = name;
    nations.add(this);
    this.config = config;
    king = uuid;
  }
  
  public void broadcastToOnlineMembers(String message) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (isMember(player.getUniqueId()))
        player.sendMessage(message);
    }
  }
  
  //setters
  
  public void addMember(Player player) {
    members.put(player.getUniqueId(), new NationMember(config, player.getUniqueId(), this));
    broadcastToOnlineMembers(msg.get("nation_member_added", true, player.getName()));
    config.saveNation(this);
  }
  
  public void kickMember(UUID uuid) {
    NationMember.getMembers().remove(members.get(uuid));
    config.delNationMember(members.get(uuid));
    members.remove(uuid);
    config.saveNation(this);
  }
  
  public void setKing(UUID uuid) {
    this.king = uuid;
  }
  
  public void banPlayer(UUID uuid) {
    if (isMember(uuid)) kickMember(uuid);
    bannedPlayers.add(uuid);
  }
  
  public void setCapital(Estate estate) {
    capital = estate;
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
  
  public Estate getCapital() {
    return capital;
  }
  
  public HashSet<UUID> getBannedPlayers() {
    return bannedPlayers;
  }
  
  public HashSet<Group> getGroups() {
    return groups;
  }
  
  public UUID getKing() {
    return king;
  }
  
  public HashSet<Estate> getEstates() {
    return estates;
  }
  
  public static Nation getPlayerNation(UUID uuid) {
    for (Nation nation : nations) {
      if (nation.isMember(uuid)) return nation;
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
  
  public static Nation getNationByName(String name) {
    for (Nation nation : nations) {
      if (nation.toString().equalsIgnoreCase(name))
        return nation;
    }
    return null;
  }
}
