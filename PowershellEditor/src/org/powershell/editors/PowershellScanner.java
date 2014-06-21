package org.powershell.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.powershell.editors.completion.dictionaries.PowershellDictionary;
import org.powershell.editors.syntax.PowershellDashOperatorDetector;
import org.powershell.editors.syntax.PowershellVariableDetector;
import org.powershell.editors.syntax.PowershellWordDetector;
import org.powershell.preferences.PowershellPreferences;

/**
 * The Class PowershellScanner.
 * 
 * @author dalexandrov
 */
public class PowershellScanner extends RuleBasedScanner {

	/** The language reserved words. */
	private static String[] languageReservedWords = { "if", "else", "elseif",
			"switch", "while", "foreach", "default", "for", "do", "until",
			"break", "continue", "function", "return", "where", "filter", "in",
			"trap", "throw", "param" };

	/** The dash operators. */
	private static String[] dashOperators = { "-eq", "-ne", "-ge", "-gt",
			"-lt", "-le", "-like", "-notlike", "-match", "-notmatch",
			"-replace", "-contains", "-notcontains", "-ieq", "-ine", "-ige",
			"-igt", "-ile", "-ilt", "-ilike", "-inotlike", "-imatch",
			"-inotmatch", "-ireplace", "-icontains", "-inotcontains", "-ceq",
			"-cne", "-cge", "-cgt", "-cle", "-clt", "-clike", "-cnotlike",
			"-f", "-cmatch", "-cnotmatch", "-creplace", "-ccontains",
			"-cnotcontains", "-is", "-isnot", "-as", "-and", "-or", "-band",
			"-bor", "-not" };

	/** The scopes. */
	private static String[] scopes = { "$script", "$global", "$function",
			"$local", "$private" };

	/** The cmdlets. */
	private static String[] cmdlets = PowershellDictionary.getInstance()
			.getForColor();

	/** The known_functions. */
	private static String[] known_functions = new String[0];

	/**
	 * Instantiates a new powershell scanner.
	 */
	public PowershellScanner() {

		IToken comment = new Token(new TextAttribute(new Color(
				Display.getCurrent(), PowershellPreferences.COMMENT)));
		IToken string = new Token(new TextAttribute(new Color(
				Display.getCurrent(), PowershellPreferences.STRING)));
		IToken variable = new Token(new TextAttribute(new Color(
				Display.getCurrent(), PowershellPreferences.VARIABLE)));
		IToken scope = new Token(new TextAttribute(new Color(
				Display.getCurrent(), PowershellPreferences.VARIABLE), null,
				SWT.BOLD));
		IToken keyword = new Token(new TextAttribute(new Color(
				Display.getCurrent(), PowershellPreferences.LANGUAGE_RESERVED),
				null, SWT.BOLD));
		IToken cmdlet = new Token(new TextAttribute(new Color(
				Display.getCurrent(), PowershellPreferences.CMDLET), null, SWT.BOLD));
		IToken known_func = new Token(new TextAttribute(new Color(
				Display.getCurrent(), PowershellPreferences.KNOWN_FUNC), null,
				SWT.BOLD));
		IToken dashKeyword = new Token(new TextAttribute(new Color(
				Display.getCurrent(), PowershellPreferences.DASH_KEYWORD), null,
				SWT.BOLD));
		IToken def = new Token(new TextAttribute(new Color(
				Display.getCurrent(), PowershellPreferences.OTHER)));

		List<IRule> rules = new ArrayList<IRule>();

		// Add rule for single line comments.
		rules.add(new EndOfLineRule("#", comment));

		// Add rule for strings and character constants.
		rules.add(new SingleLineRule("\"", "\"", string, '`', false, true));
		rules.add(new SingleLineRule("'", "'", string));

		// Add word rule for keywords, types, and constants.

		PowershellVariableDetector variableDetector = new PowershellVariableDetector(
				scope, variable);
		for (int i = 0; i < scopes.length; i++)
			variableDetector.addScope(scopes[i]);
		rules.add(variableDetector);

		WordRule wordRule = new WordRule(new PowershellWordDetector());
		for (int i = 0; i < languageReservedWords.length; i++)
			wordRule.addWord(languageReservedWords[i], keyword);
		rules.add(wordRule);

		wordRule = new WordRule(new PowershellWordDetector(), def, true);
		for (int i = 0; i < cmdlets.length; i++)
			wordRule.addWord(cmdlets[i], cmdlet);
		rules.add(wordRule);

		wordRule = new WordRule(new PowershellWordDetector(), def, true);
		for (int i = 0; i < known_functions.length; i++)
			wordRule.addWord(known_functions[i], known_func);
		rules.add(wordRule);

		wordRule = new WordRule(new PowershellDashOperatorDetector());
		for (int i = 0; i < dashOperators.length; i++)
			wordRule.addWord(dashOperators[i], dashKeyword);
		rules.add(wordRule);

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);

		setDefaultReturnToken(def);
	}
}
