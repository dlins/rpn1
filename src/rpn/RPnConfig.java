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
import rpn.parser.MethodProfile;
import rpn.parser.RPnDataModule;
import rpn.plugininterface.PluginInfoController;
import rpn.plugininterface.PluginInfoParser;
import rpnumerics.ContourConfiguration;
import rpnumerics.RPNUMERICS;

public class RPnConfig {

    private static String IMAGEPATH = System.getProperty("rpnhome") + System.getProperty("file.separator") + "share" + System.getProperty("file.separator") + "rpn-images" + System.getProperty("file.separator");
    public static ImageIcon HUGONIOT,  MANIFOLD_BWD,  MANIFOLD_FWD,  POINCARE,  ORBIT_FWD,  ORBIT_BWD,  STATPOINT;
    private static HashMap<String, MethodProfile> methodsMap_ = new HashMap<String, MethodProfile>();

    public static void configure(String physicsName) {

        RPNUMERICS.init((String) physicsName);//PHYSICS is INITIALIZATED

        numericsConfig();
        visualConfig();

//        System.out.println("Dimensao: " + RPNUMERICS.domain());

//        System.out.println("Selecionado: " + physicsName);


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

    public static void addMethod(String methodName, MethodProfile profile) {

        methodsMap_.put(methodName, profile);


    }


        public static void addParam(String methodName, String paramName, String paramValue) {

        if (!methodsMap_.containsKey(methodName)) {
            MethodProfile newProfile = new MethodProfile(methodName);
            newProfile.addParam(paramName, paramValue);

            methodsMap_.put(methodName, newProfile);
        } else {
            MethodProfile methodProfile = methodsMap_.get(methodName);
            methodProfile.addParam(paramName, paramValue);
            methodsMap_.put(methodName, methodProfile);

        }

    }

  public static ArrayList<MethodProfile> getAllMethodsProfiles() {

        ArrayList<MethodProfile> returnedArrayList = new ArrayList<MethodProfile>();

        Set<Entry<String, MethodProfile>> methodSet = methodsMap_.entrySet();

        Iterator<Entry<String, MethodProfile>> methodIterator = methodSet.iterator();

        while (methodIterator.hasNext()) {
            Entry<String, MethodProfile> entry = methodIterator.next();

            returnedArrayList.add(entry.getValue());

        }

        return returnedArrayList;

    }

  public static MethodProfile getMethodProfile(String methodName) {

        return methodsMap_.get(methodName);

    }

    public static void removeMethod(String methodName) {

        methodsMap_.remove(methodName);

    }



}
