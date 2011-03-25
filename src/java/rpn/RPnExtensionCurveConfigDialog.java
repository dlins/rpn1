/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import rpnumerics.RPNUMERICS;

public class RPnExtensionCurveConfigDialog extends RPnDialog {

    private JPanel paramsPanel_ = new JPanel();
    private JTabbedPane extensionPanel_;
    private JTextField[] textFieldsArray_;
//    private JComboBox caracteristicdomainCombo_;
    private RPnContourConfigPanel contourConfigPanel_;
    private JLabel characteristicDomainLabel_;
    private JComboBox characteristicDomainCombo_;
    private JSpinner curveFamilySpinner_;
    private JSpinner domainFamilySpinner_;
    private JLabel curveFamilyLabel_;
    private JLabel domainFamilyLabel_;
    private String[] cDomain_;
    private Integer curveFamily_;
    private Integer domainFamily_;
    private int characteristicWhere_;

    public RPnExtensionCurveConfigDialog() {
        super(false, true);




        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readValues() {

        curveFamilySpinner_.setValue(new Integer(RPNUMERICS.getParamValue("boundaryextensioncurve", "curvefamily")));

        domainFamilySpinner_.setValue(new Integer(RPNUMERICS.getParamValue("boundaryextensioncurve", "domainfamily")));

        Integer characteristicWhere = new Integer(RPNUMERICS.getParamValue("boundaryextensioncurve", "characteristicdomain"));

        System.out.println("carc where:" + characteristicWhere);

        characteristicDomainCombo_.setSelectedItem(cDomain_[characteristicWhere]);


    }

    private void jbInit() throws Exception {
        setTitle("Bifurcation Curve Configuration");
        extensionPanel_ = new JTabbedPane();
        //Resolution Tab
        contourConfigPanel_ = new RPnContourConfigPanel();

        extensionPanel_.addTab("Resolution", contourConfigPanel_);


        //Boundary Tab

        extensionPanel_.addTab("Boundary Extension", paramsPanel_);

        extensionPanel_.addChangeListener(new TabListener());

        characteristicDomainLabel_ = new JLabel("Caracteristic Where");

        cDomain_ = new String[2];


        cDomain_[0] = "CHARACTERISTIC ON CURVE";
        cDomain_[1] = "CHARACTERISTIC ON DOMAIN";


        characteristicDomainCombo_ = new JComboBox(cDomain_);

        characteristicDomainCombo_.addActionListener(new CharacteristicListener());


        GridLayout gridLayout = new GridLayout(3, 3, 10, 10);

        paramsPanel_.setLayout(gridLayout);


        paramsPanel_.add(characteristicDomainLabel_);
        paramsPanel_.add(characteristicDomainCombo_);


        curveFamilySpinner_ = new JSpinner(new SpinnerNumberModel(0, 0, RPNUMERICS.domainDim(), 1));
        domainFamilySpinner_ = new JSpinner(new SpinnerNumberModel(0, 0, RPNUMERICS.domainDim(), 1));

        curveFamilySpinner_.addChangeListener(new FamilyListener());
        domainFamilySpinner_.addChangeListener(new FamilyListener());


        curveFamilyLabel_ = new JLabel("Curve Family");
        domainFamilyLabel_ = new JLabel("Domain Family");


        paramsPanel_.add(curveFamilyLabel_);
        paramsPanel_.add(curveFamilySpinner_);

        paramsPanel_.add(domainFamilyLabel_);
        paramsPanel_.add(domainFamilySpinner_);

//
//        ContourConfiguration contourConfiguration = RPNUMERICS.getContourConfiguration();
//
//        HashMap<String, String> paramsMap = contourConfiguration.getParams();
//
//        textFieldsArray_ = new JTextField[paramsMap.size()];
//
//        Set<Entry<String, String>> paramsSet = paramsMap.entrySet();
//
//        Iterator<Entry<String, String>> paramsIterator = paramsSet.iterator();
//        int i = 0;
//        while (paramsIterator.hasNext()) {
//
//            Entry<String, String> paramsEntry = paramsIterator.next();
//            JLabel label = new JLabel(paramsEntry.getKey());
//            JTextField textField = new JTextField(paramsEntry.getValue());
//            textFieldsArray_[i] = textField;
//            textField.setName(paramsEntry.getKey());
//            paramsPanel_.add(label);
//            paramsPanel_.add(textField);
//            i++;
//        }

        setMinimumSize(new Dimension(getTitle().length() * 10, 40));
//        this.getContentPane().add(paramsPanel_, BorderLayout.CENTER);

        getContentPane().add(extensionPanel_, BorderLayout.CENTER);


        readValues();

        pack();
    }

    @Override
    protected void cancel() {
        dispose();
    }

    protected void apply() {


        RPNUMERICS.setParamValue("boundaryextensioncurve", "curvefamily", String.valueOf(curveFamilySpinner_.getValue()));
        RPNUMERICS.setParamValue("boundaryextensioncurve", "domainfamily", String.valueOf(domainFamilySpinner_.getValue()));
        RPNUMERICS.setParamValue("boundaryextensioncurve", "characteristicdomain", String.valueOf(characteristicWhere_));

        contourConfigPanel_.apply();

        dispose();

    }

    @Override
    protected void begin() {
    }

    private class CharacteristicListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (characteristicDomainCombo_.getSelectedItem().equals("CHARACTERISTIC ON DOMAIN")) {
                characteristicWhere_ = 1;
            }
            if (characteristicDomainCombo_.getSelectedItem().equals("CHARACTERISTIC ON CURVE")) {
                characteristicWhere_ = 0;
            }
        }
    }

    private class FamilyListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {

            curveFamily_ = (Integer) curveFamilySpinner_.getValue();
            domainFamily_ = (Integer) domainFamilySpinner_.getValue();

        }
    }

    private class TabListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            readValues();
        }
    }
}
