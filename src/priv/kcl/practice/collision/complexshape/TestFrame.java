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

    GraphicCircleObject g1;
    GraphicPolygonObject g2;

    InnerPanel() {
        g1 = createCircleObject();
        g2 = createPolyObject();

        g1.setReferenceLocation(new MathPoint(250, 250));

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
        if (CollisionObject.checkCollision(g1, g2))
            g2.setFillColor(GraphicPolygonObject.COLLIDE_COLOR);
        else
            g2.setFillColor(GraphicPolygonObject.SEPARATE_COLOR);

    }

    private GraphicCircleObject createCircleObject() {
        return new GraphicCircleObject(new MathPoint(0, 0), 30);
    }

    private GraphicPolygonObject createPolyObject() {
        final double radius = 30;
        final int edgeCounts = 5;

        ArrayList<MathPoint> vertices = new ArrayList<>(edgeCounts);
        for (int i = 0; i < edgeCounts; i++) {
            final double radians = Math.toRadians(360d / edgeCounts) * i;
            final double x = radius * Math.cos(radians);
            final double y = radius * Math.sin(radians);
            vertices.add(new MathPoint(x, y));
        }

        return new GraphicPolygonObject(new MathPoint(0, 0), vertices);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g1.onShow(g);
        g2.onShow(g);

        Toolkit.getDefaultToolkit().sync();
    }
}


class GraphicCircleObject extends CircleObject {

    public static final Color SEPARATE_COLOR = Color.PINK;
    public static final Color COLLIDE_COLOR = Color.RED;

    private Color fillColor = SEPARATE_COLOR;

    GraphicCircleObject(MathPoint referenceLocation, double radius) {
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

class GraphicPolygonObject extends PolygonObject {

    public static final Color SEPARATE_COLOR = Color.PINK;
    public static final Color COLLIDE_COLOR = Color.RED;

    private Color fillColor = SEPARATE_COLOR;

    GraphicPolygonObject(MathPoint referencePoint, ArrayList<MathPoint> referenceVertices) {
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

