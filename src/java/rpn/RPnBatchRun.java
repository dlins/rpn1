/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import rpn.parser.*;
import java.io.*;
import java.util.GregorianCalendar;
import javax.swing.filechooser.FileNameExtensionFilter;
import rpnumerics.RPNUMERICS;



public class RPnBatchRun {

            public static RPnConfigReader configReader_;
            public static InputStream configStream_;
            public static String dir = "";

            public void RPnBatchRun() {}

            public static void main(String args[]) {

                try {

                    configReader_ = RPnConfigReader.getReader(args[0], false, null);
                    configStream_ = configReader_.read();
                    configReader_.init(configStream_); //Reading input file                   

                    configReader_.exec(configStream_); //Reading input file

                    exportData(args[1]);

                } finally {

                    try {

                        configStream_.close();

                    } catch (NullPointerException ex) {

                    } catch (IOException ex) {
                        
                        System.out.println("IO Error");
                    }

                }

	    }
       
            public static void exportData(String fileName) {

                RPnFileWriter.batchExport(fileName);
                    

            }
}
