/**
 * 
 */
package io.github.F9Alejandro.BuildComp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * @author Alexander
 *
 */
public class Scoreboards extends JavaPlugin implements Listener  {
	Scoreboard scoreboard = null;
	Objective objective = null;
	Team team = null;
	
//TODO: create scoreboard for each arena with countdown that is global for that scoreboard
//TODO: create timer to work with scoreboard and some how show the player that has won the build competition
public void scoreManager(){
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		objective = scoreboard.registerNewObjective("arena", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.GREEN + "[Build Manager]");
	}
//when arena is created team is created with its name
public void addArena(String name){
	team = scoreboard.registerNewTeam(name);
	team.setCanSeeFriendlyInvisibles(true);
	team.setAllowFriendlyFire(false);
	
}
//when player joins team of arena name is joined
public void addPlayer(Player pl, String arena){
	team = scoreboard.getTeam(arena);
	team.addPlayer(pl);
	updateScoreboard();
}
//When player leaves team of arena name is left
public void removePlayer(Player pl, String arena){
	team = scoreboard.getTeam(arena);
	team.removePlayer(pl);
	updateScoreboard();
}
//updates the scoreboard
public void updateScoreboard(){
	for(Player player : Bukkit.getOnlinePlayers())
		player.setScoreboard(scoreboard);
}
}
