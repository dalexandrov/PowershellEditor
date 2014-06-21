package org.powershell.launcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.powershell.common.Logger;

/**
 * The Class PowershellLaunchShortcut.
 */
public class PowershellLaunchShortcut implements ILaunchShortcut {

	/** The Constant DEBUG_LAUNCH_CONFIGURATION. */
	private static final String DEBUG_LAUNCH_CONFIGURATION = "org.powershell.launcher.PSLauncher";

	/**
	 * Locates a launchable entity in the given selection and launches an
	 * application in the specified mode.
	 * 
	 * @param selection
	 *            workbench selection
	 * @param mode
	 *            one of the launch modes defined by the launch manager
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers.ISelection,
	 *      java.lang.String)
	 * @see org.eclipse.debug.core.ILaunchManager
	 */
	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			if (((IStructuredSelection) selection).getFirstElement() instanceof IFile) {
				launch((IFile) ((IStructuredSelection) selection)
						.getFirstElement(),
						mode);
			}
		}
	}

	/**
	 * Locates a launchable entity in the given active editor, and launches an
	 * application in the specified mode.
	 * 
	 * @param editor
	 *            the active editor in the workbench
	 * @param mode
	 *            one of the launch modes defined by the launch manager
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart,
	 *      java.lang.String)
	 * @see org.eclipse.debug.core.ILaunchManager
	 */
	@Override
	public void launch(IEditorPart editor, String mode) {
		FileEditorInput editorInput = (FileEditorInput) editor.getEditorInput();
		launch(editorInput.getFile(), mode);
	}

	/**
	 * Launches an application in the specified mode.
	 * 
	 * @param bin
	 *            the bin
	 * @param mode
	 *            the mode
	 */
	private void launch(IFile bin, String mode) {
		ILaunchConfiguration config = findLaunchConfiguration(bin, mode);
		if (config != null) {
			DebugUITools.launch(config, mode);
		}
	}

	/**
	 * If re-usable configuration associated with the File and the project
	 * exist, this configuration is returned. Otherwise a new configuration is
	 * created.
	 * 
	 * @param bin
	 *            the bin
	 * @param mode
	 *            the mode
	 * @return a re-useable or new config or <code>null</code> if none
	 */
	private ILaunchConfiguration findLaunchConfiguration(IFile bin, String mode) {
		ILaunchConfiguration configuration = null;
		List<ILaunchConfiguration> candidateConfigs = Collections.emptyList();
		try {
			ILaunchConfiguration[] configs = DebugPlugin.getDefault()
					.getLaunchManager().getLaunchConfigurations();
			candidateConfigs = new ArrayList<ILaunchConfiguration>(
					configs.length);
			for (int i = 0; i < configs.length; i++) {
				ILaunchConfiguration config = configs[i];
				String projectName = config.getAttribute("Project",
						(String) null);
				String programFile = config.getAttribute("File", (String) null);
				String name = bin.getName();
				if (programFile != null && programFile.equals(name)) {
					if (projectName != null
							&& projectName.equals(bin.getProject().getName())) {
						candidateConfigs.add(config);
					}
				}
			}
		} catch (CoreException e) {
			Logger.getInstance().logError("Error findling launch config", e);
		}

		// If there are no existing configs associated with the File and the
		// project, create one.
		// If there is more then one config associated with the File, return
		// the first one.
		int candidateCount = candidateConfigs.size();
		if (candidateCount < 1) {
			configuration = createConfiguration(bin);
		} else {
			configuration = candidateConfigs.get(0);
		}
		return configuration;
	}

	/**
	 * Creates a new configuration associated with the given file.
	 * 
	 * @param file
	 *            the file
	 * @return ILaunchConfiguration
	 */
	private ILaunchConfiguration createConfiguration(IFile file) {
		ILaunchConfiguration config = null;
		String projectName = file.getProjectRelativePath().toString();
		ILaunchConfigurationType[] configType = getLaunchManager()
				.getLaunchConfigurationTypes();
		ILaunchConfigurationType type = null;
		for (int i = 0; i < configType.length; i++) {
			if (configType[i].getIdentifier()
					.equals(DEBUG_LAUNCH_CONFIGURATION)) {
				type = configType[i];
			}
		}
		try {
			if (type != null) {
				ILaunchConfigurationWorkingCopy wc = type.newInstance(
						null,
						getLaunchManager().generateLaunchConfigurationName(
								file.getName()));
				wc.setAttribute("Project", projectName);
				wc.setAttribute("File", file.getProject().getName());
				// adding the resource
				IResource res = ResourcesPlugin.getWorkspace().getRoot()
						.findMember(file.getFullPath());
				IResource[] resources = new IResource[] { res };
				wc.setMappedResources(resources);
				config = wc.doSave();
			}
		} catch (CoreException e) {
			Logger.getInstance().logError("Could not create launch config", e);
		}
		return config;
	}

	/**
	 * Method to get the LaunchManager.
	 * 
	 * @return ILaunchManager
	 */
	protected ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}

}
