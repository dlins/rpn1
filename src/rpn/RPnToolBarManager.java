/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class RPnToolBarManager implements ActionListener {

    private JFrame frame_;

    public RPnToolBarManager(JFrame frame) {

        frame_ = frame;
    }

    public void actionPerformed(ActionEvent e) {
        frame_.pack();
    }
}


