package com.dkhenry.minejmx;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MineJMXPlayerListener implements Listener {

	public static MineJMX plugin;

	public MineJMXPlayerListener(MineJMX instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer() ;
		plugin.persistance.increment("server","numberOfPlayers",1.0);		
		plugin.serverData.incNumberOfPlayers() ;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer() ;
		long playerLoggedInTime;

		// Increment The Per Player Stats
		PlayerData playerData = plugin.getPlayerData(player.getName(), "MineJMX found an unregistered Player in a place where an unregistered Player should not be found");
		playerLoggedInTime = playerData.logOut();

		// ...and the server statistics
		plugin.serverData.decNumberOfPlayers() ;
		plugin.serverData.incPlayTimeBy(playerLoggedInTime);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
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
