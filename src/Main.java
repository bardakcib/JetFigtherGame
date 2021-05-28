import java.io.File;

public class Main {
	public static void main(String[] args) {
		//TestFileLocations();
		new MainMenu();
	}

	static void TestFileLocations() {
		String mainJetImgPath = "";
		File f = new File("resources\\imageFiles\\jetfighter.png");

		if (f.exists() && !f.isDirectory()) {
			System.out.println("File Found");
			mainJetImgPath = f.getAbsolutePath();
			System.out.println(mainJetImgPath);
		}
	}
}
