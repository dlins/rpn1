/* Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.ShockCurve;
import rpnumerics.WaveCurve;

import wave.multid.*;
import wave.multid.model.*;
import wave.multid.view.*;
import wave.util.RealVector;

public class ShockCurveGeomView extends WaveCurveOrbitGeomView {

    public ShockCurveGeomView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {

        super(geom, transf, attr);

    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(getViewingAttr().getColor());

        Stroke actualStroke = g.getStroke();
        float[] dash = {10f};
        BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, BasicStroke.JOIN_MITER, dash, 0f);

        g.setStroke(stroke);

        super.draw(g);
        g.setStroke(actualStroke);
        
    }


}
