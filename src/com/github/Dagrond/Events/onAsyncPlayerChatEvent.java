package com.github.Dagrond.Events;

import java.util.HashSet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.Dagrond.Nation.Nation;

public class onAsyncPlayerChatEvent implements Listener {
  
  @EventHandler
  public void onChat(AsyncPlayerChatEvent e) {
    e.setCancelled(true);
    /* message that is send to players (including nation prefix, name, permission prefix and proper message)
     * at this state, there's only permission prefix, display name and permission suffix
     * Nation prefix is added at the beginning of the string, and message that player sent - at the end of string
     */
    String msg = ""; 
    Player player = e.getPlayer();
    if (e.getMessage().startsWith("!")) {
      Nation nation = Nation.getPlayerNation(player);
      if (nation != null && Nation.isInOwn(player)) {
        
      }
    }
    //send message to 100 nearest player
    HashSet<Player> nearbyPlayers = new HashSet<>();
    for (Entity entity : player.getNearbyEntities(100, 100, 100)) {
      if (entity instanceof Player) {
        entity.sendMessage(msg);
        
      }
    }
    
  }
}
