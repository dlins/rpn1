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
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import rpn.configuration.Configuration;

public class TextFieldCreator extends UIComponentCreator {

    private JFormattedTextField textField_;

    public TextFieldCreator(Configuration configuration, RPnInputComponent inputComponent) {
        super(configuration, inputComponent);
        textField_ = new JFormattedTextField();
    }

    public TextFieldCreator(Configuration configuration, String configurationParameter) {
        super(configuration, configurationParameter);
        textField_ = new JFormattedTextField();

    }

    @Override
    public JComponent createUIComponent() {

        JPanel panel_ = new JPanel();

        GridBagConstraints gridConstraints = new GridBagConstraints();

        gridConstraints.fill = GridBagConstraints.BOTH;

        gridConstraints.gridwidth = 1;
        gridConstraints.gridheight = 1;
        gridConstraints.ipadx = 50;
        gridConstraints.gridy = 0;
        gridConstraints.gridx = 0;

        GridLayout gridBayLayout = new GridLayout(configuration_.getParamsSize(), 1);

        panel_.setLayout(gridBayLayout);


        gridConstraints.gridx = 0;
        panel_.add(createTextField(configurationParameter_, configuration_.getParam(configurationParameter_)));


        gridConstraints.gridy++;


        return panel_;

    }

    private JPanel createTextField(String paramName, String paramValue) {


        System.out.println(paramName + " " + paramValue);

        JFormattedTextField textField = new JFormattedTextField();
        GridBagConstraints gridConstraints = new GridBagConstraints();
        GridBagLayout gridBayLayout = new GridBagLayout();

        JPanel textPanel = new JPanel(gridBayLayout);


        textField.setText(paramValue);

        textField.setColumns(8);

        textField_.add(textField);

        textField.getDocument().addDocumentListener(new TextValueHandler());

        JLabel label = new JLabel(paramName);

        gridConstraints.gridx = 0;
        textPanel.add(label, gridConstraints);
        gridConstraints.gridx = 1;
        textPanel.add(textField, gridConstraints);

        return textPanel;

    }

    private class TextValueHandler implements DocumentListener {




        public void insertUpdate(DocumentEvent arg0) {


            Document doc = (Document) arg0.getDocument();

            try {
                String newValue = doc.getText(0, doc.getLength());
                configuration_.setParamValue(configurationParameter_, newValue);



            } catch (BadLocationException ex) {
                System.out.println("Excessao BadLocation" + ex.getMessage());
            } catch (NumberFormatException ex) {
                System.out.println("Excessao NumberFormat " + ex.getMessage());
            }

        }

        public void removeUpdate(DocumentEvent arg0) {
            
            System.out.println("Evento remove");
        }

        public void changedUpdate(DocumentEvent arg0) {
            System.out.println("Evento update");
        }
    }
}
