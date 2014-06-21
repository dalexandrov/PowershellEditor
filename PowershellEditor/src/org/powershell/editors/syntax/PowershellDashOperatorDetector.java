package org.powershell.editors.syntax;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * The Class PowershellDashOperatorDetector.
 * 
 * @author dalexandrov
 */
public class PowershellDashOperatorDetector implements IWordDetector {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c) {
		return Character.isLetter(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	@Override
	public boolean isWordStart(char c) {
		return c == '-';
	}

}
