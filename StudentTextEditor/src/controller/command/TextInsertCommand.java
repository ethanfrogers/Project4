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
public class TextInsertCommand implements Command {
    
    TextEditorController controller;
    TextEditorModel model;
    
    int start, length;

    public TextInsertCommand(TextEditorController controller, TextEditorModel model) {
        this.controller = controller;
        this.model = model;
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
    
    public void setAttributes(int start, int length){
        this.start = start;
        this.length = length;
    }

    @Override
    public void execute() {
        controller.textInserted(start, length);
    }
    
    
    
}
