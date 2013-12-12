/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import rpn.configuration.Configuration;
import rpn.parser.RPnDataModule;
import rpnumerics.RPNUMERICS;
import wave.util.Boundary;
import wave.util.RealVector;

public class SpinButtonCreator extends UIComponentCreator {

    private JSpinner[] resolutionSpinnerArray_;
    private String parameterName_;

    public SpinButtonCreator(Configuration configuration_, String parameterName) {
        super(configuration_, parameterName);
        parameterName_ = parameterName;
    }

    @Override
    public JComponent createUIComponent() {

        JPanel panel_ = new JPanel();

//        GridBagConstraints gridConstraints = new GridBagConstraints();
//
//        gridConstraints.fill = GridBagConstraints.BOTH;
//
//        gridConstraints.gridwidth = 1;
//        gridConstraints.gridheight = 1;
//        gridConstraints.ipadx = 50;
//        gridConstraints.gridy = 0;
//        gridConstraints.gridx = 0;

        GridLayout gridBayLayout = new GridLayout(configuration_.getParamsSize(), 1);

        panel_.setLayout(gridBayLayout);

//        gridConstraints.gridx = 0;

        if (parameterName_.contains("family")) {


            panel_.add(spinButtonCreator(parameterName_, configuration_.getParam(parameterName_)));

            return panel_;


        } else if (parameterName_.contains("resolution")) {

            panel_.add(spinButtonResolutionCreator(parameterName_, configuration_.getParam(parameterName_)));

            return panel_;
        }


        RadioGroupCreator radioCreator = new RadioGroupCreator(configuration_, parameterName_);

        return radioCreator.createUIComponent();
    }

    private JPanel spinButtonCreator(String paramName, String paramValue) {
        GridBagConstraints gridConstraints = new GridBagConstraints();
        GridBagLayout gridBayLayout = new GridBagLayout();

        JPanel familyPanel = new JPanel(gridBayLayout);

        Integer family = new Integer(paramValue);

        SpinnerNumberModel spinerModel = new SpinnerNumberModel(family.intValue(), 0, RPNUMERICS.domainDim() - 1, 1);

        JSpinner familySpinner = new JSpinner(spinerModel);

        ((DefaultEditor) familySpinner.getEditor()).getTextField().setEditable(false);

        familySpinner.addChangeListener(new FamilyChangeHandler(paramName));

        familySpinner.setValue(new Integer(paramValue));

        JLabel familyLabel = new JLabel(paramName);

        gridConstraints.anchor= GridBagConstraints.PAGE_END;
        gridConstraints.gridx = 0;
        gridConstraints.ipadx = 10;
        

        familyPanel.add(familyLabel, gridConstraints);


        gridConstraints.gridx = 1;
        

        familyPanel.add(familySpinner, gridConstraints);


        return familyPanel;


    }

    private JPanel spinButtonResolutionCreator(String paramName, String paramValue) {
        GridBagConstraints gridConstraints = new GridBagConstraints();
        GridBagLayout gridBayLayout = new GridBagLayout();

        JPanel familyPanel = new JPanel(gridBayLayout);
        int[] resolution = RPnDataModule.processResolution(paramValue);

        gridConstraints.gridx = 0;
        resolutionSpinnerArray_ = new JSpinner[resolution.length];
        JLabel familyLabel = new JLabel(paramName);
        gridConstraints.ipadx = 20;

        familyPanel.add(familyLabel, gridConstraints);
        for (int i = 0; i < resolutionSpinnerArray_.length; i++) {

            SpinnerNumberModel spinerModel = new SpinnerNumberModel();

            spinerModel.setMinimum(new Integer(1));
            spinerModel.setStepSize(new Integer(1));
            spinerModel.setValue(resolution[i]);

            resolutionSpinnerArray_[i] = new JSpinner(spinerModel);

            ((DefaultEditor) resolutionSpinnerArray_[i].getEditor()).getTextField().setEditable(true);
            ((DefaultEditor) resolutionSpinnerArray_[i].getEditor()).getTextField().setInputVerifier(new ResolutionFormatVerifier());
            ((DefaultEditor) resolutionSpinnerArray_[i].getEditor()).getTextField().getDocument().addDocumentListener(new ResolutionTextValueHandler(paramName));

            resolutionSpinnerArray_[i].addChangeListener(new ResolutionChangeHandler(paramName));

            gridConstraints.gridx++;
            familyPanel.add(resolutionSpinnerArray_[i], gridConstraints);


        }

        return familyPanel;


    }

    private class ResolutionChangeHandler implements ChangeListener {

        private String paramName_;

        public ResolutionChangeHandler(String paramName_) {
            this.paramName_ = paramName_;
        }

        public void stateChanged(ChangeEvent e) {

            StringBuilder resolution = new StringBuilder();

            for (JSpinner resolutionPart : resolutionSpinnerArray_) {
                resolution.append(resolutionPart.getValue()).append(" ");
            }

            configuration_.setParamValue(paramName_, resolution.toString());

            Boundary boundary = RPNUMERICS.boundary();

            RealVector min = boundary.getMinimums();
            RealVector max = boundary.getMaximums();

            RPNUMERICS.setResolution(min, max, configuration_.getName(), RPnDataModule.processResolution(resolution.toString()));

        }
    }

    private class FamilyChangeHandler implements ChangeListener {

        private String paramName_;

        public FamilyChangeHandler(String paramName_) {
            this.paramName_ = paramName_;
        }

        public void stateChanged(ChangeEvent e) {

            JSpinner spinner = (JSpinner) e.getSource();

            String familyValue = String.valueOf(spinner.getValue());
            configuration_.setParamValue(paramName_, familyValue);
        }
    }

    private class ResolutionTextValueHandler implements DocumentListener {//TODO Implement event methods to enable resolution input by typing

        private String paramName_;

        public ResolutionTextValueHandler(String paramName_) {
            this.paramName_ = paramName_;
        }

        public void insertUpdate(DocumentEvent arg0) {

            StringBuilder resolution = new StringBuilder();

            for (JSpinner resolutionPart : resolutionSpinnerArray_) {

                String resolutionValue = ((DefaultEditor) resolutionPart.getEditor()).getTextField().getText();
                resolution.append(resolutionValue).append(" ");
            }

            configuration_.setParamValue(paramName_, resolution.toString());

            Boundary boundary = RPNUMERICS.boundary();

            RealVector min = boundary.getMinimums();
            RealVector max = boundary.getMaximums();

            RPNUMERICS.setResolution(min, max, configuration_.getName(), RPnDataModule.processResolution(resolution.toString()));


        }

        public void removeUpdate(DocumentEvent arg0) {

            for (JSpinner resolutionPart : resolutionSpinnerArray_) {

                String resolutionValue = ((DefaultEditor) resolutionPart.getEditor()).getTextField().getText();
                if (resolutionValue.isEmpty()) {
                }

            }


        }

        public void changedUpdate(DocumentEvent arg0) {

            StringBuilder resolution = new StringBuilder();

            for (JSpinner resolutionPart : resolutionSpinnerArray_) {

                String resolutionValue = ((DefaultEditor) resolutionPart.getEditor()).getTextField().getText();
                resolution.append(resolutionValue).append(" ");
            }

            configuration_.setParamValue(paramName_, resolution.toString());

            Boundary boundary = RPNUMERICS.boundary();

            RealVector min = boundary.getMinimums();
            RealVector max = boundary.getMaximums();

            RPNUMERICS.setResolution(min, max, configuration_.getName(), RPnDataModule.processResolution(resolution.toString()));

        }
    }

    private class ResolutionFormatVerifier extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            JTextField textField = (JTextField) input;


            String textString = textField.getText();
            System.out.println(textField);

            if (textString.isEmpty()) {
                return false;
            }

            try {
                Integer testeInteger = new Integer(textString);
            } catch (NumberFormatException ex) {

                return false;
            }

            return true;






        }
    }
}
