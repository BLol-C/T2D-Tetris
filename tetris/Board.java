package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel implements ActionListener {
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    public static final int BLOCK_SIZE = 30;
    private Timer looper;
    private Color[][] board = new Color[BOARD_WIDTH][BOARD_HEIGHT];

    public Board() {
        looper = new Timer(500, this);
        looper.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.white);

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            g.drawLine(0, BLOCK_SIZE * i, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * i);
        }
        for (int i = 0; i < BOARD_WIDTH + 1; i++) {
            g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, BLOCK_SIZE * BOARD_HEIGHT);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
