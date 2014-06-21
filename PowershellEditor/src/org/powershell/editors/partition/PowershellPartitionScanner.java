package org.powershell.editors.partition;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 * The Class PowershellPartitionScanner.
 * 
 * @author dalexandrov
 */
public class PowershellPartitionScanner extends RuleBasedPartitionScanner {

	/** The Constant PS_COMMENT. */
	public final static String PS_COMMENT = "__ps_comment";

	/** The Constant PS_FUNCTION. */
	public final static String PS_FUNCTION = "__ps_function";

	/**
	 * Instantiates a new powershell partition scanner.
	 */
	public PowershellPartitionScanner() {
		IToken psComment = new Token(PS_COMMENT);
		IToken psFunction = new Token(PS_FUNCTION);

		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new EndOfLineRule("#", psComment);
		rules[1] = new SingleLineRule("function", "{", psFunction);

		setPredicateRules(rules);
	}
}
