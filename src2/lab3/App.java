package lab3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    private Box createGround() {
        Box box = new Box();
        box.setHeight(1);
        box.setWidth(8000);
        box.setDepth(2000);

        return box;
    }

    @Override
    public void start(Stage stage) {
        ImagePattern backgroundImage = new ImagePattern(new Image(getClass().getResourceAsStream("/resources/sky.jpg")));

        AmbientLight light = new AmbientLight();
        Group lightGroup = new Group();
        lightGroup.getChildren().addAll(light);

        Cylinder wheel = new Cylinder();
        wheel.setHeight(20f);
        wheel.setRadius(120f);

        Cylinder connector = new Cylinder();
        connector.setHeight(450f);
        connector.setRadius(30f);

        Cylinder center = new Cylinder();
        center.setHeight(220f);
        center.setRadius(80f);

        Capstan capstan = new Capstan(0.001, 2 * Math.PI / 60, createGround(), wheel, connector, center);
        capstan.getChildren().addAll(lightGroup);

        final Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> capstan.transform()),
                new KeyFrame(Duration.millis(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(capstan, 1024, 800, true);
        scene.setFill(backgroundImage);

        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(-500);
        camera.setTranslateY(-700);
        camera.setTranslateZ(-600);
        camera.getTransforms().addAll(new Rotate(-22.5, Rotate.X_AXIS));
        scene.setCamera(camera);

        stage.setTitle("CGLab Aufgabe 3");

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
