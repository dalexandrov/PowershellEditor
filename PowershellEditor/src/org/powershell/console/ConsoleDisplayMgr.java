package org.powershell.console;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * Create an instance of this class in any of your plugin classes.
 * 
 * Use it as follows ...
 * 
 * ConsoleDisplayMgr.getDefault().println("Some error msg",
 * ConsoleDisplayMgr.MSG_ERROR); ... ... ConsoleDisplayMgr.getDefault().clear();
 * ...
 * 
 * @author dalexandrov
 */
public class ConsoleDisplayMgr {

	/** The default. */
	private static ConsoleDisplayMgr fDefault = new ConsoleDisplayMgr();

	/** The title. */
	private final String fTitle = "Powershell Console";

	/** The message console. */
	private MessageConsole fMessageConsole = null;

	/** The Constant MSG_INFORMATION. */
	public static final int MSG_INFORMATION = 1;

	/** The Constant MSG_ERROR. */
	public static final int MSG_ERROR = 2;

	/** The Constant MSG_WARNING. */
	public static final int MSG_WARNING = 3;

	/**
	 * Instantiates a new console display mgr.
	 */
	private ConsoleDisplayMgr() {
		// just construct
	}

	/**
	 * Gets the default.
	 * 
	 * @return the default
	 */
	public static ConsoleDisplayMgr getDefault() {
		return fDefault;
	}

	/**
	 * Println.
	 * 
	 * @param msg
	 *            the msg
	 * @param msgKind
	 *            the msg kind
	 */
	public void println(String msg, int msgKind) {
		if (msg == null)
			return;
		if (!displayConsoleView()) {
			MessageDialog.openError(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), "Error", msg);
			return;
		}
		getNewMessageConsoleStream(msgKind).println(msg);
	}

	/**
	 * Clear.
	 */
	public void clear() {
		IDocument document = getMessageConsole().getDocument();
		if (document != null) {
			document.set("");
		}
	}

	/**
	 * Display console view.
	 * 
	 * @return true, if successful
	 */
	public boolean displayConsoleView() {
		try {
			IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			if (activeWorkbenchWindow != null) {
				IWorkbenchPage activePage = activeWorkbenchWindow
						.getActivePage();
				PlatformUI
						.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.showView(IConsoleConstants.ID_CONSOLE_VIEW, null,
								IWorkbenchPage.VIEW_VISIBLE);
				if (activePage != null)
					activePage.showView(IConsoleConstants.ID_CONSOLE_VIEW,
							null, IWorkbenchPage.VIEW_VISIBLE);
			}
		} catch (PartInitException partEx) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the new message console stream.
	 * 
	 * @param msgKind
	 *            the msg kind
	 * @return the new message console stream
	 */
	private MessageConsoleStream getNewMessageConsoleStream(int msgKind) {
		// int swtColorId = SWT.COLOR_DARK_GREEN;
		// switch (msgKind) {
		// case MSG_INFORMATION:
		// swtColorId = SWT.COLOR_DARK_GREEN;
		// break;
		// case MSG_ERROR:
		// swtColorId = SWT.COLOR_DARK_MAGENTA;
		// break;
		// case MSG_WARNING:
		// swtColorId = SWT.COLOR_DARK_BLUE;
		// break;
		// default:
		// }
		MessageConsoleStream msgConsoleStream = getMessageConsole()
				.newMessageStream();
		// FIXME: attach to UI Thread
		// msgConsoleStream.setColor(Display.getCurrent().getSystemColor(
		// swtColorId));
		// System.out.println(msgConsoleStream.getColor());
		return msgConsoleStream;
	}

	/**
	 * Gets the message console.
	 * 
	 * @return the message console
	 */
	private MessageConsole getMessageConsole() {
		if (fMessageConsole == null)
			createMessageConsoleStream(fTitle);
		return fMessageConsole;
	}

	/**
	 * Creates the message console stream.
	 * 
	 * @param title
	 *            the title
	 */
	private void createMessageConsoleStream(String title) {
		fMessageConsole = new MessageConsole(title, null);
		ConsolePlugin.getDefault().getConsoleManager()
				.addConsoles(new IConsole[] { fMessageConsole });
	}
}