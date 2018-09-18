package com.github.Dagrond.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.Dagrond.Nation.Nation;

public class onLeave implements Listener {
  
	@EventHandler
	public void onLogin(PlayerQuitEvent e) {
	  Player player = e.getPlayer();
	  Nation nation = Nation.getPlayerNation(player);
	  if (nation != null) {
  	  nation.delOnlineMember(player);
	  }
	}
}
