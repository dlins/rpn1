/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.graphs;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;

public class dcViewport extends Rectangle {
    //
    // Members
    //
    //
    // Constructors
    //

    private int margin_;

    public dcViewport(int w, int h) {
        super(new Point(), new Dimension(w, h));

        margin_ = 0;
    }

    public dcViewport(Point origin, int w, int h, int margin) {
        super(new Point(origin.x + margin, origin.y + margin), new java.awt.Dimension(w - 2 * margin, h - 2 * margin));

        margin_ = margin;

    }

    public int getMargin() {
        return margin_;
    }

    //
    // Accessors/Mutators
    //
    public Point getOriginPosition() {
        return this.getLocation();
    }
    //
    // Methods
    //
}
