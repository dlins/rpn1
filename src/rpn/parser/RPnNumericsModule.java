/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import org.xml.sax.SAXParseException;
import rpn.*;

import rpnumerics.RPNumericsProfile;
import rpnumerics.physics.FluxParams;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import rpnumerics.PhasePoint;
import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;
import org.xml.sax.Parser;
import org.xml.sax.InputSource;
import java.io.FileWriter;
import java.util.StringTokenizer;
import java.io.*;
import java.util.ArrayList;
import wave.util.RectBoundary;
import wave.util.IsoTriang2DBoundary;

/** This class implements methods to configure the numeric layer. The values are taked from a XML file and this values are used to setup the physics and all others numerics parameters. */

public class RPnNumericsModule {
    //
    // Constants
    //
    private static RPNumericsProfile profile_ = new RPNumericsProfile();
    private static PhasePoint bufferedPhasePoint_;
    private static double sigma_;
    private static RPnMenuCommand command_;

    static class InputHandler extends HandlerBase {
        //
        // Members
        //
        private RealVector tempVector_;
        private String currentElement_;

        private ArrayList boundsVectorArray_;

        public void startElement(String name, AttributeList att) throws
                SAXException {

            currentElement_ = name;

            if (name.equals("NUMERICS")) {

                try {
                    profile_.initSpecificHugoniotAndFlow(new Boolean(att.
                            getValue(1)).
                            booleanValue(),
                            new Boolean(att.getValue(2)).
                            booleanValue());
                } catch (Exception ex) {
                    System.out.println("Error in Hugoniot or Flow Method Setup");
                }
                profile_.setContourMethod(new Boolean(att.getValue(0)).
                                          booleanValue());

            }

            if (name.equals("PHYSICS")) {
                profile_.initPhysics(att.getValue(0),
                                     new Boolean(att.getValue(1)),
                                     att.getValue(2),new Integer(att.getValue(3)),new Integer(att.getValue(4)));




            }

            if (name.equals("BOUNDARY")) {
                int boundsNumber = new Integer(att.getValue(0)).intValue();
                boundsVectorArray_ = new ArrayList(boundsNumber);
            }

            if (name.equals("CURVE")) {


                StringTokenizer tokenizer = new StringTokenizer(att.getValue(1));
                int [] variations = new int[tokenizer.countTokens()];
                int i = 0;
                while (tokenizer.hasMoreTokens()) {
                    variations[i]= (new Integer(tokenizer.nextToken())).intValue();
                       i++;
                   }
                   profile_.setVariations(variations);
                   profile_.setCurveName(att.getValue(0));
            }

            if (name.equals("FLOWTYPE")) {
                profile_.initFlowType(att.getValue(0));

            }

            if (name.equals("RAREFACTIONFLOW")) {

                profile_.setFamilyIndexFlow ((new Integer(att.getValue(0)).intValue()));
            }

            if (name.equals("FLUXPARAMS")) {
                StringTokenizer tokenizer = new StringTokenizer(att.getValue(0));
                double doubleList[] = new double[tokenizer.countTokens()];
                int i = 0;
                while (tokenizer.hasMoreTokens()) {
                    doubleList[i++] = new Double(tokenizer.nextToken()).
                                      doubleValue();
                }
                RPNUMERICS.fluxFunction().fluxParams().setParams(doubleList);

            }

            if (name.equals("PHASEPOINT")) {

                tempVector_ = new RealVector((new Integer(att.getValue(0))).
                                             intValue());

            }
            if (name.equals("SHOCKFLOWDATA")) {

                sigma_ = new Double(att.getValue(0)).doubleValue();

            }

        }

        public void characters(char[] buff, int offset, int len) throws
                SAXException {

            String data = new String(buff, offset, len);
            data = data.trim();
            if (data.length() != 0) {

                if (currentElement_.equals("PHASEPOINT")) {
                    StringTokenizer tokenizer = new StringTokenizer(data);
                    int i = 0;
                    while (tokenizer.hasMoreTokens()) {
                        tempVector_.setElement(i,
                                               (new Double(tokenizer.nextToken()).
                                                doubleValue()));
                        i++;
                    }
                    bufferedPhasePoint_ = new PhasePoint(tempVector_);
                }

                if (currentElement_.equals("REALVECTOR")) {
                    tempVector_ = new RealVector(data);
                    boundsVectorArray_.add(tempVector_);

                }

            }

        }
        public void endElement(String name) throws SAXException {
            if (name.equals("PHYSICS")) {

                try {
                    RPNUMERICS.init(profile_);

                } catch (rpnumerics.RpException e) {
                    e.printStackTrace();
                }
            }

            if (name.equals("BOUNDARY")) {

                if (boundsVectorArray_.size() == 2) {
                    profile_.setBoundary(new RectBoundary((RealVector)boundsVectorArray_.get(0),(RealVector)boundsVectorArray_.get(1)));
                }

                if (boundsVectorArray_.size() == 3) {
                    profile_.setBoundary(new IsoTriang2DBoundary((RealVector)boundsVectorArray_.get(0),(RealVector)boundsVectorArray_.get(1),(RealVector)boundsVectorArray_.get(2)));
                }
            }

            if (name.equals("CURVE")) {

//                for (int i=0;i < variations_.length; i++){
//
//                    System.out.println("Variation " + i + " " + variations_[i]);
//                }

            }

            if (name.equals("SHOCKFLOWDATA")) {

                RPNUMERICS.resetShockFlow(bufferedPhasePoint_, sigma_);

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

    public static void setCommand(RPnMenuCommand command) {

        command_ = command;

    }

    /** Writes the actual values of the numerics parameters into a XML file. */

    public static void export(FileWriter writer) throws java.io.IOException {

        writer.write("<NUMERICS  contour=\"" +
                     RPNUMERICS.getProfile().isContourMethod() + "\"" +
                     " specifichugoniot=\"" +
                     RPNUMERICS.getProfile().isSpecificHugoniot() + "\"" +
                     " specificflow=\"" +
                     RPNUMERICS.getProfile().isSpecificShockFlow() + "\"" +
                     ">\n");

        writer.write("<PHYSICS physid=\"" + RPNUMERICS.physicsID() + "\"" +
                     " isnative=\"" + RPNUMERICS.isNativePhysics() + "\"" +
                     " libname=\"" + RPNUMERICS.getProfile().libName() + "\"" +
                     "></PHYSICS>\n");
//                 " familyindex=\""+RPNUMERICS.getProfile().getFamilyIndex().intValue()+"\""+
        writer.write("<FLOWTYPE type=\"" + RPNUMERICS.getProfile().getFlowType() +
                     "\">" + "</FLOWTYPE>\n");
        writer.write("</NUMERICS>\n");

        // export the physics params if different than default...
        FluxParams current = RPNUMERICS.fluxFunction().fluxParams();
        if (!(current.equals(RPNUMERICS.fluxFunction().fluxParams().
                             defaultParams()))) {
            writer.write("<FLUXPARAMS paramsarray=\"");
            for (int i = 0; i < current.getParams().getSize(); i++) {
                writer.write(current.getParams().getElement(i) + " ");
            }
            writer.write("\">\n</FLUXPARAMS>\n");
        }
    }
}
