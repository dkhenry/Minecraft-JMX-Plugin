
package com.dkhenry.minejmx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
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

public class ServerData implements DynamicMBean {
	// stuff we're exporting to JMX
	private long blocksPlaced ; /**< Done */
	private long blocksDestroyed ; /**< Done */
	private long blocksSpread; /**< Done */
	private long blocksDecayed; /**< Done */
	private long itemsCrafted ;
	private Map<String,Integer> mobsKilled ;/** Done */
	private Map<String,Integer> mobsKilledEnviron; /**< In Progress */
	private int playersKilled ; /** Done */
	private long playTime ; /**< Done */
	private int numberOfPlayers ; /**< Done */
	private double playerDistanceMoved = 0.0; /**< In Progress */

	// need to access the plugin object from this one
	private MineJMX plugin;

	public ServerData(MineJMX plugin) {
		this.plugin = plugin;
		this.mobsKilled = new HashMap<String,Integer>();
		// real mobs
		this.mobsKilled.put("creeper", new Integer(0));
		this.mobsKilled.put("slime", new Integer(0));
		this.mobsKilled.put("skeleton", new Integer(0));
		this.mobsKilled.put("spider", new Integer(0));
		this.mobsKilled.put("zombie", new Integer(0));
		// animals
		this.mobsKilled.put("chicken", new Integer(0));
		this.mobsKilled.put("cow", new Integer(0));
		this.mobsKilled.put("pig", new Integer(0));
		this.mobsKilled.put("sheep", new Integer(0));
		this.mobsKilled.put("wolf", new Integer(0));
	}

	// blocksPlaced {{{
	public long getBlocksPlaced() {
		return blocksPlaced;
	}

	public void setBlocksPlaced(long blocksPlaced) {
		this.blocksPlaced = blocksPlaced;
	}

	public void incBlocksPlaced() {
		this.blocksPlaced++ ;
	}
	// }}}

	// itemsCrafted {{{
	public long getItemsCrafted() {
		return itemsCrafted;
	}

	public void setItemsCrafted(long itemsCrafted) {
		this.itemsCrafted = itemsCrafted;
	}

	public void incItemsCrafted() {
		this.itemsCrafted++ ;
	}
	// }}}

	// mobsKilled {{{
	public Map<String,Integer> getMobsKilled() {
		return this.mobsKilled;
	}

	public int getMobsKilled(String type) {
		return this.mobsKilled.get(type);
	}

	public void setMobsKilled(Map<String,Integer> mobsKilled) {
		this.mobsKilled = mobsKilled;
	}

	public void incMobsKilled(String type) {
		this.mobsKilled.put(type, this.mobsKilled.get(type)+1)  ;
	}
	// }}}

	// mobsKilledEnviron {{{
	public Map<String,Integer> getMobsKilledEnviron() {
		return this.mobsKilledEnviron;
	}

	public int getMobsKilledEnviron(String type) {
		return this.mobsKilledEnviron.get(type);
	}

	public void setMobsKilledEnviron(Map<String,Integer> mobsKilledEnviron) {
		this.mobsKilledEnviron = mobsKilledEnviron;
	}

	public void incMobsKilledEnviron(String type) {
		this.mobsKilledEnviron.put(type, this.mobsKilledEnviron.get(type) + 1);
	}
	// }}}

	// playTime {{{
	public long getPlayTime() {
		return playTime;
	}

	public void setPlayTime(long playTime) {
		this.playTime = playTime;
	}

	public void incPlayTimeBy(long ms) {
		this.playTime += ms;
	}
	// }}}

	// numberOfPlayers {{{
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public void incNumberOfPlayers() {
		this.numberOfPlayers++ ;
	}

	public void decNumberOfPlayers() {
		this.numberOfPlayers-- ;
	}
	// }}}

	// playersKilled {{{
	public int getPlayersKilled() {
		return playersKilled;
	}

	public void setPlayersKilled(int playersKilled) {
		this.playersKilled = playersKilled;
	}

	public void incPlayersKilled() {
		this.playersKilled++ ;
	}
	// }}}

	// blocksDestroyed {{{
	public long getBlocksDestroyed() {
		return blocksDestroyed;
	}

	public void setBlocksDestroyed(long blocksDestroyed) {
		this.blocksDestroyed = blocksDestroyed;
	}

	public void incBlocksDestroyed() {
		this.blocksDestroyed++ ;
	}
	// }}}

	// blocksSpread {{{
	public long getBlocksSpread() {
		return this.blocksSpread;
	}

	public void setBlocksSpread(long blocksSpread) {
		this.blocksSpread = blocksSpread;
	}

	public void incBlocksSpread() {
		this.blocksSpread++;
	}
	// }}}

	// blocksDecayed {{{
	public long getBlocksDecayed() {
		return this.blocksDecayed;
	}

	public void setBlocksDecayed(long blocksDecayed) {
		this.blocksDecayed = blocksDecayed;
	}

	public void incBlocksDecayed() {
		this.blocksDecayed++;
	}
	// }}}

	// playerDistanceMoved {{{
	public double getPlayerDistanceMoved() {
		return this.playerDistanceMoved;
	}

	public void setPlayerDistanceMoved(double playerDistanceMoved) {
		this.playerDistanceMoved = playerDistanceMoved;
	}

	public void incPlayerDistanceMovedBy(double playerDistanceMoved) {
		this.playerDistanceMoved += playerDistanceMoved;
	}
	// }}}

	public long getFullPlayTime() {
		long activePlayTime = 0;

		for(Iterator i = this.plugin.playerData.entrySet().iterator(); i.hasNext(); ) {
			PlayerData player = (PlayerData)((Map.Entry)i.next()).getValue() ;
			if(1 == player.getActive()) {
				activePlayTime += player.timeSinceLogin();
			}
		}

		return activePlayTime + this.playTime;
	}

	@Override
	public Object getAttribute(String arg0) throws AttributeNotFoundException,
			MBeanException, ReflectionException {

		if(arg0.equals("blocksPlaced")) {
			return getBlocksPlaced() ;
		} else if(arg0.equals("blocksDestroyed")) {
			return getBlocksDestroyed() ;
		} else if(arg0.equals("blocksSpread")) {
			return this.getBlocksSpread();
		} else if(arg0.equals("blocksDecayed")) {
			return this.getBlocksDecayed();
		} else if(arg0.equals("itemsCrafted")) {
			return getItemsCrafted() ;
		} else if(arg0.equals("playersKilled")) {
			return getPlayersKilled() ;
		} else if(arg0.equals("playTime")) {
			return this.getFullPlayTime();
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
				this.mobsKilled.get("wolf");
		} else if(arg0.equals("chickensKilled")) {
			return this.mobsKilled.get("chicken");
		} else if(arg0.equals("cowsKilled") {
			return this.mobsKilled.get("cow");
		} else if(arg0.equals("pigsKilled") {
			return this.mobsKilled.get("pigs");
		} else if(arg0.equals("sheepsKilled") {
			return this.mobsKilled.get("sheep");
		} else if(arg0.equals("wolfsKilled") {
			return this.mobsKilled.get("wolf");
		} else if(arg0.equals("mobsKilledEnviron")) {
			return
				this.mobsKilledEnviron.get("creeper") +
				this.mobsKilledEnviron.get("skeleton") +
				this.mobsKilledEnviron.get("spider") +
				this.mobsKilledEnviron.get("zombie") +
				this.mobsKilledEnviron.get("slime");
		} else if(arg0.equals("creepersKilledEnviron")) {
			return this.mobsKilledEnviron.get("creeper");
		} else if(arg0.equals("skeletonsKilledEnviron")) {
			return this.mobsKilledEnviron.get("skeleton");
		} else if(arg0.equals("spidersKilledEnviron")) {
			return this.mobsKilledEnviron.get("spider");
		} else if(arg0.equals("zombiesKilledEnviron")) {
			return this.mobsKilledEnviron.get("zombie");
		} else if(arg0.equals("slimesKilledEnviron")) {
			return this.mobsKilledEnviron.get("slime");
		} else if(arg0.equals("animalsKilledEnviron") {
			return
				this.mobsKilledEnviron.get("chicken") +
				this.mobsKilledEnviron.get("cow") +
				this.mobsKilledEnviron.get("pig") +
				this.mobsKilledEnviron.get("sheep") +
				this.mobsKilledEnviron.get("wolf");
		} else if(arg0.equals("chickensKilledEnviron")) {
			return this.mobsKilledEnviron.get("chicken");
		} else if(arg0.equals("cowsKilledEnviron") {
			return this.mobsKilledEnviron.get("cow");
		} else if(arg0.equals("pigsKilledEnviron") {
			return this.mobsKilledEnviron.get("pigs");
		} else if(arg0.equals("sheepsKilledEnviron") {
			return this.mobsKilledEnviron.get("sheep");
		} else if(arg0.equals("wolfsKilledEnviron") {
			return this.mobsKilledEnviron.get("wolf");
		} else if(arg0.equals("numberOfPlayers")) {
			return this.getNumberOfPlayers() ;
		} else if(arg0.equals("playerDistanceMoved")) {
			return this.getPlayerDistanceMoved();
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
		OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[33];

		//Build the Attributes
		attributes[0] = new OpenMBeanAttributeInfoSupport("blocksPlaced","Number of Blocks Placed",SimpleType.LONG, true, false,false);
		attributes[1] = new OpenMBeanAttributeInfoSupport("blocksDestroyed","Number of Blocks Destroyed",SimpleType.LONG, true, false,false);
		attributes[2] = new OpenMBeanAttributeInfoSupport("blocksSpread", "Number of blocks naturally spread", SimpleType.LONG, true, false, false);
		attributes[3] = new OpenMBeanAttributeInfoSupport("blocksDecayed", "Number of blocks naturally decayed", SimpleType.LONG, true, false, false);
		attributes[4] = new OpenMBeanAttributeInfoSupport("itemsCrafted","Number of items Crafted",SimpleType.LONG, true, false,false);
		attributes[5] = new OpenMBeanAttributeInfoSupport("playersKilled","Number Of Players Killed",SimpleType.INTEGER, true, false,false);
		attributes[6] = new OpenMBeanAttributeInfoSupport("playTime","Amount Of Time People have played on this Server",SimpleType.LONG, true, false,false);
		attributes[7] = new OpenMBeanAttributeInfoSupport("mobsKilled","Number Of Mobs Killed",SimpleType.INTEGER, true, false,false);
		attributes[8] = new OpenMBeanAttributeInfoSupport("creepersKilled","Number of Creepers Killed",SimpleType.INTEGER, true, false,false);
		attributes[9] = new OpenMBeanAttributeInfoSupport("skeletonsKilled","Number of Skeletons Killed",SimpleType.INTEGER, true, false,false);
		attributes[10] = new OpenMBeanAttributeInfoSupport("spidersKilled","Number of Spiders Killed",SimpleType.INTEGER, true, false,false);
		attributes[11] = new OpenMBeanAttributeInfoSupport("zombiesKilled","Number of Zombies Killed",SimpleType.INTEGER, true, false,false);
		attributes[12] = new OpenMBeanAttributeInfoSupport("slimesKilled","Number of Slime Killed",SimpleType.INTEGER, true, false,false);
		attributes[13] = new OpenMBeanAttributeInfoSupport("animalsKilled", "Number of animals killed", SimpleType.INTEGER, true, false, false);
		attributes[14] = new OpenMBeanAttributeInfoSupport("chickensKilled", "Number of Chickens killed", SimpleType.INTEGER, true, false, false);
		attributes[15] = new OpenMBeanAttributeInfoSupport("cowsKilled", "Number of Cows killed", SimpleType.INTEGER, true, false, false);
		attributes[16] = new OpenMBeanAttributeInfoSupport("pigsKilled", "Number of Pigs killed", SimpleType.INTEGER, true, false, false);
		attributes[17] = new OpenMBeanAttributeInfoSupport("sheepKilled", "Number of Sheep killed", SimpleType.INTEGER, true, false, false);
		attributes[18] = new OpenMBeanAttributeInfoSupport("wolvesKilled", "Number of Wolves killed", SimpleType.INTEGER, true, false, false);
		attributes[19] = new OpenMBeanAttributeInfoSupport("mobsKilledEnviron","Number of mobs killed environmentally",SimpleType.INTEGER, true, false,false);
		attributes[20] = new OpenMBeanAttributeInfoSupport("creepersKilledEnviron","Number of creepers killed environmentally",SimpleType.INTEGER, true, false,false);
		attributes[21] = new OpenMBeanAttributeInfoSupport("skeletonsKilledEnviron","Number of skeletons killed environmentally",SimpleType.INTEGER, true, false,false);
		attributes[22] = new OpenMBeanAttributeInfoSupport("spidersKilledEnviron","Number of spiders killed environmentally",SimpleType.INTEGER, true, false,false);
		attributes[23] = new OpenMBeanAttributeInfoSupport("zombiesKilledEnviron","Number of zombies killed environmentally",SimpleType.INTEGER, true, false,false);
		attributes[24] = new OpenMBeanAttributeInfoSupport("slimesKilledEnviron","Number of slimes killed environmentally",SimpleType.INTEGER, true, false,false);
		attributes[25] = new OpenMBeanAttributeInfoSupport("animalsKilledEnviron", "Number of animals killed environmentally", SimpleType.INTEGER, true, false, false);
		attributes[26] = new OpenMBeanAttributeInfoSupport("chickensKilledEnviron", "Number of Chickens killed environmentally", SimpleType.INTEGER, true, false, false);
		attributes[27] = new OpenMBeanAttributeInfoSupport("cowsKilledEnviron", "Number of Cows killed environmentally", SimpleType.INTEGER, true, false, false);
		attributes[28] = new OpenMBeanAttributeInfoSupport("pigsKilledEnviron", "Number of Pigs killed environmentally", SimpleType.INTEGER, true, false, false);
		attributes[29] = new OpenMBeanAttributeInfoSupport("sheepKilledEnviron", "Number of Sheep killed environmentally", SimpleType.INTEGER, true, false, false);
		attributes[30] = new OpenMBeanAttributeInfoSupport("wolvesKilledEnviron", "Number of Wolves killed environmentally", SimpleType.INTEGER, true, false, false);
		attributes[31] = new OpenMBeanAttributeInfoSupport("numberOfPlayers","Number of Players On Server",SimpleType.INTEGER, true, false,false);
		attributes[32] = new OpenMBeanAttributeInfoSupport("playerDistanceMoved", "Total player distance traveled", SimpleType.DOUBLE, true, false, false);

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

	public String getMetricData() {
		String rvalue = "" ;
		for(Entry<String, Integer> entity : this.mobsKilled.entrySet()) {
			rvalue += ","+entity.getKey()+":"+entity.getValue() ;
		}
		return "playTime:"+this.playTime+
				",numberOfPlayers:"+this.numberOfPlayers+
				",blocksPlaced:"+this.blocksPlaced+
				",blocksDestroyed:"+this.blocksDestroyed+
				",blocksSpread:"+this.blocksSpread+
				",blocksDecayed:"+this.blocksDecayed+
				",itemsCrafted:"+this.itemsCrafted+
				",playersKilled:"+this.playersKilled ;
	}

	public static ServerData instanceFromResultSet(ResultSet rs, MineJMX plugin) throws SQLException {
		ServerData sd = new ServerData(plugin) ; ;
		String data = rs.getString("data") ;
		if(data.length() <=0 ) {
			return sd ;
		}
		String[] datas = data.split(",") ;
		for(String s : datas) {
			String[] keyval = s.split(":") ;
			if( keyval[0].equals("playTime") ) {
				sd.setPlayTime(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("numberOfPlayers") ) {
				sd.setNumberOfPlayers(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("blocksPlaced") ) {
				sd.setBlocksPlaced(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("blocksDestroyed") ) {
				sd.setBlocksDestroyed(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("blocksSpread") ) {
				sd.setBlocksSpread(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("blocksDecayed") ) {
				sd.setBlocksDecayed(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("itemsCrafted") ) {
				sd.setItemsCrafted(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("playersKilled") ) {
				sd.setPlayersKilled(Integer.decode(keyval[1])) ;
			} else {
				sd.getMobsKilled().put(keyval[0], Integer.decode(keyval[1])) ;
			}
		}
		return sd ;
	}

}

