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

public class PlayerData implements DynamicMBean {
	// stuff we're exporting
	private long timeOnServer = 0; /**< Done */
	private int numberOfLogins = 0; /**< Done */
	private int blocksPlaced = 0; /**< Done */
	private int blocksDestroyed = 0; /**< Done */
	private int itemsCrafted = 0;
	private int playersKilled = 0; /**< Done */
	private Map<String,Integer> mobsKilled ; /**< Done */
	private int deaths = 0 ; /**< Done */
	private int active = 0 ; /**< Done */
	private double distanceMoved = 0.0; /**< Done */
	private int deathsByPlayer = 0; /**< Done */
	private int deathsByEnvironment = 0; /**< Done */
	private int deathsByNpe = 0; /**< Done */

	// internal use
	private long loggedInTimestamp = -1; // timestamp of when the player logged in; -1 if they're not logged in

	// need to access the plugin object from this one
	private MineJMX plugin;

	public PlayerData(MineJMX instance) {
		this.plugin = instance;
		this.mobsKilled = new HashMap<String,Integer>();
		// real mobs
		this.mobsKilled.put("creeper", new Integer(0));
		this.mobsKilled.put("skeleton", new Integer(0));
		this.mobsKilled.put("spider", new Integer(0));
		this.mobsKilled.put("zombie", new Integer(0));
		this.mobsKilled.put("slime", new Integer(0));
		this.mobsKilled.put("enderman", new Integer(0));
		this.mobsKilled.put("blaze", new Integer(0));
		this.mobsKilled.put("pigman", new Integer(0));
		this.mobsKilled.put("ghasts", new Integer(0));
		this.mobsKilled.put("magma", new Integer(0));
		// animals
		this.mobsKilled.put("chicken", new Integer(0));
		this.mobsKilled.put("cow", new Integer(0));		
		this.mobsKilled.put("pig", new Integer(0));
		this.mobsKilled.put("dog", new Integer(0));
		this.mobsKilled.put("sheep", new Integer(0));
		this.mobsKilled.put("squid", new Integer(0));
		this.mobsKilled.put("wolf", new Integer(0));
		this.mobsKilled.put("ocolet", new Integer(0));
		this.mobsKilled.put("mooshurm", new Integer(0));
		// NPC
		this.mobsKilled.put("testificus", new Integer(0));
		this.mobsKilled.put("irongolom", new Integer(0));
		this.mobsKilled.put("snowgolom", new Integer(0));
	}

	// timeOnServer {{{
	public long getTimeOnServer() {
		return timeOnServer;
	}

	public void setTimeOnServer(int timeOnServer) {
		this.timeOnServer = timeOnServer;
	}

	public void incTimeOnServerBy(long ms) {
		this.timeOnServer += ms;
	}
	// }}}

	// {{{ numberOfLogins
	public int getNumberOfLogins() {
		return numberOfLogins;
	}

	public void setNumberOfLogins(int numberOfLogins) {
		this.numberOfLogins = numberOfLogins;
	}

	public void incNumberOfLogins() {
		this.numberOfLogins++ ;
	}
	// }}}

	// blocksPlaced {{{
	public int getBlocksPlaced() {
		return blocksPlaced;
	}

	public void setBlocksPlaced(int blocksPlaced) {
		this.blocksPlaced = blocksPlaced;
	}

	public void incBlocksPlaced() {
		this.blocksPlaced++ ;
	}
	// }}}

	// blocksDestroyed {{{
	public int getBlocksDestroyed() {
		return blocksDestroyed;
	}

	public void setBlocksDestroyed(int blocksDestroyed) {
		this.blocksDestroyed = blocksDestroyed;
	}

	public void incBlocksDestroyed() {
		this.blocksDestroyed++ ;
	}
	// }}}

	// itemsCrafted {{{
	public int getItemsCrafted() {
		return itemsCrafted;
	}

	public void setItemsCrafted(int itemsCrafted) {
		this.itemsCrafted = itemsCrafted;
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

	// mobsKilled {{{
	public Map<String, Integer> getMobsKilled() {
		return this.mobsKilled;
	}

	public int getMobsKilled(String type) {
		return this.mobsKilled.get(type);
	}

	public void setMobsKilled(HashMap<String, Integer> mobsKilled) {
		this.mobsKilled = mobsKilled;
	}

	public void incMobsKilled(String type) {
		this.mobsKilled.put(type, this.mobsKilled.get(type)+1)  ;
	}
	// }}}

	// deaths {{{
	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void incDeaths() {
		this.deaths++ ;
	}
	// }}}

	// active {{{
	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
	// }}}

	// distancedMoved {{{
	public double getDistanceMoved() {
		return this.distanceMoved;
	}

	public void setDistanceMoved(double distanceMoved) {
		this.distanceMoved = distanceMoved;
	}

	public void incDistanceMovedBy(double distanceMoved) {
		this.distanceMoved += distanceMoved;
	}
	// }}}

	// deathsByPlayer {{{
	public int getDeathsByPlayer() {
		return this.deathsByPlayer;
	}

	public void setDeathsByPlayer(int deathsByPlayer) {
		this.deathsByPlayer = deathsByPlayer;
	}

	public void incDeathsByPlayer() {
		this.deathsByPlayer++;
	}
	// }}}

	// deathsByEnvironment {{{
	public int getDeathsByEnvironment() {
		return this.deathsByEnvironment;
	}

	public void setDeathsByEnvironment(int deathsByEnvironment) {
		this.deathsByEnvironment = deathsByEnvironment;
	}

	public void incDeathsByEnvironment() {
		this.deathsByEnvironment++;
	}
	// }}}

	// deathsByNpe {{{
	public int getDeathsByNpe() {
		return this.deathsByNpe;
	}

	public void setDeathsByNpe(int deathsByNpe) {
		this.deathsByNpe = deathsByNpe;
	}

	public void incDeathsByNpe() {
		this.deathsByNpe++;
	}
	// }}}

	public long timeSinceLogin() {
		if(this.loggedInTimestamp == -1) {
			return -1;
		}
		return System.currentTimeMillis() - this.loggedInTimestamp;
	}

	public long getFullTimeOnServer() {
		return (1 == this.active) ? (this.timeOnServer + this.timeSinceLogin()) : this.timeOnServer;
	}

	public void logIn() {
		this.incNumberOfLogins();
		this.setActive(1);
		this.loggedInTimestamp = System.currentTimeMillis();
	}

	public long logOut() {
		long playerLoggedInTime = this.timeSinceLogin();
		this.setActive(0);
		this.incTimeOnServerBy(playerLoggedInTime);
		this.loggedInTimestamp = -1;
		return playerLoggedInTime;
	}

	@Override
	public Object getAttribute(String arg0) throws AttributeNotFoundException,
			MBeanException, ReflectionException {

		if(arg0.equals("timeOnServer")) {
			return this.getFullTimeOnServer();
		} else if(arg0.equals("numberOfLogins")) {
			return getNumberOfLogins() ;
		} else if(arg0.equals("blocksPlaced")) {
			return getBlocksPlaced() ;
		} else if(arg0.equals("blocksDestroyed")) {
			return getBlocksDestroyed() ;
		} else if(arg0.equals("itemsCrafted")) {
			return getItemsCrafted() ;
		} else if(arg0.equals("playersKilled")) {
			return this.getPlayersKilled();
		} else if(arg0.equals("mobsKilled")) {
			return
				this.mobsKilled.get("creeper") +
				this.mobsKilled.get("skeleton") +
				this.mobsKilled.get("spider") +
				this.mobsKilled.get("zombie") +
				this.mobsKilled.get("slime");
		} else if(arg0.equals("creepersKilled")) {
			return this.mobsKilled.get("creeper") ;
		} else if(arg0.equals("skeletonsKilled")) {
			return this.mobsKilled.get("skeleton") ;
		} else if(arg0.equals("spidersKilled")) {
			return this.mobsKilled.get("spider") ;
		} else if(arg0.equals("zombiesKilled")) {
			return this.mobsKilled.get("zombie") ;
		} else if(arg0.equals("slimesKilled")) {
			return this.mobsKilled.get("slime");
		} else if(arg0.equals("animalsKilled")) {
			return
				this.mobsKilled.get("chicken") +
				this.mobsKilled.get("cow") +
				this.mobsKilled.get("pig") +
				this.mobsKilled.get("sheep") +
				this.mobsKilled.get("squid") +
				this.mobsKilled.get("wolf");
		} else if(arg0.equals("chickensKilled")) {
			return this.mobsKilled.get("chicken");
		} else if(arg0.equals("cowsKilled")) {
			return this.mobsKilled.get("cow");
		} else if(arg0.equals("pigsKilled")) {
			return this.mobsKilled.get("pig");
		} else if(arg0.equals("sheepsKilled")) {
			return this.mobsKilled.get("sheep");
		} else if(arg0.equals("squidsKilled")) {
			return this.mobsKilled.get("squid");
		} else if(arg0.equals("wolfsKilled")) {
			return this.mobsKilled.get("wolf");
		} else if(arg0.equals("deaths")) {
			return getDeaths() ;
		} else if(arg0.equals("active")) {
			return getActive() ;
		} else if(arg0.equals("distanceMoved")) {
			return this.getDistanceMoved();
		} else if(arg0.equals("deathsByPlayer")) {
			return this.getDeathsByPlayer();
		} else if(arg0.equals("deathsByEnvironment")) {
			return this.getDeathsByEnvironment();
		} else if(arg0.equals("deathsByNpe")) {
			return this.getDeathsByNpe();
		}

		throw new AttributeNotFoundException("Cannot find " + arg0 + " attribute") ;
	}

	@Override
	public AttributeList getAttributes(String[] arg0) {
		AttributeList resultList = new AttributeList() ;
		if(arg0.length == 0 ) {
			return resultList ;
		}
		for ( int i = 0 ; i < arg0.length ; i++) {
			try {
				Object Value = getAttribute(arg0[i]) ;
				resultList.add(new Attribute(arg0[i],Value)) ;
			} catch (Exception e) {
				e.printStackTrace() ;
			}
		}
		return resultList ;
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		OpenMBeanInfoSupport info;
		OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[25];

		//Build the Attributes
		attributes[0] = new OpenMBeanAttributeInfoSupport("timeOnServer", "Time spent on this server in milliseconds", SimpleType.LONG, true, false, false);
		attributes[1] = new OpenMBeanAttributeInfoSupport("numberOfLogins","Number of Logins to this Server",SimpleType.INTEGER, true, false,false);
		attributes[2] = new OpenMBeanAttributeInfoSupport("blocksPlaced","Number of Blocks Placed",SimpleType.INTEGER, true, false,false);
		attributes[3] = new OpenMBeanAttributeInfoSupport("blocksDestroyed","Number of Blocks Destroyed",SimpleType.INTEGER, true, false,false);
		attributes[4] = new OpenMBeanAttributeInfoSupport("itemsCrafted","Number of items Crafted",SimpleType.INTEGER, true, false,false);
		attributes[5] = new OpenMBeanAttributeInfoSupport("playersKilled", "Number of players killed (PvP)", SimpleType.INTEGER, true, false, false);
		attributes[6] = new OpenMBeanAttributeInfoSupport("mobsKilled", "Number Of Mobs Killed", SimpleType.INTEGER, true, false, false);
		attributes[7] = new OpenMBeanAttributeInfoSupport("creepersKilled", "Number of Creepers Killed", SimpleType.INTEGER, true, false, false);
		attributes[8] = new OpenMBeanAttributeInfoSupport("skeletonsKilled", "Number of Skeletons Killed", SimpleType.INTEGER, true, false, false);
		attributes[9] = new OpenMBeanAttributeInfoSupport("spidersKilled", "Number of Spiders Killed", SimpleType.INTEGER, true, false, false);
		attributes[10] = new OpenMBeanAttributeInfoSupport("zombiesKilled", "Number of Zombies Killed", SimpleType.INTEGER, true, false, false);
		attributes[11] = new OpenMBeanAttributeInfoSupport("slimesKilled", "Number of Slimes killed", SimpleType.INTEGER, true, false, false);
		attributes[12] = new OpenMBeanAttributeInfoSupport("animalsKilled", "Number of animals killed", SimpleType.INTEGER, true, false, false);
		attributes[13] = new OpenMBeanAttributeInfoSupport("chickensKilled", "Number of Chickens killed", SimpleType.INTEGER, true, false, false);
		attributes[14] = new OpenMBeanAttributeInfoSupport("cowsKilled", "Number of Cows killed", SimpleType.INTEGER, true, false, false);
		attributes[15] = new OpenMBeanAttributeInfoSupport("pigsKilled", "Number of Pigs killed", SimpleType.INTEGER, true, false, false);
		attributes[16] = new OpenMBeanAttributeInfoSupport("sheepsKilled", "Number of Sheep killed", SimpleType.INTEGER, true, false, false);
		attributes[17] = new OpenMBeanAttributeInfoSupport("squidsKilled", "Number of Squids killed", SimpleType.INTEGER, true, false, false);
		attributes[18] = new OpenMBeanAttributeInfoSupport("wolfsKilled", "Number of Wolves killed", SimpleType.INTEGER, true, false, false);
		attributes[19] = new OpenMBeanAttributeInfoSupport("deaths", "Number of deaths on this server", SimpleType.INTEGER, true, false, false);
		attributes[20] = new OpenMBeanAttributeInfoSupport("active", "If this player is active", SimpleType.INTEGER, true, false, false);
		attributes[21] = new OpenMBeanAttributeInfoSupport("distanceMoved", "How far this player has moved", SimpleType.DOUBLE, true, false, false);
		attributes[22] = new OpenMBeanAttributeInfoSupport("deathsByPlayer", "How many times this player was killed by other players", SimpleType.INTEGER, true, false, false);
		attributes[23] = new OpenMBeanAttributeInfoSupport("deathsByEnvironment", "How many times this player was killed by environmental causes", SimpleType.INTEGER, true, false, false);
		attributes[24] = new OpenMBeanAttributeInfoSupport("deathsByNpe", "How many times this player was killed by non-Player Entities", SimpleType.INTEGER, true, false, false);

		//Build the info
		info = new OpenMBeanInfoSupport(this.getClass().getName(),
					"Quote - Open - MBean", attributes, null,
					null, null);
		return info;
	}

	@Override
	public Object invoke(String arg0, Object[] arg1, String[] arg2)
			throws MBeanException, ReflectionException {
		throw new ReflectionException(new NoSuchMethodException(arg0),"Cannot find the operation " + arg0) ;
	}

	@Override
	public void setAttribute(Attribute arg0) throws AttributeNotFoundException,
			InvalidAttributeValueException, MBeanException, ReflectionException {
		throw new AttributeNotFoundException("No attributes can be set on this MBean") ;

	}

	@Override
	public AttributeList setAttributes(AttributeList arg0) {
		return new AttributeList() ;
	}

	// persistence {{{
	public String getMetricData() {
		String rvalue = "" ;
		for(Entry<String, Integer> entity : this.mobsKilled.entrySet()) {
			rvalue += ","+entity.getKey()+":"+entity.getValue() ;
		}
		return "timeOnServer:"+this.timeOnServer+
				",numberOfLogins:"+this.numberOfLogins+
				",blocksPlaced:"+this.blocksPlaced+
				",blocksDestroyed:"+this.blocksDestroyed+
				",itemsCrafted:"+this.itemsCrafted+
				",playersKilled:" + this.playersKilled +
				",deaths:"+this.deaths+
				",active:"+this.active +
				",distanceMoved:" + this.distanceMoved +
				",deathsByPlayer:" + this.deathsByPlayer +
				",deathsByEnvironment:" + this.deathsByEnvironment +
				",deathsByNpe:" + this.deathsByNpe +
				rvalue;
	}

	public static PlayerData instanceFromResultSet(ResultSet rs, MineJMX plugin) throws SQLException {
		PlayerData pd = new PlayerData(plugin) ; ;
		String data = rs.getString("data") ;
		if(data.length() <=0 ) {
			return pd ;
		}
		String[] datas = data.split(",") ;
		for(String s : datas) {
			String[] keyval = s.split(":") ;
			if( keyval[0].equals("timeOnServer") ) {
				pd.setTimeOnServer(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("numberOfLogins") ) {
				pd.setNumberOfLogins(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("blocksPlaced") ) {
				pd.setBlocksPlaced(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("blocksDestroyed") ) {
				pd.setBlocksDestroyed(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("itemsCrafted") ) {
				pd.setItemsCrafted(Integer.decode(keyval[1])) ;
			} else if(keyval[0].equals("playersKilled")) {
				pd.setPlayersKilled(Integer.decode(keyval[1]));
			} else if( keyval[0].equals("deaths") ) {
				pd.setDeaths(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("active") ) {
				// Don't Set Player Active
			} else if(keyval[0].equals("distanceMoved")) {
				pd.setDistanceMoved(Double.parseDouble(keyval[1]));
			} else if(keyval[0].equals("deathsByPlayer")) {
				pd.setDeathsByPlayer(Integer.decode(keyval[1]));
			} else if(keyval[0].equals("deathsByEnvironment")) {
				pd.setDeathsByEnvironment(Integer.decode(keyval[1]));
			} else if(keyval[0].equals("deathsByNpe")) {
				pd.setDeathsByNpe(Integer.decode(keyval[1]));
			} else {
				pd.getMobsKilled().put(keyval[0], Integer.decode(keyval[1])) ;
			}
		}
		return pd ;
	}
	// }}}
}
