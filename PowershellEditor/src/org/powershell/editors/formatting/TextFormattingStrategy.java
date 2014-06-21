/**
 * 
 */
package org.powershell.editors.formatting;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.powershell.Activator;
import org.powershell.common.Logger;
import org.powershell.preferences.PreferenceConstants;

/**
 * The Class TextFormattingStrategy.
 * 
 * @author dalexandrov
 */
public class TextFormattingStrategy implements IFormattingStrategy {

	/** The Constant lineSeparator. */
	private static final String lineSeparator = System
			.getProperty("line.separator");

	/** The logger. */
	private final Logger logger = Logger.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.formatter.IFormattingStrategy#format(java.lang
	 * .String, boolean, java.lang.String, int[])
	 */
	@Override
	public String format(String content, boolean isLineStart,
			String indentation, int[] positions) {
		// build indentation from settings
		indentation = Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.INDENT_SYMBOL);
		for (int i = 1; i < Activator.getDefault().getPreferenceStore()
				.getInt(PreferenceConstants.INDENT_WIDTH); i++) {
			indentation += Activator.getDefault().getPreferenceStore()
					.getString(PreferenceConstants.INDENT_SYMBOL);
		}

		logger.logInfo("Formatting source");
		int ident = 0;
		StringBuffer result = new StringBuffer();
		String[] lines = content.split(lineSeparator);

		// first pass
		StringBuffer firstPassBuff = new StringBuffer();
		for (String line : lines) {
			// getting clean line
			String cleanLine = line.trim().replaceAll("( )+", " ");

			if (cleanLine.equals("{")) {
				firstPassBuff.append(cleanLine);
			} else if (cleanLine.equals("{")) {
				firstPassBuff.append(cleanLine);
			} else if (cleanLine.contains("{")) {
				for (int i = 0; i < cleanLine.length(); i++) {
					firstPassBuff.append(cleanLine.charAt(i));
					if (cleanLine.charAt(i) == '{'
							&& i < cleanLine.length() - 1) {
						firstPassBuff.append(lineSeparator);
					}
				}
			} else if (cleanLine.contains("}")) {
				for (int i = 0; i < cleanLine.length(); i++) {
					if (cleanLine.charAt(i) == '}' && i > 0) {
						firstPassBuff.append(lineSeparator);
					}
					firstPassBuff.append(cleanLine.charAt(i));
					if (cleanLine.charAt(i) == '}'
							&& i < cleanLine.length() - 1) {
						firstPassBuff.append(lineSeparator);
					}
				}
			} else {
				firstPassBuff.append(cleanLine);
			}

			firstPassBuff.append(lineSeparator);
		}

		lines = firstPassBuff.toString().split(lineSeparator);
		// rethink here!
		int size = lines.length;
		// flag and dash position
		int dash = -1;
		for (String line : lines) {

			// getting clean line
			String cleanLine = line.trim().replaceAll("( )+", " ");

			// very ugly but java has only this way
			if (cleanLine.contains("}")) {
				ident--;
			}

			for (int i = 0; i < ident; i++) {
				result.append(indentation);
			}
			// again ugly.. but currently the best
			if (cleanLine.startsWith("-") && dash >= 0) {
				for (int i = 0; i <= dash; i++) {
					result.append(" ");
				}
			}
			result.append(cleanLine);
			if (cleanLine.contains(" -")) {
				dash = cleanLine.indexOf(" -");
			} else if (cleanLine.startsWith("-")) {
				// do nothing
			} else {
				dash = -1;
			}

			if (--size != 0) {
				result.append(lineSeparator);
			}

			if (cleanLine.endsWith("{")) {
				ident++;
			}
		}

		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.formatter.IFormattingStrategy#formatterStarts(
	 * java.lang.String)
	 */
	@Override
	public void formatterStarts(String arg0) {
		// empty function
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.formatter.IFormattingStrategy#formatterStops()
	 */
	@Override
	public void formatterStops() {
		// TODO Auto-generated method stub

	}
}