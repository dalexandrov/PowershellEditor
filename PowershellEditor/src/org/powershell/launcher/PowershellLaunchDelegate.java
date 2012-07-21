package org.powershell.launcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.powershell.console.ConsoleDisplayMgr;

/**
 * The Class PowershellLaunchDelegate.
 * 
 * @author dalexandrov
 */
public class PowershellLaunchDelegate implements ILaunchConfigurationDelegate {

	/**
	 * Launch.
	 * 
	 * @param configuration
	 *            the configuration
	 * @param mode
	 *            the mode
	 * @param launch
	 *            the launch
	 * @param monitor
	 *            the monitor
	 * @throws CoreException
	 *             the core exception
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration,
	 *      java.lang.String, org.eclipse.debug.core.ILaunch,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {

		// cleaning screen
		ConsoleDisplayMgr.getDefault().clear();

		for (IResource resource : configuration.getMappedResources()) {
			// print info
			ConsoleDisplayMgr.getDefault().println(
					resource.getRawLocation().makeAbsolute().toString(),
					ConsoleDisplayMgr.MSG_INFORMATION);
			// the command
			String[] run_command = new String[] { "powershell.exe",
					resource.getRawLocation().makeAbsolute().toString() };
			BufferedReader reader = null;
			try {
				Process proc = Runtime.getRuntime().exec(run_command);
				proc.getOutputStream().close();
				reader = new BufferedReader(new InputStreamReader(
						proc.getInputStream()));
				String newLine = null;
				while ((newLine = reader.readLine()) != null) {
					ConsoleDisplayMgr.getDefault().println(newLine,
							ConsoleDisplayMgr.MSG_INFORMATION);
				}
				reader = new BufferedReader(new InputStreamReader(
						proc.getErrorStream()));
				newLine = null;
				while ((newLine = reader.readLine()) != null) {
					ConsoleDisplayMgr.getDefault().println(newLine,
							ConsoleDisplayMgr.MSG_INFORMATION);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}