/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import rpn.ui.RPnInputComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import rpn.controller.ui.UIController;
import rpn.configuration.Configuration;
import rpnumerics.RPNUMERICS;

public class RPnFluxParamsDialog extends RPnDialog {

    private JPanel subjectParamsPanel_;
    private JPanel fluxParamsPanel_;
    private JPanel radioPanel_ = new JPanel();            //*************
    //****
    private JTabbedPane tabsPanel;
    
    private JPanel paramsPanel_;
    
    private RPnInputComponent fluxParamInputComponent_;
    //****
//    private RPnParamsPanel paramsPanel_;

    public RPnFluxParamsDialog(RPnFluxParamsSubject[] subjectArray, RPnFluxParamsObserver paramObserver) {
        super(true, false);
        
        paramsPanel_ = new JPanel();
//        paramsPanel_.setLayout(ne);//new BoxLayout(paramsPanel_, BoxLayout.X_AXIS));
        setTitle(RPNUMERICS.physicsID());
        beginButton.setText("Close");
        subjectParamsPanel_ = new JPanel();
        fluxParamsPanel_ = new JPanel();

        removeDefaultApplyBehavior();

        tabsPanel = new JTabbedPane();
        tabsPanel.setPreferredSize(new Dimension(400, 600));
        setLocation(400, 400);
      
//        for (RPnFluxParamsSubject subject : subjectArray) {
//            RPnInputComponent inputComponent = null;
//
//            if (subject instanceof RPnFluxConvexCombination) {
//                inputComponent = new RPnInputComponent(subject, "alpha");
//            } else {
//                inputComponent = new RPnInputComponent(subject);
//            }
//
//            tabsPanel.add(inputComponent.getContainer());
//
//            //-----------------------------------------
//            if (subject.getName() == null ? "Radio" != null : !subject.getName().equals("Radio")) {
//                tabsPanel.add(inputComponent.getContainer());
//            }
//
//            //-----------------------------------------
//
//            if (subject.getName().equals("Radio")) {
//                radioPanel_.add(inputComponent.getContainer());
//            }
//
//
//        }




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


        subjectParamsPanel_.getActionMap().put("Apply", beginButton.getAction());
        subjectParamsPanel_.getActionMap().put("Cancel", cancelButton.getAction());

        this.getContentPane().add(subjectParamsPanel_, BorderLayout.CENTER);

        removeDefaultApplyBehavior();

        RPnInputComponent observerInputComponent = new RPnInputComponent(paramObserver.getConfiguration(),true);
        paramObserver.setView(observerInputComponent);

        fluxParamsPanel_.add(observerInputComponent.getContainer());

        this.getContentPane().add(fluxParamsPanel_, BorderLayout.WEST);
        

        
        
       

        
//        double [] testMinLimits = {0,0};
//        
//        double [] testMaxLimits = {1,1};
//        
//        Boundary boundary = new IsoTriang2DBoundary("0 0 0 1 1 0");
//        
//        String [] paramNames = {"grw","grg"};
//        
//        int [] paramsOrientation ={0,1};
//        
//        int [] paramIndex = {0,1};
//        
//        
//        ParamsDescriptor descriptor  = new ParamsDescriptor(paramNames, paramsOrientation, paramIndex,boundary);
//
//        RPnParamsPanel paramsPanel=     new RPnParamsPanel(descriptor);
//        
//        paramsPanel_.add(paramsPanel);
//        
//        this.getContentPane().add(paramsPanel_,BorderLayout.EAST);
        
        
//


        pack();


    }
    
    
   
    
    
    
    
    

    public RPnFluxParamsDialog() {
        super(true, false);

        setTitle(RPNUMERICS.physicsID());
        beginButton.setText("OK");
        fluxParamsPanel_ = new JPanel();

        removeDefaultApplyBehavior();
        
        setLocation(400, 400);


        Configuration physicsConfiguration = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());
        fluxParamInputComponent_ = new RPnInputComponent(physicsConfiguration.getConfiguration("fluxfunction"),true);
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
        
        Configuration physicsConfiguration = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());

        RPNUMERICS.applyFluxParams();

        String phaseSpaceName = UIController.instance().getActivePhaseSpace().getName();
        
        
        
        rpn.command.ChangeFluxParamsCommand.instance().applyChange(new PropertyChangeEvent(rpn.command.ChangeFluxParamsCommand.instance(),phaseSpaceName,
        physicsConfiguration,physicsConfiguration));
        rpn.command.ChangeFluxParamsCommand.instance().updatePhaseDiagram();
    }

    @Override
    protected void begin() {

        dispose();
    }
    //----------------------------------------------------------
}
