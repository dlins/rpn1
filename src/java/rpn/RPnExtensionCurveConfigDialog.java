/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import rpnumerics.RPNUMERICS;

public class RPnExtensionCurveConfigDialog extends RPnDialog {

    private JPanel paramsPanel_ = new JPanel();
    private JPanel newtonTolerancePanel_ = new JPanel();
    private JTabbedPane extensionPanel_;
    private HashMap<String, RPnInputComponent> inputHash_;
    private RPnContourConfigPanel contourConfigPanel_;
    private String[] cDomain_;
    private JTextField toleranceTextField_;

    public RPnExtensionCurveConfigDialog() {
        super(false, true);

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readValues() {

        inputHash_.get("characteristicdomain").setValue(new Double(RPNUMERICS.getParamValue("boundaryextensioncurve", "characteristicdomain")));
        inputHash_.get("curvefamily").setValue(new Double(RPNUMERICS.getParamValue("boundaryextensioncurve", "curvefamily")));
        inputHash_.get("domainfamily").setValue(new Double(RPNUMERICS.getParamValue("boundaryextensioncurve", "domainfamily")));
        inputHash_.get("singular").setValue(new Double(RPNUMERICS.getParamValue("bifurcation", "singular")));
        inputHash_.get("edge").setValue(new Double(RPNUMERICS.getParamValue("boundaryextensioncurve", "edge")));


        toleranceTextField_.setText(RPNUMERICS.getParamValue("Newton", "tolerance"));

    }

    private void jbInit() throws Exception {
        inputHash_ = new HashMap<String, RPnInputComponent>();
        setTitle("Configuration");
        extensionPanel_ = new JTabbedPane();

        toleranceTextField_ = new JTextField();
        toleranceTextField_.setColumns(5);
        //Resolution Tab
        contourConfigPanel_ = new RPnContourConfigPanel();
        extensionPanel_.addTab("Resolution", contourConfigPanel_);


        //Boundary Tab


        GridLayout gridLayout = new GridLayout(5, 1, 10, 10);

        paramsPanel_.setLayout(gridLayout);

        extensionPanel_.addTab("Family & Characteristic", paramsPanel_);

        extensionPanel_.addChangeListener(new TabListener());

        cDomain_ = new String[2];


        cDomain_[0] = "CHARACTERISTIC ON CURVE";
        cDomain_[1] = "CHARACTERISTIC ON DOMAIN";

        RPnInputComponent characteristicInput = new RPnInputComponent("Characteristic Where");

        characteristicInput.setMinRange(0);
        characteristicInput.setMaxRange(1);
        characteristicInput.setRelativeRange(0, 1);

        inputHash_.put("characteristicdomain", characteristicInput);

        characteristicInput.setNumericFormat(RPnInputComponent.INTEGER_FORMAT);

        RPnInputComponent singularInput = new RPnInputComponent("Singular");

        singularInput.setNumericFormat(RPnInputComponent.INTEGER_FORMAT);

        singularInput.setMinRange(0);
        singularInput.setMaxRange(1);
        singularInput.setRelativeRange(0, 1);


        inputHash_.put("singular", singularInput);


        RPnInputComponent domainFamilyInput = new RPnInputComponent("Domain Family");
        domainFamilyInput.setNumericFormat(RPnInputComponent.INTEGER_FORMAT);

        domainFamilyInput.setMinRange(0);
        domainFamilyInput.setMaxRange(1);
        domainFamilyInput.setRelativeRange(0, 1);

        inputHash_.put("domainfamily", domainFamilyInput);


        RPnInputComponent curveFamilyInput = new RPnInputComponent("Curve Family");
        curveFamilyInput.setNumericFormat(RPnInputComponent.INTEGER_FORMAT);

        curveFamilyInput.setMinRange(0);
        curveFamilyInput.setMaxRange(1);
        curveFamilyInput.setRelativeRange(0, 1);

        inputHash_.put("curvefamily", curveFamilyInput);


        RPnInputComponent edgeInput = new RPnInputComponent("Edge");
        edgeInput.setNumericFormat(RPnInputComponent.INTEGER_FORMAT);

        edgeInput.setMinRange(0);
        edgeInput.setMaxRange(1);
        edgeInput.setRelativeRange(0, 1);

        inputHash_.put("edge", edgeInput);


        paramsPanel_.add(characteristicInput.getContainer());
        paramsPanel_.add(singularInput.getContainer());
        paramsPanel_.add(domainFamilyInput.getContainer());
        paramsPanel_.add(curveFamilyInput.getContainer());
        paramsPanel_.add(edgeInput.getContainer());


        //Newton tolerance

        extensionPanel_.addTab("Newton Method Tolerance", newtonTolerancePanel_);
        newtonTolerancePanel_.add(toleranceTextField_);


        setMinimumSize(new Dimension(getTitle().length() * 10, 40));

        getContentPane().add(extensionPanel_, BorderLayout.CENTER);

        extensionPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply");

        extensionPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");

        extensionPanel_.getActionMap().put("Apply", applyButton.getAction());
        extensionPanel_.getActionMap().put("Cancel", cancelButton.getAction());


        readValues();

        pack();
    }

    @Override
    protected void cancel() {
        dispose();
    }

    protected void apply() {


        RPNUMERICS.setParamValue("boundaryextensioncurve", "curvefamily", String.valueOf(inputHash_.get("curvefamily").getValue(RPnInputComponent.NUMERIC_VALUE)));
        RPNUMERICS.setParamValue("boundaryextensioncurve", "domainfamily", String.valueOf(inputHash_.get("domainfamily").getValue(RPnInputComponent.NUMERIC_VALUE)));
        RPNUMERICS.setParamValue("boundaryextensioncurve", "characteristicdomain", String.valueOf(inputHash_.get("characteristicdomain").getValue(RPnInputComponent.NUMERIC_VALUE)));


        RPNUMERICS.setParamValue("bifurcation", "singular", String.valueOf(inputHash_.get("singular").getValue(RPnInputComponent.NUMERIC_VALUE)));
        RPNUMERICS.setParamValue("boundaryextensioncurve", "edge", String.valueOf(inputHash_.get("edge").getValue(RPnInputComponent.NUMERIC_VALUE)));


        RPNUMERICS.setParamValue("Newton", "tolerance", toleranceTextField_.getText());



        contourConfigPanel_.apply();
//
//        setVisible(false);
//        dispose();

    }

    @Override
    protected void begin() {
        System.out.println("Chamando begin do extension config");
        dispose();
    }

    private class TabListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            readValues();
        }
    }
}
