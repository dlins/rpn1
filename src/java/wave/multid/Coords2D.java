/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid;

import wave.multid.DimMismatchEx;

public class Coords2D extends CoordsArray {
    //
    // Constructors
    //
    public Coords2D() {
        super(Multid.PLANE);
    }

    public Coords2D(double x, double y) {
        this();
        double[] coords = {x,y};
        try {
            setCoords(coords);
        } catch (DimMismatchEx ex) {
            ex.printStackTrace();
        }
    }

    public Coords2D(double coords[]) {
        super(coords);
    }

    public Coords2D(Coords2D coords) {
        super(coords);
    }

    //
    // Accessors/Mutators
    //
    public double getX() { return getElement(0); }

    public double getY() { return getElement(1); }
}
