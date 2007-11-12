/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;

import java.util.ArrayList;
import java.util.List;
import rpn.component.StationaryPointGeomFactory;
import rpnumerics.RpNumerics;
import rpn.parser.RPnDataModule;
import rpn.usecase.ChangeSigmaAgent;
import rpn.component.XZeroGeomFactory;
import rpn.controller.phasespace.NUMCONFIG_READY;
import rpnumerics.HugoniotCurve;
import rpnumerics.PhasePoint;
import wave.util.RealVector;

public class SIGMA_CONFIG extends UI_ACTION_SELECTED {
    
    //
    // Members
    //
    //
    // Constructors
    //
    public SIGMA_CONFIG() {
        super(ChangeSigmaAgent.instance());
    }
    
    public void userInputComplete(rpn.controller.ui.UIController ui, RealVector userInput) {
        
        super.userInputComplete(ui, userInput);
//
//        // XZERO
        XZeroGeomFactory xzeroFactory = new XZeroGeomFactory(new rpnumerics.StationaryPointCalc(
                (RpNumerics.getXZero())));
        RPnDataModule.PHASESPACE.plot(xzeroFactory.geom());
//        // plots all other singularities
        
        HugoniotCurve hCurve = (HugoniotCurve) ( (NUMCONFIG_READY)RPnDataModule.PHASESPACE.state()).hugoniotGeom().geomFactory().geomSource();
        
//        List pList = hCurve.segments();
//        List pList = hCurve.findPoints( ( (ShockFlow) RPNUMERICS.flow()).
//                getSigma());

        
        //TODO Teste --- Simulando o metodo findPoints !!!
        ArrayList pList = new ArrayList();
        
        
        pList.add(new RealVector("0.2 0.1"));
        pList.add(new RealVector("0.1 0.1"));
        
       //-------------------------------------------------------------
        for (int i = 0; i < pList.size(); i++) {
            RealVector sigmaPoint = (RealVector) pList.get(i);
            
            
            StationaryPointGeomFactory factory = new StationaryPointGeomFactory(new
                    rpnumerics.StationaryPointCalc(new PhasePoint(sigmaPoint)));
            RPnDataModule.PHASESPACE.plot(factory.geom());
            
        }
        
        
        ui.setState(new GEOM_SELECTION());
        
    }
}


