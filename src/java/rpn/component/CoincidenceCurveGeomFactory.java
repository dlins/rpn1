/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpnumerics.BifurcationCurve;
import rpnumerics.ContourCurveCalc;
import rpnumerics.CoincidenceCurve;
import rpnumerics.HugoniotSegment;
import rpnumerics.RPNUMERICS;
import wave.util.RealSegment;

public class CoincidenceCurveGeomFactory extends BifurcationCurveGeomFactory{

    public CoincidenceCurveGeomFactory(ContourCurveCalc calc) {
        super(calc);
    }

    public CoincidenceCurveGeomFactory(ContourCurveCalc calc,CoincidenceCurve curve) {
        super(calc,curve);
    }
  
    // Methods
    //
    public RpGeometry createGeomFromSource() {

        CoincidenceCurve curve = (CoincidenceCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        RealSegGeom[] hugoniotArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i));

        }
        return new CoincidenceCurveGeom(hugoniotArray, this);

    }
    
    
    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

        // TODO confirm that all geomSource() calls will return a SegmentedCurve instance type
        BifurcationCurve geomSource = (BifurcationCurve) geomSource();

        String curve_name = '\"' + geomSource.getClass().getSimpleName() + '\"';
        String dimension = '\"' + Integer.toString(RPNUMERICS.domainDim())  + '\"';

        //
        // PRINTS OUT THE CURVE ATTS
        //
        buffer.append("<" + BifurcationCurve.XML_TAG + " curve_name=" + ' ' + curve_name + ' ' +  "dimension=" + ' ' +  dimension + ' ' +  "format_desc=\"1 segment per row\">" + "\n");

        //
        // PRINTS OUT THE CONFIGURATION INFORMATION
        //
        if(rpCalc().getConfiguration()!=null)
        buffer.append(rpCalc().getConfiguration().toXML());
        
           
        //
        // PRINTS OUT THE SEGMENTS COORDS
        //
        buffer.append("<" + BifurcationCurve.LEFT_TAG +">" + "\n");
        
        for (int i = 0; i < geomSource.leftSegments().size(); i++) {

            RealSegment realSegment =(RealSegment) geomSource.leftSegments().get(i);
            buffer.append(realSegment.toXML());
        }
        buffer.append("</" + BifurcationCurve.LEFT_TAG +">" + "\n");

        
      
      buffer.append("</" + BifurcationCurve.XML_TAG +">" );

        return buffer.toString();
    }

    
    
    

}
