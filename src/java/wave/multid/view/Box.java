/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.view;

import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.BasicStroke;
import java.awt.Stroke;

public class Box {
    //
    // Constants
    //
    //
    // Members
    //
    private Shape boxedShape_;
    private ViewingTransform transf_;

    //
    // Constructors
    //
    public Box(Shape boxed, ViewingTransform transf) {
        boxedShape_ = boxed;
        transf_ = transf;
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    public void draw(Graphics2D g) {
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
            new float[] { 2, 2 }, 0));
        Rectangle2D rect = boxedShape_.getBounds2D();
        rect.setRect(rect.getCenterX() - 25, rect.getCenterY() - 25, rect.getWidth() + 50, rect.getHeight() + 50);
        switch (transf_.viewingMap().getCodomain().getDim()) {
            case 2:
                g.draw(rect);
                break;
            case 3:
                g.draw3DRect(new Double(rect.getX()).intValue(), new Double(rect.getY()).intValue(), new Double(rect.getWidth()).intValue(),
                    new Double(rect.getHeight()).intValue(), true);
                break;
        }
        g.setStroke(oldStroke);
    }
}
