import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;

/**
 * An implementation of AbstractView specific to non-volumentric parts rendered
 * in JavaFX. It allows for arbitrary meshes or spheres. It expects as input an
 * IReactorComponent which models its data as instances of the Polygon class.
 * 
 * @author Robert Smith
 *
 */
public class JavaFXView extends AbstractView {
	TriangleMesh mesh;

	Sphere sphere;

	String type;

	/**
	 * Creates a JavaFX representation for the given component.
	 * 
	 * @param component
	 *            The component which serves as this view's model.
	 */
	public JavaFXView(IReactorComponent component) {
		super("Test View");
		transform = new Transformation();
		previousTransform = null;
		Mesh rawMesh = component.getMesh();
		type = rawMesh.getType();

		// Create a triangle mesh for a custom type mesh
		if (type == "custom") {
			ArrayList<Vertex> originalVertices = (ArrayList<Vertex>) rawMesh
					.getVertices();
			ArrayList<Float> vertices = new ArrayList<Float>();

			// Convert the vertices into tuples of floats
			for (Vertex currentVertex : originalVertices) {
				float[] currentCoordinates = currentVertex.getLocation();
				for (float coordinate : currentCoordinates) {
					vertices.add(coordinate);
				}
			}

			// Assume all entities are specified as convex polygons. Create a
			// triangle from vertex 0 for each other pair of adjacent vertices.
			// TODO Switch to ear clipping algorithm to allow for all simple
			// polygons.
			ArrayList<Integer> entities = new ArrayList<Integer>();
			for (List<Integer> face : rawMesh.getEntities()) {
				for (int i = 2; i < face.size(); i++) {
					entities.add(face.get(0));
					entities.add(0);
					entities.add(face.get(i - 1));
					entities.add(0);
					entities.add(face.get(i));
					entities.add(0);
				}
			}

			mesh = new TriangleMesh();

			// Convert vertices into array and add them to the mesh.
			float[] points = new float[vertices.size()];
			for (int i = 0; i < vertices.size(); i++) {
				points[i] = vertices.get(i);
			}
			mesh.getPoints().addAll(points);

			// Do not apply a texture, instead add a single dummy coordinate.
			float[] texCoords = { 0, 0 };
			mesh.getTexCoords().addAll(texCoords);

			// Convert entities into an array and add them to the mesh.
			int[] faces = new int[entities.size()];
			for (int i = 0; i < entities.size(); i++) {
				faces[i] = entities.get(i);
			}
			mesh.getFaces().addAll(faces);

			// For spheres, create a sphere, getting the radius from the mesh
		} else if (type == "sphere") {
			sphere = new Sphere(component.getMesh().getProperty("radius"));
		}

	}

	/**
	 * 	Return the apprioriate object holding the JavaFX representation, based on
	 * the component's type.
	 */
	@Override
	public Object getRepresentation() {
		if (type == "custom") {
			return mesh;
		} else if (type == "sphere") {
			return sphere;
		}
		return null;
	}

	/**
	 * Getter for the view's transformation.
	 */
	public Transformation getTransform() {
		return transform;
	}

	/**
	 * Setter method for the view's transformation.
	 */
	public void setTransform(Transformation transform) {
		this.transform = transform;
	}
}
