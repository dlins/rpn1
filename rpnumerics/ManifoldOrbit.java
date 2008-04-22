/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

// Orbit on the manifols
//
// stationaryPoint  the stationary point
// firstPoint       point for an orbit to start
// timDirection +1  for unstable manifold, -1 for stable manifold
// orbit            orbit on the manifold starting with firstPoint

package rpnumerics;

public class ManifoldOrbit implements RpSolution {
    //
    // Members
    //
    private StationaryPoint stationaryPoint_;
    // TODO Alexei, please comment this (meaning of firstPoint or pass through point ?)
    private PhasePoint firstPoint_;
    private Orbit orbit_;
    private int timeDirection_;
    private int finishType_;

    //
    // Constructor
    //
    public ManifoldOrbit(StationaryPoint stationaryPoint, PhasePoint firstPoint, Orbit orbit, int timeDirection) {
        stationaryPoint_ = new StationaryPoint(stationaryPoint);
        orbit_ = orbit;
        firstPoint_ = firstPoint;
        timeDirection_ = timeDirection;
        finishType_ = orbit_.getIntegrationFlag();
    }

    //
    // Accessors/Mutators
    //
    public void setStationaryPoint(StationaryPoint p) { stationaryPoint_ = new StationaryPoint(p); }

    public PhasePoint getFirstPoint() { return firstPoint_; }

    public void setTimeDirection(int tDir) { timeDirection_ = tDir; }

    public StationaryPoint getStationaryPoint() { return stationaryPoint_; }

    public Orbit getOrbit() { return orbit_; }

    public int getTimeDirection() { return timeDirection_; }

    public int getFinishType() { return finishType_; }

    //
    // Methods
    //
    
    public String toXML(){

      StringBuffer  buffer = new StringBuffer();

      buffer.append("<MANIFOLD timedirection=\""+getTimeDirection()+"\">\n");
      buffer.append(getStationaryPoint().toXML());
      buffer.append(getFirstPoint().toXML());
      buffer.append(getOrbit().toXML());
      buffer.append("</MANIFOLD>");

      return buffer.toString();

    }

    public String toXML(boolean calcReady){

      StringBuffer  buffer = new StringBuffer();
      if (calcReady){

      buffer.append("<MANIFOLD timedirection=\""+getTimeDirection()+"\">\n");
      buffer.append(getStationaryPoint().toXML());
      buffer.append(getFirstPoint().toXML());
      buffer.append(getOrbit().toXML());
      buffer.append("</MANIFOLD>");

      }
      return buffer.toString();


    }
}
