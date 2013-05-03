/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import rpn.ui.RPnInputComponent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import rpn.parser.RPnDataModule;
import rpn.configuration.Configuration;
import rpn.ui.SpinButtonCreator;
import rpn.ui.TextFieldCreator;
import rpnumerics.RPNUMERICS;
import wave.util.Boundary;
import wave.util.RealVector;

public class RPnConfigurationDialog extends RPnDialog {

    private JTabbedPane extensionPanel_;

    public RPnConfigurationDialog() {
        super(false, false);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @deprecated Usando tres grids . Um para Hugoniot, um para DoubleContact e um para as demais curvas (com resolucao da inflexao)
     * @throws Exception
     *
     */
    private void jbInit() throws Exception {
        setTitle("Configuration");
        extensionPanel_ = new JTabbedPane();
        applyButton.setText("OK");
        extensionPanel_.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        HashMap<String, Configuration> configMap = RPNUMERICS.getConfigurations();

        Set<Entry<String, Configuration>> configSet = configMap.entrySet();


        for (Entry<String, Configuration> entry : configSet) {
            SpinButtonCreator input = new SpinButtonCreator(entry.getValue(),"resolution");
            if (entry.getValue().getName().equals("inflectioncurve")) {

                extensionPanel_.addTab("Bifurcation Curves", input.createUIComponent());
            }

            if (entry.getValue().getName().equals("doublecontactcurve")) {
//                TextFieldCreator input = new TextFieldCreator(entry.getValue(), "resolution");
                extensionPanel_.addTab("DoubleContact and SecondaryBifurcation", input.createUIComponent());
            }

            if (entry.getValue().getName().equals("hugoniotcurve")) {

//                TextFieldCreator input = new TextFieldCreator(entry.getValue(), "resolution");
                extensionPanel_.addTab(entry.getKey(), input.createUIComponent());

            }

        }

        setMinimumSize(new Dimension(getTitle().length() * 50, 40));

        getContentPane().add(extensionPanel_, BorderLayout.CENTER);

        extensionPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply");
        extensionPanel_.getActionMap().put("Apply", applyButton.getAction());


        pack();
    }

    @Override
    protected void cancel() {
        dispose();
    }

    /**
     * @deprecated A resolucao esta sendo setada individualmente para Hugoniot, DoubleContact e outras curvas.
     * A resolucao da inflexao esta sendo usada no calculo das curvas diferentes de Hugoniot e DoubleContact.
     */
    protected void apply() {

        
        dispose();

//        int[] doubleContactResolution = RPnDataModule.processResolution(RPNUMERICS.getParamValue("doublecontactcurve", "resolution"));
//
//        int[] hugoniotResolution = RPnDataModule.processResolution(RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));
//
//        int[] bifurcationCurvesResolution = RPnDataModule.processResolution(RPNUMERICS.getParamValue("inflectioncurve", "resolution"));
//
//        Boundary boundary = RPNUMERICS.boundary();
//
//        RealVector min = boundary.getMinimums();
//        RealVector max = boundary.getMaximums();
//
//        RPNUMERICS.setResolution(min, max, "doublecontactcurve", doubleContactResolution);
//
//        RPNUMERICS.setResolution(min, max, "hugoniotcurve", hugoniotResolution);
//
//        RPNUMERICS.setResolution(min, max, "bifurcation", bifurcationCurvesResolution);
//
//        HashMap<String, Configuration> configMap = RPNUMERICS.getConfigurations();
//
//        Set<Entry<String, Configuration>> configSet = configMap.entrySet();
//
//
//        for (Entry<String, Configuration> entry : configSet) {
//
//            String configurationType = entry.getValue().getType();
//
//            if (configurationType.equalsIgnoreCase("CURVE") && !entry.getValue().getName().equals("hugoniotcurve") && !entry.getValue().getName().equals("doublecontactcurve")) {
//                entry.getValue().setParamValue("resolution", RPNUMERICS.getParamValue("inflectioncurve", "resolution"));
//            }
//        }
//
//        dispose();

    }

    @Override
    protected void begin() {
        dispose();
    }
}
