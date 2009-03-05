/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import rpn.parser.PhysicsProfile;
import rpn.parser.RPnInterfaceParser;
import rpnumerics.RPNUMERICS;
import wave.util.IsoTriang2DBoundary;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class RPnConfigDialog extends RPnDialog {

    private FlowLayout flowLayout1 = new FlowLayout();
    private JPanel physicsPanel_ = new JPanel();
    private JPanel boundaryPanel_ = new JPanel();
    private JPanel firstPanel_ = new JPanel(new BorderLayout());
    private JPanel secondPanel_ = new JPanel(new BorderLayout());
    private JTabbedPane tabbedPanel_ = new JTabbedPane();
    private JLabel physicsNameLabel_ = new JLabel();
    private RPnFluxParamsPanel fluxParamPanel_;
    private JPanel visualizationPanel_ = new JPanel();
    private JTextField[] axisTextArray_;
    private ArrayList<JTextField> boundaryTextArray_;
    private ArrayList<PhysicsProfile> profilesArray_;
    private PhysicsProfile physicsProfile_;
    private JComboBox physicsComboBox_;

    public RPnConfigDialog() {
        profilesArray_ = RPnInterfaceParser.getPhysicsProfiles();
        jbInit();
        buildVisualizationPanel();

    }

    private void addPhysicsName() {
        physicsComboBox_ = new JComboBox();
        for (int i = 0; i < profilesArray_.size(); i++) {
            PhysicsProfile profile = profilesArray_.get(i);
            physicsComboBox_.addItem(profile.getName());
        }

        physicsPanel_.setLayout(flowLayout1);
        physicsPanel_.add(new JLabel("Physics"));
        physicsPanel_.add(physicsComboBox_);
        firstPanel_.add(physicsPanel_, BorderLayout.NORTH);
        fluxParamPanel_ = new RPnFluxParamsPanel((String) physicsComboBox_.getSelectedItem());
        firstPanel_.add(fluxParamPanel_, BorderLayout.CENTER);

    }

    private void putFluxParams() {
        firstPanel_.remove(fluxParamPanel_);
        fluxParamPanel_ = new RPnFluxParamsPanel((String) physicsComboBox_.getSelectedItem());
        physicsNameLabel_.setText((String) physicsComboBox_.getSelectedItem());
        firstPanel_.add(fluxParamPanel_, BorderLayout.CENTER);
        getContentPane().validate();
    }

    private void putBoundary() {


        firstPanel_.remove(boundaryPanel_);
        boundaryPanel_ = new JPanel(new BorderLayout());


        JLabel boundaryLabel = new JLabel("Boundary");
        JPanel boundaryLabelPanel = new JPanel();
        JPanel boundaryDataPanel = new JPanel();

        boundaryLabelPanel.add(boundaryLabel);

        boundaryPanel_.add(boundaryLabelPanel, BorderLayout.NORTH);

       
        for (int i = 0; i < profilesArray_.size(); i++) {//Selecting the physics
            if (profilesArray_.get(i).getName().equals((String) physicsComboBox_.getSelectedItem())) {
                physicsProfile_ = profilesArray_.get(i);
            }
        }

        String[] boundaryArray = physicsProfile_.getBoundary();


        boundaryTextArray_ = new ArrayList<JTextField>();

        if (boundaryArray.length == 4 && physicsProfile_.getBoundaryDimension() == 2) { //RECT BOUNDARY
            

            JLabel minLabel = new JLabel("Min");
            JLabel maxLabel = new JLabel("Max");

            boundaryDataPanel.setLayout(new GridLayout(2, physicsProfile_.getBoundaryDimension() + 1));
            boundaryDataPanel.add(minLabel);


            for (int i = 0; i < boundaryArray.length; i++) {

                boundaryTextArray_.add(i, new JTextField((boundaryArray[i])));

                if (i == boundaryArray.length / 2) {
                    boundaryDataPanel.add(maxLabel);
                }

                boundaryDataPanel.add(boundaryTextArray_.get(i));

            }

            boundaryPanel_.add(boundaryDataPanel, BorderLayout.CENTER);

        }


        if (boundaryArray.length == 6 && physicsProfile_.getBoundaryDimension() == 2) { //ISO TRIANG BOUNDARY

            JLabel aLabel = new JLabel("A");
            JLabel bLabel = new JLabel("B");
            JLabel cLabel = new JLabel("C");
            
            boundaryDataPanel.setLayout(new GridLayout(3, physicsProfile_.getBoundaryDimension() + 1));
            boundaryDataPanel.add(aLabel);
            
            

            for (int i = 0; i < boundaryArray.length; i++) {

                boundaryTextArray_.add(i, new JTextField((boundaryArray[i])));

                if (i == boundaryArray.length/3) {
                    boundaryDataPanel.add(bLabel);
                }
                
                 if (i == 2*boundaryArray.length/3) {
                    boundaryDataPanel.add(cLabel);
                }

                boundaryDataPanel.add(boundaryTextArray_.get(i));

            }

            boundaryPanel_.add(boundaryDataPanel, BorderLayout.CENTER);
        }


        firstPanel_.add(boundaryPanel_, BorderLayout.SOUTH);
        firstPanel_.validate();

    }

    private void buildVisualizationPanel() {
        physicsNameLabel_.setText((String) physicsComboBox_.getSelectedItem());
        secondPanel_.add(physicsNameLabel_, BorderLayout.NORTH);

    }

    private void jbInit() {



        setTitle("Rpn Configuration");
        addPhysicsName();
        putBoundary();
        physicsComboBox_.addActionListener(new ComponentController());
        applyButton.setText("Ok");
        cancelButton.setText("Exit");
        buttonsPanel.remove(beginButton);
        cancelButton.setEnabled(true);
        tabbedPanel_.addTab("Physics", firstPanel_);

        tabbedPanel_.addTab("Visualization", secondPanel_);
        secondPanel_.add(visualizationPanel_, BorderLayout.CENTER);

        this.getContentPane().add(tabbedPanel_, BorderLayout.CENTER);


        setModal(false);
        setPreferredSize(new Dimension(460, 240));
        setResizable(true);
        setMinimumSize(new Dimension(200, 100));
        pack();
    }

    @Override
    protected void cancel() {
        int option = JOptionPane.showConfirmDialog(this, "Close aplication", "Exit RPn", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

    }

    protected void apply() {

        dispose();

        RPnConfig.configure((String) physicsComboBox_.getSelectedItem());
        fluxParamPanel_.applyParams();
        setBoundary();


        RPnDesktopPlotter plotter = new RPnDesktopPlotter();
        RPnUIFrame rpnUIFrame = new RPnUIFrame(plotter);

        rpnUIFrame.pack();

        rpnUIFrame.setVisible(true);
    }

    private void setBoundary() {


        if (physicsProfile_.getBoundary().length == 4 && physicsProfile_.getBoundaryDimension() == 2) { //RECT BOUNDARY

            int i = 0;
            RealVector min = new RealVector(2);
            RealVector max = new RealVector(2);
            while (i < physicsProfile_.getBoundary().length / 2) {

                min.setElement(i, new Double(boundaryTextArray_.get(i).getText()));
                i++;

            }

            i = physicsProfile_.getBoundary().length / 2;
            int index = 0;
            while (i < physicsProfile_.getBoundary().length) {

                max.setElement(index, new Double(boundaryTextArray_.get(i).getText()));
                i++;
                index++;
            }



            RectBoundary newBoundary = new RectBoundary(min, max);

            RPNUMERICS.setBoundary(newBoundary);


        }


        if (physicsProfile_.getBoundary().length == 6 && physicsProfile_.getBoundaryDimension() == 2) { //ISO TRIANG 2D BOUNDARY
                
            RealVector A = new RealVector(2);
            RealVector B = new RealVector(2);
            RealVector C = new RealVector(2);
            
            A.setElement(0, new Double(boundaryTextArray_.get(0).getText()));
            A.setElement(1, new Double(boundaryTextArray_.get(1).getText()));

            B.setElement(0, new Double(boundaryTextArray_.get(2).getText()));
            B.setElement(1, new Double(boundaryTextArray_.get(3).getText()));

            C.setElement(0, new Double(boundaryTextArray_.get(4).getText()));
            C.setElement(1, new Double(boundaryTextArray_.get(5).getText()));
            
            
            IsoTriang2DBoundary newBoundary = new IsoTriang2DBoundary(A, B, C);
            
            
            RPNUMERICS.setBoundary(newBoundary);
            
        }









    }

//    private class ParamValueFocusListener implements FocusListener {
//
//        public void focusGained(FocusEvent e) {
//            JComponent source = (JComponent) e.getComponent();
//            currentParamsEdit_ = source.getName();
//        }
//
//        public void focusLost(FocusEvent e) {
//        }
//    }
//
//    private class TextFieldActionlistener implements DocumentListener {
//
//        public void insertUpdate(DocumentEvent e) {
//
//            String newValue = null;
//            try {
//                newValue = e.getDocument().getText(0, e.getDocument().getLength());
//                tempPluginProfile_.setPluginParm(currentParamsEdit_, newValue);
//            } catch (BadLocationException ex) {
//                ex.printStackTrace();
//
//            }
//        }
//
//        public void removeUpdate(DocumentEvent e) {
//        }
//
//        public void changedUpdate(DocumentEvent e) {
//        }
//    }
    private class ComponentController implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            putFluxParams();
            putBoundary();
        }
    }
}
