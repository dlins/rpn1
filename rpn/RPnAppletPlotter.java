/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import rpn.controller.ui.*;

import javax.swing.JApplet;

import java.io.*;

public class RPnAppletPlotter extends JApplet implements RPnMenuCommand {

    private RPnConfigReader configReader_;
    private InputStream  configStream_;
    private String configFileName_;
    private RPnUIFrame rpnUIFrame_=null;

    public void init() {
	configFileName_= getParameter("configfile"); // The ConfigFile parameter will contain the name of the config file, e.g. "quad2.xml"
	configReader_= RPnConfigReader.getReader(configFileName_,true,this);
	configStream_=configReader_.read();
	configReader_.init(configStream_);
	rpnUIFrame_=new RPnUIFrame(this);
    }
    public void start (){
	if (rpnUIFrame_!=null){
	    RPnPhaseSpaceFrame [] frames=rpnUIFrame_.getPhaseSpaceFrames();
	    if ( frames==null){
		rpnUIFrame_.phaseSpaceFramesInit(rpnumerics.RpNumerics.boundary());
	    }
	    rpnUIFrame_.setVisible(true);
	    rpnUIFrame_.pack();
	    rpnUIFrame_.repaint();

	}else{
	    rpnUIFrame_=new RPnUIFrame(this);
	    //	    UIController.instance().setState(new XZERO_CONFIG());
	}
    }
    public void destroy(){
	RPnPhaseSpaceFrame [] frames=rpnUIFrame_.getPhaseSpaceFrames();
	if (frames !=null){
	    for (int i=0;i< frames.length;i++){
		frames[i].dispose();
	    }
	}
	rpnUIFrame_.dispose();
    }

    public void stop(){

	rpnUIFrame_.setVisible(false);

	RPnPhaseSpaceFrame [] frames=rpnUIFrame_.getPhaseSpaceFrames();

	if (frames !=null){

	    for (int i=0;i< frames.length;i++){

		frames[i].setVisible(false);
	    }

	}

	//Reset the application

	UIController.instance().setState(new SHOCK_CONFIG());

    }

   public void finalizeApplication(){
	destroy();
    }

    public void networkCommand(){

    }








}

