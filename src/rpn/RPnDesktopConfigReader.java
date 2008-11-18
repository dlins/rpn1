package rpn;

import java.io.*;
import javax.swing.ImageIcon;

public class RPnDesktopConfigReader extends RPnConfigReader{

    private FileInputStream configFileInputStream_;
    private ByteArrayInputStream buff_;

    public static String IMAGEPATH = System.getProperty("rpnhome") + System.getProperty("file.separator")+"share"+System.getProperty("file.separator")+"rpn-images"+System.getProperty("file.separator");

    static private void remoteImage (){

	RPnConfigReader.HUGONIOT= new ImageIcon (IMAGEPATH+"hugoniot.jpg");
	RPnConfigReader.MANIFOLD_BWD= new ImageIcon(IMAGEPATH+"manifold_bwd.jpg");
	RPnConfigReader.MANIFOLD_FWD= new ImageIcon (IMAGEPATH+"manifold_fwd.jpg");
	RPnConfigReader.POINCARE= new ImageIcon  (IMAGEPATH+"poincare.jpg");
	RPnConfigReader.ORBIT_FWD= new ImageIcon  (IMAGEPATH+"orbit_fwd.jpg");
	RPnConfigReader.ORBIT_BWD= new ImageIcon  (IMAGEPATH+"orbit_bwd.jpg");
	RPnConfigReader.STATPOINT= new ImageIcon  (IMAGEPATH+"statpoint.jpg");

    }

    public RPnDesktopConfigReader (String file){

	try {

	    remoteImage();

	    configFileInputStream_= new FileInputStream(file);

	    File configFile = new File (file);

	    byte [] bufArray=new byte[(int)configFile.length()];

	    configFileInputStream_.read(bufArray,0,(int)configFile.length());

	    buff_= new ByteArrayInputStream(bufArray);

	}catch (Exception e){

	    System.err.println(e.toString());
	}

    }


    public InputStream  read(){

	return  buff_;


    }
}

