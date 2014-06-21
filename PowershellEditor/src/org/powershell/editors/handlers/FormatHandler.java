/**
 * 
 */
package org.powershell.editors.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.powershell.common.Util;

/**
 * The Class DefinitionHandler.
 * 
 * @author dalexandrov
 */
public class FormatHandler extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IEditorPart editorPart = Util.getActiveEditor();

		if (editorPart != null) {
			ITextOperationTarget target = (ITextOperationTarget) editorPart
					.getAdapter(ITextOperationTarget.class);
			if (target instanceof ISourceViewer) {
				ISourceViewer textViewer = (ISourceViewer) target;
				((ITextOperationTarget) textViewer)
						.doOperation(ISourceViewer.FORMAT);
			}
		}

		return null;
	}
}
