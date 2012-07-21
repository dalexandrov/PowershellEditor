package org.powershell.editors.completition.dictionaries;

import java.util.List;

import org.powershell.editors.completition.helpers.DictItem;

//FIXME: think about abstract factory
/**
 * The Class AbstractDictionary.
 * 
 * @author dalexandrov
 */
public abstract class AbstractDictionary {

	/**
	 * Gets the proposals.
	 * 
	 * @param prefix
	 *            the prefix
	 * @return the proposals
	 */
	public abstract List<DictItem> getProposals(String prefix);

	/**
	 * Gets the for color.
	 * 
	 * @return the for color
	 */
	public abstract String[] getForColor();

	/**
	 * Compare to prefix.
	 * 
	 * @param prefix
	 *            the prefix
	 * @param proposal
	 *            the proposal
	 * @return true, if successful
	 */
	protected boolean compareToPrefix(String prefix, String proposal) {
		return proposal.toLowerCase().startsWith(prefix.toLowerCase());
	}
}
