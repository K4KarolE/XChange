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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;

public class UI {

    static JFrame frame = new JFrame();
    static JTextField insertField = new JTextField();
    static JLabel label_result = new JLabel("---");
    
    static HashMap<String,String> mapCurrency = CurrencyMap.createCurrencyMap();

    static JComboBox<String> combo_box_unit_from;
    static JComboBox<String> combo_box_unit_to;

    static String selectedComboBoxFrom;
    static String selectedComboBoxTo;
    static Double fieldValueFrom;
    static Double resultValueRaw;
    static String insertFieldText;
    static DecimalFormat decFormat = new java.text.DecimalFormat("0.##E0");

    static Font resultValueFontStyle = new Font("Times New Roman", Font.PLAIN, 20);
    static Font insertFieldFontStyle = new Font("Times New Roman", Font.PLAIN, 18);
    static Font historicCurrFontStyle = new Font("Times New Roman", Font.PLAIN, 18);

    
    static String workingDir = Path.of("").toAbsolutePath().toString();
    static String imgPath = workingDir + "./app/src/main/resources/frame_icon.png";
    static JLabel[] historicCurrLabel = new JLabel[5];


    static void generateWindow() {

        Functions.generateLastUsedCurrencies();
        Functions.appStartsGroupedActions();


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
        int frameWidth = 600;
        int frameHeight = 500;
        frame.setIconImage(icon);
        frame.setTitle("XChange");
        frame.setLayout(null);
        frame.setBounds(700,500,frameWidth,frameHeight);
        frame.getContentPane().setBackground(new java.awt.Color(243, 243, 243));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        insertField.setFont(insertFieldFontStyle);
        insertField.setHorizontalAlignment(JTextField.RIGHT);
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
        combo_box_unit_to.setSelectedIndex(Functions.lastUsedCurrToIndex);
        combo_box_unit_to.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLastUsedCurrs();
                Functions.saveLastUsedCurrenciesJson();
                Functions.generateRatesFromNodes();
                updateResultAction();
                updateHistoricCurrLabels();
            }
        });
            
        combo_box_unit_from = new JComboBox<>(combo_box_options);
        combo_box_unit_from.setSelectedIndex(Functions.lastUsedCurrFromIndex);
        combo_box_unit_from.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLastUsedCurrs();
                Functions.saveLastUsedCurrenciesJson();
                Functions.generateRatesFromNodes();
                updateResultAction();
                updateHistoricCurrLabels();
            }
        });


        label_result.setFont(resultValueFontStyle);
        
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
        label_result.setBounds(xBase + 2, yBase + widgetHeight, frameWidth-xBase*2, widgetHeight);

        Component[] widgets_array = {
            label_result,
            insertField,
            combo_box_unit_from,
            combo_box_unit_to,
        };

        for (Component widget : widgets_array) {
           frame.add(widget);
        }

    
        for (int i = 0; i<5; i++) {

            int gap = 30;
            double rate = 1 / Functions.ratesFrom[i] * Functions.ratesTo[i];
            String toDisplay = Functions.timeLastUpdateUtc[i] + "  " + generateHistoricRateToDisplay(rate);

            historicCurrLabel[i] = new JLabel(toDisplay);
            historicCurrLabel[i].setFont(historicCurrFontStyle);
            historicCurrLabel[i].setBounds(xBase, 130 + gap*i, widgetWidth, widgetHeight);
            frame.add(historicCurrLabel[i]);
        }

        frame.setVisible(true);


    }


    static void updateLastUsedCurrs() {
        selectedComboBoxFrom = combo_box_unit_from.getSelectedItem().toString();
        selectedComboBoxTo = combo_box_unit_to.getSelectedItem().toString();
        Functions.lastUsedCurrFrom = mapCurrency.get(selectedComboBoxFrom);
        Functions.lastUsedCurrTo = mapCurrency.get(selectedComboBoxTo);
        Functions.lastUsedCurrFromIndex = combo_box_unit_from.getSelectedIndex();
        Functions.lastUsedCurrToIndex = combo_box_unit_to.getSelectedIndex();
    }


    static String customFormat(String pattern, double value ) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(value);
    }


    static String generateResultValueToDisplay(double resultValueRaw) {
        if (resultValueRaw >= 1) {
            return customFormat("###,###.00", resultValueRaw);
        }
        else if (resultValueRaw > 0.000_001) {
            return customFormat("#.#######", resultValueRaw);
        }
        else {
            return decFormat.format(resultValueRaw);
        }
    }


    static String generateHistoricRateToDisplay(double rate) {
        if (rate >= 1 && rate < 10_000) {
            return customFormat("###,###.00", rate);
        }
        else if (rate > 0.000_01) {
            return customFormat("#.######", rate);
        }
        else {
            return decFormat.format(rate);
        }
    }


    static void updateHistoricCurrLabels() {

        for (int i = 0; i<5; i++) {

            double rate = 1 / Functions.ratesFrom[i] * Functions.ratesTo[i];
            String toDisplay = Functions.timeLastUpdateUtc[i] + generateHistoricRateToDisplay(rate);
            historicCurrLabel[i].setText(toDisplay);
        }
    }


    static void updateResultAction() {

       insertFieldText = insertField.getText().trim();

        if (!insertFieldText.isEmpty()) {

            fieldValueFrom = null;

            try {
                fieldValueFrom = abs(Double.parseDouble(insertFieldText));
            } catch (NumberFormatException e) {
                System.out.println("Invaild value!");
            }

            if (fieldValueFrom != null && fieldValueFrom != 0) {
                resultValueRaw = fieldValueFrom / Functions.ratesFrom[0] * Functions.ratesTo[0];
                label_result.setText(generateResultValueToDisplay(resultValueRaw) + "  " + Functions.lastUsedCurrTo);
            } else {
                label_result.setText("0.0" + "  " + Functions.lastUsedCurrTo);
            }
        }
        else {
            label_result.setText("0.0  " + Functions.lastUsedCurrTo);
        }
        }
    }

