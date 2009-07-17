/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import org.xml.sax.SAXParseException;


import rpnumerics.RPNumericsProfile;
import wave.util.RealVector;
import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;
import org.xml.sax.Parser;
import org.xml.sax.InputSource;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import rpnumerics.FluxParams;
import rpnumerics.RPNUMERICS;
import wave.util.RectBoundary;
import wave.util.IsoTriang2DBoundary;

/** This class implements methods to configure the numeric layer. The values are taked from a XML file and this values are used to setup the physics and all others numerics parameters. */
public class RPnNumericsModule {
    //
    // Constants
    //
    private static RPNumericsProfile profile_ = new RPNumericsProfile();

    static class InputHandler extends HandlerBase {
        //
        // Members
        //
        private RealVector tempVector_;
        private String currentElement_;
        private ArrayList boundsVectorArray_;
        private String boundaryType_;

        public void startElement(String name, AttributeList att) throws
                SAXException {

            currentElement_ = name;

            if (name.equals("NUMERICS")) {

            }

            if (name.equals("PHYSICS")) {
                profile_.initPhysics(att.getValue(0), att.getValue(1));
            }

            if (name.equals("BOUNDARY")) {
                boundaryType_= att.getValue(0);
                boundsVectorArray_ = new ArrayList();
            }

            if (name.equals("PHASEPOINT")) {
                tempVector_ = new RealVector((new Integer(att.getValue(0))).intValue());
            }

        }

        public void characters(char[] buff, int offset, int len) throws
                SAXException {

            String data = new String(buff, offset, len);
            data = data.trim();
            if (data.length() != 0) {
                if (currentElement_.equals("REALVECTOR")) {
                    tempVector_ = new RealVector(data);
                    boundsVectorArray_.add(tempVector_);

                }

            }

        }

        public void endElement(String name) throws SAXException {
            if (name.equals("PHYSICS")) {
                    RPNUMERICS.init(profile_);
            }

            if (name.equals("BOUNDARY")) {

                if (boundaryType_.equals("rect")) {
                    profile_.setBoundary(new RectBoundary((RealVector) boundsVectorArray_.get(0), (RealVector) boundsVectorArray_.get(1)));
                }

                if (boundaryType_.equals("triang")) {

                    profile_.setBoundary(new IsoTriang2DBoundary((RealVector) boundsVectorArray_.get(0), (RealVector) boundsVectorArray_.get(1), (RealVector) boundsVectorArray_.get(2)));
                }
            }

            if (name.equals("FLUXPARAMS")) {
                profile_.setFluxParams(new FluxParams(tempVector_));
            }

        }
    }

    //
    // Members
    //
    //
    // Initializers
    //
    public static void init(Parser parser, String file) {
        try {
            parser.setDocumentHandler(new InputHandler());
            parser.parse(file);
        } catch (Exception saxex) {
            saxex.printStackTrace();
        }
    }

    public static void init(Parser parser, InputStream configFileStream) {
        try {

            parser.setDocumentHandler(new InputHandler());
            System.out.println("Numerics Module");

            System.out.println("Will parse !");
            parser.parse(new InputSource(configFileStream));

            System.out.println("parsed !");

        } catch (Exception saxex) {

            if (saxex instanceof SAXParseException) {

                SAXParseException e = (SAXParseException) saxex;

                System.out.println("Line error: " + e.getLineNumber());
                System.out.println("Column error: " + e.getColumnNumber());
            }

            saxex.printStackTrace();
        }
    }

    //
    // Methods
    //
    /** Writes the actual values of the numerics parameters into a XML file. */
    public static void export(FileWriter writer) throws java.io.IOException {

        writer.write("<NUMERICS>\n");
        writer.write("<PHYSICS physicsid=\"" + RPNUMERICS.physicsID() + "\"" + " libname=\"" +  "\"" + ">" + "</PHYSICS>\n");
        writer.write("</NUMERICS>\n");


    }
}
