package org.powershell.editors.partition;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.powershell.Activator;

/**
 * The Class PowershellTextDocumentProvider.
 * 
 * @author dalexandrov
 */
public class PowershellTextDocumentProvider extends TextFileDocumentProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#createFileInfo(java.lang.Object)
	 */
	@Override
	protected FileInfo createFileInfo(Object element) throws CoreException {
		FileInfo info = super.createFileInfo(element);
		if (info == null) {
			info = createEmptyFileInfo();
		}
		IDocument document = info.fTextFileBuffer.getDocument();
		if (document != null) {
			IDocumentExtension3 docExtension = (IDocumentExtension3) document;
			IDocumentPartitioner partitioner = new DebugPartitioner(Activator
					.getDefault().getPowershellPartitionScanner(),
					new String[] { IDocument.DEFAULT_CONTENT_TYPE,
							PowershellPartitionScanner.PS_FUNCTION,
							PowershellPartitionScanner.PS_COMMENT });
			partitioner.connect(document);
			docExtension.setDocumentPartitioner(
					Activator.POWERSHELL_PARTITIONING, partitioner);
		}
		return info;
	}
}
