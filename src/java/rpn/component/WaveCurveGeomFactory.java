/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rpnumerics.OrbitPoint;
import rpnumerics.WaveCurve;
import rpnumerics.WaveCurveCalc;

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
    protected RpGeometry createGeomFromSource() {

        WaveCurve waveCurve = (WaveCurve) geomSource();

        int orbitBegin = 0;
        int orbitOffSet;

        List<WaveCurveOrbitGeom> waveCurveGeometries = new ArrayList<WaveCurveOrbitGeom>();


        int[] curvesType = waveCurve.getCurveTypes();

        for (int i = 0; i < curvesType.length; i++) {

            orbitOffSet = WaveCurve.getCurvesIndex()[i];

            //System.out.println("inicio: " + orbitBegin + " Fim: " + orbitOffSet);
            waveCurveGeometries.add(createOrbits(orbitBegin, orbitBegin + orbitOffSet, curvesType[i]));

            orbitBegin += orbitOffSet;

        }


        return new WaveCurveGeom(MultidAdapter.converseOrbitToCoordsArray(waveCurve), waveCurveGeometries, this);

    }

    private WaveCurveOrbitGeom createOrbits(int beginOfCurve, int endOfCurve, int curveType) {

        WaveCurve waveCurve = (WaveCurve) geomSource();
        OrbitPoint[] original = waveCurve.getPoints();
        OrbitPoint[] orbitPoints = Arrays.copyOfRange(original, beginOfCurve, endOfCurve);


        
        switch (curveType) {

            case 1://Rarefaction

                System.out.println("Rarefaction entre os indices : " +beginOfCurve + " e " +endOfCurve);

                //return new RarefactionGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbitPoints), this);

                return new RarefactionGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbitPoints), this, beginOfCurve, endOfCurve);


            case 2://Shock

                System.out.println("Shock entre os indices : " +beginOfCurve + " e " +endOfCurve);

                return new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbitPoints), this);


            case 3://Composite

                System.out.println("Composite entre os indices : " +beginOfCurve + " e " +endOfCurve);

                //return new CompositeGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbitPoints), this);

                return new CompositeGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbitPoints), this, beginOfCurve, endOfCurve);


            default:
                return null;
        }


        
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
