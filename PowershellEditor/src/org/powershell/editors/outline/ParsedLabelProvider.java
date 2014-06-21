/**
 * 
 */
package org.powershell.editors.outline;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.powershell.Activator;

/**
 * The Class ParsedLabelProvider.
 * 
 * @author dalexandrov
 */
public class ParsedLabelProvider extends LabelProvider {

	// returns images based upon element type. Currently only functions are
	// supported.
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return Activator.getDefault().getImageRegistry().get("Function");
	}
}