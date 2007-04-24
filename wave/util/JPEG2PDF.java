/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util;

import java.io.*;
import com.pdflib.pdflib;

public class JPEG2PDF {
    public static void convert(String sourceFileAbsPath, String targetAbsPath) throws OutOfMemoryError, IOException,
        IllegalArgumentException, IndexOutOfBoundsException, ClassCastException, ArithmeticException, RuntimeException,
        InternalError, UnknownError {
            int image;
            float width, height;
            pdflib p;
            String imagefile = sourceFileAbsPath;
            p = new pdflib();
            if (p.open_file(targetAbsPath) == -1) {
                System.err.println("Couldn't open target file \n");
                System.exit(1);
            }

/*	p.set_info("Creator", "image.java");
	p.set_info("Author", "Thomas Merz");
	p.set_info("Title", "image sample (Java)");*/

            image = p.open_image_file("jpeg", imagefile, "", 0);
            if (image == -1) {
                System.err.println("Couldn't open source file.\n");
                System.exit(1);
            }
            // See the PDFlib manual for more advanced image size calculations
            width = p.get_value("imagewidth", image);
            height = p.get_value("imageheight", image);
            // We generate a page with the image's dimensions
            p.begin_page(width, height);
            p.place_image(image, (float)0.0, (float)0.0, (float)1.0);
            p.close_image(image);
            p.end_page();
            p.close();
    }
}
