package com.github.Dagrond.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.Dagrond.Nation.Nation;

import net.milkbowl.vault.chat.Chat;

public class onAsyncPlayerChatEvent implements Listener {
  private Chat chat;
  
  public onAsyncPlayerChatEvent(Chat chat) {
    this.chat = chat;
  }
  
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		/*
		 * message that is send to players (including nation prefix, name, permission
		 * prefix and proper message) at this state, there's only permission prefix,
		 * display name and permission suffix Nation prefix is added at the beginning of
		 * the string, and message that player sent - at the end of string
		 */
		Player player = e.getPlayer();
		String msg = chat.getPlayerPrefix(player)+player.getDisplayName()+chat.getPlayerSuffix(player)+" ";
		if (e.getMessage().startsWith("!")) {
			Nation nation = Nation.getPlayerNation(player);
			if (nation != null && Nation.isInOwn(player)) {
			  msg = "["+nation.getDisplayName()+"] "+msg.substring(1, msg.length());
			  nation.broadcastToOnlineMembers(msg);
			  Bukkit.broadcast(msg, "Nations.isOP");
			  return;
			} else if (player.isOp()) {
			  msg = msg.substring(1, msg.length());
			  Bukkit.broadcastMessage(msg);
			  return;
			}
		}
		// send message to 100 nearest player
		for (Entity entity : player.getNearbyEntities(100, 100, 100)) {
			if (entity instanceof Player) {
				entity.sendMessage(msg);
			}
		}

	}
}
