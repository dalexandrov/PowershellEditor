package org.powershell.editors.completion;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.powershell.common.Logger;
import org.powershell.editors.completion.dictionaries.InterFileDictionary;

/**
 * The listener interface for receiving resourceChange events. The class that is
 * interested in processing a resourceChange event implements this interface,
 * and the object created with that class is registered with a component using
 * the component's <code>addResourceChangeListener<code> method. When
 * the resourceChange event occurs, that object's appropriate
 * method is invoked.
 */
public class ResourceChangeListener implements IResourceChangeListener,
		IResourceDeltaVisitor {

	/** The internal visitor. */
	private static ResourceChangeListener internalVisitor = new ResourceChangeListener();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org
	 * .eclipse.core.resources.IResourceChangeEvent)
	 */
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
			return;
		}
		try {
			event.getDelta().accept(internalVisitor);
		} catch (CoreException ce) {
			Logger.getInstance().logError("Resource change error", ce);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core
	 * .resources.IResourceDelta)
	 */
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource res = delta.getResource();
		if (res instanceof IFile) {
			InterFileDictionary dict = InterFileDictionary.getInstance(res
					.getProject().getName());
			dict.loadFileChanges(res.getLocation().toFile());
		}

		return true;
	}

}
