package org.powershell.editors.partition;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

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
		if (document != null) {
			IDocumentPartitioner partitioner = new DebugPartitioner(
					new PowershellPartitionScanner(), new String[] {
							PowershellPartitionScanner.PS_FUNCTION,
							PowershellPartitionScanner.PS_COMMENT });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}