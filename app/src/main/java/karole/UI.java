package karole;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

public class UI {

    static JFrame frame = new JFrame();
    static JTextField insertField = new JTextField();
    static JLabel resultLabel = new JLabel();
    static JLabel apiUrlLabel = new JLabel();

    static HashMap<String,String> mapCurrency = CurrencyMap.createCurrencyMap();

    static JComboBox<String> comboBoxUnitFrom;
    static JComboBox<String> comboBoxUnitTo;


    static String selectedComboBoxFrom;
    static String selectedComboBoxTo;
    static Double fieldValueFrom;
    static Double resultValueRaw;
    static String insertFieldText;
    static DecimalFormat decFormat = new java.text.DecimalFormat("0.##E0");

    static Font resultValueFontStyle = new Font("Times New Roman", Font.PLAIN, 20);
    static Font insertFieldFontStyle = new Font("Times New Roman", Font.PLAIN, 18);
    static Font historicCurrFontStyle = new Font("Times New Roman", Font.PLAIN, 18);

    static int historicJsonAmount = Functions.historicJsonAmount;
    static Double[] historicRates = new Double[historicJsonAmount];
    
    static String workingDir = Path.of("").toAbsolutePath().toString();
    static String imgPathWindowIcon = workingDir + "/app/src/main/resources/frame_icon.png";

    static JLabel historicDateLabel = new JLabel();
    static JLabel[] historicRateLabel = new JLabel[historicJsonAmount];

    static JFreeChart chart;
    static DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    static Color frameBGColor = new Color(50,50,50);
    static Color chartInsideBGColor = new Color(60,60,60);
    static Color chartOutsideBGColor = frameBGColor;
    static Color appFontColor = new Color (230,230,230);
    static Color apiUrlLabelColor = new Color (100,100,100);

    static void createChart() {
        chart = ChartFactory.createLineChart(
                "",
                "",
                "",
                dataset);
        chart.setBackgroundPaint(chartOutsideBGColor);
        // Legend - Example: HUF / GBP
        chart.getLegend().setBackgroundPaint(frameBGColor);
        chart.getLegend().setItemPaint(appFontColor);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(300,130-5, 480, 350);

        // Make the "Y" values dataset dependent
        // Cheers lads: https://stackoverflow.com/q/57544667
        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
        ((NumberAxis)categoryPlot.getRangeAxis()).setAutoRangeIncludesZero(false);
        categoryPlot.getRangeAxis().setTickLabelPaint(appFontColor); // Y axis
        categoryPlot.getDomainAxis().setTickLabelPaint(appFontColor);   // X axis
        categoryPlot.setBackgroundPaint(chartInsideBGColor);

        frame.add(chartPanel);
    }


    // Arrows or hyphen between the
    // historic date and rates
    static int imgSizeArrows = 15;
    static JLabel[] historicImgArrow = new JLabel[historicJsonAmount];
    static ImageIcon generateIcon(String imgName) {
        String filePath = workingDir + "/app/src/main/resources/" + imgName+ ".png";
        ImageIcon iconRaw = new ImageIcon(filePath);
        Image image = iconRaw.getImage();
        Image newImage = image.getScaledInstance(imgSizeArrows, imgSizeArrows,  Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
    static ImageIcon iconUp = generateIcon("arrow_up");
    static ImageIcon iconDown = generateIcon("arrow_down");
    static ImageIcon iconHyphen = generateIcon("arrow_hyphen");



    static void generateWindow() {

        LogToFile.createLogger();
        Functions.generateLastUsedCurrencies();
        Functions.appStartsGroupedActions();
        createChart();


        // COMBO BOX OPTIONS CREATION
        ArrayList<String> hash_map_al = new ArrayList<>(mapCurrency.keySet());
        int hash_map_size = hash_map_al.size();
        String[] combo_box_options = new String[hash_map_size];
        for (int i = 0; i < hash_map_size; i++) {
            combo_box_options[i] = hash_map_al.get(i);
        }
        Arrays.sort(combo_box_options);


        // FRAME
        Image icon = Toolkit.getDefaultToolkit().getImage(imgPathWindowIcon);
        int frameWidth = 810;
        int frameHeight = 530;
        frame.setIconImage(icon);
        frame.setTitle("XChange");
        frame.setLayout(null);
        frame.setBounds(700,500,frameWidth,frameHeight);
        frame.getContentPane().setBackground(new java.awt.Color(243, 243, 243));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(frameBGColor);


        insertField.setFont(insertFieldFontStyle);
        insertField.setHorizontalAlignment(JTextField.RIGHT);
        insertField.setText("1");
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



        comboBoxUnitTo = new JComboBox<>(combo_box_options);
        comboBoxUnitTo.setSelectedIndex(Functions.lastUsedCurrToIndex);
        comboBoxUnitTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLastUsedCurrs();
                Functions.saveLastUsedCurrenciesJson();
                Functions.generateRatesFromNodes();
                updateResultAction();
                updateHistoricRateLabels();
                updateHistoricArrows();
                updateChart();
            }
        });
            
        comboBoxUnitFrom = new JComboBox<>(combo_box_options);
        comboBoxUnitFrom.setSelectedIndex(Functions.lastUsedCurrFromIndex);
        comboBoxUnitFrom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLastUsedCurrs();
                Functions.saveLastUsedCurrenciesJson();
                Functions.generateRatesFromNodes();
                updateResultAction();
                updateHistoricRateLabels();
                updateHistoricArrows();
                updateChart();
            }
        });


        resultLabel.setFont(resultValueFontStyle);
        resultLabel.setForeground(appFontColor);
        updateResultAction();

        apiUrlLabel.setText("https://www.exchangerate-api.com");
        apiUrlLabel.setForeground(apiUrlLabelColor);


        int widgetWidth = 180;
        int historicDataWidth = 210;
        int widgetHeight = 30;
        int xBase = 40;
        int yBase = 40;

        int insertFieldWidth = 130;
        int xUnitFromCB = xBase + insertFieldWidth + 10;
        int xUnitToCB = xBase + (insertFieldWidth + 10) *2 + 50;


        insertField.setBounds(xBase, yBase, insertFieldWidth, widgetHeight + 2);
        comboBoxUnitFrom.setBounds(xUnitFromCB, yBase, widgetWidth,widgetHeight);
        comboBoxUnitTo.setBounds(xUnitToCB, yBase, widgetWidth,widgetHeight);
        resultLabel.setBounds(xBase + 2, yBase+widgetHeight+5, frameWidth-xBase*2, widgetHeight);
        apiUrlLabel.setBounds(xBase-20, frameHeight-70, 300, widgetHeight);

        Component[] widgets_array = {
                resultLabel,
            insertField,
                comboBoxUnitFrom,
                comboBoxUnitTo,
            apiUrlLabel
        };

        for (Component widget : widgets_array) {
           frame.add(widget);
        }


    
        for (int i = 0; i<historicJsonAmount; i++) {

            int gap = 41;
            int gapArrow = 105;
            int gapRate = 125;
            double rate = 1 / Functions.ratesFrom[i] * Functions.ratesTo[i];
            historicRates[i] = rate;

            historicDateLabel = new JLabel(Functions.timeLastUpdateUtc[i]);
            historicDateLabel.setFont(historicCurrFontStyle);
            historicDateLabel.setForeground(appFontColor);
            historicDateLabel.setBounds(xBase-3, 130 + gap*i, historicDataWidth, widgetHeight);
            frame.add(historicDateLabel);

            historicRateLabel[i] = new JLabel(generateHistoricRateToDisplay(rate));
            historicRateLabel[i].setFont(historicCurrFontStyle);
            historicRateLabel[i].setForeground(appFontColor);
            historicRateLabel[i].setBounds(xBase+gapRate, 130 + gap*i, historicDataWidth, widgetHeight);
            frame.add(historicRateLabel[i]);

            historicImgArrow[i] = new JLabel();
            historicImgArrow[i].setBounds(xBase+gapArrow, 130 + gap*i, widgetWidth, widgetHeight);
            frame.add(historicImgArrow[i]);
        }

        addHistoricRatesToChart();
        updateHistoricArrows();
        // no comparison indicator(arrow image) for the last rate
        historicImgArrow[historicJsonAmount-1].setIcon(iconHyphen);
        frame.setVisible(true);
    }


    static void addHistoricRatesToChart() {
        String rowKey = Functions.lastUsedCurrTo + " / " + Functions.lastUsedCurrFrom;
        for (int i = historicJsonAmount-1; i>-1; i--) {
            dataset.addValue(historicRates[i], rowKey, Functions.timeLastUpdateUtcChart[i]);
        }
    }


    static void updateChart() {
        dataset.clear();
        addHistoricRatesToChart();
        chart.fireChartChanged();
    }


    static void updateHistoricArrows() {

        for (int i = 0; i<historicRates.length-1; i++) {
            if (historicRates[i] < historicRates[i + 1]) {
                historicImgArrow[i].setIcon(iconDown);
            }
            else if ((historicRates[i] > historicRates[i + 1])) {
                historicImgArrow[i].setIcon(iconUp);
            }
            else {
                historicImgArrow[i].setIcon(iconHyphen);
            }
        }
    }


    static void updateLastUsedCurrs() {
        selectedComboBoxFrom = comboBoxUnitFrom.getSelectedItem().toString();
        selectedComboBoxTo = comboBoxUnitTo.getSelectedItem().toString();
        Functions.lastUsedCurrFrom = mapCurrency.get(selectedComboBoxFrom);
        Functions.lastUsedCurrTo = mapCurrency.get(selectedComboBoxTo);
        Functions.lastUsedCurrFromIndex = comboBoxUnitFrom.getSelectedIndex();
        Functions.lastUsedCurrToIndex = comboBoxUnitTo.getSelectedIndex();
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
            return customFormat("###,###.000", rate);
        }
        else if (rate > 0.000_001) {
            return customFormat("#.########", rate);
        }
        else {
            return decFormat.format(rate);
        }
    }


    static void updateHistoricRateLabels() {

        for (int i = 0; i<historicJsonAmount; i++) {

            double rate = 1 / Functions.ratesFrom[i] * Functions.ratesTo[i];
            historicRates[i] = rate;
            historicRateLabel[i].setText(generateHistoricRateToDisplay(rate));
        }
    }


    static void updateResultAction() {

       insertFieldText = insertField.getText().trim();

        if (!insertFieldText.isEmpty()) {

            fieldValueFrom = null;

            try {
                fieldValueFrom = abs(Double.parseDouble(insertFieldText));
            } catch (NumberFormatException e) {
                System.out.println("Invalid value!");
            }

            if (fieldValueFrom != null && fieldValueFrom != 0) {
                resultValueRaw = fieldValueFrom / Functions.ratesFrom[0] * Functions.ratesTo[0];
                resultLabel.setText(generateResultValueToDisplay(resultValueRaw) + "  " + Functions.lastUsedCurrTo);
            } else {
                resultLabel.setText("0.0" + "  " + Functions.lastUsedCurrTo);
            }
        }
        else {
            resultLabel.setText("0.0  " + Functions.lastUsedCurrTo);
        }
        }
    }

