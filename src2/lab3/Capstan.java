package lab3;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Capstan extends Group {

    private final double GROUND_Y_DELTA = 20.0;
    private final double ARENA_Y_DELTA = 18.0;
    private final double ARENA_RADIUS_DELTA = 100.0;

    private double dAngleWheel;
    private double dAngleCapstan;
    private double dAngle;

    private Rotate rotateY1 = new Rotate();
    private Rotate rotateY2 = new Rotate();

    private Box ground;
    private Cylinder arena;
    private Cylinder wheel;
    private Cylinder connector;
    private Cylinder center;

    public Capstan(double dt, double omega, Box ground, Cylinder wheel, Cylinder connector, Cylinder center) {
        this.dAngle = Math.toDegrees(omega * dt);
        this.dAngleWheel = Math.toDegrees(omega * dt * connector.getHeight() / wheel.getRadius());
        this.ground = ground;
        this.wheel = wheel;
        this.connector = connector;
        this.center = center;
        
        arena = new Cylinder();

        init();
    }

    private void init() {
        initWheel();
        initConnector();
        initGround();
        initArena();

        rotateY2.setAngle(0);
        rotateY2.setAxis(Rotate.Y_AXIS);

        Translate translateCenter = new Translate(0, wheel.getRadius(), 0);
        
        PhongMaterial material = createDiffuseMap("/resources/iron.jpg");
        connector.setMaterial(material);
        center.setMaterial(material);

        center.getTransforms().addAll(translateCenter);
        getChildren().addAll(ground, arena, wheel, connector, center);
    }

    private void initGround() {
        ground.setMaterial(createDiffuseMap("/resources/grass.jpg"));
        Translate translate = new Translate(0, center.getHeight() + GROUND_Y_DELTA, 0);
        ground.getTransforms().addAll(translate);
    }

    private void initArena() {
        arena.setHeight(1);
        arena.setRadius(connector.getHeight() + ARENA_RADIUS_DELTA);
        arena.getTransforms().addAll(new Translate(0, center.getHeight() + ARENA_Y_DELTA, 0));
        arena.setMaterial(createDiffuseMap("/resources/sand.jpg"));
    }

    private void initWheel() {
        wheel.setMaterial(createDiffuseMap("/resources/wood.jpg"));

        Rotate rotateX = new Rotate();
        rotateX.setAngle(90d);
        rotateX.setAxis(Rotate.X_AXIS);

        rotateY1 = new Rotate();
        rotateY1.setAngle(0);
        rotateY1.setAxis(Rotate.Y_AXIS);

        Translate translate = new Translate(0, wheel.getRadius(), connector.getHeight());

        wheel.getTransforms().addAll(rotateY2, translate, rotateX, rotateY1);
    }

    private void initConnector() {
        Rotate rotateX = new Rotate();
        rotateX.setAngle(90d);
        rotateX.setAxis(Rotate.X_AXIS);

        Translate translate = new Translate(0, wheel.getRadius(), connector.getHeight() / 2);

        connector.getTransforms().addAll(rotateY2, translate, rotateX);
    }

    private PhongMaterial createDiffuseMap(String resourcePath) {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream(resourcePath)));

        return material;
    }

    public void transform() {
        rotateY1.setAngle(rotateY1.getAngle() - dAngleWheel);
        rotateY2.setAngle(rotateY2.getAngle() - dAngle);
    }
}
