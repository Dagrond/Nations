package com.gmail.ZiomuuSs.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.ZiomuuSs.NationPlugin;
import com.gmail.ZiomuuSs.Nation.Estate;
import com.gmail.ZiomuuSs.Nation.Group;
import com.gmail.ZiomuuSs.Nation.Nation;
import com.gmail.ZiomuuSs.Nation.NationMember;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.gmail.ZiomuuSs.Nation.Group.NationPermission;

public class ConfigLoader {
  private ConfigAccessor msgAccessor;
  private ConfigurationSection config;
  private WorldGuardPlugin worldGuard;
  private NationPlugin plugin;
  private boolean isLoading = false; //describes if plugin is loading files (to block saving during loading)
  //options variables
  public String NotInNation; //name of nation that is always default (if an estate is not belonging to any exiting nation, it belongs to this faction
  
  public ConfigLoader(NationPlugin plugin, WorldGuardPlugin worldGuard) {
    this.plugin = plugin;
    this.worldGuard = worldGuard;
    plugin.saveDefaultConfig();
    msgAccessor = new ConfigAccessor(plugin, "Messages.yml");
    msgAccessor.saveDefaultConfig();
    msg.set(msgAccessor.getConfig());
    config = plugin.getConfig();
    loadAll();
  }
  
  //loading all files
  @SuppressWarnings("unchecked")
  private void loadAll() {
    isLoading = true;
    NotInNation = config.getString("NotInNation");
    int loadedNations = 0;
    int loadedEstates = 0;
    int loadedGroups = 0;
    int loadedMembers = 0;
    //loading estates
    if (new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "Estates").exists()) {
      for (File file : new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "Estates").listFiles()) {
        FileConfiguration cs = YamlConfiguration.loadConfiguration(file);
        String name = file.getName();
        name = name.substring(0, name.length() - 4); //remove the .yml
        ProtectedPolygonalRegion rg;
        World world = Bukkit.getWorld(cs.getString("RegionWorld"));
        if (world != null) {
          rg = (ProtectedPolygonalRegion) worldGuard.getRegionManager(world).getRegion(cs.getString("Region"));
          if (rg != null) {
            new Estate(name, rg, world.getName());
            ++loadedEstates;
          }
        }
      }
    }
    //loading nations
    if (new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "Nations").exists()) {
      for (File file : new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "Nations").listFiles()) {
        FileConfiguration cs = YamlConfiguration.loadConfiguration(file);
        String name = file.getName();
        name = name.substring(0, name.length() - 4); //remove the .yml
        Nation nation = new Nation(this, name, UUID.fromString(cs.getString("King")), Estate.getEstateByName(cs.getString("Capital")));
        ++loadedNations;
        if (cs.isList("BannedPlayers")) {
          for (String uuid : (List<String>) cs.getList("BannedPlayers")) {
            nation.getBannedPlayers().add(UUID.fromString(uuid));
          }
        }
        if (cs.isList("Estates")) {
          for (String estate : (List<String>) cs.getList("Estates")) {
            nation.getEstates().add(Estate.getEstateByName(estate));
          }
        }
        //loading nation groups
        if (new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Groups"+String.valueOf(File.separatorChar)+nation.toString()).exists()) {
          for (File f : new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Groups"+String.valueOf(File.separatorChar)+nation.toString()).listFiles()) {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
            String groupName = f.getName();
            groupName = groupName.substring(0, groupName.length() - 4); //remove the .yml
            Group group = new Group(groupName, nation, this);
            ++loadedGroups;
            nation.getGroups().add(group);
            group.setPriority(fc.getInt("Priority"));
            if (fc.isString("Prefix")) group.setPrefix(fc.getString("Prefix"));
            if (fc.isList("Permissions")) {
              for (String perm : (List<String>) fc.getList("Permissions")) {
                group.getPermissions().add(NationPermission.valueOf(perm));
              }
            }
          }
        }
        //loading nation members
        if (new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Players").exists()) {
          for (File fl : new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Players").listFiles()) {
            FileConfiguration mc = YamlConfiguration.loadConfiguration(fl);
            if (mc.getString("Nation").equals(name)) {
              String uuid = fl.getName();
              uuid = uuid.substring(0, uuid.length() - 4); //remove the .yml
              UUID player = UUID.fromString(uuid);
              nation.getMembers().put(player, new NationMember(this, player, nation));
              ++loadedMembers;
            }
          }
        }
      }
    }
    Bukkit.getLogger().info(msg.get("console_loaded", true, Integer.toString(loadedNations), Integer.toString(loadedEstates), Integer.toString(loadedGroups), Integer.toString(loadedMembers)));
    isLoading = false;
  }
  
  public void saveNation(Nation nation) {
    if (!isLoading) {
      new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Nations", nation.toString()+".yml").delete();
      ConfigAccessor ca = new ConfigAccessor(plugin, nation.toString()+".yml", "Nations");
      ConfigurationSection cs = ca.getConfig();
      cs.set("Name", nation.toString());
      cs.set("King", nation.getKing().toString());
      cs.set("Capital", nation.getCapital().toString());
      List<String> list = new ArrayList<>();
      for (Estate estate : nation.getEstates()) {
        list.add(estate.toString());
      }
      cs.set("Estates", list);
      list.clear();
      for (Group group : nation.getGroups()) {
        list.add(group.toString());
      }
      cs.set("Groups", list);
      if (!nation.getBannedPlayers().isEmpty()) {
        list.clear();
        for (UUID uuid : nation.getBannedPlayers()) {
          list.add(uuid.toString());
        }
        cs.set("BannedPlayers", list);
      }
      list.clear();
      for (UUID uuid : nation.getMembers().keySet()) {
        list.add(uuid.toString());
      }
      cs.set("Members", list);
    }
  }
  
  public void saveNationMember(NationMember player) {
    if (!isLoading) {
      new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Players", player.getUUID()+".yml").delete();
      ConfigAccessor ca = new ConfigAccessor(plugin, player.toString()+".yml", "Players");
      ConfigurationSection cs = ca.getConfig();
      cs.set("Nation", player.getNation().toString());
      List<String> list = new ArrayList<>();
      for (Group group : player.getPlayerGroups()) {
        list.add(group.toString());
      }
      cs.set("Groups", list);
      for (String language : player.getLanguages().keySet()) {
        cs.set("Languages."+language, player.getLanguages().get(language));
      }
    }
  }
  
  public void saveGroup(Group group) {
    if (!isLoading) {
      new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Groups"+String.valueOf(File.separatorChar)+group.getNation().toString(), group.toString()+".yml").delete();
      ConfigAccessor ca = new ConfigAccessor(plugin, group.toString()+".yml", "Groups"+String.valueOf(File.separatorChar)+group.getNation().toString());
      ConfigurationSection cs = ca.getConfig();
      cs.set("Priority", group.getPriority());
      if (!group.getPrefix().equals("")) cs.set("Prefix", group.getPrefix());
      if (!group.getPermissions().isEmpty()) {
        List<String> list = new ArrayList<>();
        for (NationPermission perm : group.getPermissions()) {
          list.add(perm.toString());
        }
        cs.set("Permissions", list);
      }
    }
  }
  
  public void saveEstate(Estate estate) {
    if (!isLoading) {
      new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Estates", estate.toString()+".yml").delete();
      ConfigAccessor ca = new ConfigAccessor(plugin, estate.toString()+".yml", "Estates");
      ConfigurationSection cs = ca.getConfig();
      cs.set("Region", estate.getRegion().toString());
      cs.set("RegionWorld", estate.getRegionWorld());
    }
  }
  
  public void delNationMember(NationMember member) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Players", member.getUUID()+".yml").delete();
  }
  
  public void delSavedGroup(Group group) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Groups"+String.valueOf(File.separatorChar)+group.getNation().toString(), group.toString()+".yml").delete();
  }
  
  public WorldGuardPlugin getWorldGuard() {
    return worldGuard;
  }
}
