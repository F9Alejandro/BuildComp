package io.github.F9Alejandro.BuildComp;

import java.util.List;

import org.bukkit.Location;

public class Plot {
	Location min;
	Location max;
	String owner = null;
	List<String> members = null;
	public Plot(Location pos1,Location pos2) {
		min = new Location(pos1.getWorld(), Math.min(pos1.getBlockX(), pos2.getBlockX()), 0, Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
		max = new Location(pos1.getWorld(), Math.max(pos1.getBlockX(), pos2.getBlockX()), 256, Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
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
	public boolean removeMember(String player) {
		if (members.contains(player)) {
			members.remove(player);
			return true;
		}
		return false;
	}
}
