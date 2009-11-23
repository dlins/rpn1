/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import java.util.ArrayList;
import java.util.List;

import wave.multid.CoordsArray;
import wave.multid.view.*;
import wave.util.RealSegment;
import wave.util.RealVector;

import java.awt.*;



public class BifurcationCurve extends RPnCurve implements RpSolution {
    //
    // Members
    //
   
    private int familyIndex_;
    private List segments;

    //
    // Constructor
    //
    public BifurcationCurve(int familyIndex, ArrayList states) {
        super(coordsArrayFromRealSegments(states), new ViewingAttr(Color.white));

        familyIndex_ = familyIndex;
        segments = states;
       
    }

   

    public int findClosestSegment(RealVector targetPoint, double alpha) {
    	 RealVector target = new RealVector(targetPoint);
         RealVector closest = null;
         RealVector segmentVector = null;
         alpha = 0;
         int closestSegment = 0;
         double closestDistance = -1;

         List bifurcationSegment = segments();
         for (int i = 0; i < segments.size(); i++) {

             RealSegment segment = (RealSegment) bifurcationSegment.get(i);
             segmentVector = new RealVector(segment.p1());
             segmentVector.sub(segment.p1());
             closest = new RealVector(target);
             closest.sub(segment.p2());
             alpha = closest.dot(segmentVector) /
                     segmentVector.dot(segmentVector);
             if (alpha < 0) {
                 alpha = 0;
             }
             if (alpha > 1) {
                 alpha = 1;
             }
             segmentVector.scale(alpha);
             closest.sub(segmentVector);
             if ((closestDistance < 0) || (closestDistance > closest.norm())) {
                 closestSegment = i;
                 closestDistance = closest.norm();
             }
         }
         

         return closestSegment;
    }

    //
    // Accessors/Mutators
    //
  
    public int getFamilyIndex() {
        return familyIndex_;
    }

    private static CoordsArray[] coordsArrayFromRealSegments(List segments) {

        ArrayList tempCoords = new ArrayList(segments.size());
        for (int i = 0; i < segments.size(); i++) {
            RealSegment segment = (RealSegment) segments.get(i);
            tempCoords.add(new CoordsArray(segment.p1()));
            tempCoords.add(new CoordsArray(segment.p2()));

        }

        CoordsArray[] coords = new CoordsArray[tempCoords.size()];
        for (int i = 0; i < tempCoords.size(); i++) {
            coords[i] = (CoordsArray) tempCoords.get(i);
        }
        tempCoords = null;
        return coords;

    }
    
    public String toXML(boolean calcReady) {
        StringBuffer buffer = new StringBuffer();
        if (calcReady) {

            buffer.append("<BIFURCATIONCURVE>\n");

            for (int i = 0; i < segments.size(); i++) {

                HugoniotSegment hSegment = ((HugoniotSegment) segments.get(
                        i));
                RealSegment rSegment = new RealSegment(hSegment.leftPoint(),
                        hSegment.rightPoint());
                buffer.append(rSegment.toXML());

            }
            buffer.append("</BIFURCATIONCURVE>\n");


        }

        return buffer.toString();

    }
    
    public List segments() {
        return segments;
    }

}
