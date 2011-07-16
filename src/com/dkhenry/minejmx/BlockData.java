package com.dkhenry.minejmx;

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

public class BlockData implements DynamicMBean {
	private long blocksPlaced =0 ; /**< Done */
	private long blocksDestroyed = 0 ; /**< Done */
	private long blocksSpread = 0; /**< Done */
	private long blocksDecayed = 0; /**< Done */

	// blocksPlaced {{{
	public void setBlocksPlaced(long blocksPlaced) {
		this.blocksPlaced = blocksPlaced;
	}

	public long getBlocksPlaced() {
		return blocksPlaced;
	}

	public void incBlocksPlaced() {
		this.blocksPlaced++ ;
	}
	// }}}

	// blocksDestroyed {{{
	public void setBlocksDestroyed(long blocksDestroyed) {
		this.blocksDestroyed = blocksDestroyed;
	}

	public long getBlocksDestroyed() {
		return blocksDestroyed;
	}

	public void incBlocksDestroyed() {
		this.blocksDestroyed++ ;
	}
	// }}}

	// blocksSpread {{{
	public void setBlocksSpread(long blocksSpread) {
		this.blocksSpread = blocksSpread;
	}

	public long getBlocksSpread() {
		return this.blocksSpread;
	}

	public void incBlocksSpread() {
		this.blocksSpread++;
	}
	// }}}

	// blocksDecayed {{{
	public void setBlocksDecayed(long blocksDecayed) {
		this.blocksDecayed = blocksDecayed;
	}

	public long getBlocksDecayed() {
		return this.blocksDecayed;
	}

	public void incBlocksDecayed() {
		this.blocksDecayed++;
	}
	// }}}

	@Override
	public Object getAttribute(String arg0) throws AttributeNotFoundException,
			MBeanException, ReflectionException {
		if(arg0.equals("blocksPlaced")) {
			return getBlocksPlaced() ;
		} else if(arg0.equals("blocksDestroyed")) {
			return getBlocksDestroyed();
		} else if(arg0.equals("blocksSpread")) {
			return this.getBlocksSpread();
		} else if(arg0.equals("blocksDecayed")) {
			return this.getBlocksDecayed();
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
	    OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[4];

		//Build the Attributes
		attributes[0] = new OpenMBeanAttributeInfoSupport("blocksPlaced","Number of Total Blocks Placed",SimpleType.LONG, true, false,false);
		attributes[1] = new OpenMBeanAttributeInfoSupport("blocksDestroyed","Number of Total Blocks Destroyed",SimpleType.LONG, true, false,false);
		attributes[2] = new OpenMBeanAttributeInfoSupport("blocksSpread", "Number of Total Blocks Spread", SimpleType.LONG, true, false, false);
		attributes[3] = new OpenMBeanAttributeInfoSupport("blocksDecayed", "Number of total leaves decayed", SimpleType.LONG, true, false, false);

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
		throw new AttributeNotFoundException("No attributes can be set on this MBean");
	}

	@Override
	public AttributeList setAttributes(AttributeList arg0) {
		return new AttributeList() ;
	}
}

