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
    static public List listResolution = new ArrayList();
    static public String namePhaseSpace = "";

    
    public RPnCurve findClosestCurve(RealVector targetPoint) {

        RPnCurve closestCurve_ = null;      //a curva mais proxima

        listResolution.clear();
    
        double distminCurve = 1000000.;
        double distancia = 0.;
        int k = 0;
        Iterator<RpGeometry> geomList = null ;
        //--------------------------

        if (namePhaseSpace.equals("Phase Space"))      geomList = RPnDataModule.PHASESPACE.getGeomObjIterator();
        if (namePhaseSpace.equals("RightPhase Space")) geomList = RPnDataModule.RIGHTPHASESPACE.getGeomObjIterator();
        if (namePhaseSpace.equals("LeftPhase Space"))  geomList = RPnDataModule.LEFTPHASESPACE.getGeomObjIterator();
        
        //Iterator<RpGeometry> geomList = RPnDataModule.PHASESPACE.getGeomObjIterator();
        
            while (geomList.hasNext()) {

            RpGeometry geom = (RpGeometry) geomList.next();

                if (GeometryGraphND.onCurve == 1) {

                    if ((namePhaseSpace.equals("Phase Space")  &&  geom != RPnDataModule.PHASESPACE.getLastGeometry())
                            || (namePhaseSpace.equals("RightPhase Space")  &&  geom != RPnDataModule.RIGHTPHASESPACE.getLastGeometry())
                            || (namePhaseSpace.equals("LeftPhase Space")  &&  geom != RPnDataModule.LEFTPHASESPACE.getLastGeometry())) {

                        if (geom.viewingAttr().isVisible()) {

                            RpGeomFactory factory = geom.geomFactory();
                            RPnCurve curve = (RPnCurve) factory.geomSource();

                            curve.findClosestSegment(targetPoint);   //***

                            distancia = curve.distancia;
                            
                            if (distminCurve >= distancia) {
                                distminCurve = distancia;
                                closestCurve = k;
                                closestCurve_ = curve;
                            }

                        }
                    }

                }

                if (GeometryGraphND.onCurve == 0) {

                    if (geom.viewingAttr().isVisible()) {

                        RpGeomFactory factory = geom.geomFactory();
                        RPnCurve curve = (RPnCurve) factory.geomSource();

                        if (curve instanceof SegmentedCurve) {
                            RpCalcBasedGeomFactory geomFactory = (RpCalcBasedGeomFactory) factory;
                            RpCalculation calc = geomFactory.rpCalc();
                            ContourCurveCalc curveCalc = (ContourCurveCalc) calc;
                            listResolution.add(curveCalc.getParams().getResolution());

                        } else {
                            int[] resolution = {1, 1};
                            listResolution.add(resolution);
                        }

                        curve.findClosestSegment(targetPoint);   //***
                        
                        distancia = curve.distancia;
                        
                        if (distminCurve >= distancia) {
                            distminCurve = distancia;
                            closestCurve = k;
                            closestCurve_ = curve;
                        }

                    }

                }

            k++;

        }
        
        
        //--------------------------

        return closestCurve_;   // de todas as curvas no painel

    }

    

}
