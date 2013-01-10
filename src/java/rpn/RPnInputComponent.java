/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import rpn.command.ChangeCurveConfigurationCommand;
import rpnumerics.Configuration;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

//**** tirar os listeners, que serao enviados para classes especializadas
//**** basicamente, cada tipo de campo deverá ser tratado por uma classe especialista, munida de um listener
public class RPnInputComponent {//TODO Refatorar

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
    private RPnObserverController observerController_;
//    private RPnObserverController observerController_;
    private String parameterName_;
    //CONSTS
    public static final String NUMERIC_VALUE = "NUMBER_VALUE";
    public static final String DOUBLE_FORMAT = "INTEGERTYPE";
    public static final String INTEGER_FORMAT = "DOUBLETYPE";
    private static final String FORMAT_TYPE = "FORMAT_TYPE";
    //RADIO BUTTON INDEX
    public static int rb = -1;

    public RPnInputComponent(RPnSubject subject) {

        // desfazer a associacao com grupo
        JRadioButton[] option = new JRadioButton[3];
        ButtonGroup group = new ButtonGroup();
        option[0] = new JRadioButton("Horizontal");
        option[1] = new JRadioButton("Vertical");
        option[2] = new JRadioButton("Mixed");

        textField_ = new JFormattedTextField[subject.getParamsNames().length];

        label_ = new JLabel[subject.getParamsNames().length];

        slider_ = new JSlider(0, 1, 0);



        observerController_ = new RPnObserverController(this, subject);
        GridBagConstraints gridConstraints = new GridBagConstraints();

        gridConstraints.fill = GridBagConstraints.BOTH;

        gridConstraints.gridwidth = 1;
        gridConstraints.gridheight = 1;
        gridConstraints.ipadx = 10;
        gridConstraints.gridy = 0;
        gridConstraints.gridx = 0;

        GridBagLayout gridBayLayout = new GridBagLayout();

        panel_.setLayout(gridBayLayout);
        panel_.setName(subject.getName());


        if (subject.getName() == null ? "Radio" != null : !subject.getName().equals("Radio")) {   //*** Leandro teste (introduzi o if)

            for (int i = 0; i < subject.getParamsNames().length; i++) {         //********* Fazer tratamento se o campo for vazio, para preservar os formatos


                JFormattedTextField textField = new JFormattedTextField(formatter_);

                textField.setColumns(4);
                textField_[i] = textField;

                textField.addFocusListener(new TextFocusListener());

                textField.setName(subject.getParamsNames()[i]);
                textField.getDocument().addDocumentListener(new TextObserverValueHandler());


                JLabel labelName = new JLabel(subject.getParamsNames()[i]);

                if (subject.getParamsNames()[i].equals("lambda")) {
                    labelName = new JLabel("  \u03BB");
                }

                label_[i] = labelName;

                if (i == (subject.getParamsNames().length) / 2) {
                    gridConstraints.gridy = 0;
                }

                if (i < (subject.getParamsNames().length) / 2) {
                    gridConstraints.gridx = 0;
                    panel_.add(label_[i], gridConstraints);
                    panel_.add(textField_[i], gridConstraints);
                    gridConstraints.gridx = 1;
                    panel_.add(textField_[i], gridConstraints);
                    gridConstraints.gridy++;
                } else {
                    gridConstraints.gridx = 2;
                    panel_.add(label_[i], gridConstraints);
                    panel_.add(textField_[i], gridConstraints);
                    gridConstraints.gridx = 3;
                    panel_.add(textField_[i], gridConstraints);
                    gridConstraints.gridy++;
                }



                //gridConstraints.gridy++;


            }

        }


        if (subject.getName().equals("Radio")) {
            if (RPNUMERICS.physicsID().equals("Stone")) {
                for (int i = 0; i < option.length; i++) {
                    option[i].addActionListener(new ListenerRadioButton());
                    group.add(option[i]);
                    panel_.add(option[i]);
                }

            }

        }



        //*** deverá ser retirado. Tratamentos assim serao feitos a partir do throws (RPnSubject)
        if (subject.getName().equals("Corey") && RPNUMERICS.physicsID().equals("QuadraticR2")) {
            panel_.add(new JLabel("A > 0 , B > 0 , A + B < 1"));
        }
        //***



    }

    public RPnInputComponent(RPnSubject subject, String sliderName) {

        // desfazer a associacao com grupo
        JRadioButton[] option = new JRadioButton[3];
        ButtonGroup group = new ButtonGroup();
        option[0] = new JRadioButton("Horizontal");
        option[1] = new JRadioButton("Vertical");
        option[2] = new JRadioButton("Mixed");

        textField_ = new JFormattedTextField[subject.getParamsNames().length];

        label_ = new JLabel[subject.getParamsNames().length];

        slider_ = new JSlider(0, 20, 0);

        slider_.setSnapToTicks(false);


//        Hashtable<String, Integer> hashLabel = new Hashtable<String, Integer>();
//
//
//        hashLabel.put("0", 0);
//        hashLabel.put("20", 1);


//        slider_.setLabelTable(hashLabel);

        slider_.setPaintTicks(true);


        slider_.setMinorTickSpacing(1);

        slider_.setMajorTickSpacing(10);

        slider_.setName(sliderName);

        slider_.addChangeListener(new SliderHandler());


        observerController_ = new RPnObserverController(this, subject);
        GridBagConstraints gridConstraints = new GridBagConstraints();

        gridConstraints.fill = GridBagConstraints.BOTH;

        gridConstraints.gridwidth = 1;
        gridConstraints.gridheight = 1;
        gridConstraints.ipadx = 10;
        gridConstraints.gridy = 0;
        gridConstraints.gridx = 0;

        GridBagLayout gridBayLayout = new GridBagLayout();

        panel_.setLayout(gridBayLayout);
        panel_.setName(subject.getName());


        if (subject.getName() == null ? "Radio" != null : !subject.getName().equals("Radio")) {   //*** Leandro teste (introduzi o if)

            for (int i = 0; i < subject.getParamsNames().length; i++) {         //********* Fazer tratamento se o campo for vazio, para preservar os formatos

                JFormattedTextField textField = new JFormattedTextField(formatter_);


                textField.setColumns(4);
                textField_[i] = textField;

                textField.addFocusListener(new TextFocusListener());

                textField.setName(subject.getParamsNames()[i]);
                textField.getDocument().addDocumentListener(new TextObserverValueHandler());


                JLabel labelName = new JLabel(subject.getParamsNames()[i]);

                if (subject.getParamsNames()[i].equals("lambda")) {
                    labelName = new JLabel("  \u03BB");
                }

                label_[i] = labelName;

                if (i == (subject.getParamsNames().length) / 2) {
                    gridConstraints.gridy = 0;
                }

                if (i < (subject.getParamsNames().length) / 2) {
                    gridConstraints.gridx = 0;
                    panel_.add(label_[i], gridConstraints);
                    panel_.add(textField_[i], gridConstraints);
                    gridConstraints.gridx = 1;
                    panel_.add(textField_[i], gridConstraints);
                    gridConstraints.gridy++;
                } else {
                    gridConstraints.gridx = 2;
                    panel_.add(label_[i], gridConstraints);
                    panel_.add(textField_[i], gridConstraints);
                    gridConstraints.gridx = 3;
                    panel_.add(textField_[i], gridConstraints);
                    gridConstraints.gridy++;
                }



                //gridConstraints.gridy++;


            }

        }

        gridConstraints.gridy++;
//        panel_.add(new JLabel(sliderName),gridConstraints);
        panel_.add(slider_, gridConstraints);

        if (subject.getName().equals("Radio")) {
            if (RPNUMERICS.physicsID().equals("Stone")) {
                for (int i = 0; i < option.length; i++) {
                    option[i].addActionListener(new ListenerRadioButton());
                    group.add(option[i]);
                    panel_.add(option[i]);
                }

            }

        }



        //*** deverá ser retirado. Tratamentos assim serao feitos a partir do throws (RPnSubject)
        if (subject.getName().equals("Corey") && RPNUMERICS.physicsID().equals("QuadraticR2")) {
            panel_.add(new JLabel("A > 0 , B > 0 , A + B < 1"));
        }
        //***



    }

    public RPnInputComponent(Configuration configuration, boolean useEvents) {

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

        int j = 0;

        HashMap<String, String> paramsValues = configuration.getParams();

        Set<Entry<String, String>> paramsSet = paramsValues.entrySet();

        for (Entry<String, String> value : paramsSet) {


            //JFormattedTextField textField = new JFormattedTextField(formatter_);

            JFormattedTextField textField = new JFormattedTextField();

            textField.setText(configuration.getParam(j));

            textField.setColumns(8);

            textField_[j] = textField;
            textField.setName(configuration.getParamName(j));

            if (useEvents) {
                textField.getDocument().addDocumentListener(new TextValueHandler());
            }

            JLabel label = new JLabel(configuration.getParamName(j));

            label_[j] = label;

            gridConstraints.gridx = 0;
            panel_.add(label, gridConstraints);
            gridConstraints.gridx = 1;
            panel_.add(textField, gridConstraints);

            gridConstraints.gridy++;

            j++;


        }

        controller_ = new RPnInputController(this, configuration);


    }

    public void applyConfigurationChange() {

        String[] newValues = new String[textField_.length];

        for (int j = 0; j < textField_.length; j++) {

            parameterName_ = textField_[j].getName();


            newValues[j] = textField_[j].getText();

        }

        controller_.propertyChange(
                new PropertyChangeEvent(this, parameterName_, newValues, newValues));


        ChangeCurveConfigurationCommand.instance().applyChange(new PropertyChangeEvent(this, parameterName_, null, controller_.getConfiguration()));

    }

    public void removeParameter(String parameterName) {

        for (int i = 0; i < textField_.length; i++) {
            JFormattedTextField jFormattedTextField = textField_[i];

            if (jFormattedTextField.getName().equals(parameterName)) {
                panel_.remove(jFormattedTextField);
                panel_.remove(label_[i]);
            }


        }


    }

    public void keepParameter(String parameterName) {

        for (int i = 0; i < textField_.length; i++) {
            JFormattedTextField jFormattedTextField = textField_[i];

            if (!jFormattedTextField.getName().equals(parameterName)) {
                panel_.remove(jFormattedTextField);
                panel_.remove(label_[i]);
            }


        }


    }

    public JFormattedTextField[] getTextField() {
        return textField_;
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

    private class SliderHandler implements ChangeListener {

        public void stateChanged(ChangeEvent e) {

            JSlider slider = (JSlider) e.getSource();
            String[] newValues = new String[textField_.length];


            Double sliderValue = new Double(slider.getValue());
            for (int j = 0; j < textField_.length; j++) {

                if (textField_[j].getName().equals(slider.getName())) {

                    sliderValue /= 20;
                    textField_[j].setText(String.valueOf(sliderValue));

                }

                newValues[j] = textField_[j].getText();
            }





            Double alpha = new Double(textField_[textField_.length - 1].getText());

            System.out.println("Valor de alpha: " + alpha + " Slider value: " + sliderValue);

            if (sliderValue != alpha) {
                observerController_.propertyChange(new PropertyChangeEvent(this, slider.getName(), newValues, newValues));

                RPNUMERICS.applyFluxParams();
                rpn.command.ChangeFluxParamsCommand.instance().applyChange(new PropertyChangeEvent(rpn.command.ChangeFluxParamsCommand.instance(), "", null, RPNUMERICS.getFluxParams().getParams()));

                rpn.command.ChangeFluxParamsCommand.instance().updatePhaseDiagram();

            }






        }
    }

    private class TextValueHandler implements DocumentListener {

        public void insertUpdate(DocumentEvent arg0) {



//            RealVector newValues = new RealVector(textField_.length);
            String[] newValues = new String[textField_.length];
//            double doubleNewValue;

            Document doc = (Document) arg0.getDocument();

            //RealVector newValues = new RealVector(textField_.length);

            //double doubleNewValue;


            try {
                for (int j = 0; j < textField_.length; j++) {

                    if (textField_[j].getDocument() == doc) {
                        parameterName_ = textField_[j].getName();
                        String newValue = doc.getText(0, doc.getLength());

                        newValues[j] = newValue;

                    } else {
                        //doubleNewValue = new Double(textField_[j].getText());
                        //newValues.setElement(j, doubleNewValue);

                        newValues[j] = textField_[j].getText();

                    }


                    controller_.propertyChange(new PropertyChangeEvent(this, parameterName_, newValues, newValues));
                }
            } catch (BadLocationException ex) {
                //System.out.println("Excessao Bad" + ex.getMessage());
            } catch (NumberFormatException ex) {
                System.out.println("Excessao NumberFormat " + ex.getMessage());
            }

        }

        public void removeUpdate(DocumentEvent arg0) {
        }

        public void changedUpdate(DocumentEvent arg0) {
        }
    }

    private class TextObserverValueHandler implements DocumentListener {

        public void insertUpdate(DocumentEvent arg0) {
            Document doc = (Document) arg0.getDocument();


            RealVector newValues = new RealVector(textField_.length);
            double doubleNewValue;

            try {

                for (int j = 0; j < textField_.length; j++) {

                    if (textField_[j].getDocument() == doc) {
                        String newValue = doc.getText(0, doc.getLength());
                        doubleNewValue = new Double(newValue);
                        newValues.setElement(j, doubleNewValue);
                    } else {
                        doubleNewValue = new Double(textField_[j].getText());
                        newValues.setElement(j, doubleNewValue);
                    }


                }

                // chamado qdo subject fica completo (comecando com textFields vazios...)
//                observerController_.propertyChange(new PropertyChangeEvent(this, "fazendo teste", null, RPnFluxParamsSubject.realVectorToStringArray(newValues)));

                // chamado qdo subject fica completo (comecando com textFields vazios...)
                observerController_.propertyChange(new PropertyChangeEvent(this, "fazendo teste", null, RPnFluxParamsSubject.realVectorToStringArray(newValues)));

            } catch (BadLocationException ex) {
                //System.out.println("Excessao Bad" + ex.getMessage());
            } catch (NumberFormatException ex) {
                //System.out.println("Excessao NumberFormat " + ex.getMessage());
            }


        }

        public void removeUpdate(DocumentEvent arg0) {
        }

        public void changedUpdate(DocumentEvent arg0) {
        }
    }

    private class TextFocusListener implements FocusListener {

        public void focusGained(FocusEvent e) {
            System.out.println("Ganhou foco : " + e.getComponent().getName());

            for (int i = 0; i < textField_.length; i++) {
                textField_[i].setForeground(Color.black);
            }

        }

        public void focusLost(FocusEvent e) {

            for (int i = 0; i < textField_.length; i++) {
                textField_[i].setForeground(Color.lightGray);

            }

        }
    }

    private class ListenerRadioButton implements ActionListener {

        public void actionPerformed(ActionEvent e) {


            System.out.println("Clicou num RadioButton : " + e.getActionCommand());

            if (e.getActionCommand().equals("Horizontal")) {
                rb = 0;
            }
            if (e.getActionCommand().equals("Vertical")) {
                rb = 1;
            }

            if (e.getActionCommand().equals("Mixed")) {
                rb = 2;
            }
            //System.out.println("Valor de rb : " +rb);
            RealVector newValues = new RealVector(3);      //*** ESTE SERÄ O BOOLEANO

            observerController_.propertyChange(new PropertyChangeEvent(this, "fazendo teste", null, RPnFluxParamsSubject.realVectorToStringArray(newValues)));

        }
    }
}
