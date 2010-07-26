/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import rpn.parser.ConfigurationProfile;
import rpn.parser.RPnInterfaceParser;
import rpnumerics.Configuration;
import rpnumerics.FluxParams;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class RPnFluxParamsPanel extends JPanel {

    private GridBagLayout gridLayout = new GridBagLayout();
    private ArrayList<JTextField> valuesArray_ = new ArrayList<JTextField>();
    private ArrayList<JSlider> sliderArray_ = new ArrayList<JSlider>();
    private ConfigurationProfile physicsProfile_;
    private DecimalFormat formatter_ = new DecimalFormat("0.000");
    private int index;

    public RPnFluxParamsPanel() {

        searchPhysics(RPNUMERICS.physicsID());
        buildPanel(false);
    }

    public RPnFluxParamsPanel(String physicsName) {
        searchPhysics(physicsName);
        buildPanel(true);
    }

    private void buildPanel(boolean useDefaults) {
        this.setLayout(gridLayout);

        GridBagConstraints gridConstraints = new GridBagConstraints();

        gridConstraints.gridwidth = 3;
        gridConstraints.gridheight = 12;
        HashMap<String, String> fluxParamsArrayList = null;

        gridConstraints.ipadx = 40;
        if (useDefaults) {
            ConfigurationProfile physicsConfiguration = RPnConfig.getConfigurationProfile("QuadraticR2");
            fluxParamsArrayList = physicsConfiguration.getParams();
        } else {
            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsProfile_.getName());
            fluxParamsArrayList = physicsConfiguration.getParams();
        }
        int i = 0;
        Set<Entry<String, String>> fluxParamsSet = fluxParamsArrayList.entrySet();

        Iterator<Entry<String, String>> paramsIterator = fluxParamsSet.iterator();

        while (paramsIterator.hasNext()) {
            Entry<String, String> entry = paramsIterator.next();
            JLabel fluxParamName = new JLabel(entry.getKey());
            fluxParamName.setName(entry.getKey());
            JFormattedTextField fluxValueField = new JFormattedTextField(formatter_);
            JSlider slider = new JSlider(SwingConstants.HORIZONTAL, -100, +100, 0);
            Double paramValue;
            valuesArray_.add(i, fluxValueField);
            sliderArray_.add(i, slider);
            if (useDefaults) {
                paramValue = new Double(entry.getValue());
                fluxValueField.setText(formatter_.format(paramValue));

            } else {
                FluxParams fluxParam = RPNUMERICS.getFluxParams();
                paramValue = fluxParam.getElement(i);
                valuesArray_.get(i).setText(formatter_.format(paramValue));

            }

            slider.setName(String.valueOf(i));
            fluxValueField.setName(String.valueOf(i));

            slider.addChangeListener(new SliderHandler(new Double(paramValue.toString())));
            fluxValueField.getDocument().addDocumentListener(new TextValueHandler(new Double(paramValue.toString())));
            fluxValueField.addFocusListener(new FocusHandler());

            gridConstraints.gridx = 0;

            this.add(fluxParamName, gridConstraints);
            gridConstraints.gridx = 1;
            this.add(fluxValueField, gridConstraints);
            this.add(slider, gridConstraints);
            i++;
        }
    }

    private void searchPhysics(String physicsName) {

        Iterator<ConfigurationProfile> physics = RPnInterfaceParser.getPhysicsProfiles().iterator();

        while (physics.hasNext()) {
            ConfigurationProfile physicsProfile = physics.next();
            if (physicsProfile.getName().equals(physicsName)) {
                physicsProfile_ = physicsProfile;
            }
        }

    }

    public void applyParams() {

        StringBuffer paramsBuffer = new StringBuffer();

        for (int i = 0; i < valuesArray_.size(); i++) {
            JTextField jTextField = valuesArray_.get(i);
            paramsBuffer.append(jTextField.getText());
            paramsBuffer.append(" ");
        }
        FluxParams oldParams = RPNUMERICS.getFluxParams();
        RealVector paramsVector = new RealVector(paramsBuffer.toString());
        FluxParams newParams = new FluxParams(paramsVector);
        RPNUMERICS.setFluxParams(newParams);

        rpn.usecase.ChangeFluxParamsAgent.instance().applyChange(new PropertyChangeEvent(rpn.usecase.ChangeFluxParamsAgent.instance(), "", oldParams, newParams));

    }

    private class SliderHandler implements ChangeListener {

        private Double referenceValue_;

        public SliderHandler(Double referenceValue) {
            referenceValue_ = referenceValue;
        }

        public void stateChanged(ChangeEvent e) {
            if (e.getSource() instanceof JSlider) {
                JSlider slider = (JSlider) e.getSource();
                JTextField textField = valuesArray_.get(new Integer(slider.getName()));
                Double paramValue = referenceValue_ + (new Double(slider.getValue()) / 100);
                textField.setText(formatter_.format(paramValue));
                applyParams();

            }
        }
    }

    private class FocusHandler implements FocusListener {

        public void focusGained(FocusEvent e) {
            JTextField text = (JTextField) e.getSource();
            index = new Integer(text.getName());
        }

        public void focusLost(FocusEvent e) {
        }
    }

    private class TextValueHandler implements DocumentListener {

        private Double referenceValue_;

        public TextValueHandler(Double referenceValue) {
            referenceValue_ = referenceValue;
        }

        public void insertUpdate(DocumentEvent e) {

            Document doc = (Document) e.getDocument();
            System.out.println("atualizando");
            JSlider slider = sliderArray_.get(index);
            if (doc.getLength() != 0) {
                try {

                    Double paramValue = new Double(doc.getText(0, doc.getLength()));

                    ChangeListener[] changeListener = slider.getChangeListeners();

                    slider.removeChangeListener(changeListener[0]);

                    slider.setValue(sliderPosition(paramValue));

                    slider.addChangeListener(new SliderHandler(paramValue));


                } catch (BadLocationException ex) {
                    System.out.println("BadLocationException");
                } catch (NumberFormatException ex) {
                    System.out.println("NumberException");
                }

            }
        }

        public void removeUpdate(DocumentEvent e) {
//            System.out.println("Removendo");
        }

        public void changedUpdate(DocumentEvent e) {
            System.out.println("Trocando");
        }

        private int sliderPosition(Double paramValue) {
            return (int) ((paramValue - referenceValue_) * 100);
        }
    }
}
