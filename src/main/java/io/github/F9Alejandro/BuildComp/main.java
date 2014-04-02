package io.github.F9Alejandro.BuildComp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class main extends JavaPlugin implements Listener {
	public HashMap<String, Integer> mapPlayer = new HashMap<String, Integer>();
	public HashMap<String, Location> arenaLoc = new HashMap<String, Location>();
	public HashMap<String, Integer> arenaTime = new HashMap<String, Integer>();
	public HashMap<Player, Location> lastLocation = new HashMap<Player, Location>();
	public List<String> list = new ArrayList<String>(arenaLoc.keySet());
	private String name;
	
	public String oegetKey(String arena, Location value){
		for(String arena : arenaLoc.keySet()){
			if(arenaLoc.get(arena).equals(value)) 
				name = arena;
				return name;
	}
		name = null;
		return name;
	}
	@Override
	public void onEnable() {
		getLogger().info("onEnable has been invoked!");
		getServer().getPluginManager().registerEvents(this,  this);
	}
 
	@Override
	public void onDisable() {
		getLogger().info("onDisable has been invoked!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
	    Player player = (Player) sender;
	    Location location = player.getLocation();
		if(commandLabel.equalsIgnoreCase("BuCo")){
			if(args.length == 0){
				player.sendMessage(ChatColor.DARK_RED + "Nonthing here yet!");
			}else if(args[0].equalsIgnoreCase("help")||args[0].equalsIgnoreCase("?")){
				player.sendMessage(ChatColor.BLUE + "Commands: " + ChatColor.AQUA + "help, ? " + ChatColor.DARK_BLUE + "join, leave");
			}else if(args[0].equalsIgnoreCase("join")){
				Location lastLoc = player.getLocation();
				lastLocation.put(player, lastLoc);
				Location localloc = null;
				arenaLoc.containsKey(args[1]);
				 localloc = arenaLoc.get(args[1]);
				 if(localloc != null){
					 player.teleport(localloc);
					 player.sendMessage("[Build Manager]: " + ChatColor.BLUE + "Teleporting you to Build Arena, " + args[2]);
				 }
				 else if(args[1] == null){ 
					 localloc = arenaLoc.get("default");
					 if(localloc != null){
						 player.teleport(localloc);
						 player.sendMessage(ChatColor.GREEN + "[Build Manager]: " + ChatColor.BLUE + "Teleporting you to Build Arena, Default");
					 }
				 }
				 else{
					 player.sendMessage(ChatColor.GREEN + "[Build Manager]: " + ChatColor.RED + "Arena Does not exist");
				 }
			}else if(args[0].equalsIgnoreCase("leave")){
				Location home = player.getBedSpawnLocation();
				if(home != null){
				player.teleport(home);
				player.sendMessage(ChatColor.GREEN + "[Build Manager]: " + ChatColor.RED + "Sending you home! Thanks for player :)");
				}
				else if(home == null){
					home = lastLocation.get(player);
					player.teleport(home);
					player.sendMessage(ChatColor.GREEN + "[Build Manager]: " + ChatColor.RED + "Sending you to last location! Thanks for player :)");
				}
				else{
					player.setHealth(0);
					player.sendMessage(ChatColor.GREEN + "[Build Manager]: " + ChatColor.RED + "Sending you to spawn! Thanks for player :)");
				}
			}else if(args[0].equalsIgnoreCase("list")){
				for(String s : arenaLoc.keySet()){
					player.sendMessage(s);
				}
				
			}else if(args[0].equalsIgnoreCase("set")){
				if(args[1].equalsIgnoreCase("arena")&&player.hasPermission("BuCo.arena.set")){
					if(args[2] != null){
					arenaLoc.put(args[2], location);
					}
					else{
						arenaLoc.put("default", location);
					}
				}else if(args[1].equalsIgnoreCase("time")&&player.hasPermission("BuCo.arena.set.time")){
					int time = 0;
						time = Integer.parseInt(args[3].toString());
						/*String name = args[2];*/
					if(args[2] != null &&args[3] != null){
						oegetKey(args[2], location);
						if(name != null){
						arenaTime.put(name, time);
						}
						else{
							player.sendMessage(ChatColor.GREEN +"[Build Manager]: " + ChatColor.BLUE + "You must enter a valid arena name");	
						}
					}else if(args[2] !=null&&args[3] == null){
						oegetKey(args[2], location);
						if(name != null){
						arenaTime.put(name, null);
						}
						else{
							player.sendMessage(ChatColor.GREEN +"[Build Manager]: " + ChatColor.BLUE + "You must enter a valid arena name");	
						}
					}else if(args[2] == null){
						player.sendMessage(ChatColor.GREEN +"[Build Manager]: " + ChatColor.BLUE + "You must enter a valid arena name and time");
						}
					}
				}
			}
			else if(args[0].equalsIgnoreCase("remove")){
				if(args[1].equalsIgnoreCase("arena")&&player.hasPermission("BuCo.arena.remove")){
					if(args[2] != null){
						arenaLoc.remove(args[2]);
					}
					else if(){
						
					}
				}
				
			}
			else{
				player.sendMessage(ChatColor.DARK_RED + "Invalid Arguments, Please refer to /BuCo ?");
				}
				
		}
		return false;
			
		}

}
