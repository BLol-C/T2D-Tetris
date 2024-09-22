package tetris;

// import java.awt.*;
import javax.swing.*;

public class MainGame extends JPanel {

    public static final int WIDTH = 445, HEIGHT = 629;
   

    private Board board;

    public MainGame() {
        JFrame f = new JFrame("T2D");
        board = new Board();
        f.setSize(WIDTH, HEIGHT);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.add(board);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new MainGame();
    }
}
