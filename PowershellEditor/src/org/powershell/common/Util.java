/**
 * 
 */
package org.powershell.common;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * The Class Util.
 * 
 * @author dalexandrov
 */
public class Util {

	/**
	 * Gets the active editor.
	 * 
	 * @return the active editor
	 */
	public static IEditorPart getActiveEditor() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();
	}

	/**
	 * Gets the active file.
	 * 
	 * @return the active file
	 */
	public static IResource getActiveFile() {
		IEditorPart editorPart = getActiveEditor();
		IEditorInput editorInput = editorPart.getEditorInput();
		if (editorInput instanceof IPathEditorInput) {
			IFile[] files = ResourcesPlugin
					.getWorkspace()
					.getRoot()
					.findFilesForLocationURI(
							((IURIEditorInput) editorInput).getURI());
			return (files.length == 1) ? files[0] : null;
		}

		return null;
	}

	/**
	 * Gets the active document.
	 * 
	 * @param event
	 *            the event
	 * @return the active document
	 */
	public static IDocument getActiveDocument(ExecutionEvent event) {
		IEditorPart part = HandlerUtil.getActiveEditor(event);
		IDocument doc = null;
		if ((part != null) && (part instanceof AbstractTextEditor)) {
			ITextEditor editor = (ITextEditor) part;
			doc = editor.getDocumentProvider().getDocument(
					editor.getEditorInput());
		}

		return doc;
	}

}
