package rpn.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public BifurcationCurveGeomFactory(BifurcationCurveCalc calc) {
        super(calc);
    }

    protected RpGeometry createGeomFromSource() {

        BifurcationCurve curve = (BifurcationCurve) geomSource();
        ArrayList<RpGeometry> geometryArray = new ArrayList<RpGeometry>();

        int resultSize = curve.leftSegments().size();

        BifurcationSegGeom[] bifurcationArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            bifurcationArray[i] = new BifurcationSegGeom((RealSegment) curve.leftSegments().get(i));

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

                    BifurcationSegGeom[] bifurcationSegGeoms = new BifurcationSegGeom[tempSegmentArray.size()];

                    for (int i = 0; i < tempSegmentArray.size(); i++) {
                        BifurcationSegGeom bifurcationSegment = new BifurcationSegGeom(tempSegmentArray.get(i));
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

        buffer.append("<BIFURCATIONCALC >\n");

        buffer.append(((BifurcationCurve) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));

        buffer.append("</BIFURCATIONCALC>\n");

        return buffer.toString();
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
