
package com.dkhenry.minejmx;

import org.bukkit.scheduler.BukkitScheduler;

public class ServerTickPoller implements Runnable {
	
	private MineJMX plugin ;
	private long interval =40;
	private long lastPoll = System.currentTimeMillis() ; 	
	
	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public long getLastPoll() {
		return lastPoll;
	}

	public void setLastPoll(long lastPoll) {
		this.lastPoll = lastPoll;
	}

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