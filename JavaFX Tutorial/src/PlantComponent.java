/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * Base class for RELAP7 components.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
@XmlRootElement(name = "PlantComponent")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantComponent extends VizObject implements IReactorComponent {

	Mesh mesh;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public PlantComponent() {

		// Set the name, ID and description.
		this.objectName = "Plant Component 1";
		this.uniqueId = 1;
		this.objectDescription = "Plant-level reactor component";

		return;
	}

	/**
	 * <p>
	 * Parameterized constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            Name of the RELAP7 component.
	 *            </p>
	 */
	public PlantComponent(String name) {

		// Call the nullary constructor and then set the name.
		this();
		this.setName(name);

		return;
	}

	/**
	 * Constructor for a plant component specified by a list of vertices and
	 * entities.
	 * 
	 * @param vertices
	 *            The component's vertices.
	 * @param entities
	 *            Polygons which describe the mesh's faces.
	 */
	public PlantComponent(List<Vertex> vertices, List<Polygon> entities) {
		this();
		
		//Convert the polygons into a list of indices into the vertex list.
		ArrayList<List<Integer>> indexedEntities = new ArrayList<List<Integer>>();
		for (Polygon entity : entities) {
			ArrayList<Integer> newEntity = new ArrayList<Integer>();
			ArrayList<Vertex> tempVertices = entity.getVertices();
			for (Vertex currVertex : tempVertices) {
				if (vertices.contains(currVertex)) {
					newEntity.add(vertices.indexOf(currVertex));
				}
			}
			indexedEntities.add(newEntity);
		}
		mesh = new Mesh(vertices, indexedEntities);

	}

	/**
	 * Constructor for a plant component specified by a property map.
	 * 
	 * @param type The component's mesh's type.
	 * @param properties The map of properties that describe the component.
	 */
	public PlantComponent(String type, Map<String, Double> properties) {
		this();
		mesh = new Mesh(type, properties);
	}

	/**
	 * Getter for the component's mesh's type.
	 */
	@Override
	public String getMeshType() {
		return mesh.getType();
	}

	/**
	 * Getter for the component's mesh.
	 */
	@Override
	public Mesh getMesh() {
		return mesh;
	}

	/**
	 * <p>
	 * Performs an equality check between two Objects.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object to compare against.
	 *            </p>
	 * @return <p>
	 *         Returns true if the two objects are equal, otherwise false.
	 *         </p>
	 */
	@Override
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}

		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof PlantComponent) {

			// Cast the other object to an PlantComponent.
			PlantComponent component = (PlantComponent) otherObject;

			// Call the super's equality method.
			equals = super.equals(component);
		}

		return equals;
	}

	/**
	 * <p>
	 * Performs a deep copy and returns a newly instantiated Object.
	 * </p>
	 * 
	 * @return <p>
	 *         The newly instantiated Object.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Initialize a new object.
		PlantComponent object = new PlantComponent();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/**
	 * <p>
	 * Deep copies the contents of otherObject.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object to copy the contents from.
	 *            </p>
	 */
	public void copy(PlantComponent otherObject) {

		// Check the otherObject is valid.
		if (otherObject == null) {
			return;
		}
		// Call the super's copy method.
		super.copy(otherObject);

		return;
	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashCode of the Object.
	 *         </p>
	 */
	@Override
	public int hashCode() {

		// Call the super's hashCode.
		int hash = super.hashCode();

		return hash;
	}

	/**
	 * <!-- begin-UML-doc --> Gets a String representation of the
	 * PlantComponent. <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The name of the PlantComponent as a String.
	 *         </p>
	 * @see <p>
	 *      IReactorComponent#toString()
	 *      </p>
	 */
	@Override
	public String toString() {

		return objectName;

	}
}