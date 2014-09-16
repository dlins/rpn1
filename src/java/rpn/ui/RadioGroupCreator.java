/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import rpn.configuration.Configuration;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;


public class RadioGroupCreator extends UIComponentCreator {

    private ButtonGroup buttonGroup_;
    private JRadioButton forwardCheckBox_;
    private JRadioButton backwardCheckBox_;
    private JRadioButton insideDomainCheckBox_;
    private JRadioButton outsideDomainCheckBox_;
    private JRadioButton wholeDomainCheckBox_;
        

    
    private int currentOrbitDirection_;


    public RadioGroupCreator(Configuration configuration_, String configurationParameter) {
        super(configuration_, configurationParameter);

    }

    @Override
    public JComponent createUIComponent() {

        buttonGroup_ = new ButtonGroup();
        if (configurationParameter_.equals("direction")) {

            return fillButtonGroup();

        }
        if (configurationParameter_.equals("domain")){
            return fillDomainButtonGroup();
        }
        
        
        
        
        CheckBoxCreator checkBoxCreator = new CheckBoxCreator(configuration_, configurationParameter_);

        return checkBoxCreator.createUIComponent();

    }
    
    
     private JPanel fillDomainButtonGroup() {


        JPanel domainPanel = new JPanel();
        domainPanel.setLayout(new GridLayout(3,1));


        insideDomainCheckBox_ = new JRadioButton("Inside region");
        insideDomainCheckBox_.addActionListener(new DomainSelectionListener());
        insideDomainCheckBox_.setEnabled(true);
        
        
        outsideDomainCheckBox_ = new JRadioButton("Outside region");
        outsideDomainCheckBox_.addActionListener(new DomainSelectionListener());
        outsideDomainCheckBox_.setEnabled(true);

        
        wholeDomainCheckBox_ = new JRadioButton("Whole domain");
        wholeDomainCheckBox_.addActionListener(new DomainSelectionListener());
        wholeDomainCheckBox_.setSelected(true);//Default         
        wholeDomainCheckBox_.setEnabled(true);


        buttonGroup_.add(insideDomainCheckBox_);
        buttonGroup_.add(outsideDomainCheckBox_);
        buttonGroup_.add(wholeDomainCheckBox_);

        
        domainPanel.add(insideDomainCheckBox_);
        domainPanel.add(outsideDomainCheckBox_);
        domainPanel.add(wholeDomainCheckBox_);


        return domainPanel;


    }
    
    
    

    private JPanel fillButtonGroup() {


        JPanel directionPanel = new JPanel();

        forwardCheckBox_ = new JRadioButton("Forward");
        forwardCheckBox_.setSelected(true);//Default 

        forwardCheckBox_.setEnabled(true);

        forwardCheckBox_.addActionListener(new OrbitDirectionListener());
        forwardCheckBox_.setText("Forward");

        backwardCheckBox_ = new JRadioButton("Backward");

        backwardCheckBox_.setEnabled(true);
        backwardCheckBox_.addActionListener(new OrbitDirectionListener());

        backwardCheckBox_.setText("Backward");

        buttonGroup_.add(forwardCheckBox_);
        buttonGroup_.add(backwardCheckBox_);

        directionPanel.add(forwardCheckBox_);
        directionPanel.add(backwardCheckBox_);


        return directionPanel;


    }

    private class OrbitDirectionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (forwardCheckBox_.isSelected()) {
                currentOrbitDirection_ = Orbit.FORWARD_DIR;
            } else {
                currentOrbitDirection_ = Orbit.BACKWARD_DIR;
            }

            RPNUMERICS.setParamValue(configuration_.getName(), "direction", String.valueOf(currentOrbitDirection_));

        }
    }
    
     private class DomainSelectionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            
            int domain=1;
            
            if (insideDomainCheckBox_.isSelected()) {
                domain =0;
            } else if (outsideDomainCheckBox_.isSelected()){
                domain=-1;
            }else if (wholeDomainCheckBox_.isSelected()){
                domain=1;
            }

            RPNUMERICS.setParamValue("extensioncurve", "domain", String.valueOf(domain));
        }
     }
}
