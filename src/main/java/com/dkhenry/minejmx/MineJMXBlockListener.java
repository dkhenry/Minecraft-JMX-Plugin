package com.dkhenry.minejmx;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class MineJMXBlockListener extends BlockListener {
	public static MineJMX plugin;

	public MineJMXBlockListener(MineJMX instance) {
		plugin = instance;
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material mat = block.getType();

		// Increment The Per Block Stats
		BlockData blockData = plugin.getBlockData(mat,"");
		blockData.incBlocksPlaced() ;

		// Increment The Per Server Stats
		plugin.serverData.incBlocksPlaced() ;

		// Increment The Per Player Stats
		PlayerData playerData = plugin.getPlayerData(player.getName(), "MineJMX found a Player event For an unrecognized Player (this shouldn't happen)");
		playerData.incBlocksPlaced() ;
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material mat = block.getType();

		// Increment The Per block Stats
		BlockData blockData = plugin.getBlockData(mat,"");
		blockData.incBlocksDestroyed() ;

		// Increment The Per Server Stats
		plugin.serverData.incBlocksDestroyed() ;

		// Increment The Per Player Stats
		PlayerData playerData = plugin.getPlayerData(player.getName(), "MineJMX found a Player event for an unrecognized Player (tThis shouldn't happen)");
		playerData.incBlocksDestroyed() ;
	}

	@Override public void onBlockSpread(BlockSpreadEvent event) {
		Material mat = event.getSource().getType();

		// Increment the per-block stats
		BlockData blockData = plugin.getBlockData(mat,"");
		blockData.incBlocksSpread();

		// Increment the per-server stats
		plugin.serverData.incBlocksSpread();
	}

	@Override public void onLeavesDecay(LeavesDecayEvent event) {
		Material mat = event.getBlock().getType();

		// Increment the per-block stats
		BlockData blockData = plugin.getBlockData(mat,"");
		blockData.incBlocksDecayed();

		// Increment the per-server stats
		plugin.serverData.incBlocksDecayed();
	}
}

