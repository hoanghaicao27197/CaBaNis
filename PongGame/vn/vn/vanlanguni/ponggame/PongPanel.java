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
 * @author CaBaNis - Team 2.5
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
				Sound.play(""); // SOUND HERE //
				ballDeltaY *= -1;
			}

			// will the ball go off the left side?
			if (nextBallLeft < playerOneRight) {
				// is it going to miss the paddle?
				if (nextBallTop > playerOneBottom || nextBallBottom < playerOneTop) {
					Sound.play("");   // SOUND HERE //
					playerTwoScore++;

					// Player 2 Win, restart the game
					if (playerTwoScore == 3) {
						playing = false;
						gameOver = true;
						Sound.play("");  // SOUND HERE //
						playerOneScore = 0;
						playerTwoScore = 0;
					}
					ballX = 250; // qua bong di chuyen tu vi tri trung tam khi
									// bat dau game
					ballY = 250; // ...
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
					Sound.play("");    // SOUND HERE //
					playerOneScore++;

					// Player 1 Win, restart the game
					if (playerOneScore == 3) {
						playing = false;
						gameOver = true;
						playerOneScore = 0;
						playerTwoScore = 0;
						Sound.play(""); // SOUND HERE //
					}
					ballX = 250; // qua bong di chuyen tu vi tri trung tam khi
									// bat dau game
					ballY = 250; // ...
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
		imgbtnPlay = new ImageIcon(nameP);
		imgbtnSetting = new ImageIcon(nameS);
		imgbgP = new ImageIcon("");
		
		if (showTitleScreen) {
			
			Sound.play("") // Welcome screen sound
			
			/* Show welcome screen */
			Image imgbpong = new ImageIcon("").getImage(); // WELCOME screen image
			
			// Draw game title and start message
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			//g.drawString("Pong Game", 130, 100);
			g.drawImage( imgbpong,0,0,500,500,null);
			g.drawImage(imgbtnPlay.getImage(),pPlay.x - rPlay, pPlay.y - rPlay, rPlay * 2, rPlay * 2,null);
			g.drawImage(imgbtnSetting.getImage(),pSetting.x - rSetting, pSetting.y - rSetting, rSetting * 2, rSetting * 2,null	);
			if (intersec4) {
				g.setColor(Color.white);
				g.setFont(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 20));
				g.drawString("Setting", pSetting.x +30 , pSetting.y +10);
			}
			
			// FIXME Wellcome message below show smaller than game title
			//g.drawString("Press 'P' to play.", 175, 400);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			//g.drawString("Press 'S' to Setting.", 135, 400);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			
			
		} else if (playing) {
			
			/* Game is playing */
			Image background = new ImageIcon("").getImage(); // playing background //
			g.drawImage(background, 0, 0, 500, 500, null);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.setColor(Color.blue);
			g.drawString(namePlayer1, 30, 50);
			g.setColor(Color.red);
			g.drawString(namePlayer2, 320, 50);
			
			// set the coordinate limit
			int playerOneRight = playerOneX + playerOneWidth;
			int playerTwoLeft = playerTwoX;

			/** 
			 * 
			// draw dashed line down center
			g.setColor(Color.GREEN);
			for (int lineY = 0; lineY < getHeight(); lineY += 50) {
				g.drawLine(250, lineY, 250, lineY + 25);
			}

			// draw "goal lines" on each side
			g.setColor(Color.YELLOW);// Fix 2 duong bien mau xam
			g.drawLine(playerOneRight, 0, playerOneRight, getHeight());
			g.drawLine(playerTwoLeft, 0, playerTwoLeft, getHeight()); 
			 *
			**/

			// draw the scores
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString(String.valueOf(playerOneScore), 215, 270); // Player 1
																	// score
			g.drawString(String.valueOf(playerTwoScore), 265, 270); // Player 2
																	// score

			// draw the ball
			g.setColor(Color.RED);
			//g.fillOval(ballX, ballY, diameter, diameter);
			g.drawImage(ball1.getImage(),ballX, ballY, diameter, diameter,null);
			
			// draw the paddles
			//g.fillRect(playerOneX, playerOneY, playerOneWidth, playerOneHeight);
			//g.fillRect(playerTwoX, playerTwoY, playerTwoWidth, playerTwoHeight);
			g.drawImage(imgpad1.getImage(), playerOneX, playerOneY, playerOneWidth, playerTwoHeight, null); //paddle 1
			g.drawImage(imgpad2.getImage(), playerTwoX, playerTwoY, playerTwoWidth, playerTwoHeight, null); //paddle 2
		} 
		if (setting){
			imgbtnBack = new ImageIcon(nameB);
			g.drawImage(imgbgP.getImage(),0,0,500,500,null); // press C to Menu
			g.drawImage(imgbtnBack.getImage(),pBack.x - rBack, pBack.y - rBack, rBack * 2, rBack * 2,null); // Game Tittle Image
			if (intersec2) {
				g.setColor(Color.white);
				g.setFont(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 20));
				g.drawString("Back", pSetting.x +20 , pSetting.y +5);
			}
			
		} else if (gameOver) {
			imgbtnMenu = new ImageIcon(""); //Menu Button Image
			imgbtnSa = new ImageIcon(""); // Restart Button Image
			/* Show End game screen with winner name and score */

			/** 
			Draw scores
			Draw the winner name
			Draw Restart message
			*/
			
			// TODO Set Blue color
			// TODO Draw a restart message
			g.drawImage(imgbgP.getImage(),0,0,500,500,null);
			g.setColor(Color.BLUE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString(namePlayer1, 30, 50);
			g.setColor(Color.RED);
			g.drawString(namePlayer2, 320, 50);
			g.setColor(Color.BLUE);
			g.drawString(String.valueOf(playerOneScore), 80, 100);
			g.setColor(Color.RED);
			g.drawString(String.valueOf(playerTwoScore), 380, 100);
			g.drawImage(imgbtnMenu.getImage(), pMenu.x - rMenu, pMenu.y - rMenu, rMenu * 2, rMenu * 2,null);
			g.drawImage(imgbtnSa.getImage(), pSa.x - rSa, pSa.y - rSa, rSa * 2, rSa * 2,null);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			if (playerOneScore > playerTwoScore) {
				g.setColor(Color.BLUE);
				g.drawString ("The Winner is :"+namePlayer1 , 15, 200);
			} else {
				g.setColor(Color.RED);
				g.drawString("The Winner is :"+namePlayer2, 15, 200);
			}
			if (intersec) {
				g.setColor(Color.blue);
				g.setFont(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 20));
				g.drawString("Back to Menu", pMenu.x +35 , pMenu.y +25);
				
			}
			if (intersec1) {
				g.setColor(Color.blue);
				g.setFont(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 20));
				g.drawString("Restart the game", pSa.x +35 , pSa.y +25);
			}
			
			/**
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			if (playerOneScore > playerTwoScore) {
				g.drawString("Player 1 Wins!", 165, 200);
			} else {
				g.drawString("Player 2 Wins!", 165, 200);
			}
					
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
			 * 
			 */
		}
	}

public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		
		if (playing) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				wPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				sPressed = true;
			}else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE){
				isPaused = true ;
			}
		} else if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
			gameOver = false;
			showTitleScreen = true;
			playerOneY = 250;
			playerTwoY = 250;
			ballX = 250;
			ballY = 250;
			playerOneScore = 0;
			playerTwoScore = 0;
		}else if (setting && e.getKeyCode() == KeyEvent.VK_N){
			SecondWindow w = new SecondWindow();
			w.setLocationRelativeTo(PongPanel.this);
			w.setVisible(true);
			Settings s = w.getSetings();
			System.out.println("After open window");
			
			// Stop and wait for user input
			
			if (w.dialogResult == MyDialogResult.YES) {
				System.out.printf("User settings: \n Username1: %s \n Username2: %s",
						s.getUserName1(), s.getUserName2());
				namePlayer1 = s.getUserName1();
				namePlayer2 =s.getUserName2();
			} else {
				System.out.println("User chose to cancel");
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			wPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			sPressed = false;
		}
	}
	public void pauseGame (){
	
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (getPointDistance(arg0.getPoint(),pPlay) <= rPlay) {
			//intersec = true;
			nameP = "";
		} else {
			//intersec = false;
			nameP = "";
		}
		if (getPointDistance(arg0.getPoint(), pSetting)<=rSetting){
			intersec4 = true ;
		}
		else {
			intersec4 = false;
		}
		if (getPointDistance(arg0.getPoint(), pBack)<=rBack){
			intersec2 =true ;
			
		}
		else {
			intersec2 = false;
			}
		if (getPointDistance(arg0.getPoint(), pMenu)<=rMenu){
			intersec =true ;
			
		}
		else {
			intersec = false;
			}
		if (getPointDistance(arg0.getPoint(), pSa)<=rSa){
			intersec1 =true ;
			
		}
		else {
			intersec1 = false;
			}
	}
		public double getPointDistance(Point p1, Point p2) {
			return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			
		}
		@Override
		public void mousePressed(MouseEvent arg0) {
			
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (showTitleScreen){
			if (getPointDistance(e.getPoint(),pPlay) <= rPlay){
				Sound.play(""); //Click Sound
				showTitleScreen = false;
				gameOver = false ;
				setting = false;
				playing = true;
			}
			}
			if (showTitleScreen){
				if (getPointDistance(e.getPoint(), pSetting)<=rSetting){
				
				Sound.play(""); //Click Sound
				setting = true;
				showTitleScreen = false;
				playing = false ;
				gameOver = false ;
				}
			}
			
			else if (setting){
				if (getPointDistance(e.getPoint(), pBack)<=rBack){
				
				Sound.play(""); //Click Sound
				showTitleScreen = true;
				playing = false ;
				setting = false;
				gameOver = false;
			}
		}
			if (gameOver){
				if ( getPointDistance(e.getPoint(), pMenu)<=rMenu){
					Sound.play(""); //Click Sound
					gameOver = false;
					showTitleScreen = true;
					playerOneY = 250;
					playerTwoY = 250;
					ballX = 250;
					ballY = 250;
					playerOneScore = 0;
					playerTwoScore = 0;
					
				}
				else if (getPointDistance(e.getPoint(), pSa)<=rSa){
					gameOver = false;
					playerOneY = 250;
					playerTwoY = 250;
					ballX = 250;
					ballY = 250;
					playerOneScore = 0;
					playerTwoScore = 0;
					playing = true;
					
				}
			}
			
	}	
	
}