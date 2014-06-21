/**
 * 
 */
package org.powershell.editors.outline;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPartitioningException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.ui.texteditor.ITextEditor;
import org.powershell.Activator;
import org.powershell.common.Logger;
import org.powershell.common.Util;
import org.powershell.editors.partition.PowershellPartitionScanner;

/**
 * The Enum OutlineModel.
 * 
 * @author mitia
 */
public enum OutlineModel {

	/** The instance. */
	INSTANCE;

	/** The function pattern. */
	Pattern functionPattern = Pattern.compile(
			"function ([\\w\\-]*)[\\s|\\{|\\(]", Pattern.CASE_INSENSITIVE);

	/** The items. */
	private final List<OutlineItem> items = new ArrayList<OutlineItem>();

	/**
	 * Gets the items.
	 * 
	 * @return the items
	 */
	public List<OutlineItem> getItems() {
		return items;
	}

	/**
	 * Recalculate.
	 */
	public void recalculate() {
		List<OutlineItem> oldItems = new ArrayList<OutlineItem>();
		oldItems.addAll(items);
		ITextEditor editor = (ITextEditor) Util.getActiveEditor();
		IDocument document = editor.getDocumentProvider().getDocument(
				editor.getEditorInput());
		if (document != null) {
			items.clear();
			try {
				IDocumentExtension3 extension3 = (IDocumentExtension3) document;
				for (ITypedRegion pos : extension3.computePartitioning(
						Activator.POWERSHELL_PARTITIONING, 0,
						document.getLength(), false)) {
					if (pos.getType().equals(
							PowershellPartitionScanner.PS_FUNCTION)) {
						String part = document.get(pos.getOffset(),
								pos.getLength());
						Matcher m = functionPattern.matcher(part);
						if (m.find()) {
							items.add(new OutlineItem(m.group(1), pos
									.getOffset()));
						}
					}
				}
			} catch (BadLocationException e) {
				Logger.getInstance().logError("Location problem", e);
			} catch (BadPartitioningException e) {
				Logger.getInstance().logError("Partitioning problem", e);
			}
		}
	}
}
