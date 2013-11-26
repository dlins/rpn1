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
import java.util.logging.*;
import rpn.RPnPhaseSpaceFrame;
import rpn.message.*;
import wave.util.RealVector;

// this is in order to make PathIterator serializable
import salvo.jesus.graph.java.awt.geom.*;
import wave.multid.Coords2D;


public class RPnGlassPane extends JComponent {

   
    private GeneralPath path_;
    private GeneralPath wpath_;
    private RPnPhaseSpaceFrame parentFrame_;
  
    


    public RPnGlassPane(RPnPhaseSpaceFrame parentFrame) {

       
        path_ = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        wpath_ = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        parentFrame_ = parentFrame;


        setDoubleBuffered(false);

        addMouseListener(new MouseAdapter(){

                public void mousePressed(MouseEvent e){

                    path_.moveTo(new Double(e.getPoint().getX()),new Double(e.getPoint().getY()));

                    Coords2D dcPoint = new Coords2D(new Double(e.getPoint().getX()),new Double(e.getPoint().getY()));
                    Coords2D wcPoint = new Coords2D();
                    parentFrame_.phaseSpacePanel().scene().getViewingTransform().dcInverseTransform(dcPoint,wcPoint);
                    wpath_.moveTo(wcPoint.getX(),wcPoint.getY());
                }

                public void mouseReleased(MouseEvent event) {

                    PathIterator it = wpath_.getPathIterator(new AffineTransform());

                    /*
                         // this is the famous WC to DC transform...
                    while (!it.isDone()) {

                        int domainDim = parentFrame_.phaseSpacePanel().scene().getViewingTransform().coordSysTransform().getDomain().getDim();
                        double[] wc_coords = new double[domainDim];
                        it.currentSegment(wc_coords);

                        for (int i=0;i < wc_coords.length;i++)
                            //Logger.getLogger(this.getClass()).debug("wc coords are : " + wc_coords[i]);
                            System.out.println("wc coords are : " + wc_coords[i]);



                        it.next();

                    }*/

                    // TODO add coords
                    if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {

                        SerializablePathIterator wPath = new SerializablePathIterator(wpath_.getPathIterator(new AffineTransform()));
                        RPnNetworkStatus.instance().sendCommand(wPath);                       

                    }

                    
                    

                }

        });

        addMouseMotionListener(new MouseMotionAdapter(){
                public void mouseDragged(MouseEvent e){

                    path_.lineTo(new Double(e.getPoint().getX()), new Double(e.getPoint().getY()));

                    Coords2D dcPoint = new Coords2D(new Double(e.getPoint().getX()),new Double(e.getPoint().getY()));
                    Coords2D wcPoint = new Coords2D();
                    parentFrame_.phaseSpacePanel().scene().getViewingTransform().dcInverseTransform(dcPoint,wcPoint);
                    wpath_.lineTo(wcPoint.getX(),wcPoint.getY());

                    repaint();
                    
                    path_.moveTo(new Double(e.getPoint().getX()), new Double(e.getPoint().getY()));

                    dcPoint = new Coords2D(new Double(e.getPoint().getX()),new Double(e.getPoint().getY()));
                    wcPoint = new Coords2D();
                    parentFrame_.phaseSpacePanel().scene().getViewingTransform().dcInverseTransform(dcPoint,wcPoint);
                    wpath_.moveTo(wcPoint.getX(),wcPoint.getY());

                }
        });
    }

    //
    // Accessors/Mutators
    //
    public GeneralPath path() {return path_;}
    public void updatePath(PathIterator it) {      
        
        // a done Iterator means CLEAR for now...
        if (it.isDone()) {

            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "a done Iterator means CLEAR...");
            clear();

        } else {


            path_.reset();


            // this is the famous WC to DC transform...
            while (!it.isDone()) {

                int domainDim = parentFrame_.phaseSpacePanel().scene().getViewingTransform().coordSysTransform().getDomain().getDim();
                double[] wc_coords = new double[domainDim];
                int drawMode = it.currentSegment(wc_coords);
                Coords2D wcPoint = new Coords2D(wc_coords[0],wc_coords[1]);
                Coords2D dcPoint = new Coords2D();

                parentFrame_.phaseSpacePanel().scene().getViewingTransform().viewPlaneTransform(wcPoint, dcPoint);

                if (drawMode == PathIterator.SEG_LINETO) {

                    path_.lineTo(dcPoint.getX(), dcPoint.getY());
                    repaint();
                }

                if (drawMode == PathIterator.SEG_MOVETO)
                    path_.moveTo(dcPoint.getX(), dcPoint.getY());

                it.next();

            }

            invalidate();
            repaint();

        }
    }


    //
    // Methods
    //
    @Override
    protected void paintComponent(Graphics g) {
               
        g.setColor(Color.red);
        ((Graphics2D)g).draw(path_);
    }

    public void clear() {
        
        path_.reset();
        wpath_.reset();
        invalidate();
        repaint();

   
    }
}