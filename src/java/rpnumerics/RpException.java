/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

public class RpException extends Exception {
    //
    // Constants
    //
    public static final String RP_BANNER = "RP Exception ... ";

    //
    // Constructors
    //
    public RpException(String message) {
        super(RP_BANNER + message);
    }
}
