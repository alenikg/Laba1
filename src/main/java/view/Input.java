/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author elenagoncarova
 */
public class Input {
    private JTextField textField;
    private JLabel label;


    public Input(String title) {
        label = new JLabel(title);
        textField = new JTextField();
    }

    public void setStartPoint(int x, int y) {
        label.setBounds(x, y, 100, 20);
        textField.setBounds(x + 100 + 10, y, 370, 20);
    }

    public void setWidth(int width) {
        textField.setBounds(textField.getX(), textField.getY(), width, 20);
    }
    
    public void setParent(Frame parent) {
        parent.add(label);
        parent.add(textField);
    }


    public String getValue() {
        return textField.getText();
    }
}
