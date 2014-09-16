/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import rpn.controller.DiagramController;
import rpn.controller.RpController;
import rpnumerics.CompositeCurve;
import rpnumerics.RPnCurve;
import rpnumerics.RarefactionCurve;
import rpnumerics.ShockCurve;
import rpnumerics.WaveCurve;
import rpnumerics.WaveCurveBranch;
import rpnumerics.FundamentalCurve;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.RpSolution;
import rpnumerics.WaveCurveOrbitCalc;
import rpnumerics.WaveCurveRRegions;
import rpnumerics.WaveCurveRRegionsCalc;
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
    int waveCurveIndexForRRegions_;
    
    public WaveCurveGeomFactory(WaveCurveOrbitCalc calc) {
        super(calc);
    }
    
    
     public WaveCurveGeomFactory(WaveCurveRRegionsCalc calc,WaveCurve curve,int waveCurveIndex) {
        super(calc,curve);
        waveCurveIndexForRRegions_ = waveCurveIndex;
        

    }
    
     
       @Override
    protected RpController createUI() {
        
        return new DiagramController();
    }


    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    @Override
    public RpGeometry createGeomFromSource() {

        RpSolution solution = (RpSolution) geomSource();
        
        WaveCurve waveCurve =null;
        
        if (solution instanceof WaveCurve)
        
            waveCurve  = (WaveCurve) geomSource();
        
        if (solution instanceof WaveCurveRRegions){
            
            WaveCurveRRegions rRegions = (WaveCurveRRegions)geomSource();

            waveCurve = rRegions.getWaveCurve(waveCurveIndexForRRegions_);
            
        }
        
        List<RealSegment> list = waveCurve.segments();

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

        if (branch instanceof RarefactionCurve) {
            return new RarefactionCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(branch.getPoints()), this, (RarefactionCurve) branch);
        }

        if (branch instanceof ShockCurve) {
            return new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(branch.getPoints()), this, (ShockCurve) branch);
        }

        if (branch instanceof CompositeCurve) {
            return new CompositeGeom(MultidAdapter.converseOrbitPointsToCoordsArray(branch.getPoints()), this, (CompositeCurve) branch);
        }


        return null;

    }

    @Override
    protected ViewingAttr selectViewingAttr() {
        
         RpSolution solution = (RpSolution) geomSource();
         WaveCurve waveCurve =null;
        
        if (solution instanceof WaveCurve)
        
            waveCurve  = (WaveCurve) geomSource();
        
        if (solution instanceof WaveCurveRRegions){
            
            WaveCurveRRegions rRegions = (WaveCurveRRegions)geomSource();

            waveCurve = rRegions.getWaveCurve(waveCurveIndexForRRegions_);
            
        }
        
        
        
        int family = waveCurve.getFamily();

        if (family == 1) {
            return new ViewingAttr(Color.red);
        }
        if (family == 0) {
            return new ViewingAttr(Color.blue);
        }
        return null;
    }

    @Override
    public String toXML() {


        StringBuilder buffer = new StringBuilder();

        WaveCurve geomSource = (WaveCurve) geomSource();
        OrbitPoint referencePoint = geomSource.getReferencePoint();

        String curve_name = '\"' + geomSource.getClass().getSimpleName() + '\"';
        String dimension = '\"' + Integer.toString(RPNUMERICS.domainDim()) + '\"';

        WaveCurveOrbitCalc orbitCalc = (WaveCurveOrbitCalc) rpCalc();

        //
        // PRINTS OUT THE CURVE ATTS
        //
        buffer.append("<").append(Orbit.XML_TAG).append(" curve_name=" + ' ').append(curve_name).append(' ' + " dimension=" + ' ').append(dimension).append(' ' + " startpoint=\"").append(referencePoint.getCoords()).append('\"'
                + " format_desc=\"1 segment per row\">" + "\n");

        //
        // PRINTS OUT THE CONFIGURATION INFORMATION
        //
        buffer.append(orbitCalc.getConfiguration().toXML());
        //
        // PRINTS OUT THE SEGMENTS COORDS
        //
        List<WaveCurveBranch> branchsList = geomSource.getBranchsList();
        
        for (WaveCurveBranch waveCurveBranch : branchsList) {
            FundamentalCurve fundamentalCurve = (FundamentalCurve)waveCurveBranch;
            String branch_name = fundamentalCurve.getClass().getSimpleName();
            
            buffer.append("<SUBCURVE name=\"").append(branch_name).append("\">\n");
            buffer.append(fundamentalCurve.toXML());
            buffer.append("</SUBCURVE>\n");
                
            

        }


        buffer.append("</" + Orbit.XML_TAG + ">" + "\n");

        return buffer.toString();

    }
}
