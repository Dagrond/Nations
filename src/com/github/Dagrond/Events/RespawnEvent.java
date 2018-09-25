package com.github.Dagrond.Events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.github.Dagrond.Nation.Estate;
import com.github.Dagrond.Nation.Nation;

public class RespawnEvent implements Listener {
  public static HashMap<UUID, Location> deathLocation = new HashMap<>();

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
	  Player player = e.getPlayer();
    if (deathLocation.containsKey(player.getUniqueId())) {
      Nation nation = Nation.getPlayerNation(player);
      if (nation != null) {
        Location loc = deathLocation.get(player.getUniqueId());
        Location nearestSpawn = null;
        double distance = -1;
        double pDistance;
        for (Estate estate : nation.getEstates()) {
          pDistance = loc.distance(estate.getSpawn());
          if (distance == -1) {
            distance = pDistance;
            nearestSpawn = estate.getSpawn();
            break;
          }
          if (pDistance < distance) {
            distance = pDistance;
            nearestSpawn = estate.getSpawn();
          }
        }
        if (nearestSpawn != null) {
          e.setRespawnLocation(nearestSpawn);
        }
      }
    }
	}
}
