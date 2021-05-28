import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ScoreController {

	private Scanner myReader;
	private FileHandler myTextFile;
	List<ArrayList<String>> namesAndScores;

	public ScoreController() {
		myTextFile = new FileHandler();
		namesAndScores = new ArrayList<ArrayList<String>>();
	}

	public List<ArrayList<String>> GetScoreData() {
		try {
			myReader = myTextFile.Open("scores"); //open scores.txt
			while (myReader.hasNextLine()) { 
				namesAndScores.add(new ArrayList<String>(Arrays.asList(myReader.nextLine(), myReader.nextLine()))); 
			}
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		myTextFile.Close(myReader); // close file
		
		// sort the list lower to higher.
		Collections.sort(namesAndScores, new Comparator<ArrayList<String>>() {
			@Override
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				return Integer.compare(Integer.parseInt(o1.get(0)), Integer.parseInt(o2.get(0)));
			}
		});

		return namesAndScores;
	}
	// save score data to scores.txt
	public void InsertScore(String username, int score) {
		try {
			Writer output = new BufferedWriter(new FileWriter("resources\\database\\scores.txt", true));
			output.append(String.valueOf(score));
			output.append('\n');
			output.append(username.toLowerCase());
			output.append('\n');
			output.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
