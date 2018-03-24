package com.gmail.ZiomuuSs.Commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Nation.Estate;
import com.gmail.ZiomuuSs.Nation.Group;
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
    if (Nation.getPlayerNation(player.getUniqueId()) == null) {
      player.sendMessage(msg.get("error_not_in_nation", true));
      return false;
    } else {
      return true;
    }
  }
  
  public boolean hasNoNation() {
    if (Nation.getPlayerNation(player.getUniqueId()) != null) {
      player.sendMessage(msg.get("error_already_in_nation", true));
      return false;
    } else {
      return true;
    }
  }
  
  public boolean isFreeEstate(String name) {
    if (Estate.getEstateByName(name).getNation() == null)
      return true;
    else {
      player.sendMessage(msg.get("error_estate_not_free", true, name));
      return false;
    }
  }
  
  public boolean isEstate(String name) {
    if (Estate.getEstateByName(name) != null) 
      return true;
    else {
      player.sendMessage(msg.get("estate_estate_not_exist", true, name));
      return false;
    }
  }
  
  public boolean hasNation(String name) {
    if (Nation.getPlayerNation(player.getUniqueId()) == null) {
      player.sendMessage(msg.get("error_not_in_nation", true));
      return false;
    } else {
      return true;
    }
  }
  
  @SuppressWarnings("deprecation")
  public boolean hasGroup(Group group, String name) {
    if (hasEventPlayed(name) && hasNation(name)) {
      UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
      NationMember member = NationMember.matchMemberByUUID(uuid);
      if (member.getPlayerGroups().contains(group))
        return true;
      else {
        player.sendMessage(msg.get("error_not_in_group", true, name, group.toString()));
        return false;
      }
    } else {
      return false;
    }
  }
  
  public boolean isInt(String number) {
    if (number.matches("-?\\d+"))
      return true;
    else {
      player.sendMessage(msg.get("error_not_a_number", true, number));
      return false;
    }
  }
  
  @SuppressWarnings("deprecation")
  public boolean hasNotGroup(Group group, String name) {
    if (hasEventPlayed(name) && hasNation(name)) {
      UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
      NationMember member = NationMember.matchMemberByUUID(uuid);
      if (member.getPlayerGroups().contains(group)) {
        player.sendMessage(msg.get("error_in_group", true, name, group.toString()));
        return false;
      } else
        return true;
    } else {
      return true;
    }
  }
  
  @SuppressWarnings("deprecation")
  public boolean isInSameNation(String name) {
    if (hasEventPlayed(name) && hasNation(name)) {
      UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
      if (Nation.getPlayerNation(uuid).equals(Nation.getPlayerNation(player.getUniqueId()))) 
        return true;
      else {
        player.sendMessage(msg.get("error_not_in_same_nation", true, name));
        return false;
      }
    } else {
      return false;
    }
  }
  
  @SuppressWarnings("deprecation")
  public boolean hasEventPlayed(String name) {
    if (Bukkit.getOfflinePlayer(name).hasPlayedBefore())
      return true;
    else {
      player.sendMessage(msg.get("error_not_a_player", true, name));
      return false;
    }
  }
  
  public boolean hasPermission(String...perm) {
    if (perm.length<1) return true;
    for (String p : perm) {
      if (player.hasPermission(p) || player.isOp())
        return true;
    }
    player.sendMessage(msg.get("error_permission", true));
    return false;
  }
  
  public boolean isKing() {
    if (Nation.getPlayerNation(player.getUniqueId()) != null && Nation.getPlayerNation(player.getUniqueId()).getKing().equals(player.getUniqueId()))
      return true;
    else {
      player.sendMessage(msg.get("error_not_king", true));
      return false;
    }
  }
  
  public boolean hasNationPermission(NationPermission...perm) {
    if (perm.length<1) return true;
    for (NationPermission p : perm) {
      if (!NationMember.matchMemberByUUID(player.getUniqueId()).hasPermission(p))
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
  
  public boolean hasNationPermission(Group group, NationPermission np) {
    if (group.hasPermission(np))
      return true;
    else {
      player.sendMessage(msg.get("error_group_dont_have_permission", true, group.toString(), np.toString()));
      return false;
    }
  }
  
  private boolean isNationPermission(String perm) {
    try {
      NationPermission.valueOf(perm);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  
  public boolean isPermission(String perm) {
    try {
      NationPermission.valueOf(perm);
      return true;
    } catch (Exception e) {
      player.sendMessage(msg.get("error_no_such_permission", true, perm));
      return false;
    }
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
