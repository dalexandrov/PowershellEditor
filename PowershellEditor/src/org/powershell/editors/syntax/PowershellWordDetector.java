package org.powershell.editors.syntax;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * The Class PowershellWordDetector.
 * 
 * @author dalexandrov
 */
public class PowershellWordDetector implements IWordDetector {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c) {
		return Character.isLetter(c) || c == '-';
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	@Override
	public boolean isWordStart(char c) {
		return Character.isLetter(c);
	}

}
