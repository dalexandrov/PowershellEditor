package org.powershell.editors.outline;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.powershell.common.Util;

/**
 * The Class PowershellContentOutline.
 * 
 * @author dalexandrov
 */
public class PowershellContentOutline extends ContentOutlinePage {

	public static PowershellContentOutline INSTANCE = new PowershellContentOutline();

	/** The Constant ID. */
	public static final String ID = IPageLayout.ID_OUTLINE;

	private IContentProvider contentProvider;

	private PowershellContentOutline() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.contentoutline.ContentOutlinePage#init(org.eclipse
	 * .ui.part.IPageSite)
	 */
	@Override
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		pageSite.setSelectionProvider(this);
		contentProvider = new ParsedContentProvider();
	}

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
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new ParsedLabelProvider());
		viewer.addSelectionChangedListener(this);
		OutlineModel.INSTANCE.recalculate();
		viewer.setInput(OutlineModel.INSTANCE.getItems());

	}

	public void recalculate() {
		TreeViewer viewer = getTreeViewer();
		OutlineModel.INSTANCE.recalculate();
		viewer.setInput(OutlineModel.INSTANCE.getItems());
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		fireSelectionChanged(event.getSelection());
		// FIXME: find a better way for this
		if (getTreeViewer().getTree().getSelection().length > 0) {
			OutlineItem outlined = (OutlineItem) getTreeViewer().getTree()
					.getSelection()[0].getData();
			((ITextEditor) Util.getActiveEditor()).setHighlightRange(
					outlined.getOffset(), outlined.getName().length(), true);
		}
	}
}
