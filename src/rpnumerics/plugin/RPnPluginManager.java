/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpnumerics.plugin;


public class RPnPluginManager {

    
    public native static void setPluginDir(String pluginDir);
    public native static void addClass(String libName, String className, String constructorMethod);

}




//
