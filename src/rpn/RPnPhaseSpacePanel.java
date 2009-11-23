/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

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

import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import javax.swing.JPanel;
import java.io.FileOutputStream;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class RPnPhaseSpacePanel extends JPanel implements Printable {
    //
    // Constants
    //

    static final public Color DEFAULT_BACKGROUND_COLOR = Color.gray;
    static final public Color DEFAULT_BOUNDARY_COLOR = Color.black;
    static final public Color DEFAULT_POINTMARK_COLOR = Color.white;

    public static boolean isCursorLine() {
        return cursorLine_;
    }
    //
    // Members
    //
    private Scene scene_;
    private Point cursorPos_;
    private JPEGImageEncoder encoder_;
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
        cursorPos_ = new Point(0, 0);
        setBackground(DEFAULT_BACKGROUND_COLOR);
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

        g.setColor(DEFAULT_BOUNDARY_COLOR);
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

        for (Rectangle2D.Double rectangle : getCastedUI().getSelectionAreas()) {

            g.drawRect((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.width, (int) rectangle.height);

        }

        /*
         * USER CURSOR ORIENTATION
         *
         * for printing and 3D projections we will not use cursor
         * orientation
         */
        if (showCursorLine_ && isCursorLine()) {

            if ((!printFlag_) &&
                    (scene().getViewingTransform() instanceof Viewing2DTransform)) {
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

    public void createJPEGImageFile(String targetAbsPath) {
        try {
            // First save it as JPEG
            FileOutputStream out = new FileOutputStream(targetAbsPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(createOffSetImageBuffer());
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
        g2d.scale(pf.getImageableWidth() /
                scene().getViewingTransform().viewPlane().getViewport().
                getWidth(),
                pf.getImageableHeight() /
                scene().getViewingTransform().viewPlane().getViewport().
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

    public void eraseSelectedArea() {

        Graphics2D g = (Graphics2D) this.getGraphics();

        g.setColor(Color.BLACK);

        for (Rectangle2D.Double rectangle : getCastedUI().getSelectionAreas()) {
        g.fill(rectangle);
        }

    }
}