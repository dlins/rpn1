/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;

public class DoubleContactGeomFactory extends BifurcationCurveGeomFactory {

    public DoubleContactGeomFactory(DoubleContactCurveCalc calc, DoubleContactCurve curve) {
        super(calc, curve);
        
    }

    public DoubleContactGeomFactory(DoubleContactCurveCalc calc) {
        super(calc);
    }

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
