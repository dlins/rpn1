/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpn.usecase;

public class RarefactionCalcAgent extends RpModelConfigChangeAgent {

    //
    // Constants
    //
    
    static public final String DESC_TEXT = "Change RarefactionConfig";

    //
    // Members
    //

    static private RarefactionCalcAgent instance_ = null;
    
    //
    // Constructors
    //
    protected RarefactionCalcAgent() {
        super(DESC_TEXT);
    }


    public void execute() {
     
    }


    public void unexecute() {
     
    }

    static public RarefactionCalcAgent instance() {
        if (instance_ == null)
            instance_ = new RarefactionCalcAgent();
        return instance_;
    }



}
