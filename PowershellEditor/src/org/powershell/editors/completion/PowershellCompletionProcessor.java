package org.powershell.editors.completion;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.PlatformUI;
import org.powershell.common.Logger;
import org.powershell.editors.completion.dictionaries.InterFileDictionary;
import org.powershell.editors.completion.dictionaries.MemberDictionary;
import org.powershell.editors.completion.dictionaries.PowershellDictionary;
import org.powershell.editors.completion.helpers.DictItem;

/**
 * The Class PowershellCompletionProcessor.
 * 
 * @author dalexandrov
 */
public class PowershellCompletionProcessor implements IContentAssistProcessor {

	/** The empty proposal. */
	private final ICompletionProposal[] emptyProposal = new ICompletionProposal[0];

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * computeCompletionProposals(org.eclipse.jface.text.ITextViewer, int)
	 */
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {

		InterFileDictionary interDict = null;

		IResource activeFile = getActiveFile();

		String prefix = getPrefix(viewer.getDocument(), offset);

		if (activeFile == null) {
			// Cannot locate this active file return no completion
			return emptyProposal;
		}
		String activeFileName = activeFile.getLocation().toFile()
				.getAbsolutePath();

		try {
			interDict = InterFileDictionary.getInstance(activeFile.getProject()
					.getName());
		} catch (CoreException ce) {
			Logger.getInstance().logError("Error getting proposals", ce);
		}

		List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
		List<DictItem> iterateOver = new ArrayList<DictItem>();
		iterateOver.addAll(interDict.getProposals(prefix, activeFileName));
		iterateOver.addAll(MemberDictionary.getInstance().getProposals(prefix));
		iterateOver.addAll(PowershellDictionary.getInstance().getProposals(
				prefix));

		for (DictItem iterable : iterateOver) {
			String displayString = iterable.getItem() + " : "
					+ iterable.getType() + " - " + iterable.getDescription();
			CompletionProposal prop = new CompletionProposal(
					iterable.getItem(), offset - prefix.length(),
					prefix.length(), iterable.getItem().length(),
					iterable.getImage(), displayString, null, null);
			result.add(prop);

		}

		return result.toArray(new ICompletionProposal[result.size()]);

	}

	/**
	 * Gets the prefix.
	 * 
	 * @param doc
	 *            the doc
	 * @param offset
	 *            the offset
	 * @return the prefix
	 */
	private String getPrefix(IDocument doc, int offset) {
		char ch;
		try {
			for (int i = offset - 1; i > 0; i--) {
				ch = doc.getChar(i);
				if (!Character.isLetterOrDigit(ch) && ch != '-') {
					return doc.get(i + 1, offset - i - 1);
				}
			}
		} catch (BadLocationException ble) {
			Logger.getInstance().logError("Error getting prefix", ble);
		}

		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * computeContextInformation(org.eclipse.jface.text.ITextViewer, int)
	 */
	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * getCompletionProposalAutoActivationCharacters()
	 */
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { ':' };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * getContextInformationAutoActivationCharacters()
	 */
	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return new char[] { '.' };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * getContextInformationValidator()
	 */
	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.contentassist.IContentAssistProcessor#getErrorMessage
	 * ()
	 */
	@Override
	public String getErrorMessage() {
		return null;
	}

	/**
	 * Gets the active editor.
	 * 
	 * @return the active editor
	 */
	private static IEditorPart getActiveEditor() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();
	}

	/**
	 * Gets the active file.
	 * 
	 * @return the active file
	 */
	private static IResource getActiveFile() {
		IEditorPart editorPart = getActiveEditor();
		IEditorInput editorInput = editorPart.getEditorInput();
		if (editorInput instanceof IURIEditorInput) {
			IFile[] files = ResourcesPlugin
					.getWorkspace()
					.getRoot()
					.findFilesForLocationURI(
							((IURIEditorInput) editorInput).getURI());

			return (files.length == 1) ? files[0] : null;
		}

		return null;
	}
}
