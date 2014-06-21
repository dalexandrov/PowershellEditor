package org.powershell.editors.completion.helpers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.powershell.Activator;

/**
 * The Class DictItem.
 * 
 * @author dalexandrov
 */
public final class DictItem {

	/** The images. */
	private static Map<String, Image> images = new HashMap<String, Image>();

	// TODO: Is this the correct place?
	static {
		images.put("Cmdlet", Activator.getImageDescriptor("icons/cmdlet.png")
				.createImage());
		images.put("Alias", Activator.getImageDescriptor("icons/alias.png")
				.createImage());
		images.put("Function",
				Activator.getImageDescriptor("icons/function.gif")
						.createImage());
		images.put("Variable",
				Activator.getImageDescriptor("icons/variable.gif")
						.createImage());
	}

	/** The item. */
	private final String item;

	/** The description. */
	private final String description;

	/** The type. */
	private final String type; // thing about enum

	/**
	 * Instantiates a new dict item.
	 * 
	 * @param item
	 *            the item
	 * @param type
	 *            the type
	 * @param description
	 *            the description
	 */
	public DictItem(String item, String type, String description) {
		super();
		this.item = item;
		this.type = type;
		this.description = description;
	}

	/**
	 * Gets the image.
	 * 
	 * @return the image
	 */
	public Image getImage() {
		return images.get(type);
	}

	/**
	 * Gets the item.
	 * 
	 * @return the item
	 */
	public String getItem() {
		return item;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}
}