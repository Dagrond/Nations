package com.github.Dagrond.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.Dagrond.Nation.Nation;
import com.github.Dagrond.Nation.NationMember;
import com.github.Dagrond.Utils.ConfigLoader;
import com.github.Dagrond.Utils.msg;


public class NationCommand implements CommandExecutor {
  ConfigLoader config;
  
  public NationCommand(ConfigLoader config) {
    this.config = config;
  }
  
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
                  if (!Nation.isNation(args[2])) {
                    new Nation(args[2], config);
                    sender.sendMessage(msg.get("nation_created", true, args[2]));
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
                    @SuppressWarnings("deprecation")
                    OfflinePlayer king = Bukkit.getOfflinePlayer(args[4]);
                    if (king.hasPlayedBefore() || king.isOnline()) {
                      Nation nation = Nation.getNationByString(args[3]);
                      NationMember kingMember = NationMember.getNationMember(king.getUniqueId());
                      if (kingMember.hasNation() && kingMember.getNation().equals(nation)) {
                        //del player from assistants, purge permissions etc
                        kingMember.setNation(nation);
                      } else if (!kingMember.hasNation()) {
                        kingMember.setNation(nation);
                        //send proper message that this player was added to nation first
                        sender.sendMessage(msg.get("error_king_not_in_nation", true, args[4], args[3]));
                      } else
                        sender.sendMessage(msg.get("error_king_in_other_nation", true, args[4], kingMember.getNation().getDisplayName()));
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
                    @SuppressWarnings("deprecation")
                    OfflinePlayer ass = Bukkit.getOfflinePlayer(args[4]);
                    if (ass.hasPlayedBefore() || ass.isOnline()) {
                      NationMember assMember = NationMember.getNationMember(ass.getUniqueId());
                      if (assMember.hasNation() && assMember.getNation().equals(nation)) {
                        //del player from assistants, purge permissions etc
                        assMember.setNation(nation);
                      } else if (!assMember.hasNation()) {
                        assMember.setNation(nation);
                        //send proper message that this player was added to nation first
                        sender.sendMessage(msg.get("error_king_not_in_nation", true, args[4], args[3]));
                      } else
                        sender.sendMessage(msg.get("error_king_in_other_nation", true, args[4], kingMember.getNation().getDisplayName()));
                      sender.sendMessage(msg.get("nation_ass_added", true, args[4], nation.getDisplayName()));
                    } else
                      sender.sendMessage(msg.get("error_ass_has_to_played_before", true, args[4]));
                  } else if (args[4].equalsIgnoreCase("del")) {
                    
                  } else
                    sender.sendMessage(msg.get("error_usage", true, "/at a n assistant <panstwo> <add/del/list> (gracz)"));
                } else
                  sender.sendMessage(msg.get("error_usage", true, "/at a n assistant <panstwo> <add/del/list> (gracz)"));
              } else
                sender.sendMessage(msg.get("error_usage", true, "/n help admin"));
            } else if (args[1].equalsIgnoreCase("reload")) {
              config.getMain().reload(sender);
            } else if (args[1].equalsIgnoreCase("estate") || args[1].equalsIgnoreCase("e")) {
              //todo...
            } else if (args[1].equalsIgnoreCase("member") || args[1].equalsIgnoreCase("m")) {
              
            } else
              sender.sendMessage(msg.get("error_usage", true, "/n help admin"));
          } else
            msg.get("error_permission", true);
        } else
          sender.sendMessage(msg.get("error_usage", true, "/n help admin"));
      } else if (args[0].equalsIgnoreCase("ban")) {
        sender.sendMessage("todo...");
      }
    }
  return true;
  }
  
}