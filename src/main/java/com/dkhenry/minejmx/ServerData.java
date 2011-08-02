
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
	private long npesKilled ;/** In progress */
	private int playersKilled ; /** Done */
	private long playTime ; /**< Done */
	private int numberOfPlayers ; /**< Done */
	private double playerDistanceMoved = 0.0; /**< In Progress */

	// need to access the plugin object from this one
	private MineJMX plugin;

	public ServerData(MineJMX plugin) {
		this.plugin = plugin;
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

	// npesKilled {{{
	public long getNpesKilled() {
		return this.npesKilled;
	}

	public void setNpesKilled(long npesKilled) {
		this.npesKilled = npesKilled;
	}

	public void incNpesKilled() {
		this.npesKilled++;
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

	@SuppressWarnings("rawtypes")
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
		} else if(arg0.equals("npesKilled")) {
			return this.getNpesKilled();
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
		OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[10];

		//Build the Attributes
		attributes[0] = new OpenMBeanAttributeInfoSupport("blocksPlaced","Number of Blocks Placed",SimpleType.LONG, true, false,false);
		attributes[1] = new OpenMBeanAttributeInfoSupport("blocksDestroyed","Number of Blocks Destroyed",SimpleType.LONG, true, false,false);
		attributes[2] = new OpenMBeanAttributeInfoSupport("blocksSpread", "Number of blocks naturally spread", SimpleType.LONG, true, false, false);
		attributes[3] = new OpenMBeanAttributeInfoSupport("blocksDecayed", "Number of blocks naturally decayed", SimpleType.LONG, true, false, false);
		attributes[4] = new OpenMBeanAttributeInfoSupport("itemsCrafted","Number of items Crafted",SimpleType.LONG, true, false,false);
		attributes[5] = new OpenMBeanAttributeInfoSupport("playersKilled","Number Of Players Killed",SimpleType.INTEGER, true, false,false);
		attributes[6] = new OpenMBeanAttributeInfoSupport("playTime","Amount Of Time People have played on this Server",SimpleType.LONG, true, false,false);
		attributes[7] = new OpenMBeanAttributeInfoSupport("npesKilled","Number of non-Player Entities killed",SimpleType.INTEGER, true, false, false);
		attributes[8] = new OpenMBeanAttributeInfoSupport("numberOfPlayers","Number of Players On Server",SimpleType.INTEGER, true, false,false);
		attributes[9] = new OpenMBeanAttributeInfoSupport("playerDistanceMoved", "Total player distance traveled", SimpleType.DOUBLE, true, false, false);

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
		return "playTime:"+this.playTime+
				",blocksPlaced:"+this.blocksPlaced+
				",blocksDestroyed:"+this.blocksDestroyed+
				",blocksSpread:"+this.blocksSpread+
				",blocksDecayed:"+this.blocksDecayed+
				",itemsCrafted:"+this.itemsCrafted+
				",playersKilled:"+this.playersKilled +
				",npesKilled:" + this.npesKilled +
				",playerDistanceMoved:" + this.playerDistanceMoved;
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
			} else if(keyval[0].equals("npesKilled")) {
				sd.setNpesKilled(Integer.decode(keyval[1]));
			} else if(keyval[0].equals("playerDistanceMoved")) {
				sd.setPlayerDistanceMoved(Double.parseDouble(keyval[1]));
			}
		}
		return sd ;
	}

}

