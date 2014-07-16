package org.powershell.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * The Class NewPSWizard.
 */
public class NewPSWizard extends Wizard implements INewWizard {

	/** The selection. */
	private IStructuredSelection selection;
	
	/** The new ps page. */
	private NewPSPage newPSPage;
	
	/** The workbench. */
	@SuppressWarnings("unused")
	private IWorkbench workbench;

	/**
	 * Instantiates a new new ps wizard.
	 */
	public NewPSWizard() {
		super();
		setWindowTitle("New Powershell Script");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {

		newPSPage = new NewPSPage(selection);
		addPage(newPSPage);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		IFile file = newPSPage.createNewFile();
		if (file != null)
			return true;
		else
			return false;
	};

}
