/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpn.command.BifurcationRefineCommand;

public class BIFURCATIONREFINE_CONFIG extends UI_ACTION_SELECTED {

    public BIFURCATIONREFINE_CONFIG() {

        super(BifurcationRefineCommand.instance());
        System.out.println("Construtor de BIFURCATIONREFINE_CONFIG");

    }

    @Override
    public int actionDimension() {

        System.out.println("BIFURCATIONREFINE_CONFIG : actionDimension()");
        return rpnumerics.RPNUMERICS.domainDim() * 2;       //*** Por que o * 2 ?
    }
}
