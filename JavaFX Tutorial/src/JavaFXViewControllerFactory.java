/**
 * A ViewControllerFactory which creates an appropriate view and controller for
 * an IReactorComponent displayed in JavaFX. It handles sphere meshes and custom
 * meshes, in which case the mesh is rendered as a solid.
 * 
 * @author Robert Smith
 *
 */
public class JavaFXViewControllerFactory implements IViewControllerFactory {

	/**
	 * Creates a JavaFXView and JavaFXController for the given
	 * IReactorComponent, returning the new controller.
	 */
	@Override
	public AbstractController createViewController(IReactorComponent model) {
		JavaFXController controller = null;

		// If the model is a PlantComponent, create the view and controller.
		if (model instanceof PlantComponent) {
			JavaFXView view = new JavaFXView(model);
			controller = new JavaFXController(model, view);
		}
		return controller;
	}
}
