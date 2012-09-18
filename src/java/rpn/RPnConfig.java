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
    private static RPnFluxParamsSubject[] teste;
    private static RPnFluxParamsObserver paramObserver;

    public static void configure(String physicsName) {

        activePhysics_ = physicsName;
        RPNUMERICS.init((String) physicsName);//PHYSICS is INITIALIZATED
        numericsConfig();
        visualConfig();

    }

    public static void createParamsFluxSubject(String physicsName) {

        if (physicsName.equals("QuadraticR2")) {
            teste = new RPnFluxParamsSubject[3];

            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsName);

            Configuration fluxConfiguration = physicsConfiguration.getConfiguration("fluxfunction");

            paramObserver = new RPnFluxParamsObserver(fluxConfiguration);

            teste[0] = new RPnSchearerSchaeffer(new String[3], new String[]{"A", "B", "C"});
            teste[1] = new RPnPalmeira(new String[3], new String[]{"B1", "B2", "C"});
            teste[2] = new RPnCorey(new String[2], new String[]{"A", "B"});

            teste[0].attach(paramObserver);
            teste[1].attach(paramObserver);
            teste[2].attach(paramObserver);


        }
        if (physicsName.equals("Stone")) {
            teste = new RPnFluxParamsSubject[4];

            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsName);

            Configuration fluxConfiguration = physicsConfiguration.getConfiguration("fluxfunction");

            paramObserver = new RPnFluxParamsObserver(fluxConfiguration);

            teste[0] = new RPnCoreyToStone(new String[9], new String[]{"muw", "mug", "muo", "expw", "expg", "expo", "cnw", "cng", "cno"});
            teste[1] = new RPnStoneToStone(new String[10], new String[]{"muw", "mug", "muo", "expw", "expg", "expow", "expog", "cnw", "cng", "cno"});
            teste[2] = new RPnCoreyBrooks(new String[8], new String[]{"muw", "mug", "muo", "epsl", "cnw", "cng", "cno", "lambda"});
            teste[3] = new RPnRadioButtonToStone(new String[3], new String[]{"", "", ""});       // aqui virao os nomes dos botoes

            teste[0].attach(paramObserver);
            teste[1].attach(paramObserver);
            teste[2].attach(paramObserver);
            teste[3].attach(paramObserver);

        }



        if (physicsName.equals("CoreyQuad")) {
            teste = new RPnFluxParamsSubject[0];

            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsName);

            Configuration fluxConfiguration = physicsConfiguration.getConfiguration("fluxfunction");

            paramObserver = new RPnFluxParamsObserver(fluxConfiguration);
//
//            teste[0] = new RPnCoreyToStone(new String[9], new String[]{"muw", "mug", "muo", "expw", "expg", "expo", "cnw", "cng", "cno"});
//            teste[1] = new RPnStoneToStone(new String[10], new String[]{"muw", "mug", "muo", "expw", "expg", "expow", "expog", "cnw", "cng", "cno"});
//            teste[2] = new RPnCoreyBrooks(new String[8], new String[]{"muw", "mug", "muo", "epsl", "cnw", "cng", "cno", "lambda"});
//            teste[3] = new RPnRadioButtonToStone(new String[3], new String[]{"", "", ""});       // aqui virao os nomes dos botoes
//
//            teste[0].attach(paramObserver);
//            teste[1].attach(paramObserver);
//            teste[2].attach(paramObserver);
//            teste[3].attach(paramObserver);

        }


        if (physicsName.equals("Polydisperse")) {

            teste = new RPnFluxParamsSubject[2];
            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsName);

            Configuration fluxConfiguration = physicsConfiguration.getConfiguration("fluxfunction");

            paramObserver = new RPnFluxParamsObserver(fluxConfiguration);

            teste[0] = new RPnBasson(new String[5], new String[]{"phimax", "Vinf1", "Vinf2", "n1", "n2"});
            teste[1] = new RPnMLB(new String[7], new String[]{"phimax", "rho1", "rho2", "d1", "d2", "n1", "n2"});

            teste[0].attach(paramObserver);
            teste[1].attach(paramObserver);

        }



    }

    public static RPnFluxParamsSubject[] getFluxParamsSubject() {
        return teste;
    }

    public static RPnFluxParamsObserver getFluxParamsObserver() {
        return paramObserver;
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
