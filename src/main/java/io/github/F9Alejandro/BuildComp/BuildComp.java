package io.github.F9Alejandro.BuildComp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class BuildComp extends JavaPlugin implements Listener {
	private HashMap<String, Arena> arenas = new HashMap<String, Arena>();
	private Location pos1;
	private Location pos = null;
	private Double X = pos.getX();
	private Double Y = pos.getY();
	private Double Z = pos.getZ();
	private Location pos2;
	private HashMap<Player, Location> lastLocation = new HashMap<Player, Location>();
	
	
	// TODO get the plot you are at.
	public Plot getPlot(Location location) {
		//TODO get plot
		return null;
	}
	
	// Start of plot protection (We don't need worldguard)
	// Prevents PVP in the arena
	@EventHandler
	private void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (getPlot(player.getLocation())!=null) {
				event.setCancelled(true);
			}
		}
	}
	// Prevents building in other plots or if arena is disabled
	@EventHandler
	private void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();
		Plot plot = getPlot(location);
		if (plot==null) {
			return;
		}
		if (plot.isAllowed(player.getName())) {
			if (plot.getArena().isRunning()) {
				return;
			}
			else {
				msg(player,getmsg("MSG5"));
				event.setCancelled(true);
				return;
			}
		}
		else {
			msg(player,getmsg("MSG6"));
			event.setCancelled(true);
			return;
		}
	}
	// Prevents breaking in other plots or if arena is disabled
	@EventHandler
	private void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();
		Plot plot = getPlot(location);
		if (plot==null) {
			return;
		}
		if (plot.isAllowed(player.getName())) {
			if (plot.getArena().isRunning()) {
				return;
			}
			else {
				msg(player,getmsg("MSG5"));
				event.setCancelled(true);
				return;
			}
		}
		else {
			msg(player,getmsg("MSG6"));
			event.setCancelled(true);
			return;
		}
	}
	// Prevents interacting, or spawning mobs etc in other plots
	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Location location = event.getClickedBlock().getLocation();
		Plot plot = getPlot(location);
		if (plot==null) {
			return;
		}
		if (plot.isAllowed(player.getName())) {
			if (plot.getArena().isRunning()) {
				return;
			}
			else {
				msg(player,getmsg("MSG5"));
				event.setCancelled(true);
				return;
			}
		}
		else {
			msg(player,getmsg("MSG6"));
			event.setCancelled(true);
			return;
		}
	}
	// End of block protection
	
	
	// Returns true if a location is within 2 locations, assuming Location[] contains 2 locations ordered [min,max]
	public boolean isin(Location[] locs,Location loc){
		if (locs[0].getWorld().equals(loc.getWorld())) {
			if (loc.getBlockX() < locs[0].getBlockX()) { return false; }
			if (loc.getBlockX() > locs[1].getBlockX()) { return false; }
			if (loc.getBlockZ() < locs[0].getBlockZ()) { return false; }
			if (loc.getBlockZ() > locs[1].getBlockZ()) { return false; }
			if (loc.getBlockY() < locs[0].getBlockY()) { return false; }
			if (loc.getBlockY() > locs[1].getBlockY()) { return false; }
		}
		else {
			return false;
		}
		return true;
	}
	
	// Checks if a player has a permission
	//  - returns true if console (i.e. player = null)
	//  - returns true if player is op
	//  - supports * node e.g. example.perm.*
    public boolean checkperm(Player player,String perm) {
    	boolean hasperm = false;
    	String[] nodes = perm.split("\\.");
    	
    	String n2 = "";
    	if (player==null) {
    		return true;
    	}
    	else if (player.hasPermission(perm)) {
    		hasperm = true;
    	}
    	else if (player.isOp()==true) {
    		hasperm = true;
    	}
    	else {
    		for(int i = 0; i < nodes.length-1; i++) {
    			n2+=nodes[i]+".";
            	if (player.hasPermission(n2+"*")) {
            		hasperm = true;
            	}
    		}
    	}
		return hasperm;
    }
    
    // Messages a player (null = console) - supports color codes e.g. &a &1
    private void msg(Player player,String mystring) {
    	if (player==null) {
    		getServer().getConsoleSender().sendMessage(colorise(mystring));
    	}
    	else if (player instanceof Player==false) {
    		getServer().getConsoleSender().sendMessage(colorise(mystring));
    	}
    	else {
    		msg(player,colorise(mystring));
    	}

    }
    
    // Used to convert color codes to proper color.
    public String colorise(String mystring) {
    	String[] codes = {"&1","&2","&3","&4","&5","&6","&7","&8","&9","&0","&a","&b","&c","&d","&e","&f","&r","&l","&m","&n","&o","&k"};
    	for (String code:codes) {
    		mystring = mystring.replace(code, "§"+code.charAt(1));
    	}
    	return mystring;
    }
	Timer timer = new Timer ();
	TimerTask mytask = new TimerTask () {
		@Override
	    public void run () {
			for (Arena arena:getArenas()) {
				// Stops an arena if the timer is finished.
				if (arena.isRunning()) {
					long time = System.currentTimeMillis()/1000;
					if (arena.getTimer()<=time) {
						arena.Stop();
					}
				}
				
			}
		}
	};
    
	//Converts a user input to a timestamp e.g. 5w 10d 15h 43m 2s
    public long timetostamp(String input) {
    	try {
    	input = input.toLowerCase().trim();
    	int toadd = 0;
    	if (input.contains("w")) {
    		toadd = 604800*Integer.parseInt(input.split("w")[0].trim());
    	}
    	else if ((input.contains("d"))&&(input.contains("sec")==false)) {
    		toadd = 86400*Integer.parseInt(input.split("d")[0].trim());
    	}
    	else if (input.contains("h")) {
    		toadd = 3600*Integer.parseInt(input.split("h")[0].trim());
    	}
    	else if (input.contains("m")) {
    		toadd = 60*Integer.parseInt(input.split("m")[0].trim());
    	}
    	else if (input.contains("s")) {
    		toadd = Integer.parseInt(input.split("s")[0].trim());
    	}
    	
    	return (System.currentTimeMillis()/1000)+toadd;
    	}
    	catch (Exception e) {
    		return (System.currentTimeMillis()/1000);
    	}
    }
    public long timetosec(String input) {
    	try {
    	input = input.toLowerCase().trim();
    	int toadd = 0;
    	if (input.contains("w")) {
    		toadd = 604800*Integer.parseInt(input.split("w")[0].trim());
    	}
    	else if ((input.contains("d"))&&(input.contains("sec")==false)) {
    		toadd = 86400*Integer.parseInt(input.split("d")[0].trim());
    	}
    	else if (input.contains("h")) {
    		toadd = 3600*Integer.parseInt(input.split("h")[0].trim());
    	}
    	else if (input.contains("m")) {
    		toadd = 60*Integer.parseInt(input.split("m")[0].trim());
    	}
    	else if (input.contains("s")) {
    		toadd = Integer.parseInt(input.split("s")[0].trim());
    	}
    	
    	return toadd;
    	}
    	catch (Exception e) {
    		return 0;
    	}
    }
    
	@Override
	public void onEnable() {
		// Runs the timer every second
		timer.schedule (mytask,0l, 1000);
		
		// Adding version and language to config.yml
		// * Using this method you can update the config without erasing values.
		saveResource("english.yml", true);
		getConfig().options().copyDefaults(true);
		final Map<String, Object> options = new HashMap<String, Object>();
        getConfig().set("version", "0.0.1");
        options.put("language","english");
        for (final Entry<String, Object> node : options.entrySet()) {
       	if (!getConfig().contains(node.getKey())) {
       		getConfig().set(node.getKey(), node.getValue());
       	}
        }
        saveConfig();
        
        getServer().getPluginManager().registerEvents(this,  this);
	}
 
	@Override
	public void onDisable() {
		// Bukkit automatically prints when the plugin is enabled
	}
	
	// Gets a given message from the language file set in the config.yml
	public String getmsg(String key) {
		File yamlFile = new File(getDataFolder(), getConfig().getString("language").toLowerCase()+".yml"); 
		YamlConfiguration.loadConfiguration(yamlFile);
		try {
			return colorise(YamlConfiguration.loadConfiguration(yamlFile).getString(key));
		}
		catch (Exception e){
			return "";
		}
	}
	
	public synchronized void addArena(Arena arena) {
		arenas.put(arena.getKey(),arena);
	}
    public synchronized List<Arena> getArenas() {
    	return new ArrayList<Arena>(arenas.values());
    }
    public synchronized Arena removeArena (Arena arena) {
    	return arenas.remove(arena.getKey());
    }
    public synchronized Arena removeArena (String key) {
    	return arenas.remove(key);
    }
    public synchronized Arena getArena (String key) {
    	return arenas.get(key);
    }
	
	
	// You shouldn't have capitols in a permission node.
	//TODO finish adding all messages to english.yml
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		// Console will cause exception, better to catch.
		
		Player player;
		try {
			player = (Player) sender;
		}
		catch (Exception e) {
			player = null;
		}
		Location location = player.getLocation();
	    if((cmd.getName().equalsIgnoreCase("buco"))||(cmd.getName().equalsIgnoreCase("buildcomp"))||(cmd.getName().equalsIgnoreCase("buildcompetition"))) {
			if (player!=null) {
				if (args.length > 0) {
					
					if(args[0].equalsIgnoreCase("help")||args[0].equalsIgnoreCase("?")){
						msg(player,getmsg("MSG2"));
					} 
					else if(args[0].equalsIgnoreCase("join")){
						//TODO JOIN ARENA
					}
					else if(args[0].equalsIgnoreCase("leave")){
						Location home = player.getBedSpawnLocation();
						if(home != null){
						player.teleport(home);
						msg(player,ChatColor.GREEN + "[Build Manager]: " + ChatColor.RED + "Sending you home! Thanks for player :)");
						}
						else if(home == null){
							home = lastLocation.get(player);
							player.teleport(home);
							msg(player,ChatColor.GREEN + "[Build Manager]: " + ChatColor.RED + "Sending you to last location! Thanks for player :)");
						}
					}
					else if(args[0].equalsIgnoreCase("list")){
						if (checkperm(player,"buco.arena.list")) {
							msg(player,"&7Arenas for '&6BuildComp&7':");
							for(Arena arena : getArenas()){
								if (arena.isOpen()) {
									msg(player,"&7 - &a"+arena.getKey());
								}
								else if (arena.isRunning()) {
									msg(player,"&7 - &9"+arena.getKey());
								}
								else  {
									msg(player,"&7 - "+arena.getKey());
								}
							}
						}
						else {
							msg(player,"no perm");
						}
					}
					else if(args[0].equalsIgnoreCase("start")){
						if (checkperm(player,"buco.arena.start")) {
							if (args[1]!=null) {
								Arena arena = getArena(args[1]);
								if (arena!=null) {
									if (arena.getPeriod()<1) {
										msg(player,"&cWarning, arena will need to be manually stopped OR use /buco settime");
									}
									msg(player,"Starting arena "+args[1]);
									arena.Start();
								}
								else {
									msg(player,"Arena not found\n/buco list");
								}
							}
						}
						else {
							msg(player,"no perm");
						}
					}
					else if(args[0].equalsIgnoreCase("settime")){
						if (checkperm(player,"buco.arena.settime")) {
							if (args.length > 3) {
								try {
									Arena arena = getArena(args[2]);
									String argtime = "";
									for (int i = 3;i<args.length;i++) {
										argtime+=args[i]+" ";
									}
									arena.setPeriod(timetosec(argtime.trim()));
								}
								catch (Exception e) {
									if (getArenas().contains(args[2])) {
										msg(player,"Invalid time period");
									}
									else {
										msg(player,"Invalid arena, use /buco arena list");
									}
								}
							}
							else {
								msg(player,"/buco time <arena> 20m 15s");
							}
						}
						else {
							msg(player,"no perm");
						}
					}
					else if(args[0].equalsIgnoreCase("stop")){
						if (checkperm(player,"buco.arena.stop")) {
							if (args[1]!=null) {
								Arena arena = getArena(args[1]);
								if (arena!=null) {
									msg(player,"Stopped arena "+args[1]);
									arena.Stop();
								}
								else {
									msg(player,"Arena not found\n/buco list");
								}
							}
							else {
								msg(player,"/buco stop <Arena>");
							}
						}
					}
					else if(args[0].equalsIgnoreCase("open")){
						//TODO allow players to join an arena
					}
					else if(args[0].equalsIgnoreCase("remove")){
						if (checkperm(player,"buco.arena.remove")) {
							if (args.length > 1) {
								//TODO remove arena
							}
							else {
								msg(player,"/buco remove <arena>");
							}
						}
						else {
							//TODO no perm message
						}
					}
					else if(args[0].equalsIgnoreCase("setpoint")){
						if(args[1] != null&&args[1] == "1"){
							pos = location;
							pos1 = new Location(player.getWorld(), X, Y, Z);
						}
						else if(args[1] != null&&args[1] == "2"){
							pos = location;
							pos2 = new Location(player.getWorld(), X, Y, Z);
						}
						else if(args[1] != null&&args[1] == "clear"){
							pos1 = null;
							pos2 = null;
							pos = null;
						}
						else{	
							msg(player,"please enter in a point, /buco setpoint 1, 2, clear");
						}
					}
					else if(args[0].equalsIgnoreCase("create")){
						if(checkperm(player,"buco.arena.create")){
							if(args[1] != null&&pos1 != null&&pos2 !=null){
								if (args[2] != null) {
									try {
										Integer.parseInt(args[3]);
									}
									catch (Exception e) {
										msg(player,getmsg("MSG11"));
										return false;
									}
								}
								
								//TODO  worldedit selection and set it to pos1 and pos2
								/**pos1 = new Location(world, x, y, z);
								pos2 = new Location(world, x, y, z);
								pos1 == setpoint 1
								pos2 == setpoint 2**/
								if (getArena(args[1])!=null) {
									msg(player,"Arena already exists");
									return false;
								}
								else {
									//Arena arenas = (new Arena(args[2], pos1, pos2, size, player.getLocation()));
									//TODO put pos1 and pos2 in place of null ^
								}
							}
							else{
								msg(player,"/buco create <Arena> <PlotSize>");
							}
						}
						}
						else {
							//give the unknown command message here as well.
							msg(player,getmsg("MSG1"));
						}
					}
					else {
						// No arguments message
						// You should give the help here, or at least a list of commands
						msg(player,getmsg("MSG1"));
				}
			}
			else {
				msg(player,getmsg("MSG10"));
				// Command sent from console
				// DO stuff
			}
			}
		return false;
		}
}
