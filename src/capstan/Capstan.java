package capstan;

import javafx.animation.*;
import javafx.application.Application;
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
 * @version 1.0
 */
public class Capstan extends Application {

    private static final double SCENE_WIDTH = 1000;
    private static final double SCENE_HEIGHT = 800;
    private static final double WHEEL_RADIUS = 120;
    private static final double POLE_LENGTH = 450;

    private Scene scene;
    private PerspectiveCamera camera;
    private Group capstan;
    private Cylinder wheel;
    private Cylinder pole;

    @Override
    public void start(Stage primaryStage) throws Exception {

        prepareCapstan();

        scene = new Scene(capstan, SCENE_WIDTH, SCENE_HEIGHT);
        camera = new PerspectiveCamera(true);
        scene.setCamera(camera);
        camera.translateZProperty().set(-1500);
        camera.setNearClip(1);
        camera.setFarClip(2000);

        primaryStage.setTitle("Computergrafik Labor 3 - Capstan");
        primaryStage.setScene(scene);
        primaryStage.show();

        startAnimation();
    }

    private void prepareCapstan() {
        preparePole();
        prepareWheel();

        capstan = new Group();
        capstan.getChildren().addAll(new AmbientLight(), wheel, pole);
        capstan.getTransforms().add(new Translate(-POLE_LENGTH / 2, -WHEEL_RADIUS, 0));
    }

    private void preparePole() {
        PhongMaterial steel = new PhongMaterial();
        steel.setDiffuseMap(new Image(getClass().getResourceAsStream(
                "/resources/Metal23_col.jpg")));
        steel.setBumpMap(new Image(getClass().getResourceAsStream(
                "/resources/Metal23_disp.jpg")));

        pole = new Cylinder(5, POLE_LENGTH);
        pole.setMaterial(steel);

        pole.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
    }

    private void prepareWheel() {
        PhongMaterial wood = new PhongMaterial();
        wood.setDiffuseMap(new Image(getClass().getResourceAsStream(
                "/resources/Planks12_col.jpg")));
        wood.setBumpMap(new Image(getClass().getResourceAsStream(
                "/resources/Planks12_disp.jpg")));

        wheel = new Cylinder(WHEEL_RADIUS, 10);
        wheel.setMaterial(wood);

        wheel.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
        wheel.translateXProperty().set(-POLE_LENGTH / 2);
    }

    private void startAnimation() {
        RotateTransition wheelRotation = new RotateTransition(Duration.seconds(60), wheel);
        wheelRotation.setCycleCount(Integer.MAX_VALUE);
        wheelRotation.setAxis(Rotate.X_AXIS);
        wheelRotation.setByAngle(360 * POLE_LENGTH / WHEEL_RADIUS);
        wheelRotation.setInterpolator(Interpolator.LINEAR);

        RotateTransition capstanRotation = new RotateTransition(Duration.seconds(60), capstan);
        capstanRotation.setCycleCount(Integer.MAX_VALUE);
        capstanRotation.setAxis(Rotate.Y_AXIS);
        capstanRotation.setByAngle(-360);
        capstanRotation.setInterpolator(Interpolator.LINEAR);

        ParallelTransition animation = new ParallelTransition(wheelRotation, capstanRotation);
        animation.play();
    }
}