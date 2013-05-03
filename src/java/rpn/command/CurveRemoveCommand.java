/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.util.ArrayList;
import java.util.Iterator;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceManager;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import rpnumerics.RPnCurve;

public class CurveRemoveCommand extends RpModelConfigChangeCommand  {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Curve Remove Command";
    //
    // Members
    //
    static private CurveRemoveCommand instance_ = null;
    //
    // Constructors
    //

 
    protected CurveRemoveCommand() {
        super(DESC_TEXT);
    }

    public void unexecute() {
    }

    public void execute() {

    }

    public void remove(int curveId) {

        System.out.println("removendo curva no comando");
        
        RPnPhaseSpaceAbstraction phaseSpace = UIController.instance().getActivePhaseSpace();//RPnDataModule.getPhaseSpace(phaseSpaceName);

        ArrayList<RpGeometry> toRemove = new ArrayList<RpGeometry>();
        Iterator<RpGeometry> geometryIterator = phaseSpace.getGeomObjIterator();

        while (geometryIterator.hasNext()) {
            RpGeometry rpGeometry = geometryIterator.next();

            RpGeomFactory factory = (RpGeomFactory) rpGeometry.geomFactory();

            RPnCurve curve = (RPnCurve) factory.geomSource();

            if (curve.getId() == curveId) {
                toRemove.add(rpGeometry);

            }

        }
        
        for (RpGeometry rpGeometry : toRemove) {
            RPnPhaseSpaceManager.instance().remove(phaseSpace,rpGeometry);
        }
        

    }

    
    
  
    
    static public CurveRemoveCommand instance() {
        if (instance_ == null) {
            instance_ = new CurveRemoveCommand();
        }
        return instance_;
    }
    
    
    
    

}
