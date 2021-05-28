import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainMenu extends JFrame {
	private static int gameTypeSelection;
	private int screenWidth = 624;
	private int screenHeight = 600;
	private JPanel panel;
	private boolean isLoggedIn;
	private static JTextField username;
	private UserController user; // user operations
	private HighestScoresListener hsl; //
	private JPasswordField password, password2; // textfield degil passfield cünkü passfield * koyduruyor.
	private JButton registerButton, loginButton;
	private String defaultFont = "Comic Sans MS";
	private JLabel background, author, userDetail;
	private JLabel usernameLabel, passwordLabel, passwordLabel2;
	private JButton quitButton, playTheGameButton, highest10ResultsButton;

	// jframe extend ettim her seferinde yeniden tanýtmamak için.
	public MainMenu() {
		isLoggedIn = false;
		user = new UserController();
		hsl = new HighestScoresListener();

		InitializeMenuGUI();
	}

	private void InitializeMenuGUI() {
		setUndecorated(true); // windows barý yok etmek için	
		setSize(screenWidth, screenHeight);
		setLocationRelativeTo(null); // center screen

		background = new JLabel();
		background.setName("menuimage");
		background.setIcon(new ImageIcon("resources\\imageFiles\\menu.jpg"));
		Dimension backgroundsize = background.getPreferredSize(); // örnej üzerinden gititm jpg frame'e otursun diye
		background.setBounds(0, 0, backgroundsize.width, backgroundsize.height);

		panel = new JPanel();
		panel.setLayout(null);

		quitButton = new JButton("EXIT");
		quitButton.setLocation(510, 15);
		quitButton.setSize(100, 25);
		quitButton.setFont(new Font(defaultFont, Font.BOLD, 18));
		quitButton.addActionListener(ExitGame());

		registerButton = new JButton("Register");
		registerButton.setLocation(410, 120);
		registerButton.setSize(140, 50);
		registerButton.setFont(new Font(defaultFont, Font.BOLD, 18));
		registerButton.addActionListener(LoginOrRegisterListener("Register"));

		loginButton = new JButton("Login");
		loginButton.setLocation(410, 220);
		loginButton.setSize(140, 50);
		loginButton.setFont(new Font(defaultFont, Font.BOLD, 18));
		loginButton.addActionListener(LoginOrRegisterListener("Login"));

		playTheGameButton = new JButton("Start Game");
		playTheGameButton.setLocation(410, 320);
		playTheGameButton.setSize(140, 50);
		playTheGameButton.setFont(new Font(defaultFont, Font.BOLD, 18));
		playTheGameButton.addActionListener(StartGame());

		highest10ResultsButton = new JButton("Score Table");
		highest10ResultsButton.setLocation(410, 420);
		highest10ResultsButton.setSize(140, 50);
		highest10ResultsButton.setFont(new Font(defaultFont, Font.BOLD, 18));
		highest10ResultsButton.addActionListener(hsl.HighScoresListener());

		author = new JLabel("Created by Bedirhan Bardakci ©");
		author.setName("authorLabel");
		author.setLocation(380, 580);
		author.setSize(250, 25);
		author.setFont(new Font(defaultFont, Font.BOLD, 15));

		userDetail = new JLabel();
		userDetail.setName("userDetailLabel"); // welcome
		userDetail.setLocation(420, 100);
		userDetail.setSize(250, 100);
		userDetail.setFont(new Font(defaultFont, Font.BOLD, 22));
		userDetail.setForeground(Color.white);
		userDetail.setVisible(false);

		background.add(userDetail);
		background.add(author);
		background.add(registerButton);
		background.add(loginButton);
		background.add(quitButton);
		background.add(playTheGameButton);
		background.add(highest10ResultsButton);

		panel.add(background);
		add(panel);
		
		setVisible(true);
		validate(); // add iþlemlerinden sonra validate yapýldý
	}

	private void ShowLoginRegisterPopUp(String type) {
		Object[] buttonNames = { type, "Cancel" }; // type login mi register mý oldugunu tutuyor. ayný zamanda butonun adý

		int result = JOptionPane.showOptionDialog(this, LoginRegisterPopUpPanel(type), type, JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, buttonNames, null); // ????

		String actionResultMsg = "Wrong User Name or Password";

		if (result == JOptionPane.YES_OPTION)  // Login / Register
		{
			if (type.equals("Login")) {
				if (user.IsSuccessfulLogin(username.getText(), String.valueOf(password.getPassword()))) {
					InitializeSuccessfulLogin(username.getText());
				}
			}

			if (type.equals("Register")) {
				// actionResultMsg Insert()'den alýyor
				actionResultMsg = user.Insert(username.getText(), String.valueOf(password.getPassword()),
						String.valueOf(password2.getPassword()));
				if (actionResultMsg.equals("OK")) {
					InitializeSuccessfulLogin(username.getText());
				}
			}

			if (!isLoggedIn) {
				JOptionPane.showConfirmDialog(null, actionResultMsg, type + " Failed !!!", JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		{
			// result == JOptionPane.CANCEL_OPTION
		}
	}

	//creating a panel to insert our pop up which created by JOptionPane to show register / login popup
	private JPanel LoginRegisterPopUpPanel(String type) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		usernameLabel = new JLabel("              Username : ");
		passwordLabel = new JLabel("              Password : ");
		passwordLabel2 = new JLabel("Retype Password : ");
		username = new JTextField();
		username.setPreferredSize(new Dimension(100, 25));

		password = new JPasswordField();
		password.setPreferredSize(new Dimension(100, 25));

		panel.add(usernameLabel);
		panel.add(username);
		panel.add(passwordLabel);
		panel.add(password);

		if (type.equals("Register")) {
			password2 = new JPasswordField();
			password2.setPreferredSize(new Dimension(100, 25));
			panel.add(passwordLabel2);
			// retype password
			panel.add(password2);
		}

		return panel;
	}

	private ActionListener LoginOrRegisterListener(String type) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowLoginRegisterPopUp(type);
			}
		};
	}

	private ActionListener StartGame() {
		Object[] buttonNames = { "Start Mini Game", "Start Mid Game", "Start Long Game", "Return To Menu" };
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isLoggedIn) {
					// optionpane kullandým cünkü kullanýcý sececegi 4 tane buton ekledim
					gameTypeSelection = JOptionPane.showOptionDialog(panel,
							"You can use WASD or Arrows to Move Jet Fighter"
									+ "\n\nPlease Hold 'Space' Key to Attack Continuously." + "\n\nGood Luck!\n\n",
							"INFO", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttonNames, null);
					// Start Mini Game
					if (gameTypeSelection == JOptionPane.YES_OPTION)
						gameTypeSelection = 0;
					// Start Mid Game
					else if (gameTypeSelection == JOptionPane.NO_OPTION)
						gameTypeSelection = 1;
					// Start Long Game
					else if (gameTypeSelection == JOptionPane.CANCEL_OPTION)
						gameTypeSelection = 2;
					// Return To Menu
					else
						return;

					RunGame();

				} else {
					ShowLoginRegisterPopUp("Login");
				}
			}
		};
	}

	private ActionListener ExitGame() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(panel, "Are you sure you want to quit the game?", "Quit Game?",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					dispose(); // extend ettigimiz frame'i dispose ettik.
					System.exit(0);
				}
			}
		};
	}

	private void InitializeSuccessfulLogin(String username) {
		isLoggedIn = true; // artýk start the game diyebilir
		loginButton.setVisible(false); // login butonu ekrandan gizlenir
		registerButton.setVisible(false); // register butonu ekrandan gizlenir
		background.validate();
		playTheGameButton.setLocation(410, 220);
		highest10ResultsButton.setLocation(410, 320);
		userDetail.setText("<html>Welcome<br/>" + username.toLowerCase() + "</html>"); // \n olmadý. internetten buldum.
																						// 2 label yaratmak yerine tek
																						// seferde 2 satýr yaratmýþ
																						// olduk
		userDetail.setVisible(true);
	}

	public static void RunGame() {
		JetFighterGame jf = new JetFighterGame(gameTypeSelection, username.getText());
		Thread thread = new Thread(jf);
		jf.Play();
		thread.start();
	}
}
