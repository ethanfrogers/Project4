/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.command;

import controller.TextEditorController;

/**
 *
 * @author efrogers
 */
public class UndoCommand implements Command {
    
    TextEditorController controller;
    
    public UndoCommand(TextEditorController t){
        controller = t;
    }
    
    @Override
    public void execute() {
        controller.undo();
    }

    @Override
    public void setAttributes(int start, int length) {
    }
    
    
    
}
