package org.powershell.editors.completion.dictionaries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.powershell.common.Logger;
import org.powershell.editors.completion.ResourceChangeListener;
import org.powershell.editors.completion.SourceToFileMapping;
import org.powershell.editors.completion.helpers.DictItem;

/**
 * The Class InterFileDictionary.
 * 
 * @author dalexandrov
 */
public class InterFileDictionary {

	/** The Constant POWERSHELL_FILE_SUFFIX. */
	private static final String POWERSHELL_FILE_SUFFIX = ".ps1";

	private static final String POWERSHELL_MODULE_SUFFIX = ".psm1";

	/** The instances. */
	private static HashMap<String, InterFileDictionary> instances = new HashMap<String, InterFileDictionary>();

	/** The change listener. */
	private static ResourceChangeListener changeListener = null;

	/** The project. */
	private IProject project = null;

	/** The workspace. */
	private IWorkspace workspace = null;

	/** The workspace root. */
	private IWorkspaceRoot workspaceRoot = null;

	/** The src files p. */
	private final Pattern includesPattern = Pattern
			.compile("\\.\\s*([A-Za-z0-9\\\\/-]*\\.ps1|psm1)");

	/** The functions p. */
	private final Pattern functionsPattern = Pattern
			.compile("function \\s*([a-zA-Z0-9]*\\(.*\\))");

	/** The variables p. */
	private final Pattern variablesPattern = Pattern
			.compile("\\$[a-zA-z0-9_]*");

	/** The dict. */
	private HashMap<String, SourceToFileMapping> dict = new HashMap<String, SourceToFileMapping>();

	/**
	 * Instantiates a new inter file dictionary.
	 * 
	 * @param projName
	 *            the proj name
	 * @throws CoreException
	 *             the core exception
	 */
	private InterFileDictionary(String projName) throws CoreException {
		workspace = ResourcesPlugin.getWorkspace();
		workspaceRoot = workspace.getRoot();
		project = workspaceRoot.getProject(projName);
		if (project.exists()) {
			if (!project.isOpen())
				project.open(null);
		}

		generateDictionary();
	}

	/**
	 * Generate dictionary.
	 * 
	 * @throws CoreException
	 *             the core exception
	 */
	private void generateDictionary() throws CoreException {
		IPath projectRootPath = project.getLocation();
		File projectRoot = projectRootPath.toFile();
		addSourceDirectoryToDict(projectRoot);

		IResource[] members = project.members();
		for (IResource member : members) {
			if (member.isLinked()) {
				addSourceDirectoryToDict(member.getLocation().toFile());
			}
		}
	}

	/**
	 * Adds the source directory to dict.
	 * 
	 * @param directory
	 *            the directory
	 */
	private void addSourceDirectoryToDict(File directory) {
		String[] files = directory.list();
		for (int i = 0; i < files.length; i++) {
			File file = new File(directory.getAbsolutePath()
					+ File.separatorChar + files[i]);
			if (file.isDirectory()) {
				addSourceDirectoryToDict(file);
			}

			addFileToDict(file);
		}
	}

	/**
	 * Adds the file to dict.
	 * 
	 * @param file
	 *            the file
	 */
	private void addFileToDict(File file) {
		if (file.getName().endsWith(POWERSHELL_FILE_SUFFIX)
				|| file.getName().endsWith(POWERSHELL_MODULE_SUFFIX)) {
			String fileRelPath = file.getAbsolutePath();
			SourceToFileMapping data = new SourceToFileMapping(fileRelPath);

			String text = loadFile(file);
			data.setSourcedFiles(getSourcedFiles(text));
			data.setFunctions(getFunctions(text));
			data.setVariables(getVariables(text));
			dict.put(fileRelPath, data);
		}
	}

	/**
	 * Gets the sourced files.
	 * 
	 * @param content
	 *            the content
	 * @return the sourced files
	 */
	private String[] getSourcedFiles(String content) {
		Set<String> sourced = new HashSet<String>();

		Matcher matcher = includesPattern.matcher(content);
		while (matcher.find()) {
			sourced.add(matcher.group(1));
		}

		String[] result = new String[sourced.size()];
		return sourced.toArray(result);
	}

	/**
	 * Gets the functions.
	 * 
	 * @param content
	 *            the content
	 * @return the functions
	 */
	private String[] getFunctions(String content) {
		Set<String> sourced = new HashSet<String>();

		Matcher matcher = functionsPattern.matcher(content);
		while (matcher.find()) {
			sourced.add(matcher.group(1));
		}

		String[] result = new String[sourced.size()];
		return sourced.toArray(result);

	}

	/**
	 * Gets the variables.
	 * 
	 * @param content
	 *            the content
	 * @return the variables
	 */
	private String[] getVariables(String content) {
		Set<String> sourced = new HashSet<String>();

		Matcher matcher = variablesPattern.matcher(content);
		while (matcher.find()) {
			sourced.add(matcher.group(0));
		}

		String[] result = new String[sourced.size()];
		return sourced.toArray(result);
	}

	/**
	 * Gets the single instance of InterFileDictionary.
	 * 
	 * @param project
	 *            the project
	 * @return single instance of InterFileDictionary
	 * @throws CoreException
	 *             the core exception
	 */
	public static InterFileDictionary getInstance(String project)
			throws CoreException {

		InterFileDictionary result = InterFileDictionary.instances.get(project);
		if (result == null) {
			result = new InterFileDictionary(project);
			InterFileDictionary.instances.put(project, result);
		}

		if (changeListener == null) {
			// Install a resource change listener which should update the
			// dictionaries if a workspace resource is changed
			changeListener = new ResourceChangeListener();
			ResourcesPlugin.getWorkspace().addResourceChangeListener(
					changeListener, IResourceChangeEvent.POST_CHANGE);
		}

		return result;
	}

	/**
	 * Gets the full filename for ending.
	 * 
	 * @param ending
	 *            the ending
	 * @return the full filename for ending
	 */
	public String getFullFilenameForEnding(String ending) {
		String lowerCasedEnding = convertFileNameToWindowsNotation(ending)
				.toLowerCase();
		String result = ending;

		// FIXME: HACK NOT CORRECT AND THE IMPLEMENTATION IS UGLY
		Set<String> keys = dict.keySet();
		for (String key : keys) {
			if (key.toLowerCase().endsWith(lowerCasedEnding)) {
				result = key;
				break;
			}
		}

		return result;
	}

	/**
	 * Gets the proposals.
	 * 
	 * @param prefix
	 *            the prefix
	 * @param document
	 *            the document
	 * @return the proposals
	 */
	// FIXME: Think about different implementation
	public List<DictItem> getProposals(String prefix, String document) {
		List<DictItem> proposals = new ArrayList<DictItem>();

		Set<String> keys = dict.keySet();
		for (String key : keys) {
			if (key.toLowerCase().endsWith(document.toLowerCase())) {
				document = key;
				break;
			}
		}

		SourceToFileMapping data = dict.get(document);
		if (data == null) {
			// No proposals for this document
			return proposals;
		}

		String fileNameIncluded = document
				.substring(document.lastIndexOf('\\') + 1);
		String[] variables = data.getVariables();
		for (String variable : variables) {
			if (variable.toLowerCase().startsWith(prefix.toLowerCase())) {
				proposals.add(new DictItem(variable, "Variable",
						fileNameIncluded));
			}
		}

		String[] functions = data.getFunctions();
		for (String function : functions) {

			String funcName = "";
			if (function.indexOf('(') != -1) {
				// Add regular functions
				funcName = function.substring(0, function.indexOf('('));
			} else {
				// Nothing to add
				break;
			}

			if (function.toLowerCase().startsWith(prefix.toLowerCase())) {
				proposals.add(new DictItem(funcName, "Function",
						fileNameIncluded));
			}
		}

		String[] includes = data.getSourcedFiles();
		for (String include : includes) {
			proposals.addAll(getProposals(prefix, include));
		}

		return proposals;
	}

	/**
	 * Load file changes.
	 * 
	 * @param file
	 *            the file
	 */
	public void loadFileChanges(File file) {
		addFileToDict(file);
	}

	/**
	 * Rebuild dictionary.
	 * 
	 * @throws CoreException
	 *             the core exception
	 */
	public void rebuildDictionary() throws CoreException {
		dict = new HashMap<String, SourceToFileMapping>();

		generateDictionary();
	}

	/**
	 * Gets the file data.
	 * 
	 * @param filename
	 *            the filename
	 * @return the file data
	 */
	public SourceToFileMapping getFileData(String filename) {
		return dict.get(filename);
	}

	/**
	 * Load file.
	 * 
	 * @param file
	 *            the file
	 * @return the string
	 */
	private String loadFile(File file) {
		StringBuffer sb = new StringBuffer();
		char[] buffer = new char[1024];
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			int readChar = br.read(buffer);
			while (readChar != -1) {
				sb.append(buffer, 0, readChar);
				readChar = br.read(buffer);
			}
		} catch (Exception e) {
			Logger.getInstance().logError("Error loading source file", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Logger.getInstance().logError("Closing stream", e);
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Convert file name to windows notation.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the string
	 */
	public static String convertFileNameToWindowsNotation(String fileName) {
		return fileName.replace('/', '\\');
	}

	/**
	 * Gets the for color.
	 * 
	 * @return the for color
	 */
	public String[] getForColor() {
		List<String> result = new ArrayList<String>();
		for (Entry<String, SourceToFileMapping> entry : dict.entrySet()) {
			String[] functions = entry.getValue().getFunctions();
			for (String function : functions) {
				String funcName = "";
				if (function.indexOf('(') != -1) {
					// Add regular functions
					funcName = function.substring(0, function.indexOf('('));
				} else {
					// Nothing to add
					break;
				}
				result.add(funcName);
			}

		}
		return result.toArray(new String[result.size()]);
	}
}
