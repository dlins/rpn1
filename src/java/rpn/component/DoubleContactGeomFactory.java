/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;

public class DoubleContactGeomFactory extends BifurcationCurveGeomFactory {

    private static ViewingAttr viewAtt_ = new ViewingAttr(Color.white);

    public DoubleContactGeomFactory(DoubleContactCurveCalc calc, DoubleContactCurve curve) {
        super(calc, curve);
        
    }

    public DoubleContactGeomFactory(DoubleContactCurveCalc calc) {
        super(calc);
    }

    //
    // Methods
    //
//    @Override
//    protected RpGeometry createGeomFromSource() {
//
//        DoubleContactCurve curve = (DoubleContactCurve) geomSource();
//
//        RealSegGeom[] leftBifurcationSegArray = null;
//
//        int resultSize = curve.segments().size();
//        System.out.println("Tamanho da curve em DoubleContactGeomFactory , metodo createGeomFromSource() : " + resultSize);        //*******************************8
//
//        leftBifurcationSegArray = new RealSegGeom[resultSize];
//        for (int i = 0; i < resultSize; i++) {
//            leftBifurcationSegArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i), viewAtt_);
//
//        }
//
//        return new DoubleContactCurveGeom(leftBifurcationSegArray, this);
//
//    }



    @Override
    protected ViewingAttr leftViewingAttr(){
        return new ViewingAttr(Color.yellow);
    }


    @Override
    protected ViewingAttr rightViewingAttr() {
        return new ViewingAttr(Color.magenta);
    }

    @Override
    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append(super.toXML());

        BifurcationCurve curve = (BifurcationCurve) geomSource();

        DoubleContactCurveCalc doubleContactCalc = (DoubleContactCurveCalc) rpCalc();

        buffer.append(" curvefamily=\"" + doubleContactCalc.getCurveFamily() + "\"" + " domainfamily=\"" + doubleContactCalc.getDomainFamily() + "\"" + ">\n");

        buffer.append(curve.toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();

    }
}
