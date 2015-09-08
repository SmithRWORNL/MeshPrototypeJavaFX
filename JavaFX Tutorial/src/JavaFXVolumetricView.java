import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;

/**
 * A JavaFX specific implementation of AbstractView. This view models a custom
 * mesh as a series of edges
 * 
 * @author Robert Smith
 *
 */
public class JavaFXVolumetricView extends AbstractView {
	ArrayList<Cylinder> edges;

	public JavaFXVolumetricView(IReactorComponent component) {
		super("Test View");

		// Ignore non-custom meshes, which cannot be volumetric
		if (component.getMeshType() == "custom") {
			Mesh rawMesh = component.getMesh();
			ArrayList<Vertex> originalVertices = (ArrayList<Vertex>) rawMesh
					.getVertices();

			edges = new ArrayList<Cylinder>();

			// For each face in the model...
			for (List<Integer> face : rawMesh.getEntities()) {

				// For each edge in the model...
				// TODO Create list of already converted edges so that each edge
				// is only converted once.
				for (int i = 0; i < face.size(); i++) {
					
					//Get the edge's endpoints
					float[] start = originalVertices.get(face.get(i))
							.getLocation();
					float[] end = originalVertices.get(
							face.get((i + 1) % face.size())).getLocation();
					
					//Create a cylinder situated at the edge's midpoint with the edge's length.
					Cylinder edge = new Cylinder(.5, Math.sqrt((Math.pow(
							start[0] - end[0], 2))
							+ (Math.pow(start[1] - end[1], 2))
							+ (Math.pow(start[2] - end[2], 2))));
					edge.setTranslateX((start[0] + end[0]) / 2);
					edge.setTranslateY((start[1] + end[1]) / 2);
					edge.setTranslateZ((start[2] + end[2]) / 2);
					
					//Get the angle between the two points
					Point3D start3D = new Point3D(start[0], start[1], start[2]);
					Point3D end3D = new Point3D(end[0], end[1], end[2]);
					Point3D angle = end3D.subtract(start3D);

					//Get the axis of rotation the cylinder
					Point3D axis = angle.crossProduct(0f, 1f, 0f);
					
					//Calculate the number of degrees to rotate about the axis.
					double rotationAmount = Math.acos(angle.normalize()
							.dotProduct(0, 1, 0));
					
					//Apply the rotation to the cylinder
					Rotate rotation = new Rotate(
							-Math.toDegrees(rotationAmount), axis);
					edge.getTransforms().addAll(rotation);

					//Add the edge to the model.
					edges.add(edge);
				}
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * @see AbstractView#getRepresentation()
	 */
	@Override
	public Object getRepresentation() {
		return edges;
	}
}
