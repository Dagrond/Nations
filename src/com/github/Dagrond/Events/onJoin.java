package com.github.Dagrond.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.github.Dagrond.Nation.Nation;

public class onJoin implements Listener {
  
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
	  Player player = e.getPlayer();
	  Nation nation = Nation.getPlayerNation(player);
	  if (nation != null) {
  	  nation.addOnlineMember(player);
	  }
	}
}
