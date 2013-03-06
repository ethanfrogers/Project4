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
public class RedoCommand implements Command {
    
    TextEditorController controller;
    
    public RedoCommand(TextEditorController c){
        controller = c;
    }

    @Override
    public void execute() {
        controller.redo();
    }

    @Override
    public void setAttributes(int start, int length) {
    }
    
    
    
}
