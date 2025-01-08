package karole;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Component;
import java.awt.Font;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class UI {

    static Functions fs = new Functions();
    static JFrame frame = new JFrame();
    static JTextField insertField = new JTextField();
    static JLabel label_result = new JLabel("0.0");
    
    static HashMap<String,String> mapCurrency = new CurrencyMap().createCurrencyMap();

    static JComboBox<String> combo_box_unit_from;
    static JComboBox<String> combo_box_unit_to;

    static String selectedComboBoxFrom;
    static String selectedComboBoxTo;
    static Double fieldValueFrom;
    static Double resultValue;
    static String insertFieldText;

    static Font label_font_style = new Font("Times New Roman", Font.PLAIN, 20);
    static Font insertFieldFontStyle = new Font("Times New Roman", Font.PLAIN, 18);

    
    static String workingDir = Path.of("").toAbsolutePath().toString();
    static String imgPath = workingDir + "./app/src/main/resources/frame_icon.png";


    public static void generateWindow() {

        fs.appStartsGroupedActions();
        fs.generateLastUsedCurrencies();


        // COMBO BOX OPTIONS CREATION
        ArrayList<String> hash_map_al = new ArrayList<>(mapCurrency.keySet());
        int hash_map_size = hash_map_al.size();
        String[] combo_box_options = new String[hash_map_size];
        for (int i = 0; i < hash_map_size; i++) {
            combo_box_options[i] = hash_map_al.get(i);
        }
        Arrays.sort(combo_box_options);


        // FRAME
        Image icon = Toolkit.getDefaultToolkit().getImage(imgPath);
        frame.setIconImage(icon);
        frame.setTitle("XChange");
        frame.setLayout(null);
        frame.setBounds(700,500,600,500);
        frame.getContentPane().setBackground(new java.awt.Color(243, 243, 243));
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        insertField.setFont(insertFieldFontStyle);
        insertField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateResultAction();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateResultAction(); 
            }

            @Override
            public void changedUpdate(DocumentEvent e) { }          
            });

        

        combo_box_unit_to = new JComboBox<>(combo_box_options);
        combo_box_unit_to.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateResultAction();
            }
        });
            
        combo_box_unit_from = new JComboBox<>(combo_box_options);
        combo_box_unit_from.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateResultAction();
            }
        });


        label_result.setFont(label_font_style);
        
        int widgetWidth = 180;
        int widgetHeight = 30;
        int xBase = 40;
        int yBase = 40;

        int insertFieldWidth = 130;
        int xUnitFromCB = xBase + insertFieldWidth + 10;
        int xUnitToCB = xBase + (insertFieldWidth + 10) *2 + 50;


        insertField.setBounds(xBase, yBase, insertFieldWidth, widgetHeight + 2);
        combo_box_unit_from.setBounds(xUnitFromCB, yBase, widgetWidth,widgetHeight);
        combo_box_unit_to.setBounds(xUnitToCB, yBase, widgetWidth,widgetHeight);
        label_result.setBounds(xBase + 2, yBase + widgetHeight, 200,widgetHeight);

        Component[] widgets_array = {
            label_result,
            insertField,
            combo_box_unit_from,
            combo_box_unit_to,
        };

        for (Component widget : widgets_array) {
           frame.add(widget);
        }
    }


    static void updateResultAction() {

       insertFieldText = insertField.getText().trim();

        if (insertFieldText.length() != 0) {

            fieldValueFrom = null;
        
            try {
                fieldValueFrom = Double.parseDouble(insertFieldText);
            }
            catch (NumberFormatException e) {}
            
            if (fieldValueFrom != null) {
                selectedComboBoxFrom = combo_box_unit_from.getSelectedItem().toString();
                selectedComboBoxTo = combo_box_unit_to.getSelectedItem().toString();
                fs.lastUsedCurrFrom = mapCurrency.get(selectedComboBoxFrom);
                fs.lastUsedCurrTo = mapCurrency.get(selectedComboBoxTo);
                fs.generateRates();

                resultValue = fieldValueFrom / fs.rateFrom * fs.rateTo;
                label_result.setText(resultValue + "  " + fs.lastUsedCurrTo);
            }
        }
    }
}
