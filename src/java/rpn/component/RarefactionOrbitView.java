package rpn.component;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;
import wave.util.Arrow;

public class RarefactionOrbitView extends WaveCurveOrbitGeomView{

 

    public RarefactionOrbitView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);
    }

    @Override
    public Shape createShape() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        arrowsCalculations();

        composite.append(super.createShape(), false);

        return composite;
    }


    @Override
    public void draw(Graphics2D g) {

        g.setColor(getViewingAttr().getColor());

        super.draw(g);

        for (int i = 0; i < arrowList_.size(); i++) {

            ((Arrow) (arrowList_.get(i))).paintComponent(g);
        }


    }

}
