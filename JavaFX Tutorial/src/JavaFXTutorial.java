/*
 * Copyright (c) 2013, 2014 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * MoleculeSampleApp
 */
public class JavaFXTutorial extends Application implements
		IVizUpdateableListener {

	final Group root = new Group();
	final Xform axisGroup = new Xform();
	final Xform moleculeGroup = new Xform();
	final Xform world = new Xform();
	final PerspectiveCamera camera = new PerspectiveCamera(true);
	final Xform cameraXform = new Xform();
	final Xform cameraXform2 = new Xform();
	final Xform cameraXform3 = new Xform();
	private static final double CAMERA_INITIAL_DISTANCE = -450;
	private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
	private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
	private static final double CAMERA_NEAR_CLIP = 0.1;
	private static final double CAMERA_FAR_CLIP = 10000.0;
	private static final double AXIS_LENGTH = 250.0;
	private static final double HYDROGEN_ANGLE = 104.5;
	final double CONTROL_MULTIPLIER = 0.1;
	final double SHIFT_MULTIPLIER = 10.0;
	final double MOUSE_SPEED = 0.1;
	final double ROTATION_SPEED = 2.0;
	final double TRACK_SPEED = 0.3;
	TriangleMesh tri;
	ArrayList<Cylinder> triV;
	JavaFXController controller;
	JavaFXController controllerV;
	JavaFXController controllerS;

	double mousePosX;
	double mousePosY;
	double mouseOldX;
	double mouseOldY;
	double mouseDeltaX;
	double mouseDeltaY;
	int modifierFactor = 1;

	private void buildCamera() {
		root.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);
		cameraXform3.setRotateZ(180.0);

		camera.setNearClip(CAMERA_NEAR_CLIP);
		camera.setFarClip(CAMERA_FAR_CLIP);
		camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
		cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
		cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);

	}

	private void buildAxes() {
		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.DARKRED);
		redMaterial.setSpecularColor(Color.RED);

		final PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.DARKGREEN);
		greenMaterial.setSpecularColor(Color.GREEN);

		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.DARKBLUE);
		blueMaterial.setSpecularColor(Color.BLUE);

		final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
		final Box yAxis = new Box(1, AXIS_LENGTH, 1);
		final Box zAxis = new Box(1, 1, AXIS_LENGTH);

		xAxis.setMaterial(redMaterial);
		yAxis.setMaterial(greenMaterial);
		zAxis.setMaterial(blueMaterial);

		axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
		axisGroup.setVisible(true);
		world.getChildren().addAll(axisGroup);
	}

	private void buildMolecule() {
		//
		// final PhongMaterial redMaterial = new PhongMaterial();
		// redMaterial.setDiffuseColor(Color.DARKRED);
		// redMaterial.setSpecularColor(Color.RED);
		//
		// final PhongMaterial whiteMaterial = new PhongMaterial();
		// whiteMaterial.setDiffuseColor(Color.WHITE);
		// whiteMaterial.setSpecularColor(Color.LIGHTBLUE);
		//
		// final PhongMaterial greyMaterial = new PhongMaterial();
		// greyMaterial.setDiffuseColor(Color.DARKGREY);
		// greyMaterial.setSpecularColor(Color.GREY);
		//
		// // Molecule Hierarchy
		// // [*] moleculeXform
		// // [*] oxygenXform
		// // [*] oxygenSphere
		// // [*] hydrogen1SideXform
		// // [*] hydrogen1Xform
		// // [*] hydrogen1Sphere
		// // [*] bond1Cylinder
		// // [*] hydrogen2SideXform
		// // [*] hydrogen2Xform
		// // [*] hydrogen2Sphere
		// // [*] bond2Cylinder
		//
		// Xform moleculeXform = new Xform();
		// Xform oxygenXform = new Xform();
		// Xform hydrogen1SideXform = new Xform();
		// Xform hydrogen1Xform = new Xform();
		// Xform hydrogen2SideXform = new Xform();
		// Xform hydrogen2Xform = new Xform();
		//
		// Sphere oxygenSphere = new Sphere(40.0);
		// oxygenSphere.setMaterial(redMaterial);
		//
		// Sphere hydrogen1Sphere = new Sphere(30.0);
		// hydrogen1Sphere.setMaterial(whiteMaterial);
		// hydrogen1Sphere.setTranslateX(0.0);
		//
		// Sphere hydrogen2Sphere = new Sphere(30.0);
		// hydrogen2Sphere.setMaterial(whiteMaterial);
		// hydrogen2Sphere.setTranslateZ(0.0);
		//
		// Cylinder bond1Cylinder = new Cylinder(5, 100);
		// bond1Cylinder.setMaterial(greyMaterial);
		// bond1Cylinder.setTranslateX(50.0);
		// bond1Cylinder.setRotationAxis(Rotate.Z_AXIS);
		// bond1Cylinder.setRotate(90.0);
		//
		// Cylinder bond2Cylinder = new Cylinder(5, 100);
		// bond2Cylinder.setMaterial(greyMaterial);
		// bond2Cylinder.setTranslateX(50.0);
		// bond2Cylinder.setRotationAxis(Rotate.Z_AXIS);
		// bond2Cylinder.setRotate(90.0);
		//
		// moleculeXform.getChildren().add(oxygenXform);
		// moleculeXform.getChildren().add(hydrogen1SideXform);
		// moleculeXform.getChildren().add(hydrogen2SideXform);
		// oxygenXform.getChildren().add(oxygenSphere);
		// hydrogen1SideXform.getChildren().add(hydrogen1Xform);
		// hydrogen2SideXform.getChildren().add(hydrogen2Xform);
		// hydrogen1Xform.getChildren().add(hydrogen1Sphere);
		// hydrogen2Xform.getChildren().add(hydrogen2Sphere);
		// hydrogen1SideXform.getChildren().add(bond1Cylinder);
		// hydrogen2SideXform.getChildren().add(bond2Cylinder);
		//
		// hydrogen1Xform.setTx(100.0);
		// hydrogen2Xform.setTx(100.0);
		// hydrogen2SideXform.setRotateY(HYDROGEN_ANGLE);
		//
		// moleculeGroup.getChildren().add(moleculeXform);
		//
		// world.getChildren().addAll(moleculeGroup);
	}

	private void handleMouse(Scene scene, final Node root) {

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();
			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				mouseOldX = mousePosX;
				mouseOldY = mousePosY;
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseDeltaX = (mousePosX - mouseOldX);
				mouseDeltaY = (mousePosY - mouseOldY);

				double modifier = 1.0;

				if (me.isControlDown()) {
					modifier = CONTROL_MULTIPLIER;
				}
				if (me.isShiftDown()) {
					modifier = SHIFT_MULTIPLIER;
				}
				if (me.isPrimaryButtonDown()) {
					cameraXform.ry.setAngle(cameraXform.ry.getAngle()
							- mouseDeltaX * modifierFactor * modifier
							* ROTATION_SPEED); //
					cameraXform.rx.setAngle(cameraXform.rx.getAngle()
							+ mouseDeltaY * modifierFactor * modifier
							* ROTATION_SPEED); // -
				} else if (me.isSecondaryButtonDown()) {
					double z = camera.getTranslateZ();
					double newZ = z + mouseDeltaX * MOUSE_SPEED * modifier;
					camera.setTranslateZ(newZ);
				} else if (me.isMiddleButtonDown()) {
					cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX
							* MOUSE_SPEED * modifier * TRACK_SPEED); // -
					cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY
							* MOUSE_SPEED * modifier * TRACK_SPEED); // -
				}
			}
		}); // setOnMouseDragged
	} // handleMouse

	private void handleKeyboard(Scene scene, final Node root) {

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Transformation oldTransform;
				Transformation newTransform;
				Transformation transform;
				switch (event.getCode()) {
				case Z:
					cameraXform2.t.setX(0.0);
					cameraXform2.t.setY(0.0);
					cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
					cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
					break;
				case X:
					axisGroup.setVisible(!axisGroup.isVisible());
					break;
				case V:
					moleculeGroup.setVisible(!moleculeGroup.isVisible());
					break;
				case W:
					oldTransform = controllerV.getTransform();
					newTransform = (Transformation) oldTransform.clone();
					newTransform.shiftTranslation(5f, 0f, 0f);
					controllerV.setTransform(newTransform);
					break;
				case A:
					oldTransform = controllerV.getTransform();
					newTransform = (Transformation) oldTransform.clone();
					newTransform.shiftTranslation(0f, 0f, -5f);
					controllerV.setTransform(newTransform);
					break;
				case S:
					oldTransform = controllerV.getTransform();
					newTransform = (Transformation) oldTransform.clone();
					newTransform.shiftTranslation(-5f, 0f, 0f);
					controllerV.setTransform(newTransform);
					break;
				case D:
					oldTransform = controllerV.getTransform();
					newTransform = (Transformation) oldTransform.clone();
					newTransform.shiftTranslation(0f, 0f, 5f);
					controllerV.setTransform(newTransform);
					break;
				} // switch
			} // handle()
		}); // setOnKeyPressed
	} // handleKeyboard()

	@Override
	public void start(Stage primaryStage) {
		axisGroup.setVisible(false);
		buildCamera();
		buildAxes();
		buildMolecule();

		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.BLUE);
		blueMaterial.setSpecularColor(Color.LIGHTBLUE);

		final Sphere blue = new Sphere(400);
		blue.setMaterial(blueMaterial);

		Scene scene = new Scene(root, 1024, 768, true);
		scene.setFill(Color.GREY);
		handleKeyboard(scene, world);
		handleMouse(scene, world);

		root.getChildren().add(world);

		// Create the two kinds of factories
		IViewControllerFactory factory = new JavaFXViewControllerFactory();
		IViewControllerFactory factoryV = new JavaFXVolumetricViewControllerFactory();

		// Create the vertices for the first face
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		Vertex v1 = new Vertex(50f, 0f, -50f);
		v1.setId(1);
		Vertex v2 = new Vertex(0f, 100f, 0f);
		v2.setId(2);
		Vertex v3 = new Vertex(50f, 0f, 50f);
		v3.setId(3);

		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);

		// Create the edges for the first face
		ArrayList<Edge> edges = new ArrayList<Edge>();
		Edge e1 = new Edge(v1, v2);
		e1.setId(1);
		Edge e2 = new Edge(v2, v3);
		e2.setId(2);
		Edge e3 = new Edge(v3, v1);
		e3.setId(3);

		edges.add(e1);
		edges.add(e2);
		edges.add(e3);

		// Add the polygon to the mesh
		Polygon face = new Polygon(edges, vertices);
		ArrayList<Polygon> entities = new ArrayList<Polygon>();
		entities.add(face);

		// Create the second face
		Vertex v4 = new Vertex(-50f, 0f, -50f);
		v4.setId(4);

		Edge e4 = new Edge(v2, v4);
		e4.setId(4);
		Edge e5 = new Edge(v4, v1);
		e5.setId(5);
		ArrayList<Vertex> vertices2 = new ArrayList<Vertex>();
		vertices2.add(v4);
		vertices2.add(v2);
		vertices.add(v4);
		vertices2.add(v1);
		ArrayList<Edge> edges2 = new ArrayList<Edge>();
		edges2.add(e4);
		edges2.add(e1);
		edges2.add(e5);

		Polygon face2 = new Polygon(edges2, vertices2);
		entities.add(face2);

		// Create the third face
		Vertex v5 = new Vertex(-50f, 0f, 50f);
		v5.setId(5);
		ArrayList<Vertex> vertices3 = new ArrayList<Vertex>();
		vertices3.add(v5);
		vertices3.add(v2);
		vertices.add(v5);
		vertices3.add(v4);

		Edge e6 = new Edge(v2, v5);
		e6.setId(6);
		Edge e7 = new Edge(v5, v4);
		e7.setId(7);
		ArrayList<Edge> edges3 = new ArrayList<Edge>();
		edges3.add(e6);
		edges3.add(e4);
		edges3.add(e7);

		Polygon face3 = new Polygon(edges3, vertices3);
		entities.add(face3);

		// Create the fourth face
		ArrayList<Vertex> vertices4 = new ArrayList<Vertex>();
		vertices4.add(v3);
		vertices4.add(v2);
		vertices4.add(v5);

		Edge e8 = new Edge(v5, v3);
		e8.setId(8);
		ArrayList<Edge> edges4 = new ArrayList<Edge>();
		edges4.add(e2);
		edges4.add(e6);
		edges4.add(e8);

		Polygon face4 = new Polygon(edges4, vertices4);
		entities.add(face4);

		// Create the bottom face
		ArrayList<Vertex> vertices5 = new ArrayList<Vertex>();
		vertices5.add(v1);
		vertices5.add(v3);
		vertices5.add(v5);
		vertices5.add(v4);

		ArrayList<Edge> edges5 = new ArrayList<Edge>();
		edges5.add(e3);
		edges5.add(e8);
		edges5.add(e7);
		edges5.add(e5);

		Polygon face5 = new Polygon(edges5, vertices5);
		entities.add(face5);

		// Create the component
		PlantComponent model = new PlantComponent(vertices, entities);

		HashMap<String, Double> sphereProperties = new HashMap<String, Double>();
		sphereProperties.put("radius", 25d);
		PlantComponent modelS = new PlantComponent("sphere", sphereProperties);

		// Get two controllers from the factories, one for a standard JFX mesh,
		// and one for a volumetric mesh
		controller = (JavaFXController) factory.createViewController(model);
		controllerV = (JavaFXController) factoryV.createViewController(model);
		controllerS = (JavaFXController) factory.createViewController(modelS);

		// Register as listener to controllers
		controller.register(this);
		controllerV.register(this);
		controllerS.register(this);

		// Get the TriangleMesh representation of the model and add it to the
		// scene.
		tri = (TriangleMesh) controller.getRepresentation();
		MeshView triView = new MeshView(tri);

		final PhongMaterial purpleMaterial = new PhongMaterial();
		purpleMaterial.setDiffuseColor(Color.BLUEVIOLET);
		purpleMaterial.setSpecularColor(Color.DARKGREY);
		triView.setMaterial(purpleMaterial);

		root.getChildren().add(triView);

		// Get the cylinder representation of the edges of the mesh.
		triV = (ArrayList<Cylinder>) controllerV.getRepresentation();

		// Create a transformation for translating a view 150 units in the X
		// direction and apply it to the volumetric representation
		Transformation transform = new Transformation();
		transform.setTranslation(150, 0, 0);
		controllerV.setTransform(transform);

		// Apply the transformation and add the edges to the scene
		for (Cylinder edge : triV) {
			Transformation shapeTransform = controllerV.getTransform();
			edge.setTranslateX(edge.getTranslateX()
					+ shapeTransform.getTranslation()[0]);

			root.getChildren().add(edge);
		}

		// Create a transformation for translating a view 150 units in the
		// negative x axis direction and apply it to the sphere
		Transformation transformS = new Transformation();
		transformS.setTranslation(-150f, 0, 0);
		controllerS.setTransform(transformS);

		Sphere sphereView = (Sphere) controllerS.getRepresentation();
		sphereView.setMaterial(purpleMaterial);
		sphereView
				.setTranslateX(controllerS.getTransform().getTranslation()[0]);
		root.getChildren().add(sphereView);

		primaryStage.setTitle("Molecule Sample Application");
		primaryStage.setScene(scene);
		primaryStage.show();
		scene.setCamera(camera);

	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 *
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * When an update is received, redraw the movable part of the image.
	 */
	@Override
	public void update(IVizUpdateable updateable) {
		JavaFXController controller = (JavaFXController) updateable;
		// If the previous transformation is null, the transformation is being
		// set for the first time and doesn't need to be updated.
		if (controller.getPreviousTransform() != null) {
			//Apply the difference between the current and previous translations to the edges.
			for (Cylinder edge : triV) {
				edge.setTranslateZ(edge.getTranslateZ()
						- controller.getPreviousTransform().getTranslation()[2]
						+ controllerV.getTransform().getTranslation()[2]);
				edge.setTranslateX(edge.getTranslateX()
						- controller.getPreviousTransform().getTranslation()[0]
						+ controllerV.getTransform().getTranslation()[0]);
			}
		}
	}
}