package org.powershell.editors.partition;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

/**
 * The Class DebugPartitioner. This is a helper class. May be removed in future.
 * 
 * @author dalexandrov
 */

public class DebugPartitioner extends FastPartitioner {

	/**
	 * Instantiates a new debug partitioner.
	 * 
	 * @param scanner
	 *            the scanner
	 * @param legalContentTypes
	 *            the legal content types
	 */
	public DebugPartitioner(IPartitionTokenScanner scanner,
			String[] legalContentTypes) {
		super(scanner, legalContentTypes);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.rules.FastPartitioner#connect(org.eclipse.jface
	 * .text.IDocument, boolean)
	 */
	@Override
	public void connect(IDocument document, boolean delayInitialise) {
		super.connect(document, delayInitialise);
		printPartitions(document);
	}

	/**
	 * Prints the partitions.
	 * 
	 * @param document
	 *            the document
	 */
	public void printPartitions(IDocument document) {
		StringBuffer buffer = new StringBuffer();

		ITypedRegion[] partitions = computePartitioning(0, document.getLength());
		for (int i = 0; i < partitions.length; i++) {
			try {
				buffer.append("Partition type: " + partitions[i].getType()
						+ ", offset: " + partitions[i].getOffset()
						+ ", length: " + partitions[i].getLength());
				buffer.append("\n");
				buffer.append("Text:\n");
				buffer.append(document.get(partitions[i].getOffset(),
						partitions[i].getLength()));
				buffer.append("\n---------------------------\n\n\n");
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		System.out.print(buffer);
	}

}
