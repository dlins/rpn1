/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.Font;
import javax.swing.ImageIcon;
import rpn.controller.phasespace.NumConfigImpl;
import rpn.parser.RPnDataModule;
import rpn.parser.RPnVisualizationModule;
import rpn.plugininterface.PluginInfoController;
import rpn.plugininterface.PluginInfoParser;
import rpnumerics.RPNUMERICS;
import wave.multid.Space;

public class RPnConfig {

    private static String IMAGEPATH = System.getProperty("rpnhome") + System.getProperty("file.separator") + "share" + System.getProperty("file.separator") + "rpn-images" + System.getProperty("file.separator");
    public static ImageIcon HUGONIOT,  MANIFOLD_BWD,  MANIFOLD_FWD,  POINCARE,  ORBIT_FWD,  ORBIT_BWD,  STATPOINT;


    public static void configure(String physicsName) {

        RPNUMERICS.init((String) physicsName);//PHYSICS is INITIALIZATED

        numericsConfig();
        visualConfig();

//        System.out.println("Dimensao: " + RPNUMERICS.domain());

//        System.out.println("Selecionado: " + physicsName);


    }

    private static void visualConfig() {

        remoteImage();
        int[] projIndices = new int[2];
        projIndices[0] = 0;
        projIndices[1] = 1;
        boolean iso2equi_ = false;

        RPnDataModule.PHASESPACE = new RPnPhaseSpaceAbstraction("Phase Space",
                RPNUMERICS.domain(), new NumConfigImpl());//  RpNumerics.domain(),


        RPnVisualizationModule.DESCRIPTORS.add(
                new RPnProjDescriptor(new Space("", new Integer(2).intValue()), "Label", new Integer(800).intValue(),
                new Integer(800).intValue(), projIndices, iso2equi_));

    }

    private static void numericsConfig() {

        PluginInfoParser pluginParser = new PluginInfoParser();
        PluginInfoController.updatePluginInfo(pluginParser);


    }

    static public void remoteImage() {

        HUGONIOT = new ImageIcon(IMAGEPATH + "hugoniot.jpg");
        MANIFOLD_BWD = new ImageIcon(IMAGEPATH + "manifold_bwd.jpg");
        MANIFOLD_FWD = new ImageIcon(IMAGEPATH + "manifold_fwd.jpg");
        POINCARE = new ImageIcon(IMAGEPATH + "poincare.jpg");
        ORBIT_FWD = new ImageIcon(IMAGEPATH + "orbit_fwd.jpg");
        ORBIT_BWD = new ImageIcon(IMAGEPATH + "orbit_bwd.jpg");
        STATPOINT = new ImageIcon(IMAGEPATH + "statpoint.jpg");

    }
}
