package com.github.Dagrond.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.Dagrond.Nation.Nation;
import com.github.Dagrond.Utils.Msg;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class onAsyncPlayerChatEvent implements Listener {
  
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
	  String message = e.getMessage();
	  if (!message.startsWith("/")) {
  		e.setCancelled(true);
  		Player player = e.getPlayer();
  		PermissionUser user = PermissionsEx.getUser(player);
  		String msg = user.getPrefix()+player.getDisplayName()+user.getSuffix();
  		if (message.startsWith("!")) {
  			Nation nation = Nation.getPlayerNation(player);
  			if (nation != null && Nation.isInOwn(player)) {
  		     if (player.hasPermission("Nations.ChatColor")) 
  		        msg = ChatColor.translateAlternateColorCodes('&', "["+nation.getDisplayName()+"] "+msg+message.substring(1, message.length()));
  		      else
  		        msg = ChatColor.translateAlternateColorCodes('&', "["+nation.getDisplayName()+"] "+msg)+message.substring(1, message.length());
  			  nation.broadcastToOnlineMembers(msg);
  			  Bukkit.broadcast(msg, "Nations.isOP");
  			  Bukkit.getLogger().info(msg);
  			  return;
  			} else if (player.isOp()) {
  			  Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg+message.substring(1, message.length())));
  			  return;
  			} else {
  			  message = message.substring(1, message.length());
  			  player.sendMessage(Msg.get("error_not_in_own_nation", false));
  			}
  		}
  		// send message to 100 nearest player
  		if (player.hasPermission("Nations.ChatColor")) 
  		  msg = ChatColor.translateAlternateColorCodes('&', msg+message);
  		else
  		  msg = ChatColor.translateAlternateColorCodes('&', msg)+message;
  		for (Entity entity : player.getNearbyEntities(100, 100, 100)) {
  			if (entity instanceof Player) {
  				entity.sendMessage(msg);
  				player.sendMessage(msg);
  			}
  		}
  		msg = "[l] "+msg;
  		Bukkit.broadcast(msg, "Nations.isOP");
  		Bukkit.getLogger().info(msg);
	  }
	}
}