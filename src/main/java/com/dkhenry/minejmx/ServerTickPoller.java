
package com.dkhenry.minejmx;

import org.bukkit.scheduler.BukkitScheduler;

public class ServerTickPoller implements Runnable {
	
	private MineJMX plugin ;
	private long interval ;
	private long lastPoll = System.currentTimeMillis() ; 	
	
	public ServerTickPoller(MineJMX instance) { 
		plugin = instance ; 
	}
	
	public void registerWithScheduler(BukkitScheduler scheduler) { 
				scheduler.scheduleAsyncRepeatingTask(plugin, this, 0, interval) ; 
	}
	
	@Override
	public void run() {
		// Cache the current time 
		long current = System.currentTimeMillis() ;

		// Calculate the Delta 
		long delta = current - this.lastPoll ;
		
		// Make sure we check for a DivByZero error 
		if(delta <= 0 ) { 
			delta = 1 ; 
		}
		
		int tickRate = new Long(this.interval*1000 / delta).intValue() ;
		
		plugin.serverPerformanceData.setTickRate(tickRate) ; 
		plugin.serverPerformanceData.addTicks(this.interval) ;
		this.lastPoll = current ;  
	}		
	
}