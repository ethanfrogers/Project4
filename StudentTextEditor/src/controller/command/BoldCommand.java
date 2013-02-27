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
public class BoldCommand implements Command {
    
    TextEditorController controller;
    TextEditorModel model;
    private int start, length;
    
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
    
    
    
    public BoldCommand(TextEditorController t, TextEditorModel m){
        controller = t;
        model = m;
    }

    @Override
    public void execute() {
        boolean state = model.isRangeBold(start, length);
        controller.setBold(start, length, !state);
    }
    
    
    
}
