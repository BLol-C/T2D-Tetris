package tetris;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private int numOfPlayers;

    public Window() {
        setTitle("Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(44, 66, 86);
                Color color2 = new Color(41, 125, 180);
                GradientPaint gp = new GradientPaint(0, 0, color2, 0, getHeight(), color1);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("T 2 P");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel promptLabel = new JLabel("Start game: ");
        promptLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton onePlayerButton = createStyledButton("1 Player");
        JButton twoPlayersButton = createStyledButton("2 Players");
        JButton exit = createStyledButton("Exit");

        onePlayerButton.addActionListener((e) -> {
            numOfPlayers = 1;
            startGame();
        });

        twoPlayersButton.addActionListener((e) -> {
            numOfPlayers = 2;
            startGame();
        });

        exit.addActionListener((e) -> {
            System.exit(0);
        });

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        menuPanel.add(promptLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(onePlayerButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(twoPlayersButton);
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(exit);
        menuPanel.add(Box.createVerticalGlue());

        add(menuPanel);

        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 152, 219));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(150, 40));
        return button;
    }

    private void startGame() {
        getContentPane().removeAll();
        setSize(400 * numOfPlayers, 600);
        add(new TetrisPanel(numOfPlayers));
        revalidate();
        repaint();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window().setVisible(true);
            }
        });
    }
}