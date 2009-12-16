/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.*;
import java.awt.*;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpnumerics.BifurcationProfile;

public class RPnSelectedAreaDialog extends RPnDialog {

    private JPanel paramsPanel_ = new JPanel();

    JLabel xResolutionLabel;
    JLabel yResolutionLabel;
    JTextField xResolutionTextField;
    JTextField yResolutionTextField;

    public RPnSelectedAreaDialog() {
        super(false, true);

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setTitle("Area Selection Resolution");


        xResolutionLabel = new JLabel("X Resolution");
        yResolutionLabel = new JLabel("Y Resolution");

        xResolutionTextField = new JTextField();
        yResolutionTextField = new JTextField();

        paramsPanel_.setLayout(new GridLayout(2, 2));
        paramsPanel_.add(xResolutionLabel);
        paramsPanel_.add(xResolutionTextField);
        paramsPanel_.add(yResolutionLabel);
        paramsPanel_.add(yResolutionTextField);


        setMinimumSize(new Dimension(getTitle().length() * 10, 40));
        this.getContentPane().add(paramsPanel_, BorderLayout.CENTER);

        pack();
    }

    @Override
    protected void cancel() {

        int lastAreaSelected = BifurcationProfile.instance().getSelectedAreas().size() - 1;
        int lastAreaFromPanel =  UIController.instance().getFocusPanel().getCastedUI().getSelectionAreas().size()-1;
        BifurcationProfile.instance().getSelectedAreas().remove(lastAreaSelected);
        UIController.instance().getFocusPanel().getCastedUI().getSelectionAreas().remove(lastAreaFromPanel);
        UIController.instance().getFocusPanel().repaint();

        dispose();
    }

    protected void apply() {

        int xRes = new Integer(xResolutionTextField.getText());
        int yRes = new Integer(yResolutionTextField.getText());

        int lastAreaSelected = BifurcationProfile.instance().getSelectedAreas().size() - 1;

        BifurcationProfile.instance().getSelectedAreas().get(lastAreaSelected).setxResolution(xRes);
        BifurcationProfile.instance().getSelectedAreas().get(lastAreaSelected).setyResolution(yRes);

        dispose();

    }

    @Override
    protected void begin() {
    }
}
