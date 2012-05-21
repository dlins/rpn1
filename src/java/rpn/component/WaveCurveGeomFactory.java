/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import rpnumerics.OrbitPoint;
import rpnumerics.WaveCurve;
import rpnumerics.WaveCurveCalc;
import wave.multid.view.ViewingAttr;

public class WaveCurveGeomFactory extends WaveCurveOrbitGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public WaveCurveGeomFactory(WaveCurveCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    @Override
    protected ViewingAttr selectViewingAttr() {
        return new ViewingAttr(Color.white);

    }

    @Override
    protected RpGeometry createGeomFromSource() {

        WaveCurve waveCurve = (WaveCurve) geomSource();


        List<OrbitPoint[]> curves = waveCurve.getCurvesList();

        int[] curvesType = waveCurve.getCurveTypes();


        List<WaveCurveOrbitGeom> waveCurveGeometries = new ArrayList<WaveCurveOrbitGeom>();


        for (int i = 0; i < curvesType.length; i++) {


            System.out.println("Curves type" + curvesType[i]);

            if (curvesType[i] == 1) {
                RarefactionGeom rarefactionGeom = new RarefactionGeom(MultidAdapter.converseOrbitPointsToCoordsArray(curves.get(i)), this);

                waveCurveGeometries.add(rarefactionGeom);

            }

            if (curvesType[i] == 2) {

                ShockCurveGeom shockGeom = new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(curves.get(i)), this);

                waveCurveGeometries.add(shockGeom);

            }


            if (curvesType[i] == 3) {

                CompositeGeom compositeGeom = new CompositeGeom(MultidAdapter.converseOrbitPointsToCoordsArray(curves.get(i)), this);

                waveCurveGeometries.add(compositeGeom);

            }




//            switch (curvesType[i]) {
//
//
//
//
//                case 1: //Rarefaction
//
//                    RarefactionGeom rarefactionGeom = new RarefactionGeom(MultidAdapter.converseOrbitPointsToCoordsArray(curves.get(i)), this);
//
//                    waveCurveGeometries.add(rarefactionGeom);
//
//                    break;
//
//
//                case 2://Shock
//
//
//                    ShockCurveGeom shockGeom = new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(curves.get(i)), this);
//
//                    waveCurveGeometries.add(shockGeom);
//                    break;
//
//                case 3://Composite
//
//                    CompositeGeom compositeGeom = new CompositeGeom(MultidAdapter.converseOrbitPointsToCoordsArray(curves.get(i)), this);
//
//                    waveCurveGeometries.add(compositeGeom);
//
//                    break;
//
//
//            }
//
//
//
//
//
        }



        return new WaveCurveGeom(MultidAdapter.converseOrbitToCoordsArray(waveCurve), waveCurveGeometries, this);



    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
