package tetris;

import java.awt.Color;

import javax.swing.JFrame;

public class WindowGame {
    public static final int WIDTH = 900, HEIGHT = 639;
  

    private Board board1;
    private Board board2;
    private Title title;
    private JFrame window;

    public WindowGame() {
        window = new JFrame("T2D");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);

        board1 = new Board(0, 0, true);  // Left board
        board2 = new Board(445, 0, false);  // Right board
        title = new Title(this);

        window.addKeyListener(board1);
        window.addKeyListener(board2);
        window.addKeyListener(title);
        window.add(title);
        window.setVisible(true);
    }

    public void startTetris() {
        window.remove(title);
        window.add(board1);
        window.add(board2);
        board1.startGame();
        board2.startGame();
        window.revalidate();
    }

    public static void main(String[] args) {
        new WindowGame();
    }
}