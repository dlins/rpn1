/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component.util;

//import java.awt.geom.Area;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
//import java.util.Vector;
//import rpn.RPnPhaseSpacePanel;
//import rpn.component.MultidAdapter;
//import rpnumerics.HugoniotCurve;
//import java.util.Map.Entry;
//import java.util.Set;
//import rpn.component.MultidAdapter;
//import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
//import rpn.parser.RPnDataModule;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotSegment;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
//import rpnumerics.RarefactionOrbit;
import rpnumerics.SegmentedCurve;
//import wave.multid.model.MultiGeometry;
import wave.util.RealSegment;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class GeometryUtil {

    static public RealVector pMarca = new RealVector(RPNUMERICS.domainDim());
    static public RealVector targetPoint = new RealVector(RPNUMERICS.domainDim());
    static public RealVector cornerRet = new RealVector(RPNUMERICS.domainDim());

    static public int closestCurve;
    static public List zContido = new ArrayList();
    static public List wContido = new ArrayList();
    static public List indContido = new ArrayList();      //** isso é uma lista de indices (Leandro)

    static public List tipo = new ArrayList();            //** para classificar os segmentos (Leandro)
    static public List vel = new ArrayList();             //** para as velocidades (Leandro)


    public static int findClosestCurve(RealVector targetPoint, List <RPnCurve> curvelist_, double alpha) {

        RealVector target = new RealVector(targetPoint);
        alpha = 0.;

        double[] distCurve = new double[curvelist_.size()];   //** guarda as distancias entre targetPoint e o segmento mais proximo de cada curva, tem o tamanho da lista de curvas. (Leandro)
        int[] cadaSegIndex = new int[curvelist_.size()];
        double[][] pCurve = new double[curvelist_.size()][RPNUMERICS.domainDim()];

        //System.out.println("Valor de curvelist_.size() : " +curvelist_.size());

        double distminCurve = 0, distproxCurve;

        RealSegment[] segmento = new RealSegment[curvelist_.size()];
        OrbitPoint[] segOrbit = new OrbitPoint[curvelist_.size()];

        for (int i = 0; i < curvelist_.size(); i++) {

            if (curvelist_.get(i) instanceof SegmentedCurve) {

                SegmentedCurve curve = (SegmentedCurve) curvelist_.get(i);

                cadaSegIndex[i] = curve.findClosestSegment(target, alpha);

                distCurve[i] = curve.distancia;

                segmento[i] =  (RealSegment) (curve.segments()).get(cadaSegIndex[i]);

                for (int j = 0; j < RPNUMERICS.domainDim(); j++) {
                    pCurve[i][j] = segmento[i].p1().getElement(j);
                }

            }

            if (curvelist_.get(i) instanceof Orbit){

                Orbit curve = (Orbit) curvelist_.get(i);
                cadaSegIndex[i] = curve.findClosestSegment(target, alpha);

                distCurve[i] = curve.distancia;

                segOrbit[i] = curve.getPoints()[cadaSegIndex[i]];

                for (int j = 0; j < RPNUMERICS.domainDim(); j++) {
                    pCurve[i][j] = segOrbit[i].getElement(j);
                }

            }
        }

        distminCurve = distCurve[0];

        for (int i = 0; i < distCurve.length; i++){
            distproxCurve = distCurve[i];
            if (distproxCurve <= distminCurve) {
                distminCurve = distproxCurve;
                closestCurve = i;
            }
        }

        System.out.println("Curva mais proxima:");
        System.out.println(closestCurve);

        System.out.println("Target point:");
        for (int j = 0; j < RPNUMERICS.domainDim(); j++) {
            System.out.println(target.getElement(j));
        }

        System.out.println("Ponto mais proximo:");
        for (int j = 0; j < RPNUMERICS.domainDim(); j++) {
            System.out.println(pCurve[closestCurve][j]);
            pMarca.setElement(j, pCurve[closestCurve][j]);
        }

        //****************
        if (UIController.instance().getState() instanceof CLASSIFIERAGENT_CONFIG  &&  curvelist_.get(closestCurve) instanceof HugoniotCurve) {
            HugoniotSegment segment = (HugoniotSegment) segmento[closestCurve];
            tipo.add(segment.getType());
        }

        //****************
        if (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG  &&  curvelist_.get(closestCurve) instanceof Orbit) {
            OrbitPoint pontoOrbit = (OrbitPoint) segOrbit[closestCurve];
            vel.add(pontoOrbit.getLambda());
        }
        //***************

        if (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG  &&  curvelist_.get(closestCurve) instanceof HugoniotCurve) {
            HugoniotSegment segment = (HugoniotSegment) segmento[closestCurve];
            vel.add(segment.leftSigma());
        }
        //***************

        return closestCurve;   // de todas as curvas no painel

    }

}
