package com.dkhenry.minejmx;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class MineJMXPlayerListener extends PlayerListener {

	public static MineJMX plugin;

	public MineJMXPlayerListener(MineJMX instance) {
		plugin = instance;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer() ;

		// Increment The Per Player Stats
		PlayerData playerData = null ;
		if(plugin.playerData.containsKey(player.getName())) {
			playerData = plugin.playerData.get(player.getName()) ;
		} else {
			plugin.log.info("MineJMX Found a new first time Player") ;
			playerData = new PlayerData() ;
			plugin.addPlayer(player.getName(),playerData) ;
			return ;
		}
		playerData.logIn();

		// ...and the server statistics
		plugin.serverData.incNumberOfPlayers() ;
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer() ;
		long playerLoggedInTime;

		// Increment The Per Player Stats
		PlayerData playerData = null ;
		if(plugin.playerData.containsKey(player.getName())) {
			playerData = plugin.playerData.get(player.getName()) ;
		} else {
			plugin.log.info("MineJMX Found an Unregisted Player in a place where an Unregisted Player should not be found") ;
			playerData = new PlayerData() ;
			plugin.addPlayer(player.getName(),playerData) ;
			return ;
		}
		playerLoggedIntime = playerData.logOut();

		// ...and the server statistics
		plugin.serverData.decNumberOfPlayers() ;
		plugin.serverData.incPlayTimeBy(playerLoggedInTime);
	}
}
