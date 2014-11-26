/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpn.controller.LevelCurveController;
import rpn.controller.RpController;
import rpnumerics.*;
import wave.util.RealSegment;
import wave.util.RealVector;

public class LevelCurveGeomFactory extends RpCalcBasedGeomFactory {

    public LevelCurveGeomFactory(CharacteristicPolynomialLevelCalc calc) {
        super(calc);


    }
    
      public LevelCurveGeomFactory(CharacteristicPolynomialLevelCalc calc,EigenValueCurve curve) {
        super(calc,curve);


    }


    //
    // Methods
    //
    public RpGeometry createGeomFromSource() {
        
        Color color = Color.yellow;
        if (geomSource() instanceof EigenValueCurve){
            EigenValueCurve curve = (EigenValueCurve)geomSource();
            color = selectViewingAttribute(curve.getFamily());
        }
        

     CharacteristicsPolynomialCurve curve = (CharacteristicsPolynomialCurve) geomSource();
        int resultSize = curve.segments().size();
        RealSegGeom[] realSegArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            realSegArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i));
            realSegArray[i].viewingAttr().setColor(color);
        }
        return new LevelCurveGeom(realSegArray, this);

    }

    @Override
    protected RpController createUI() {
        return new LevelCurveController();
    }

    private Color selectViewingAttribute(int family) {

        switch (family) {

            case 0:
                return Color.blue;

            case 1:

                return Color.red;


        }

        return null;

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


        EigenValueCurve geomSource = (EigenValueCurve) geomSource();


        String curve_name = '\"' + geomSource.getClass().getSimpleName() + '\"';
        String dimension = '\"' + Integer.toString(RPNUMERICS.domainDim()) + '\"';

        CharacteristicPolynomialLevelCalc levelCalc = (CharacteristicPolynomialLevelCalc) rpCalc();


        //
        // PRINTS OUT THE CURVE ATTS
        //
        buffer.append("<").append(Orbit.XML_TAG).append(" curve_name=" + ' ').append(curve_name).append(' ' + " dimension=" + ' ').append(dimension);


        if (levelCalc instanceof EigenValuePointLevelCalc) {

            EigenValuePointLevelCalc pointLevelCalc = (EigenValuePointLevelCalc) levelCalc;
            RealVector startPoint = pointLevelCalc.getStartPoint();

            buffer.append(' ' + " startpoint=\"").append(startPoint);
            
            buffer.append('\"');


        }



        buffer.append(" format_desc=\"1 segment per row\">" + "\n");

        //
        // PRINTS OUT THE CONFIGURATION INFORMATION
        //
        buffer.append(levelCalc.getConfiguration().toXML());

        //
        // PRINTS OUT THE SEGMENTS COORDS
        //
        buffer.append(geomSource.toXML());

        buffer.append("</" + Orbit.XML_TAG + ">" + "\n");

        return buffer.toString();

    }
}
