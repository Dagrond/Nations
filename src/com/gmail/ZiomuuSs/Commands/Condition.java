package com.gmail.ZiomuuSs.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Nation.Group.NationPermission;
import com.gmail.ZiomuuSs.Nation.Nation;
import com.gmail.ZiomuuSs.Nation.NationMember;
import com.gmail.ZiomuuSs.Utils.msg;

public class Condition {
  private Player player;
  
  public Condition(Player player) {
    this.player = player;
  }
  
  public boolean hasNation() {
    if (Nation.getPlayerNation(player) == null) {
      player.sendMessage(msg.get("error_not_in_nation", true));
      return false;
    } else {
      return true;
    }
  }
  
  public boolean hasPermission(String...perm) {
    if (perm.length<1) return true;
    for (String p : perm) {
      if (!player.hasPermission(p) && !player.isOp())
        player.sendMessage(msg.get("error_permission", true));
        return false;
    }
    return true;
  }
  
  public boolean hasNationPermission(String...perm) {
    if (perm.length<1) return true;
    for (String p : perm) {
      if (!NationMember.matchMemberByPlayer(player).hasPermission(NationPermission.valueOf(p)))
        player.sendMessage(msg.get("error_permission", true));
        return false;
    }
    return true;
  }
  
  public boolean isNation(String nation) {
    for (Nation n : Nation.getNations()) {
      if (n.toString().equalsIgnoreCase(nation))
        return true;
    }
    player.sendMessage(msg.get("error_not_a_nation", true, nation));
    return false;
  }
  
  public boolean isNotNation(String nation) {
    for (Nation n : Nation.getNations()) {
      if (n.toString().equalsIgnoreCase(nation)) {
        player.sendMessage(msg.get("error_not_a_nation", true, nation));
        return false;
      }
    }
    return true;
  }
  
  @SuppressWarnings("deprecation")
  public boolean notMemberofNation(String name) {
    OfflinePlayer player = Bukkit.getOfflinePlayer(name);
    if (player.hasPlayedBefore()) {
      for(NationMember member : NationMember.getMembers()) {
        if (member.getUUID().equals(player.getUniqueId()))
          return false;
      }
    } else {
      this.player.sendMessage(msg.get("error_player_not_exist", true, name));
      return true;
    }
    this.player.sendMessage(msg.get("error_player_in_nation", true, name));
    return true;
  }
  
}
