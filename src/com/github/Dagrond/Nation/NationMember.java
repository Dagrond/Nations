package com.github.Dagrond.Nation;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.github.Dagrond.Utils.ConfigLoader;
import com.github.Dagrond.Utils.Msg;

public class NationMember {

	/*
	 * Permissions: - jail - player can jail others player - kick - player can kick
	 * others from nation - ban - player can ban player from nation (nation
	 * recognises banned player as enemy, even if he's without nation) -
	 * permission_view - player can view permission of other players -
	 * permission_manage - player can manage permissions of nations members that are
	 * lower priority than himself todo
	 */
	public enum NationPerm {
		JAIL, KICK, BAN, PERMISSION_VIEW, PERMISSION_MANAGE; // todo: a lot of perms
	}

	private static HashSet<NationMember> members = new HashSet<>(); // NationMember instances of currently online players
	private static ConfigLoader config;
	private UUID player; // UUID of current member
	private Nation nation = null; // nation of this player
	private int priority = 0; // priority of this player
	private HashSet<NationPerm> permissions = new HashSet<>(); // permissions of this player

	public NationMember(UUID player) {
		this.player = player;
		addOnlineMember(this);
	}

	public void save() {
		config.saveNationMember(this);
	}

	// setters
	public void setPriority(int priority) {
		this.priority = priority;
		save();
	}

	public void setNation(Nation nation) {
		priority = 0;
		permissions.clear();
		if (this.nation != null)
			this.nation.delMember(player);
		this.nation = nation;
		nation.addMember(player);
		save();
	}

	public void addPermission(NationPerm permission) {
		permissions.add(permission);
		save();
	}

	public void clearPermissions() {
		permissions.clear();
		save();
	}

	public void delPermission(NationPerm permission) {
		permissions.remove(permission);
		save();
	}

	// booleans
	public boolean hasPermission(NationPerm permission) {
		return permissions.contains(permission);
	}

	public boolean hasNation() {
		return nation != null;
	}

	public boolean isKing() {
		return nation != null && nation.getKing().equals(player);
	}

	public boolean isAssistant() {
		return nation != null && nation.getAssistants().contains(player);
	}

	// getters
	@Override
	public String toString() {
	  return Bukkit.getOfflinePlayer(player).getName();
	}
	
	public UUID getUUID() {
		return player;
	}

	public Nation getNation() {
		return nation;
	}

	public int getPriority() {
		return priority;
	}

	public String getPermissionsList() {
		String list = "";
		for (NationPerm perm : permissions) {
			list += perm.toString() + ", ";
		}
		if (!list.equalsIgnoreCase(""))
			return list.substring(0, list.length() - 2);
		else
			return Msg.get("none", false);
	}

	public HashSet<NationPerm> getPermissions() {
		return permissions;
	}

	// Static
	public static void addOnlineMember(NationMember member) {
		if (!members.contains(member))
		  members.add(member);
	}
	
	public static String getOnlineMembersList() {
	  String list = "";
	  for (NationMember member : members) {
	    if (member == null) Bukkit.getLogger().info("DUPAAAAAAAAAA");
	    list += member.toString() + ", ";
	  }
	  if (!list.equalsIgnoreCase(""))
	    return list.substring(0, list.length() - 2);
	  else
	    return Msg.get("none", false);
	}
	
	public static void purge() {
	  members.clear();
	}

	public static void delOnlineMember(NationMember member) {
		members.remove(member);
	}

	public static void updateConfig(ConfigLoader nconfig) {
		config = nconfig;
	}

	public static NationMember getNationMember(UUID player) {
		for (NationMember member : members) {
			if (player.equals(member.getUUID()))
				return member;
		}
		return config.loadMember(player);
	}
}
