package view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import view.TextEditorPanel;
import view.TextEditorMenu;
import controller.TextEditorController;
import model.TextEditorModel;

public class TextEditor
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("340 Text Editor");
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
		addComponents(frame);

		frame.pack();
		frame.setVisible(true);
	}
	
	private static void addComponents(JFrame frame)
	{
		TextEditorModel model = new TextEditorModel();
		TextEditorController controller = new TextEditorController(model);
		
		TextEditorPanel panel = new TextEditorPanel(controller, model);
		TextEditorMenu menu = new TextEditorMenu(controller);
		
		frame.add(panel);
		frame.setJMenuBar(menu);
	}
}

