package org.powershell.editors.completion.dictionaries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.powershell.common.Logger;
import org.powershell.editors.completion.helpers.DictItem;

/**
 * The Class MemberDictionary.
 * 
 * @author dalexandrov
 */
public class MemberDictionary extends AbstractDictionary {

	/** The instance. */
	private static MemberDictionary instance = new MemberDictionary();

	/**
	 * Instantiates a new member dictionary.
	 */
	private MemberDictionary() {
		// Empty constructor
	}

	/**
	 * Gets the single instance of MemberDictionary.
	 * 
	 * @return single instance of MemberDictionary
	 */
	public static MemberDictionary getInstance() {
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
		if (prefix != null && !prefix.contains(".")) {
			return result;
		}
		try {
			String[] command = new String[] {
					"powershell.exe",
					"-Command",
					"\""
							+ prefix.substring(0, prefix.indexOf("."))
							+ " -ErrorAction SilentlyContinue|gm -ErrorAction SilentlyContinue|foreach{Write-host $_.Name $_.MemberType}\"" };
			Process proc = Runtime.getRuntime().exec(command);
			proc.getOutputStream().close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String newLine = null;
			while ((newLine = reader.readLine()) != null
					&& newLine.startsWith(prefix)) {
				result.add(new DictItem(newLine.substring(0,
						newLine.indexOf(" ")), newLine.substring(newLine
						.indexOf(" ") + 1), "Member"));
			}
			reader.close();
		} catch (Exception e) {
			Logger.getInstance().logError("Error in getting propsals", e);
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
		return null;
	}

}
