/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import rpn.component.util.ControlClick;
import rpnumerics.Configuration;
import rpnumerics.FluxParams;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;

public class RPnFluxParamsDialog extends RPnDialog {

    private JPanel subjectParamsPanel_;
    private JPanel fluxParamsPanel_;
    private JPanel radioPanel_ = new JPanel();            //*************
    //****
    private JTabbedPane tabsPanel;
    private RPnInputComponent fluxParamInputComponent_;
    //****

    public RPnFluxParamsDialog(RPnFluxParamsSubject[] subjectArray, RPnFluxParamsObserver paramObserver) {
        super(true, false);

        setTitle(RPNUMERICS.physicsID());
        beginButton.setText("Close");
        subjectParamsPanel_ = new JPanel();
        fluxParamsPanel_ = new JPanel();
    
        removeDefaultApplyBehavior();

//<<<<<<< HEAD
        tabsPanel = new JTabbedPane();
        tabsPanel.setPreferredSize(new Dimension(400, 250));

        
        //*****
//        JRadioButton[] option = new JRadioButton[3];
//        ButtonGroup group = new ButtonGroup();
//        option[0] = new JRadioButton("Horizontal");
//        option[1] = new JRadioButton("Vertical");
//        option[2] = new JRadioButton("Mixed");
        //*****
        
        
        for (RPnFluxParamsSubject subject : subjectArray) {

            RPnInputComponent inputComponent = new RPnInputComponent(subject);

            //tabsPanel.add(inputComponent.getContainer());

            //-----------------------------------------
            if (subject.getName() == null ? "Radio" != null : !subject.getName().equals("Radio")) {
                tabsPanel.add(inputComponent.getContainer());
            }

//            if (subject.getName().equals("Radio")) {
//                if (RPNUMERICS.physicsID().equals("Stone")) {
//                    for (int i = 0; i < option.length; i++) {
//                        option[i].addActionListener(new ListenerRadioButton());
//                        group.add(option[i]);
//
//                        subjectParamsPanel_.add(inputComponent.getContainer());
//                        subjectParamsPanel_.add(option[i]);
//                    }
//
//                }
//
//            }
            //-----------------------------------------

            if (subject.getName().equals("Radio")) {radioPanel_.add(inputComponent.getContainer());}


        }

        
        //------------
        ChangeListener changeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent changeEvent) {

                tabsPanel.requestFocus(false);
                int index = tabsPanel.getSelectedIndex();
                System.out.println("Aba ativa : " + tabsPanel.getTitleAt(index));

            }
        };


        tabsPanel.addChangeListener(changeListener);
        
        //------------

        subjectParamsPanel_.add(tabsPanel);
        subjectParamsPanel_.add(radioPanel_);
        
        subjectParamsPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply");

        subjectParamsPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
//=======
//        Configuration physicsConfiguration = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());
//
//        Configuration fluxFunctionConfig = physicsConfiguration.getConfiguration("fluxfunction");
//
//
//        RPnInputComponent inputComponent = new RPnInputComponent(fluxFunctionConfig);
//
//        paramsPanel_.add(inputComponent.getContainer());
//>>>>>>> ff35486a0245d1e571f55dc8f9605ff5cf138d9c

        subjectParamsPanel_.getActionMap().put("Apply", beginButton.getAction());
        subjectParamsPanel_.getActionMap().put("Cancel", cancelButton.getAction());

        this.getContentPane().add(subjectParamsPanel_, BorderLayout.CENTER);

        removeDefaultApplyBehavior();

        RPnInputComponent observerInputComponent = new RPnInputComponent(paramObserver.getConfiguration());
        paramObserver.setView(observerInputComponent);

        fluxParamsPanel_.add(observerInputComponent.getContainer());

        this.getContentPane().add(fluxParamsPanel_, BorderLayout.WEST);
        
        pack();


    }

    public RPnFluxParamsDialog() {
        super(true, false);
        
        setTitle(RPNUMERICS.physicsID());
        beginButton.setText("OK");
        fluxParamsPanel_ = new JPanel();

        removeDefaultApplyBehavior();


        Configuration physicsConfiguration = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());
        fluxParamInputComponent_ = new RPnInputComponent(physicsConfiguration.getConfiguration("fluxfunction"));
        fluxParamsPanel_.add(fluxParamInputComponent_.getContainer());


        fluxParamsPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply");

        fluxParamsPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");

        fluxParamsPanel_.getActionMap().put("Apply", beginButton.getAction());
        fluxParamsPanel_.getActionMap().put("Cancel", cancelButton.getAction());

        this.getContentPane().add(fluxParamsPanel_, BorderLayout.CENTER);

        pack();
        
    }

    @Override
    protected void apply() {

        ControlClick.clearAllStrings();
        
        Configuration physicsConfiguration = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());
        Configuration fluxConfiguration = physicsConfiguration.getConfiguration("fluxfunction");
        //System.out.println("Novos parametros no apply: " + fluxConfiguration.getParamVector());


        RPNUMERICS.applyFluxParams();

        rpn.usecase.ChangeFluxParamsAgent.instance().applyChange(new PropertyChangeEvent(rpn.usecase.ChangeFluxParamsAgent.instance(), "", null, RPNUMERICS.getFluxParams()));

//        System.out.println("Chamando apply do flux dialog");

    }

    @Override
    protected void begin() {

        dispose();
    }

    //----------------------------------------------------------
    
    private class TabFocusListener implements FocusListener {

        public void focusGained(FocusEvent e) {
            System.out.println("Ganhou foco na aba");
            //tabsPanel.addChangeListener(changeListener);
        }

        public void focusLost(FocusEvent e) {
            System.out.println("Perdeu foco da aba");

        }
    }

    //----------------------------------------------------------


//    private class ListenerRadioButton implements ActionListener {
//
//        public void actionPerformed(ActionEvent e) {
//            System.out.println("Clicou num RadioButton : " +e.getActionCommand());
//
//            if (e.getActionCommand().equals("Horizontal")) RPnInputComponent.rb = 0;
//            if (e.getActionCommand().equals("Vertical")) RPnInputComponent.rb = 1;
//            if (e.getActionCommand().equals("Mixed")) RPnInputComponent.rb = 2;
//
//            System.out.println("Valor de rb : " +RPnInputComponent.rb);
//
//        }
//
//    }

}
