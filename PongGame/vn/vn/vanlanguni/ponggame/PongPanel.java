package vn.vanlanguni.ponggame;

/*
 * PONG GAME REQUIREMENTS
 * This simple "tennis like" game features two paddles and a ball, 
 * the goal is to defeat your opponent by being the first one to gain 3 point,
 *  a player gets a point once the opponent misses a ball. 
 *  The game can be played with two human players, one on the left and one on 
 *  the right. They use keyboard to start/restart game and control the paddles. 
 *  The ball and two paddles should be red and separating lines should be green. 
 *  Players score should be blue and background should be black.
 *  Keyboard requirements:
 *  + P key: start
 *  + Space key: restart
 *  + W/S key: move paddle up/down
 *  + Up/Down key: move paddle up/down
 *  
 *  Version: 0.5
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 
 * @author CaBaNis = Team 2.5
 *
 */
public class PongPanel extends JPanel implements ActionListener, KeyListener, MouseMotionListener , MouseListener{
	
	private static final long serialVersionUID = 1L;

	private boolean showTitleScreen = true;
	private boolean playing;
	private boolean gameOver;

	
	boolean setting;
	

	private volatile boolean isPaused = false;
	//button play
	Point  pPlay , pSetting,pBack,pMenu,pSa;
	ImageIcon imgbtnPlay,imgbtnSetting,imgbtnBack,imgbgP,imgbtnMenu,imgbtnSa;
	int  rPlay,rSetting,rBack,rMenu,rSa;
	String nameP,nameS,nameB,namePlayer1,namePlayer2;
	boolean intersec, intersec1,intersec2,intersec3,intersec4 ;

	/** Background. */
	//private Color backgroundColor = Color.BLACK;//
	ImageIcon imgbpong;
	
	/** State on the control keys. */
	private boolean upPressed;
	private boolean downPressed;
	private boolean wPressed;
	private boolean sPressed;

	/** The ball: position, diameter */
	private int ballX = 200; // qua bong di chuyen tu vi tri trung tam khi bat
								// dau game
	private int ballY = 200; // ... //
	private int diameter = 20;
	private int ballDeltaX = -1;
	private int ballDeltaY = 3;

	/** Player 1's paddle: position and size */
	private int playerOneX = 0;
	private int playerOneY = 250;
	private int playerOneWidth = 30;
	private int playerOneHeight = 80;

	/** Player 2's paddle: position and size */
	private int playerTwoX = 465; // Doi chieu dai cua 2 thanh chan bang nhau
	private int playerTwoY = 250;
	private int playerTwoWidth = 30;
	private int playerTwoHeight = 80;

	/** Speed of the paddle - How fast the paddle move. */
	private int paddleSpeed = 5;

	/** Player score, show on upper left and right. */
	private int playerOneScore;
	private int playerTwoScore;
	

	/** Construct a PongPanel. */
	public PongPanel() {
		
		//setBackground(backgroundColor); //Chung ta them hinh` khong dung` mau`
		//Positions setup
		namePlayer1 = "Dep trai 1 ";
		namePlayer2 = "Dep trai 2 ";
		pBack = new Point(25,445);
		pPlay = new Point(222, 300);
		pSetting = new Point(25,445);
		pMenu = new Point(180,280);
		pSa = new Point(180,400);
		rMenu = 40 ;
		rSa = 35;
		rSetting = 20;
		rBack = 15;
		rPlay = 40;
		
		
		// Khai bien de chen` hinh`
		nameP = ""; //nut play
		nameS = ""; //nut setting
		nameB = ""; //nut back
		imgpad1 = new ImageIcon(""); // paddle 1
		imgpad2 = new ImageIcon(""); // paddle 2

		ball1 = new ImageIcon(""); //skin 1
		ball2 = new ImageIcon(""); //skin 2 
		ball3 = new ImageIcon(""); //skin 3
		
		
		// listen to key presses
		setFocusable(true);
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		
		// call step() 60 fps
		Timer timer = new Timer(800 / 60, this);
		timer.start();
	}

	/** Implement actionPerformed */
	public void actionPerformed(ActionEvent e) {
		step();
	}

	/** Repeated task */
	public void step() {

		if (playing) {

			/* Playing mode */

			// move player 1
			// Move up if after moving, paddle is not outside the screen
			if (upPressed && playerOneY - paddleSpeed >= 0) {
				playerOneY -= paddleSpeed;
			}
			// Move down if after moving paddle is not outside the screen
			if (downPressed && playerOneY + playerOneHeight + paddleSpeed <= getHeight()) {
				playerOneY += paddleSpeed;
			}

			// move player 2
			// Move up if after moving paddle is not outside the screen
			if (wPressed && playerTwoY - paddleSpeed >= 0) {
				playerTwoY -= paddleSpeed;
			}
			// Move down if after moving paddle is not outside the screen
			if (sPressed && playerTwoY + playerTwoHeight + paddleSpeed <= getHeight()) {
				playerTwoY += paddleSpeed;
			}

			/*
			 * where will the ball be after it moves? calculate 4 corners: Left,
			 * Right, Top, Bottom of the ball used to determine whether the ball
			 * was out yet
			 */
			int nextBallLeft = ballX + ballDeltaX;
			int nextBallRight = ballX + diameter + ballDeltaX;
			// FIXME Something not quite right here
			int nextBallTop = ballY + ballDeltaY;
			int nextBallBottom = ballY + diameter + ballDeltaY;

			// Player 1's paddle position
			int playerOneRight = playerOneX + playerOneWidth;
			int playerOneTop = playerOneY;
			int playerOneBottom = playerOneY + playerOneHeight;

			// Player 2's paddle position
			int playerTwoLeft = playerTwoX;
			int playerTwoTop = playerTwoY;
			int playerTwoBottom = playerTwoY + playerTwoHeight;

			// ball bounces off top and bottom of screen
			if (nextBallTop < 0 || nextBallBottom > getHeight()) {
				ballDeltaY *= -1;
			}

			// will the ball go off the left side?
			if (nextBallLeft < playerOneRight) {
				// is it going to miss the paddle?
				if (nextBallTop > playerOneBottom || nextBallBottom < playerOneTop) {

					playerTwoScore++;

					// Player 2 Win, restart the game
					if (playerTwoScore == 3) {
						playing = false;
						gameOver = true;
						playerOneScore = 0;
						playerTwoScore = 0;
					}
					ballX = 240; // qua bong di chuyen tu vi tri trung tam khi
									// bat dau game
					ballY = 240; // ...
					ballmove1 = true;
					ballmove2 = false;
				} else {
					// If the ball hitting the paddle, it will bounce back
					ballDeltaX *= -1; // bong cham vao thanh chan cua player 1
										// se bat lai
				}
			}

			// will the ball go off the right side?
			if (nextBallRight > playerTwoLeft) {
				// is it going to miss the paddle?
				if (nextBallTop > playerTwoBottom || nextBallBottom < playerTwoTop) {

					playerOneScore++;

					// Player 1 Win, restart the game
					if (playerOneScore == 3) {
						playing = false;
						gameOver = true;
						playerOneScore = 0;
						playerTwoScore = 0;
					}
					ballX = 240; // qua bong di chuyen tu vi tri trung tam khi
									// bat dau game
					ballY = 240; // ...
					ballmove1 = false;
					ballmove2 = true;
				} else {

					// If the ball hitting the paddle, it will bounce back
					ballDeltaX *= -1; // bong cham vao thanh chan cua player 2
										// se bat lai
				}
			}

			// move the ball
			ballX += ballDeltaX;
			ballY += ballDeltaY;
		}

		// stuff has moved, tell this JPanel to repaint itself
		repaint();
	}

	/** Paint the game screen. */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (showTitleScreen) {

			/* Show welcome screen */

			// Draw game title and start message
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString("Pong Game", 130, 100);

			// FIXME Wellcome message below show smaller than game title
			g.drawString("Press 'P' to play.", 175, 400);
		} else if (playing) {
			/* Game is playing */

			// set the coordinate limit
			int playerOneRight = playerOneX + playerOneWidth;
			int playerTwoLeft = playerTwoX;

			// draw dashed line down center
			g.setColor(Color.GREEN);
			for (int lineY = 0; lineY < getHeight(); lineY += 50) {
				g.drawLine(250, lineY, 250, lineY + 25);
			}

			// draw "goal lines" on each side
			g.setColor(Color.YELLOW);// Fix 2 duong bien mau xam
			g.drawLine(playerOneRight, 0, playerOneRight, getHeight());
			g.drawLine(playerTwoLeft, 0, playerTwoLeft, getHeight());

			// draw the scores
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString(String.valueOf(playerOneScore), 100, 100); // Player 1
																	// score
			g.drawString(String.valueOf(playerTwoScore), 400, 100); // Player 2
																	// score

			// draw the ball
			g.setColor(Color.RED);
			g.fillOval(ballX, ballY, diameter, diameter);
			
			// draw the paddles
			g.fillRect(playerOneX, playerOneY, playerOneWidth, playerOneHeight);
			g.fillRect(playerTwoX, playerTwoY, playerTwoWidth, playerTwoHeight);
		} else if (gameOver) {

			/* Show End game screen with winner name and score */

			// Draw scores
			// TODO Set Blue color
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString(String.valueOf(playerOneScore), 100, 100);
			g.drawString(String.valueOf(playerTwoScore), 400, 100);

			// Draw the winner name
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			if (playerOneScore > playerTwoScore) {
				g.drawString("Player 1 Wins!", 165, 200);
			} else {
				g.drawString("Player 2 Wins!", 165, 200);
			}

			// Draw Restart message
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
			// TODO Draw a restart message
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (showTitleScreen) {
			if (e.getKeyCode() == KeyEvent.VK_P ) { 
				showTitleScreen = false;
				playing = true;
			}
		} else if (playing) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				wPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				sPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				upPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				downPressed = true;
			}
		} else if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
			gameOver = false;
			showTitleScreen = true;
			playerOneY = 250;
			playerTwoY = 250;
			ballX = 240; // qua bong di chuyen tu vi tri trung tam khi bat dau
							// game
			ballY = 240; // ...
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			wPressed = false;		
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			sPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			upPressed = false;		
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			downPressed = false; 		
		}
	}

}
