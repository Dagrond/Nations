package com.github.Dagrond.Events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.Dagrond.Nation.Nation;
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
	  /* dupa debug */ if (uuid == null) Bukkit.getLogger().info("dupa");;
	  if (config.isSavedMember(uuid)) {
	    NationMember member = new NationMember(uuid);
	    config.saveNationMember(member);
	  } else {
	    NationMember.addOnlineMember(config.loadMember(uuid));
	  }
	  Nation nation = Nation.getPlayerNation(player);
	  if (nation != null) {
  	  nation.addOnlineMember(player);
	  }
	}
}
