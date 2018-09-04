package com.github.Dagrond;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

import com.github.Dagrond.Commands.NationCommand;
import com.github.Dagrond.Events.OnDeathEvent;
import com.github.Dagrond.Events.RespawnEvent;
import com.github.Dagrond.Events.onAsyncPlayerChatEvent;
import com.github.Dagrond.Utils.ConfigLoader;
import com.github.Dagrond.Utils.msg;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public final class NationPlugin extends JavaPlugin {
  private ConfigLoader config;
  private WorldGuardPlugin worldGuard;
  private DynmapAPI dynmap;
  
  public void onEnable() {
    reload(null);
  }
  
  public void onDisable() {
    //?
  }
  
  public void reload(CommandSender sender) {
    //clear old data
    HandlerList.unregisterAll(this);
    getServer().getPluginManager().registerEvents(new onAsyncPlayerChatEvent(), this);
    getServer().getPluginManager().registerEvents(new OnDeathEvent(), this);
    getServer().getPluginManager().registerEvents(new RespawnEvent(), this);
    //load plugin
    Plugin wg = getServer().getPluginManager().getPlugin("WorldGuard");
    if (!(wg == null || !(wg instanceof WorldGuardPlugin))) worldGuard = (WorldGuardPlugin) wg;
    Plugin dm = getServer().getPluginManager().getPlugin("dynmap");
    if (!(dm == null || !(dm instanceof DynmapAPI))) dynmap = (DynmapAPI) dm;
    config = new ConfigLoader(this, worldGuard);
    getCommand("Nation").setExecutor(new NationCommand(config));
    if (sender != null) sender.sendMessage(msg.get("reloaded", true));
  }
  
  public DynmapAPI getDynmap() {
    return dynmap;
  }
  
}
