/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import javax.swing.JToggleButton;
import rpn.RPnSelectedAreaDialog;
import rpn.controller.ui.UIController;
import rpnumerics.Area;
import rpnumerics.BifurcationProfile;
import wave.util.RealVector;

public class AreaSelectionAgent extends RpModelActionAgent {

    static public final String DESC_TEXT = "Select Area";
    static private AreaSelectionAgent instance_ = null;
    private JToggleButton button_;
    private RealVector resolution_;
    private boolean validResolution_;

    /** Creates a new instance of ScratchAgent */
    public AreaSelectionAgent() {
        super(DESC_TEXT, null);

        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);
        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        setEnabled(true);
    }

    public static AreaSelectionAgent instance() {
        if (instance_ == null) {
            instance_ = new AreaSelectionAgent();
        }
        return instance_;
    }

    public JToggleButton getContainer() {
        return button_;
    }

    public void setResolution(RealVector resolution) {
        resolution_ = resolution;
    }

    public void setValidResolution(boolean validResolution){
        validResolution_=validResolution;
    }

    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {

        RealVector[] diagonal = UIController.instance().userInputList();

        RealVector testUp = new RealVector(diagonal[0]);
        RealVector testDown = new RealVector(diagonal[1]);

        testUp.sub(testDown);

        if (testUp.getElement(0) > 0 && testUp.getElement(1) > 0) {

            RPnSelectedAreaDialog dialog = new RPnSelectedAreaDialog();
            dialog.setVisible(true);

            if (validResolution_) {
                Area selectedArea = new Area(resolution_, diagonal[0], diagonal[1]);
                BifurcationProfile.instance().addArea(selectedArea);

            }

        }

    }
}


