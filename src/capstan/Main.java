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

    private static final float SCENE_WIDTH = 800;
    private static final float SCENE_HEIGHT = 800;
    private Scene scene;
    private PerspectiveCamera camera;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Cylinder wheel = new Cylinder(100, 200);

        Group group = new Group();
        group.getChildren().add(wheel);

        scene = new Scene(group, SCENE_WIDTH, SCENE_HEIGHT);
        camera = new PerspectiveCamera(true);
        scene.setCamera(camera);
        camera.translateZProperty().set(-1000);
        camera.setNearClip(1);
        camera.setFarClip(1000);

        primaryStage.setTitle("Capstan");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}