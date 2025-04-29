/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author elenagoncarova
 */
public class Frame extends JFrame implements ActionListener {
    private Input importFilePathInput;
    private Input listInput;
    private Input exportFilePathInput;
    private Button importButton;
    private Button exportButton;
    private Button calculateButton;
    private ImportHandler importHandler;
    private ExportHandler exportHandler;
    private PressHandler calculateHandler;

    public Frame() {
        setTitle("Калькулятор показателей");
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds(dimension.width / 2 - 250, dimension.height / 2 - 150, 500, 300);

        importFilePathInput = new Input("Файл импорта");
        importFilePathInput.setStartPoint(10, 10);
        importFilePathInput.setParent(this);
        listInput = new Input("Индекс листа");
        listInput.setStartPoint(10, 40);
        listInput.setWidth(30);
        listInput.setParent(this);
        exportFilePathInput = new Input("Файл экспорта");
        exportFilePathInput.setStartPoint(10, 70);
        exportFilePathInput.setParent(this);
        
        importButton = new Button("Импортировать");
        importButton.setBounds(15, 180, 150, 40);
        importButton.setParent(this);
        importButton.setPressHandler(() -> {
            String filePath = importFilePathInput.getValue().trim();
            System.out.println(filePath);
            int listIndex = 0;
            try {
                listIndex = Integer.parseInt(listInput.getValue().trim());
            } catch (Exception e) {
                this.showMessage("Введите число в поле индекс листа");
                return;
            }
            importHandler.handle(filePath, listIndex);
        });
        
        calculateButton = new Button("Рассчитать");
        calculateButton.setBounds(170, 180, 150, 40);
        calculateButton.setParent(this);
        calculateButton.setPressHandler(() -> calculateHandler.handle());
        
        exportButton = new Button("Экспортировать");
        exportButton.setBounds(325, 180, 150, 40);
        exportButton.setParent(this);
        exportButton.setPressHandler(() -> {
            String filePath = exportFilePathInput.getValue().trim();
            System.out.println(filePath);
            if (exportHandler != null) { 
                exportHandler.handle(filePath);
            } else {
                showMessage("Ошибка: обработчик экспорта не установлен.");
            }
        });
        
        Button exitButton = new Button("Выход");
        exitButton.setBounds(377, 230, 100, 30);
        exitButton.setParent(this);
        exitButton.setPressHandler(() -> System.exit(0));

        setVisible(true);
    }
    
    public void setImportHandler(ImportHandler importHandler) {
        this.importHandler = importHandler;
    }

    public void setCalculateHandler(PressHandler calculateHandler) {
        this.calculateHandler = calculateHandler;
    }

    public void setExportHandler(ExportHandler exportHandler) {
        this.exportHandler = exportHandler;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("exit")) {
            System.exit(0);
        }
    }
}
