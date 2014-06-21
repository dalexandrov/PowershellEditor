package org.powershell.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.powershell.Activator;

/**
 * The Class PowershellPreferencePage.
 */
public class PowershellPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	/**
	 * Instantiates a new powershell preference page.
	 */
	public PowershellPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Powershell preference page");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors
	 * ()
	 */
	@Override
	protected void createFieldEditors() {
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_COMMENT,
				"Comment coloring (# comment)", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_DASH_KEYWORD,
				"-Keyword coloring (-and, -or, etc.)", getFieldEditorParent()));
		addField(new ColorFieldEditor(
				PreferenceConstants.COLOR_LANGUAGE_RESERVED,
				"Keyword coloring (if, for, etc.)", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_STRING,
				"String coloring (\"String\" or 'String')",
				getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_VARIABLE,
				"Variable coloring ($var)", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_KNOWN_FUNC,
				"Known functions coloring", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_CMDLETS,
				"Cmdlets coloring", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_OTHER,
				"General text coloring (everything else)",
				getFieldEditorParent()));

		// Indent width

		IntegerFieldEditor tabWidth = new IntegerFieldEditor(
				PreferenceConstants.INDENT_WIDTH, "Autoformatter indent width",
				getFieldEditorParent());
		tabWidth.setValidRange(0, 8);
		addField(tabWidth);

		RadioGroupFieldEditor indentSymbol = new RadioGroupFieldEditor(
				PreferenceConstants.INDENT_SYMBOL,
				"Autoformatter indent symbol", 1, new String[][] {
						{ "Space", " " }, { "Tab", "\t" } },
				getFieldEditorParent());

		addField(indentSymbol);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
	 */
	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		boolean success = super.performOk();
		PowershellPreferences.loadPreferences();
		return success;
	}

}
