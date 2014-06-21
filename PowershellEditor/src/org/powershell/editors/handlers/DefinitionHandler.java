/**
 * 
 */
package org.powershell.editors.handlers;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.powershell.common.Logger;
import org.powershell.common.Util;
import org.powershell.editors.completion.dictionaries.InterFileDictionary;

/**
 * The Class DefinitionHandler.
 * 
 * @author dalexandrov
 */
public class DefinitionHandler extends AbstractHandler {

	/** The logger. */
	private final Logger logger = Logger.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection selection = ((ITextEditor) Util.getActiveEditor())
				.getSelectionProvider().getSelection();
		TextSelection tselection = null;
		if (selection instanceof TextSelection) {
			tselection = (TextSelection) selection;
		} else {
			return null;
		}

		String functionName = getWord(Util.getActiveDocument(event).get(),
				tselection.getOffset());
		ITextEditor targetEditor = null;
		try {
			InterFileDictionary dict = InterFileDictionary.getInstance(Util
					.getActiveFile().getProject().getName());
			String fileName = dict.getFileData(
					Util.getActiveFile().getLocation().toFile()
							.getAbsolutePath()).getFunctionOwner(functionName,
					dict);
			if (fileName == null)
				return null;
			targetEditor = (ITextEditor) openEditor(fileName);
		} catch (CoreException e) {
			logger.logError(
					"Error while trying to get Completion dictionary object", e);
		}

		if (targetEditor != null) {
			int offset = Util.getActiveDocument(event).get()
					.indexOf(functionName + "(");
			targetEditor.setHighlightRange(offset, functionName.length(), true);
		}

		return null;
	}

	/**
	 * Gets the word.
	 * 
	 * @param document
	 *            the document
	 * @param offset
	 *            the offset
	 * @return the word
	 */
	private String getWord(String document, int offset) {
		document += "\n";// FIXME: kind of ugly.
		int startIndex = offset;
		int endIndex = offset;
		while (Character.isLetterOrDigit(document.charAt(startIndex)))
			startIndex--;
		while (Character.isLetterOrDigit(document.charAt(endIndex)))
			endIndex++;
		startIndex++;

		return document.substring(startIndex, endIndex);
	}

	/**
	 * Open editor.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the i editor part
	 */
	private IEditorPart openEditor(String fileName) {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFile files[];
		try {
			files = root.findFilesForLocationURI(new File(fileName).toURI());
			if (files.length != 0) {
				IEditorDescriptor desc = PlatformUI.getWorkbench()
						.getEditorRegistry()
						.getDefaultEditor(files[0].getName());
				return page.openEditor(new FileEditorInput(files[0]),
						desc.getId());
			}
		} catch (PartInitException e) {
			logger.logError("Unable to open editor", e);
		}

		return null;
	}
}
