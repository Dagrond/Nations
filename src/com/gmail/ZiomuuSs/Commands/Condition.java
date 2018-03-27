package com.gmail.ZiomuuSs.Commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Nation.Estate;
import com.gmail.ZiomuuSs.Nation.Group;
import com.gmail.ZiomuuSs.Nation.Group.NationPermission;
import com.gmail.ZiomuuSs.Nation.Nation;
import com.gmail.ZiomuuSs.Nation.NationMember;
import com.gmail.ZiomuuSs.Utils.ConfigLoader;
import com.gmail.ZiomuuSs.Utils.HexValidator;
import com.gmail.ZiomuuSs.Utils.msg;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Condition {
  private Player player;
  private ConfigLoader config;
  
  public Condition(Player player, ConfigLoader config) {
    this.player = player;
    this.config = config;
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
      player.sendMessage(msg.get("error_estate_estate_not_exist", true, name));
      return false;
    }
  }
  
  public boolean isAlreadyInEstate(ProtectedPolygonalRegion region, World world) {
    for (Estate estate : Estate.getEstates()) {
      if (estate.getRegion().equals(region) && estate.getRegionWorld().equals(world)) {
        player.sendMessage(msg.get("error_region_already_in_use", true, region.getId()));
        return true;
      }
    }
    return false;
  }
  
  public boolean isNotCapital(Estate e) {
    if (e.getNation() != null) {
      if (e.getNation().getCapital().equals(e)) {
        player.sendMessage(msg.get("error_estate_is_capital", true, e.toString(), e.getNation().toString()));
        return false;
      }
    }
    return true;
  }
  
  public boolean isHex(String c) {
    HexValidator x = new HexValidator();
    if (x.validate(c)) {
      return true;
    }
    player.sendMessage(msg.get("error_not_hex", true, c));
    return false;
  }
  
  public boolean isPolygonalRegion(String name, World world) {
    ProtectedRegion region = config.getWorldGuard().getRegionManager(world).getRegion(name);
    if (region != null && region instanceof ProtectedPolygonalRegion) {
      return true;
    } else {
      player.sendMessage(msg.get("error_not_poly_region", true, name));
      return false;
    }
  }
  
  public boolean isNotEstate(String name) {
    if (Estate.getEstateByName(name) == null) 
      return true;
    else {
      player.sendMessage(msg.get("error_estate_estate_exist", true, name));
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
  
  public boolean isBannedFromNation(Nation nation) {
    if (nation.getBannedPlayers().contains(player.getUniqueId())) {
      player.sendMessage(msg.get("error_banned_in_nation", true, nation.toString()));
      return true;
    }
    return false;
  }
  
  public boolean checkIfCanJoin(Nation nation) {
    for (Nation n : Nation.getNations()) {
      if (n.getMembers().size() >= nation.getMembers().size()+5) {
        player.sendMessage(msg.get("error_nation_balance", true, nation.toString()));
        return false;
      }
    }
    return true;
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
        if (member.getUUID().equals(player.getUniqueId())) {
          this.player.sendMessage(msg.get("error_player_in_nation", true, name));
          return false;
        }
      }
    } else {
      this.player.sendMessage(msg.get("error_player_not_exist", true, name));
      return false;
    }
    return true;
  }
  
}
