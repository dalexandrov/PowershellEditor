package org.powershell.editors.outline;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * The Class PowershellContentOutline.
 * 
 * @author dalexandrov
 */
public class PowershellContentOutline extends ContentOutlinePage {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.contentoutline.ContentOutlinePage#createControl(
	 * org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(null);
		viewer.addSelectionChangedListener(this);
		viewer.setInput("data");
	}
}
