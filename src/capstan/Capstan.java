package capstan;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.stage.Stage;

/**
 * @author LabGroup
 * @version 0.1
 */
public class Capstan extends Application {

    private static final float SCENE_WIDTH = 1000;
    private static final float SCENE_HEIGHT = 800;
    private static final float WHEEL_RADIUS = 120;
    private static final float POLE_LENGTH = 450;

    private Scene scene;
    private PerspectiveCamera camera;
    private Group group;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) throws Exception {
        PhongMaterial green_surface = new PhongMaterial(Color.DARKOLIVEGREEN);

        PhongMaterial wood = new PhongMaterial();
        wood.setDiffuseMap(new Image(getClass().getResourceAsStream(
                "/resources/Planks12_col.jpg")));
        wood.setBumpMap(new Image(getClass().getResourceAsStream(
                "/resources/Planks12_disp.jpg")));

        Cylinder wheel = new Cylinder(WHEEL_RADIUS, 10);
        wheel.setMaterial(wood);
        wheel.rotateProperty().set(90);
        wheel.translateYProperty().set(-WHEEL_RADIUS);
        wheel.translateXProperty().set(-POLE_LENGTH);

        Cylinder pole = new Cylinder(5, POLE_LENGTH);
        pole.setMaterial(green_surface);
        pole.rotateProperty().set(90);
        pole.translateYProperty().set(-WHEEL_RADIUS);
        pole.translateXProperty().set(-POLE_LENGTH / 2);

        group = new Group();
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