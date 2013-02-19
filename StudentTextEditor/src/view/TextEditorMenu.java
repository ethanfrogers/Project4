package view;

import controller.TextEditorController;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.FileDialog;

public class TextEditorMenu extends JMenuBar
{
	private TextEditorController controller;
	
	public TextEditorMenu(TextEditorController controllerRef)
	{
		this.add(createFileMenu());
		
		controller = controllerRef;
	}
	
	private JMenu createFileMenu()
	{
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_S);
		
		JMenuItem openFile = new JMenuItem(new OpenActionListener());
		JMenuItem saveFile = new JMenuItem(new SaveActionListener());
		
		fileMenu.add(openFile);
		fileMenu.add(saveFile);
		
		return fileMenu;
	}
	
	private class OpenActionListener extends AbstractAction
	{
		public OpenActionListener()
		{
			super("Open File");
		}
		
		public void actionPerformed(ActionEvent e)
		{
			controller.openFile();
		}
	}
		
	private class SaveActionListener extends AbstractAction
	{
		public SaveActionListener()
		{
			super("Save File");
		}
		
		public void actionPerformed(ActionEvent e)
		{
			controller.saveFile();
		}
	}
}
