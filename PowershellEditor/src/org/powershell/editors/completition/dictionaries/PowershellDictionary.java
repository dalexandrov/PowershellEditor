package org.powershell.editors.completition.dictionaries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.powershell.editors.completition.helpers.DictItem;

/**
 * The Class PowershellDictionary.
 * 
 * @author dalexandrov
 */
public class PowershellDictionary extends AbstractDictionary {

	/** The Constant GET_CMDLETS_CMD. */
	private static final String[] GET_CMDLETS_CMD = new String[] {
			"powershell.exe", "-Command",
			"\"Get-Command|foreach {Write-Host $_.Name $_.CommandType}\"" };

	/** The Constant GET_VARIABLES_CMD. */
	private static final String[] GET_VARIABLES_CMD = new String[] {
			"powershell.exe", "-Command",
			"\"Get-Variable|foreach {Write-Host $_.Name}\"" };

	/** The cmdlets. */
	private List<DictItem> cmdlets;

	/** The variables. */
	private List<DictItem> variables;

	/** The instance. */
	private static PowershellDictionary instance = new PowershellDictionary();

	/**
	 * Instantiates a new powershell dictionary.
	 */
	private PowershellDictionary() {
		try {
			// CMDLETS
			cmdlets = new ArrayList<DictItem>();
			Process proc = Runtime.getRuntime().exec(GET_CMDLETS_CMD);
			proc.getOutputStream().close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String newLine = null;
			while ((newLine = reader.readLine()) != null) {
				cmdlets.add(new DictItem(newLine.substring(0,
						newLine.indexOf(" ")), newLine.substring(newLine
						.indexOf(" ") + 1), "Global"));
			}
			reader.close();

			// Variables
			variables = new ArrayList<DictItem>();
			proc = Runtime.getRuntime().exec(GET_VARIABLES_CMD);
			proc.getOutputStream().close();
			reader = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			newLine = null;
			while ((newLine = reader.readLine()) != null) {
				variables
						.add(new DictItem("$" + newLine, "Variable", "Global"));
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the single instance of PowershellDictionary.
	 * 
	 * @return single instance of PowershellDictionary
	 */
	public static PowershellDictionary getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.powershell.editors.completition.dictionaries.AbstractDictionary#
	 * getProposals(java.lang.String)
	 */
	@Override
	public List<DictItem> getProposals(String prefix) {
		List<DictItem> result = new ArrayList<DictItem>();
		List<DictItem> iterateOver = null;
		if (prefix.contains(".")) {
			return result;
		}
		if (prefix.startsWith("$")) {
			iterateOver = variables;
		} else if (prefix.equals("")) {
			iterateOver = variables;
			iterateOver.addAll(cmdlets);
		}

		else {
			iterateOver = cmdlets;
		}

		for (DictItem iterable : iterateOver) {
			if (iterable.getItem().toLowerCase()
					.startsWith(prefix.toLowerCase())) {
				result.add(iterable);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.powershell.editors.completition.dictionaries.AbstractDictionary#
	 * getForColor()
	 */
	@Override
	public String[] getForColor() {
		List<String> forColors = new ArrayList<String>();
		for (DictItem item : cmdlets) {
			forColors.add(item.getItem());
		}
		return forColors.toArray(new String[forColors.size()]);
	}

}
