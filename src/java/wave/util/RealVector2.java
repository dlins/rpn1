/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

public class RealVector2 extends RealVector {
    public RealVector2(RealVector2 copy) {
        super(copy);
    }

    public RealVector2(double x, double y) {
        super(
            new double[] { x, y });
    }

    public double getX() { return getElement(0); }

    public double getY() { return getElement(1); }
}
