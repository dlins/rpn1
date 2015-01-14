/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Polygon;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class RPnConfigurationFrame extends JFrame {

//    private final JPanel checkBoxPanel_;
    
    private static RPnCurvesConfigPanel configPanel_;

    public RPnConfigurationFrame(String title) throws HeadlessException {
        super(title);

        setSize(new Dimension(20,20));

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        configPanel_ = new RPnCurvesConfigPanel();
        
        getContentPane().add(configPanel_.getContainer());

//        checkBoxPanel_ = new JPanel();
//        createMainBoundarySelectionPanel();
//        getContentPane().add(checkBoxPanel_);

        setUIFramePosition();


    }
    
    public static void setFocus(String panelName){
        configPanel_.setFocus(panelName);
    }

    private void createMainBoundarySelectionPanel() {

        RPnPhaseSpaceFrame[] allFrames = RPnUIFrame.getPhaseSpaceFrames();
        for (RPnPhaseSpaceFrame jFrame : allFrames) {
            JLabel panelLabel = new JLabel(jFrame.getTitle());
//            checkBoxPanel_.add(panelLabel);
            addBoundaryCheckBox(jFrame.phaseSpacePanel());
            
        }

    }

    private void addBoundaryCheckBox(RPnPhaseSpacePanel phaseSpacePanel) {

        RPnBoundarySelector selector = new RPnBoundarySelector(phaseSpacePanel);

        Polygon boundaryPolygon = phaseSpacePanel.getPhysicalBoundaryPolygon();

        int[] xVerticesCoords = boundaryPolygon.xpoints;
        int[] yVerticesCoords = boundaryPolygon.ypoints;

        for (int i = 0; i < selector.getVerticesArray().size(); i++) {

            JCheckBox checkBox = selector.getVerticesArray().get(i);
            checkBox.setBounds(xVerticesCoords[i], yVerticesCoords[i], 20, 20);
//            checkBoxPanel_.add(checkBox);

        }

    }

    private void setUIFramePosition() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        int height = dim.height;
        int width = dim.width;
        this.setLocation((int) (width - (width * .30)), (int) (height - (height * .90)));
//        this.setLocation((int) (width - (width * .35)), 100);
    }
}
