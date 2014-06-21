/**
 * 
 */
package org.powershell.preferences;

import java.util.StringTokenizer;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.RGB;
import org.powershell.Activator;

/**
 * The Class PowershellPreferences.
 * 
 * @author dalexandrov
 */
public class PowershellPreferences {

	/** The preferences. */
	private static IPreferenceStore preferences = Activator.getDefault()
			.getPreferenceStore();

	/** The comment. */
	public static RGB COMMENT;

	/** The string. */
	public static RGB STRING;

	/** The variable. */
	public static RGB VARIABLE;

	/** The language reserved. */
	public static RGB LANGUAGE_RESERVED;

	/** The dash keyword. */
	public static RGB DASH_KEYWORD;

	/** The known func. */
	public static RGB KNOWN_FUNC;

	/** The cmdlet. */
	public static RGB CMDLET;

	/** The other. */
	public static RGB OTHER;

	static {
		IPreferenceStore preferences = Activator.getDefault()
				.getPreferenceStore();
		preferences = Activator.getDefault().getPreferenceStore();
		preferences.setDefault(PreferenceConstants.COLOR_STRING,
				setRGB(IPSColorConstants.STRING));
		preferences.setDefault(PreferenceConstants.COLOR_COMMENT,
				setRGB(IPSColorConstants.COMMENT));
		preferences.setDefault(PreferenceConstants.COLOR_VARIABLE,
				setRGB(IPSColorConstants.VARIABLE));
		preferences.setDefault(PreferenceConstants.COLOR_LANGUAGE_RESERVED,
				setRGB(IPSColorConstants.LANGUAGE_RESERVED));
		preferences.setDefault(PreferenceConstants.COLOR_DASH_KEYWORD,
				setRGB(IPSColorConstants.DASH_OPERATOR));
		preferences.setDefault(PreferenceConstants.COLOR_OTHER,
				setRGB(IPSColorConstants.DEFAULT));
		preferences.setDefault(PreferenceConstants.COLOR_KNOWN_FUNC,
				setRGB(IPSColorConstants.KNOWN_FUNC));
		preferences.setDefault(PreferenceConstants.COLOR_CMDLETS,
				setRGB(IPSColorConstants.CMDLET));
		preferences.setDefault(PreferenceConstants.INDENT_WIDTH, 4);
		loadPreferences();
	}

	/**
	 * Load preferences.
	 */
	public static void loadPreferences() {
		COMMENT = getRGB(PreferenceConstants.COLOR_COMMENT);
		STRING = getRGB(PreferenceConstants.COLOR_STRING);
		VARIABLE = getRGB(PreferenceConstants.COLOR_VARIABLE);
		LANGUAGE_RESERVED = getRGB(PreferenceConstants.COLOR_LANGUAGE_RESERVED);
		DASH_KEYWORD = getRGB(PreferenceConstants.COLOR_DASH_KEYWORD);
		KNOWN_FUNC = getRGB(PreferenceConstants.COLOR_KNOWN_FUNC);
		CMDLET = getRGB(PreferenceConstants.COLOR_CMDLETS);
		OTHER = getRGB(PreferenceConstants.COLOR_OTHER);

	}

	/**
	 * Gets the rgb.
	 * 
	 * @param prefInput
	 *            the pref input
	 * @return the rgb
	 */
	private static RGB getRGB(String prefInput) {
		preferences = Activator.getDefault().getPreferenceStore();
		String property = preferences.getString(prefInput);
		StringTokenizer st = new StringTokenizer(property, ",");
		int red = Integer.parseInt(st.nextToken());
		int green = Integer.parseInt(st.nextToken());
		int blue = Integer.parseInt(st.nextToken());

		return new RGB(red, green, blue);
	}

	// utility
	/**
	 * Sets the rgb.
	 * 
	 * @param rgbInput
	 *            the rgb input
	 * @return the string
	 */
	private static String setRGB(RGB rgbInput) {
		return "" + rgbInput.red + "," + rgbInput.green + "," + rgbInput.blue;
	}
}