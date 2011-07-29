package com.dkhenry.minejmx;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
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
				if( subject instanceof Creeper) {
					playerData.incMobsKilled("creeper") ;
					plugin.serverData.incMobsKilled("creeper") ;
				} else if (subject instanceof Skeleton ) {
					playerData.incMobsKilled("skeleton") ;
					plugin.serverData.incMobsKilled("skeleton") ;
				} else if (subject instanceof Zombie ) {
					playerData.incMobsKilled("zombie") ;
					plugin.serverData.incMobsKilled("zombie") ;
				} else if (subject instanceof Spider ) {
					playerData.incMobsKilled("spider") ;
					plugin.serverData.incMobsKilled("spider") ;
				} else if(subject instanceof Slime) {
					playerData.incMobsKilled("slime");
					plugin.serverData.incMobsKilled("slime");
				}
			}
		} else if(cause instanceof EntityDamageByBlockEvent) {
			// it was killed environmentally; let's increment the appropriate counter for this mob type
			if(subject instanceof Creeper) {
				plugin.serverData.incMobsKilledEnviron("creeper");
			} else if(subject instanceof Skeleton) {
				plugin.serverData.incMobsKilledEnviron("skeleton");
			} else if(subject instanceof Zombie) {
				plugin.serverData.incMobsKilledEnviron("zombie");
			} else if(subject instanceof Spider) {
				plugin.serverData.incMobsKilledEnviron("spider");
			}
		}
	}
}

