/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class RPnConfigurationFrame extends JFrame {

    public RPnConfigurationFrame(String title) throws HeadlessException {
        super(title);

        getContentPane().add(new RPnCurvesConfigPanel());
        setUIFramePosition();


    }

    private void setUIFramePosition() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        int height = dim.height;
        int width = dim.width;
        this.setLocation((int) (width - (width * .35)), (int) (height - (height * .9)));
        this.setLocation((int) (width - (width * .35)), 100);
    }
}
