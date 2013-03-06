/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.command;

import controller.TextEditorController;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author efrogers
 */
public class MacroCommand implements Command, Observer{

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
        
        UnderlineCommand under = new UnderlineCommand(controller);
        under.setAttributes(start, length);
        
        addCommand(bld);
        addCommand(under);
    }
    
    public void setAttributes(int start, int length)
    {
        for(Command c: macro)
        {
            c.setAttributes(start, length);
        }
    }
    
    public void recordMacro(ArrayList<Command> list)
    {
        for(Command c:list)
        {
            addCommand(c);
        }
    }
    
    @Override
    public void execute() {
        for(Command c: macro){
            c.execute();
        }
    }

    @Override
    public void update(Observable o, Object o1) {
        Command c = (Command) o1;
        addCommand(c);
    }
    
    
    
}
