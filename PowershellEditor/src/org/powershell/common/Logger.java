/**
 * 
 */
package org.powershell.common;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * The Class Logger.
 * 
 * @author dalexandrov
 */
public class Logger {

	/** The Constant LOGGER_PLUGIN_ID. */
	public static final String LOGGER_PLUGIN_ID = "org.powershell";

	/** The instance. */
	private static Logger instance = new Logger();

	/** The logger. */
	private ILog logger = null;

	/**
	 * Instantiates a new logger.
	 */
	public Logger() {
		logger = ResourcesPlugin.getPlugin().getLog();
	}

	/**
	 * Log info.
	 * 
	 * @param message
	 *            the message
	 */
	public void logInfo(String message) {
		Status status = new Status(IStatus.INFO, LOGGER_PLUGIN_ID, message);

		if (logger != null) {
			logger.log(status);
		} else {
			System.out.println(status);
		}
	}

	/**
	 * Log error.
	 * 
	 * @param message
	 *            the message
	 * @param e
	 *            the e
	 */
	public void logError(String message, Exception e) {
		Status status = new Status(IStatus.ERROR, LOGGER_PLUGIN_ID, message, e);

		if (logger != null) {
			logger.log(status);
		} else {
			System.out.println(status);
		}
	}

	/**
	 * Gets the single instance of Logger.
	 * 
	 * @return single instance of Logger
	 */
	public static Logger getInstance() {
		return instance;
	}
}