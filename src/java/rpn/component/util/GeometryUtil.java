/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.parser.RPnDataModule;
import rpnumerics.ContourCurveCalc;
import rpnumerics.RPnCurve;
import rpnumerics.RpCalculation;
import rpnumerics.SegmentedCurve;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class GeometryUtil {

    static public int closestCurve;             //indice da curva mais proxima
    static public RPnCurve closestCurve_ ;      //a curva mais proxima
    static public int closestSeg ;              //indice do segmento mais proximo
    static public List listResolution = new ArrayList();

    
    public static RPnCurve findClosestCurve(RealVector targetPoint) {

        listResolution.clear();
    
        double distminCurve = 1000000.;
        double distancia = 0.;
        int k = 0;
        int seg = 0;

        //--------------------------
        Iterator<RpGeometry> geomList = RPnDataModule.PHASESPACE.getGeomObjIterator();

        while (geomList.hasNext()) {

            RpGeometry geom = (RpGeometry) geomList.next();

            if (geom.viewingAttr().isVisible()) {

//            if (curve instanceof SegmentedCurve) {
//                SegmentedCurve curveSeg = (SegmentedCurve)curve;
//                seg = curveSeg.findClosestSegment(targetPoint);
//                distancia = curveSeg.distancia;
//            }
//            if (curve instanceof Orbit) {
//                Orbit curveOrb = (Orbit)curve;
//                seg = curveOrb.findClosestSegment(targetPoint);
//                distancia = curveOrb.distancia;
//            }

                RpGeomFactory factory = geom.geomFactory();
                RPnCurve curve = (RPnCurve) factory.geomSource();

                if (curve instanceof SegmentedCurve) {
                    RpCalcBasedGeomFactory geomFactory = (RpCalcBasedGeomFactory) factory;
                    RpCalculation calc = geomFactory.rpCalc();
                    ContourCurveCalc curveCalc = (ContourCurveCalc) calc;
                    listResolution.add(curveCalc.getParams().getResolution());

                }
                else {
                    int[] resolution = {1, 1};
                    listResolution.add(resolution);
                }
                
                seg = curve.findClosestSegment(targetPoint);
                distancia = curve.distancia;


                if (distminCurve >= distancia) {
                    distminCurve = distancia;
                    closestCurve = k;
                    closestCurve_ = curve;
                    closestSeg = seg;
                }

            }

            k++;

        }
        //--------------------------

        return closestCurve_;   // de todas as curvas no painel

    }

    

}
