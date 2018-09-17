package com.github.Dagrond.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.Dagrond.Nation.Estate;
import com.github.Dagrond.Nation.Nation;
import com.github.Dagrond.Nation.NationMember;
import com.github.Dagrond.Utils.ConfigLoader;
import com.github.Dagrond.Utils.msg;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;


public class NationCommand implements CommandExecutor {
  ConfigLoader config;
  
  public NationCommand(ConfigLoader config) {
    this.config = config;
  }
  
  @SuppressWarnings("deprecation")
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("Nation") || cmd.getName().equalsIgnoreCase("n")) {
      if (args.length < 1) {
        sender.sendMessage(msg.get("error_help", true));
        return true;
      }
      if (args[0].equalsIgnoreCase("admin") || args[0].equalsIgnoreCase("a")) {
        if (args.length > 2) {
          if (sender.hasPermission("Nations.Admin")) {
            if (args[1].equalsIgnoreCase("nation") || args[1].equalsIgnoreCase("n")) {
              if (args[2].equalsIgnoreCase("create")) {
                if (args.length > 3) {
                  if (!Nation.isNation(args[3])) {
                    new Nation(args[3], config);
                    sender.sendMessage(msg.get("nation_created", true, args[3]));
                  } else
                    sender.sendMessage(msg.get("error_nation_exist", true, args[2]));
                } else
                  sender.sendMessage(msg.get("error_usage", true, "/n a n create <nazwa>"));
              } else if (args[2].equalsIgnoreCase("info")) {
                if (args.length > 3) {
                  if (Nation.isNation(args[3])) {
                    Nation nation = Nation.getNationByString(args[3]);
                    sender.sendMessage(msg.get("nation_admin_info", true, nation.getDisplayName()));
                    sender.sendMessage(msg.get("nation_info_king", false, nation.getKing() == null ? msg.get("none", false) : Bukkit.getOfflinePlayer(nation.getKing()).getName()));
                    sender.sendMessage(msg.get("nation_info_capital", false, nation.getCapital() == null ? msg.get("none", false) : nation.getCapital().toString()));
                    sender.sendMessage(msg.get("nation_info_assistants", false, Integer.toString(nation.getAssistantAmount()), nation.getAssistantsList()));
                    sender.sendMessage(msg.get("nation_info_members", false, Integer.toString(nation.getMemberAmount())));
                    sender.sendMessage(msg.get("nation_info_estates", false, Integer.toString(nation.getEstateAmount()), nation.getEstatesList()));
                    sender.sendMessage(msg.get("nation_info_enemies", false, Integer.toString(nation.getBannedAmount())));
                    sender.sendMessage(msg.get("nation_info_colors", false, "#"+nation.getHEXcolor(), nation.getMCcolor().toString()));
                  } else
                    sender.sendMessage(msg.get("error_not_a_nation", true, args[3]));
                } else
                  sender.sendMessage(msg.get("error_usage", true, "/n a n info <panstwo>"));
              } else if (args[2].equalsIgnoreCase("king")) {
                if (args.length > 4) {
                  if (Nation.isNation(args[3])) {
                    OfflinePlayer king = Bukkit.getOfflinePlayer(args[4]);
                    if (king.hasPlayedBefore() || king.isOnline()) {
                      Nation nation = Nation.getNationByString(args[3]);
                      NationMember kingMember = NationMember.getNationMember(king.getUniqueId());
                      if (kingMember.hasNation() && kingMember.getNation().equals(nation)) {
                        if (kingMember.isKing()) {
                          sender.sendMessage(msg.get("error_already_king", true, args[4]));
                          return true;
                        }
                        //del player from assistants, purge permissions etc
                        kingMember.setNation(nation);
                        nation.setKing(Bukkit.getOfflinePlayer(args[4]).getUniqueId());
                      } else if (!kingMember.hasNation()) {
                        kingMember.setNation(nation);
                        //send proper message that this player was added to nation first
                        sender.sendMessage(msg.get("error_king_not_in_nation", true, args[4], args[3]));
                        nation.setKing(Bukkit.getOfflinePlayer(args[4]).getUniqueId());
                      } else {
                        sender.sendMessage(msg.get("error_king_in_other_nation", true, args[4], args[3]));
                        return true;
                      }
                      sender.sendMessage(msg.get("nation_king_setted", true, args[4], nation.getDisplayName()));
                    } else
                      sender.sendMessage(msg.get("error_king_has_to_played_before", true, args[4]));
                  } else
                    sender.sendMessage(msg.get("error_not_a_nation", true, args[3]));
                } else 
                  sender.sendMessage(msg.get("error_usage", true, "/n a n king <panstwo> <krol>"));
              } else if (args[2].equalsIgnoreCase("list")) {
                sender.sendMessage(msg.get("nation_list", true, Integer.toString(Nation.getNationAmount()), Nation.getNationsList()));
              } else if (args[2].equalsIgnoreCase("assistant")) {
                if (!Nation.isNation(args[3])) {
                  sender.sendMessage(msg.get("error_not_a_nation", true, args[3]));
                  return true;
                }
                Nation nation = Nation.getNationByString(args[3]);
                if (args.length > 4) {
                  if (args[4].equalsIgnoreCase("list")) {
                    sender.sendMessage(msg.get("nation_info_assistants", true, Integer.toString(nation.getAssistantAmount()), nation.getAssistantsList()));
                  } else if (args[4].equalsIgnoreCase("add")) {
                    OfflinePlayer ass = Bukkit.getOfflinePlayer(args[4]);
                    if (ass.hasPlayedBefore() || ass.isOnline()) {
                      NationMember assMember = NationMember.getNationMember(ass.getUniqueId());
                      if (assMember.hasNation() && assMember.getNation().equals(nation)) {
                        if (assMember.isAssistant() || assMember.isKing()) {
                          sender.sendMessage(msg.get("error_already_assistant", true, args[4]));
                          return true;
                        }
                        //purge permissions etc
                        assMember.setNation(nation);
                        nation.addAssistant(Bukkit.getOfflinePlayer(args[4]).getUniqueId());
                      } else {
                        sender.sendMessage(msg.get("error_assistant_in_other_nation", true, args[4], args[3]));
                        return true;
                      }
                      sender.sendMessage(msg.get("nation_ass_added", true, args[4], nation.getDisplayName()));
                    } else
                      sender.sendMessage(msg.get("error_ass_has_to_played_before", true, args[4]));
                  } else if (args[4].equalsIgnoreCase("del")) {
                    OfflinePlayer ass = Bukkit.getOfflinePlayer(args[4]);
                    if (ass.hasPlayedBefore() || ass.isOnline()) {
                      NationMember assMember = NationMember.getNationMember(ass.getUniqueId());
                      if (assMember.hasNation() && assMember.getNation().equals(nation)) {
                        if (assMember.isAssistant()) {
                          nation.delAssisstant(Bukkit.getOfflinePlayer(args[4]).getUniqueId());
                          sender.sendMessage(msg.get("nation_ass_deleted", true, args[4], nation.getDisplayName()));
                          return true;
                        } else {
                          sender.sendMessage(msg.get("error_not_assistant", true, args[4]));
                          return true;
                        }
                      } else
                        sender.sendMessage(msg.get("error_assistant_in_other_nation", true, args[4]));
                      sender.sendMessage(msg.get("nation_ass_added", true, args[4], nation.getDisplayName()));
                    } else
                      sender.sendMessage(msg.get("error_ass_has_to_played_before", true, args[4]));
                  } else
                    sender.sendMessage(msg.get("error_usage", true, "/at a n assistant <panstwo> <add/del/list> (gracz)"));
                } else
                  sender.sendMessage(msg.get("error_usage", true, "/at a n assistant <panstwo> <add/del/list> (gracz)"));
              } else
                sender.sendMessage(msg.get("error_usage", true, "/n help admin"));
            } else if (args[1].equalsIgnoreCase("reload")) {
              config.getMain().reload(sender);
            } else if (args[1].equalsIgnoreCase("estate") || args[1].equalsIgnoreCase("e")) {
              if (args.length > 2) {
                if (args[2].equalsIgnoreCase("info")) {
                  if (args.length > 3) {
                    if (Estate.isEstate(args[3])) {
                      Estate estate = Estate.getEstate(args[3]);
                      sender.sendMessage(msg.get("estate_info", true, (estate.getNation() != null ? estate.getNation().getMCcolor() : ChatColor.GRAY)+args[3]));
                      sender.sendMessage(msg.get("estate_info_nation", false, (estate.getNation() != null ? estate.getNation().getDisplayName() : msg.get("raw_not_claimed", false))));
                      sender.sendMessage(msg.get("estate_info_region", false, estate.getRegion().toString(), estate.getWorld().getName()));
                      Location loc = estate.getSpawn();
                      sender.sendMessage(msg.get("estate_info_spawn", false, msg.get("loc", false, Double.toString(loc.getX()), Double.toString(loc.getY()), Double.toString(loc.getZ()), loc.getWorld().getName())));
                      sender.sendMessage("todo...");
                    } else
                      sender.sendMessage(msg.get("error_estate_not_exist", true, args[3]));
                  } else
                    sender.sendMessage(msg.get("error_usage", true, "/n a e info (nazwa)"));
                } else if (args[2].equalsIgnoreCase("list")) {
                  sender.sendMessage(msg.get("estates_list", true, Estate.getFullList()));
                } else if (args[2].equalsIgnoreCase("add")) {
                  if (sender instanceof Player) {
                    if (args.length > 4) {
                      if (!Estate.isEstate(args[3])) {
                        Player player = (Player) sender;
                        ProtectedRegion region = config.getWorldGuard().getRegionManager(player.getWorld()).getRegion(args[4]);
                        if (region != null) {
                          if (region instanceof ProtectedPolygonalRegion) {
                            Location loc = player.getLocation();
                            if (WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc).getRegions().contains(region)) {
                              new Estate(args[3], loc, (ProtectedPolygonalRegion) region, config);
                              sender.sendMessage(msg.get("estate_created", true, args[3], args[4], player.getWorld().getName(),
                              msg.get("loc", false, Double.toString(loc.getX()), Double.toString(loc.getY()), Double.toString(loc.getZ()), loc.getWorld().getName())));
                            } else 
                              sender.sendMessage(msg.get("error_not_in_region" , true, args[3]));
                          } else 
                            sender.sendMessage(msg.get("error_no_polygonal", true, args[4], player.getWorld().getName()));
                        } else
                          sender.sendMessage(msg.get("error_no_region", true, args[4], player.getWorld().getName()));
                      } else
                        sender.sendMessage(msg.get("error_already_estate", true, args[3]));
                    } else
                      sender.sendMessage(msg.get("error_usage", true, "/n a e add (nazwa) (region)"));
                  } else
                    sender.sendMessage(msg.get("error_must_be_a_player", true));
                } else if (args[2].equalsIgnoreCase("del")) {
                  if (args.length > 3) {
                    if (Estate.isEstate(args[3])) {
                      Estate.getEstate(args[3]).delete();
                      sender.sendMessage(msg.get("estate_deleted", true, args[3]));
                    } else
                      sender.sendMessage(msg.get("error_estate_not_exist", true, args[3]));
                  } else
                    sender.sendMessage(msg.get("error_usage", true, "/n a e del (nazwa)"));
                } else if (args[2].equalsIgnoreCase("give")) {
                  if (args.length > 4) {
                    if (Estate.isEstate(args[3])) {
                      if (Nation.isNation(args[4])) {
                        Estate estate = Estate.getEstate(args[3]);
                        Nation nation = Nation.getNationByString(args[4]);
                        if (!nation.estateBelongs(estate)) {
                          if (estate.getNation() != null) {
                            sender.sendMessage(msg.get("error_estate_belongs_to_other_nation", true, args[3], estate.getNation().getDisplayName()));
                            estate.clearAffiliation();
                          }
                          nation.addEstate(estate);
                          estate.setNation(nation);
                          sender.sendMessage(msg.get("estate_joined_nation", true, args[3], nation.getDisplayName()));
                        } else
                          sender.sendMessage(msg.get("error_estate_already_belongs", true, args[3], nation.getDisplayName()));
                      } else
                        sender.sendMessage(msg.get("error_not_a_nation", true, args[4]));
                    } else 
                      sender.sendMessage(msg.get("error_estate_not_exist", true, args[3]));
                  } else
                    sender.sendMessage(msg.get("error_usage", true, "/n a e give (prowincja) (panstwo)"));
                } else
                  sender.sendMessage(msg.get("error_usage", true, "/n help admin"));
              } else
                sender.sendMessage(msg.get("error_usage", true, "/n a e info/list/add/del/give <nazwa>"));
            } else if (args[1].equalsIgnoreCase("member") || args[1].equalsIgnoreCase("m")) {
              
            } else
              sender.sendMessage(msg.get("error_usage", true, "/n help admin"));
          } else
            msg.get("error_permission", true);
        } else
          sender.sendMessage(msg.get("error_usage", true, "/n help admin"));
      } else if (args[0].equalsIgnoreCase("ban")) {
        sender.sendMessage("todo...");
      } else if (args[0].equalsIgnoreCase("help")) {
        sender.sendMessage(">pluginy Oskara");
        sender.sendMessage(">jakas strona pomocy na kiju");
      } else {
        sender.sendMessage(msg.get("error_usage", true, "/n help"));
      }
    }
  return true;
  }
  
}