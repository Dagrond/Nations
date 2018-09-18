package com.github.Dagrond.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.github.Dagrond.Nation.Nation;
import com.github.Dagrond.Nation.NationMember;
import com.github.Dagrond.Utils.ConfigLoader;

public class onJoin implements Listener {
  private ConfigLoader config;
  
  public onJoin(ConfigLoader config) {
    this.config = config;
  }
  
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
	  Player player = e.getPlayer();
	  if (config.loadMember(player.getUniqueId()) == null) {
	    NationMember member = new NationMember(player.getUniqueId());
	    config.saveNationMember(member);
	    NationMember.addOnlineMember(member);
	  } else {
	    NationMember.addOnlineMember(config.loadMember(player.getUniqueId()));
	  }
	  Nation nation = Nation.getPlayerNation(player);
	  if (nation != null) {
  	  nation.addOnlineMember(player);
	  }
	}
}
