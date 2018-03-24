package com.gmail.ZiomuuSs.Utils;

import java.io.File;
import java.util.HashSet;

import org.bukkit.configuration.ConfigurationSection;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Nation.City;
import com.gmail.ZiomuuSs.Nation.Estate;
import com.gmail.ZiomuuSs.Nation.Group;
import com.gmail.ZiomuuSs.Nation.Language;
import com.gmail.ZiomuuSs.Nation.Nation;
import com.gmail.ZiomuuSs.Nation.NationMember;

public class ConfigLoader {
  private ConfigAccessor msgAccessor;
  private ConfigurationSection config;
  private Main plugin;
  private HashSet<Nation> nations = new HashSet<>(); //list of all nations
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
  private void loadAll() {
    NotInNation = config.getString("NotInNation");
  }
  
  public void saveNation(Nation nation) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Nations", nation.toString()+".yml").delete();
    ConfigAccessor ca = new ConfigAccessor(plugin, nation.toString()+".yml", "Nations");
    ConfigurationSection cs = ca.getConfig();
    //todo
  }
  
  public void saveLanguage(Language language) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Languages", language.toString()+".yml").delete();
    ConfigAccessor ca = new ConfigAccessor(plugin, language.toString()+".yml", "Languages");
    ConfigurationSection cs = ca.getConfig();
    //todo
  }
  
  public void saveNationMember(NationMember player) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Players", player.toString()+".yml").delete();
    ConfigAccessor ca = new ConfigAccessor(plugin, player.toString()+".yml", "Players");
    ConfigurationSection cs = ca.getConfig();
    //todo
  }
  
  public void saveGroup(Group group) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Groups"+String.valueOf(File.separatorChar)+group.getNation().toString(), group.toString()+".yml").delete();
    ConfigAccessor ca = new ConfigAccessor(plugin, group.toString()+".yml", "Groups"+String.valueOf(File.separatorChar)+group.getNation().toString());
    ConfigurationSection cs = ca.getConfig();
    //todo
  }
  
  public void saveEstate(Estate estate) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Estates", estate.toString()+".yml").delete();
    ConfigAccessor ca = new ConfigAccessor(plugin, estate.toString()+".yml", "Estates");
    ConfigurationSection cs = ca.getConfig();
    //todo
  }
  
  public void saveCity(City city) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Cities", city.toString()+".yml").delete();
    ConfigAccessor ca = new ConfigAccessor(plugin, city.toString()+".yml", "Cities");
    ConfigurationSection cs = ca.getConfig();
    //todo
  }
  
  public void delSavedGroup(Group group) {
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Groups"+String.valueOf(File.separatorChar)+group.getNation().toString(), group.toString()+".yml").delete();
  }
}
