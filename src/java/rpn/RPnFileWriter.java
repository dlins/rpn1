/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn;

import java.io.*;
import rpn.parser.*;

public class RPnFileWriter extends FileWriter {

    public RPnFileWriter(String fileName) throws IOException {

        super(fileName);
        openSession();
    }

    public void openSession() {

        try {


            write(RPnConfigReader.XML_HEADER);
            write("<RPNSESSION>\n");
            write(" <PHASESPACE name=\"Phase Space\">\n");
            write("  <RPNCONFIGURATION>\n");

        } catch (IOException exception) {


            System.out.println("IO Error");
        }
    }

    public static void batchExport(String fileName) {


        try {

            RPnFileWriter writer = new RPnFileWriter(fileName);

            RPnNumericsModule.export(writer);
            RPnVisualizationModule.export(writer);

            writer.write("  </RPNCONFIGURATION>\n");

            RPnDataModule.export(writer);

            writer.closeSession();

        } catch (FileNotFoundException ex) {
            System.out.println("Could not write to output file");

        } catch (IOException exception) {


                System.out.println("IO Error");
        }
    }


    public static void desktopExport(String fileName) {


        try {

            RPnFileWriter writer = new RPnFileWriter(fileName);

            RPnNumericsModule.export(writer);
            RPnVisualizationModule.export(writer);

            writer.write("  </RPNCONFIGURATION>\n");

            RPnDataModule.export(writer);
            RPnCommandModule.export(writer);

            writer.closeSession();

        } catch (FileNotFoundException ex) {
            System.out.println("Could not write to output file");

        } catch (IOException exception) {


                System.out.println("IO Error");
        }
    }


    public void closeSession() {

        try {


            write(" </PHASESPACE>\n");
            write("</RPNSESSION>");
            close();

        } catch (IOException exception) {
            System.out.println("IO Error");
        }
    }
}
