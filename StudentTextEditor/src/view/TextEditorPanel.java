package view;

import controller.TextEditorController;
import controller.TextEditorView;
import controller.command.BoldCommand;
import controller.command.ItalicCommand;
import controller.command.MacroCommand;
import controller.command.RedoCommand;
import controller.command.TextInsertCommand;
import controller.command.TextRemoveCommand;
import controller.command.UnderlineCommand;
import controller.command.UndoCommand;

import model.StyleList;
import model.TextEditorModel;
import model.TextChange;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

import java.util.Observer;
import java.util.Observable;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.text.Keymap;



public class TextEditorPanel extends JPanel implements TextEditorView, Observer
{
	private static String iconPath = TextEditorPanel.class.getResource("icons/").getPath();
        
	
	private JTextEditor text;
	
	private TextEditorController controller;
	private TextEditorModel model;
	
	private JButton undoButton;
	private JButton redoButton;
       
        
	private BoldActionListener boldAct;
	private ItalicActionListener italicAct;
	private UnderlineActionListener underlineAct;
	private UndoActionListener undoAct;
	private RedoActionListener redoAct;
        private ColorActionListener colorAct;
        private MacroActionListener macroAct;
        
	
	private FileNameExtensionFilter plainTextFilter;
	private FileNameExtensionFilter htmlFilter;
	private FileNameExtensionFilter googleTalkFilter;
	private List<FileNameExtensionFilter> filters;
	private FileFilter previousFilter;
	
	private boolean textChangeFromEditor;
	private boolean updatingFromModel;
	
	public TextEditorPanel(TextEditorController controllerRef, TextEditorModel modelRef)
	{
		initializePanel();
		initializeActions();
		initializeToolbar();
		initializeEditor();
		assignActions();
		
		initializeFilters();
		
		setUndoEnabled(true);
		setRedoEnabled(true);
		
		textChangeFromEditor = false;
		updatingFromModel = false;
		
		controller = controllerRef;
		controller.setView(this);
		
		model = modelRef;
		model.addObserver(this);
	}
	
	private void initializePanel()
	{
		setPreferredSize(new Dimension(525, 400));
		setLayout(new BorderLayout());
	}
	
	private void initializeActions()
	{
		boldAct = new BoldActionListener();
		italicAct = new ItalicActionListener();
		underlineAct = new UnderlineActionListener();
		undoAct = new UndoActionListener();
		redoAct = new RedoActionListener();
                colorAct = new ColorActionListener();
                macroAct = new MacroActionListener();
	}
	private KeyStroke getControlPlusKey(int keyEventVal)
	{
		return KeyStroke.getKeyStroke(keyEventVal, InputEvent.CTRL_DOWN_MASK);
	}
	
	private void initializeToolbar()
	{
		JPanel topButtons = new JPanel();
		topButtons.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
                topButtons.add(createButton("format-text-color.png", colorAct));
		topButtons.add(createButton("format-text-bold.png", boldAct));
		topButtons.add(createButton("format-text-italic.png", italicAct));
		topButtons.add(createButton("format-text-underline.png", underlineAct));
                topButtons.add(createButton("command-macro.png",macroAct));
		
		undoButton = createButton("edit-undo.png", undoAct);
		redoButton = createButton("edit-redo.png", redoAct);
		
		topButtons.add(undoButton);
		topButtons.add(redoButton);
		
		add(topButtons, BorderLayout.PAGE_START);
	}
	
	private JButton createButton(String imageName, ActionListener listener)
	{
		JButton result = new JButton();
		result.setText("");
		result.setIcon(new ImageIcon(iconPath + imageName));
		result.addActionListener(listener);
		return result;
	}
	
	private void initializeEditor()
	{
		text = new JTextEditor();
		text.getStyledDocument().addDocumentListener(new EditorDocumentListener());
		
		JScrollPane textScroller = new JScrollPane(text);
		textScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		add(textScroller, BorderLayout.CENTER);
	}
        
        
	private void assignActions()
	{
		Keymap map = text.getKeymap();
		map.addActionForKeyStroke(getControlPlusKey(KeyEvent.VK_B), boldAct);
		map.addActionForKeyStroke(getControlPlusKey(KeyEvent.VK_I), italicAct);
		map.addActionForKeyStroke(getControlPlusKey(KeyEvent.VK_U), underlineAct);
		map.addActionForKeyStroke(getControlPlusKey(KeyEvent.VK_Y), redoAct);
		map.addActionForKeyStroke(getControlPlusKey(KeyEvent.VK_Z), undoAct);
		
		map.addActionForKeyStroke(getControlPlusKey(KeyEvent.VK_S), new StylePrintListener());
		map.addActionForKeyStroke(getControlPlusKey(KeyEvent.VK_T), new TextPrintListener());
		map.addActionForKeyStroke(getControlPlusKey(KeyEvent.VK_P), new CommandPrintListener());
	}
	
	private void initializeFilters()
	{
		plainTextFilter = new FileNameExtensionFilter("Plain Text File", "txt"); 
		htmlFilter = new FileNameExtensionFilter("HTML File", "html", "htm");
		googleTalkFilter = new FileNameExtensionFilter("Google Talk File", "gt");
		
		filters = new LinkedList<FileNameExtensionFilter>();
		
		filters.add(plainTextFilter);
		filters.add(htmlFilter);
		filters.add(googleTalkFilter);
		
		previousFilter = null;
	}
	
	public JTextEditor getEditor()
	{
		return text;
	}
	
	public void setUndoEnabled(boolean enabled)
	{
		undoButton.setEnabled(enabled);
		undoAct.setEnabled(enabled);
	}
	public void setRedoEnabled(boolean enabled)
	{
		redoButton.setEnabled(enabled);
		redoAct.setEnabled(enabled);
	}
	
	public StyleList getStylesInRange(int start, int length)
	{
		return text.getStylesInRange(start, length);
	}
	public StyleList getStyles()
	{
		return text.getStyles();
	}
	
	public String getText()
	{
		return text.getText();
	}
	
	public void removeTextAt(int start, int length)
	{
		text.removeTextAt(start, length);
	}
	public void insertTextAt(int start, String textValue, StyleList textStyles)
	{
		text.insertTextAt(start, textValue, textStyles);
	}
	
	public void setUnderline(int start, int length, boolean underlineOn)
	{
		text.setUnderline(start, length, underlineOn);
	}
	public void setItalic(int start, int length, boolean italicOn)
	{
		text.setItalic(start, length, italicOn);
	}
	public void setBold(int start, int length, boolean boldOn)
	{
		text.setBold(start, length, boldOn);
	}
        public void setColor(int start, int length, boolean colorOn)
        {
                text.setColor(start,length,colorOn);
        }
	
	public boolean isUnderline(int index)
	{
		return text.isUnderline(index);
	}
	public boolean isItalic(int index)
	{
		return text.isItalic(index);
	}
	public boolean isBold(int index)
	{
		return text.isBold(index);
	}
        public boolean isColor(int index)
        {
                return text.isColor(index);
        }
	
	public void update(Observable observed, Object changed)
	{
		if(textChangeFromEditor == false)
		{
			updatingFromModel = true;
			
			TextChange tc = (TextChange)changed;
			switch(tc.getChangeType())
			{
				case INSERT:
					insertTextAt(tc.getStartIndex(),
						model.getTextSubstring(tc.getStartIndex(), tc.getLength()),
						model.getStylesInRange(tc.getStartIndex(), tc.getLength()));
					break;
					
				case REMOVE:
					removeTextAt(tc.getStartIndex(), tc.getLength());
					break;
					
				case BOLD:
					setBold(tc.getStartIndex(), tc.getLength(), !isBold(tc.getStartIndex()));
					break;
					
				case ITALIC:
					setItalic(tc.getStartIndex(), tc.getLength(), !isItalic(tc.getStartIndex()));
					break;
					
				case UNDERLINE:
					setUnderline(tc.getStartIndex(), tc.getLength(), !isUnderline(tc.getStartIndex()));
					break;
                                case COLOR:
                                        setColor(tc.getStartIndex(),tc.getLength(),!isColor(tc.getStartIndex()));
			}
			
			updatingFromModel = false;
		}
	}
	
	// ----------------------------------------------------------------------------
	// File Input/Output
	// ----------------------------------------------------------------------------
	public String getOpenFileName()
	{
		JFileChooser dialog = getFileChooser("Select File to Open");
		String result = "";
		
		if(dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			result = dialog.getSelectedFile().getAbsolutePath();
			previousFilter = dialog.getFileFilter();
		}
		
		return result;
	}
	
	public String getSaveFileName()
	{
		JFileChooser dialog = getFileChooser("Select File to Save");
		String result = "";
		
		if(dialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			result = addChosenExtension(dialog.getFileFilter(), dialog.getSelectedFile());
			previousFilter = dialog.getFileFilter();
		}
		
		return result;
	}
	
	private String addChosenExtension(FileFilter filter, File chosenFile)
	{
		String result = chosenFile.getAbsolutePath();
		
		boolean accepted = false;
		for(int i = 0; i < filters.size(); i++)
		{
			if(filters.get(i).accept(chosenFile))
			{
				accepted = true;
			}
		}
		
		if(accepted == false)
		{
			FileNameExtensionFilter filterCast = (FileNameExtensionFilter)filter;
			result += "." + filterCast.getExtensions()[0];
		}
		
		return result;
	}
	private JFileChooser getFileChooser(String title)
	{
		JFileChooser dialog = new JFileChooser();
		dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		dialog.setMultiSelectionEnabled(false);
		dialog.setDialogTitle(title);
		
		addFilters(dialog);
		setPreviousFilter(dialog);
		
		dialog.setCurrentDirectory(new File(System.getProperty("user.dir")));
		return dialog;
	}
	private void addFilters(JFileChooser dest)
	{
		for(int i = 0; i < filters.size(); i++)
		{
			dest.addChoosableFileFilter(filters.get(i));
		}
	}
	private void setPreviousFilter(JFileChooser dialog)
	{
		if(previousFilter == null)
		{
			previousFilter = dialog.getAcceptAllFileFilter();
		}
		dialog.setFileFilter(previousFilter);
	}
	
	// ----------------------------------------------------------------------------
	// Action Listeners
	// ----------------------------------------------------------------------------
	private class CommandPrintListener extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			controller.printCommands();
		}
	}
	
	private class TextPrintListener extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			controller.printTextComparison();
		}
	}
	
	private class StylePrintListener extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			controller.printStyleComparison();
		}
	}
	
        private class MacroActionListener extends AbstractAction implements ActionListener
        {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    MacroCommand cmd = new MacroCommand(controller);
                    cmd.useDefaultMacro(text.getSelectionStart(), text.getSelectionLength());
                    controller.addUndoCommand(cmd);
                    cmd.execute();
                }
            
        }
        
        private class ColorActionListener extends AbstractAction implements ActionListener
        {

                public void actionPerformed(ActionEvent ae) 
                {   
                    System.out.println("Color change pressed");
                    int start = text.getSelectionStart();
                    //controller.setColor(start,text.getSelectionLength(),!text.isColor(start));
                    Color c = TextColorChooser.colorChooser();
                    System.out.println(c.toString());
                    text.requestFocus();
                }
            
        }
	private class BoldActionListener extends AbstractAction implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int start = text.getSelectionStart();
                        BoldCommand cmd = new BoldCommand(controller);
                        cmd.setStart(start);
                        cmd.setLength(text.getSelectionLength());
                        cmd.execute();
                        controller.addUndoCommand(cmd);
			text.requestFocus();
		}
	}
	
	private class ItalicActionListener extends AbstractAction implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int start = text.getSelectionStart();
                        ItalicCommand cmd = new ItalicCommand(controller);
                        cmd.setStart(start);
                        cmd.setLength(text.getSelectionLength());
                        cmd.execute();
                        controller.addUndoCommand(cmd);
			text.requestFocus();
		}
	}
	
	private class UnderlineActionListener extends AbstractAction implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int start = text.getSelectionStart();
                        UnderlineCommand cmd = new UnderlineCommand(controller);
                        cmd.setStart(start);
                        cmd.setLength(text.getSelectionLength());
                        cmd.execute();
                        controller.addUndoCommand(cmd);
			text.requestFocus();
		}
	}
	
	private class UndoActionListener extends AbstractAction implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
                        UndoCommand cmd = new UndoCommand(controller);
                        cmd.execute();
			text.requestFocus();
		}
	}
	
	private class RedoActionListener extends AbstractAction implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			RedoCommand cmd = new RedoCommand(controller);
                        cmd.execute();
			text.requestFocus();
		}
	}
	
	private class EditorDocumentListener implements DocumentListener
	{
            
		public void changedUpdate(DocumentEvent e)
		{
		}
		
		public void insertUpdate(DocumentEvent e)
		{
                        

			if(updatingFromModel == false)
			{
				textChangeFromEditor = true;
				//controller.textInserted(e.getOffset(), e.getLength());
                                TextInsertCommand cmd = new TextInsertCommand(controller,model);
                                cmd.setAttributes(e.getOffset(), e.getLength());
                                TextRemoveCommand cmd1 = new TextRemoveCommand(controller, model);
                                cmd1.setAttributes(e.getOffset(), e.getLength());                              
                                cmd.execute();
                                //controller.addUndoCommand(cmd1);
                                text.requestFocus();
                                
				textChangeFromEditor = false;
			}
		}
		
		public void removeUpdate(DocumentEvent e)
		{
                        

			if(updatingFromModel == false)
			{
				textChangeFromEditor = true;
				//controller.textRemoved(e.getOffset(), e.getLength());
                                TextRemoveCommand cmd = new TextRemoveCommand(controller,model);
                                cmd.setAttributes(e.getOffset(), e.getLength());
                                TextInsertCommand cmd1 = new TextInsertCommand(controller, model);
                                cmd1.setAttributes(e.getOffset(), e.getLength());
                                cmd.execute();
                                //controller.addRedoCommand(cmd1);                                
				textChangeFromEditor = false;
			}
		}
	}
}

