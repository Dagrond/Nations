package com.gmail.ZiomuuSs.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Nation.Estate;
import com.gmail.ZiomuuSs.Nation.Group;
import com.gmail.ZiomuuSs.Nation.Group.NationPermission;
import com.gmail.ZiomuuSs.Nation.Nation;
import com.gmail.ZiomuuSs.Nation.NationMember;
import com.gmail.ZiomuuSs.Utils.ConfigLoader;
import com.gmail.ZiomuuSs.Utils.msg;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;


public class NationCommand implements CommandExecutor {
  ConfigLoader config;
  
  public NationCommand(ConfigLoader config) {
    this.config = config;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("Nation") || cmd.getName().equalsIgnoreCase("n")) {
      Player player;
      if (sender instanceof Player)
        player = (Player) sender;
      else {
        sender.sendMessage(msg.get("error_player_needed", true));
        return true;
      }
      Condition c = new Condition(player, config);
      if (args.length<1) {
        sender.sendMessage(msg.get("error_usage", true, "/n help"));
        return true;
      }
      switch (args[0].toLowerCase()) {
      case "admin":
        if (!c.hasPermission("Nations.admin", "Nations.*")) return true;
        if (args.length>1) {
          switch (args[1]) {
          case "create":
            if (!c.hasPermission("Nations.create", "Nations.*")) return true;
            if (args.length>4) {
              if (!c.isNotNation(args[2])) return true;
              if (!c.notMemberofNation(args[3])) return true;
              if (!c.isEstate(args[4])) return true;
              if (!c.isFreeEstate(args[4])) return true;
              new Nation(config, args[2], args[3], Estate.getEstateByName(args[4]));
              sender.sendMessage(msg.get("nation_created", true, args[2], args[3]));
            } else {
              sender.sendMessage(msg.get("error_usage", true, "/n admin create <name> <king> <capital>"));
            }
            break;
          case "color":
            if (args.length>3) {
              if (!c.isNation(args[2])) return true;
              if (!c.isHex(args[3])) return true;
              Nation.getNationByName(args[2]).setColor(Integer.parseInt(args[3].substring(1), 16));
              player.sendMessage(msg.get("nation_color_setted", true, args[2], args[3])); //todo: save color to file and load it
            } else {
              player.sendMessage(msg.get("error_usage", true, "/n admin color <nation> <color>"));
            }
            break;
          case "estate":
            if (args.length>2) {
              switch (args[2].toLowerCase()) {
              case "add":
                if (args.length>4) {
                  if (!c.isNotEstate(args[3])) return true;
                  if (!c.isPolygonalRegion(args[4], player.getWorld())) return true;
                  ProtectedPolygonalRegion region = (ProtectedPolygonalRegion) config.getWorldGuard().getRegionManager(player.getWorld()).getRegion(args[4]);
                  if (c.isAlreadyInEstate(region, player.getWorld())) return true;
                  new Estate(args[3], region, player.getWorld(), config);
                  player.sendMessage(msg.get("estate_added", true, args[3]));
                } else {
                  player.sendMessage(msg.get("error_usage", true, "/n admin estate add <name> <region>"));
                }
                break;
              case "del":
                if (args.length>3) {
                  if (!c.isEstate(args[3])) return true;
                  Estate est = Estate.getEstateByName(args[3]);
                  if (!c.isNotCapital(est)) return true;
                  est.del();
                  player.sendMessage(msg.get("estate_removed", true, args[3]));
                } else {
                  player.sendMessage(msg.get("error_usage", true, "/n admin estate del <name>"));
                }
                break;
              case "list":
                String list = "";
                for (Estate estate : Estate.getEstates()) {
                  list+= estate.toString()+", ";
                }
                if (!list.equalsIgnoreCase(""))
                  list = list.substring(0, list.length() - 2);
                else
                  list = msg.get("none", false);
                player.sendMessage(msg.get("estate_list", true, list));
                break;
              case "info":
                if (args.length>3) {
                  if (!c.isEstate(args[3])) return true;
                  Estate e = Estate.getEstateByName(args[3]);
                  player.sendMessage(msg.get("estate_info_name", true, e.toString()));
                  player.sendMessage(msg.get("estate_info_nation", true, e.getNation() == null ? msg.get("not_assigned", false) : e.getNation().toString()));
                } else {
                  player.sendMessage(msg.get("error_usage", true, "/n admin estate info <name>"));
                }
                break;
              default:
                player.sendMessage(msg.get("error_usage", true, "/n admin estate add/del/list/info"));
                break;
              }
            } else {
              player.sendMessage(msg.get("error_usage", true, "/n admin estate add/del/list/info"));
            }
            break;
          case "reload":
            config.getMain().reload(sender);
            break;
          default:
            player.sendMessage(msg.get("error_usage", true, "/n admin <create/estate/reload>"));
            break;
          }
        } else {
          player.sendMessage(msg.get("error_usage", true, "/n admin <create/estate/reload>"));
        }
        break;
      case "rank":
        if (!c.hasNation()) return true;
        if (!c.isKing()) return true;
        if (args.length>2 && !args[1].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("list")) {
          Group group = Group.matchGroup(args[1], Nation.getPlayerNation(player.getUniqueId()));
          if (group == null) {
            sender.sendMessage(msg.get("error_group_not_exist", true, args[1]));
            return true;
          }
          switch (args[2].toLowerCase()) {
          case "perm":
            if (args.length>3) {
              switch (args[3].toLowerCase()) {
              case "listall":
                String list = "";
                for (NationPermission g : NationPermission.values()) {
                  list+= g.toString()+", ";
                }
                if (!list.equalsIgnoreCase(""))
                  list = list.substring(0, list.length() - 2);
                else
                  list = msg.get("none", false);
                player.sendMessage(msg.get("nation_group_allpermlist", true, list));
                break;
              case "list":
                String l = "";
                for (NationPermission g : group.getPermissions()) {
                  l+= g.toString()+", ";
                }
                if (!l.equalsIgnoreCase(""))
                  l = l.substring(0, l.length() - 2);
                else
                  l = msg.get("none", false);
                player.sendMessage(msg.get("nation_group_permlist", true, args[1], l));
                break;
              case "add":
                if (args.length>4) {
                  if (!c.isPermission(args[4])) return true;
                  if (!c.hasNationPermission(group, NationPermission.valueOf(args[4]))) return true;
                  group.addPermission(args[4]);
                  player.sendMessage(msg.get("nation_group_permission_added", true, args[4], args[1]));
                  return true;
                } else {
                  player.sendMessage(msg.get("error_usage", true, "/n rank "+args[1]+" perm add <perm>"));
                }
                break;
              case "del":
                if (args.length>4) {
                  if (!c.isPermission(args[4])) return true;
                  if (!c.hasNationPermission(group, NationPermission.valueOf(args[4]))) return true;
                  group.addPermission(args[4]);
                  player.sendMessage(msg.get("nation_group_permission_deleted", true, args[4], args[1]));
                  return true;
                } else {
                  player.sendMessage(msg.get("error_usage", true, "/n rank "+args[1]+" perm del <perm>"));
                }
                break;
              default:
                player.sendMessage(msg.get("error_usage", true, "/n rank <rank> perm list/listall/add/del"));
                break;
              }
            } else
              player.sendMessage(msg.get("error_usage", true, "/n rank <rank> perm list/listall/add/del"));
            break;
          case "del":
            group.delGroup();
            player.sendMessage(msg.get("nation_group_deleted", true, args[1]));
            break;
          case "promote":
            if (args.length>3) {
              if (!c.isInSameNation(args[3])) return true;
              if (!c.hasNotGroup(group, args[3])) return true;
              NationMember.matchMemberByUUID(Bukkit.getPlayer(args[3]).getUniqueId()).addToGroup(group);
              player.sendMessage(msg.get("nation_group_player_added", true, args[1], args[3]));
            } else
              player.sendMessage(msg.get("error_usage", true, "/n rank <rank> promote <player>"));
            break;
          case "demote":
            if (args.length>3) {
              if (!c.isInSameNation(args[3])) return true;
              if (!c.hasGroup(group, args[3])) return true;
              NationMember.matchMemberByUUID(Bukkit.getPlayer(args[3]).getUniqueId()).kickFromGroup(group);
              player.sendMessage(msg.get("nation_group_player_deleted", true, args[1], args[3]));
            } else
              player.sendMessage(msg.get("error_usage", true, "/n rank <rank> demote <player>"));
            break;
          case "priority":
            if (args.length>3) {
              if (!c.isInt(args[3])) return true;
              int priority = Integer.valueOf(args[3]);
              if (priority>= 0) {
                group.setPriority(priority);
                player.sendMessage(msg.get("nation_priority_setted", true, args[1], args[3]));
              } else {
                player.sendMessage(msg.get("error_must_be_greater_than", true, "priority", "0"));
              }
            } else
              player.sendMessage(msg.get("error_usage", true, "/n rank <rank> priority <priority>"));
            break;
          case "prefix":
            if (args.length>3) {
              group.setPrefix(args[3]);
              player.sendMessage(msg.get("nation_prefix_setted", true, args[1], args[3]));
            } else
              player.sendMessage(msg.get("error_usage", true, "/n rank <rank> prefix <prefix>"));
            break;
          default:
            sender.sendMessage(msg.get("error_usage", true, "/n rank <rank> <perm/del/promote/demote/prefix/info/priority>"));
            break;
          }
        } else {
          switch (args[1].toLowerCase()) {
          case "list":
            String list = "";
            for (Group g : Group.getGroups()) {
              list+= g.toString()+", ";
            }
            if (!list.equalsIgnoreCase(""))
              list = list.substring(0, list.length() - 2);
            else
              list = msg.get("none", false);
            player.sendMessage(msg.get("nation_group_list", true, list));
            break;
          case "add":
            if (args.length>2) {
              Group group = Group.matchGroup(args[2], Nation.getPlayerNation(player.getUniqueId()));
              if (group == null) {
                if (!args[2].equalsIgnoreCase("list") && !args[2].equalsIgnoreCase("listall") && !args[2].equalsIgnoreCase("add")) {
                  new Group(args[2], Nation.getPlayerNation(player.getUniqueId()), config);
                  player.sendMessage(msg.get("nation_group_created", true, args[2]));
                  return true;
                } else {
                  player.sendMessage(msg.get("error_banned_name", true, args[2]));
                }
              } else {
                player.sendMessage(msg.get("error_group_exist", true, args[2]));
              }
            } else {
              sender.sendMessage(msg.get("error_usage", true, "/n rank add <name>"));
            }
            break;
          default:
            sender.sendMessage(msg.get("error_usage", true, "/n rank <rank> <list/add>"));
          }
        break;
        }
      case "info":
        Nation nation = null;
        if (args.length>1) {
          if (!c.isNation(args[1])) return true;
          nation = Nation.getNationByName(args[1]);
        } else  {
          if (!c.hasNation()) return true;
          nation = Nation.getPlayerNation(player.getUniqueId());
        }
        player.sendMessage(msg.get("nation_info_name", true, nation.toString()));
        player.sendMessage(msg.get("nation_info_capital", false, nation.getCapital().toString()));
        player.sendMessage(msg.get("nation_info_king", false, Bukkit.getOfflinePlayer(nation.getKing()).getName()));
        player.sendMessage(msg.get("nation_info_members", false, Integer.toString(nation.getMembers().size())));
        player.sendMessage(msg.get("nation_info_estates", false, Integer.toString(nation.getEstates().size())));
        break;
      case "join":
        if (args.length>1) {
          if (!c.isNation(args[1])) return true;
          if (!c.hasNoNation()) return true;
          Nation n = Nation.getNationByName(args[1]);
          if (!c.checkIfCanJoin(n)) return true;
          if (c.isBannedFromNation(n)) return true;
          n.addMember(player);
          player.sendMessage(msg.get("nation_joined", true, args[1]));
        } else {
          player.sendMessage(msg.get("error_usage", true, "/n join <nation>"));
        }
        break;
      case "ban":
        //todo
        break;
      case "list":
        //todo
        break;
      default:
        sender.sendMessage(msg.get("error_usage", true, "/n help"));
      }
      return true;
    }
  return true;
  }
  
}