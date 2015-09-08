
/**
 * A concrete implementation for AbstractController for use with JavaFX.
 * 
 * @author Robert Smith
 *
 */
public class JavaFXController extends AbstractController{
	
	/**
	 * The constructor.
	 * 
	 * @param model The managed model.
	 * @param view The managed view.
	 */
	public JavaFXController(IReactorComponent model, AbstractView view){
		super(model, view);
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractController#getRepresentation()
	 */
	public Object getRepresentation(){
		return view.getRepresentation();
	}

	/*
	 * (non-Javadoc)
	 * @see AbstractController#update(IVizUpdateable)
	 */
	@Override
	public void update(IVizUpdateable component) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractController#getTransform()
	 */
	public Transformation getTransform(){
		return view.getTransform();
	}
	

}
