package priv.kcl.practice.collision.complexshape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class TestFrame extends JFrame {

    InnerPanel innerPanel;

    TestFrame() {

        innerPanel = new InnerPanel();

        setContentPane(innerPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }


    public static void main(String[] args) {
        new TestFrame();
    }

}

class InnerPanel extends JPanel {

//    GraphicCircleCollisionObject g1;
//    GraphicPolygonCollisionObject g2;
    GraphicsComplexCollisionObject g1;
    GraphicPolygonCollisionObject g2;

    InnerPanel() {
//        g1 = createCircleObject();
//        g2 = createPolyObject();
        g1 = createComplexObject();
        g2 = createPolyObject();

        g1.move(new MathPoint(250, 250));

//        g2.rotate(Math.toRadians(90));
        g2.move(new MathPoint(250, 175));


        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);



                if (e.isShiftDown())
                    g2.move(new MathPoint(e.getX(), e.getY()));
                else if (e.isControlDown())
                    g2.rotate(Math.atan2(e.getY() - g2.getReferenceLocation().y, e.getX() - g2.getReferenceLocation().x));
                checkCollision();
                repaint();
            }
        });

        setPreferredSize(new Dimension(500, 500));
    }

    private void checkCollision() {
        if (g2.isCollidedWith(g1))
            g2.setFillColor(GraphicPolygonCollisionObject.COLLIDE_COLOR);
        else
            g2.setFillColor(GraphicPolygonCollisionObject.SEPARATE_COLOR);

    }

    private GraphicCircleCollisionObject createCircleObject() {
        return new GraphicCircleCollisionObject(new MathPoint(0, 0), 30);
    }

    private GraphicPolygonCollisionObject createPolyObject() {
        final double radius = 30;
        final int edgeCounts = 5;

        ArrayList<MathPoint> vertices = new ArrayList<>(edgeCounts);
        for (int i = 0; i < edgeCounts; i++) {
            final double radians = Math.toRadians(360d / edgeCounts) * i;
            final double x = radius * Math.cos(radians);
            final double y = radius * Math.sin(radians);
            vertices.add(new MathPoint(x, y));
        }

        return new GraphicPolygonCollisionObject(new MathPoint(0, 0), vertices);
    }

    private GraphicsComplexCollisionObject createComplexObject() {
        ArrayList<MathPoint> penisBodyVertices = new ArrayList<>(4);
        penisBodyVertices.add(new MathPoint(15, -40));
        penisBodyVertices.add(new MathPoint(-15, -40));
        penisBodyVertices.add(new MathPoint(-15, 40));
        penisBodyVertices.add(new MathPoint(15, 40));
        CircleCollisionObject penisHead = new CircleCollisionObject(new MathPoint(0+0, 40+0), 20d);
        PolygonCollisionObject penisBody = new PolygonCollisionObject(new MathPoint(0+0, 0+0), penisBodyVertices);
        CircleCollisionObject penisBase1 = new CircleCollisionObject(new MathPoint(-15+0, -40+0), 10d);
        CircleCollisionObject penisBase2 = new CircleCollisionObject(new MathPoint(15+0, -40+0), 10d);
        ArrayList<CollisionObject> penis = new ArrayList<>(4);
        penis.add(penisHead);
        penis.add(penisBody);
        penis.add(penisBase1);
        penis.add(penisBase2);
        return new GraphicsComplexCollisionObject(new MathPoint(0, 0), penis);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g1.onShow(g);
        g2.onShow(g);

        Toolkit.getDefaultToolkit().sync();
    }
}


class GraphicCircleCollisionObject extends CircleCollisionObject {

    public static final Color SEPARATE_COLOR = Color.PINK;
    public static final Color COLLIDE_COLOR = Color.RED;

    private Color fillColor = SEPARATE_COLOR;

    GraphicCircleCollisionObject(MathPoint referenceLocation, double radius) {
        super(referenceLocation, radius);
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void onShow(Graphics g) {
        Color originalColor = g.getColor();
        g.setColor(fillColor);
        g.fillOval(referenceLocation.getAWTX() - (int) radius, referenceLocation.getAWTY() - (int) radius, (int) (radius*2), (int) (radius*2));
        g.setColor(originalColor);

    }
}

class GraphicPolygonCollisionObject extends PolygonCollisionObject {

    public static final Color SEPARATE_COLOR = Color.PINK;
    public static final Color COLLIDE_COLOR = Color.RED;

    private Color fillColor = SEPARATE_COLOR;

    GraphicPolygonCollisionObject(MathPoint referencePoint, ArrayList<MathPoint> referenceVertices) {
        super(referencePoint, referenceVertices);
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void onShow(Graphics g) {
        Color originalColor = g.getColor();
        g.setColor(fillColor);
        Polygon p = new Polygon();
        getRotatedVertices().forEach(vertex -> p.addPoint(vertex.getAWTX(), vertex.getAWTY()));
        g.fillPolygon(p);
        g.setColor(originalColor);
    }
}

class GraphicsComplexCollisionObject extends ComplexCollisionObject {

    public static final Color SEPARATE_COLOR = Color.PINK;
    public static final Color COLLIDE_COLOR = Color.RED;

    private Color fillColor = SEPARATE_COLOR;

    GraphicsComplexCollisionObject(MathPoint referencePoint, ArrayList<CollisionObject> referenceCollisionParts) {
        super(referencePoint, referenceCollisionParts);
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void onShow(Graphics g) {
        Color originalColor = g.getColor();
        g.setColor(fillColor);
        for (CollisionObject rotatedElement : rotatedCollisionParts) {
            if (rotatedElement.getObjectType() == ObjectType.TYPE_CIRCLE) {
                MathPoint circleRefLocation = rotatedElement.getReferenceLocation();
                double circleRadius = ((CircleCollisionObject) rotatedElement).getRadius();

                GraphicCircleCollisionObject gcco = new GraphicCircleCollisionObject(circleRefLocation, circleRadius);
                gcco.setFillColor(fillColor);
                gcco.onShow(g);
            }
            else if (rotatedElement.getObjectType() == ObjectType.TYPE_POLYGON) {
                MathPoint polygonRefLocation = rotatedElement.referenceLocation;
                ArrayList<MathPoint> polygonRotatedVertices = ((PolygonCollisionObject) rotatedElement).getRotatedVertices();

                GraphicPolygonCollisionObject gpco = new GraphicPolygonCollisionObject(polygonRefLocation, polygonRotatedVertices);
                gpco.setFillColor(fillColor);
                gpco.onShow(g);
            }
        }
        g.setColor(originalColor);
    }

}
