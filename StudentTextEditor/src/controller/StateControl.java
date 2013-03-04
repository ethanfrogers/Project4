/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.command.*;
import model.TextEditorModel;
/**
 *
 * @author Sara
 */
public class StateControl {
    
    
    public static Boolean getModelState(int start, int length, Command c, TextEditorModel m)
    {
        if(c instanceof BoldCommand){
            return m.isRangeBold(start, length);
        }
        else if(c instanceof ItalicCommand){
            return m.isRangeItalic(start, length);
        }
        else if(c instanceof UnderlineCommand){
            return m.isRangeUnderline(start, length);
        }
        else
            return false;
    }
    
}
