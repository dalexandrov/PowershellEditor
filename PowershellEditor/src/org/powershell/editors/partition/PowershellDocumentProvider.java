package org.powershell.editors.partition;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.powershell.Activator;

/**
 * The Class PowershellDocumentProvider.
 * 
 * @author dalexandrov
 */
public class PowershellDocumentProvider extends FileDocumentProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.editors.text.StorageDocumentProvider#createDocument(java
	 * .lang.Object)
	 */
	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		IDocumentExtension3 docExtension = (IDocumentExtension3) document;
		if (document != null) {
			IDocumentPartitioner partitioner = new DebugPartitioner(Activator
					.getDefault().getPowershellPartitionScanner(),
					new String[] { IDocument.DEFAULT_CONTENT_TYPE,
							PowershellPartitionScanner.PS_FUNCTION,
							PowershellPartitionScanner.PS_COMMENT });
			partitioner.connect(document);
			docExtension.setDocumentPartitioner(
					Activator.POWERSHELL_PARTITIONING, partitioner);
		}
		return document;
	}
}