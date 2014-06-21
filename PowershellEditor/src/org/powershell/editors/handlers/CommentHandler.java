/**
 * 
 */
package org.powershell.editors.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.powershell.common.Logger;
import org.powershell.common.Util;

/**
 * The Class CommentHandler.
 * 
 * @author dalexandrov
 */
public class CommentHandler extends AbstractHandler {

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
		IDocument doc = Util.getActiveDocument(event);
		if (doc == null) {
			return null;
		}

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if ((selection != null) && (selection instanceof TextSelection)) {
			TextSelection docSelection = (TextSelection) selection;
			try {
				int lineStart = docSelection.getStartLine();
				int lineEnd = docSelection.getEndLine();
				boolean shouldComment = shouldComment(lineStart, lineEnd, doc);
				for (int i = lineStart; i <= lineEnd; i++) {
					if (shouldComment)
						doc.replace(doc.getLineOffset(i), 0, "#");
					else
						doc.replace(doc.getLineOffset(i), 1, "");
				}
			} catch (BadLocationException e) {
				logger.logError("Unable to comment", e);
			}
		}

		return null;
	}

	/**
	 * Should comment.
	 * 
	 * @param lineStart
	 *            the line start
	 * @param lineEnd
	 *            the line end
	 * @param doc
	 *            the doc
	 * @return true, if successful
	 * @throws BadLocationException
	 *             the bad location exception
	 */
	private boolean shouldComment(int lineStart, int lineEnd, IDocument doc)
			throws BadLocationException {
		boolean addComment = false;
		for (int i = lineStart; i <= lineEnd; i++) {
			if (doc.getChar(doc.getLineOffset(lineStart)) != '#') {
				addComment = true;
			}
		}

		return addComment;
	}
}
