/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import javax.swing.AbstractAction;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class RPnInputComponent extends AbstractAction {

    private JPanel panel_;
    private JSlider slider_;
    private JFormattedTextField textField_;
    private JLabel label_;
    private DecimalFormat formatter_;
    private DecimalFormat doubleFormatter_ = new DecimalFormat("0.000");
    private DecimalFormat integerFormatter_ = new DecimalFormat("0");
    private double maxRange_;
    private double minRange_;
    private Object value_;
    //CONSTS


    public static final String NUMERIC_VALUE = "NUMBER_VALUE";
    public static final String DOUBLE_FORMAT = "INTEGERTYPE";
    public static final String INTEGER_FORMAT = "DOUBLETYPE";


    private static final String FORMAT_TYPE = "FORMAT_TYPE";

    public RPnInputComponent(double value) {

        formatter_=doubleFormatter_;
        panel_ = new JPanel(new GridLayout(1, 3));
        value_ = value;
        textField_ = new JFormattedTextField(formatter_);
        slider_ = new JSlider(-100, 100);

        textField_.getDocument().addDocumentListener(new TextValueHandler());
        slider_.addChangeListener(new SliderHandler());

        maxRange_ = 1;
        minRange_ = -1;

        label_ = new JLabel();
        label_.setText("Label");

        panel_.add(label_, 0, 0);
        panel_.add(textField_, 0, 1);
        panel_.add(slider_, 0, 2);

        putValue(NUMERIC_VALUE, value);
    }

    public RPnInputComponent(String label, double value) {
        this(value);
        setLabel(label);
    }

    public RPnInputComponent(String label) {
        this(0);
        setLabel(label);
    }

    public void setRelativeRange(int min, int max) {

        slider_.setMinimum(min);
        slider_.setMaximum(max);

    }

    public void setNumericFormat(String numericFormat){

        firePropertyChange(FORMAT_TYPE,"", numericFormat);


    }

    public void setDisplayColumns(int col) {
        textField_.setColumns(col);
    }

    public void setLabel(String caption) {
        label_.setText(caption);
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

    public void setValue(Double value) {
        textField_.setText(formatter_.format(value));

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
                Double value = new Double(doc.getText(0, doc.getLength()));
                ChangeListener changeListener[] = slider_.getChangeListeners();
                slider_.removeChangeListener(changeListener[0]);
                slider_.setValue(setSliderPosition(value));
                slider_.addChangeListener(changeListener[0]);
                value_ = value;
                putValue(NUMERIC_VALUE, value);
//                firePropertyChange(NUMERIC_VALUE, value_, value);
            } catch (BadLocationException ex) {
                System.out.println("Excessao Bad" + ex.getMessage());
            } catch (NumberFormatException ex) {
                System.out.println("Excessao NumberFormat " + ex.getMessage());
            }

        }

        public void removeUpdate(DocumentEvent arg0) {
            //            insertUpdate(arg0);
        }

        public void changedUpdate(DocumentEvent arg0) {
//            insertUpdate(arg0);
        }
    }

    private class SliderHandler implements ChangeListener {

        public void stateChanged(ChangeEvent arg0) {

            JSlider slider = (JSlider) arg0.getSource();
            Double newValue = setValue(slider.getValue());


//            textField_.setText(formatter_.format(new Double(newValue)));

            textField_.setText(formatter_.format(slider.getValue()));

            if (!slider.getValueIsAdjusting()) {
                putValue(NUMERIC_VALUE, newValue);
            }
        }
    }

    @Override
    public Object getValue(String key) {

        if (key.equals(NUMERIC_VALUE)) {
            return formatter_.format(value_);
        }

        return null;


    }

    @Override
    public void putValue(String key, Object value) {

        if (key.equals(NUMERIC_VALUE)) {
            firePropertyChange(NUMERIC_VALUE, value_, value);
        }


    }

    @Override
    protected void firePropertyChange(String propName, Object oldValue, Object newValue) {

        if (propName.equalsIgnoreCase(NUMERIC_VALUE)) {
                value_ =  newValue;
        }


        if (propName.equalsIgnoreCase(FORMAT_TYPE)) {

            if (newValue.equals(RPnInputComponent.INTEGER_FORMAT)){

                integerFormatter_.setMaximumFractionDigits(0);
                formatter_ = integerFormatter_;

            }

            if (newValue.equals(RPnInputComponent.DOUBLE_FORMAT)){
                doubleFormatter_.setMaximumFractionDigits(3);
                formatter_ = doubleFormatter_;
            }

            textField_.setText(formatter_.format(value_));
        }


        super.firePropertyChange(propName, oldValue, newValue);
    }

    @Override
    public void setEnabled(boolean en) {

        if (!en) {

            slider_.setEnabled(false);
            textField_.setEditable(false);

            return;


        }

        slider_.setEnabled(true);
        textField_.setEnabled(true);

    }

    public void actionPerformed(ActionEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet.");

    }
}
