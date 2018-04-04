package com.github.Dagrond.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.Dagrond.Nation.Nation;
import com.github.Dagrond.Nation.NationMember;

public class onAsyncPlayerChatEvent implements Listener {
  
  @EventHandler
  public void onChat(AsyncPlayerChatEvent e) {
    e.setFormat(e.getFormat().replaceAll("NATION", (NationMember.matchMemberByUUID(e.getPlayer().getUniqueId()) != null ? "["+Nation.getPlayerNation(e.getPlayer().getUniqueId()).toString()+"] " : "")));
  }
}
