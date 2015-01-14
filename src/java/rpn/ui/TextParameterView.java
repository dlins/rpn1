/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import rpn.configuration.Parameter;

public class TextParameterView extends ParameterView {

    private JFormattedTextField textField_;
    private DocumentListener listener_;

    public TextParameterView(Parameter parameter) {
        super(parameter);
        
        textField_ = new JFormattedTextField();

        textField_.setText(parameter.getValue());

        textField_.setColumns(8);
        
        listener_= new TextValueHandler();

        textField_.getDocument().addDocumentListener(listener_);


    }
   
    @Override
    public void associatedChanged(Parameter associatedParameter, Object arg) {

    }

    @Override
    public JComponent getComponent() {
        return textField_;
    }

    @Override
    protected void decorate(JComponent component) {
         JPanel panel_ = (JPanel) component;
         panel_.add(new JLabel(getParameter().getName()));
         
         
    }

    @Override
    public void changeView(Object obj) {
   
    }

    

    private class TextValueHandler implements DocumentListener {

        public void insertUpdate(DocumentEvent arg0) {


            Document doc = (Document) arg0.getDocument();

            try {
                String newValue = doc.getText(0, doc.getLength());
                getParameter().setValue(newValue);

            } catch (BadLocationException ex) {
                System.out.println("Excessao BadLocation" + ex.getMessage());
            } catch (NumberFormatException ex) {
                System.out.println("Excessao NumberFormat " + ex.getMessage());
            }

        }

        public void removeUpdate(DocumentEvent arg0) {
            
//            System.out.println("Evento remove");
        }

        public void changedUpdate(DocumentEvent arg0) {
//            System.out.println("Evento update");
        }
    }
}
