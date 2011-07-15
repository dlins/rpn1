/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.batik.ext.swing.GridBagConstants;
import rpnumerics.Configuration;

public class RPnInputComponent {

    private JPanel panel_ = new JPanel();
    private JSlider slider_;
    private JFormattedTextField[] textField_;
    private JLabel[] label_;
    private DecimalFormat formatter_;
    private DecimalFormat doubleFormatter_ = new DecimalFormat("0.000");
    private DecimalFormat integerFormatter_ = new DecimalFormat("0");
    private double maxRange_;
    private double minRange_;
    private RPnInputController controller_;
    private String parameterName_;
    //CONSTS
    public static final String NUMERIC_VALUE = "NUMBER_VALUE";
    public static final String DOUBLE_FORMAT = "INTEGERTYPE";
    public static final String INTEGER_FORMAT = "DOUBLETYPE";
    private static final String FORMAT_TYPE = "FORMAT_TYPE";

    public RPnInputComponent(Configuration configuration) {

        textField_ = new JFormattedTextField[configuration.getParamsSize()];

        label_ = new JLabel[configuration.getParamsSize()];

        GridBagConstraints gridConstraints = new GridBagConstraints();

        gridConstraints.fill = GridBagConstraints.BOTH;

        gridConstraints.gridwidth = 1;
        gridConstraints.gridheight = 1;
        gridConstraints.ipadx = 50;
        gridConstraints.gridy = 0;

        gridConstraints.gridx = 0;

        GridBagLayout gridBayLayout = new GridBagLayout();



        
        panel_.setLayout(gridBayLayout);

        HashMap<String, String> params = configuration.getParams();

        Set<Entry<String, String>> paramsSet = params.entrySet();
        int i = 0;

        for (Entry<String, String> entry : paramsSet) {


            String value = entry.getValue();
            if (value.contains(".")) {

                formatter_ = doubleFormatter_;
            } else {
                formatter_ = integerFormatter_;
            }


            JFormattedTextField textField = new JFormattedTextField(formatter_);

            textField.setText(entry.getValue());
            textField.setColumns(4);

            textField_[i] = textField;
            textField.setName(entry.getKey());
            textField.addFocusListener(new FocusHandler());
            textField.getDocument().addDocumentListener(new TextValueHandler());


            JLabel label = new JLabel(entry.getKey());

            label_[i] = label;

            gridConstraints.gridx = 0;
            panel_.add(label, gridConstraints);
            gridConstraints.gridx = 1;
            panel_.add(textField,gridConstraints);


            gridConstraints.gridy++;

            i++;

        }

        controller_ = new RPnInputController(this, configuration);

    }

    public void setRelativeRange(int min, int max) {

        slider_.setMinimum(min);
        slider_.setMaximum(max);

    }

    public JPanel getContainer() {
        return panel_;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange_ = maxRange;
    }

    public void setMinRange(double minRange) {
        this.minRange_ = minRange;
    }

    public void removeSlider() {
        panel_.remove(slider_);
    }

    private Double setValue(int sliderPosition) {
        double deltaValue = (maxRange_ - minRange_);
        int deltaSlider = slider_.getMaximum() - slider_.getMinimum();
        Double x = new Double(sliderPosition - slider_.getMinimum());
        return (((x / (deltaSlider)) * deltaValue) + minRange_);
    }

    private int setSliderPosition(double value) {
        double deltaValue = (maxRange_ - minRange_);
        int deltaSlider = slider_.getMaximum() - slider_.getMinimum();
        return (int) ((((value - minRange_) / (deltaValue)) * deltaSlider) + slider_.getMinimum());
    }

    private class TextValueHandler implements DocumentListener {

        public void insertUpdate(DocumentEvent arg0) {
            Document doc = (Document) arg0.getDocument();
            try {

                String newValue = doc.getText(0, doc.getLength());
                controller_.propertyChange(new PropertyChangeEvent(this, parameterName_, newValue, newValue));
            } catch (BadLocationException ex) {
                System.out.println("Excessao Bad" + ex.getMessage());
            } catch (NumberFormatException ex) {
                System.out.println("Excessao NumberFormat " + ex.getMessage());
            }

        }

        public void removeUpdate(DocumentEvent arg0) {
        }

        public void changedUpdate(DocumentEvent arg0) {
        }
    }

    private class SliderHandler implements ChangeListener {

        public void stateChanged(ChangeEvent arg0) {

            JSlider slider = (JSlider) arg0.getSource();
            Double newValue = setValue(slider.getValue());


//            textField_.setText(formatter_.format(new Double(newValue)));

//            textField_.setText(formatter_.format(slider.getValue()));

            if (!slider.getValueIsAdjusting()) {
//                putValue(NUMERIC_VALUE, newValue);
            }
        }
    }

    private class FocusHandler implements FocusListener {

        public void focusGained(FocusEvent e) {

            JTextField textField = (JTextField) e.getSource();
            parameterName_ = textField.getName();
        }

        public void focusLost(FocusEvent e) {
        }
    }
}
