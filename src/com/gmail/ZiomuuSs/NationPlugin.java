package com.gmail.ZiomuuSs;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

import com.gmail.ZiomuuSs.Commands.NationCommand;
import com.gmail.ZiomuuSs.Nation.Estate;
import com.gmail.ZiomuuSs.Nation.Group;
import com.gmail.ZiomuuSs.Nation.Nation;
import com.gmail.ZiomuuSs.Nation.NationMember;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.gmail.ZiomuuSs.Utils.ConfigLoader;
import com.gmail.ZiomuuSs.Utils.msg;

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
    Estate.getEstates().clear();
    Nation.getNations().clear();
    NationMember.getMembers().clear();
    Group.getGroups().clear();
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
