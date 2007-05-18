package rpn;

import java.io.*;
import java.net.*;
import javax.swing.JApplet;
import rpn.RPnConfigReader;
import javax.swing.ImageIcon;

public class RPnAppletConfigReader extends RPnConfigReader{


    private InputStream configFileInputStream_1;
    
    private ByteArrayInputStream buff_;

    private  URL url1;
  
    private URLConnection serverCon1;

    private static JApplet  APPLET;

    static public URL IMAGEPATH= null;

    static private void remoteImage (JApplet applet){

	RPnConfigReader.HUGONIOT=  new ImageIcon(applet.getImage(applet.getCodeBase(),"images/hugoniot.jpg"));
	RPnConfigReader.MANIFOLD_BWD=  new ImageIcon(applet.getImage(applet.getCodeBase(),"images/manifold_bwd.jpg"));
	RPnConfigReader.MANIFOLD_FWD=  new ImageIcon(applet.getImage(applet.getCodeBase(),"images/manifold_fwd.jpg"));
	RPnConfigReader.POINCARE=  new ImageIcon(applet.getImage(applet.getCodeBase(),"images/poincare.jpg"));
	RPnConfigReader.ORBIT_FWD=  new ImageIcon(applet.getImage(applet.getCodeBase(),"images/orbit_fwd.jpg"));
	RPnConfigReader.ORBIT_BWD=  new ImageIcon(applet.getImage(applet.getCodeBase(),"images/orbit_bwd.jpg"));
	RPnConfigReader.STATPOINT= new ImageIcon (applet.getImage(applet.getCodeBase(),"images/statpoint.jpg"));

    }

    public RPnAppletConfigReader (String file,JApplet applet){
	
	try {

	    remoteImage(applet);

	    System.out.println(applet.getCodeBase());

	    url1 = new URL(applet.getCodeBase(),file);
	    
	    serverCon1 = url1.openConnection();
    
	    serverCon1.setDoInput(true);
	    
	    configFileInputStream_1 = serverCon1.getInputStream();

	    byte [] bufArray = new byte[configFileInputStream_1.available()];

	    configFileInputStream_1.read(bufArray,0,configFileInputStream_1.available());
	    
	    buff_ =  new  ByteArrayInputStream(bufArray);

	    
	}catch (Exception e){
	    
	    System.err.println(e.toString());
	}
	
    }



    public static void  downloadLibrary (String libName,String dirName){

	try{

	    URL url = new URL("jar:"+APPLET.getCodeBase()+"rpnApplet.jar!"+"/"+libName);
	    
	    JarURLConnection serverCon = (JarURLConnection)url.openConnection();
	    
	    serverCon.setDoInput(true);
	    
	    BufferedInputStream in= new BufferedInputStream(serverCon.getInputStream());

	    FileOutputStream fileOut = new  FileOutputStream(dirName) ;
	    
	    BufferedOutputStream out = new BufferedOutputStream(fileOut);

	    int b;

	    while ((b = in.read()) != -1){
		
		fileOut.write(b);
	    }

	    in.close();
   
	    fileOut.close();
	    
	}catch (Exception e){

	    //	    System.out.println ("Erro no download da biblioteca !");

	    //	    System.out.println(e.toString());
	}


    }

    public InputStream read(){
	
	return buff_;
	
    }  
        
    
}
