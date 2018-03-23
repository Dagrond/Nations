package com.gmail.ZiomuuSs.Nation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Nation.Group.NationPermission;

public class NationMember {
  private static HashMap<UUID, NationMember> members = new HashMap<>(); //set of all nation members
  private UUID player; //player that is this class for
  private Nation nation; //nation of this player
  private HashSet<Group> groups = new HashSet<>(); //groups that player is into
  private HashSet<Language> languages = new HashSet<>(); //languages that player knows and what level of knowing this language (0 - not at all, 1 - beginner, 2 - advanced, 3 - native)
  private int pvpRank = 0; //player pvp rank. 0 is not specified
  
  public NationMember(UUID uuid, Nation nation) {
    this.player = uuid;
    this.nation = nation;
    members.put(uuid, this);
  }
  
  public boolean hasPermission (NationPermission perm) {
    for (Group group : groups) {
      if (group.getPermissions().contains(perm))
        return true;
    }
    return false;
  }
  
  //getters
  @Override
  public String toString() {
    return Bukkit.getOfflinePlayer(player).getName();
  }
  
  public UUID getUUID() {
    return player;
  }
  
  public int getPvPRank() {
    return pvpRank;
  }
  
  public HashSet<Group> getPlayerGroups() {
    return groups;
  }
  
  public static NationMember matchMemberByPlayer(Player player) {
    return members.get(player.getUniqueId());
  }
  
  public static Collection<NationMember> getMembers() {
    return members.values();
  }
}
