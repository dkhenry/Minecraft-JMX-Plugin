package com.dkhenry.minejmx;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class MineJMXEntityListener extends EntityListener {
	private MineJMX plugin ;

	public MineJMXEntityListener(MineJMX instance) {
		plugin = instance ;
	}

	private void handlePlayerDeath(Player subject) {
		EntityDamageEvent cause = subject.getLastDamageCause();

		// Increment the PlayerData total death stat
		PlayerData playerData = this.plugin.getPlayerData(subject.getName(), "MineJMX found a new Player");
		playerData.incDeaths();

		// Increment the ServerData statistics
		plugin.serverData.incPlayersKilled();

		if(cause instanceof EntityDamageByEntityEvent) {
			Entity predicate = ((EntityDamageByEntityEvent)cause).getDamager();
			if(predicate instanceof Player) {
				// The player was killed by another player, dick move bro
				PlayerData killerData = this.plugin.getPlayerData(((Player)predicate).getName(),"");
				killerData.incPlayersKilled();

				// Increment the victim's PvP death stat
				playerData.incDeathsByPlayer();
			} else {
				// The player was killed by a mob, increment the NpeData statistics
				NpeData killerData = this.plugin.getNpeDataByClass(predicate.getClass());
				killerData.incPlayersKilled();

				// Increment the victim's death by NPE stat
				playerData.incDeathsByNpe();
			}
		} else if(cause instanceof EntityDamageByBlockEvent) {
			// drowned, burned, fell, got stabbed by cactus, etc...increment the environmental death counter
			playerData.incDeathsByEnvironment();
		}
	}

	private void handleNpeDeath(Entity subject) {
		EntityDamageEvent cause = subject.getLastDamageCause();

		// Increment the NPE's total death stat
		NpeData npeData = this.plugin.getNpeDataByClass(subject.getClass());
		npeData.incTotalDeaths();

		// Increment the ServerData statistics
		//plugin.serverData.incMobsKilled();

		if(cause instanceof EntityDamageByEntityEvent) {
			Entity predicate = ((EntityDamageByEntityEvent)cause).getDamager();
			if(predicate instanceof Player) {
				// The NPE was killed by a player, reward them for their accomplishments
				PlayerData killerData = this.plugin.getPlayerData(((Player)predicate).getName(),"");
				String mobName = this.plugin.getSimpleClassName(subject.getClass()).toLowerCase();
				if(killerData.getMobsKilled().containsKey(mobName)) {
					killerData.incMobsKilled(mobName);
				} else {
					plugin.log.info("MineJMX: A player killed an unknown mob type (\"" + mobName + "\")");
				}

				// and increment the NPE's specific death stat
				npeData.incKillsByPlayer();
			} else {
				// The NPE was killed by another mob, increment NpeData statistics again
				NpeData killerData = this.plugin.getNpeDataByClass(predicate.getClass());
				killerData.incNpesKilled();

				// and increment the original NPE's specific death stat
				npeData.incKillsByNpe();
			}
		} else if(cause instanceof EntityDamageByBlockEvent) {
			// killed by environment, increment the specific death counter
			npeData.incKillsByEnvironment();
		}
	}

	@Override public void onEntityDeath(EntityDeathEvent event) {
		Entity subject = event.getEntity(), predicate;

		if(subject instanceof Player) {
			this.handlePlayerDeath((Player)subject);
		} else {
			this.handleNpeDeath(subject);
		}
	}
}

