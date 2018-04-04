package com.github.Dagrond.Utils;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.Dagrond.NationPlugin;
import com.github.Dagrond.Nation.Estate;
import com.github.Dagrond.Nation.Group;
import com.github.Dagrond.Nation.Nation;
import com.github.Dagrond.Nation.NationMember;
import com.github.Dagrond.Nation.Group.NationPermission;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

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
            new Estate(name, rg, world, this, new Location(Bukkit.getWorld(cs.getString("Spawn.World")), cs.getDouble("Spawn.X"), cs.getDouble("Spawn.Y"), cs.getDouble("Spawn.Z"), (float) cs.getDouble("Spawn.Yaw"), (float) cs.getDouble("Spawn.Pitch")));
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
        nation.setColor(Integer.parseInt(cs.getString("Color").substring(1), 16));
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
    isLoading = false;
    new DynmapUpdater(plugin);
    Bukkit.getLogger().info(msg.get("console_loaded", true, Integer.toString(loadedNations), Integer.toString(loadedEstates), Integer.toString(loadedGroups), Integer.toString(loadedMembers)));
  }
  
  public void saveNation(Nation nation) {
    if (!isLoading) {
      new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Nations", nation.toString()+".yml").delete();
      ConfigAccessor ca = new ConfigAccessor(plugin, nation.toString()+".yml", "Nations");
      ConfigurationSection cs = ca.getConfig();
      cs.set("Name", nation.toString());
      cs.set("King", nation.getKing().toString());
      cs.set("Capital", nation.getCapital().toString());
      cs.set("Color", "#"+Integer.toHexString(nation.getColor()));
      HashSet<String> estates = new HashSet<>();
      for (Estate estate : nation.getEstates()) {
        estates.add(estate.toString());
      }
      cs.set("Estates", estates.toArray(new String[estates.size()]));
      if (!nation.getGroups().isEmpty()) {
        HashSet<String> groups = new HashSet<>();
        for (Group group : nation.getGroups()) {
          groups.add(group.toString());
        }
        cs.set("Groups", groups.toArray(new String[groups.size()]));
      }
      if (!nation.getBannedPlayers().isEmpty()) {
        HashSet<String> banned = new HashSet<>();
        for (UUID uuid : nation.getBannedPlayers()) {
          banned.add(uuid.toString());
        }
        cs.set("BannedPlayers", banned.toArray(new String[banned.size()]));
      }
      HashSet<String> members = new HashSet<>();
      for (UUID uuid : nation.getMembers().keySet()) {
        members.add(uuid.toString());
      }
      cs.set("Members", members.toArray(new String[members.size()]));
      ca.saveConfig();
    }
  }
  
  public void saveNationMember(NationMember player) {
    if (!isLoading) {
      new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Players", player.getUUID()+".yml").delete();
      ConfigAccessor ca = new ConfigAccessor(plugin, player.getUUID()+".yml", "Players");
      ConfigurationSection cs = ca.getConfig();
      cs.set("Nation", player.getNation().toString());
      if (!player.getPlayerGroups().isEmpty()) {
        HashSet<String> groups = new HashSet<>();
        for (Group group : player.getPlayerGroups()) {
          groups.add(group.toString());
        }
        cs.set("Groups", groups.toArray(new String[groups.size()]));
      }
      for (String language : player.getLanguages().keySet()) {
        cs.set("Languages."+language, player.getLanguages().get(language));
      }
      ca.saveConfig();
    }
  }
  
  public void saveGroup(Group group) {
    if (!isLoading) {
      new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Groups"+String.valueOf(File.separatorChar)+group.getNation().toString(), group.toString()+".yml").delete();
      ConfigAccessor ca = new ConfigAccessor(plugin, group.toString()+".yml", "Groups", group.getNation().toString());
      ConfigurationSection cs = ca.getConfig();
      cs.set("Priority", group.getPriority());
      if (!group.getPrefix().equals("")) cs.set("Prefix", group.getPrefix());
      if (!group.getPermissions().isEmpty()) {
        HashSet<String> list = new HashSet<>();
        for (NationPermission perm : group.getPermissions()) {
          list.add(perm.toString());
        }
        cs.set("Permissions", list.toArray(new String[list.size()]));
      }
      ca.saveConfig();
    }
  }
  
  public void saveEstate(Estate estate) {
    if (!isLoading) {
      new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Estates", estate.toString()+".yml").delete();
      ConfigAccessor ca = new ConfigAccessor(plugin, estate.toString()+".yml", "Estates");
      ConfigurationSection cs = ca.getConfig();
      cs.set("Region", estate.getRegion().getId());
      cs.set("RegionWorld", estate.getRegionWorld().getName());
      cs.set("Spawn.World", estate.getSpawn().getWorld().getName());
      cs.set("Spawn.X", estate.getSpawn().getX());
      cs.set("Spawn.Y", estate.getSpawn().getY());
      cs.set("Spawn.Z", estate.getSpawn().getZ());
      cs.set("Spawn.Yaw", estate.getSpawn().getYaw());
      cs.set("Spawn.Pitch", estate.getSpawn().getPitch());
      ca.saveConfig();
    }
  }
  
  public void delNationMember(NationMember member) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Players", member.getUUID()+".yml").delete();
  }
  
  public void delSavedGroup(Group group) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Groups"+String.valueOf(File.separatorChar)+group.getNation().toString(), group.toString()+".yml").delete();
  }
  
  public void delSavedEstate(Estate estate) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Estates", estate.toString()+".yml").delete();
  }
  
  public WorldGuardPlugin getWorldGuard() {
    return worldGuard;
  }
  
  public NationPlugin getMain() {
    return plugin;
  }
}
