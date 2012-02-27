package rpn.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.parser.RPnDataModule;
import rpn.usecase.BifurcationRefineAgent;
import rpnumerics.*;
import rpnumerics.methods.contour.support.CurveDomainManager;
import rpnumerics.methods.contour.support.DimensionDoenstMatch;
import rpnumerics.methods.contour.support.NoContourMethodDefined;
import wave.util.*;

public class BifurcationCurveGeomFactory extends RpCalcBasedGeomFactory {

    public BifurcationCurveGeomFactory(ContourCurveCalc calc) {
        super(calc);
    }


     public BifurcationCurveGeomFactory(ContourCurveCalc calc,RpSolution curve) {
        super(calc,curve);
    }

    protected RpGeometry createGeomFromSource() {

        BifurcationCurve curve = (BifurcationCurve) geomSource();
       

        int resultSize = curve.leftSegments().size();

        RealSegGeom[] bifurcationArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            bifurcationArray[i] = new RealSegGeom((RealSegment) curve.leftSegments().get(i));

        }
        return new BifurcationCurveGeom(bifurcationArray, this);
        

//        int rightResultSize = curve.rightSegments().size();
//
//        BifurcationSegGeom[] bifurcationRightArray = new BifurcationSegGeom[rightResultSize];
//        for (int i = 0; i < rightResultSize; i++) {
//            bifurcationArray[i] = new BifurcationSegGeom((RealSegment) curve.rightSegments().get(i));
//
//        }
//        geometryArray.add(new BifurcationCurveGeom(bifurcationRightArray, this));
//
//        return geometryArray;


    }

    public RpGeometry refine() {
        try {

            ArrayList<Area> areaArray = rpnumerics.RPNUMERICS.getBifurcationProfile().getSelectedAreas();
            System.out.println("Chamando refine do factory");
            CurveDomainManager.instance().repositionAreas(areaArray);

            Iterator geomIterator = RPnDataModule.AUXPHASESPACE.getGeomObjIterator();

            while (geomIterator.hasNext()) {
                RpGeometry geom = (RpGeometry) geomIterator.next();

                if (geom instanceof BifurcationCurveGeom) {
                    BifurcationCurveGeomFactory factory = (BifurcationCurveGeomFactory) geom.geomFactory();

                    BifurcationCurve bifurcationCurve = (BifurcationCurve) factory.geomSource();
                    if (areaArray.get(areaArray.size() - 1) == null) {
                        System.out.println("Area nula");
                    }
                    RPnCurve result = CurveDomainManager.instance().fillSubDomain(bifurcationCurve, areaArray.get(areaArray.size() - 1));

                    ArrayList<RealSegment> tempSegmentArray = MultidAdapter.converseRPnCurveToRealSegments(result);

                    RealSegGeom[] bifurcationSegGeoms = new RealSegGeom[tempSegmentArray.size()];

                    for (int i = 0; i < tempSegmentArray.size(); i++) {
                        RealSegGeom bifurcationSegment = new RealSegGeom(tempSegmentArray.get(i));
                        bifurcationSegGeoms[i] = bifurcationSegment;
                    }
                    BifurcationCurveGeom bifurcationGeom = new BifurcationCurveGeom(bifurcationSegGeoms, factory);

                    return bifurcationGeom;


                }
            }


        } catch (NoContourMethodDefined ex) {
            Logger.getLogger(BifurcationRefineAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DimensionDoenstMatch ex) {
            Logger.getLogger(BifurcationRefineAgent.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public String toXML() {
        StringBuffer buffer = new StringBuffer();

        String commandName = geomSource().getClass().getName();
        commandName = commandName.toLowerCase();
        commandName = commandName.replaceAll(".+\\.", "");

        ContourCurveCalc calc = ((ContourCurveCalc) rpCalc());

        ContourParams params = calc.getParams();
        
        buffer.append("<COMMAND name=\""+commandName+"\" ");

        buffer.append(params.toString());


        return buffer.toString();
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
