/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;

public class SecondaryBifurcationGeomFactory extends BifurcationCurveGeomFactory {

    public SecondaryBifurcationGeomFactory(SecondaryBifurcationCurveCalc calc, SecondaryBifurcationCurve curve) {
        super(calc, curve);
        
    }

    public SecondaryBifurcationGeomFactory(SecondaryBifurcationCurveCalc calc) {
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


        buffer.append(curve.toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();

    }
}
