/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.parser;
import rpn.*;

import wave.multid.Space;
import rpnumerics.RpNumerics;
import org.xml.sax.HandlerBase;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.AttributeList;
import org.xml.sax.InputSource;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.*;

/** This class configures the initial visualization properties. Reading a XML file that contains the necessary information, this class sets the axis, labels , domain, etc to represents correctly the physics. */

public class RPnVisualizationModule {
    static public List DESCRIPTORS;

    static class InputHandler extends HandlerBase {
        String domain_;
        String label_;
        String axis_;
        String vpw_;
        String vph_;
        boolean iso2equi_ = false;

        public void startElement(String name, AttributeList att) throws SAXException {
          if (name.equals("VIEWINGPARAMS")){
              domain_ = att.getValue(0);

            }
            if (name.equals("ISO2EQUI_TRANSFORM"))
                iso2equi_ = true;
            if (name.equals("PROJDESC")) {
                iso2equi_ = false;
                label_ = att.getValue(0);
                axis_ = att.getValue(1);
                vpw_ = att.getValue(2);
                vph_ = att.getValue(3);
            }
        }

        public void endElement(String name) throws SAXException {
            if (name.equals("PROJDESC")) {
                StringTokenizer axisTokenizer = new StringTokenizer(axis_);
                int[] projIndices = new int[axisTokenizer.countTokens()];
                int i = 0;
                while (axisTokenizer.hasMoreTokens())
                    projIndices[i++] = new Integer(axisTokenizer.nextToken()).intValue();
                DESCRIPTORS.add(
                    new RPnProjDescriptor(new Space("", new Integer(domain_).intValue()), label_, new Integer(vpw_).intValue(),
                    new Integer(vph_).intValue(), projIndices, iso2equi_));
            }
        }






    }


    //
    // Initializers
    //

    /** Initializes the XML parser to configure the visualization properties. */

    public static void init(Parser parser, String file) {
        try {
            DESCRIPTORS = new ArrayList();
            parser.setDocumentHandler(new InputHandler());
            parser.parse(file);
        } catch (Exception saxex) {
            saxex.printStackTrace();
        }
    }

    /** Initializes the XML parser to configure the visualization properties. */

    public static  void init(Parser parser, InputStream configFileStream) {
        try {
            DESCRIPTORS = new ArrayList();
            parser.setDocumentHandler(new InputHandler());
	    System.out.println ("Visualization Module");

	    System.out.println("Will parse !");
	    parser.parse(new InputSource(configFileStream));

	    System.out.println("parsed !");
        } catch (Exception saxex) {
            saxex.printStackTrace();
        }
    }



    //
    // Methods
    //

    /** Writes the actual visualization configuration into a XML file. */

    static public void export(FileWriter writer) throws java.io.IOException {
        writer.write("<VIEWINGPARAMS modeldomain=\"" + RpNumerics.domainDim() + "\">\n");
        for (int i = 0; i < DESCRIPTORS.size(); i++)
            writer.write(((RPnProjDescriptor)DESCRIPTORS.get(i)).toXML() + "\n");
        writer.write("</VIEWINGPARAMS>\n");
    }
}
