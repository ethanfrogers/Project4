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
public class UnderlineCommand implements Command{
    TextEditorController controller;
    TextEditorModel model;
    private int start, length;
    
    public UnderlineCommand(TextEditorController t, TextEditorModel m){
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
        boolean state = model.isRangeUnderline(start, length);
        controller.setUnderline(start, length, !state);
    }
    
}
