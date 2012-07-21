package org.powershell.editors;

import org.eclipse.ui.editors.text.TextEditor;
import org.powershell.editors.partition.PowershellDocumentProvider;

/**
 * The Class PowershellEditor.
 * 
 * @author dalexandrov
 */
public class PowershellEditor extends TextEditor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.editors.text.TextEditor#initializeEditor()
	 */
	@Override
	protected void initializeEditor() {
		super.initializeEditor();
		setSourceViewerConfiguration(new PowershellEditorConfiguration());
	}

	/**
	 * Instantiates a new powershell editor.
	 */
	public PowershellEditor() {
		super();
		setSourceViewerConfiguration(new PowershellEditorConfiguration());
		setDocumentProvider(new PowershellDocumentProvider());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.editors.text.TextEditor#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	// public Object getAdapter(Class key) {
	// if (key.equals(IContentOutlinePage.class)) {
	//
	// return getContentOutlinePage();
	//
	// }
	// else {
	// return super.getAdapter(key);
	// }
	// }
	//
	// public IContentOutlinePage getContentOutlinePage() {
	// return new PowershellContentOutline();
	// }
}
