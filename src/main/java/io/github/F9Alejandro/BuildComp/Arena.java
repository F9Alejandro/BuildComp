package io.github.F9Alejandro.BuildComp;

import java.util.List;

import org.bukkit.Location;


//TODO: upon player leaving arena area scoreboard is removed and plot is reset
public class Arena {
	private Location min;
	private Location max;
	private List<Plot> plots;
	private String key;
	private Location teleport;
	
	private boolean open;
	private boolean running;
	private long timer;
	private long period;
	public Arena(String name, Location pos1,Location pos2,int plotsize, Location spawn) {
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
		open = false;
		running = false;
		teleport = spawn;
		key = name;
		min = new Location(pos1.getWorld(), Math.min(pos1.getBlockX(), pos2.getBlockX()), 0, Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
		max = new Location(pos1.getWorld(), Math.max(pos1.getBlockX(), pos2.getBlockX()), 256, Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
		for (int i = 0;i<((max.getBlockX()-min.getBlockX())/plotsize);i++) {
			for (int j = 0;j<((max.getBlockZ()-min.getBlockZ())/plotsize);j++) {
				plots.add(new Plot(this, min.add(i*plotsize,0,i*plotsize),  min.add((i+1)*plotsize,256,(i+1)*plotsize)));
			}
		}
		//TODO arena events | timers | etc
	
	}
	public void setTimer(Long timestamp) {
		timer = timestamp;
	}
	public Long getTimer() {
		return timer;
	}
	public boolean isOpen() {
		return open;
	}
	public boolean isRunning() {
		return running;
	}
	public void Start(Long timestamp) {
		timer = timestamp;
		open = false;
		running = true;
	}
	public void Start() {
		if (period!=0) {
			timer = (System.currentTimeMillis()/1000)+period;
		}
		open = false;
		running = true;
	}
	public void Stop() {
		if (running) {
			//TODO announce to players when arena is stopped
			//Do voting etc and other post competition tasks
		}
		open = false;
		running = false;
	}
	public void Open() {
		open = true;
		running = false;
	}
	public Location getSpawn() {
		return teleport;
	}
	public void setSpawn(Location spawn) {
		teleport = spawn;
	}
	public Location getMin() {
		return min;
	}
	public Location getMax() {
		return max;
	}
	public List<Plot> getPlots() {
		return plots;
	}
	public boolean addPlot(Plot plot) {
		if (plots.contains(plot)) {
			return false;
		}
		plots.add(plot);
		return true;
	}
	public boolean removePlot(Plot plot) {
		if (plots.contains(plot)) {
			plots.remove(plot);
			return true;
		}
		return false;
	}
	public void setKey(String name) {
		key = name;
	}
	public String getKey() {
		return key;
	}
	public void setPeriod(Long time) {
		period = time;
	}
	public long getPeriod() {
		return period;
	}
}

