/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
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

        return null;

//        WaveCurve waveCurve = (WaveCurve) geomSource();
//
//
//        List<OrbitPoint[]> curves = waveCurve.getCurvesList();
//
//        int orbitBegin = 0;
//        int orbitEnd;
//
//        List<WaveCurveOrbitGeom> waveCurveGeometries = new ArrayList<WaveCurveOrbitGeom>();
//
//
//        int[] curvesType = waveCurve.getCurveTypes();
//
//        for (int i = 0; i < curvesType.length; i++) {
//
//            orbitEnd = WaveCurve.getCurvesIndex()[i];
//
//            System.out.println("inicio: " + orbitBegin + " Fim: " + orbitEnd);
//            waveCurveGeometries.add(createOrbits(orbitBegin,orbitBegin+orbitEnd, curvesType[i]));
//
//            orbitBegin += orbitEnd;
//
//        }
//
//
//        return new WaveCurveGeom(MultidAdapter.converseOrbitToCoordsArray(waveCurve), waveCurveGeometries, this);

    }

    private WaveCurveOrbitGeom createOrbits(int beginOfCurve, int endOfCurve, int curveType) {

        OrbitPoint[] orbitPoints = new OrbitPoint[endOfCurve];

        int arrayIndex = 0;

        WaveCurve waveCurve = (WaveCurve) geomSource();
        for (int i = beginOfCurve; i < beginOfCurve+endOfCurve; i++) {


            orbitPoints[arrayIndex] = new OrbitPoint(waveCurve.getPoints()[i]);


            System.out.println(orbitPoints[arrayIndex]);
            arrayIndex++;

        }




        if (curveType == 1) {

            return new RarefactionGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbitPoints), this);
        }//Rarefaction


        if (curveType == 2) {
            return new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbitPoints), this);
        }


        if (curveType == 3) {
            return new CompositeGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbitPoints), this);
        }


        return null;

    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
