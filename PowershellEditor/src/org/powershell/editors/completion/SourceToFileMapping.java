package org.powershell.editors.completion;

import java.util.Arrays;
import java.util.Vector;

import org.powershell.editors.completion.dictionaries.InterFileDictionary;

/**
 * The Class SourceToFileMapping.
 * 
 * @author dalexandrov
 */
public class SourceToFileMapping {

	/** The file name. */
	public String fileName;

	/** The sourced files. */
	private String[] sourcedFiles = new String[0];

	/** The functions. */
	private String[] functions = new String[0];

	/** The variables. */
	private String[] variables = new String[0];

	/**
	 * Instantiates a new source to file mapping.
	 * 
	 * @param fileName
	 *            the file name
	 */
	public SourceToFileMapping(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Sets the sourced files.
	 * 
	 * @param data
	 *            the new sourced files
	 */
	public void setSourcedFiles(String[] data) {
		this.sourcedFiles = data;
	}

	/**
	 * Gets the sourced files.
	 * 
	 * @return the sourced files
	 */
	public String[] getSourcedFiles() {
		return this.sourcedFiles;
	}

	/**
	 * Sets the functions.
	 * 
	 * @param data
	 *            the new functions
	 */
	public void setFunctions(String[] data) {
		this.functions = data;
	}

	/**
	 * Gets the functions.
	 * 
	 * @return the functions
	 */
	public String[] getFunctions() {
		return this.functions;
	}

	/**
	 * Sets the variables.
	 * 
	 * @param data
	 *            the new variables
	 */
	public void setVariables(String[] data) {
		this.variables = data;
	}

	/**
	 * Gets the variables.
	 * 
	 * @return the variables
	 */
	public String[] getVariables() {
		return this.variables;
	}

	/**
	 * Gets the members.
	 * 
	 * @return the members
	 */
	public String[] getMembers() {
		Vector<String> memberList = new Vector<String>();

		// First add the variables
		if (this.variables.length > 0) {
			memberList.addAll(Arrays.asList(this.variables));
		}

		// Then add the functions
		if (this.functions.length > 0) {
			memberList.addAll(Arrays.asList(this.functions));
		}

		// FIXME: [IN] Find a better way to cast to String[]?
		return memberList.toArray(new String[] {});
	}

	// TODO: Make the search wide instead of depth
	/**
	 * Gets the function owner.
	 * 
	 * @param function
	 *            the function
	 * @param dict
	 *            the dict
	 * @return the function owner
	 */
	public String getFunctionOwner(String function, InterFileDictionary dict) {
		String result = null;

		for (int i = 0; i < functions.length; i++) {
			if (functions[i].startsWith(function + "(")) {
				return this.fileName;
			}
		}

		for (int i = 0; i < sourcedFiles.length; i++) {
			SourceToFileMapping data = dict.getFileData(dict
					.getFullFilenameForEnding(sourcedFiles[i]));
			if (data != null) {
				result = data.getFunctionOwner(function, dict);
			}
			if (result != null)
				break;
		}

		return result;
	}
}
