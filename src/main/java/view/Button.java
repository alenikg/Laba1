/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author elenagoncarova
 */
public class Button extends JButton implements ActionListener {
    private PressHandler pressHandler; 

    public Button(String title) {
        super(title);
        addActionListener(this);
    }

    public void setPressHandler(PressHandler pressHandler) {
        this.pressHandler = pressHandler;
    }

    public void setParent(JFrame parent) {
        parent.add(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pressHandler.handle();
    }
}
