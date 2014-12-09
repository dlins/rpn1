/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

//import wave.multid.Space;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.*;
import java.util.SortedSet;
import java.util.TreeSet;

import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import rpn.command.BoundaryExtensionCurveCommand;
import rpn.command.BuckleyLeverettiInflectionCommand;
import rpn.command.CoincidencePlotCommand;
import rpn.command.CompositePlotCommand;
import rpn.command.DerivativeDiscriminantLevelCurvePlotCommand;
import rpn.command.DiscriminantLevelCurvePlotCommand;
import rpn.command.DiscriminantPointLevelCurvePlotCommand;
import rpn.command.DoubleContactCommand;
import rpn.command.EllipticBoundaryCommand;
import rpn.command.EllipticBoundaryExtensionCommand;
import rpn.command.EnvelopeCurveCommand;
import rpn.command.HugoniotPlotCommand;
import rpn.command.HysteresisPlotCommand;
import rpn.command.InflectionPlotCommand;
import rpn.command.IntegralCurvePlotCommand;
import rpn.command.LevelCurvePlotCommand;
import rpn.command.PhysicalBoundaryPlotCommand;
import rpn.command.PointLevelCurvePlotCommand;
import rpn.command.RarefactionCurvePlotCommand;
import rpn.command.RarefactionExtensionCurvePlotCommand;
import rpn.command.RpModelPlotCommand;
import rpn.command.SecondaryBifurcationCurveCommand;
import rpn.command.ShockCurvePlotCommand;
import rpn.command.SubInflectionPlotCommand;
import rpn.command.TransitionalLinePlotCommand;
import rpn.command.WaveCurvePlotCommand;
import rpn.configuration.Configuration;

/**
 * This class configures the initial visualization properties. Reading a XML
 * file that contains the necessary information, this class sets the axis,
 * labels , domain, etc to represents correctly the physics.
 */
public class RPnInterfaceModule {

    private static SortedSet<RpModelPlotCommand> toolBar_;
    private static SortedSet<RpModelPlotCommand> auxtoolBar_;

    private static class RPnInterfaceParser implements ContentHandler {

        public void startElement(String uri, String localName, String qName, Attributes att) throws SAXException {

            if (localName.equals("CURVECONFIGURATION")) {

                if (att.getValue("name").equals("hugoniotcurve")) {
                    toolBar_.add(HugoniotPlotCommand.instance());

                }

                if (att.getValue("name").equals("fundamentalcurve")) {

                    toolBar_.add(ShockCurvePlotCommand.instance());
                    toolBar_.add(RarefactionCurvePlotCommand.instance());
                    toolBar_.add(IntegralCurvePlotCommand.instance());
                    toolBar_.add(CompositePlotCommand.instance());
                    toolBar_.add(WaveCurvePlotCommand.instance());

                }

                if (att.getValue("name").equals("doublecontactcurve")) {
                    toolBar_.add(DoubleContactCommand.instance());
                }
                if (att.getValue("name").equals("inflectioncurve")) {
                    toolBar_.add(InflectionPlotCommand.instance());
                }

                if (att.getValue("name").equals("envelopecurve")) {
                    toolBar_.add(EnvelopeCurveCommand.instance());
                }

                if (att.getValue("name").equals("hysteresiscurve")) {
                    toolBar_.add(HysteresisPlotCommand.instance());
                }

                if (att.getValue("name").equals("boundaryextensioncurve")) {
                    toolBar_.add(BoundaryExtensionCurveCommand.instance());
                }

                if (att.getValue("name").equals("coincidencecurve")) {
                    toolBar_.add(CoincidencePlotCommand.instance());

                }

                if (att.getValue("name").equals("subinflectioncurve")) {
                    toolBar_.add(SubInflectionPlotCommand.instance());

                }

                if (att.getValue("name").equals("secondarybifurcationcurve")) {
                    toolBar_.add(SecondaryBifurcationCurveCommand.instance());

                }

                if (att.getValue("name").equals("buckleylevertcurve")) {
                    toolBar_.add(BuckleyLeverettiInflectionCommand.instance());

                }

                if (att.getValue("name").equals("rarefactionextensioncurve")) {
                    toolBar_.add(RarefactionExtensionCurvePlotCommand.instance());
                }

                //Aux tool bar
                if (att.getValue("name").equals("ellipticboundary")) {
                    auxtoolBar_.add(EllipticBoundaryCommand.instance());

                }

                if (att.getValue("name").equals("ellipticboundaryextension")) {
                    auxtoolBar_.add(EllipticBoundaryExtensionCommand.instance());
                }

                if (att.getValue("name").equals("derivativediscriminant")) {
                    auxtoolBar_.add(DerivativeDiscriminantLevelCurvePlotCommand.instance());
                    auxtoolBar_.add(DerivativeDiscriminantLevelCurvePlotCommand.instance());

                }

                if (att.getValue("name").equals("discriminantlevelcurve")) {
                    auxtoolBar_.add(DiscriminantPointLevelCurvePlotCommand.instance());
                    auxtoolBar_.add(DiscriminantLevelCurvePlotCommand.instance());

                }

                if (att.getValue("name").equals("physicalboundary")) {
                    auxtoolBar_.add(PhysicalBoundaryPlotCommand.instance());

                }

                if (att.getValue("name").equals("levelcurve")) {
                    auxtoolBar_.add(LevelCurvePlotCommand.instance());
                    auxtoolBar_.add(PointLevelCurvePlotCommand.instance());
                }

                if (att.getValue("name").equals("transitionalline")) {
                    auxtoolBar_.add(TransitionalLinePlotCommand.instance());
                    Configuration config = rpnumerics.RPNUMERICS.getConfiguration("transitionalline");

                    
                    config.addObserver(TransitionalLinePlotCommand.instance());
                    
//                    for (Parameter parameter : config.getParamList()) {
//                        parameter.addObserver(TransitionalLinePlotCommand.instance());
//                    }

                }

            }

        }

        public void endElement(String uri, String localName, String qName) throws SAXException {

        }

        public void setDocumentLocator(Locator locator) {
        }

        public void startDocument() throws SAXException {
        }

        public void endDocument() throws SAXException {

        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {
        }

        public void endPrefixMapping(String prefix) throws SAXException {
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        }

        public void processingInstruction(String target, String data) throws SAXException {
        }

        public void skippedEntity(String name) throws SAXException {
        }
    }
    //
// Initializers
//

    /**
     * Initializes the XML parser to configure the visualization properties.
     */
    public static void init(XMLReader parser, String file) {
        try {

            parser.setContentHandler(new RPnInterfaceParser());
            parser.parse(file);

        } catch (Exception saxex) {
            saxex.printStackTrace();

        }
    }

    /**
     * Initializes the XML parser to configure the visualization properties.
     */
    public static void init(XMLReader parser, InputStream configFileStream) {
        try {

            parser.setContentHandler(new RPnInterfaceParser());

            toolBar_ = new TreeSet();
            auxtoolBar_ = new TreeSet();

            System.out.println("Interface Module parsing started...");
            parser.parse(new InputSource(configFileStream));

            System.out.println("Interface Module parsing finished sucessfully !");

        } catch (Exception saxex) {
            saxex.printStackTrace();

        }
    }

    public static SortedSet<RpModelPlotCommand> getMainToolSelectedButtons() {
        return toolBar_;
    }

    public static SortedSet<RpModelPlotCommand> getAuxToolSelectedButtons() {
        return auxtoolBar_;
    }

}
