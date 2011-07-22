package com.dkhenry.minejmx;

import org.bukkit.event.server.ServerListener;

public class MineJMXServerListener extends ServerListener {

	public static MineJMX plugin;

	public MineJMXServerListener(MineJMX instance) {
		plugin = instance ;
	}

}
