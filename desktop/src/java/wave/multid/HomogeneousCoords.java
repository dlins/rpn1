/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.multid;

public class HomogeneousCoords extends CoordsArray {
    public HomogeneousCoords(CoordsArray source) {
        super(new Space(("Homogeneous"), source.getSpace().getDim() + 1));
        for (int i = 0; i < source.getCoords().length; i++)
            setElement(i, source.getElement(i));
        setElement(getSpace().getDim() - 1, 1d);
    }

    public double[] getRegularCoords() {
        // except homogenous factors
        double[] regValues = new double[getSpace().getDim() - 1];
        for (int i = 0; i < regValues.length; i++)
            regValues[i] = getElement(i);
        return regValues;
    }
}
