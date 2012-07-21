package org.powershell.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

/**
 * The Class PSPerspective.
 * 
 * @author dalexandrov
 */
public class PSPerspective implements IPerspectiveFactory {

	/** The Constant VIEW_ID. */
	private static final String VIEW_ID = "org.eclipse.powershell.editor.PowershellEditor";

	/** The Constant CONSOLE_ID. */
	private static final String CONSOLE_ID = "org.eclipse.powershell.console.ConsoleDisplayMgr";

	/** The Constant BOTTOM. */
	private static final String BOTTOM = "bottom";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui
	 * .IPageLayout)
	 */
	@Override
	public void createInitialLayout(IPageLayout myLayout) {

		myLayout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT,
				0.30f, myLayout.getEditorArea());

		IFolderLayout bot = myLayout.createFolder(BOTTOM, IPageLayout.BOTTOM,
				0.76f, myLayout.getEditorArea());
		bot.addPlaceholder(VIEW_ID);

		String editorArea = myLayout.getEditorArea();
		myLayout.setEditorAreaVisible(false);

		myLayout.addStandaloneView(CONSOLE_ID, true, IPageLayout.BOTTOM, .35f,
				editorArea);
		myLayout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM,
				.5f, editorArea);

	}
}
