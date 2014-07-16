package org.powershell.editors.completion.dictionaries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.powershell.common.Logger;
import org.powershell.editors.completion.helpers.DictItem;

/**
 * The Class PowershellDictionary.
 * 
 * @author dalexandrov
 */
public class PowershellDictionary extends AbstractDictionary {

	/** The Constant GET_CMDLETS_CMD. */
	private static final String[] GET_CMDLETS_CMD = new String[] {
			"powershell.exe",
			"-Command",
			"\"Get-PSSnapin -Registered -ErrorAction SilentlyContinue|Add-PSSnapin -ErrorAction SilentlyContinue;Get-Module -All | Import-Module;Get-Command|foreach {Write-Host $_.Name $_.CommandType}\"" };

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
		// CMDLETS
		cmdlets = new ArrayList<DictItem>();
		String newLine = null;
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			try {
				Process proc = Runtime.getRuntime().exec(GET_CMDLETS_CMD);
				proc.getOutputStream().close();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(proc.getInputStream()));

				while ((newLine = reader.readLine()) != null) {
					cmdlets.add(new DictItem(newLine.substring(0,
							newLine.indexOf(" ")), newLine.substring(newLine
							.indexOf(" ") + 1), "Global"));
				}
				reader.close();
			} catch (Exception e) {
				Logger.getInstance().logError(
						"Error getting powershell variables and functions", e);
			}
		}

		// in case we have no powershell..
		if (cmdlets.size() < 1) {
			BufferedReader fileReader = new BufferedReader(
					new InputStreamReader(
							(getClass().getResourceAsStream("/commands.list"))));
			try {
				while ((newLine = fileReader.readLine()) != null) {
					cmdlets.add(new DictItem(newLine.substring(0,
							newLine.indexOf(" ")), newLine.substring(newLine
							.indexOf(" ") + 1), "Global"));
				}
				fileReader.close();
			} catch (IOException e) {
				Logger.getInstance().logError(
						"Error getting powershell variables and functions", e);
			}
		}

		// Variables
		variables = new ArrayList<DictItem>();
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			try {
				Process proc = Runtime.getRuntime().exec(GET_VARIABLES_CMD);
				proc.getOutputStream().close();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(proc.getInputStream()));
				newLine = null;
				while ((newLine = reader.readLine()) != null) {
					variables.add(new DictItem("$" + newLine, "Variable",
							"Global"));
				}
				reader.close();
			} catch (Exception e) {
				Logger.getInstance().logError(
						"Error getting powershell variables and functions", e);
			}
		}
		// in case we have no powershell..
		if (variables.size() < 1) {
			BufferedReader fileReader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream(
							"/variables.list")));
			try {
				while ((newLine = fileReader.readLine()) != null) {
					variables.add(new DictItem("$" + newLine, "Variable",
							"Global"));
				}
				fileReader.close();
			} catch (IOException e) {
				Logger.getInstance().logError(
						"Error getting powershell variables and functions", e);
			}

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
	 * @see org.powershell.editors.completion.dictionaries.AbstractDictionary#
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
	 * @see org.powershell.editors.completion.dictionaries.AbstractDictionary#
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
