package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

	// Assets
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage pause, refresh;

	// board dimensions (the playing area)
	private final int boardHeight = 20, boardWidth = 10;

	// block size
	public static final int blockSize = 30;

	// field
	private Color[][] board = new Color[boardHeight][boardWidth];

	// array with all the possible shapes
	private Shape[] shapes = new Shape[7];

	// currentShape
	private static Shape currentShape, nextShape;

	// game loop
	private Timer looper;

	private int FPS = 60;

	private int delay = 1000 / FPS;

	// mouse events variables
	private int mouseX, mouseY;

	private boolean leftClick = false;

	private Rectangle stopBounds, refreshBounds;

	private boolean gamePaused = false;

	private boolean gameOver = false;

	private boolean isLeftBoard;

	private Color[] colors = { Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"),
			Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc") };
	private Random random = new Random();
	// buttons press lapse
	private Timer buttonLapse = new Timer(300, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			buttonLapse.stop();
		}
	});

	// score
	private int score = 0;

	private int boardX, boardY;


	public Board(int x, int y, boolean isLeftBoard) {
		this.boardX = x;
		this.boardY = y;
		this.isLeftBoard = isLeftBoard;

		pause = ImageLoader.loadImage("/pause.png");
		refresh = ImageLoader.loadImage("/refresh.png");

		mouseX = 0;
		mouseY = 0;

		stopBounds = new Rectangle(boardX + 350, boardY + 500, pause.getWidth(),
				pause.getHeight() + pause.getHeight() / 2);
		refreshBounds = new Rectangle(boardX + 350, boardY + 500 - refresh.getHeight() - 20, refresh.getWidth(),
				refresh.getHeight() + refresh.getHeight() / 2);

		// create game looper
		looper = new Timer(delay, new GameLooper());

		// create shapes
		shapes[0] = new Shape(new int[][] { { 1, 1, 1, 1 } // I shape;
		}, this, colors[0]);

		shapes[1] = new Shape(new int[][] { { 1, 1, 1 }, { 0, 1, 0 }, // T shape;
		}, this, colors[1]);

		shapes[2] = new Shape(new int[][] { { 1, 1, 1 }, { 1, 0, 0 }, // L shape;
		}, this, colors[2]);

		shapes[3] = new Shape(new int[][] { { 1, 1, 1 }, { 0, 0, 1 }, // J shape;
		}, this, colors[3]);

		shapes[4] = new Shape(new int[][] { { 0, 1, 1 }, { 1, 1, 0 }, // S shape;
		}, this, colors[4]);

		shapes[5] = new Shape(new int[][] { { 1, 1, 0 }, { 0, 1, 1 }, // Z shape;
		}, this, colors[5]);

		shapes[6] = new Shape(new int[][] { { 1, 1 }, { 1, 1 }, // O shape;
		}, this, colors[6]);

	}

	private void update() {
		if (stopBounds.contains(mouseX, mouseY) && leftClick && !buttonLapse.isRunning() && !gameOver) {
			buttonLapse.start();
			gamePaused = !gamePaused;
		}

		if (refreshBounds.contains(mouseX, mouseY) && leftClick) {
			startGame();
		}

		if (gamePaused || gameOver) {
			return;
		}
		currentShape.update();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw background
		g.setColor(Color.BLACK);
		g.fillRect(boardX, boardY, boardWidth * blockSize, boardHeight * blockSize);
		g.fillRect(0, 0, boardWidth * blockSize, boardHeight * blockSize);

		// Draw Shape_left
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {

				if (board[row][col] != null) {
					g.setColor(board[row][col]);
					g.fillRect(boardX + col * blockSize, boardY + row * blockSize, blockSize, blockSize);
				}

			}
		}

		// Draw Shape_right
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {

				if (board[row][col] != null) {
					g.setColor(board[row][col]);
					g.fillRect(0 + col * blockSize, 0 + row * blockSize, blockSize, blockSize);
				}

			}
		}

		// Draw current shape
		g.setColor(currentShape.getColor());
		for (int row = 0; row < currentShape.getCoords().length; row++) {
			for (int col = 0; col < currentShape.getCoords()[0].length; col++) {
				if (currentShape.getCoords()[row][col] != 0) {
					g.fillRect(boardX + (currentShape.getX() + col) * blockSize,
							boardY + (currentShape.getY() + row) * blockSize, blockSize, blockSize);
				}
			}
		}

		// Draw next shape
		g.setColor(nextShape.getColor());
		for (int row = 0; row < nextShape.getCoords().length; row++) {
			for (int col = 0; col < nextShape.getCoords()[0].length; col++) {
				if (nextShape.getCoords()[row][col] != 0) {
					g.fillRect(boardX + col * 30 + 320, boardY + row * 30 + 50, Board.blockSize, Board.blockSize);
				}
			}
		}
		currentShape.render(g);

		// Draw buttons
		if (stopBounds.contains(mouseX, mouseY)) {
			g.drawImage(
					pause.getScaledInstance(pause.getWidth() + 3, pause.getHeight() + 3, BufferedImage.SCALE_DEFAULT),
					boardX + stopBounds.x + 3, boardY + stopBounds.y + 3, null);
		} else {
			g.drawImage(pause, boardX + stopBounds.x, boardY + stopBounds.y, null);
		}

		if (refreshBounds.contains(mouseX, mouseY)) {
			g.drawImage(
					refresh.getScaledInstance(refresh.getWidth() + 3, refresh.getHeight() + 3,
							BufferedImage.SCALE_DEFAULT),
					boardX + refreshBounds.x + 3, boardY + refreshBounds.y + 3, null);
		} else {
			g.drawImage(refresh, boardX + refreshBounds.x, boardY + refreshBounds.y, null);
		}

		// Draw game state messages
		g.setColor(Color.WHITE);
		g.setFont(new Font("Georgia", Font.BOLD, 20));

		if (gamePaused) {
			String gamePausedString = "GAME PAUSED";
			g.drawString(gamePausedString, boardX + 35, boardY + WindowGame.HEIGHT / 2);
		}
		if (gameOver) {
			String gameOverString = "GAME OVER";
			g.drawString(gameOverString, boardX + 50, boardY + WindowGame.HEIGHT / 2);
		}

		g.setColor(Color.BLACK);

		g.setFont(new Font("Georgia", Font.BOLD, 20));

		// Draw score
		g.drawString("SCORE", boardX + WindowGame.WIDTH / 2 - 125, boardY + WindowGame.HEIGHT / 2);
		g.drawString(score + "", boardX + WindowGame.WIDTH / 2 - 125, boardY + WindowGame.HEIGHT / 2 + 30);

		g.setColor(Color.WHITE);

		// Draw Lines_left
		for (int i = 0; i <= boardHeight; i++) {
			g.drawLine(boardX, boardY + i * blockSize, boardX + boardWidth * blockSize, boardY + i * blockSize);
		}
		for (int j = 0; j <= boardWidth; j++) {
			g.drawLine(boardX + j * blockSize, boardY, boardX + j * blockSize, boardY + boardHeight * blockSize);
		}

		// Draw Lines_light
		for (int i = 0; i <= boardHeight; i++) {
			g.drawLine(0, boardY + i * blockSize, 0 + boardWidth * blockSize, boardY + i * blockSize);
		}
		for (int j = 0; j <= boardWidth; j++) {
			g.drawLine(0 + j * blockSize, boardY, 0 + j * blockSize, boardY + boardHeight * blockSize);
		}
	}

	public void setNextShape() {
		int index = random.nextInt(shapes.length);
		int colorIndex = random.nextInt(colors.length);
		nextShape = new Shape(shapes[index].getCoords(), this, colors[colorIndex]);
	}

	public void setCurrentShape() {
		currentShape = nextShape;
		setNextShape();

		for (int row = 0; row < currentShape.getCoords().length; row++) {
			for (int col = 0; col < currentShape.getCoords()[0].length; col++) {
				if (currentShape.getCoords()[row][col] != 0) {
					if (board[currentShape.getY() + row][currentShape.getX() + col] != null) {
						gameOver = true;
					}
				}
			}
		}

	}

	public Color[][] getBoard() {
		return board;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (isLeftBoard) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				currentShape.rotateShape();
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				currentShape.setDeltaX(1);
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				currentShape.setDeltaX(-1);
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				currentShape.speedUp();
			}
		} else {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				currentShape.rotateShape();
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				currentShape.setDeltaX(1);
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				currentShape.setDeltaX(-1);
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				currentShape.speedUp();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (isLeftBoard) {
			if (e.getKeyCode() == KeyEvent.VK_S) {
				currentShape.speedDown();
			}
		} else {
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				currentShape.speedDown();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void startGame() {
		stopGame();
		setNextShape();
		setCurrentShape();
		gameOver = false;
		looper.start();

	}

	public void stopGame() {
		score = 0;

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col] = null;
			}
		}
		looper.stop();
	}

	class GameLooper implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			update();
			repaint();
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			leftClick = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			leftClick = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public void addScore() {
		score++;
	}

	public void handleKeyPress(KeyEvent e) {
        if (isLeftBoard) {
            if (e.getKeyCode() == KeyEvent.VK_W) {
                currentShape.rotateShape();
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                currentShape.setDeltaX(1);
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                currentShape.setDeltaX(-1);
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                currentShape.speedUp();
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                currentShape.rotateShape();
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                currentShape.setDeltaX(1);
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                currentShape.setDeltaX(-1);
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                currentShape.speedUp();
            }
        }
    }
	

}
