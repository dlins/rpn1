/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import wave.util.RealVector;

public class ConnectionOrbit implements RpSolution {
    //
    // Members
    //
    private StationaryPoint uMinus_;
    private StationaryPoint uPlus_;
    private Orbit orbit_;

    //
    // Constructors
    //
    public ConnectionOrbit(StationaryPoint uMinus, StationaryPoint uPlus, Orbit orbit) {
        uMinus_ = uMinus;
        uPlus_ = uPlus;
        orbit_ = orbit;
    }


    //
    // Methods
    //
    public native RealVector orbitCenter();


    public String toXML(){

      StringBuffer buffer = new StringBuffer();

      buffer.append("<CONNECTIONORBIT>\n");
      buffer.append("<UMINUS>\n");
      buffer.append(uMinus().toXML());
      buffer.append("</UMINUS>\n");

      buffer.append("<UPLUS>\n");
      buffer.append(uPlus().toXML());
      buffer.append("</UPLUS>\n");

      buffer.append(orbit().toXML());

      buffer.append("</CONNECTIONORBIT>");

      return  buffer.toString();


    }

    public String toXML(boolean calcReady){

      StringBuffer buffer = new StringBuffer();
      if (calcReady){
      buffer.append("<CONNECTIONORBIT>\n");
      buffer.append("<UMINUS>\n");
      buffer.append(uMinus().toXML());
      buffer.append("</UMINUS>\n");

      buffer.append("<UPLUS>\n");
      buffer.append(uPlus().toXML());
      buffer.append("</UPLUS>\n");

      buffer.append(orbit().toXML());

      buffer.append("</CONNECTIONORBIT>");
      }

      return  buffer.toString();

    }

    //
    // Accessors/Mutators
    //
    public StationaryPoint uMinus() { return uMinus_; }

    public StationaryPoint uPlus() { return uPlus_; }

    public Orbit orbit() { return orbit_; }

}
