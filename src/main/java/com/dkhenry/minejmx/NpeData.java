package com.dkhenry.minejmx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.SimpleType;

public class NpeData implements DynamicMBean {
	// stuff we're exporting
	private int killsByPlayer = 0;/**< In progress */
	private int killsByEnvironment = 0;/**< In progress */
	private int killsByNpe = 0;/**< In progress */
	private int playersKilled = 0;/**< In progress */
	//private int npesKilled = 0;/**< Not sure if relevant */

	// need to access the plugin object from this one
	private MineJMX plugin;

	public NpeData(MineJMX instance) {
		this.plugin = instance;
	}

	// killsByPlayer {{{
	public int getKillsByPlayer() {
		return this.killsByPlayer;
	}

	public void setKillsByPlayer(int killsByPlayer) {
		this.killsByPlayer = killsByPlayer;
	}

	public void incKillsByPlayer() {
		this.killsByPlayer++;
	}
	// }}}

	// killsByEnvironment {{{
	public int getKillsByEnvironment() {
		return this.killsByEnvironment;
	}

	public void setKillsByEnvironment(int killsByEnvironment) {
		this.killsByEnvironment = killsByEnvironment;
	}

	public void incKillsByEnvironment() {
		this.killsByEnvironment++;
	}
	// }}}

	// killsByNpe {{{
	public int getKillsByNpe() {
		return this.killsByNpe;
	}

	public void setKillsByNpe(int killsByNpe) {
		this.killsByNpe = killsByNpe;
	}

	public void incKillsByNpe() {
		this.killsByNpe++;
	}
	// }}}

	// playersKilled {{{
	public int getPlayersKilled() {
		return this.playersKilled;
	}

	public void setPlayersKilled(int playersKilled) {
		this.playersKilled = playersKilled;
	}

	public void incPlayersKilled() {
		this.playersKilled++;
	}
	// }}}

	@Override public Object getAttribute(String arg0) throws AttributeNotFoundException, MBeanException, ReflectionException {
		if(arg0.equals("killsByPlayer")) {
			return this.getKillsByPlayer();
		} else if(arg0.equals("killsByEnvironment")) {
			return this.getKillsByEnvironment();
		} else if(arg0.equals("killsByNpe")) {
			return this.getKillsByNpe();
		} else if(arg0.equals("playersKilled")) {
			return this.getPlayersKilled();
		}
		throw new AttributeNotFoundException("Cannot find " + arg0 + " attribute");
	}

	@Override public AttributeList getAttributes(String[] arg0) {
		AttributeList resultList = new AttributeList();
		if(arg0.length == 0) {
			return resultList;
		}
		for(int i = 0; i < arg0.length; i++) {
			try {
				Object Value = getAttribute(arg0[i]);
				resultList.add(new Attribute(arg0[i], Value));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	@Override public MBeanInfo getMBeanInfo() {
		OpenMBeanInfoSupport info;
		OpenMBeanAttributeInfoSupport[] attributes = {
			new OpenMBeanAttributeInfoSupport("killsByPlayer", "Number of times killed by a player", SimpleType.INTEGER, true, false, false),
			new OpenMBeanAttributeInfoSupport("killsByEnvironment", "Number of times killed by the environment", SimpleType.INTEGER, true, false, false),
			new OpenMBeanAttributeInfoSupport("killsByNpe", "Number of times killed by other non-player entities", SimpleType.INTEGER, true, false, false),
			new OpenMBeanAttributeInfoSupport("playersKilled", "Number of players killed", SimpleType.INTEGER, true, false, false)
		};

		//Build the info
		info = new OpenMBeanInfoSupport(this.getClass().getName(), "Quote - Open - MBean", attributes, null, null, null);
		return info;
	}

	@Override public Object invoke(String arg0, Object[] arg1, String[] arg2) throws MBeanException, ReflectionException {
		throw new ReflectionException(new NoSuchMethodException(arg0), "Cannot find the operation " + arg0);
	}

	@Override public void setAttribute(Attribute arg0) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
		throw new AttributeNotFoundException("No attributes can be set on this MBean");
	}

	@Override public AttributeList setAttributes(AttributeList arg0) {
		return new AttributeList();
	}

	// persistence {{{
	public String getMetricData() {
		String rvalue = "";
		return "killsByPlayer:" + this.killsByPlayer +
		       ",killsByEnvironment:" + this.killsByEnvironment +
			   ",killsByNpe:" + this.killsByNpe +
			   ",playersKilled:" + this.playersKilled;
	}

	public static NpeData instanceFromResultSet(ResultSet rs, MineJMX plugin) throws SQLException {
		NpeData entry = new NpeData(plugin);
		String data = rs.getString("data");
		if(data.length() <= 0) {
			return entry;
		}
		String[] datas = data.split(",");
		for(String s : datas) {
			String[] keyval = s.split(":");
			if(keyval[0].equals("killsByPlayer")) {
				entry.setKillsByPlayer(Integer.decode(keyval[1]));
			} else if(keyval[0].equals("killsByEnvironment")) {
				entry.setKillsByEnvironment(Integer.decode(keyval[1]));
			} else if(keyval[0].equals("killsByNpe")) {
				entry.setKillsByNpe(Integer.decode(keyval[1]));
			} else if(keyval[0].equals("playersKilled")) {
				entry.setPlayersKilled(Integer.decode(keyval[1]));
			}
		}
		return entry;
	}
	// }}}
}
