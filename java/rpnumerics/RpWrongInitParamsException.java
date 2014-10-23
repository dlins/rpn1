/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

public class RpWrongInitParamsException extends Exception {

    public  RpWrongInitParamsException (String parameter){

	super (parameter +":Wrong Parameter");

    }


}
