package io.github.F9Alejandro.BuildComp;

import java.util.List;

import org.bukkit.Location;
//players get their own plots to work in given by world edit/given permission depending on if there are going to be subplots for each player allowed in the competition
//TODO: create arena area in which the event is held
public class Plot {
	Location min;
	Location max;
	String owner = null;
	List<String> members = null;
	Arena arena;
	public Plot(Arena myarena,Location pos1,Location pos2) {
		arena = myarena;
		min = new Location(pos1.getWorld(), Math.min(pos1.getBlockX(), pos2.getBlockX()), 0, Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
		max = new Location(pos1.getWorld(), Math.max(pos1.getBlockX(), pos2.getBlockX()), 256, Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
	}
	public Arena getArena() {
		return arena;
	}
	public Location getMin() {
		return min;
	}
	public Location getMax() {
		return max;
	}
	public String getOwner() {
		return owner;
	}
	public List<String> getMembers() {
		return members;
	}
	public boolean setOwner(String player) {
		if (player.equals(owner)) {
			return false;
		}
		owner = player;
		return true;
	}
	public boolean addMember(String player) {
		if (members.contains(player)) {
			return false;
		}
		else {
			members.add(player);
			return true;
		}
		
	}
	public boolean isAllowed(String player) {
		if (owner.equals(player)) {
			return true;
		}
		if (members.contains(player)) {
			return true;
		}
		return false;
	}
	public boolean removeMember(String player) {
		if (members.contains(player)) {
			members.remove(player);
			return true;
		}
		return false;
	}
}
