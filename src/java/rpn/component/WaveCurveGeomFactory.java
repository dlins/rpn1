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
import rpnumerics.RPnCurve;
import rpnumerics.RarefactionCurve;
import rpnumerics.ShockCurve;
import rpnumerics.WaveCurve;
import rpnumerics.WaveCurveBranch;
import rpnumerics.FundamentalCurve;
import rpnumerics.WaveCurveOrbitCalc;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;

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

    public WaveCurveGeomFactory(WaveCurveOrbitCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    @Override
    public RpGeometry createGeomFromSource() {

        WaveCurve waveCurve = (WaveCurve) geomSource();
        List<RealSegment> list = waveCurve.segments();
        
        System.out.println("Tamanho da lista de branches: "+ waveCurve.getBranchsList().size());

        WaveCurveGeom wcGeom = new WaveCurveGeom(MultidAdapter.converseRealSegmentsToCoordsArray(list), this);

        for (WaveCurveBranch branch : waveCurve.getBranchsList()) {

            List<RealSegment> segList = new ArrayList<RealSegment>();
           


            for (WaveCurveBranch leaf : branch.getBranchsList()) {
                segList.addAll(((RPnCurve) leaf).segments());

            }
            
            
             
          

            WaveCurveGeom wcGeomComposite = new WaveCurveGeom(MultidAdapter.converseRealSegmentsToCoordsArray(segList), this);

            for (WaveCurveBranch waveCurveBranch : branch.getBranchsList()) {
                wcGeomComposite.add(createOrbits((FundamentalCurve) waveCurveBranch));
            }

            wcGeom.add(wcGeomComposite);

        }

        return wcGeom;

    }

    private WaveCurveOrbitGeom createOrbits(FundamentalCurve branch) {

        System.out.println(branch.getClass().getCanonicalName());

        if (branch instanceof RarefactionCurve) {

//            System.out.println("Dentro do createOrbits de WaveCurveGeomFactory : Rarefaction --- " +branch.getPoints().length);
            return new RarefactionCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(branch.getPoints()), this, (RarefactionCurve) branch);

        }

        if (branch instanceof ShockCurve) {

//            System.out.println("Dentro do createOrbits de WaveCurveGeomFactory : Shock --- " +branch.getPoints().length);

                
            System.out.println("Em factory: " +branch.toXML());
                

            return new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(branch.getPoints()), this, (ShockCurve) branch);

        }

        if (branch instanceof CompositeCurve) {

//            System.out.println("Dentro do createOrbits de WaveCurveGeomFactory : Composite --- " +branch.getPoints().length);
            return new CompositeGeom(MultidAdapter.converseOrbitPointsToCoordsArray(branch.getPoints()), this, (CompositeCurve) branch);

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
}
