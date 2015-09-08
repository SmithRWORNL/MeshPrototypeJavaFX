/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *   
 *******************************************************************************/


/**
 * This class provides a base implementation for a view managed by an
 * {@link AbstractController}. This class essentially provides a root
 * {@link Node} to contain jME3 scene objects and operations to add or remove
 * the root node to or from the scene.<br>
 * <br>
 * <b>Operations in this class (not including the constructor) should be called
 * from a SimpleApplication's simpleUpdate() thread.</b>
 * 
 * @author Jordan Deyton
 * 
 */
public abstract class AbstractView extends VizObject implements IVizUpdateable {

	Transformation transform;
	
	Transformation previousTransform;
	
	/**
	 * The default constructor.
	 * 
	 * @param name
	 *            The name of the AbstractView's node, {@link #viewNode}. If
	 *            null, "AbstractView" is used instead.
	 */
	public AbstractView(String name) {
		return;
	}

	/**
	 * Disposes of the AbstractView's spatials attached to the jME3 scene.
	 */
	public void dispose() {
		return;
	}
	
	/**
	 * Returns the application specific representation of the data contained in the associated component.
	 * 
	 * @return The object containing a renderable representation of the view's data.
	 */
	public Object getRepresentation(){
		return null;
	}
	
	/**
	 * Getter for the View's transformation
	 * 
	 * @return The view's transformation
	 */
	public Transformation getTransform(){
		return transform;
	}
	
	/**
	 * Mutator function for the view's transformation.
	 * 
	 * @param transform The new transformation to apply to the view.
	 */
	public void setTransform(Transformation transform){
		previousTransform = this.transform;
		this.transform = transform; 
		notifyListeners();
	}
	
	/**
	 * Getter method for the previousTransformation
	 * 
	 * @return the view's previous transformation.
	 */
	public Transformation getPreviousTransform(){
		return previousTransform;
	}
}
