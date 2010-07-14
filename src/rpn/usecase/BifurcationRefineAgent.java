/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpnumerics.Area;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import rpn.RPnSelectedAreaDialog;
import rpn.component.*;
import rpn.controller.ui.BIFURCATIONREFINE_CONFIG;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.BifurcationCurve;
import rpnumerics.BifurcationProfile;
import rpnumerics.RPnCurve;
import rpnumerics.methods.contour.support.CurveDomainManager;
import rpnumerics.methods.contour.support.DimensionDoenstMatch;
import rpnumerics.methods.contour.support.NoContourMethodDefined;
import wave.util.*;

public class BifurcationRefineAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Refine Bifurcation Curve";
    // Members
    //
    static private BifurcationRefineAgent instance_ = null;
    private RealVector resolution_;
    private boolean validResolution_;

    //
    // Constructors/Initializers
    //
    protected BifurcationRefineAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JButton());
        getContainer().setEnabled(false);
        validResolution_ = false;

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        UIController.instance().setState(new BIFURCATIONREFINE_CONFIG());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

         Iterator geomIterator = RPnDataModule.AUXPHASESPACE.getGeomObjIterator();

          while (geomIterator.hasNext()) {
                RpGeometry geom = (RpGeometry) geomIterator.next();

                if (geom instanceof BifurcationCurveGeom) {
                    BifurcationCurveGeomFactory factory = (BifurcationCurveGeomFactory) geom.geomFactory();
                    return factory.refine();
                }

          }


        return null;
    }

    static public BifurcationRefineAgent instance() {
        if (instance_ == null) {
            instance_ = new BifurcationRefineAgent();
        }
        return instance_;
    }

    private void showAreaSelectionDialog(RealVector up, RealVector down) {

        while (!validResolution_) {
            RPnSelectedAreaDialog dialog = new RPnSelectedAreaDialog();
            dialog.setVisible(true);

        }

        Area selectedArea = new Area(resolution_, up, down);
        BifurcationProfile.instance().addArea(selectedArea);

    }

    @Override
    public void execute() {
        //AREA SELECTION

        System.out.println("Chamando execute do refino");

        RealVector[] diagonal = UIController.instance().userInputList();

        System.out.println(diagonal[0]);
        System.out.println(diagonal[1]);

        RealVector testUp = new RealVector(diagonal[0]);
        RealVector testDown = new RealVector(diagonal[1]);

        boolean selectionDirectionOk = true;

        for (int i = 0; i < testUp.getSize(); i++) {
            if (testUp.getElement(i) < testDown.getElement(i)) {
                selectionDirectionOk = false;
                break;
            }
        }

        if (!selectionDirectionOk) {
            UIController.instance().globalInputTable().reset();
            JOptionPane.showMessageDialog(UIController.instance().getFocusPanel(), "Wrong area selection", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            showAreaSelectionDialog(testUp, testDown);
            super.execute();
        }


    }

    public void setValidResolution(boolean validResolution) {
        this.validResolution_ = validResolution;
    }

    public void setResolution(RealVector resolutionVector) {
        resolution_ = resolutionVector;
    }
}
