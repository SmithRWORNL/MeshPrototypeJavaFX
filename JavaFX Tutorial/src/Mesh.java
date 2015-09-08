import java.util.*;

/**
 * A class containing the specifying information for the graphical
 * representation of a component to be modeled. It has two different methods of
 * representing data, dependent on how the part is described.
 * 
 * 1) The part can be described by a series of vertices in three dimensional
 * space and a series of entities, lists of indices into the vertex list which
 * provide information about which vertices are graphically associated with one
 * another. An entity may have any cardinality, and could, for example,
 * represent an edge (two vertices) or a polygonal face (three or more vertices)
 * 
 * 2) The part can be described by a series of arbitrary properties with a
 * double value associated with each. Each property should describe some
 * physical characteristic of the part to be used in modeling. A pipe, for
 * example, may have "length," "inner_radius," and "outer_radius" properties.
 * 
 * A mesh has a type specifying which of the two it uses. The type "custom"
 * should be used for meshes which specify vertices directly. For meshes which
 * use properties, the type should be a descriptive name which will communicate
 * to the client which properties it should have. For example, a type might be
 * "sphere," implying a "radius" property, or "cube," implying "width,"
 * "height," and "depth" properties
 * 
 * @author Robert Smith
 *
 */
public class Mesh {

	private String type;

	private List<Vertex> vertices;

	private List<List<Integer>> entities;

	private Map<String, Double> properties;

	/**
	 * Constructor for custom meshes.
	 * 
	 * @param vertices
	 *            The vertices for the mesh.
	 * @param entities
	 *            The lists of associated vertices which comprise the entities
	 *            to be modeled in the mesh.
	 */
	public Mesh(List<Vertex> vertices, List<List<Integer>> entities) {
		this.type = "custom";
		this.vertices = vertices;
		this.entities = entities;
	}

	/**
	 * Constructor for properties using meshes.
	 * 
	 * @param type
	 *            The type of the mesh.
	 * @param properties
	 *            The map of the part's graphical properties.
	 */
	public Mesh(String type, Map<String, Double> properties) {
		this.type = type;
		this.properties = properties;
	}

	/**
	 * Getter method for the mesh's type.
	 * 
	 * @return The mesh's type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Getter method for the mesh's entities.
	 * 
	 * @return The mesh's entities.
	 */
	public List<List<Integer>> getEntities() {
		return entities;
	}

	/**
	 * Getter method for the mesh's vertices.
	 * 
	 * @return The mesh's vertices.
	 */
	public List<Vertex> getVertices() {
		return vertices;
	}

	/**
	 * Gets the value for a specific property from the mesh.
	 * 
	 * @param property
	 *            The property whose value is requested.
	 * @return The specified property's value.
	 */
	public double getProperty(String property) {
		return properties.get(property);
	}

	/**
	 * Setter for the value of the mesh's properties. This overwrites any
	 * previous value, as each property should have exactly one associated
	 * value.
	 * 
	 * @param property The property to set
	 * @param value The property's new value.
	 */
	public void setProperty(String property, double value) {
		properties.remove(property);
		properties.put(property, value);
	}

}
