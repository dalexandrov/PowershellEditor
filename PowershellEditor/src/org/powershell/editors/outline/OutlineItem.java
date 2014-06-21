/**
 * 
 */
package org.powershell.editors.outline;

/**
 * The Class OutlineItem.
 * 
 * @author dalexandrov
 */
public class OutlineItem {

	/** The name. */
	private final String name;

	/** The offset. */
	private final int offset;

	/**
	 * Instantiates a new outline item.
	 * 
	 * @param name
	 *            the name
	 * @param offset
	 *            the offset
	 */
	public OutlineItem(String name, int offset) {
		this.name = name;
		this.offset = offset;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the offset.
	 * 
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
