/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import rpn.RPnBasson;
import rpn.RPnCorey;
import rpn.RPnCoreyBrooks;
import rpn.RPnCoreyToStone;
import rpn.RPnFluxConvexCombination;
import rpn.RPnFluxParamsObserver;
import rpn.RPnFluxParamsSubject;
import rpn.RPnMLB;
import rpn.RPnPalmeira;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnRadioButtonToStone;
import rpn.RPnSchearerSchaeffer;
import rpn.RPnStoneToStone;
import rpn.RPnViscosityMatrix;
import rpn.controller.phasespace.NumConfigImpl;

import rpn.parser.RPnDataModule;
import rpn.plugininterface.PluginInfoController;
import rpn.plugininterface.PluginInfoParser;
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
            teste = new RPnFluxParamsSubject[5];

            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsName);

            Configuration fluxConfiguration = physicsConfiguration.getConfiguration("fluxfunction");

            paramObserver = new RPnFluxParamsObserver(fluxConfiguration);


//              System.out.println("VETOTR A : ");
            String[] namesA = new String[fluxConfiguration.getParamsSize()];
            for (int i=0; i<namesA.length;i++) {
                namesA[i] = "A " +fluxConfiguration.getParamName(i);
                System.out.println(namesA[i]);
            }


//            System.out.println("VETOTR B : ");
            String[] namesB = new String[fluxConfiguration.getParamsSize()];
            for (int i=0; i<namesB.length;i++) {
                namesB[i] = "B " +fluxConfiguration.getParamName(i);
                System.out.println(namesB[i]);
            }

            String[] finalNames = new String[fluxConfiguration.getParamsSize()*2 + 1];
            for (int i=0; i<namesA.length; i++) {
                finalNames[i] = namesA[i];
            }

            for (int i=0; i<namesA.length; i++) {
                finalNames[i+namesA.length] = namesB[i];
            }

            finalNames[finalNames.length - 1] = "alpha";
//
//            System.out.println("VETOTR FINAL_NAMES : ");
//            for (int i=0; i<finalNames.length; i++) {
//                System.out.println(finalNames[i]);
//            }


            String[] values = new String[fluxConfiguration.getParamsSize()*2 + 1];
            for (int i=0; i<values.length-1;i++) {
                values[i]=String.valueOf(i);
            }
            teste[0] = new RPnSchearerSchaeffer(new String[3], new String[]{"A", "B", "C"});
            teste[1] = new RPnPalmeira(new String[3], new String[]{"B1", "B2", "C"});
            teste[2] = new RPnCorey(new String[2], new String[]{"A", "B"});
            teste[3] = new RPnFluxConvexCombination(values, finalNames);
            teste[4]= new RPnViscosityMatrix(new String[4], new String[]{"f1","g1","f2","g2"});
            

            teste[0].attach(paramObserver);
            teste[1].attach(paramObserver);
            teste[2].attach(paramObserver);
            teste[3].attach(paramObserver);
            teste[4].attach(paramObserver);


        }
        if (physicsName.equals("Stone")) {      //testando combinacao convexa de parametros
            teste = new RPnFluxParamsSubject[5];

            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsName);

            Configuration fluxConfiguration = physicsConfiguration.getConfiguration("fluxfunction");

            paramObserver = new RPnFluxParamsObserver(fluxConfiguration);

//            System.out.println("VETOTR A : ");
            String[] namesA = new String[fluxConfiguration.getParamsSize()];
            for (int i=0; i<namesA.length;i++) {
                namesA[i] = "A " +fluxConfiguration.getParamName(i);
//                System.out.println(namesA[i]);
            }


//            System.out.println("VETOTR B : ");
            String[] namesB = new String[fluxConfiguration.getParamsSize()];
            for (int i=0; i<namesB.length;i++) {
                namesB[i] = "B " +fluxConfiguration.getParamName(i);
//                System.out.println(namesB[i]);
            }

            String[] finalNames = new String[fluxConfiguration.getParamsSize()*2 + 1];
            for (int i=0; i<namesA.length; i++) {
                finalNames[i] = namesA[i];
            }

            for (int i=0; i<namesA.length; i++) {
                finalNames[i+namesA.length] = namesB[i];
            }

            finalNames[finalNames.length - 1] = "alpha";

//            System.out.println("VETOTR FINAL_NAMES : ");
            for (int i=0; i<finalNames.length; i++) {
//                System.out.println(finalNames[i]);
            }

            String[] values = new String[fluxConfiguration.getParamsSize()*2 + 1];
            for (int i=0; i<values.length-1;i++) {
                values[i]=String.valueOf(i);
            }

            teste[0] = new RPnCoreyToStone(new String[9], new String[]{"muw", "mug", "muo", "expw", "expg", "expo", "cnw", "cng", "cno"});
            teste[1] = new RPnStoneToStone(new String[10], new String[]{"muw", "mug", "muo", "expw", "expg", "expow", "expog", "cnw", "cng", "cno"});
            teste[2] = new RPnCoreyBrooks(new String[8], new String[]{"muw", "mug", "muo", "epsl", "cnw", "cng", "cno", "lambda"});
            teste[3] = new RPnRadioButtonToStone(new String[3], new String[]{"", "", ""});       // aqui virao os nomes dos botoes
            //teste[4] = new RPnFluxConvexCombination(new String[fluxConfiguration.getParamsSize()*2 + 1], finalNames);
            teste[4] = new RPnFluxConvexCombination(values, finalNames);

            teste[0].attach(paramObserver);
            teste[1].attach(paramObserver);
            teste[2].attach(paramObserver);
            teste[3].attach(paramObserver);
            teste[4].attach(paramObserver);


        }



        if (physicsName.equals("CoreyQuad")) {
            teste = new RPnFluxParamsSubject[0];

            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsName);

            Configuration fluxConfiguration = physicsConfiguration.getConfiguration("fluxfunction");

            paramObserver = new RPnFluxParamsObserver(fluxConfiguration);


        }
        
        
           if (physicsName.equals("TriPhase")) {
            teste = new RPnFluxParamsSubject[0];

            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsName);

            Configuration fluxConfiguration = physicsConfiguration.getConfiguration("fluxfunction");

            paramObserver = new RPnFluxParamsObserver(fluxConfiguration);

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
                RPNUMERICS.domain(), new NumConfigImpl());
        RPnDataModule.LEFTPHASESPACE = new RPnPhaseSpaceAbstraction("Left Phase Space",
                RPNUMERICS.domain(), new NumConfigImpl());
        RPnDataModule.RIGHTPHASESPACE = new RPnPhaseSpaceAbstraction("Right Phase Space",
                RPNUMERICS.domain(), new NumConfigImpl());


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
        try {

            configurationsProfileMap_.put(configurationName, profile);

            HashMap<String, Configuration> innerConfigurations = createConfigurationList(profile);

            Configuration configuration = null;


            if (profile.getType().equals(ConfigurationProfile.PHYSICS_PROFILE)) {
                configuration = new PhysicsConfiguration(profile, innerConfigurations);
            }


            if (profile.getType().equals(ConfigurationProfile.VISUALIZATION)) {

//                configuration = new VisualConfiguration(profile);
                configuration = new VisualConfiguration(profile, innerConfigurations);
            }


            if (profile.getType().equals(ConfigurationProfile.BOUNDARY)) {
                configuration = new BoundaryConfiguration(profile, innerConfigurations);
            }


            if (profile.getType().equals(ConfigurationProfile.METHOD)) {
                configuration = new MethodConfiguration(profile);
            }

            if (profile.getType().equals(ConfigurationProfile.CURVECONFIGURATION)) {
                configuration = new CurveConfiguration(profile);
            }



            RPNUMERICS.setConfiguration(configuration.getName(), configuration);
        } catch (Exception ex) {
            Logger.getLogger(RPnConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static HashMap<String, Configuration> createConfigurationList(ConfigurationProfile profile) throws Exception {


        HashMap<String, Configuration> configurationMap = new HashMap<String, Configuration>();

        
        if(profile.getType().equals(ConfigurationProfile.VISUALIZATION)){
            
            System.out.println("Quantidade de profiles dentro do visual:" +profile.getProfiles().values().size());
        }
        

        Set<Entry<String, ConfigurationProfile>> configurationProfileSet = profile.getProfiles().entrySet();
        


        for (Entry<String, ConfigurationProfile> entry : configurationProfileSet) {


            if (entry.getValue().getType().equals(ConfigurationProfile.PHYSICS_PROFILE)
                    || entry.getValue().getType().equals(ConfigurationProfile.PHYSICS_CONFIG)
                    || entry.getValue().getType().equals(ConfigurationProfile.BOUNDARY)
                    || entry.getValue().getType().equals(ConfigurationProfile.VISUALIZATION)) {


                if (entry.getValue().getType().equals(ConfigurationProfile.PHYSICS_PROFILE)) {

                    configurationMap.put(entry.getKey(), new PhysicsConfiguration(entry.getValue()));
                }


                if (entry.getValue().getType().equals(ConfigurationProfile.PHYSICS_CONFIG)) {
                    configurationMap.put(entry.getKey(), new PhysicsConfigurationParams(entry.getValue()));
                }

                if (entry.getValue().getType().equals(ConfigurationProfile.VISUALIZATION)) {





                    configurationMap.put(entry.getKey(), new VisualConfiguration(entry.getValue()));
                }


                if (entry.getValue().getType().equals(ConfigurationProfile.BOUNDARY)) {
                    configurationMap.put(entry.getKey(), new BoundaryConfiguration(entry.getValue()));
                }






            } else {
                throw new Exception("Profile type unknow:" + entry.getValue().getName() + " " + entry.getValue().getType());
            }

        }




        return configurationMap;

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
        
         HashMap<String, Configuration> innerConfigurations;
        try {
            innerConfigurations = createConfigurationList(configurationsProfileMap_.get(visualConfigName));
            visualConfiguration_ = new VisualConfiguration(configurationsProfileMap_.get(visualConfigName),innerConfigurations);
        } catch (Exception ex) {
            Logger.getLogger(RPnConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
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
