package com.gmail.ZiomuuSs.Nation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.gmail.ZiomuuSs.Nation.Group.NationPermission;
import com.gmail.ZiomuuSs.Utils.ConfigLoader;

public class NationMember {
  private static HashMap<UUID, NationMember> members = new HashMap<>(); //set of all nation members
  private UUID player; //player that is this class for
  private Nation nation; //nation of this player
  private ConfigLoader config;
  private HashSet<Group> groups = new HashSet<>(); //groups that player is into
  private HashSet<Language> languages = new HashSet<>(); //languages that player knows and what level of knowing this language (0 - not at all, 1 - beginner, 2 - advanced, 3 - native)
  
  public NationMember(ConfigLoader config, UUID uuid, Nation nation) {
    this.player = uuid;
    this.nation = nation;
    this.config = config;
    members.put(uuid, this);
  }
  
  public boolean hasPermission (NationPermission perm) {
    for (Group group : groups) {
      if (group.getPermissions().contains(perm) || nation.getKing().equals(player) || group.getPermissions().contains(NationPermission.ALL))
        return true;
    }
    return false;
  }
  
  //setters
  
  public void kickFromGroup(Group g) {
    groups.remove(g);
    config.saveNationMember(this);
  }
  
  public void addToGroup(Group g) {
    groups.add(g);
    config.saveNationMember(this);
  }
  
  //getters
  @Override
  public String toString() {
    return Bukkit.getOfflinePlayer(player).getName();
  }
  
  public UUID getUUID() {
    return player;
  }
  
  public Nation getNation() {
    return nation;
  }
  
  public HashSet<Group> getPlayerGroups() {
    return groups;
  }
  
  public static NationMember matchMemberByUUID(UUID uuid) {
    return members.get(uuid);
  }
  
  public static Collection<NationMember> getMembers() {
    return members.values();
  }
}
