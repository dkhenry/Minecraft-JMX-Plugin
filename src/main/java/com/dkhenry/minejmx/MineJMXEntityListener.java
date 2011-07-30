package com.dkhenry.minejmx;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class MineJMXEntityListener extends EntityListener {
	private static MineJMX plugin ;

	public MineJMXEntityListener(MineJMX instance) {
		plugin = instance ;
	}

	@Override public void onEntityDeath(EntityDeathEvent event) {
		Entity subject = event.getEntity() ;
		// If a player Died we Increment His deaths and carry on
		if( subject instanceof Player ) {
			// Increment The Per Player Stats
			Player player = (Player) subject ;
			PlayerData playerData = null ;
			if(plugin.playerData.containsKey(player.getName())) {
				playerData = plugin.playerData.get(player.getName()) ;
			} else {
				plugin.log.info("MineJMX Found a new first time Player") ;
				playerData = new PlayerData(plugin) ;
				plugin.addPlayer(player.getName(),playerData) ;
			}
			playerData.incDeaths() ;

			// Increment The Server Stats
			plugin.serverData.incPlayersKilled() ;
			return;
		}

		// So This wasn't a Player Dieing but a mob Dieing. Lets find who doneit and reward them for their accomplishments
		EntityDamageEvent cause = subject.getLastDamageCause() ;
		if( cause instanceof EntityDamageByEntityEvent ) {
			Entity predicate = ((EntityDamageByEntityEvent)cause).getDamager() ;
			if( predicate instanceof Player ) {
				Player player = (Player) predicate ;
				String mobType;

				// Increment The Per Player Stats
				PlayerData playerData = null ;
				if(plugin.playerData.containsKey(player.getName())) {
					playerData = plugin.playerData.get(player.getName()) ;
				} else {
					plugin.log.info("MineJMX Found a new first time Player") ;
					playerData = new PlayerData(plugin) ;
					plugin.addPlayer(player.getName(),playerData) ;
					return ;
				}

				// Find out  What kind of Monster it was
				mobType = subject.class.getName().replace('$', '.');
				if(mobType.lastIndexOf('.') > 0) {
					mobType = mobType.substring(mobType.lastIndexOf('.') + 1).toLowerCase();
				}
				if(plugin.serverData.getMobsKilled().containsKey(mobType) {
					playerData.incMobsKilled(mobType);
					plugin.serverData.incMobsKilled(mobType);
				} else {
					plugin.log.debug("MineJMX: A player killed an unknown mob type (\"" + mobType + "\")");
				}
			}
		} else if(cause instanceof EntityDamageByBlockEvent) {
			// it was killed environmentally; let's increment the appropriate counter for this mob type
			String mobType = subject.class.getName().replace('$', '.');
			if(mobType.lastIndexOf('.') > 0) {
				mobType = mobType.substring(mobType.lastIndexOf('.') + 1).toLowerCase();
			}
			if(plugin.serverData.getMobsKilled().containsKey(mobType) {
				plugin.serverData.incMobsKilledEnviron(mobType);
			} else {
				plugin.log.debug("MineJMX: An unknown mob type (\"" + mobType + "\") was killed environmentally");
			}
		}
	}
}

