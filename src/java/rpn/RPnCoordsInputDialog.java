/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn;
/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
import java.awt.Dimension;


public class RPnCoordsInputDialog extends RPnDialog{

    public RPnCoordsInputDialog(boolean displayBeginButton, boolean displayCancelButton) {
        super(displayBeginButton, displayCancelButton);
        setSize(new Dimension(300, 200));
    }


    @Override
    protected void apply() {
        System.out.println("Clicando em apply");
   }

    @Override
    protected void begin() {

        System.out.println("Clicando em begin");


    }

}
