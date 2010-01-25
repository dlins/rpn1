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
    private boolean hasFluxParams_=false;
    private String physicsId_;
    private FluxParams fluxParams_;


    // Constructors/Initializers
    public void initPhysics(String physicsId) {
        physicsId_ = physicsId;

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

        if (hasBoundary())
            return boundary_;
        throw new Exception("RPNumericsProfile has no boundary");
    }

    public String getPhysicsID() { 
        return physicsId_;
    }

    public String getLibName() {
        return libName_;
    }

    public void setFluxParams (FluxParams fluxParams){
        fluxParams_=fluxParams;
        hasFluxParams_=true;
    }

    public FluxParams getFluxParams() throws Exception
    {
        if (hasFluxParams())
            return fluxParams_;
        throw new Exception("Profile has no flux params");
    }

    public boolean hasFluxParams() {
        return hasFluxParams_;
    }

}
