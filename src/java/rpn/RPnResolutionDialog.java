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
import rpn.configuration.Configuration;
import rpn.ui.SpinButtonCreator;
import rpnumerics.RPNUMERICS;

public class RPnResolutionDialog extends RPnDialog {

    private JTabbedPane extensionPanel_;

    public RPnResolutionDialog() {
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
                extensionPanel_.addTab("DoubleContact and SecondaryBifurcation", input.createUIComponent());
            }

            if (entry.getValue().getName().equals("hugoniotcurve")) {
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


    }

    @Override
    protected void begin() {
        dispose();
    }
}
