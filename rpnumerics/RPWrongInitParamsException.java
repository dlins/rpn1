/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

public class RPWrongInitParamsException extends Exception {

    public  RPWrongInitParamsException (String parameter){

	super (parameter +":Wrong Parameter");

    }


}
