/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.plugininterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class PluginInfoParser {

    private String pluginName_;
    private String pluginLibName_;
    private String pluginVersion_;
    private HashMap<String, String> pluginClasses_;
    private HashMap<String, HashMap<String, Vector<HashMap<String, String>>>> library_;
    private HashMap<String, PluginProfile> pluginProfileMap_;
    private String currentPluginType_;

    public PluginInfoParser() {
        pluginClasses_ = new HashMap<String, String>();//ClassName,constructorMethod
        library_ = new HashMap<String, HashMap<String, Vector<HashMap<String, String>>>>();
        //HashMap<LibName, HashMap<PluginType,Vector <HashMap<ClassName,constructorMethod>>>> 

        pluginProfileMap_ = new HashMap<String, PluginProfile>(1);
    //HashMap<PluginType,PluginProfile>;
    }

    public void parse() {

        library_.clear();

        try {

            File pluginDir = new File(PluginTableModel.getPluginDir());

            File[] filesArray = pluginDir.listFiles(new FileName());

            for (int i = 0; i < filesArray.length; i++) {
                XMLReader xmlReader = XMLReaderFactory.createXMLReader();
                xmlReader.setContentHandler(new PluginInfoHandler());
                InputStream inputStream = new FileInputStream(filesArray[i]);
                xmlReader.parse(new InputSource(inputStream));
            }

        } catch (SAXException ex) {
            ex.printStackTrace();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    public String getPluginVersion() {
        return pluginVersion_;
    }

    public String getPluginName() {
        return pluginName_;
    }

    public String getPluginLibName() {
        return pluginLibName_;
    }

    public HashMap<String, String> getPluginClasses() {
        return pluginClasses_;
    }

    public HashMap<String, HashMap<String, Vector<HashMap<String, String>>>> getLibrary() {
        return library_;
    }

    private class FileName implements FilenameFilter {

        public boolean accept(File dir, String name) {

            if (name.endsWith(".xml")) {
                return true;
            }
            return false;
        }
    }

    private class PluginInfoHandler extends DefaultHandler {

        public PluginInfoHandler() {
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {


            if (localName.equals("NAME")) {

                pluginName_ = attributes.getValue(0);
                pluginVersion_ = attributes.getValue(1);


            }

            if (localName.equals("PLUGINCONFIG")) {
                currentPluginType_ = attributes.getValue(0);
                PluginProfile profile = new PluginProfile();

                pluginProfileMap_.put(currentPluginType_, profile);

            }

            if (localName.equals("PARAM")) {

                PluginProfile profile = pluginProfileMap_.get(currentPluginType_);

                profile.addPluginParam(attributes.getValue(0), attributes.getValue(1));

                pluginProfileMap_.put(currentPluginType_, profile);

            }

            if (localName.equals("LIBRARY")) {
                pluginLibName_ = attributes.getValue(0);

                HashMap<String, Vector<HashMap<String, String>>> libraryContent = new HashMap<String, Vector<HashMap<String, String>>>();
                library_.put(pluginLibName_, libraryContent);

            }


            if (localName.equals("CLASS")) {

                HashMap<String, String> newClass = new HashMap<String, String>(1);
                newClass.put(attributes.getValue(0),"constructor=\""+ attributes.getValue(2)+"\""+" "+"destructor=\""+attributes.getValue(3)+"\"");
                HashMap<String, Vector<HashMap<String, String>>> actualLibraryContent = library_.get(pluginLibName_);

                if (actualLibraryContent.containsKey((String) attributes.getValue(1))) {// This plugin type exists 

                    Vector<HashMap<String, String>> contentVector = actualLibraryContent.get(attributes.getValue(1));
                    contentVector.add(newClass);
                    actualLibraryContent.put(attributes.getValue(1), contentVector);
                    library_.put(pluginLibName_, actualLibraryContent);

                } else {

                    Vector<HashMap<String, String>> classVector = new Vector<HashMap<String, String>>();
                    classVector.add(newClass);
                    actualLibraryContent.put(attributes.getValue(1), classVector);
                    library_.put(pluginLibName_, actualLibraryContent);

                }

            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (localName.equals("PLUGINCONFIG")) {
                PluginTableModel.setPluginConfigMap(pluginProfileMap_);

            }

        }
    }
}
