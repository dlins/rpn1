/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util.graphs;

import java.awt.Rectangle;
import java.awt.Point;

public class dcViewport extends Rectangle {
    //
    // Members
    //
    //
    // Constructors
    //
    public dcViewport(int w, int h) {
        this(new Point(), w, h);
    }

    public dcViewport(Point origin, int w, int h) {
        super(origin, new java.awt.Dimension(w, h));
    }

    //
    // Accessors/Mutators
    //
    public Point getOriginPosition() { return this.getLocation(); }
    //
    // Methods
    //
}
