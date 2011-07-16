package com.dkhenry.minejmx;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class MineJMXBlockListener extends BlockListener {
	public static MineJMX plugin;

	public MineJMXBlockListener(MineJMX instance) {
		plugin = instance;
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event){

		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material mat = block.getType();

		// Increment The Per block Stats
		BlockData blockData = null ;
		if(plugin.blockData.containsKey(mat)) {
			 blockData = plugin.blockData.get(mat) ;
		} else {
			blockData = new BlockData() ;
			plugin.addBlock(mat,blockData) ;
		}
		blockData.incBlocksPlaced() ;

		// Increment The Per Server Stats
		plugin.serverData.incBlocksPlaced() ;

		// Increment The Per Player Stats
		PlayerData playerData = null ;
		if(plugin.playerData.containsKey(player.getName())) {
			playerData = plugin.playerData.get(player.getName()) ;
		} else {
			plugin.log.info("MineJMX Found a Player Event For an unrecongnised Player ( This Shouldn't happen )") ;
			playerData = new PlayerData() ;
			plugin.addPlayer(player.getName(),playerData) ;
		}
		playerData.incBlocksPlaced() ;

	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {

		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material mat = block.getType();

		// Increment The Per block Stats
		BlockData blockData = null ;
		if(plugin.blockData.containsKey(mat)) {
			 blockData = plugin.blockData.get(mat) ;
		} else {
			blockData = new BlockData() ;
			plugin.addBlock(mat,blockData) ;
		}
		blockData.incBlocksDestroyed() ;

		// Increment The Per Server Stats
		plugin.serverData.incBlocksDestroyed() ;

		// Increment The Per Player Stats
		PlayerData playerData = null ;
		if(plugin.playerData.containsKey(player.getName())) {
			playerData = plugin.playerData.get(player.getName()) ;
		} else {
			plugin.log.info("MineJMX Found a Player Event For an unrecongnised Player ( This Shouldn't happen )") ;
			playerData = new PlayerData() ;
			plugin.addPlayer(player.getName(),playerData) ;
		}
		playerData.incBlocksDestroyed() ;

	}
}
