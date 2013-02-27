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
    
    public ItalicCommand(TextEditorController t, TextEditorModel m){
        controller = t;
        model = m;
    }

    @Override
    public void execute(int start, int length) {
       boolean state = model.isRangeItalic(start, length);
       controller.setItalic(start, length, !state);
    }
    
    
    
}
