


import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class provides a base implementation for a controller that manages an
 * {@link IUpdateable} model and an {@link AbstractView} associated with that
 * model. It ensures that changes to the model or features provided by the
 * controller itself are synchronized with the view through the parent
 * SimpleApplication's simpleUpdate() thread.
 * 
 * @author Jordan Deyton
 * 
 */
public abstract class AbstractController extends VizObject implements IVizUpdateableListener {
	/*
	 * Note: We previously had an IControllerListener interface whereby objects
	 * could listen for changes to an AbstractController. If this interface is
	 * required for future work, this package (widgets.jme) and the plant
	 * package (widgets.reactoreditor.plant) should be selectively reverted to
	 * restore IControllerListener, AbstractController, and PipeController to
	 * their state before 20140903-1600. Also note that this change was
	 * committed to the branch.
	 */

	/**
	 * The model for which this controller provides a view.
	 */
	protected final IReactorComponent model;
	/**
	 * The {@link AbstractView} associated with this controller.
	 */
	protected final AbstractView view;

	/**
	 * Whether or not the controller and its view have been disposed.
	 */
	protected final AtomicBoolean disposed;

	// ----------------------- //

	/**
	 * The default constructor. If any arguments are invalid (null), an
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param model
	 *            The model for which this controller provides a view.
	 * @param view
	 *            The view associated with this controller. This needs to be
	 *            instantiated by the sub-class.
	 * @param renderQueue
	 *            The queue responsible for tasks that need to be performed on
	 *            the jME rendering thread.
	 */
	public AbstractController(IReactorComponent model, AbstractView view) {

		// Set the model and register with it. It should not be null.
		this.model = (model != null ? model : new PlantComponent());
		this.model.register(this);

		// Set the view. If it is null, create a new, basic view.
		this.view = (view != null ? view : new AbstractView("Invalid View") {
		});


		// Initialize the disposed boolean.
		disposed = new AtomicBoolean(false);

		// --------------------------------------- //

		return;
	}

	/**
	 * @return The model for which this controller provides a view.
	 */
	public IReactorComponent getModel() {
		return model;
	}


	/**
	 * Updates the controller and/or view if the {@link #model} has changed.
	 */
	@Override
	public void update(IVizUpdateable component){
		notifyListeners();
	};

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
		return view.getTransform();
	}
	
	/**
	 * Mutator function for the view's transformation.
	 * 
	 * @param transform The new transformation to apply to the view.
	 */
	public void setTransform(Transformation transform){
		view.setTransform(transform);
		notifyListeners();
	}
	
	/**
	 * Gets the view's previous transformation.
	 * 
	 * @return The view's previous transformation. 
	 */
	public Transformation getPreviousTransform(){
		return view.getPreviousTransform();
	}
}
