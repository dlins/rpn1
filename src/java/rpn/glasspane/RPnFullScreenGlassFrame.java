/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.glasspane;

import javax.swing.JFrame;
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

import java.util.logging.*;
import javax.swing.JComponent;
import rpn.RPnPhaseSpaceFrame;
import rpn.message.*;
import salvo.jesus.graph.java.awt.geom.SerializablePathIterator;
import wave.multid.Coords2D;
import wave.multid.view.Scene;

/**
 *
 * @author mvera
 */
public class RPnFullScreenGlassFrame extends JComponent {

    
    private GeneralPath path_;
    private GeneralPath wpath_;    
    private Scene scene_;          
    
    
    public RPnFullScreenGlassFrame() {
        
        path_ = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        wpath_ = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        //scene_ = new Scene();


        //setDoubleBuffered(false);

        addMouseListener(new MouseAdapter(){

                public void mousePressed(MouseEvent e){
                    
                    path_.moveTo(new Double(e.getPoint().getX()),new Double(e.getPoint().getY()));

                    Coords2D dcPoint = new Coords2D(new Double(e.getPoint().getX()),new Double(e.getPoint().getY()));
                    Coords2D wcPoint = new Coords2D();
                    scene().getViewingTransform().dcInverseTransform(dcPoint,wcPoint);
                    wpath_.moveTo(wcPoint.getX(),wcPoint.getY());                                            
                }

                public void mouseReleased(MouseEvent event) {

                    PathIterator it = wpath_.getPathIterator(new AffineTransform());

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
                        scene().getViewingTransform().dcInverseTransform(dcPoint,wcPoint);
                        wpath_.lineTo(wcPoint.getX(),wcPoint.getY());

                        repaint();
                    
                        path_.moveTo(new Double(e.getPoint().getX()), new Double(e.getPoint().getY()));

                        dcPoint = new Coords2D(new Double(e.getPoint().getX()),new Double(e.getPoint().getY()));
                        wcPoint = new Coords2D();
                        scene().getViewingTransform().dcInverseTransform(dcPoint,wcPoint);
                        wpath_.moveTo(wcPoint.getX(),wcPoint.getY());
                    
                }
        });
    }

    //
    // Accessors/Mutators
    //
    public Scene scene() { return null;}
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

                int domainDim = scene().getViewingTransform().coordSysTransform().getDomain().getDim();
                double[] wc_coords = new double[domainDim];
                int drawMode = it.currentSegment(wc_coords);
                Coords2D wcPoint = new Coords2D(wc_coords[0],wc_coords[1]);
                Coords2D dcPoint = new Coords2D();

                scene().getViewingTransform().viewPlaneTransform(wcPoint, dcPoint);

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
