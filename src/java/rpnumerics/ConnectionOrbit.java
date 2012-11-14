/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealVector;
import wave.multid.view.ViewingAttr;
import java.awt.Color;
import rpn.component.MultidAdapter;

public class ConnectionOrbit extends RpCurve implements RpSolution {
    //
    // Members
    //
    private StationaryPoint uMinus_;
    private StationaryPoint uPlus_;
    private Orbit orbit_;

    //
    // Constructors
    //
    public ConnectionOrbit(StationaryPoint uMinus, StationaryPoint uPlus, Orbit orbit) {
        super(MultidAdapter.converseOrbitPointsToCoordsArray(orbit.getPoints()), new ViewingAttr(Color.orange));
        uMinus_ = uMinus;
        uPlus_ = uPlus;
        orbit_ = orbit;
    }
    //
    // Methods
    //
    public RealVector orbitCenter() {


        OrbitPoint centerOPoint = orbit_.getPoints()[0];
        RealVector center = new RealVector(centerOPoint.getCoords());
//        ShockFlow flow = (ShockFlow) RPNUMERICS.createShockFlow();

//        WaveState input = new WaveState(new PhasePoint(center));
//        JetMatrix output = new JetMatrix(center.getSize());
//
//        flow.jet(input, output, 0);

//        RealVector fCenter = flow.flux(center);
        
        

//        RealVector fCenter = flow.flux(center);
//        RealVector fCenter = RPNUMERICS.flow().flux(center);
        for (int i = 1; i < orbit_.getPoints().length; i++) {
            centerOPoint = orbit_.getPoints()[i];
            RealVector nextCenter = new RealVector(centerOPoint.getCoords());
            
//            WaveState in = new WaveState(new PhasePoint(nextCenter));
//            
//            flow.jet(in, output, 0);
//            RealVector nextFCenter = flow.flux(nextCenter);
//            RealVector nextFCenter = flow.flux(nextCenter);
//            RealVector nextFCenter = RPNUMERICS.flow().flux(nextCenter);
//            if (fCenter.norm() < nextFCenter.norm()) {
//                fCenter.set(nextFCenter);
//                center.set(nextCenter);
//            }
       }
        return center;
    }


    public int findClosestSegment(RealVector point, double alpha) {
        return 0;
    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<CONNECTIONORBIT>\n");
        buffer.append("<UMINUS>\n");
        buffer.append(uMinus().toXML());
        buffer.append("</UMINUS>\n");

        buffer.append("<UPLUS>\n");
        buffer.append(uPlus().toXML());
        buffer.append("</UPLUS>\n");

        buffer.append(orbit().toXML());

        buffer.append("</CONNECTIONORBIT>");

        return buffer.toString();


    }

    public String toXML(boolean calcReady) {

        StringBuffer buffer = new StringBuffer();
        if (calcReady) {
            buffer.append("<CONNECTIONORBIT>\n");
            buffer.append("<UMINUS>\n");
            buffer.append(uMinus().toXML());
            buffer.append("</UMINUS>\n");

            buffer.append("<UPLUS>\n");
            buffer.append(uPlus().toXML());
            buffer.append("</UPLUS>\n");

            buffer.append(orbit().toXML());

            buffer.append("</CONNECTIONORBIT>");
        }

        return buffer.toString();

    }

    //
    // Accessors/Mutators
    //
    public StationaryPoint uMinus() {
        return uMinus_;
    }

    public StationaryPoint uPlus() {
        return uPlus_;
    }

    public Orbit orbit() {
        return orbit_;
    }

    @Override
    public List segments() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
