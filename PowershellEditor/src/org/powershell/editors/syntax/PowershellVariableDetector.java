package org.powershell.editors.syntax;

import java.util.HashSet;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * The Class PowershellVariableDetector.
 * 
 * @author dalexandrov
 */
public class PowershellVariableDetector implements IRule {

	/** The scope. */
	IToken scope = Token.UNDEFINED;

	/** The variable. */
	IToken variable = Token.UNDEFINED;

	/** The predefined. */
	HashSet<String> predefined = new HashSet<String>();

	/** The buffer. */
	StringBuffer buffer = new StringBuffer();

	/**
	 * Instantiates a new powershell variable detector.
	 * 
	 * @param scope
	 *            the scope
	 * @param variable
	 *            the variable
	 */
	public PowershellVariableDetector(IToken scope, IToken variable) {
		this.scope = scope;
		this.variable = variable;
	}

	/**
	 * Adds the scope.
	 * 
	 * @param scope
	 *            the scope
	 */
	public void addScope(String scope) {
		predefined.add(scope.toLowerCase());
	}

	/**
	 * Evaluate.
	 * 
	 * @param scanner
	 *            the scanner
	 * @return the i token
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		boolean wasMatchEmpty = (buffer.length() == 0) ? true : false;
		int ch = scanner.read();
		if (ch != ICharacterScanner.EOF && ((char) ch == '$' || !wasMatchEmpty)) {
			buffer.setLength(0);

			do {
				buffer.append((char) ch);
				ch = scanner.read();
			} while (Character.isJavaIdentifierPart((char) ch));

			if ((char) ch == ':') {
				String match = (buffer.toString()).toLowerCase();
				return (predefined.contains(match)) ? scope : variable;
			} else {
				buffer.setLength(0);
				scanner.unread();
				return variable;
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}
}