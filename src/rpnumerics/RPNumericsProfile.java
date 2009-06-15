/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

//import rpnumerics.physics.*;
import wave.util.Boundary;

public class RPNumericsProfile {
    private String libName_;

//    public Physics physics_;
    private Boundary boundary_;
    private boolean hasBoundary_ = false;
    private String physicsId_;

    // Constructors/Initializers
    public void initPhysics(String physicsId,String libname) {
        physicsId_ = physicsId;
        libName_=libname;
    }

    //Acessors
    public void setBoundary(Boundary boundary) {
        boundary_ = boundary;
        hasBoundary_ = true;
    }

    public boolean hasBoundary() {
        return hasBoundary_;
    }

    public Boundary getBoundary() throws Exception {

        if (hasBoundary()) {

            return boundary_;
        } else {
            throw new Exception("RPNumericsProfile has no boundary");
           
        }
    }

    public String getPhysicsID() { 
        return physicsId_;
    }

    public String getLibName() {
        return libName_;
    }
}
