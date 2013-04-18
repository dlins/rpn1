/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid;

public class Coords3D extends CoordsArray {
    //
    // Constructors
    //
    public Coords3D() {
        super(Multid.SPACE);
    }

    public Coords3D(double x, double y, double z) {
        this();
        double[] coords = {x,y,z};
        try {
            setCoords(coords);
        } catch (DimMismatchEx ex) {
            ex.printStackTrace();
        }
    }

    public Coords3D(double[] coords) {
        super(coords);
    }

    public Coords3D(Coords3D coords) {
        super(coords);
    }

    //
    // Accessors/Mutators
    //
    public double getX() { return getElement(0); }

    public double getY() { return getElement(1); }

    public double getZ() { return getElement(2); }
}
