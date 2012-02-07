/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpnumerics.Area;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import rpn.RPnSelectedAreaDialog;
import rpn.component.*;
import rpn.component.util.GeometryUtil;
import rpn.controller.ui.AREASELECTION_CONFIG;
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

public class BifurcationRefineAgent extends RpModelConfigChangeAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Refine Bifurcation Curve";
    // Members
    //
    static private BifurcationRefineAgent instance_ = null;
    
    //
    // Constructors/Initializers
    //
    protected BifurcationRefineAgent() {
        super(DESC_TEXT);
        
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println("actionPerformed do BifurcationRefineAgent");
        UIController.instance().setState(new BIFURCATIONREFINE_CONFIG());    //*** chamada original

        PropertyChangeEvent propertyEvent = new PropertyChangeEvent(this, "refine", "", AreaSelectionAgent.instance().getListArea().get(0));

        applyChange(propertyEvent);

        AreaSelectionAgent.instance().getListArea().clear();

//        for (int i = 0; i < GeometryUtil.targetPoint.getSize(); i++) {        // Pode ser útil na hora de fazer inclusao dos novos segmentos (para nao serem eliminados)
//            GeometryUtil.cornerRet.setElement(i, 0);
//            GeometryUtil.targetPoint.setElement(i, 0.);
//        }

    }


    static public BifurcationRefineAgent instance() {
        if (instance_ == null) {
            instance_ = new BifurcationRefineAgent();
        }
        return instance_;
    }



    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
