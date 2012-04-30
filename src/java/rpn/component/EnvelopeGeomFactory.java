/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;

public class EnvelopeGeomFactory extends BifurcationCurveGeomFactory {

    private static ViewingAttr viewAtt_ = new ViewingAttr(Color.white);

    public EnvelopeGeomFactory(EnvelopeCurveCalc calc, EnvelopeCurve curve) {
        super(calc, curve);
        
    }

    public EnvelopeGeomFactory(EnvelopeCurveCalc calc) {
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


    //TODO

    @Override
    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append(super.toXML());

        BifurcationCurve curve = (BifurcationCurve) geomSource();

        EnvelopeCurveCalc doubleContactCalc = (EnvelopeCurveCalc) rpCalc();

        buffer.append(" envelope curve");

        buffer.append(curve.toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();

    }
}
