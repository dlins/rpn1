/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import rpn.configuration.Parameter;
import rpnumerics.Orbit;

public class ChooseParameterView extends ParameterView {

    private ButtonGroup buttonGroup_;
    private JPanel panel_;
    private HashMap<Integer, String> flagMap_;

    public ChooseParameterView(Parameter parameter) {
        super(parameter);
        buttonGroup_ = new ButtonGroup();
        panel_ = new JPanel();

        flagMap_ = new HashMap<Integer, String>();

        flagMap_.put(Orbit.FORWARD_DIR, "Forward");
        flagMap_.put(Orbit.BACKWARD_DIR, "Backward");
        
        

        List<String> options = parameter.getOptions();

        for (String option : options) {

            JRadioButton radionButton = new JRadioButton(option);
            radionButton.setName(option);
            String value = flagMap_.get(Integer.parseInt(parameter.getValue()));

            if (value != null) {
                if (value.equals(option)) {
                    radionButton.setSelected(true);

                }

            }

            radionButton.setEnabled(true);

            radionButton.addActionListener(new OrbitDirectionListener());

            buttonGroup_.add(radionButton);
            panel_.add(radionButton);

        }

    }

    @Override
    public void associatedChanged(Parameter associatedParameter, Object arg) {

    }

    @Override
    public JComponent getComponent() {
        return panel_;
    }

    @Override
    protected  void decorate(JComponent component) {

    }

    @Override
    public void changeView(Object obj) {
        String newValue = (String )obj;
        
        if(flagMap_.containsKey(obj)){
            Enumeration<AbstractButton> elements = buttonGroup_.getElements();
            while (elements.hasMoreElements()) {
                AbstractButton abstractButton = elements.nextElement();
                if(abstractButton.getName().equals(obj))
                    abstractButton.setSelected(true);
                
                
            }
            
        }
        
        
        
        
    }

    private class OrbitDirectionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            JRadioButton button = (JRadioButton)e.getSource();
            
            Set<Integer> keySet = flagMap_.keySet();
            
            for (Integer integer : keySet) {

                String value = flagMap_.get(integer);
                
                if(value.equals(button.getText())){
                    getParameter().setValue(String.valueOf(integer));
                }
                
            }
         
        }
    
    }

}
