package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Board extends JPanel implements KeyListener {

    // กำหนดค่าตาราง
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    public static final int BLOCK_SIZE = 30;
    private Timer looper;

    private Color[][] board = new Color[BOARD_WIDTH][BOARD_HEIGHT];
    private Color[][] shape = {
            { Color.RED, Color.RED, Color.RED },
            { null, Color.RED, null } };

    // ระยะของรูปทรง
    private int x = 3, y = 0;

    // กำหนดระดับเร็ว
    private static int fps = 60;
    private static int delay = fps / 1000;

    private int normal = 600;
    private int fast = 50;
    private int delayTimerForMovement = normal;
    private long beginTime;

    private int deltaX = 0;

    public Board() {
        // ปรับเวลา
        looper = new Timer(delay, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                x += deltaX;
                deltaX = 0;
                if (System.currentTimeMillis() - beginTime > delayTimerForMovement) {
                    y++;
                    beginTime = System.currentTimeMillis();
                }
                repaint();
            }

        });
        looper.start();

    }

    // วางตาราง
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        // วาดทรง
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] != null) {
                    g.setColor(shape[i][j]);
                    g.fillRect(j * BLOCK_SIZE + x * BLOCK_SIZE, i * BLOCK_SIZE + y * BLOCK_SIZE, BLOCK_SIZE,
                            BLOCK_SIZE);
                }
            }
        }
        g.setColor(Color.white);

        // แนวนอน
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            g.drawLine(0, BLOCK_SIZE * i, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * i);
        }

        // แนวตั้ง
        for (int i = 0; i < BOARD_WIDTH + 1; i++) {
            g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, BLOCK_SIZE * BOARD_HEIGHT);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            delayTimerForMovement = fast;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            deltaX = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            deltaX = -1;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            delayTimerForMovement = normal;
        }

    }

}