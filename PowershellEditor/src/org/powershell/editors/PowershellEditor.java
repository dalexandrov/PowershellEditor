package org.powershell.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.powershell.editors.outline.PowershellContentOutline;
import org.powershell.editors.partition.PowershellDocumentProvider;
import org.powershell.editors.partition.PowershellTextDocumentProvider;

/**
 * The Class PowershellEditor.
 * 
 * @author dalexandrov
 */
public class PowershellEditor extends TextEditor {

	/** The Constant EDITOR_MATCHING_BRACKETS. */
	public final static String EDITOR_MATCHING_BRACKETS = "matchingBrackets";
	
	/** The Constant EDITOR_MATCHING_BRACKETS_COLOR. */
	public final static String EDITOR_MATCHING_BRACKETS_COLOR = "matchingBracketsColor";

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
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.editors.text.TextEditor#doSetInput(org.eclipse.ui.IEditorInput)
	 */
	@Override
	protected final void doSetInput(IEditorInput input) throws CoreException {
		setDocumentProvider(createDocumentProvider(input));
		super.doSetInput(input);
	}

	/**
	 * Creates the document provider.
	 *
	 * @param input the input
	 * @return the i document provider
	 */
	private IDocumentProvider createDocumentProvider(IEditorInput input) {
		if(input instanceof IFileEditorInput){
			return new PowershellDocumentProvider();
		} else if(input instanceof IStorageEditorInput){
			return new PowershellTextDocumentProvider();
		} else {
			return new PowershellTextDocumentProvider();
		}
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

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#configureSourceViewerDecorationSupport(org.eclipse.ui.texteditor.SourceViewerDecorationSupport)
	 */
	@Override
	protected void configureSourceViewerDecorationSupport(
			SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);

		char[] matchChars = { '(', ')', '[', ']', '{', '}' }; // which brackets
																// to match
		ICharacterPairMatcher matcher = new DefaultCharacterPairMatcher(
				matchChars, IDocumentExtension3.DEFAULT_PARTITIONING);
		support.setCharacterPairMatcher(matcher);
		support.setMatchingCharacterPainterPreferenceKeys(
				EDITOR_MATCHING_BRACKETS, EDITOR_MATCHING_BRACKETS_COLOR);

		// Enable bracket highlighting in the preference store
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(EDITOR_MATCHING_BRACKETS, true);
		store.setDefault(EDITOR_MATCHING_BRACKETS_COLOR, "128,128,128");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.editors.text.TextEditor#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IContentOutlinePage.class)) {

			return getContentOutlinePage();

		} else {
			return super.getAdapter(key);
		}
	}

	/**
	 * Gets the content outline page.
	 *
	 * @return the content outline page
	 */
	public IContentOutlinePage getContentOutlinePage() {
		return PowershellContentOutline.INSTANCE;
	}
}
