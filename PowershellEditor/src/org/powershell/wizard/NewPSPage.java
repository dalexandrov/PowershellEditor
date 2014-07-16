package org.powershell.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.powershell.Activator;

/**
 * The Class NewPSWizard.
 */
public class NewPSPage extends WizardNewFileCreationPage {

	/**
	 * Instantiates a new new ps wizard.
	 *
	 * @param pageName
	 *            the page name
	 * @param selection
	 *            the selection
	 */
	public NewPSPage(IStructuredSelection selection) {
		super("NewPSWizard", selection);

		setTitle("Powershell script");
		setDescription("Creates a new Powershell script");
		setFileExtension("ps1");
		setImageDescriptor(Activator.getImageDescriptor("icons/powershell_big.png"));
	}

}
