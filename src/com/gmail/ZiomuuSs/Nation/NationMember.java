package com.gmail.ZiomuuSs.Nation;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.gmail.ZiomuuSs.Nation.Group.NationPermission;

public class NationMember {
  private static HashSet<NationMember> members = new HashSet<>(); //set of all nation members
  private UUID player; //player that is this class for
  private Nation nation; //nation of this player
  private HashSet<Group> groups = new HashSet<>(); //groups that player is into
  private HashSet<Language> languages = new HashSet<>(); //languages that player knows and what level of knowing this language (0 - not at all, 1 - beginner, 2 - advanced, 3 - native)
  private int pvpRank = 0; //player pvp rank. 0 is not specified
  
  public NationMember(UUID uuid, Nation nation) {
    this.player = uuid;
    this.nation = nation;
    members.add(this);
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
  
  public static HashSet<NationMember> getMembers() {
    return members;
  }
}
