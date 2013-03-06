/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.command;

/**
 *
 * @author efrogers
 */
public interface Command {
    
    public abstract void execute();
    
    public abstract void setAttributes(int start, int length);
}
