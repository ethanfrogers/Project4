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
    
    public BoldCommand(TextEditorController t, TextEditorModel m){
        controller = t;
        model = m;
    }

    @Override
    public void execute(int start, int length) {
        boolean state = model.isRangeBold(start, length);
        controller.setBold(start, length, !state);
    }
    
    
    
}
