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
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;

import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import rpn.command.BoundaryExtensionCurveCommand;
import rpn.command.BuckleyLeverettiInflectionCommand;
import rpn.command.CoincidencePlotCommand;
import rpn.command.CompositePlotCommand;
import rpn.command.DoubleContactCommand;
import rpn.command.EnvelopeCurveCommand;
import rpn.command.HugoniotPlotCommand;
import rpn.command.HysteresisPlotCommand;
import rpn.command.InflectionPlotCommand;
import rpn.command.IntegralCurvePlotCommand;
import rpn.command.RarefactionCurvePlotCommand;
import rpn.command.RarefactionExtensionCurvePlotCommand;
import rpn.command.SecondaryBifurcationCurveCommand;
import rpn.command.ShockCurvePlotCommand;
import rpn.command.SubInflectionPlotCommand;
import rpn.command.WaveCurvePlotCommand;

/**
 * This class configures the initial visualization properties. Reading a XML
 * file that contains the necessary information, this class sets the axis,
 * labels , domain, etc to represents correctly the physics.
 */
public class RPnInterfaceModule {

    private static List<AbstractButton> toolBar_;

    private static class RPnInterfaceParser implements ContentHandler {

        public void startElement(String uri, String localName, String qName, Attributes att) throws SAXException {

            if (localName.equals("CURVECONFIGURATION")) {

                if (att.getValue("name").equals("hugoniotcurve")) {
                    toolBar_.add(HugoniotPlotCommand.instance().getContainer());

                }

                if (att.getValue("name").equals("fundamentalcurve")) {

                    toolBar_.add(ShockCurvePlotCommand.instance().getContainer());
                    toolBar_.add(RarefactionCurvePlotCommand.instance().getContainer());
                    toolBar_.add(IntegralCurvePlotCommand.instance().getContainer());
                    toolBar_.add(CompositePlotCommand.instance().getContainer());
                    toolBar_.add(WaveCurvePlotCommand.instance().getContainer());

                }

                if (att.getValue("name").equals("doublecontactcurve")) {
                    toolBar_.add(DoubleContactCommand.instance().getContainer());
                }
                if (att.getValue("name").equals("inflectioncurve")) {
                    toolBar_.add(InflectionPlotCommand.instance().getContainer());
                }

                if (att.getValue("name").equals("envelopecurve")) {
                    toolBar_.add(EnvelopeCurveCommand.instance().getContainer());
                }

                if (att.getValue("name").equals("hysteresiscurve")) {
                    toolBar_.add(HysteresisPlotCommand.instance().getContainer());
                }

                if (att.getValue("name").equals("boundaryextensioncurve")) {
                    toolBar_.add(BoundaryExtensionCurveCommand.instance().getContainer());
                }

                if (att.getValue("name").equals("coincidencecurve")) {
                    toolBar_.add(CoincidencePlotCommand.instance().getContainer());
              
                }
                
                
                if (att.getValue("name").equals("subinflectioncurve")) {
                    toolBar_.add(SubInflectionPlotCommand.instance().getContainer());
                    

                }
                
                if (att.getValue("name").equals("secondarybifurcationcurve")) {
                    toolBar_.add(SecondaryBifurcationCurveCommand.instance().getContainer());

                }
                
                
                if (att.getValue("name").equals("buckleylevertcurve")) {
                    toolBar_.add(BuckleyLeverettiInflectionCommand.instance().getContainer());

                }
                
              
                if (att.getValue("name").equals("rarefactionextensioncurve")) {
                     toolBar_.add(RarefactionExtensionCurvePlotCommand.instance().getContainer());
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

            toolBar_ = new ArrayList<AbstractButton>();
                        System.out.println("Interface Module parsing started...");
            parser.parse(new InputSource(configFileStream));

            System.out.println("Interface Module parsing finished sucessfully !");

        } catch (Exception saxex) {
            saxex.printStackTrace();

        }
    }

    public static List<AbstractButton> getMainToolSelectedButtons() {
        return toolBar_;
    }

}
