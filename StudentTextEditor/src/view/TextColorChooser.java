/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Sara
 */
public class TextColorChooser{
    
   
    public static Color colorChooser(){
        JFrame frame = new JFrame();
        Color c = null;
        JPanel panel = new JPanel();
        JColorChooser colors = new JColorChooser();
        panel.add(colors);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.requestFocus();
        c = colors.getColor();
        return c;
        
    }
}
