/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.io.UnsupportedEncodingException;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import wave.multid.view.*;
import wave.multid.*;
import wave.util.*;
import rpn.controller.PhaseSpacePanel2DController;
import rpn.controller.PhaseSpacePanelController;
import rpn.controller.PhaseSpacePanel3DController;
import java.awt.*;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import rpn.command.GenericExtensionCurveCommand;
import rpn.command.RpCommand;
import rpn.component.util.AreaSelected;
import rpn.component.util.LinePlotted;
import rpn.component.util.GeometryGraph;
import rpn.component.util.GeometryGraphND;
import rpn.component.util.GraphicsUtil;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.CLASSIFIERAGENT_CONFIG;
import rpn.controller.ui.UIController;
import rpn.controller.ui.VELOCITYAGENT_CONFIG;
import rpn.message.RPnNetworkStatus;
import wave.multid.DimMismatchEx;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.model.MultiPolygon;

public class RPnPhaseSpacePanel extends JPanel implements Printable {
    //
    // Constants
    //

    //*** alterei aqui  (Leandro)
    static public Color DEFAULT_BOUNDARY_COLOR = Color.gray;
    static public Color DEFAULT_BACKGROUND_COLOR = Color.black;
    static public Color DEFAULT_POINTMARK_COLOR = Color.red;
    static public Color DEFAULT_LASTINPUT_CURSOR_COLOR = Color.yellow;
    static public Color DEFAULT_LASTINPUT_HIGHLIGHT_CURSOR_COLOR = Color.white;

    static float[] DEFAULT_LASTINPUT_CURSOR_DASH = { 5F, 5 };  

    static Stroke DEFAULT_LASTINPUT_CURSOR_STROKE = new BasicStroke( 1.1F, BasicStroke.CAP_SQUARE,  
		BasicStroke.JOIN_MITER, 3F, DEFAULT_LASTINPUT_CURSOR_DASH, 0F );  

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
    // a flag to say the current location is close enough...
    private boolean showLastInputCursorPosHighlight_ = false;
    private boolean showLastInputCursorPos_ = true;
    private static boolean showCursorLine_ = true;

    public List<MultiGeometryImpl> getConvexSelection() {
        return testeList_;
    }


    protected List<GraphicsUtil> graphicsUtilList_;
    protected List<MultiGeometryImpl> testeList_;//TODO Will replace List<GraphicsUtil> graphicsUtilList_
    private Scene scene_;
    private Point cursorPos_;
    private Point trackedPoint_;
//    private JPEGImageEncoder encoder_;
    private boolean printFlag_ = false;
    private PhaseSpacePanelController ui_;
    private boolean blinkLastInputCursorPos_ = false;
    private static boolean cursorLine_;
    private boolean physicalBoundarySelected_;

    //
    // Constructors
    //
    public RPnPhaseSpacePanel(Scene scene) {

        scene_ = scene;
        testeList_ = new ArrayList<MultiGeometryImpl>();

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

        cursorPos_ = new Point(0, 0);

        setBackground(DEFAULT_BOUNDARY_COLOR);
        setPreferredSize(new java.awt.Dimension(myW, myH));
        graphicsUtilList_ = new ArrayList();
        this.setName("");

    }

    //
    // Accessors/Mutators
    //
    public void setLastInputCursorPosHightlight(boolean bool) {

	showLastInputCursorPosHighlight_ = bool;

    }

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

    public boolean isShowLastInputCursorPos() {
        return showLastInputCursorPos_;
    }

    public boolean isBlinkLastInputCursorPos() {
        return blinkLastInputCursorPos_;
    }

    
    public void setShowLastInputCursorPosHighlight(boolean showCursor) {
        showLastInputCursorPosHighlight_ = showCursor;
    }

    public void setBlinkLastInputCursorPos(boolean blinkCursor) {
        blinkLastInputCursorPos_ = blinkCursor;
    }

    public void setTrackedPoint(Point trackedPoint) {

        this.trackedPoint_ = trackedPoint;
    }

    public void addGraphicUtil(GraphicsUtil gu) {


        graphicsUtilList_.add(gu);


    }

    public boolean isPhysicalBoundarySelected() {
        return physicalBoundarySelected_;
    }

    public void setPhysicalBoundarySelected(boolean physicalBoundarySelected) {
        physicalBoundarySelected_ = physicalBoundarySelected;
    }

    public void clearGraphicsList() {
        graphicsUtilList_.clear();
    }

    public Polygon getPhysicalBoundaryPolygon() {

        Polygon dcView = scene_.getViewingTransform().viewPlane().getWindow().dcView(scene_.getViewingTransform());
//            PathIterator pathIterator = dcView.getPathIterator(null);

//            while (!pathIterator.isDone()) {
//                double[] segmentArray = new double[2];
//                int segment = pathIterator.currentSegment(segmentArray);
//
//                for (int i = 0; i < segmentArray.length; i++) {
//                    double d = segmentArray[i];
//
//                    System.out.println(d);
//
//                }
//
//                System.out.println("-------------------------");
//                pathIterator.next();
//
//            }

        return dcView;


    }

    public void clearAreaSelection() {
        ArrayList<GraphicsUtil> toRemove = new ArrayList();

        for (int i = 0; i < graphicsUtilList_.size(); i++) {
            GraphicsUtil graphUtil = graphicsUtilList_.get(i);


            if (graphUtil instanceof AreaSelected) {
                toRemove.add(graphUtil);
            }

        }
        testeList_.clear();
        graphicsUtilList_.removeAll(toRemove);
        setPhysicalBoundarySelected(false);

    }

    public void setLastGraphicsUtil(GraphicsUtil lastGraphicsUtil) {


        if (graphicsUtilList_.isEmpty()) {
            graphicsUtilList_.add(lastGraphicsUtil);
        } else {
            graphicsUtilList_.set(graphicsUtilList_.size() - 1, lastGraphicsUtil);
        }
    }

    // -----
    public List<GraphicsUtil> getGraphicsUtil() {
        return graphicsUtilList_;
    }

    public void clearAllStrings() {
        GeometryGraphND.clearpMarca();
        ArrayList<GraphicsUtil> toRemove = new ArrayList();

        for (int i = 0; i < graphicsUtilList_.size(); i++) {
            GraphicsUtil graphUtil = graphicsUtilList_.get(i);

            if (graphUtil instanceof LinePlotted) {
                toRemove.add(graphUtil);
            }

        }
        graphicsUtilList_.removeAll(toRemove);
        getCastedUI().getTypeString().clear();
        getCastedUI().getVelocityString().clear();

    }

    public void clearLastString() {
        GeometryGraphND.clearpMarca();
        ArrayList<GraphicsUtil> toRemove = new ArrayList();
        for (int i = 0; i < graphicsUtilList_.size(); i++) {
            GraphicsUtil graphUtil = graphicsUtilList_.get(i);

            if (graphUtil instanceof LinePlotted) {
                toRemove.add(graphUtil);
            }

        }
        int index = toRemove.size() - 1;
        if (index >= 0) {
            graphicsUtilList_.remove(index);
            getCastedUI().getTypeString().remove(index);
            getCastedUI().getVelocityString().remove(index);
        }
    }

    public void clearVelocities() {
        GeometryGraphND.clearpMarca();
        ArrayList<GraphicsUtil> toRemove = new ArrayList();
        ArrayList<String> velRemove = new ArrayList();
        ArrayList<String> strRemove = new ArrayList();
        for (int i = 0; i < graphicsUtilList_.size(); i++) {
            GraphicsUtil graphUtil = graphicsUtilList_.get(i);

            if (graphUtil instanceof LinePlotted && !(getCastedUI().getVelocityString().get(i).equals(""))) {
                toRemove.add(graphUtil);
                velRemove.add(getCastedUI().getVelocityString().get(i));
                strRemove.add(getCastedUI().getTypeString().get(i));
            }

        }

        graphicsUtilList_.removeAll(toRemove);
        getCastedUI().getVelocityString().removeAll(velRemove);
        getCastedUI().getTypeString().removeAll(strRemove);

    }

    public void clearClassifiers() {

        GeometryGraphND.clearpMarca();
        ArrayList<GraphicsUtil> toRemove = new ArrayList();
        ArrayList<String> velRemove = new ArrayList();
        ArrayList<String> strRemove = new ArrayList();
        for (int i = 0; i < graphicsUtilList_.size(); i++) {
            GraphicsUtil graphUtil = graphicsUtilList_.get(i);

            if (graphUtil instanceof LinePlotted && !(getCastedUI().getTypeString().get(i).equals(""))) {
                toRemove.add(graphUtil);
                velRemove.add(getCastedUI().getVelocityString().get(i));
                strRemove.add(getCastedUI().getTypeString().get(i));
            }

        }

        graphicsUtilList_.removeAll(toRemove);
        getCastedUI().getVelocityString().removeAll(velRemove);
        getCastedUI().getTypeString().removeAll(strRemove);

    }
    // -----

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

        Font font = new Font("Verdana", Font.PLAIN, 13);
        g.setFont(font);
        FontMetrics metrics = new FontMetrics(font) {
        };

        /*
         * BOUNDARY WINDOW
         */

        g.setColor(DEFAULT_BACKGROUND_COLOR);
        Shape s = scene_.getViewingTransform().viewPlane().getWindow().dcView(scene_.getViewingTransform());
        ((Graphics2D) g).fill(s);

/* EDSON, TODO ??
        if (isPhysicalBoundarySelected()) {
            Color selectedColor = new Color(255, 0, 0, 60);
            g.setColor(selectedColor);
            g.fillPolygon(getPhysicalBoundaryPolygon());
        }

*/
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

        int i = 0;
        for (GraphicsUtil graphicUtil : graphicsUtilList_) {

            // ------------
//            if (graphicUtil instanceof LinePlotted) {
////                String typeStr = getCastedUI().getTypeString().get(i);
////                String velStr = getCastedUI().getVelocityString().get(i);
//                i++;
//                Line2D.Double line = (Line2D.Double) graphicUtil.createShape();
//                Rectangle2D boundsType = metrics.getStringBounds(typeStr, null);
//                Rectangle2D boundsVel = metrics.getStringBounds(velStr, null);
//                int typeWidth = (int) boundsType.getWidth();
//                int velWidth = (int) boundsVel.getWidth();
//
//                if (line.x1 < line.x2) {
//                    g.drawString(typeStr, (int) (line.x2 + 5), (int) (line.y2 + 5));
//                    g.drawString(velStr, (int) (line.x2 + 5), (int) (line.y2 + 5));
//                } else {
//                    g.drawString(typeStr, (int) (line.x2 - (typeWidth + 2)), (int) (line.y2 + 5));
//                    g.drawString(velStr, (int) (line.x2 - (velWidth + 2)), (int) (line.y2 + 5));
//                }
//
//            }
            // ------------

            graphicUtil.draw((Graphics2D) g);

        }

        g.setColor(DEFAULT_POINTMARK_COLOR);

        /*
         * Tracked Point
         */
        if (trackedPoint_ != null) {
            g.fillRect(trackedPoint_.x, trackedPoint_.y, 5, 5);
        }

        //** Leandro: início.

        myH_ = getHeight();
        myW_ = getWidth();


        GeometryGraph geom = new GeometryGraph();
        geom.markPoints(scene());
        geom.paintComponent(g, scene(), this);


        if (UIController.instance().getState() instanceof AREASELECTION_CONFIG) {        // acrescentei isso (Leandro)
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

	    // returns to default values	
            g.setColor(prev);
            ((Graphics2D) g).setStroke(stroke);
        }

	// this is a design flaw due to the addition of several PhaseSpaces
	// UIController was supposed to be a singleton contrlling the only instance of a PhaseSpace originally.
	RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction)scene_.getAbstractGeom();
	String phaseSpaceName = phaseSpace.getName();

	if ((showLastInputCursorPos_) && (scene().getViewingTransform() instanceof Viewing2DTransform) 
		&& !phaseSpaceName.startsWith("Left") && !phaseSpaceName.startsWith("Right")) {

            ((Graphics2D) g).setStroke(DEFAULT_LASTINPUT_CURSOR_STROKE);

	    if (showLastInputCursorPosHighlight_)
		g.setColor(DEFAULT_LASTINPUT_HIGHLIGHT_CURSOR_COLOR);
	    else
		g.setColor(DEFAULT_LASTINPUT_CURSOR_COLOR);

	    Coords2D dcCoords = new Coords2D();
	    RealVector lastValues = UIController.instance().globalInputTable().lastValues();
            CoordsArray wcCoords = new Coords2D(lastValues.toDouble());
            scene().getViewingTransform().viewPlaneTransform(wcCoords, dcCoords);

            int xCursor = new Double(dcCoords.getX()).intValue();
            int yCursor = new Double(dcCoords.getY()).intValue();
            g.drawLine(xCursor, 0, xCursor, getHeight());
            g.drawLine(0, yCursor, getWidth(), yCursor);

	    // returns to default values	
            g.setColor(prev);
            ((Graphics2D) g).setStroke(stroke);
	}

        for (MultiGeometryImpl multiPolyLine : testeList_) {
            try {

                GeomObjView createView = multiPolyLine.createView(scene_.getViewingTransform());
                createView.draw((Graphics2D) g);

            } catch (DimMismatchEx ex) {
                Logger.getLogger(RPnPhaseSpacePanel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


        /*
         * SCENE
         */


        if (scene_ != null) {
            scene_.draw((Graphics2D) g);
        }




    }

    public void updateGraphicsUtil() {

        for (GraphicsUtil graphicsUtil : graphicsUtilList_) {

            graphicsUtil.update(scene().getViewingTransform());

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
                if (geometry.getViewingAttr().isVisible()) {
                    geometry.draw(svgGenerator);
                }


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

    public List<AreaSelected> getSelectedAreas() {
        List<AreaSelected> selectedAreasList = new ArrayList();
        for (GraphicsUtil area : graphicsUtilList_) {

            if (area instanceof AreaSelected) {
                selectedAreasList.add((AreaSelected) area);
            }
        }
        return selectedAreasList;

    }

    public void removeGraphicsUtil(List<? extends GraphicsUtil> listToRemove) {
        graphicsUtilList_.removeAll(listToRemove);
    }

    public void removeGraphicsUtil(GraphicsUtil g) {
        graphicsUtilList_.remove(g);
    }

    public List<AreaSelected> interceptedAreas(GeomObjView geomView) {

        Iterator<AreaSelected> areaIterator = getSelectedAreas().iterator();

        ArrayList<AreaSelected> intersectedAreas = new ArrayList<AreaSelected>();


        while (areaIterator.hasNext()) {
            AreaSelected area = areaIterator.next();

            if (geomView.intersect((Polygon) area.getShape())) {
                intersectedAreas.add(area);
            }

        }

        return intersectedAreas;


    }

    public void addGenericSelection(MultiGeometryImpl multiPolyLine) {

        RpCommand command = new RpCommand(((MultiPolygon) multiPolyLine).toXML());

        GenericExtensionCurveCommand.instance().logCommand(command);


        if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
            RPnNetworkStatus.instance().sendCommand(rpn.controller.ui.UndoActionController.instance().getLastCommand().toXML());
        }




        testeList_.add(multiPolyLine);
    }

    public void setLastGenericSelection(MultiGeometryImpl lastGraphicsUtil) {

        if (testeList_.isEmpty()) {
            testeList_.add(lastGraphicsUtil);
        } else {
            testeList_.set(testeList_.size() - 1, lastGraphicsUtil);
        }
    }
}
