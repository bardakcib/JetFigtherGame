import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class UserController {
	private Scanner myReader;
	private FileHandler myTextFile;

	public UserController() {
		myTextFile = new FileHandler();
	}
	//check if entered username is already exists.
	public boolean isUserExists(String username) { 
		try {
			myReader = myTextFile.Open("user");
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if (username.toLowerCase().equals(data)) {
					System.out.println("User Added Before");
					myTextFile.Close(myReader);
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		myTextFile.Close(myReader);
		return false;
	}
	//check if entered username and password are matching
	public boolean IsSuccessfulLogin(String username, String password) { 
		try {
			myReader = myTextFile.Open("user");
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if (data.equals(username.toLowerCase())) {
					String pass = myReader.nextLine();
					if (pass.equals(password)) {

						myTextFile.Close(myReader);
						return true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		myTextFile.Close(myReader);
		return false;
	}
	//register 
	public String Insert(String username, String password, String password2) {  
		try {
			if (password.length() < 8 || password.length() > 12)
				return "Password Length Should Be Between 8 and 12";

			if (username.length() < 3 || username.length() > 12)
				return "User Name Length Should Be Between 3 and 12";

			if (!password.equals(password2))
				return "Passwords Not Match!!";
			//if entered username is not created before -> OK!
			if (!isUserExists(username)) { 
				Writer output = new BufferedWriter(new FileWriter("resources\\database\\user.txt", true));
				output.append(username.toLowerCase());
				output.append('\n');
				output.append(password);
				output.append('\n');
				output.close();
				return "OK";
			}
			else
			{
				return "User Added Before";
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return "ERROR";
	}
}
