package capstan;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 * @version 0.1
 */
public class Main extends Application {

    private static final float SCENE_WIDTH = 1000;
    private static final float SCENE_HEIGHT = 800;
    private static final float WHEEL_RADIUS = 120;
    private static final float POLE_LENGTH = 450;
    private Scene scene;
    private PerspectiveCamera camera;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Cylinder wheel = new Cylinder(WHEEL_RADIUS, 10);
        wheel.rotateProperty().set(90);
        wheel.translateYProperty().set(-WHEEL_RADIUS);
        wheel.translateXProperty().set(-POLE_LENGTH);

        Cylinder pole = new Cylinder(5, POLE_LENGTH);
        pole.rotateProperty().set(90);
        pole.translateYProperty().set(-WHEEL_RADIUS);
        pole.translateXProperty().set(-POLE_LENGTH / 2);


        Group group = new Group();
        group.getChildren().add(wheel);
        group.getChildren().add(pole);

        scene = new Scene(group, SCENE_WIDTH, SCENE_HEIGHT);
        camera = new PerspectiveCamera(true);
        scene.setCamera(camera);
        camera.translateZProperty().set(-1500);
        camera.setNearClip(1);
        camera.setFarClip(2000);

        primaryStage.setTitle("Capstan");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}