
package com.dkhenry.minejmx;

import java.sql.ResultSet;
import java.sql.SQLException;
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


public class ServerPerformanceData implements DynamicMBean {

	private MineJMX plugin ; 
	
	private long serverTicks ; 	
	private long runningTasks ; 
	private long pendingTasks ; 
	private int ticksPerSecondAverage ; 
	private int ticksPerSecondInstantious ; 
	private int serverLag ; 
		
	public ServerPerformanceData(MineJMX instance) { 
		plugin = instance ; 
	}

	@Override
	public Object getAttribute(String arg0) throws AttributeNotFoundException,
			MBeanException, ReflectionException {
		if(arg0.equals("serverTicks")) {
			return getServerTicks() ;
		} else if(arg0.equals("runningTasks")) {
			return getRunningTasks() ;
		} else if(arg0.equals("pendingTasks")) {
			return getPendingTasks() ;
		} else if(arg0.equals("ticksPerSecondAverage")) {
			return getTicksPerSecondAverage() ;
		} else if(arg0.equals("ticksPerSecondInstantious")) {
			return getTicksPerSecondInstantious() ;
		} else if(arg0.equals("serverLag")) {
			return getServerLag() ;
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
		OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[6];

		//Build the Attributes
		attributes[0] = new OpenMBeanAttributeInfoSupport("serverTicks","Number or Total Server Ticks",SimpleType.LONG, true, false,false);		
		attributes[1] = new OpenMBeanAttributeInfoSupport("runningTasks","Number or Running Tasks on this Server",SimpleType.LONG, true, false,false);
		attributes[2] = new OpenMBeanAttributeInfoSupport("pendingTasks","Number or Total Server Ticks",SimpleType.LONG, true, false,false);
		attributes[3] = new OpenMBeanAttributeInfoSupport("ticksPerSecondAverage","Average Number of Ticks Per Second ",SimpleType.INTEGER, true, false,false);
		attributes[4] = new OpenMBeanAttributeInfoSupport("ticksPerSecondInstantious","Number of Ticks Per Second ",SimpleType.INTEGER, true, false,false);
		attributes[5] = new OpenMBeanAttributeInfoSupport("serverLag","Generic Server Lag Indicator ( In Ticks per Second )",SimpleType.INTEGER, true, false,false);
		
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

	public long getServerTicks() {
		return serverTicks;
	}

	public void setServerTicks(long serverTicks) {
		this.serverTicks = serverTicks;
	}
	
	public void addTicks(long ServerTicks) { 
		this.serverTicks+=ServerTicks ; 
	}

	public long getRunningTasks() {
		return plugin.getServer().getScheduler().getActiveWorkers().size() ; 
	}

	public void setRunningTasks(long runningTasks) {		
	}

	public long getPendingTasks() {
		return plugin.getServer().getScheduler().getPendingTasks().size() ; 
	}

	public void setPendingTasks(long pendingTasks) {		
	}

	public int getTicksPerSecondAverage() {
		return ticksPerSecondAverage;
	}

	public void setTicksPerSecondAverage(int ticksPerSecondAverage) {
		this.ticksPerSecondAverage = ticksPerSecondAverage;
	}

	public int getTicksPerSecondInstantious() {
		return ticksPerSecondInstantious;
	}

	public void setTicksPerSecondInstantious(int ticksPerSecondInstantious) {
		this.ticksPerSecondInstantious = ticksPerSecondInstantious;
	}
	
	public void setTickRate(int ticksPerSecond) { 
		this.ticksPerSecondInstantious = ticksPerSecond ; 
		this.ticksPerSecondAverage = (this.ticksPerSecondAverage*19 + ticksPerSecond) / 20 ;  
	}

	public int getServerLag() {		
		return this.ticksPerSecondAverage - this.ticksPerSecondInstantious  ;  		
	}

	public void setServerLag(int serverLag) {
		
	}
	
	public String getMetricData() {		 
		String rvalue = "" ;
		return "serverTicks:"+this.serverTicks+
				",runningTasks:"+this.runningTasks+
				",pendingTasks:"+this.pendingTasks+
				",ticksPerSecondAverage:"+this.ticksPerSecondAverage+
				",ticksPerSecondInstantious:"+this.ticksPerSecondInstantious+
				",serverLag:"+this.serverLag ; 				
	}

	public static ServerPerformanceData instanceFromResultSet(ResultSet rs, MineJMX plugin) throws SQLException {
		ServerPerformanceData sd = new ServerPerformanceData(plugin) ; 
		String data = rs.getString("data") ;
		if(data.length() <=0 ) {
			return sd ;
		}
		String[] datas = data.split(",") ;		
		for(String s : datas) {
			String[] keyval = s.split(":") ;
			if( keyval[0].equals("serverTicks") ) {
				sd.setServerTicks(Long.decode(keyval[1])) ;
			} else if( keyval[0].equals("runningTasks") ) {
				sd.setRunningTasks(Long.decode(keyval[1])) ;
			} else if( keyval[0].equals("pendingTasks") ) {
				sd.setPendingTasks(Long.decode(keyval[1])) ;
			} else if( keyval[0].equals("ticksPerSecondAverage") ) {
				sd.setTicksPerSecondAverage(Integer.decode(keyval[1])) ;
			} else if( keyval[0].equals("ticksPerSecondInstantious") ) {
				sd.setTicksPerSecondInstantious(Integer.decode(keyval[1])) ;
			}  else if( keyval[0].equals("serverLag") ) {
				sd.setServerLag(Integer.decode(keyval[1])) ;
			}  
		}
		return sd ;
	}
}
