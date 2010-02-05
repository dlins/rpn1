/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.ImageIcon;
import rpn.controller.phasespace.NumConfigImpl;
import rpn.parser.ConfigurationProfile;
import rpn.parser.RPnDataModule;
import rpn.plugininterface.PluginInfoController;
import rpn.plugininterface.PluginInfoParser;
import rpnumerics.RPNUMERICS;

public class RPnConfig {

    private static String IMAGEPATH = System.getProperty("rpnhome") + System.getProperty("file.separator") + "share" + System.getProperty("file.separator") + "rpn-images" + System.getProperty("file.separator");
    public static ImageIcon HUGONIOT, MANIFOLD_BWD, MANIFOLD_FWD, POINCARE, ORBIT_FWD, ORBIT_BWD, STATPOINT;
    private static HashMap<String, ConfigurationProfile> methodsMap_ = new HashMap<String, ConfigurationProfile>();

    public static void configure(String physicsName) {

        RPNUMERICS.init((String) physicsName);//PHYSICS is INITIALIZATED

        numericsConfig();
        visualConfig();


    }

    private static void visualConfig() {

        remoteImage();


        RPnDataModule.PHASESPACE = new RPnPhaseSpaceAbstraction("Phase Space",
                RPNUMERICS.domain(), new NumConfigImpl());//  RpNumerics.domain(),




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

    public static void addConfiguration(String configurationName, ConfigurationProfile profile) {

        methodsMap_.put(configurationName, profile);

    }

    public static ArrayList<ConfigurationProfile> getAllConfigurationProfiles() {

        ArrayList<ConfigurationProfile> returnedArrayList = new ArrayList<ConfigurationProfile>();

        Set<Entry<String, ConfigurationProfile>> methodSet = methodsMap_.entrySet();

        Iterator<Entry<String, ConfigurationProfile>> methodIterator = methodSet.iterator();

        while (methodIterator.hasNext()) {
            Entry<String, ConfigurationProfile> entry = methodIterator.next();

            returnedArrayList.add(entry.getValue());

        }

        return returnedArrayList;

    }

    public static ConfigurationProfile getConfigurationProfile(String configurationName) {

        return methodsMap_.get(configurationName);

    }

    public static void removeConfiguration(String configurationName) {

        methodsMap_.remove(configurationName);

    }
}
