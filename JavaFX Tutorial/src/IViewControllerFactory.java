/**
 * An interface for classes which take as input an IReactorComponent and create
 * instances of AbstractView and AbstractController subclasses specific to that
 * implementation.
 * 
 * @author r8s
 *
 */
public interface IViewControllerFactory {

	/**
	 * Creates and initializes controller, along with a view contained in it,
	 * appropriate to the type of IReactorComponent given as input.
	 * 
	 * @param model
	 *            The component for which a view and controller are to be
	 *            created.
	 * @return A controller containing the model and an apprioriate view.
	 */
	public AbstractController createViewController(IReactorComponent model);
}
