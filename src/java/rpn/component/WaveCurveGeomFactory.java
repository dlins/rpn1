/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import rpnumerics.CompositeCurve;
import rpnumerics.RarefactionOrbit;
import rpnumerics.ShockCurve;
import rpnumerics.WaveCurve;
import rpnumerics.WaveCurveCalc;
import rpnumerics.WaveCurveOrbit;
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
    protected RpGeometry createGeomFromSource() {       // entra aqui uma vez para cada WC

        WaveCurve waveCurve = (WaveCurve) geomSource();
        //waveCurve.segments(); //RealSegment (uniao de todos os branchs)

        System.out.println("waveCurve.getSubCurvesList().size() :::::::::::::::: " +waveCurve.getSubCurvesList().size());

        WaveCurveOrbit firstOrbit = waveCurve.getSubCurvesList().get(0);
        
        WaveCurveGeom wcGeom = new WaveCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(firstOrbit.getPoints()), this);
        WaveCurveGeom wcGeomComposite = new WaveCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(firstOrbit.getPoints()), this);

        
        for (WaveCurveOrbit waveCurveOrbit : waveCurve.getSubCurvesList()) {
            //wcGeom.add(createOrbits(waveCurveOrbit));
            wcGeomComposite.add(createOrbits(waveCurveOrbit));
        }

        wcGeom.add(wcGeomComposite);

        return wcGeom;
        //return wcGeomComposite;

    }

    private WaveCurveOrbitGeom createOrbits(WaveCurveOrbit branch) {

        System.out.println(branch.getClass().getCanonicalName());

        if (branch instanceof RarefactionOrbit) {

            System.out.println("Dentro do createOrbits de WaveCurveGeomFactory : Rarefaction --- " +branch.getPoints().length);
            return new RarefactionGeom(MultidAdapter.converseOrbitPointsToCoordsArray(branch.getPoints()), this, (RarefactionOrbit)branch);

        }

        if (branch instanceof ShockCurve) {

            System.out.println("Dentro do createOrbits de WaveCurveGeomFactory : Shock --- " +branch.getPoints().length);
            //return new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(branch.getPoints()), this);
            return new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(branch.getPoints()), this, (ShockCurve)branch);

        }

        if (branch instanceof CompositeCurve) {

            System.out.println("Dentro do createOrbits de WaveCurveGeomFactory : Composite --- " +branch.getPoints().length);
            return new CompositeGeom(MultidAdapter.converseOrbitPointsToCoordsArray(branch.getPoints()), this, (CompositeCurve)branch);

        }


        return null;

    }


     @Override
      protected ViewingAttr selectViewingAttr() {
        int family = (((WaveCurve) this.geomSource()).getFamily());

        if (family == 1) {
            return new ViewingAttr(Color.red);
        }
        if (family == 0) {
            return new ViewingAttr(Color.blue);
        }
        return null;
      }


    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
