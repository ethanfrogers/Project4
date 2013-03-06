/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.command;

import controller.TextEditorController;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;


/**
 *
 * @author efrogers
 */
public class MacroRecorder implements Observer{
    
    private ArrayList<Command> commands = new <Command>ArrayList();
    private TextEditorController controller;
    
    public MacroRecorder(MacroCommand m)
    {   
        controller = m.controller;
        RecorderDisplay display = new RecorderDisplay(m);
    }
    
    
    
    public ArrayList<Command> getRecordedCommands()
    {
        System.out.println(commands.size());
        return commands;
    }
    
    private void addCommand(Command c){
        System.out.println("Command added");
        commands.add(c);
    }
    
    @Override
    public void update(Observable o, Object o1) {
        addCommand((Command)o1);
    }
    
    
    private class RecorderDisplay extends Observable implements ActionListener{

        
        public RecorderDisplay(Observer o){
            this.addObserver(o);
            recordCommands();
        }
        
        public void recordCommands(){
            JFrame frame = new JFrame();
            createPanel(frame);
            frame.pack();
            frame.requestFocus();
            frame.setVisible(true);
        
        }
        
        private void createPanel(JFrame f)
        {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            JLabel lbl_instruct = new JLabel("Please choose which macro commands you would like\n"
                    + "to use in your custom macro: ");
            panel.add(lbl_instruct,BorderLayout.NORTH);

            JPanel panel2 = new JPanel();
            panel2.setLayout(new BoxLayout(panel2,BoxLayout.X_AXIS));
            for(JRadioButton b : getCommandButtons())
            {
                panel2.add(b);
            }


            panel.add(panel2);
            f.add(panel);
        }
        
        private ArrayList<JRadioButton> getCommandButtons()
        {
            ArrayList<JRadioButton> buttons = new <JRadioButton>ArrayList();
            buttons.add(new JRadioButton("Bold"));
            buttons.add(new JRadioButton("Italic"));
            buttons.add(new JRadioButton("Underline"));

            for(JRadioButton b:buttons)
            {
                b.addActionListener(this);
                if(b.getText().equals("Bold")){
                    b.setActionCommand("bold");
                }
                else if(b.getText().equals("Italic")){
                    b.setActionCommand("italic");
                }
                else if(b.getText().equals("Underline")){
                    b.setActionCommand("underline");
                }
            }

            return buttons;
        }
        
        
        
        @Override
        public void actionPerformed(ActionEvent ae) {
                
                Command c;
                if(ae.getActionCommand().equals("bold")){
                    c = new BoldCommand(controller);
                }
                else if(ae.getActionCommand().equals("italic")){
                    c = new ItalicCommand(controller);
                }
                else if(ae.getActionCommand().equals("underline")){
                    c = new UnderlineCommand(controller);
                }
                else
                {
                    c = null;
                }
                
                setChanged();
                notifyObservers(c);
                
        }
        
        
    }
}


