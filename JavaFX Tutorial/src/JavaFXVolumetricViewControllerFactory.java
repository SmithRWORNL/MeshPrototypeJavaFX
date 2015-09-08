/**
 * A ViewControllerFactory which creates an appropriate view and controller for
 * an IReactorComponent displayed in JavaFX. It handles custom meshes which are
 * to be drawn in wireframe mode, such as to display internal vertices in a
 * volumetric mesh.
 * 
 * @author Robert Smith
 *
 */
public class JavaFXVolumetricViewControllerFactory implements
		IViewControllerFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see IViewControllerFactory#createViewController(IReactorComponent)
	 */
	@Override
	public AbstractController createViewController(IReactorComponent model) {
		JavaFXController controller = null;

		// If the model is a plant component, create a view and controller for
		// it.
		if (model instanceof PlantComponent) {
			AbstractView view = new JavaFXVolumetricView(model);
			controller = new JavaFXController(model, view);
		}
		return controller;
	}
}
