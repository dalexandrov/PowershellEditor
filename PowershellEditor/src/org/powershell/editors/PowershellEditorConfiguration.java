package org.powershell.editors;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.powershell.Activator;
import org.powershell.editors.completion.PowershellCompletionProcessor;
import org.powershell.editors.formatting.TextFormattingStrategy;
import org.powershell.editors.partition.PowershellPartitionScanner;

// TODO: Auto-generated Javadoc
/**
 * The Class PowershellEditorConfiguration.
 * 
 * @author dalexandrov
 */
public class PowershellEditorConfiguration extends SourceViewerConfiguration {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredDocumentPartitioning(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return Activator.POWERSHELL_PARTITIONING;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				PowershellPartitionScanner.PS_COMMENT,
				PowershellPartitionScanner.PS_FUNCTION };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getContentFormatter(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		ContentFormatter formatter = new ContentFormatter();
		formatter.setFormattingStrategy(new TextFormattingStrategy(),
				IDocument.DEFAULT_CONTENT_TYPE);
		formatter.setFormattingStrategy(new TextFormattingStrategy(),
				PowershellPartitionScanner.PS_COMMENT);
		formatter.setFormattingStrategy(new TextFormattingStrategy(),
				PowershellPartitionScanner.PS_FUNCTION);
		return formatter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler
				.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
				new PowershellScanner());
		reconciler.setDamager(dr, PowershellPartitionScanner.PS_FUNCTION);
		reconciler.setRepairer(dr, PowershellPartitionScanner.PS_FUNCTION);

		dr = new DefaultDamagerRepairer(new PowershellScanner());
		reconciler.setDamager(dr, PowershellPartitionScanner.PS_COMMENT);
		reconciler.setRepairer(dr, PowershellPartitionScanner.PS_COMMENT);

		dr = new DefaultDamagerRepairer(new PowershellScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getContentAssistant(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		IContentAssistProcessor proc = new PowershellCompletionProcessor();
		DialogSettings dsettings = new DialogSettings("pscoderContentAssist");
		assistant.setContentAssistProcessor(proc,
				IDocument.DEFAULT_CONTENT_TYPE);
		assistant.enableAutoActivation(true);
		assistant.setRestoreCompletionProposalSize(dsettings);

		return assistant;
	}
}