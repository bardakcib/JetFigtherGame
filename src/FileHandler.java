import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileHandler {
	Scanner myReader;
	public FileHandler() {
		myReader = null;
	}
	//open file
	public Scanner Open(String fileName) {
		try {
			File myObj = new File("resources\\database\\" + fileName + ".txt");
			myReader = new Scanner(myObj);
		} catch (FileNotFoundException e) {
			System.out.println("File Open Error : " + e.getMessage());
			e.printStackTrace();
		}
		return myReader;
	}
	//close file
	public void Close(Scanner myFile) {
		try {
			myFile.close();
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
