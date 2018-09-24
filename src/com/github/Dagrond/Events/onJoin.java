package com.github.Dagrond.Events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.Dagrond.Nation.NationMember;
import com.github.Dagrond.Utils.ConfigLoader;

public class onJoin implements Listener {
  private ConfigLoader config;
  
  public onJoin(ConfigLoader config) {
    this.config = config;
  }
  
	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
	  Player player = e.getPlayer();
	  UUID uuid = player.getUniqueId();
	  if (config.isSavedMember(uuid)) {
	    config.loadMember(uuid);
	  } else {
	    new NationMember(uuid).save();
	  }
	}
}
