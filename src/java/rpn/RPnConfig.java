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
import rpnumerics.Configuration;
import rpnumerics.RPNUMERICS;

public class RPnConfig {

    private static String IMAGEPATH = System.getProperty("rpnhome") + System.getProperty("file.separator") + "share" + System.getProperty("file.separator") + "rpn-images" + System.getProperty("file.separator");
    public static ImageIcon HUGONIOT, MANIFOLD_BWD, MANIFOLD_FWD, POINCARE, ORBIT_FWD, ORBIT_BWD, STATPOINT;
    private static HashMap<String, ConfigurationProfile> configurationsProfileMap_ = new HashMap<String, ConfigurationProfile>();
    private static String activePhysics_;
    private static String activeVisualConfig_;
    private static Configuration visualConfiguration_;

    public static void configure(String physicsName) {
        activePhysics_ = physicsName;
        RPNUMERICS.init((String) physicsName);//PHYSICS is INITIALIZATED
        numericsConfig();
        visualConfig();


    }

    private static void visualConfig() {
        remoteImage();
        RPnDataModule.PHASESPACE = new RPnPhaseSpaceAbstraction("Phase Space",
                RPNUMERICS.domain(), new NumConfigImpl());//  RpNumerics.domain(),

        RPnDataModule.AUXPHASESPACE = new RPnPhaseSpaceAbstraction("Phase Space",
                RPNUMERICS.domain(), new NumConfigImpl());//  Rp


        RPnDataModule.LEFTPHASESPACE = new RPnPhaseSpaceAbstraction("LeftPhase Space",
                RPNUMERICS.domain(), new NumConfigImpl());//  RpNumerics.domain(),
        RPnDataModule.RIGHTPHASESPACE = new RPnPhaseSpaceAbstraction("RightPhase Space",
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

    public static void addProfile(String configurationName, ConfigurationProfile profile) {

        configurationsProfileMap_.put(configurationName, profile);
        Configuration configuration = new Configuration(profile);
        RPNUMERICS.setConfiguration(configuration.getName(), configuration);

    }

    public static ArrayList<ConfigurationProfile> getAllMethodsProfiles() {
        return filterProfiles(ConfigurationProfile.METHOD);
    }

    public static ArrayList<ConfigurationProfile> getAllVisualizationProfiles() {

        return filterProfiles(ConfigurationProfile.VISUALIZATION);

    }

    public static ArrayList<ConfigurationProfile> getAllPhysicsProfiles() {
        return filterProfiles(ConfigurationProfile.PHYSICS_PROFILE);

    }

    public static ConfigurationProfile getPhysicsProfile(String physicsName) {
        ConfigurationProfile physicsProfile = configurationsProfileMap_.get(physicsName);
        if (physicsProfile != null && physicsProfile.getType().equals(ConfigurationProfile.PHYSICS_PROFILE)) {
            return physicsProfile;
        }
        return null;

    }

    public static ArrayList<ConfigurationProfile> getAllConfigurationProfiles() {

        ArrayList<ConfigurationProfile> returnedArrayList = new ArrayList<ConfigurationProfile>();
        Set<Entry<String, ConfigurationProfile>> methodSet = configurationsProfileMap_.entrySet();
        Iterator<Entry<String, ConfigurationProfile>> profilesIterator = methodSet.iterator();

        while (profilesIterator.hasNext()) {
            Entry<String, ConfigurationProfile> entry = profilesIterator.next();
            returnedArrayList.add(entry.getValue());
        }
        return returnedArrayList;

    }

    public static ConfigurationProfile getConfigurationProfile(String configurationName) {

        return configurationsProfileMap_.get(configurationName);

    }

    public static void removeConfiguration(String configurationName) {

        configurationsProfileMap_.remove(configurationName);

    }

    public static void setActivePhysics(String physicsName) {
        activePhysics_ = physicsName;
    }

    public static void setActiveVisualConfiguration(String visualConfigName) {
        activeVisualConfig_ = visualConfigName;

//        System.out.println("Active visual configuration: " + activeVisualConfig_);
        visualConfiguration_ = new Configuration(configurationsProfileMap_.get(visualConfigName));
    }

    public static ConfigurationProfile getActivePhysicsProfile() {

        return configurationsProfileMap_.get(activePhysics_);

    }

    public static ConfigurationProfile getActiveVisualProfile() {
        return configurationsProfileMap_.get(activeVisualConfig_);
    }

    public static Configuration getVisualConfiguration() {
        return visualConfiguration_;
    }


    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        Set<Entry<String, ConfigurationProfile>> configurationSet = configurationsProfileMap_.entrySet();
        for (Entry<String, ConfigurationProfile> entry : configurationSet) {
            buffer.append(entry.getValue().toString());
        }
        return buffer.toString();

    }

    private static ArrayList<ConfigurationProfile> filterProfiles(String profileType) {
        ArrayList<ConfigurationProfile> filteredProfiles = new ArrayList<ConfigurationProfile>();
        Set<Entry<String, ConfigurationProfile>> methodSet = configurationsProfileMap_.entrySet();

        for (Entry<String, ConfigurationProfile> configEntry : methodSet) {

            if (configEntry.getValue().getType().equals(profileType)) {
                filteredProfiles.add(configEntry.getValue());
            }
        }
        return filteredProfiles;

    }
}
