package org.powershell.launcher;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;

/**
 * The Class PSLauncher.
 * 
 * @author dalexandrov
 */
public class PSLauncher implements ILaunchConfigurationTabGroup {

	/** The tabs. */
	private final ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[2];

	/**
	 * Instantiates a new pS launcher.
	 */
	public PSLauncher() {
		tabs[0] = new PowershellMainTab();
		tabs[1] = new org.eclipse.debug.ui.CommonTab();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(org.eclipse
	 * .debug.ui.ILaunchConfigurationDialog, java.lang.String)
	 */
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		dialog.setActiveTab(tabs[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#getTabs()
	 */
	@Override
	public ILaunchConfigurationTab[] getTabs() {
		return tabs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTabGroup#initializeFrom(org.
	 * eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		tabs[0].initializeFrom(configuration);
		tabs[1].initializeFrom(configuration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTabGroup#launched(org.eclipse
	 * .debug.core.ILaunch)
	 */
	@Override
	public void launched(ILaunch launch) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTabGroup#performApply(org.eclipse
	 * .debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		tabs[0].performApply(configuration);
		tabs[1].performApply(configuration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTabGroup#setDefaults(org.eclipse
	 * .debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		tabs[0].setDefaults(configuration);
		tabs[1].setDefaults(configuration);
	}
}