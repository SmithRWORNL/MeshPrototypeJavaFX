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
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * <p>
 * This class represents a polygon composed of a certain number of vertices
 * connected by edges.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
@XmlRootElement(name = "Polygon")
@XmlAccessorType(XmlAccessType.FIELD)
public class Polygon extends VizObject implements IVizUpdateableListener {

	/**
	 * <p>
	 * The collection of Vertices composing the polygon.
	 * </p>
	 * 
	 */
	@XmlElement(name = "Vertex")
	protected ArrayList<Vertex> vertices;
	/**
	 * <p>
	 * The collection of Edges composing the polygon.
	 * </p>
	 * 
	 */
	@XmlElement(name = "Edge")
	protected ArrayList<Edge> edges;

	/**
	 * <p>
	 * A nullary constructor. This creates a Polygon with no vertices or edges
	 * and initializes any fields necessary. Required for persistence.
	 * </p>
	 * 
	 */
	public Polygon() {
		super();

		// Initialize the lists of edges and vertices.
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		// Set the default name, id, and description.
		setName("Polygon");
		setDescription("");

		return;
	}

	/**
	 * <p>
	 * The default constructor.
	 * </p>
	 * 
	 * @param edges
	 *            <p>
	 *            A collection of edges connecting a collection of vertices.
	 *            </p>
	 * @param vertices
	 *            <p>
	 *            A collection of vertices connected by a collection of edges.
	 *            </p>
	 */
	public Polygon(ArrayList<Edge> edges, ArrayList<Vertex> vertices) {

		// Initialize the defaults.
		this();

		// A Polygon has the following requirements:
		// Edge i must have as its start and end points vertices i and i+1.
		// (for the last edge, these are vertices i and 0).
		// The Vertex references stored in the Edge should be the same as stored
		// in the Polygon's list of vertices.

		// This does not check for simple/complex polygons, only that the outer
		// edges form a cycle.

		// Check the parameters for null values.
		if (vertices == null || vertices.contains(null) || vertices.size() < 3) {
			throw new IllegalArgumentException(
					"Polygon error: The vertex list must be a non-null list with at least 3 vertices.");
		} else if (edges == null || edges.contains(null) || edges.size() < 3) {
			throw new IllegalArgumentException(
					"Polygon error: The edge list must be a non-null list with at least 3 edges.");
		}
		// Get the sizes of the two lists.
		int size = vertices.size();

		// Use a Set of vertex IDs to make sure no vertex connects more than two
		// edges.
		TreeSet<Integer> vertexIds = new TreeSet<Integer>();
		TreeSet<Integer> edgeIds = new TreeSet<Integer>();

		// Make sure the edges form a cycle.
		for (int i = 0, j = 1; i < size; i++, j = (i + 1) % size) {
			int v1 = vertices.get(i).getId();
			int v2 = vertices.get(j).getId();
			int[] ids = edges.get(i).getVertexIds();

			// Add the current vertex ID to the Set.
			vertexIds.add(v1);

			// Make sure the two adjacent vertex IDs are not the same.
			if (v1 == v2) {
				throw new IllegalArgumentException(
						"Polygon error: Same ID for adjacent vertices.");
			}
			// Make sure the IDs v1 and v2 are both in the edge's vertex ID
			// list.
			else if ((v1 != ids[0] && v1 != ids[1])
					|| (v2 != ids[0] && v2 != ids[1])) {
				throw new IllegalArgumentException("Polygon error: Edge " + i
						+ " uses unknown vertex.");
			}
			edgeIds.add(edges.get(i).getId());
		}

		// Check the size of the Set of vertex IDs. Every ID should be unique!
		if (vertexIds.size() < size) {
			throw new IllegalArgumentException(
					"Polygon error: A vertex connects more than two edges.");
		} else if (edgeIds.size() < size) {
			throw new IllegalArgumentException(
					"Polygon error: Same edge ID used more than once.");
		}
		// If the vertices/edges supplied are valid, duplicate them. We also
		// must ensure the edges are registered with the correct vertex
		// instances.
		for (int i = 0; i < size; i++) {
			// Get the edge from the supplied list.
			Edge edge = edges.get(i);

			// Get the new start and end vertices for this edge.
			Vertex start = vertices.get(i);
			Vertex end = vertices.get((i + 1) % size);

			// Register the clone with the start and end.
			start.register(edge);
			end.register(edge);

			// Send the start and end vertices to the clone.
			edge.update(start);
			edge.update(end);

			// Add the edge and vertex to the Polygon's lists.
			this.edges.add(edge);
			this.vertices.add(start);
		}

		return;
	}

	/**
	 * <p>
	 * Gets the collection of edges composing the polygon.
	 * </p>
	 * 
	 * @return <p>
	 *         The collection of Edges composing the 2D polygon.
	 *         </p>
	 */
	public ArrayList<Edge> getEdges() {

		// Return a copy of the edge list.
		return new ArrayList<Edge>(edges);
	}

	/**
	 * <p>
	 * Gets the collection of vertices composing the polygon.
	 * </p>
	 * 
	 * @return <p>
	 *         The collection of Vertices composing the 2D polygon.
	 *         </p>
	 */
	public ArrayList<Vertex> getVertices() {

		// Return a copy of the vertex list.
		return new ArrayList<Vertex>(vertices);
	}


	/**
	 * <p>
	 * This operation copies the contents of a Polygon into the current object
	 * using a deep copy.
	 * </p>
	 * 
	 * @param polygon
	 *            <p>
	 *            The Object from which the values should be copied.
	 *            </p>
	 */
	public void copy(Polygon polygon) {

		// Check the parameters.
		if (polygon == null) {
			return;
		}
		// Copy the super's data.
		super.copy(polygon);

		// Deep copy the vertices.
		vertices.clear();
		for (Vertex vertex : polygon.getVertices()) {
			vertices.add((Vertex) vertex.clone());
		}

		// Deep copy the edges.
		edges.clear();
		ArrayList<Edge> otherEdges = polygon.getEdges();
		int size = otherEdges.size();
		for (int i = 0; i < size; i++) {
			// Clone the edge. This deep copies the other edge's vertices, so we
			// must hook the clone up to our vertex instances.
			Edge clone = (Edge) otherEdges.get(i).clone();

			// Add the clone to the list of edges.
			edges.add(clone);

			// Get the new start and end vertices for this edge.
			Vertex start = vertices.get(i);
			Vertex end = vertices.get((i + 1) % size);

			// Register the clone with the start and end.
			start.register(clone);
			end.register(clone);

			// Send the start and end vertices to the clone.
			clone.update(start);
			clone.update(end);
		}


		return;
	}

	/**
	 * <p>
	 * This operation returns a clone of the Polygon using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Initialize a new object.
		Polygon object = new Polygon();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}


	/**
	 * <p>
	 * Implements the update operation. Currently, the Polygon registers with
	 * its current set of BoundaryConditions.
	 * </p>
	 * 
	 * @param component
	 *            <p>
	 *            The updated component. This should always be a
	 *            BoundaryCondition.
	 *            </p>
	 */
	@Override
	public void update(IVizUpdateable component) {

		// TODO Currently, this code has been disabled. We still need to decide
		// if the polygon should pass on an update notification when a boundary
		// condition has changed, or if interested parties should register
		// directly with the boundary condition.

		// This method is only called if one of the boundary conditions was
		// updated.

		// Notify listeners when one of the boundary conditions was changed.
		notifyListeners();

		return;
	}

}