import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class HighestScoresListener {
	ScoreController scores;

	public HighestScoresListener() {
		scores = new ScoreController();
	}

	public ActionListener HighScoresListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon("resources\\imageFiles\\leaderboard.png"); //load leaderboard png

				JOptionPane.showConfirmDialog(null, HighScoresPopUpPanel(), "High Scores TOP 10",  //show top 10 high score
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
			}
		};
	}

	//creating a panel to insert our pop up which created by JOptionPane to show high scores
	public JPanel HighScoresPopUpPanel() {
		JPanel panel = new JPanel(); // create panel of pop up
		panel.setPreferredSize(new Dimension(300, 300));
		List<ArrayList<String>> namesAndScoresOrderByAsc = scores.GetScoreData();

		String[][] tableData = new String[namesAndScoresOrderByAsc.size()][];
		
		// now start from the last element of namesAndScoresOrderByAsc
		// to get the highest score first and add it to tableData
		int temp = namesAndScoresOrderByAsc.size() -1 ;
		for (int i = 0; i < namesAndScoresOrderByAsc.size(); i++) {
			ArrayList<String> row = namesAndScoresOrderByAsc.get(i);
			tableData[temp] = row.toArray(new String[row.size()]);
			temp--;
		}

		String[] columnNames = { "Score", "User" };
		JTable table = new JTable(tableData, columnNames);
		panel.add(new JScrollPane(table));

		table.setPreferredSize(new Dimension(300, 280));
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);

		table.setRowHeight(25); 
		table.setDefaultEditor(Object.class, null); // to disable editable cells

		return panel;
	}
}