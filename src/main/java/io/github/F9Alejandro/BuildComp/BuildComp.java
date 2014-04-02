package io.github.F9Alejandro.BuildComp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class BuildComp extends JavaPlugin implements Listener {
	//Short term storing of Variables
	//TODO: use better method of checking/verifying and storing
	public HashMap<String, Integer> mapPlayer = new HashMap<String, Integer>();
	public HashMap<String, Location> arenaLoc = new HashMap<String, Location>();
	public HashMap<String, Integer> arenaTime = new HashMap<String, Integer>();
	public HashMap<Player, Location> lastLocation = new HashMap<Player, Location>();
	public List<String> list = new ArrayList<String>(arenaLoc.keySet());
	private String name;
	
	public String oegetKey(String arena, Location value){
		for(String myarena : arenaLoc.keySet()){
			if(arenaLoc.get(myarena).equals(value)) 
				return myarena;
	}
		name = null;
		return name;
	}
	
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
    
	@Override
	public void onEnable() {
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
	
	// You shouldn't have capitols in a permission node.
	//TODO finish adding all messages to english.yml
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
	    Player player = (Player) sender;
	    Location location = player.getLocation();
	    if((cmd.getName().equalsIgnoreCase("buco"))||(cmd.getName().equalsIgnoreCase("buildcomp"))||(cmd.getName().equalsIgnoreCase("buildcompetition"))) {
			if(args.length == 0){
				// You should give the help here, or at least a list of commands
				msg(player,getmsg("MSG1"));
			} else if(args[0].equalsIgnoreCase("help")||args[0].equalsIgnoreCase("?")){
				msg(player,getmsg("MSG2"));
			} else if(args[0].equalsIgnoreCase("join")){
				Location lastLoc = player.getLocation();
				lastLocation.put(player, lastLoc);
				Location localloc = null;
				arenaLoc.containsKey(args[1]);
				 localloc = arenaLoc.get(args[1]);
				 if(localloc != null){
					 player.teleport(localloc);
					 msg(player,getmsg("MSG3") + args[2]);
				 }
				 else if(args[1] == null){ 
					 localloc = arenaLoc.get("default");
					 if(localloc != null){
						 player.teleport(localloc);
						 msg(player,getmsg("MSG4"));
					 }
				 }
				 else{
					 msg(player,ChatColor.GREEN + "[Build Manager]: " + ChatColor.RED + "Arena Does not exist");
				 }
			}else if(args[0].equalsIgnoreCase("leave")){
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
			}else if(args[0].equalsIgnoreCase("list")){
				for(String s : arenaLoc.keySet()){
					msg(player,s);
				}
				
			}else if(args[0].equalsIgnoreCase("create")){
				if(args[1].equalsIgnoreCase("arena")&&checkperm(player,"buco.arena.create")){
					//TODO use Arena.java class and Plot.java class
					//TODO save arenas so it doesn't delete during a reload
					//
					if(args[2] != null){
						arenaLoc.put(args[2], location);
					}
					else{
						arenaLoc.put("default", location);
					}
				}else if(args[1].equalsIgnoreCase("time")&&checkperm(player,"buco.arena.set.time")){
					int time = 0;
						time = Integer.parseInt(args[3].toString());
						/*String name = args[2];*/
					if(args[2] != null &&args[3] != null){
						oegetKey(args[2], location);
						if(name != null){
						arenaTime.put(name, time);
						}
						else{
							msg(player,ChatColor.GREEN +"[Build Manager]: " + ChatColor.BLUE + "You must enter a valid arena name");	
						}
					}else if(args[2] !=null&&args[3] == null){
						oegetKey(args[2], location);
						if(name != null){
						arenaTime.put(name, null);
						}
						else{
							msg(player,ChatColor.GREEN +"[Build Manager]: " + ChatColor.BLUE + "You must enter a valid arena name");	
						}
					}else if(args[2] == null){
						msg(player,ChatColor.GREEN +"[Build Manager]: " + ChatColor.BLUE + "You must enter a valid arena name and time");
						}
					}
				}
			}
			else if(args[0].equalsIgnoreCase("remove")){
				if(args[1].equalsIgnoreCase("arena")&&checkperm(player,"buco.arena.remove")){
					if(args[2] != null){
						arenaLoc.remove(args[2]);
					}
					else if(true){
						
					}
				}
				
			}
			else{
				msg(player,"&4Invalid Arguments, Please refer to /buco ?");
				}
		return false;
			
		}

}
