/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.command;

import controller.TextEditorController;
import java.util.ArrayList;
import model.TextEditorModel;

/**
 *
 * @author efrogers
 */
public class MacroCommand implements Command{

    ArrayList<Command> macro = new <Command>ArrayList();
    
    TextEditorController controller;
    
    public MacroCommand(TextEditorController controller){
        
        this.controller = controller;
    }
    
    public void addCommand(Command c){
        macro.add(c);
    }
    
    public void addCommand(ArrayList<Command> list){
        int i = 0;
        for(Command c : list){
            macro.set(i, c);
            i++;
        }
    }
    
    public void useDefaultMacro(int start, int length){
        BoldCommand bld = new BoldCommand(controller);
        bld.setAttributes(start,length);
        
        ItalicCommand itl = new ItalicCommand(controller);
        itl.setAttributes(start, length);
        
        addCommand(bld);
        addCommand(itl);
    }
    
    @Override
    public void execute() {
        for(Command c: macro){
            c.execute();
        }
    }
    
    
    
}
