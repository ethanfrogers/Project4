package controller;

import controller.command.Command;
import java.util.ArrayList;

import model.TextEditorModel;
import model.StyleList;
import model.StylePrinter;


public class TextEditorController
{
	private TextEditorView view;
	
	private TextEditorModel model;
	private ArrayList<Command> undoCommands = new <Command>ArrayList();
        private ArrayList<Command> redoCommands = new <Command>ArrayList();
        
	public TextEditorController(TextEditorModel modelRef)
	{
		setView(null);
		setModel(modelRef);
	}
        
        public void addUndoCommand(Command c){
            undoCommands.add(c);
        }
       
        public void removeUndoCommand(Command c){
            undoCommands.remove(c);
        }
        
        public void addRedoCommand(Command c){
            redoCommands.add(c);
        }
        
        public void removeRedoCommand(Command c){
            redoCommands.remove(c);
        }
        
	public void setView(TextEditorView viewRef)
	{
		view = viewRef;
	}
	private void setModel(TextEditorModel modelRef)
	{
		model = modelRef;
	}
	public void setColor(int start, int length, boolean colorOn){
                model.setColor(start, length, colorOn);
        }
	public void setBold(int start, int length, boolean boldOn)
	{
		model.setBold(start, length, boldOn);
	}
	public void setItalic(int start, int length, boolean italicOn)
	{
		model.setItalic(start, length, italicOn);
	}
	public void setUnderline(int start, int length, boolean underlineOn)
	{
		model.setUnderline(start, length, underlineOn);
	}
	
        public Boolean getModelState(Command c,int start, int length)
        {
            return StateControl.getModelState(start, length, c, model);
            
        }
        
	public void undo()
	{
            try
            {
                int index = undoCommands.size() - 1;
                Command cmd = undoCommands.get(index);
                cmd.execute();
                addRedoCommand(cmd);
                removeUndoCommand(cmd);
            }
            catch(ArrayIndexOutOfBoundsException iob){
                System.out.println("NO UNDO COMMANDS");
            }
           
           
	}
	public void redo()
	{
            try
            {
                int index = redoCommands.size() - 1;
                Command cmd = redoCommands.get(index);
                cmd.execute();
                addUndoCommand(cmd);
                removeRedoCommand(cmd);
            }
            catch(ArrayIndexOutOfBoundsException iob)
            {
                System.out.println("NO REDO COMMANDS");
            }
	}
	
	public void textInserted(int start, int length)
	{
		String insertedText = view.getText().substring(start, start + length);
		StyleList insertedStyles = view.getStylesInRange(start, length);
		
		model.insertTextAt(start, insertedText, insertedStyles);
	}
	public void textRemoved(int start, int length)
	{
		model.removeTextAt(start, length);
	}
	
	private void updateUndoRedoButtons()
	{
	}
	
	public void openFile()
	{
		String fileName = view.getOpenFileName();
		
		if(fileName.equals("") == false)
		{
			/**
			 * Insert your template method pattern file opening code here.
			 */
		}
	}
	
	public void saveFile()
	{
		String fileName = view.getSaveFileName();
		
		if(fileName.equals("") == false)
		{
			/**
			 * Insert your builder pattern file saving code here.
			 */
		}
	}
	
	private SaveType getFileType(String filePath)
	{
		if(filePath.endsWith(".html") || filePath.endsWith(".htm"))
		{
			return SaveType.HTML;
		}
		else if(filePath.endsWith(".gt"))
		{
			return SaveType.GOOGLE_TALK;
		}
		else
		{
			return SaveType.PLAIN_TEXT;
		}
	}
	
	// ----------------------------------------------------------------------------
	// Printing Debug Methods
	// ----------------------------------------------------------------------------
	public void printCommands()
	{
		System.out.println();
	}
	
	public void printTextComparison()
	{
		System.out.println("Editor Text: \"" + view.getText() + "\"");
		System.out.println("Model Text:  \"" + model.getText() + "\"");
		System.out.println();
	}
	
	public void printStyleComparison()
	{
		System.out.println("Editor Styles:");
		StylePrinter.printStyles(view.getStyles());
		System.out.println("Model Styles:");
		StylePrinter.printStyles(model.getStyles());	
		System.out.println();
	}
}
