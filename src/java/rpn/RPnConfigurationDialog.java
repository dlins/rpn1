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
import java.util.Map.Entry;
import java.util.Set;
import rpn.parser.RPnDataModule;
import rpnumerics.Configuration;
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

//    private void jbInit() throws Exception {
//        setTitle("Configuration");
//        extensionPanel_ = new JTabbedPane();
//        applyButton.setText("OK");
//        extensionPanel_.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
//        HashMap<String, Configuration> configMap = RPNUMERICS.getConfigurations();
//
//        Set<Entry<String, Configuration>> configSet = configMap.entrySet();
//
//
//        for (Entry<String, Configuration> entry : configSet) {
//
//            String configurationType = entry.getValue().getType();
//
//            if (!configurationType.equalsIgnoreCase("PHYSICS") && !configurationType.equalsIgnoreCase("VISUAL")) {
//
//                RPnInputComponent inputComponent = new RPnInputComponent(entry.getValue());
//
//                inputComponent.keepParameter("resolution");
//                if (inputComponent.getContainer().getComponentCount() > 0)
//                extensionPanel_.addTab(entry.getKey(), inputComponent.getContainer());
//
//            }
//
//        }
//
//        setMinimumSize(new Dimension(getTitle().length() * 50, 40));
//
//        getContentPane().add(extensionPanel_, BorderLayout.CENTER);
//
//        extensionPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply");
//        extensionPanel_.getActionMap().put("Apply", applyButton.getAction());
//
//
//        pack();
//    }
    /**
     * @deprecated Modificar quando cada curva tiver a sua resolucao
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

        JComboBox stateComboBox = RPnUIFrame.copyComboBox;


        for (Entry<String, Configuration> entry : configSet) {

            String configurationType = entry.getValue().getType();
            RPnInputComponent inputComponent = new RPnInputComponent(entry.getValue());
            inputComponent.keepParameter("resolution");
//            if (!configurationType.equalsIgnoreCase("PHYSICS") && !configurationType.equalsIgnoreCase("VISUAL") && entry.getValue().getName().equals("hugoniotcurve")) {
            if (configurationType.equalsIgnoreCase("CURVE") && entry.getValue().getName().equals("hugoniotcurve")) {


                if (inputComponent.getContainer().getComponentCount() > 0) {
                    extensionPanel_.addTab("resolution", inputComponent.getContainer());
                }

            }

            //if (entry.getValue().getName().equals("doublecontactcurve")) {
            if (entry.getValue().getName().equals("doublecontactcurve")  &&  !stateComboBox.getSelectedItem().equals("Phase Diagram")) {
            
                if (inputComponent.getContainer().getComponentCount() > 0) {
                    extensionPanel_.addTab(entry.getKey(), inputComponent.getContainer());
                }


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
     * @deprecated Modificar quando cada curva tiver a sua resolucao
     */
    protected void apply() {


        int[] resolution = RPnDataModule.processResolution(RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));

        Boundary boundary = RPNUMERICS.boundary();

        RealVector min = boundary.getMinimums();
        RealVector max = boundary.getMaximums();

        RPNUMERICS.setResolution(min, max, resolution);

        HashMap<String, Configuration> configMap = RPNUMERICS.getConfigurations();

        Set<Entry<String, Configuration>> configSet = configMap.entrySet();


        for (Entry<String, Configuration> entry : configSet) {

            String configurationType = entry.getValue().getType();

            if (configurationType.equalsIgnoreCase("CURVE") && !entry.getValue().getName().equals("hugoniotcurve")) {
                entry.getValue().setParamValue("resolution", RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));
            }
        }

        dispose();

    }

    @Override
    protected void begin() {
        dispose();
    }
}
