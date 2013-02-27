/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.command;

import controller.TextEditorController;
import model.TextEditorModel;

/**
 *
 * @author efrogers
 */
public class ItalicCommand implements Command {

    TextEditorController controller;
    TextEditorModel model;
    private int start, length;
    
    public ItalicCommand(TextEditorController t, TextEditorModel m){
        controller = t;
        model = m;
    }
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    

    @Override
    public void execute() {
       boolean state = model.isRangeItalic(start, length);
       controller.setItalic(start, length, !state);
    }
    
    
    
}
