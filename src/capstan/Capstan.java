package capstan;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author LabGroup
 * @version 0.1
 */
public class Capstan extends Application {

    private static final double SCENE_WIDTH = 1000;
    private static final double SCENE_HEIGHT = 800;
    private static final double WHEEL_RADIUS = 120;
    private static final double POLE_LENGTH = 450;

    private Scene scene;
    private PerspectiveCamera camera;
    private Group group;
    private Cylinder wheel;
    private Cylinder pole;
    private Point3D centerPoint = new Point3D(0,0,0);

    @Override
    public void start(Stage primaryStage) throws Exception {

        preparePole();
        prepareWheel();

        group = new Group();
        group.getChildren().addAll(new AmbientLight(), wheel, pole);
//        group.setRotationAxis(Rotate.X_AXIS);

        scene = new Scene(group, SCENE_WIDTH, SCENE_HEIGHT);
        camera = new PerspectiveCamera(true);
        scene.setCamera(camera);
        camera.translateZProperty().set(-1500);
        camera.setNearClip(1);
        camera.setFarClip(2000);

        primaryStage.setTitle("Capstan");
        primaryStage.setScene(scene);
        primaryStage.show();

        startAnimation();
    }

    private void preparePole() {
        PhongMaterial steel = new PhongMaterial();
        steel.setDiffuseMap(new Image(getClass().getResourceAsStream(
                "/resources/Metal23_col.jpg")));
        steel.setBumpMap(new Image(getClass().getResourceAsStream(
                "/resources/Metal23_disp.jpg")));

        pole = new Cylinder(5, POLE_LENGTH);
        pole.setMaterial(steel);
//        pole.rotateProperty().set(90);
        pole.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
//        pole.translateYProperty().set(-WHEEL_RADIUS);
//        pole.translateXProperty().set(-POLE_LENGTH / 2);
        pole.getTransforms().add(new Translate(-WHEEL_RADIUS,POLE_LENGTH / 2,0));
    }

    private void prepareWheel() {
        PhongMaterial wood = new PhongMaterial();
        wood.setDiffuseMap(new Image(getClass().getResourceAsStream(
                "/resources/Planks12_col.jpg")));
        wood.setBumpMap(new Image(getClass().getResourceAsStream(
                "/resources/Planks12_disp.jpg")));


        wheel = new Cylinder(WHEEL_RADIUS, 10);
        wheel.setMaterial(wood);
//        wheel.rotateProperty().set(90);
        wheel.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
        wheel.translateYProperty().set(-WHEEL_RADIUS);
        wheel.translateXProperty().set(-POLE_LENGTH);
//        wheel.getTransforms().add(new Translate(-WHEEL_RADIUS,POLE_LENGTH,0));
    }

    private void startAnimation() {

        RotateTransition wheelRotation = new RotateTransition(Duration.seconds(60), wheel);
        wheelRotation.setCycleCount(Integer.MAX_VALUE);
        wheelRotation.setAxis(Rotate.X_AXIS);
//        wheelRotation.axisProperty().set(Rotate.X_AXIS);
        wheelRotation.setByAngle(360 * POLE_LENGTH / WHEEL_RADIUS);
        wheelRotation.setInterpolator(Interpolator.LINEAR);

        RotateTransition capstanRotation = new RotateTransition(Duration.seconds(60), group);
        capstanRotation.setCycleCount(Integer.MAX_VALUE);
//        capstanRotation.setAxis(Rotate.Y_AXIS);
//        capstanRotation.axisProperty().set(Rotate.Y_AXIS);
        capstanRotation.setAxis(centerPoint.midpoint(Rotate.Y_AXIS));
        capstanRotation.setByAngle(-360);
        capstanRotation.setInterpolator(Interpolator.LINEAR);

        ParallelTransition animation = new ParallelTransition(wheelRotation,capstanRotation);
        animation.play();

    }
}