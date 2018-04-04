package com.github.Dagrond.Events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.github.Dagrond.Nation.Estate;
import com.github.Dagrond.Nation.Nation;

public class RespawnEvent implements Listener {
  public static HashMap<UUID, Location> deathLoc = new HashMap<>();
  
  @EventHandler
  public void onRespawn(PlayerRespawnEvent e) {
    Location lastDeath = deathLoc.get(e.getPlayer().getUniqueId());
    Location newSpawn = Estate.getNearestLocation(lastDeath, Nation.getPlayerNation(e.getPlayer().getUniqueId()));
    if (newSpawn != null)
      e.setRespawnLocation(newSpawn);
    deathLoc.remove(e.getPlayer().getUniqueId());
  }
  
  
  public static HashMap<UUID, Location> getDeathLocations() {
    return deathLoc;
  }
}
