package com.dkhenry.minejmx;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause ; 

public class MineJMXEntityListener implements Listener {
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
			// as it turns out this is only valid for cacti/lava...it might be 
			//    completely obsolete in favor of the following switch statement,
			//    but I'm going to leave it in for the time being just in case
			playerData.incDeathsByEnvironment();
		} else {
			// drowned, burned, fell, etc...increment the environmental death counter
			switch(cause.getCause()) {
				case CONTACT:
				case DROWNING:
				case FALL:
				case FIRE:
				case FIRE_TICK:
				case LAVA:
				case LIGHTNING:
					playerData.incDeathsByEnvironment();
					break;
			}
		}
	}

	private void handleNpeDeath(Entity subject) {
		EntityDamageEvent cause = subject.getLastDamageCause();

		// Increment the NPE's total death stat
		NpeData npeData = this.plugin.getNpeDataByClass(subject.getClass());
		npeData.incTotalDeaths();

		// Increment the ServerData statistics
		plugin.serverData.incNpesKilled() ;

		if(cause instanceof EntityDamageByEntityEvent) {
			Entity predicate = ((EntityDamageByEntityEvent)cause).getDamager();
			if(predicate instanceof Player) {
				// The NPE was killed by a player, reward them for their accomplishments
				PlayerData killerData = this.plugin.getPlayerData(((Player)predicate).getName(),"");
				String mobName = this.plugin.getSimpleClassName(subject.getClass()).toLowerCase();

				if(mobName.startsWith("craft")) {
					// it looks like a lot of the class names follow the format "CraftX", such as "CraftWolf" or "CraftCreeper"
					mobName = mobName.substring(5);
				}

				if(killerData.getMobsKilled().containsKey(mobName)) {
					killerData.incMobsKilled(mobName);
				} else {
					plugin.log.info("MineJMX: A player killed an unknown mob type (\"" + mobName + "\")");
				}

				// and increment the NPE's specific death stat
				npeData.incDeathsByPlayer();
			} else {
				// The NPE was killed by another mob, increment NpeData statistics again
				NpeData killerData = this.plugin.getNpeDataByClass(predicate.getClass());
				killerData.incNpesKilled();

				// and increment the original NPE's specific death stat
				npeData.incDeathsByNpe();
			}
		} else if(cause instanceof EntityDamageByBlockEvent) {
			// killed by environment, increment the specific death counter
			npeData.incDeathsByEnvironment();
		} else {
			// drowned, burned, fell, etc...increment the environmental death counter
			switch(cause.getCause()) {
				case CONTACT:
				case DROWNING:
				case FALL:
				case FIRE:
				case FIRE_TICK:
				case LAVA:
				case LIGHTNING:
					npeData.incDeathsByEnvironment();
					break;
			}
		}
	}

	@EventHandler 
	public void onEntityDeath(EntityDeathEvent event) {
		Entity subject = event.getEntity(), predicate;

		if(subject instanceof Player) {
			this.handlePlayerDeath((Player)subject);
		} else {
			this.handleNpeDeath(subject);
		}
	}
}

