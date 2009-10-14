/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpn.component.*;
import rpnumerics.*;
import wave.multid.*;
import wave.util.*;

public class BifurcationPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Bifurcation Curve";
    // Members
    //
    static private BifurcationPlotAgent instance_ = null;



    //
    // Constructors/Initializers
    //
    protected BifurcationPlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        
//        OrbitPoint testePoint = new OrbitPoint(new RealVector(2));
//        OrbitGeomFactory testeFactory = new OrbitGeomFactory(RPNUMERICS.createOrbitCalc(testePoint, 1));

//        CoordsArray [] testeCoords = new CoordsArray[2];

//        testeCoords[0]=new CoordsArray(new RealVector(2));
//        testeCoords[1] = new CoordsArray(new RealVector(2));

//        return new OrbitGeom(testeCoords, testeFactory);

//        if (this.getContainer().isSelected()) {
//            System.out.println("Chamando createGeometry");
//
//        } else {
//            System.out.println(" Nao esta selecionado");
//        }

//        BifurcationCurveGeomFactory factory = new BifurcationCurveGeomFactory(RPNUMERICS.createBifurcationCalc());
// return factory.geom();

        BifurcationCurveCalc curveCalc = RPNUMERICS.createBifurcationCalc();
        BifurcationCurveGeomFactory factory = new BifurcationCurveGeomFactory(curveCalc);
        return factory.geom();        

    }

    static public BifurcationPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new BifurcationPlotAgent();
        }
        return instance_;
    }
}
