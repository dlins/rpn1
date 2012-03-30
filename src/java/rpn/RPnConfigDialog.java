/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import rpn.parser.ConfigurationProfile;
import rpn.parser.RPnVisualizationModule;
import rpnumerics.RPNUMERICS;
import rpnumerics.Configuration;
import wave.multid.Space;
import wave.util.RealVector;
import wave.util.RectBoundary;

/**
 *
 * @deprecated To be replaced by RPnConfigurationDialog
 */

public class RPnConfigDialog extends RPnDialog {

//    private Dimension dialogDimension_ = new Dimension(480, 380);
    private String currentParamEdit_;
    private JPanel physicsPanel_ = new JPanel();
    private JPanel boundaryPanel_ = new JPanel();
    private JPanel firstPanel_ = new JPanel(new GridBagLayout());//BorderLayout());
    private JPanel secondPanel_ = new JPanel(new BorderLayout());
    private JPanel thirdPanel_ = new JPanel(new BorderLayout());
    private JPanel methodsParamsPanel_ = new JPanel();
    private JTabbedPane tabbedPanel_ = new JTabbedPane();
    private JLabel physicsNameLabel_ = new JLabel();
//    private RPnFluxParamsPanel fluxParamPanel_;
    private JPanel visualizationPanel_ = new JPanel();
    private JCheckBox[] axisCheckBoxArray_;
    private ArrayList<RPnInputComponent> boundaryTextArray_;
    private JTextField[] frameSize_ = new JTextField[2];
    private JTextField[] panelsLabelTextField_;
    private ArrayList<ConfigurationProfile> profilesArray_;
    private ConfigurationProfile physicsProfile_;
    private JComboBox physicsComboBox_;
    private JComboBox methodComboBox_;
    private boolean[] axisSelected_;
    private HashMap<String, Configuration> methodConfigMap_;

    public RPnConfigDialog() {
        profilesArray_ = RPnConfig.getAllPhysicsProfiles();


        methodConfigMap_ = new HashMap<String, Configuration>();
        initLocalParamsConfigMap();
        removeDefaultApplyBehavior();
        applyButton.addActionListener(new ApplyButtonController());
        jbInit();
        buildVisualizationPanel();
        buildMethodPanel();

    }

    private void initLocalParamsConfigMap() {

        ArrayList<ConfigurationProfile> methodsProfiles = RPnConfig.getAllMethodsProfiles();//ConfigurationProfiles();

        methodComboBox_ = new JComboBox();

        for (int i = 0; i < methodsProfiles.size(); i++) {
            methodComboBox_.addItem(methodsProfiles.get(i).getName());
        }

        for (int i = 0; i < methodsProfiles.size(); i++) {

            ConfigurationProfile profile = methodsProfiles.get(i);

            String methodName = profile.getName();

//            HashMap<String, String> profileParams = profile.getParams();

//            Configuration methodConfiguration = new Configuration(profile.getName(), profile.getType(), profileParams);

            Configuration methodConfiguration = new Configuration(profile);

            methodConfigMap_.put(methodName, methodConfiguration);

        }

    }

    private void addPhysicsName() {

        physicsComboBox_ = new JComboBox();
        for (int i = 0; i < profilesArray_.size(); i++) {
            ConfigurationProfile profile = profilesArray_.get(i);
            physicsComboBox_.addItem(profile.getName());
        }
        physicsPanel_.add(new JLabel("Physics"));
        physicsPanel_.add(physicsComboBox_);

        GridBagConstraints constrains = new GridBagConstraints();
        constrains.gridx = 0;
        constrains.gridy = 0;
        firstPanel_.add(physicsPanel_, constrains);
//        fluxParamPanel_ = new RPnFluxParamsPanel((String) physicsComboBox_.getSelectedItem());
        constrains.gridx = 0;
        constrains.gridy = 1;
//        firstPanel_.add(fluxParamPanel_, constrains);

    }

    private void buildFluxParamsPanel() {

        for (int i = 0; i < profilesArray_.size(); i++) {//Selecting physics
            if (profilesArray_.get(i).getName().equals((String) physicsComboBox_.getSelectedItem())) {
                physicsProfile_ = profilesArray_.get(i);
            }
        }
        RPNUMERICS.init(physicsProfile_.getName());

//        firstPanel_.remove(fluxParamPanel_);
//        fluxParamPanel_ = new RPnFluxParamsPanel((String) physicsComboBox_.getSelectedItem());
        physicsNameLabel_.setText((String) physicsComboBox_.getSelectedItem());
        GridBagConstraints constrains = new GridBagConstraints();
        constrains.gridx = 0;
        constrains.gridy = 1;
//        firstPanel_.add(fluxParamPanel_, constrains);
        getContentPane().validate();
    }

    private void buildBoundaryPanel() {

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

        boundaryTextArray_ = new ArrayList<RPnInputComponent>();

        ConfigurationProfile boundaryProfile = physicsProfile_.getConfigurationProfile(ConfigurationProfile.BOUNDARY);

        if (boundaryProfile.getName().equalsIgnoreCase("rect")) {//RECT BOUNDARY

            int dimension = new Integer(boundaryProfile.getParam("dimension"));

            int totalInputComponets = dimension * 2;

            ArrayList<RPnInputComponent> inputComponentsArray = new ArrayList<RPnInputComponent>(totalInputComponets);

            boundaryDataPanel.setLayout(new GridLayout(2, dimension + 1));

            String[] limitsNumbers = boundaryProfile.getParam("limits").split(" ");

            int vectorIndex = 0;
            for (int i = 0; i < dimension; i++) {
//                RPnInputComponent Min = new RPnInputComponent(new Double(limitsNumbers[vectorIndex]));
//                Min.setLabel("Axis " + i + " Min");
//                inputComponentsArray.add(Min);
//
//                vectorIndex += 2;

            }

            vectorIndex = 1;

            for (int i = 0; i < dimension; i++) {

//                RPnInputComponent Max = new RPnInputComponent(new Double(limitsNumbers[vectorIndex]));
//                Max.setLabel("Axis " + i + " Max");
//                inputComponentsArray.add(Max);
//
//                vectorIndex += 2;

            }
            for (int i = 0; i < totalInputComponets; i++) {
                boundaryTextArray_.add(i, inputComponentsArray.get(i));
                boundaryDataPanel.add(boundaryTextArray_.get(i).getContainer());
            }


            boundaryPanel_.add(boundaryDataPanel, BorderLayout.CENTER);


        } else {//ISO TRIANGULAR BOUNDARY

            JLabel triangularBoundaryLabel = new JLabel("Triangular Boundary", SwingConstants.CENTER);

            boundaryDataPanel.setLayout(new BorderLayout());
            boundaryDataPanel.add(triangularBoundaryLabel, BorderLayout.CENTER);

            boundaryPanel_.add(boundaryDataPanel, BorderLayout.CENTER);
        }

        GridBagConstraints constrains = new GridBagConstraints();
        constrains.gridx = 0;
        constrains.gridy = 2;
        firstPanel_.add(boundaryPanel_, constrains);//BorderLayout.SOUTH);
        firstPanel_.validate();

    }

    private void buildMethodPanel() {

        thirdPanel_.removeAll();
        JScrollPane scrollPane = new JScrollPane(methodsParamsPanel_);

        JPanel methodComboPanel = new JPanel(new BorderLayout());

        methodComboPanel.add(methodComboBox_, BorderLayout.CENTER);

        methodComboBox_.addItemListener(new MethodNameItemController());

        CardLayout methodParamPanelLayout = new CardLayout();

        methodsParamsPanel_.setLayout(methodParamPanelLayout);

        Set<Entry<String, Configuration>> configurationSet = methodConfigMap_.entrySet();

        Iterator<Entry<String, Configuration>> methodIterator = configurationSet.iterator();

        while (methodIterator.hasNext()) {

            Entry<String, Configuration> entry = methodIterator.next();

            Configuration methodConfiguration = entry.getValue();

            HashMap<String, String> params = methodConfiguration.getParams();

            Set<Entry<String, String>> paramSet = params.entrySet();

            Iterator<Entry<String, String>> paramIterator = paramSet.iterator();

            JPanel methodPanel = new JPanel(false);

            GridBagLayout methodParamLayout = new GridBagLayout();

            GridBagConstraints methodPanelLayoutConstraints = new GridBagConstraints();

            methodPanel.setLayout(methodParamLayout);

            methodsParamsPanel_.add(methodPanel, entry.getKey());

            while (paramIterator.hasNext()) {

                Entry<String, String> paramEntry = paramIterator.next();

                JTextField paramTextField = new JTextField(methodConfiguration.getParam(paramEntry.getKey()));

                paramTextField.setName(paramEntry.getKey());

                paramTextField.addFocusListener(new ParamValueFocusListener());
                paramTextField.getDocument().addDocumentListener(new TextFieldActionlistener());

                JLabel paramNameLabel = new JLabel(paramEntry.getKey());


                methodPanelLayoutConstraints.gridx = 0;
                methodPanel.add(paramNameLabel, methodPanelLayoutConstraints);
                methodPanelLayoutConstraints.gridx = 1;
                methodPanel.add(paramTextField, methodPanelLayoutConstraints);


            }
        }

        thirdPanel_.add(methodComboPanel, BorderLayout.NORTH);
        thirdPanel_.add(scrollPane, BorderLayout.CENTER);

    }

    private void buildVisualizationPanel() {

        secondPanel_.removeAll();


        JPanel physicsLabelPanel = new JPanel(new FlowLayout());
        JPanel axisCheckPanel = new JPanel(new GridBagLayout());

        JScrollPane axisScroll = new JScrollPane(axisCheckPanel);
        JPanel panelsSizePanel = new JPanel(new FlowLayout());



        frameSize_[0] = new JTextField();
        frameSize_[0].setColumns(4);
        frameSize_[1] = new JTextField();
        frameSize_[1].setColumns(4);

        frameSize_[0].setName("panelX");
        frameSize_[1].setName("panelY");


        frameSize_[0].setText("650");
        frameSize_[1].setText("650");


        JLabel panelsW = new JLabel("Width");
        JLabel panelsH = new JLabel("Height");


        physicsNameLabel_.setText("Axis (Physics: " + (String) physicsComboBox_.getSelectedItem() + ")");


        physicsLabelPanel.add(physicsNameLabel_);

        secondPanel_.add(physicsLabelPanel, BorderLayout.NORTH);

        //AXIS CONFIGURATION

        ConfigurationProfile selectedPhysicsProifle = RPnConfig.getPhysicsProfile((String) physicsComboBox_.getSelectedItem());

        ConfigurationProfile boundaryProfile = selectedPhysicsProifle.getConfigurationProfile(ConfigurationProfile.BOUNDARY);

        if (!boundaryProfile.getName().equals("triang")) {
            int dimension = new Integer(boundaryProfile.getParam("dimension"));

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
            axisSelected_ = new boolean[combinations.size()];

            axisSelected_[0] = true;//The first axis is checked by default

//            System.out.println("Tamanho de combination size:" + combinations.size());

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

                secondPanel_.add(axisScroll, BorderLayout.CENTER);
                secondPanel_.add(panelsSizePanel, BorderLayout.SOUTH);
            }



        } else {

            JLabel boundaryLabel = new JLabel("Triangular Boundary");
            boundaryLabel.setHorizontalAlignment(JLabel.CENTER);
            secondPanel_.add(boundaryLabel, BorderLayout.CENTER);

        }


        //PANELS SIZE CONFIGURATION

        secondPanel_.add(panelsSizePanel, BorderLayout.SOUTH);
        panelsSizePanel.add(panelsW);
        panelsSizePanel.add(frameSize_[0]);
        panelsSizePanel.add(panelsH);
        panelsSizePanel.add(frameSize_[1]);


    }

    private void jbInit() {

        setTitle("Rpn Configuration");
        addPhysicsName();
        buildBoundaryPanel();
        physicsComboBox_.addActionListener(new ComponentController());
        ConfigurationProfile boundaryProfile = physicsProfile_.getConfigurationProfile(ConfigurationProfile.BOUNDARY);
        int dimension = new Integer(boundaryProfile.getParam("dimension"));

        axisSelected_ = new boolean[dimension];

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

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rectangle = ge.getMaximumWindowBounds();

        setLocationByPlatform(true);
        Dimension d = rectangle.getSize();

        d.height = (int) (d.height * 0.5);
        d.width = (int) (d.width * 0.5);

        setSize(d);
        setResizable(true);
        pack();
    }

    @Override
    protected void cancel() {
        int option = JOptionPane.showConfirmDialog(this, "Close aplication", "Exit RPn", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

    }

    private void setMethodConfiguration() {

        Set<Entry<String, Configuration>> configurationSet = methodConfigMap_.entrySet();

        Iterator<Entry<String, Configuration>> methodIterator = configurationSet.iterator();

        while (methodIterator.hasNext()) {

            Entry<String, Configuration> entry = methodIterator.next();

            Configuration methodConfiguration = entry.getValue();

            HashMap<String, String> params = methodConfiguration.getParams();

            Set<Entry<String, String>> paramSet = params.entrySet();

            Iterator<Entry<String, String>> paramIterator = paramSet.iterator();

            while (paramIterator.hasNext()) {

                Entry<String, String> paramEntry = paramIterator.next();

                RPNUMERICS.setParamValue(entry.getKey(), paramEntry.getKey(), paramEntry.getValue());

            }
        }
    }

    private void setVisualConfiguration() {
        ConfigurationProfile boundaryProfile = physicsProfile_.getConfigurationProfile(ConfigurationProfile.BOUNDARY);
        int dimension = new Integer(boundaryProfile.getParam("dimension"));
        Space space = new Space("", dimension);
        Space auxSpace = new Space("", dimension * 2);
        int[] projIndices;
        if (!boundaryProfile.getName().equalsIgnoreCase("triang")) {//RECT BOUNDARY
            for (int i = 0; i < axisCheckBoxArray_.length; i++) {

                if (axisCheckBoxArray_[i].isSelected()) {//Building selected axis array

                    StringTokenizer tokens = new StringTokenizer(axisCheckBoxArray_[i].getText(), "-");
                    projIndices = new int[tokens.countTokens()];

                    int index = 0;
                    while (tokens.hasMoreTokens()) {
                        projIndices[index++] = new Integer(tokens.nextToken());

                    }

                    RPnProjDescriptor projDesc = new RPnProjDescriptor(space, panelsLabelTextField_[i].getText(), new Integer(frameSize_[0].getText()), new Integer(frameSize_[1].getText()), projIndices, false);
                    RPnVisualizationModule.DESCRIPTORS.add(projDesc);

//                    RPnVisualizationModule.createAuxDescriptor(projDesc, auxSpace, projDesc.isIso2equi());

                }
            }
        } else {//TODO Triangular boundary . Use iso2equi value as flag
            projIndices = new int[2];
            projIndices[0] = 0;
            projIndices[1] = 1;
            RPnVisualizationModule.DESCRIPTORS.add(
                    new RPnProjDescriptor(space, "Axis 0 1", new Integer(frameSize_[0].getText()), new Integer(frameSize_[1].getText()), projIndices, true));
        }


    }

    protected void apply() {

        dispose();

        RPnConfig.configure((String) physicsComboBox_.getSelectedItem());
        setVisualConfiguration();
//        fluxParamPanel_.applyParams();

        setBoundary();
        setMethodConfiguration();

        RPnDesktopPlotter plotter = new RPnDesktopPlotter();
        RPnUIFrame rpnUIFrame = new RPnUIFrame(plotter);
        RPnDesktopPlotter.setUIFrame(rpnUIFrame);
        RPnCurvesListFrame curvesFrame = new RPnCurvesListFrame("Main");
        rpnUIFrame.setCurvesFrame(curvesFrame);
        rpnUIFrame.pack();
        rpnUIFrame.setVisible(true);

    }

    private void setBoundary() { //Creating a boundary using user's input values
        ConfigurationProfile boundaryProfile = physicsProfile_.getConfigurationProfile(ConfigurationProfile.BOUNDARY);
//        boolean iso2equi = new Boolean(boundaryProfile.getParam("iso2equi"));
        if (!boundaryProfile.getName().equalsIgnoreCase("triang")) {//RECT BOUNDARY. TODO Use iso2equi value as flag

            int dimension = new Integer(boundaryProfile.getParam("dimension"));
            RealVector min = new RealVector(dimension);
            RealVector max = new RealVector(dimension);


            for (int i = 0; i < boundaryTextArray_.size() / 2; i++) {//Filling min limits

//                min.setElement(i, (Double) boundaryTextArray_.get(i).getValue(RPnInputComponent.NUMERIC_VALUE));
            }
            for (int i = 0; i < boundaryTextArray_.size() / 2; i++) {//Filling max limits
//                max.setElement(i, (Double) boundaryTextArray_.get(i + boundaryTextArray_.size() / 2).getValue(RPnInputComponent.NUMERIC_VALUE));
            }

            RectBoundary newBoundary = new RectBoundary(min, max);

            RPNUMERICS.setBoundary(newBoundary);


        }
        //If is triangular boundary, nothing to do . Default iso triangle domain in triphase physics will be used.
    }

    private class ComponentController implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (e.getSource() instanceof JComboBox) {

                JComboBox combo = (JComboBox) e.getSource();
                RPnConfig.setActivePhysics((String) combo.getSelectedItem());
                buildFluxParamsPanel();
                buildBoundaryPanel();

            }
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


            if (physicsProfile_.getConfigurationProfile(ConfigurationProfile.BOUNDARY).getName().equals("triang")) {
                apply();
                return;
            }


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

    private class MethodNameItemController implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            CardLayout cardLayout = (CardLayout) (methodsParamsPanel_.getLayout());
            cardLayout.show(methodsParamsPanel_, (String) e.getItem());

        }
    }

    private class ParamValueFocusListener implements FocusListener {

        public void focusGained(FocusEvent e) {
            JComponent source = (JComponent) e.getComponent();
            currentParamEdit_ = source.getName();
        }

        public void focusLost(FocusEvent e) {
        }
    }

    private class TextFieldActionlistener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {

            String newValue = null;
            try {

                newValue = e.getDocument().getText(0, e.getDocument().getLength());

                String methodName = (String) methodComboBox_.getSelectedItem();

                Configuration methodConfig = methodConfigMap_.get(methodName);

                methodConfig.setParamValue(currentParamEdit_, newValue);

                methodConfigMap_.put(methodName, methodConfig);


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
