package com.github.Dagrond.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.github.Dagrond.Nation.Nation;

public class OnDeathEvent implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
	  Player player = e.getEntity();
	  Nation nation = Nation.getPlayerNation(player);
	  if (nation != null) {
	    RespawnEvent.deathLocation.put(player.getUniqueId(), player.getLocation());
	  }
	}

}
