/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import rpnumerics.*;
import rpn.controller.HugoniotController;
import rpn.controller.RpController;
import wave.multid.CoordsArray;
import wave.multid.model.MultiPoint;
import wave.multid.view.ViewingAttr;
import wave.util.RealVector;

public class HugoniotCurveGeomFactory extends RpCalcBasedGeomFactory {

    public HugoniotCurveGeomFactory(HugoniotCurveCalc calc) {
        super(calc);


    }
    
    
     public HugoniotCurveGeomFactory(HugoniotCurveCalc calc,HugoniotCurve curve) {
        super(calc,curve);


    }

    @Override
    protected RpController createUI() {
        return new HugoniotController();
    }

    //
    // Methods
    //
    public RpGeometry createGeomFromSource() {

        HugoniotCurve curve = (HugoniotCurve) geomSource();
        
        List<MultiPoint> multiPointList = new ArrayList();
        for (RealVector multiPoint : curve.getTransitionList()) {
          MultiPoint  transPoint = new MultiPoint(new CoordsArray(multiPoint), new ViewingAttr(Color.yellow));
          multiPointList.add(transPoint);
        }

        HugoniotSegGeom.DIRECTION = curve.getDirection();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();

        HugoniotSegGeom[] hugoniotArray = new HugoniotSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.segments().get(i));
        }
        HugoniotCurveGeom geom = new HugoniotCurveGeom(hugoniotArray, this);
        
        
        geom.setTransitionList(multiPointList);
        
        return geom;

    }

    private String createAxisLabel3D(int x, int y, int z) {

        String axisName[] = new String[3];

        axisName[0] = "s";
        axisName[1] = "T";
        axisName[2] = "u";


        StringBuffer buffer = new StringBuffer();
        buffer.append("xlabel('");
        buffer.append(axisName[x] + "')\n");
        buffer.append("ylabel('" + axisName[y] + "')\n");

        buffer.append("zlabel('" + axisName[z] + "')\n");

        return buffer.toString();



    }
    
    @Override
     public String toXML() {


        StringBuilder buffer = new StringBuilder();
        

        HugoniotCurve  geomSource = (HugoniotCurve) geomSource();
        PhasePoint  referencePoint = geomSource.getXZero();

        String curve_name = '\"' + geomSource.getClass().getSimpleName() + '\"';
        String dimension = '\"' + Integer.toString(RPNUMERICS.domainDim())  + '\"';
        
        HugoniotCurveCalc hugoniotCalc = (HugoniotCurveCalc) rpCalc();

        //
        // PRINTS OUT THE CURVE ATTS
        //
        buffer.append("<").append(Orbit.XML_TAG).append(" curve_name=" + ' ').append(curve_name).append(' ' + " dimension=" + ' ').append(dimension).append(' ' + " startpoint=\"").append(referencePoint.getCoords()).append('\"' +
                        " format_desc=\"1 segment per row\">" + "\n");

        //
        // PRINTS OUT THE CONFIGURATION INFORMATION
        //
        buffer.append(hugoniotCalc.getConfiguration().toXML());

        //
        // PRINTS OUT THE SEGMENTS COORDS
        //
        buffer.append(geomSource.toXML());

        buffer.append("</" + Orbit.XML_TAG + ">" + "\n");

        return buffer.toString();

    }
    
    
    
    

}
