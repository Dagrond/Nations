package com.github.Dagrond;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

import com.github.Dagrond.Commands.NationCommand;
import com.github.Dagrond.Events.OnDeathEvent;
import com.github.Dagrond.Events.RespawnEvent;
import com.github.Dagrond.Events.onAsyncPlayerChatEvent;
import com.github.Dagrond.Events.onJoin;
import com.github.Dagrond.Events.onLeave;
import com.github.Dagrond.Nation.Estate;
import com.github.Dagrond.Nation.Nation;
import com.github.Dagrond.Nation.NationMember;
import com.github.Dagrond.Utils.ConfigLoader;
import com.github.Dagrond.Utils.Msg;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public final class NationPlugin extends JavaPlugin {
	private ConfigLoader config;
	private WorldGuardPlugin worldGuard;
	private DynmapAPI dynmap;
	private Economy econ;
  private Permission perms;
  private Chat chat;

	public void onEnable() {
		reload(null);
	}

	public void onDisable() {
		// ?
	}

	public void reload(CommandSender sender) {
	 // clear old data
	 long milis = System.currentTimeMillis();
	 NationMember.purge();
	 Estate.purge();
	 Nation.purge();
	 if (!setupEconomy()) {
	   this.getLogger().severe("Nie znaleziono Vault!");
     Bukkit.getPluginManager().disablePlugin(this);
     return;
	  }
    Plugin wg = getServer().getPluginManager().getPlugin("WorldGuard");
    if (!(wg == null || !(wg instanceof WorldGuardPlugin)))
      worldGuard = (WorldGuardPlugin) wg;
    Plugin dm = getServer().getPluginManager().getPlugin("dynmap");
    if (!(dm == null || !(dm instanceof DynmapAPI)))
      dynmap = (DynmapAPI) dm;
	  config = new ConfigLoader(this, worldGuard);
	  NationMember.updateConfig(config);
		HandlerList.unregisterAll(this);
		getServer().getPluginManager().registerEvents(new onAsyncPlayerChatEvent(), this);
		getServer().getPluginManager().registerEvents(new OnDeathEvent(), this);
		getServer().getPluginManager().registerEvents(new RespawnEvent(), this);
		getServer().getPluginManager().registerEvents(new onJoin(config), this);
		getServer().getPluginManager().registerEvents(new onLeave(), this);
		this.setupPermissions();
		this.setupChat();
		getCommand("Nation").setExecutor(new NationCommand(config));
		if (sender != null) {
			sender.sendMessage(Msg.get("reloaded", true, System.currentTimeMillis()-milis+""));
		}
	}

	public DynmapAPI getDynmap() {
		return dynmap;
	}
  private boolean setupEconomy() {
    if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
        return false;
    }
    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
        return false;
    }
    econ = rsp.getProvider();
    return econ != null;
  }
  private boolean setupChat() {
    RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
    if (rsp != null) 
      chat = rsp.getProvider();
    return chat != null;
  }
  private boolean setupPermissions() {
    RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
    if (rsp != null)
      perms = rsp.getProvider();
    return perms != null;
  }
  public Economy getEcononomy() {
    return econ;
  }
  public Permission getPermissions() {
    return perms;
  }
  public Chat getChat() {
    return chat;
  }
}
