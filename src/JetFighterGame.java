import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

public class JetFighterGame extends JFrame implements Runnable {
	private JPanel panel;
	private JFrame frame;
	private Sounds mySound;
	private Clip gameClip; // to stop background game music when game ends or user exits to main menu.
	private ScoreController sc;
	private Set<Integer> pressedKeys;
	private HighestScoresListener hsl;
	private KeyListener myKeyListener;
	private Image healthImage, jetFighterImage, collideImage, ripImage, successImage, hitImage, exitImage, enemyImage,
			enemyBulletImage, bulletImage;
	private boolean spacePressed = false, isThreadRunning = true;
	private int enemyPeriodInMills, bulletPeriodInMills, healthLimit, healthBarDecreaseSizeX, randomX, mainJetSpeed,
			enemySpeed, bulletSpeed, enemyBulletSpeed, enemyBulletSize, gameDuration, hitPoint, planeSize, screenWidth,
			screenHeight, backgroundPositionY, jetFighterBulletSize, Menu_ObjHeight, upLimit, score, downLimit,
			removeKaboomDustPeriodInMills, counter, mainjetCurrentX, mainjetCurrentY, healthBarSizeX;
	private String userName, lastHitEnemy, defaultFont, backgroundImagePath;
	private JLabel collideLabel, exitLabel, jetFighter, healthBar, background, scoreLabel, scoreIntLabel, enemyHitLabel;

	public JetFighterGame(int duration, String name) {
		userName = name;
		gameDuration = duration;
		InitializeFixedParameters();
	}

	public void Play() {
		InitializeParameters();
		InitializeGuiElements();
	}

	private void InitializeImages() {
		collideImage = new ImageIcon("resources\\imageFiles\\collide.png").getImage();
		collideImage = collideImage.getScaledInstance(96, 96, java.awt.Image.SCALE_SMOOTH);

		ripImage = new ImageIcon("resources\\imageFiles\\rip.png").getImage();
		ripImage = ripImage.getScaledInstance(planeSize * 2, planeSize * 2, java.awt.Image.SCALE_SMOOTH);

		successImage = new ImageIcon("resources\\imageFiles\\done.png").getImage();
		successImage = successImage.getScaledInstance(planeSize * 4, planeSize * 4, java.awt.Image.SCALE_SMOOTH);

		jetFighterImage = new ImageIcon("resources\\imageFiles\\jetfighter.png").getImage();
		jetFighterImage = jetFighterImage.getScaledInstance(planeSize, planeSize, java.awt.Image.SCALE_SMOOTH);

		exitImage = new ImageIcon("resources\\imageFiles\\exit.png").getImage();
		exitImage = exitImage.getScaledInstance(140, 45, java.awt.Image.SCALE_SMOOTH);

		hitImage = new ImageIcon("resources\\imageFiles\\enemyHit.png").getImage();
		hitImage = hitImage.getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH);

		bulletImage = new ImageIcon("resources\\imageFiles\\bullet.png").getImage();
		bulletImage = bulletImage.getScaledInstance(jetFighterBulletSize, jetFighterBulletSize * 2,
				java.awt.Image.SCALE_SMOOTH);

		enemyBulletImage = new ImageIcon("resources\\imageFiles\\enemyBullet.png").getImage();
		enemyBulletImage = enemyBulletImage.getScaledInstance(enemyBulletSize, enemyBulletSize * 2,
				java.awt.Image.SCALE_SMOOTH);

		enemyImage = new ImageIcon("resources\\imageFiles\\enemy.png").getImage();
		enemyImage = enemyImage.getScaledInstance(planeSize, planeSize, java.awt.Image.SCALE_SMOOTH);

		healthImage = new ImageIcon("resources\\imageFiles\\healthBar.png").getImage();
		healthImage = healthImage.getScaledInstance(healthBarSizeX, 48, java.awt.Image.SCALE_SMOOTH);
	}

	private void InitializeFixedParameters() {
		sc = new ScoreController();
		hsl = new HighestScoresListener();
		mySound = new Sounds();

		defaultFont = "Comic Sans MS";
		planeSize = 64;
		screenWidth = 624;
		screenHeight = 600;
		backgroundPositionY = 0;
		Menu_ObjHeight = 65; // if windows default bar is active then 100 else 65
		randomX = 0; // enemy positions
		mainJetSpeed = 20;
		enemySpeed = 25;
		bulletSpeed = 80;
		enemyBulletSpeed = 45;
		downLimit = 0; // background
		hitPoint = 100;
		upLimit = 0; // background move speed?
		enemyBulletSize = 33;
		jetFighterBulletSize = 11;
		healthBarSizeX = 180;
		enemyPeriodInMills = 500; // yeni düþman yaratma sýklýðý
		bulletPeriodInMills = 225; // space basýlý olursa yeni bullet yaratma sýklýðý
		removeKaboomDustPeriodInMills = 2500;
		InitializeImages();

		if (gameDuration == 0)
			backgroundImagePath = "resources\\imageFiles\\background.jpg";
		if (gameDuration == 1) // mid
			backgroundImagePath = "resources\\imageFiles\\backgroundMiddle.jpg";
		if (gameDuration == 2) // long
			backgroundImagePath = "resources\\imageFiles\\backgroundLongGame.jpg";
	}

	private void InitializeParameters() {
		isThreadRunning = true;

		gameClip = mySound.GameBackgroundSound();
		gameClip.start();

		healthLimit = 6;
		healthBarDecreaseSizeX = (healthBarSizeX - 43) / healthLimit;

		score = 0;
		counter = 1;
		mainjetCurrentX = 250;
		mainjetCurrentY = 0;
		pressedKeys = new HashSet<>(); // çapraz gitmesi için
	}

	private void InitializeGuiElements() {
		setUndecorated(true); // Remove title bar
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createLineBorder(Color.red));

		background = new JLabel();
		background.setName("backgroundimage");
		background.setIcon(new ImageIcon(backgroundImagePath));
		Dimension backgroundsize = background.getPreferredSize();

		mainjetCurrentY = (int) backgroundsize.getHeight() - Menu_ObjHeight;
		downLimit = (int) backgroundsize.getHeight() - Menu_ObjHeight;
		backgroundPositionY = -1 * ((int) backgroundsize.getHeight() - screenHeight);

		upLimit = downLimit - screenHeight + Menu_ObjHeight;

		background.setBounds(0, backgroundPositionY, backgroundsize.width, backgroundsize.height); // set backdround

		exitLabel = new JLabel();
		exitLabel.setIcon(new ImageIcon(exitImage));
		exitLabel.setName("exitButton");
		exitLabel.setSize(140, 45);
		exitLabel.setFont(new Font(defaultFont, Font.BOLD, 18));
		exitLabel.addMouseListener(ExitGame());
		background.add(exitLabel);

		collideLabel = new JLabel();
		collideLabel.setIcon(new ImageIcon(collideImage));
		collideLabel.setName("expCollision");
		collideLabel.setSize(96, 96);
		collideLabel.setVisible(false);
		background.add(collideLabel);

		enemyHitLabel = new JLabel();
		enemyHitLabel.setIcon(new ImageIcon(hitImage));
		enemyHitLabel.setName("expHitDown");
		enemyHitLabel.setSize(64, 64);
		enemyHitLabel.setVisible(false);
		background.add(enemyHitLabel);

		InitializeJetFighter();
		InitializeScoreBar();
		InitializeHealthBar();

		panel.add(background);
		add(panel);
		setSize(screenWidth, screenHeight);
		setLocationRelativeTo(null); // center screen
		myKeyListener = MyMultiKeyListener();
		addKeyListener(myKeyListener);
		validate();
	}

	private void InitializeJetFighter() {
		jetFighter = new JLabel();
		jetFighter.setName("mainJet");
		jetFighter.setIcon(new ImageIcon(jetFighterImage));
		Dimension size = jetFighter.getPreferredSize();
		jetFighter.setBounds(mainjetCurrentX, mainjetCurrentY, size.width, size.height); // set player jet position
		background.add(jetFighter);
	}

	private void InitializeScoreBar() {
		scoreLabel = new JLabel();
		scoreLabel.setName("score");
		scoreLabel.setText("SCORE : ");
		scoreLabel.setFont(new Font(defaultFont, Font.BOLD, 24));
		scoreLabel.setForeground(Color.DARK_GRAY);
		scoreLabel.setSize(111, 30);
		scoreLabel.setBackground(Color.LIGHT_GRAY);
		scoreLabel.setOpaque(true);
		background.add(scoreLabel);

		scoreIntLabel = new JLabel();
		scoreIntLabel.setName("scoreint");
		scoreIntLabel.setText(String.valueOf(score));
		scoreIntLabel.setFont(new Font(defaultFont, Font.BOLD, 24));
		scoreIntLabel.setForeground(Color.DARK_GRAY);
		scoreIntLabel.setSize(150, 30);
		scoreIntLabel.setBackground(Color.LIGHT_GRAY);
		scoreIntLabel.setOpaque(true);
		background.add(scoreIntLabel);
	}

	private void InitializeHealthBar() {
		healthBar = new JLabel();
		healthBar.setName("expHealthBar");
		healthBar.setIcon(new ImageIcon(healthImage));
		healthBar.setSize(healthBarSizeX, 48);
		background.add(healthBar);
	}

	private void CreateNewEnemy() {
		if (isThreadRunning) {
			randomX = GetRandomNumberUsingNextInt(randomX, 0, 550);
			background.add(AddEnemy(randomX));
			panel.validate();
		}
	}

	private JLabel AddEnemy(int newx) {
		counter++;
		JLabel jetFighterEnemy = new JLabel();
		jetFighterEnemy.setName("enemy" + counter);
		jetFighterEnemy.setIcon(new ImageIcon(enemyImage));
		Dimension sizeEnemy = jetFighterEnemy.getPreferredSize();
		jetFighterEnemy.setBounds(newx, upLimit, sizeEnemy.width, sizeEnemy.height);

		counter++;
		JLabel enemyBullet = new JLabel();
		enemyBullet.setName("enemyHostileFire" + counter);
		enemyBullet.setIcon(new ImageIcon(enemyBulletImage));
		enemyBullet.setSize(enemyBulletSize, enemyBulletSize * 2);
		enemyBullet.setLocation(newx + (planeSize / 2) - enemyBulletSize / 2, upLimit + planeSize);
		background.add(enemyBullet);

		return jetFighterEnemy;
	}

	private void MoveBackground() {
		upLimit = downLimit - screenHeight + Menu_ObjHeight;

		if (upLimit <= 200) {
			GameOver(true);
		} else {
			backgroundPositionY = backgroundPositionY + 1;
			downLimit = downLimit - 1;

			healthBar.setLocation(0, upLimit);
			exitLabel.setLocation(500, upLimit);

			scoreLabel.setLocation(200, upLimit + 5);
			scoreIntLabel.setLocation(310, upLimit + 5);

			if (mainjetCurrentY >= downLimit) {
				mainjetCurrentY = downLimit;
				jetFighter.setLocation(jetFighter.getX(), downLimit);
			}

			if (mainjetCurrentY <= upLimit) {
				mainjetCurrentY = upLimit;
				jetFighter.setLocation(jetFighter.getX(), upLimit);
			}

			background.setLocation(background.getX(), backgroundPositionY);
		}
	}

	private void Addbullet() {
		// left side bullet
		counter++;
		JLabel bullet = new JLabel();
		bullet.setName("bullet" + counter);
		bullet.setIcon(new ImageIcon(bulletImage));
		bullet.setSize(jetFighterBulletSize, jetFighterBulletSize * 2);
		bullet.setLocation(mainjetCurrentX, mainjetCurrentY + (jetFighterBulletSize / 2));
		background.add(bullet);

		// right side bullet
		counter++;
		JLabel bullet2 = new JLabel();
		bullet2.setName("bullet" + counter);
		bullet2.setIcon(new ImageIcon(bulletImage));
		bullet2.setSize(jetFighterBulletSize, jetFighterBulletSize * 2);
		bullet2.setLocation(mainjetCurrentX + 50, mainjetCurrentY + (jetFighterBulletSize / 2));
		background.add(bullet2);

		mySound.BulletSound();
	}

	private void CompareLocations(Container parent) {
		Component[] all = parent.getComponents();

		for (Component enemy : all) {
			if (enemy.getName().contains("enemy")) {
				// when enemy got out from screen remove the related component
				if (enemy.getY() > downLimit+30) {
					parent.remove(enemy);
					parent.validate();
				} else if (Math.abs(enemy.getX() - mainjetCurrentX) < 49
						&& Math.abs(enemy.getY() - mainjetCurrentY) < 49) {
					System.out.println("Collide !!! JetFighter Position : " + mainjetCurrentX + " , " + mainjetCurrentY
							+ " ---- Enemy Position : " + enemy.getX() + " , " + enemy.getY());

					collideLabel.setVisible(true);
					collideLabel.setLocation(mainjetCurrentX, mainjetCurrentY);

					if (!enemy.getName().contains("HostileFire")) {
						score = score + hitPoint;
						scoreIntLabel.setText(String.valueOf(score));
					}

					mySound.Collision();
					healthLimit--;
					healthBarSizeX = healthBarSizeX - healthBarDecreaseSizeX;
					healthBar.setSize(healthBarSizeX, 48);

					parent.remove(enemy);
					parent.validate();

					if (healthLimit == 0)
						GameOver(false);

				} else {
					for (Component bullet : all) {
						if (bullet.getName().contains("bullet")) {
							if (bullet.getY() < (upLimit)) {
								parent.remove(bullet);
								parent.validate();
							} else if (Math.abs(bullet.getY() - enemy.getY()) < 49
									&& Math.abs(bullet.getX() - enemy.getX()) < 49) {

								if (lastHitEnemy != enemy.getName()) {
									System.out.println("Hit !!! " + bullet.getName() + "'s Y offset : " + bullet.getY()
											+ " , " + enemy.getName() + "'s Y offset : " + enemy.getY());

									if (!enemy.getName().contains("HostileFire")) {
										score = score + hitPoint;
										scoreIntLabel.setText(String.valueOf(score));
										lastHitEnemy = enemy.getName();
										mySound.EnemyDown();
										enemyHitLabel.setVisible(true);
										enemyHitLabel.setLocation(enemy.getX(), enemy.getY());
									}
								}

								parent.remove(bullet);
								parent.remove(enemy);
								parent.validate();
							}
						}
					}
				}
			}
		}
		parent.repaint();
		parent.validate();
	}

	private void UpdateEnemyLocations(Container parent) {
		Component[] all = parent.getComponents();

		for (Component c : all) {
			if (c.getName().contains("enemy") && isThreadRunning)
				c.setLocation(c.getX(), c.getY() + enemySpeed);
		}
	}

	private void UpdateBulletLocations(Container parent) {
		Component[] all = parent.getComponents();

		for (Component c : all) {
			if (c.getName().contains("bullet") && isThreadRunning)
				c.setLocation(c.getX(), c.getY() - bulletSpeed);

			if (c.getName().contains("HostileFire") && isThreadRunning)
				c.setLocation(c.getX(), c.getY() + enemyBulletSpeed);
		}
	}

	private KeyListener MyMultiKeyListener() {
		return new KeyListener() {
			@Override
			public synchronized void keyPressed(KeyEvent e) {
				pressedKeys.add(e.getKeyCode());

				mainjetCurrentX = jetFighter.getX();
				mainjetCurrentY = jetFighter.getY();

				if (!pressedKeys.isEmpty()) {
					for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
						switch (it.next()) {
						case KeyEvent.VK_SPACE:
							spacePressed = true;
							break;
						case KeyEvent.VK_W:
						case KeyEvent.VK_UP:
							if (mainjetCurrentY != upLimit)
								jetFighter.setLocation(jetFighter.getX(), jetFighter.getY() - mainJetSpeed);
							break;
						case KeyEvent.VK_A:
						case KeyEvent.VK_LEFT:
							if (mainjetCurrentX >= 0)
								jetFighter.setLocation(jetFighter.getX() - mainJetSpeed, jetFighter.getY());
							break;
						case KeyEvent.VK_S:
						case KeyEvent.VK_DOWN:
							if (mainjetCurrentY != downLimit)
								jetFighter.setLocation(jetFighter.getX(), jetFighter.getY() + mainJetSpeed);
							break;
						case KeyEvent.VK_D:
						case KeyEvent.VK_RIGHT:
							if (mainjetCurrentX != (screenWidth - 74))
								jetFighter.setLocation(jetFighter.getX() + mainJetSpeed, jetFighter.getY());
							break;
						}
					}
					mainjetCurrentX = jetFighter.getX();
					mainjetCurrentY = jetFighter.getY();
				}
			}

			@Override
			public synchronized void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 32) {
					spacePressed = false;
				}
				pressedKeys.remove(e.getKeyCode());

			}

			@Override
			public void keyTyped(KeyEvent e) {
				/* Not used */ }

		};
	}

	// to prevent new enemy created close to last created enemy.
	private int GetRandomNumberUsingNextInt(int lastGenerated, int min, int max) {
		Random random = new Random();
		int created = random.nextInt(max - min) + min;

		while (Math.abs(lastGenerated - created) < planeSize)
			created = random.nextInt(max - min) + min;

		return created;
	}

	private void GameOver(boolean success) {
		StopThread();

		if (!success) {
			mySound.GameOverFail();
			System.out.println("Game Over :(");
			jetFighter.setIcon(new ImageIcon(ripImage));
		} else {
			mySound.LevelComplete();
			System.out.println("Level Completed :)");
			jetFighter.setIcon(new ImageIcon(successImage));
		}

		jetFighter.setLocation(180, downLimit - 300);
		jetFighter.setSize(256, 256);

		scoreLabel.setLocation(185, downLimit - 500 + Menu_ObjHeight);
		scoreLabel.setText("Your Final Score");
		scoreLabel.setSize(300, 25);
		scoreLabel.setBackground(Color.LIGHT_GRAY);
		scoreLabel.setOpaque(true);
		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

		scoreIntLabel.setLocation(185, downLimit - 475 + Menu_ObjHeight);
		scoreIntLabel.setText(String.valueOf(score));
		scoreIntLabel.setSize(300, 25);
		scoreIntLabel.setBackground(Color.LIGHT_GRAY);
		scoreIntLabel.setOpaque(true);
		scoreIntLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// removeKeyListener from JFrame as no more need to move jet figther.
		removeKeyListener(myKeyListener);

		JButton playAgainButton = new JButton("Play Again");
		playAgainButton.setLocation(185, downLimit - 450 + Menu_ObjHeight);
		playAgainButton.setSize(140, 50);
		playAgainButton.setName("playAgainButton");
		playAgainButton.setFont(new Font(defaultFont, Font.BOLD, 18));
		playAgainButton.addActionListener(PlayAgainListener());

		JButton highest10ResultsButton = new JButton("Score Table");
		highest10ResultsButton.setLocation(345, downLimit - 450 + Menu_ObjHeight);
		highest10ResultsButton.setSize(140, 50);
		highest10ResultsButton.setName("highestResultsButton");
		highest10ResultsButton.setFont(new Font(defaultFont, Font.BOLD, 18));
		highest10ResultsButton.addActionListener(hsl.HighScoresListener());

		sc.InsertScore(userName, score);
		background.add(playAgainButton);
		background.add(highest10ResultsButton);
		ClearComponents();
		background.repaint();
		background.validate();
		// buttons click events
		// Save Result for the user
	}

	private ActionListener PlayAgainListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				MainMenu.RunGame();
			}
		};
	}

	private MouseAdapter ExitGame() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit the game?", "Quit Game?",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					StopThread();
					dispose();
				} else { // option "no" selected
					// do nothing, return game menu
				}
			}
		};
	}

	@Override
	public void run() {
		isThreadRunning = true;

		long current = System.currentTimeMillis();
		long end = current + enemyPeriodInMills;

		long currentForBullet = System.currentTimeMillis();
		long endForBullet = current + bulletPeriodInMills;

		long currentForEnemyHit = System.currentTimeMillis();
		long endForEnemyHit = currentForEnemyHit + removeKaboomDustPeriodInMills;

		while (isThreadRunning) {
			try {
				Thread.sleep(100);
				if (isThreadRunning) {
					MoveBackground();
					UpdateBulletLocations(background);
					UpdateEnemyLocations(background);
					CompareLocations(background);

					// Remova kaboom and dust images in Every 2500 milliseconds
					if (System.currentTimeMillis() > endForEnemyHit) {
						currentForEnemyHit = System.currentTimeMillis();
						endForEnemyHit = currentForEnemyHit + removeKaboomDustPeriodInMills;

						if (enemyHitLabel.isVisible())
							enemyHitLabel.setVisible(false);

						if (collideLabel.isVisible())
							collideLabel.setVisible(false);
					}
					// Create New Enemy Every 500 milliseconds
					if (System.currentTimeMillis() > end) {
						CreateNewEnemy();
						current = System.currentTimeMillis();
						end = current + enemyPeriodInMills;
					}
					// if space hold then auto attack every 225 milliseconds
					if (System.currentTimeMillis() > endForBullet && spacePressed == true) {
						Addbullet();
						currentForBullet = System.currentTimeMillis();
						endForBullet = currentForBullet + bulletPeriodInMills;
					}
				}
			} catch (Exception e) {
				Thread.currentThread().interrupt();
				System.out.println("Thread was interrupted, Failed to complete operation");
			}
		}
		System.out.println("Thread Dead");
	}

	private void StopThread() {
		gameClip.close();
		isThreadRunning = false;
	}

	private void ClearComponents() {
		Component[] all = background.getComponents();

		try {
			for (Component c : all) {
				if (c.getName() != null) {
					if (c.getName().contains("enemy") || c.getName().contains("bullet")
							|| c.getName().contains("exp")) {
						background.remove(c);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		System.out.println("Components Removed");
	}
}