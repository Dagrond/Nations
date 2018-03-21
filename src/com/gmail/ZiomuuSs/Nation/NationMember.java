package com.gmail.ZiomuuSs.Nation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import com.gmail.ZiomuuSs.Nation.Group.NationPermission;

public class NationMember {
  private UUID player; //player that is this class for
  private Nation nation; //nation of this player
  private HashSet<Group> groups = new HashSet<>(); //groups that player is into
  private HashMap<String, Integer> languages = new HashMap<>(); //languages that player knows and what level of knowing this language (0 - not at all, 1 - beginner, 2 - advanced, 3 - native)
  private int pvpRank = 0; //player pvp rank. 0 is not specified
  
  public NationMember(UUID uuid) {
    this.player = uuid;
    
  }
  
  public boolean hasPermission (NationPermission perm) {
    for (Group group : groups) {
      if (group.getPermissions().contains(perm))
        return true;
    }
    return false;
  }
}
