package com.gmail.ZiomuuSs.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Nation.Estate;
import com.gmail.ZiomuuSs.Nation.Group;
import com.gmail.ZiomuuSs.Nation.Nation;
import com.gmail.ZiomuuSs.Nation.NationMember;
import com.gmail.ZiomuuSs.Nation.Group.NationPermission;

public class ConfigLoader {
  private ConfigAccessor msgAccessor;
  private ConfigurationSection config;
  private Main plugin;
  //options variables
  public String NotInNation; //name of nation that is always default (if an estate is not belonging to any exiting nation, it belongs to this faction
  
  public ConfigLoader(Main plugin) {
    this.plugin = plugin;
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
    NotInNation = config.getString("NotInNation");
    //todo
    for (File file : new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "Nations").listFiles()) {
      FileConfiguration cs = YamlConfiguration.loadConfiguration(file);
      String name = file.getName();
      name = name.substring(0, name.length() - 4); //remove the .yml
      Nation nation = new Nation(this, name, UUID.fromString(cs.getString("King"))); //remember to add capital after loading estates!
      if (cs.isList("BannedPlayers")) {
        for (String uuid : (List<String>) cs.getList("BannedPlayers")) {
          nation.getBannedPlayers().add(UUID.fromString(uuid));
        }
      }
    }
  }
  
  public void saveNation(Nation nation) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Nations", nation.toString()+".yml").delete();
    ConfigAccessor ca = new ConfigAccessor(plugin, nation.toString()+".yml", "Nations");
    ConfigurationSection cs = ca.getConfig();
    cs.set("Name", nation.toString());
    cs.set("King", nation.getKing().toString());
    cs.set("Capital", 1);
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
  
  public void saveNationMember(NationMember player) {
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
  
  public void saveGroup(Group group) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Groups"+String.valueOf(File.separatorChar)+group.getNation().toString(), group.toString()+".yml").delete();
    ConfigAccessor ca = new ConfigAccessor(plugin, group.toString()+".yml", "Groups"+String.valueOf(File.separatorChar)+group.getNation().toString());
    ConfigurationSection cs = ca.getConfig();
    //todo
    cs.set("Priority", group.getPriority());
    cs.set("Prefix", group.getPrefix());
    List<String> list = new ArrayList<>();
    for (NationPermission perm : group.getPermissions()) {
      list.add(perm.toString());
    }
    cs.set("Permissions", list);
  }
  
  public void saveEstate(Estate estate) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Estates", estate.toString()+".yml").delete();
    ConfigAccessor ca = new ConfigAccessor(plugin, estate.toString()+".yml", "Estates");
    ConfigurationSection cs = ca.getConfig();
    cs.set("Region", estate.getRegion().toString());
    cs.set("RegionWorld", estate.getRegionWorld());
  }
  
  public void delNationMember(NationMember member) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Players", member.getUUID()+".yml").delete();
  }
  
  public void delSavedGroup(Group group) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Groups"+String.valueOf(File.separatorChar)+group.getNation().toString(), group.toString()+".yml").delete();
  }
}
