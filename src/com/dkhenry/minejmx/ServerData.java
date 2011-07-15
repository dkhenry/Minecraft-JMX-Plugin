
package com.dkhenry.minejmx;

import java.util.HashMap;
import java.util.Map;

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
	private int blocksPlaced ; /**< Done */
	private int blocksDestroyed ; /**< Done */
	private int itemsCrafted ;
	private Map<String,Integer> mobsKilled ;/** Done */
	private int playersKilled ; /** Done */
	private long playTime ; /**< Done */
	private int numberOfPlayers ; /**< Done */

	public ServerData() {
		mobsKilled = new HashMap<String,Integer>() ;
	}

	public int getBlocksPlaced() {
		return blocksPlaced;
	}

	public void setBlocksPlaced(int blocksPlaced) {
		this.blocksPlaced = blocksPlaced;
	}

	public void incBlocksPlaced() {
		this.blocksPlaced++ ;
	}

	public int getItemsCrafted() {
		return itemsCrafted;
	}

	public void setItemsCrafted(int itemsCrafted) {
		this.itemsCrafted = itemsCrafted;
	}

	public void incItemsCrafted() {
		this.itemsCrafted++ ;
	}

	public Map<String,Integer> getMobsKilled() {
		return mobsKilled;
	}

	public void setMobsKilled(Map<String,Integer> mobsKilled) {
		this.mobsKilled = mobsKilled;
	}

	public void incMobsKilled(String type) {
		this.mobsKilled.put(type, this.mobsKilled.get(type)+1)  ;
	}

	public long getPlayTime() {
		return playTime;
	}

	public void setPlayTime(long playTime) {
		this.playTime = playTime;
	}

	public void incPlayTimeBy(long ms) {
		this.playTime += ms;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public void incNumberOfPlayers() {
		this.numberOfPlayers++ ;
	}

	public void decNumberofPlayers() {
		this.numberOfPlayers-- ;
	}

	public void setPlayersKilled(int playersKilled) {
		this.playersKilled = playersKilled;
	}

	public int getPlayersKilled() {
		return playersKilled;
	}

	public void incPlayersKilled() {
		this.playersKilled++ ;
	}

	public void setBlocksDestroyed(int blocksDestroyed) {
		this.blocksDestroyed = blocksDestroyed;
	}

	public int getBlocksDestroyed() {
		return blocksDestroyed;
	}

	public void incBlocksDestroyed() {
		this.blocksDestroyed++ ;
	}

	@Override
	public Object getAttribute(String arg0) throws AttributeNotFoundException,
			MBeanException, ReflectionException {

		if(arg0.equals("blocksPlaced")) {
			return getBlocksPlaced() ;
		} else if(arg0.equals("blocksDestroyed")) {
			return getBlocksDestroyed() ;
		}else if(arg0.equals("itemsCrafted")) {
			return getItemsCrafted() ;
		} else if(arg0.equals("playersKilled")) {
			return getPlayersKilled() ;
		} else if(arg0.equals("playTime")) {
			return getPlayTime() ;
		} else if(arg0.equals("mobsKilled")) {
			return this.mobsKilled.get("creeper") +this.mobsKilled.get("skeleton") + this.mobsKilled.get("zombie") +this.mobsKilled.get("spider") ;
		} else if(arg0.equals("creepersKilled")) {
			return this.mobsKilled.get("creeper") ;
		} else if(arg0.equals("skeletonsKilled")) {
			return this.mobsKilled.get("skeleton") ;
		} else if(arg0.equals("zombiesKilled")) {
			return this.mobsKilled.get("zombie") ;
		} else if(arg0.equals("spidersKilled")) {
			return this.mobsKilled.get("spider") ;
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
	    OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[11];

		//Build the Attributes
		attributes[0] = new OpenMBeanAttributeInfoSupport("blocksPlaced","Number of Blocks Placed",SimpleType.INTEGER, true, false,false);
		attributes[1] = new OpenMBeanAttributeInfoSupport("blocksDestroyed","Number of Blocks Destroyed",SimpleType.INTEGER, true, false,false);
		attributes[2] = new OpenMBeanAttributeInfoSupport("itemsCrafted","Number of items Crafted",SimpleType.DOUBLE, true, false,false);
		attributes[3] = new OpenMBeanAttributeInfoSupport("mobsKilled","Number Of Mobs Killed",SimpleType.DOUBLE, true, false,false);
		attributes[4] = new OpenMBeanAttributeInfoSupport("playersKilled","Number Of Players Killed",SimpleType.INTEGER, true, false,false);
		attributes[5] = new OpenMBeanAttributeInfoSupport("playTime","Amount Of Time People have played on this Server",SimpleType.LONG, true, false,false);
		attributes[6] = new OpenMBeanAttributeInfoSupport("mobsKilled","Number Of Mobs Killed",SimpleType.DOUBLE, true, false,false);
		attributes[7] = new OpenMBeanAttributeInfoSupport("creepersKilled","Number of Creepers Killed",SimpleType.INTEGER, true, false,false);
		attributes[8] = new OpenMBeanAttributeInfoSupport("skeletonsKilled","Number of Skeletons Killed",SimpleType.INTEGER, true, false,false);
		attributes[9] = new OpenMBeanAttributeInfoSupport("zombiesKilled","Number of Zombies Killed",SimpleType.INTEGER, true, false,false);
		attributes[10] = new OpenMBeanAttributeInfoSupport("spidersKilled","Number of Spiders Killed",SimpleType.INTEGER, true, false,false);
		
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

	public void decNumberOfPlayers() {
		this.numberOfPlayers-- ;

	}

}
