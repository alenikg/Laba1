/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.laba1_excel;

import controller.Controller;
import model.Model;
import view.Frame;

/**
 *
 * @author elenagoncarova
 */
public class Laba1_Excel {
    private static Model model;
    private static Controller controller;
    private static Frame frame;
    // /Users/elenagoncarova/Documents/data.xlsx

    public static void main(String[] args) {

        model = new Model();
        frame = new Frame();
        controller = new Controller(frame, model);
    }
}

