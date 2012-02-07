/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.io.UnsupportedEncodingException;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import wave.multid.view.*;
import rpn.controller.PhaseSpacePanel2DController;
import rpn.controller.PhaseSpacePanelController;
import rpn.controller.PhaseSpacePanel3DController;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;

import javax.swing.JPanel;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import rpn.component.util.AREASELECTION_CONFIG2;
import rpn.component.util.CLASSIFIERAGENT_CONFIG;
import rpn.component.util.ControlClick;
import rpn.component.util.GeometryGraph;
import rpn.component.util.GeometryGraph3D;
import rpn.component.util.GeometryGraphND;
import rpn.component.util.GeometryUtil;
import rpn.component.util.VELOCITYAGENT_CONFIG;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpnumerics.BifurcationProfile;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;

public class RPnPhaseSpacePanel extends JPanel implements Printable {
    //
    // Constants
    //

    //*** alterei aqui  (Leandro)
    static public Color DEFAULT_BOUNDARY_COLOR = Color.gray;
    static public Color DEFAULT_BACKGROUND_COLOR = Color.black;
    static public Color DEFAULT_POINTMARK_COLOR = Color.white;
    //***

    public static List<Area> listaArea = new ArrayList<Area>();     //** declarei isso    (Leandro) - ainda nao esta sendo usado

    public static int myH_;                                          //** declarei isso    (Leandro)
    public static int myW_;                                          //** declarei isso    (Leandro)

    //*** declarei esses métodos (Leandro)
    public static void blackBackground() {
        DEFAULT_BACKGROUND_COLOR = Color.black;
        DEFAULT_POINTMARK_COLOR = Color.white;
    }

    public static void whiteBackground() {
        DEFAULT_BACKGROUND_COLOR = Color.white;
        DEFAULT_POINTMARK_COLOR = Color.black;
    }
    //***

    public static boolean isCursorLine() {
        return cursorLine_;
    }
    //
    // Members
    //
    private Scene scene_;
    private Point cursorPos_;
    private Point trackedPoint_;
//    private JPEGImageEncoder encoder_;
    private boolean printFlag_ = false;
    private PhaseSpacePanelController ui_;
    private static boolean showCursorLine_;
    private static boolean cursorLine_;

    //
    // Constructors
    //
    public RPnPhaseSpacePanel(Scene scene) {
        scene_ = scene;

        if (scene_.getViewingTransform() instanceof Viewing3DTransform) {
            ui_ = new PhaseSpacePanel3DController(scene_.getViewingTransform().
                    projectionMap().
                    getCompIndexes()[0],
                    scene_.getViewingTransform().
                    projectionMap().
                    getCompIndexes()[1],
                    scene_.getViewingTransform().
                    projectionMap().
                    getCompIndexes()[2]);
        } else {
            ui_ = new PhaseSpacePanel2DController(scene_.getViewingTransform().
                    projectionMap().
                    getCompIndexes()[0],
                    scene_.getViewingTransform().
                    projectionMap().
                    getCompIndexes()[1]);
        }
        ui_.install(this);
        // calculates viewing window dimensions
        int myW = new Double(scene().getViewingTransform().viewPlane().
                getViewport().getWidth()).intValue();
        int myH = new Double(scene().getViewingTransform().viewPlane().
                getViewport().getHeight()).intValue();

        //myW = 3*myW/4;             // Basta isso para redefinir os tamanho dos painéis
        //myH = 3*myH/4;

        //myW = 2*myW;
        //myH = 2*myH;

        cursorPos_ = new Point(0, 0);
        setBackground(DEFAULT_BOUNDARY_COLOR);
        setPreferredSize(new java.awt.Dimension(myW, myH));
    }

    //
    // Accessors/Mutators
    //
    public PhaseSpacePanelController getCastedUI() {
        return ui_;
    }

    public Scene scene() {
        return scene_;
    }

    // cursor orientation
    public void setCursorPos(Point pos) {
        cursorPos_ = pos;
    }

    public Point getCursorPos() {
        return cursorPos_;
    }

    public static boolean isShowCursor() {
        return showCursorLine_;
    }

    public static void setCursorLineVisible(boolean aSetCursorLine_) {
        cursorLine_ = aSetCursorLine_;
    }

    public void setTrackedPoint(Point trackedPoint) {

        this.trackedPoint_ = trackedPoint;
    }

    //
    // Methods
    //
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Stroke stroke = ((Graphics2D) g).getStroke();
        BasicStroke newStroke = new BasicStroke(1.1f);
        ((Graphics2D) g).setStroke(newStroke);
        Color prev = g.getColor();
        Graphics2D gra = (Graphics2D) g;

        /*
         * BOUNDARY WINDOW
         */

        g.setColor(DEFAULT_BACKGROUND_COLOR);
        Shape s = scene_.getViewingTransform().viewPlane().getWindow().dcView(scene_.getViewingTransform());
        ((Graphics2D) g).fill(s);


        /*
         * SCENE
         */

        if (scene_ != null) {
            scene_.draw((Graphics2D) g);
        }

        /*
         * POINT MARKS
         */

        g.setColor(DEFAULT_POINTMARK_COLOR);
        for (int i = 0; i < getCastedUI().pointMarkBuffer().size(); i++) {
            g.fillRect(((Point) getCastedUI().pointMarkBuffer().get(i)).x,
                    ((Point) getCastedUI().pointMarkBuffer().get(i)).y, 5, 5);
        }

        /*
         * SELECTED AREAS
         */

        g.setColor(DEFAULT_POINTMARK_COLOR);

        for (rpnumerics.Area selectedArea : BifurcationProfile.instance().getSelectedAreas()) {
        }

        /*
         * Tracked Point
         */
        if (trackedPoint_ != null) {
            g.fillRect(trackedPoint_.x, trackedPoint_.y, 5, 5);
        }


        for (Rectangle2D.Double rectangle : getCastedUI().getSelectionAreas()) {

            g.drawRect((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.width, (int) rectangle.height);

        }

        //** Leandro: início.

        myH_ = getHeight();
        myW_ = getWidth();


        if (RPNUMERICS.domainDim() == 2) {
            
            GeometryGraph geom = new GeometryGraph();
            geom.markPoints(scene());
            geom.paintComponent(g, scene());

        }

        if (RPNUMERICS.domainDim() == 3) {

            GeometryGraph3D geom3D = new GeometryGraph3D();
            geom3D.markPoints(scene());
            geom3D.paintComponent(g, scene());

        }

//        if (RPNUMERICS.domainDim() == 4) {
//            GeometryGraph4D.markPoints(GeometryUtil.targetPoint, GeometryUtil.pMarca, scene());
//            GeometryGraph4D.paintComponent(g, scene());
//        }

        if (UIController.instance().getState() instanceof AREASELECTION_CONFIG ||
                UIController.instance().getState() instanceof AREASELECTION_CONFIG2) {        // acrescentei isso (Leandro)
            getCastedUI().pointMarkBuffer().clear();
            showCursorLine_ = false;
            repaint();
            getCastedUI().resetCursorCoords();
        }

        if (UIController.instance().getState() instanceof CLASSIFIERAGENT_CONFIG) {      // acrescentei isso (Leandro)
            getCastedUI().pointMarkBuffer().clear();
            showCursorLine_ = false;
            repaint();
            getCastedUI().resetCursorCoords();
        }

        if (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG) {        // acrescentei isso (Leandro)
            getCastedUI().pointMarkBuffer().clear();
            showCursorLine_ = false;
            repaint();
            getCastedUI().resetCursorCoords();
        }


        //** Leandro: fim.
        //*****************************************

        /*
         * USER CURSOR ORIENTATION
         *
         * for printing and 3D projections we will not use cursor
         * orientation
         */
        if (showCursorLine_ && isCursorLine()) {

            if ((!printFlag_)
                    && (scene().getViewingTransform() instanceof Viewing2DTransform)) {
                g.setColor(Color.red);
                int xCursor = new Double(cursorPos_.getX()).intValue();
                int yCursor = new Double(cursorPos_.getY()).intValue();
                g.drawLine(xCursor, 0, xCursor, getHeight());
                g.drawLine(0, yCursor, getWidth(), yCursor);
            }
            g.setColor(prev);
            ((Graphics2D) g).setStroke(stroke);
        }


    }

    public BufferedImage createOffSetImageBuffer() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = env.getDefaultScreenDevice();
        GraphicsConfiguration[] configs = dev.getConfigurations();
        GraphicsConfiguration config = dev.getDefaultConfiguration();
        BufferedImage buffedImage = config.createCompatibleImage(getWidth(),
                getHeight());
        Graphics2D g = buffedImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth() + 15, getHeight() + 15);
        printFlag_ = true;
        paintComponent(g);
        printFlag_ = false;
        return buffedImage;
    }

//    public void createJPEGImageFile(String targetAbsPath) {
//        try {
//            // First save it as JPEG
//            FileOutputStream out = new FileOutputStream(targetAbsPath);
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//            encoder.encode(createOffSetImageBuffer());
//            out.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    public int print(Graphics g, PageFormat pf, int pageIndx) {
        /* TODO some operating systems (UNIX) offer the option
        of local postscript file printing. This should *not*
        be enabled assuming that the PrintJob behaviour is to
        stop the callback for print only when the return value
        is NO_SUCH_PAGE or the job is queued to a spooler

        we could possibly have a save to file option dialog instead.
         */

        if (pageIndx != 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) g;
        pf.setOrientation(PageFormat.LANDSCAPE);
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g2d.scale(pf.getImageableWidth()
                / scene().getViewingTransform().viewPlane().getViewport().
                getWidth(),
                pf.getImageableHeight()
                / scene().getViewingTransform().viewPlane().getViewport().
                getHeight());
        // toggle double buffering
        boolean buffered = isDoubleBuffered();
        setDoubleBuffered(false);
        printFlag_ = true;
        paintComponent(g);
        printFlag_ = false;
        if (buffered) {
            setDoubleBuffered(true);
        }
        return PAGE_EXISTS;
    }

    public static void setShowCursor(boolean showCursor) {
        showCursorLine_ = showCursor;

    }

    public void createSVG(File file) {
        try {

            // Get a DOMImplementation.
            DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

            // Create an instance of org.w3c.dom.Document.
            String svgNS = "http://www.w3.org/2000/svg";
            Document document = domImpl.createDocument(svgNS, "svg", null);
            // Create an instance of the SVG Generator.
            SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
            // Ask the test to render into the SVG Graphics2D implementation.
            boolean useCSS = true; // we want to use CSS style attributes


            //Draw boundary
            Shape s = scene_.getViewingTransform().viewPlane().getWindow().dcView(scene_.getViewingTransform());
            svgGenerator.fill(s);

            //Draw geometries
            Iterator it = scene_.geometries();
            while (it.hasNext()) {
                GeomObjView geometry = (GeomObjView) it.next();
                geometry.draw(svgGenerator);


            }
            // Finally, stream out SVG to the standard output using
            // UTF-8 encoding.
            svgGenerator.stream(file.getCanonicalPath(), useCSS);

        } catch (SVGGraphics2DIOException ex) {
            ex.printStackTrace();

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();


        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void eraseSelectedArea() {

        Graphics2D g = (Graphics2D) this.getGraphics();

        g.setColor(Color.BLACK);

        for (Rectangle2D.Double rectangle : getCastedUI().getSelectionAreas()) {
            g.fill(rectangle);
        }

    }
}
