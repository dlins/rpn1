/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.util.List;
import rpn.controller.RarefactionExtensionController;
import rpn.controller.RpController;
import rpnumerics.FundamentalCurve;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.RarefactionExtensionCalc;
import rpnumerics.RarefactionExtensionCurve;
import rpnumerics.WaveCurveBranch;
import rpnumerics.WaveCurveOrbitCalc;
import wave.util.RealSegment;

public class RarefactionExtensionGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public RarefactionExtensionGeomFactory(RarefactionExtensionCalc calc) {
        super(calc);
    }
    
    
     public RarefactionExtensionGeomFactory(RarefactionExtensionCalc calc, RarefactionExtensionCurve curve) {
        super(calc,curve);
    }
    //
    // Accessors/Mutators
    //
    //
    // Methods
    //

    @Override
    public RpGeometry createGeomFromSource() {

        RarefactionExtensionCurve curve = (RarefactionExtensionCurve) geomSource();

       
        int resultSize = curve.segments().size();

        RealSegGeom[] hugoniotArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i));


        }

        return new RarefactionExtensionGeom(hugoniotArray, this);

    }


    @Override
     protected RpController createUI() {
        return new RarefactionExtensionController();
    }
    
    
    
    
    
     @Override
    public String toXML() {


        StringBuilder buffer = new StringBuilder();

        RarefactionExtensionCurve geomSource = (RarefactionExtensionCurve) geomSource();


        String curve_name = '\"' + geomSource.getClass().getSimpleName() + '\"';
        String dimension = '\"' + Integer.toString(RPNUMERICS.domainDim()) + '\"';

        RarefactionExtensionCalc orbitCalc = (RarefactionExtensionCalc) rpCalc();
        PhasePoint referencePoint = orbitCalc.getStart();
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
       buffer.append(geomSource.toXML());


        buffer.append("</" + Orbit.XML_TAG + ">" + "\n");

        return buffer.toString();

    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

   
}
