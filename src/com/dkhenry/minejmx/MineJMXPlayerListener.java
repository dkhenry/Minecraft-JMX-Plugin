package com.dkhenry.minejmx;

import org.bukkit.Location;
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
			plugin.log.info("MineJMX Found an Unregistered Player in a place where an Unregistered Player should not be found") ;
			playerData = new PlayerData() ;
			plugin.addPlayer(player.getName(),playerData) ;
		}
		playerLoggedInTime = playerData.logOut();

		// ...and the server statistics
		plugin.serverData.decNumberOfPlayers() ;
		plugin.serverData.incPlayTimeBy(playerLoggedInTime);
	}

	@Override public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location from = event.getFrom(), to = event.getTo();
		double distance = Math.sqrt(Math.pow(to.getX() - from.getX(), 2) + Math.pow(to.getY() - from.getY(), 2) + Math.pow(to.getZ() - from.getZ(), 2));

		// Increment the per-Player stats
		PlayerData playerData = plugin.getPlayerData(player.getName(), "MineJMX found an unregsitered Player in a place where an unregistered Player should not be found");
		playerData.incDistanceMovedBy(distance);

		// Increment the server stats
		plugin.serverData.incPlayerDistanceMovedBy(distance);
	}
}
