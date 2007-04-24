//TODO : Usar dois Hashtables para armazenar altura e comprimento do frame e usar no redimensionamento

package rpn;

import rpn.controller.PhaseSpacePanelController;
import wave.multid.view.Scene;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import rpn.*;
import java.util.*;
import rpn.controller.ui.UIController;

/**
 * <p>Title: RPn GUI</p> <p>Description: GUI components for </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: FLUID</p>
 * @author Mario de Sa Vera
 * @version 1.3
 */
public class RPnPhaseSpaceFrame extends JFrame {
    JPanel contentPane;
    RPnPhaseSpacePanel phaseSpacePanel = null;
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel statusPanel = new JPanel();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JPanel jPanel4 = new JPanel();
    JPanel jPanel5 = new JPanel();
    RPnCursorMonitor coordsField = new RPnCursorMonitor();
    BorderLayout borderLayout2 = new BorderLayout();
    RPnMenuCommand commandMenu_=null;
    JSlider slider = new JSlider(-5,5,0);


    private Hashtable labels_=new Hashtable();

    private Dimension frameSize_=null;

    //    int sliderValue_;

    public RPnPhaseSpaceFrame(Scene scene,RPnMenuCommand command) {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
	//	enableEvents(AWTEvent.FOCUS_EVENT_MASK);


	commandMenu_=command;
        try {
            phaseSpacePanel = new RPnPhaseSpacePanel(scene);
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Component initialization
    private void jbInit() throws Exception {
        //setIconImage(Toolkit.getDefaultToolkit().createImage(Frame.class.getResource("[Your Icon]")));

        slider.setEnabled(true);

        contentPane = (JPanel)this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setResizable(false);
        jPanel2.setBackground(Color.gray);
        jPanel3.setBackground(Color.gray);
        jPanel4.setBackground(Color.gray);
	contentPane.setBackground(Color.gray);
        setTitle(" RPn - " + new Integer(((PhaseSpacePanelController)phaseSpacePanel.getCastedUI()).getAbsIndex()).intValue() + ',' +
            new Integer(((PhaseSpacePanelController)phaseSpacePanel.getCastedUI()).getOrdIndex()).intValue());
        phaseSpacePanel.addMouseMotionListener(coordsField.getMouseMotionController());
        statusPanel.setBackground(Color.gray);
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.setLayout(borderLayout2);
        coordsField.setText("50:50");
        contentPane.add(phaseSpacePanel, BorderLayout.CENTER);
        contentPane.add(statusPanel, BorderLayout.SOUTH);
        contentPane.add(jPanel2, BorderLayout.WEST);
        contentPane.add(jPanel3, BorderLayout.EAST);
        contentPane.add(jPanel4, BorderLayout.NORTH);
        statusPanel.add(coordsField, BorderLayout.EAST);
        statusPanel.add(jPanel5, BorderLayout.SOUTH);
	jPanel5.add(slider);

	slider.setMajorTickSpacing(1);
	slider.setPaintLabels(true);

	slider.setPaintTicks(true);
	slider.setSnapToTicks(true);
	createLabels(slider.getMinimum(),slider.getMaximum());

	slider.setLabelTable(labels_);

	//	slider.addChangeListener(new SliderState(RPnUIFrame.getDefaultFrameSize().height));

       	slider.addChangeListener(new SliderState());

        //    statusPanel.add(jProgressBar1,  BorderLayout.WEST);
        //    setSize(new Double(phaseSpacePanel.getPreferredSize().getWidth() + jPanel2.getPreferredSize().getWidth() +
        // jPanel3.getInsets().left + jPanel3.getInsets().right + jPanel3.getPreferredSize().getWidth()).intValue() + getInsets().right + 55,
        //            new Double(phaseSpacePanel.getPreferredSize().getHeight() + jPanel4.getInsets().top +
        // jPanel4.getInsets().bottom + jPanel4.getPreferredSize().getHeight() + statusPanel.getInsets().top +
        // statusPanel.getInsets().bottom + statusPanel.getPreferredSize().getHeight()).intValue() + getInsets().bottom + 55);
    }


    class SliderState implements ChangeListener{

	private	HashMap heightTable_;
	private	HashMap widthTable_;

	public SliderState(){
	    heightTable_= new HashMap(20);
	    widthTable_= new HashMap(20);
	}

	private void createSizeScale(int min,int max){

	    int h = RPnPhaseSpaceFrame.this.frameSize_.height;
	    int w =RPnPhaseSpaceFrame.this.frameSize_.width;
	    int i;
	    float delta=0;
	    for (i=-5;i<=5;i++){
		delta=(w*i*5)/100;
		heightTable_.put(new Integer(i),new Integer((int)(h+delta)));
		widthTable_.put(new Integer(i),new Integer((int)(w+delta)));
	    }

	}

	public void stateChanged(ChangeEvent e) {

	    int value=0;
	    Integer h=new Integer(value);
	    Integer w=new Integer(value);
	    if ((heightTable_.isEmpty()))
		createSizeScale(-5,5);
	    JSlider source = (JSlider)(e.getSource());
		if (!source.getValueIsAdjusting()) {
		    value = source.getValue();
		    h= (Integer)heightTable_.get(new Integer(value));
		    w= (Integer)widthTable_.get(new Integer(value));
		    RPnPhaseSpaceFrame.this.setSize(w.intValue(),h.intValue());
		    RPnPhaseSpaceFrame.this.validate();
		}

	}

    }


    //Overridden so we can exit when window is closed
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
	    commandMenu_.finalizeApplication();
        }
    }

    protected void processComponentEvent(ComponentEvent e){

	if (e.getID()==ComponentEvent.COMPONENT_SHOWN ){

	    if (frameSize_==null)
		frameSize_=getSize();
	}

    }

    private void createLabels (int min,int max){

	int i;
	for (i=min;i<=max;i++){

	    if (i == min)
		labels_.put(new Integer(i),new JLabel("Min"));
	    if (i==max)
		labels_.put(new Integer(i),new JLabel("Max"));
	}

    }


    public RPnPhaseSpacePanel phaseSpacePanel() { return phaseSpacePanel; }
}
