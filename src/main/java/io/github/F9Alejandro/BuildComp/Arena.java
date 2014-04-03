package io.github.F9Alejandro.BuildComp;

import org.bukkit.Location;

//TODO: upon player leaving arena area scoreboard is removed and plot is reset
public class Arena {
	private Location min;
	private Location max;
	private Plot[] plots;
	public Arena(Location pos1,Location pos2,int plotsize) {
		if (pos1.getWorld().equals(pos2.getWorld())==false) {
			throw new IllegalArgumentException("pos1 and pos2 must be in the same world!");
		}
		if (pos1.getBlockX()==(pos2.getBlockX())) {
			throw new IllegalArgumentException("pos1.X cannot equal pos2.X!");
		}
		if (pos1.getBlockZ()==(pos2.getBlockZ())) {
			throw new IllegalArgumentException("pos1.Z cannot equal pos2.Z!");
		}
		if (plotsize<1) {
			throw new IllegalArgumentException("plotsize must be greater than zero!");
		}
		if (Math.abs(pos1.getBlockX()-pos2.getBlockX())<plotsize) {
			throw new IllegalArgumentException("Arena is not big enough to support plot!");
		}
		if (Math.abs(pos1.getBlockZ()-pos2.getBlockZ())<plotsize) {
			throw new IllegalArgumentException("Arena is not big enough to support plot!");
		}
		min = new Location(pos1.getWorld(), Math.min(pos1.getBlockX(), pos2.getBlockX()), 0, Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
		max = new Location(pos1.getWorld(), Math.max(pos1.getBlockX(), pos2.getBlockX()), 256, Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
	
		//TODO create plots
		
		//TODO arena events | timers | etc
	
	}
	public Location getMin() {
		return min;
	}
	public Location getMax() {
		return max;
	}
	public Plot[] getPlots() {
		return plots;
	}
}

