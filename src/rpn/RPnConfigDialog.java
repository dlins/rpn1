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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import rpn.parser.MethodProfile;
import rpn.parser.PhysicsProfile;
import rpn.parser.RPnInterfaceParser;
import rpn.parser.RPnVisualizationModule;
import rpn.parser.VisualizationProfile;
import rpnumerics.RPNUMERICS;
import wave.multid.Space;
import wave.util.IsoTriang2DBoundary;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class RPnConfigDialog extends RPnDialog {

    private FlowLayout flowLayout1 = new FlowLayout();
    private JPanel physicsPanel_ = new JPanel();
    private JPanel boundaryPanel_ = new JPanel();
    private JPanel firstPanel_ = new JPanel(new BorderLayout());
    private JPanel secondPanel_ = new JPanel(new BorderLayout());
    private JPanel thirdPanel_ = new JPanel(new GridLayout(3, 1));
    private JTabbedPane tabbedPanel_ = new JTabbedPane();
    private JLabel physicsNameLabel_ = new JLabel();
    private JLabel panelsSizeLabel_ = new JLabel("Panels size: ");
    private RPnFluxParamsPanel fluxParamPanel_;
    private JPanel visualizationPanel_ = new JPanel();
    private JCheckBox[] axisCheckBoxArray_;
    private ArrayList<JTextField> boundaryTextArray_;
    private JTextField[] panelsSizeTextField_ = new JTextField[2];
    private JTextField[] panelsLabelTextField_;
    private ArrayList<PhysicsProfile> profilesArray_;
    private PhysicsProfile physicsProfile_;
    private JComboBox physicsComboBox_;
    private boolean[] axisSelected_;

    public RPnConfigDialog() {
        profilesArray_ = RPnInterfaceParser.getPhysicsProfiles();
        removeDefaultApplyBehavior();
        applyButton.addActionListener(new ApplyButtonController());
        jbInit();
        buildVisualizationPanel();
        buildMethodPanel();

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

        for (int i = 0; i < profilesArray_.size(); i++) {//Selecting physics
            if (profilesArray_.get(i).getName().equals((String) physicsComboBox_.getSelectedItem())) {
                physicsProfile_ = profilesArray_.get(i);
            }
        }


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


        for (int i = 0; i < profilesArray_.size(); i++) {//Selecting physics
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

                if (i == boundaryArray.length / 3) {
                    boundaryDataPanel.add(bLabel);
                }

                if (i == 2 * boundaryArray.length / 3) {
                    boundaryDataPanel.add(cLabel);
                }

                boundaryDataPanel.add(boundaryTextArray_.get(i));

            }

            boundaryPanel_.add(boundaryDataPanel, BorderLayout.CENTER);
        }


        firstPanel_.add(boundaryPanel_, BorderLayout.SOUTH);
        firstPanel_.validate();

    }

    private void buildMethodPanel() {

        thirdPanel_.removeAll();
        
        ArrayList<MethodProfile> methodsProfiles = RPNUMERICS.getAllMethodsProfiles();

        JTabbedPane methodTabbedPane = new JTabbedPane();


        for (int i = 0; i < methodsProfiles.size(); i++) {

            MethodProfile profile = methodsProfiles.get(i);

            System.out.println("Nome: " + profile.getName());
            
            JPanel methodPanel = new JPanel(false);

            methodPanel.setLayout(new GridLayout(profile.getParams().size(), 2));

            JScrollPane methodScrollPane = new JScrollPane(methodPanel);
            
            methodScrollPane.setPreferredSize(new Dimension(400,200));

            methodTabbedPane.addTab(profile.getName(),methodScrollPane);

            HashMap<String, String> params = profile.getParams();

            Set<Entry<String, String>> paramSet = params.entrySet();

            Iterator<Entry<String, String>> paramIterator = paramSet.iterator();

            while (paramIterator.hasNext()) {
                Entry<String, String> entry = paramIterator.next();

                JLabel paramNameLabel = new JLabel(entry.getKey());
                paramNameLabel.setName(profile.getName());
                JTextField paramTextField = new JTextField(entry.getValue());
                paramTextField.setName(profile.getName());

                methodPanel.add(paramNameLabel);
                methodPanel.add(paramTextField);

                System.out.println("Param name: " + entry.getKey());
                System.out.println("Param value: " + entry.getValue());

            }

        }
        thirdPanel_.add(methodTabbedPane,BorderLayout.CENTER);

    }

    private void buildVisualizationPanel() {

        secondPanel_.removeAll();

        JPanel physicsLabelPanel = new JPanel(new FlowLayout());
        JPanel axisCheckPanel = new JPanel(new GridLayout(2, 2));
        JScrollPane axisScroll = new JScrollPane(axisCheckPanel);
        JPanel panelsSizePanel = new JPanel(new FlowLayout());

        panelsSizeTextField_[0] = new JTextField();
        panelsSizeTextField_[0].setColumns(4);
        panelsSizeTextField_[1] = new JTextField();
        panelsSizeTextField_[1].setColumns(4);

        physicsNameLabel_.setText("Axis (Physics: " + (String) physicsComboBox_.getSelectedItem() + ")");


        physicsLabelPanel.add(physicsNameLabel_);


        int dimension = physicsProfile_.getBoundaryDimension();


        ArrayList<String> combinations = new ArrayList<String>();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i != j) {
                    combinations.add(i + "-" + j);
                }
            }
        }

        axisCheckBoxArray_ = new JCheckBox[combinations.size()];

        panelsLabelTextField_ = new JTextField[combinations.size()];

        for (int i = 0; i < combinations.size(); i++) {

            if (axisSelected_[i] == true) {
                axisCheckBoxArray_[i] = new JCheckBox(combinations.get(i), true);
            } else {
                axisCheckBoxArray_[i] = new JCheckBox(combinations.get(i), false);
            }

            axisCheckBoxArray_[i].addActionListener(new AxisCheckController());
            axisCheckBoxArray_[i].setName("" + i);

            panelsLabelTextField_[i] = new JTextField("Axis " + combinations.get(i));

            axisCheckPanel.add(axisCheckBoxArray_[i]);
            axisCheckPanel.add(panelsLabelTextField_[i]);
        }


        panelsSizeTextField_[0].setName("panelX");
        panelsSizeTextField_[1].setName("panelY");

        VisualizationProfile visProfile = RPnInterfaceParser.getVisualizationProfiles().get(0);
        panelsSizePanel.add(panelsSizeLabel_);
        panelsSizePanel.add(panelsSizeTextField_[0]);
        panelsSizePanel.add(panelsSizeTextField_[1]);

        panelsSizeTextField_[0].setText(visProfile.getViewPort()[0]);
        panelsSizeTextField_[1].setText(visProfile.getViewPort()[1]);

        secondPanel_.add(physicsLabelPanel, BorderLayout.NORTH);

        secondPanel_.add(axisScroll, BorderLayout.CENTER);
        secondPanel_.add(panelsSizePanel, BorderLayout.SOUTH);




    }

    private void jbInit() {

        setTitle("Rpn Configuration");
        addPhysicsName();
        putBoundary();
        physicsComboBox_.addActionListener(new ComponentController());
        axisSelected_ = new boolean[physicsProfile_.getBoundaryDimension()];

        tabbedPanel_.addChangeListener(new TabbedPanelController());
        applyButton.setText("Ok");
        cancelButton.setText("Exit");
        buttonsPanel.remove(beginButton);
        cancelButton.setEnabled(true);
        tabbedPanel_.addTab("Physics", firstPanel_);

        tabbedPanel_.addTab("Visualization", secondPanel_);

        tabbedPanel_.addTab("Methods", thirdPanel_);

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

    private void visualConfiguration() {

        boolean iso2equi_ = physicsProfile_.isIso2equiBoundary();

        for (int i = 0; i < axisCheckBoxArray_.length; i++) {

            if (axisCheckBoxArray_[i].isSelected()) {

                StringTokenizer tokens = new StringTokenizer(axisCheckBoxArray_[i].getText(), "-");
                int[] projIndices = new int[tokens.countTokens()];

                int index = 0;
                while (tokens.hasMoreTokens()) {
                    projIndices[index++] = new Integer(tokens.nextToken());

                }
                int dimension = physicsProfile_.getBoundaryDimension();


                Space space = new Space("", dimension);

                RPnVisualizationModule.DESCRIPTORS.add(
                        new RPnProjDescriptor(space, panelsLabelTextField_[i].getText(), new Integer(panelsSizeTextField_[0].getText()),
                        new Integer(panelsSizeTextField_[1].getText()), projIndices, iso2equi_));

            }

        }


    }

    protected void apply() {


        dispose();

        RPnConfig.configure((String) physicsComboBox_.getSelectedItem());
        visualConfiguration();
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

    private class ComponentController implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            putFluxParams();
            putBoundary();

        }
    }

    private class TabbedPanelController implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            buildVisualizationPanel();
        }
    }

    private class AxisCheckController implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            JCheckBox checkBox = (JCheckBox) e.getSource();
            Integer axisOrder = new Integer(checkBox.getName());
            if (checkBox.isSelected()) {

                axisSelected_[axisOrder] = true;

            } else {
                axisSelected_[axisOrder] = false;
            }


        }
    }

    private class ApplyButtonController implements ActionListener {

        public void actionPerformed(ActionEvent e) {


            boolean oneAxisSelected = false;

            for (int i = 0; i < axisSelected_.length; i++) {

                if (axisSelected_[i] == true) {
                    oneAxisSelected = true;
                }
            }

            if (oneAxisSelected == false) {
                JOptionPane.showMessageDialog(null, "No axis Selected", "Error", JOptionPane.ERROR_MESSAGE);

            } else {

                apply();
            }
        }
    }
}
