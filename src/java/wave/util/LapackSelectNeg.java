/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util;

/*
 * This is an adapter implementation for Lapack Select
 * Object derived class described in org.netlib.lapack.DGEES
 *
 * select(x1,x2) -> returns true iff x1 < 0
 */

public class LapackSelectNeg {
    //
    // Constants
    //
    //
    // Members
    //
    static public boolean select(double v1, double v2) {
        if (v1 < 0)
            return true;
        return false;
    }
}
