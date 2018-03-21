package com.gmail.ZiomuuSs.Commands;

import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Utils.msg;

public class Condition {
  private Player player;
  
  public Condition(Player player) {
    this.player = player;
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
  
}
