/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.plugininterface;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import rpn.RPnDialog;

public class PluginConfigDialog extends RPnDialog {

    private JPanel paramsPanel_,  titlePanel_;
    private String pluginType_;
    private String currentParamsEdit_;
    private PluginProfile tempPluginProfile_;

    public PluginConfigDialog(String pluginType) {
        super(false, true);

        pluginType_ = pluginType;

        BorderLayout dialogLayout = (BorderLayout) this.getContentPane().getLayout();

        dialogLayout.setVgap(30);

        PluginProfile pluginProfile = PluginTableModel.getPluginConfig(pluginType);

        tempPluginProfile_ = new PluginProfile(pluginProfile);

        String[] paramsNames = pluginProfile.getParamsName();
        String[] paramsValues = pluginProfile.getParamsDefValues();


        GridBagLayout paramsPanelLayout = new GridBagLayout();
        paramsPanel_ = new JPanel(paramsPanelLayout);
        GridBagConstraints layoutConstraints = new GridBagConstraints();

        paramsPanelLayout.setConstraints(paramsPanel_, layoutConstraints);

        titlePanel_ = new JPanel(new BorderLayout());

        JLabel pluginTypeLabel = new JLabel(pluginType, JLabel.CENTER);


        titlePanel_.add(pluginTypeLabel, BorderLayout.CENTER);

        for (int i = 0; i < paramsNames.length; i++) {

            JLabel paramName = new JLabel(paramsNames[i] + " :");
            JTextField paramValue = new JTextField(paramsValues[i]);
            paramValue.setName(paramsNames[i]);
            paramValue.addFocusListener(new ParamValueFocusListener());
            paramValue.getDocument().addDocumentListener(new TextFieldActionlistener());
            paramValue.setColumns(paramValue.getText().length() + 5);
            layoutConstraints.ipadx = 10;
            paramsPanel_.add(paramName, layoutConstraints);
            paramsPanel_.add(paramValue);

        }

        this.getContentPane().add(paramsPanel_, BorderLayout.CENTER);
        this.getContentPane().add(titlePanel_, BorderLayout.NORTH);

        pack();


    }

    @Override
    protected void apply() {
        PluginTableModel.setPluginConfig(pluginType_, tempPluginProfile_);
    }

    private class ParamValueFocusListener implements FocusListener {

        public void focusGained(FocusEvent e) {
            JComponent source = (JComponent) e.getComponent();
            currentParamsEdit_ = source.getName();
        }

        public void focusLost(FocusEvent e) {
        }
    }

    private class TextFieldActionlistener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {

            String newValue = null;
            try {
                newValue = e.getDocument().getText(0, e.getDocument().getLength());
                tempPluginProfile_.setPluginParm(currentParamsEdit_, newValue);
            } catch (BadLocationException ex) {
                ex.printStackTrace();

            }
        }

        public void removeUpdate(DocumentEvent e) {
        }

        public void changedUpdate(DocumentEvent e) {
        }
    }

    @Override
    protected void begin() {

    }
}
