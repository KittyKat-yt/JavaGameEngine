package game_engine.render;

import game_engine.script.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class RenderWindow extends JFrame implements KeyListener, MouseMotionListener {
    public static final float ASPECT_RATIO = (3f / 2f);
    private static final int WINDOW_HEIGHT = 900;
    private static final int scalingFactor = 2;

    private static Robot robot = null;
    private static Camera cam;

    public RenderWindow(String title, float fov) {
        super(title);
        addKeyListener(this);
        addMouseMotionListener(this);
        setResizable(false);

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "invisible cursor");
        setCursor(invisibleCursor);

        int windowWidth = (int) (WINDOW_HEIGHT * ASPECT_RATIO);
        setSize(windowWidth, WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        cam = new Camera(fov, windowWidth / scalingFactor, WINDOW_HEIGHT / scalingFactor, windowWidth, WINDOW_HEIGHT);
        add(cam);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void startRendering() {
        setVisible(true);
        setFocusable(true);
        cam.startRendering();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        Input.pressKey(e.getKeyCode());
    }
    @Override
    public void keyReleased(KeyEvent e) {
        Input.releaseKey(e.getKeyCode());
    }

    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {
        int halfWidth = getWidth() / 2;
        int halfHeight = getHeight() / 2;
        Input.moveMouse(
                (e.getXOnScreen() - getX() - halfWidth) / ((float) halfWidth),
                (e.getYOnScreen() - getY() - halfHeight) / ((float) halfHeight));
        robot.mouseMove(getX() + halfWidth, getY() + halfHeight);
    }
}
