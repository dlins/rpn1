/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.glasspane;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import javax.swing.JComponent;
import rpn.message.*;
import wave.util.RealVector;

// this is in order to make PathIterator serializable
import salvo.jesus.graph.java.awt.geom.*;


public class RPnGlassPane extends JComponent {

   
    GeneralPath path_;
  
    


    public RPnGlassPane() {

       
        path_ = new GeneralPath(GeneralPath.WIND_EVEN_ODD);



        setDoubleBuffered(false);

        addMouseListener(new MouseAdapter(){

                public void mousePressed(MouseEvent e){

                    path_.moveTo(new Double(e.getPoint().getX()),new Double(e.getPoint().getY()));                                      
                        
                }

                public void mouseReleased(MouseEvent event) {

                  

                    // TODO add coords
                    if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
                        RPnNetworkStatus.instance().sendCommand(new SerializablePathIterator(path_.getPathIterator(new AffineTransform())));
                    }

                    
                    

                }

        });

        addMouseMotionListener(new MouseMotionAdapter(){
                public void mouseDragged(MouseEvent e){

                    path_.lineTo(new Double(e.getPoint().getX()), new Double(e.getPoint().getY()));
                    repaint();
                    path_.moveTo(new Double(e.getPoint().getX()), new Double(e.getPoint().getY()));
                }
        });
    }

    //
    // Accessors/Mutators
    //
    public GeneralPath path() {return path_;}
    public void updatePath(PathIterator it) {

        // a done it means a CLEAR for now...
        if (it.isDone())
            clear();
        else {


            path_.reset();
            path_.append(it, false);
            invalidate();
            repaint();

        }
    }


    //
    // Methods
    //
    public void updatePath(int drawMode,RealVector refPoint) {

        if (drawMode == PathIterator.SEG_LINETO) {

            path_.lineTo(refPoint.getElement(0),refPoint.getElement(1));
            repaint();
            path_.moveTo(refPoint.getElement(0),refPoint.getElement(1));
        }

        if (drawMode == PathIterator.SEG_MOVETO)
            path_.moveTo(refPoint.getElement(0),refPoint.getElement(1));
    }

    @Override
    protected void paintComponent(Graphics g) {
               
        g.setColor(Color.red);
        ((Graphics2D)g).draw(path_);
    }

    public void clear() {
        
        path_.reset();
        invalidate();
        repaint();

   
    }


}