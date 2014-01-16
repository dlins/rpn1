/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.*;
import rpn.component.util.AreaSelected;
import rpn.component.util.GraphicsUtil;
import rpn.configuration.Configuration;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.Boundary;
import wave.util.BoxND;
import wave.util.HyperOctree;
import wave.util.RealSegment;
import wave.util.RealVector;

public class OctreeTesteCommand extends BifurcationPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Octree Test";
    //
    // Members
    //
    static private OctreeTesteCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected OctreeTesteCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JButton());
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        
        Iterator<RPnPhaseSpacePanel> panelsIterator = UIController.instance().getInstalledPanelsIterator();
        
        while (panelsIterator.hasNext()) {
            RPnPhaseSpacePanel rPnPhaseSpacePanel = panelsIterator.next();
            
            List<GraphicsUtil> graphicsUtilList = rPnPhaseSpacePanel.getGraphicsUtil();
            
            if (!graphicsUtilList.isEmpty()) {
                
                List<AreaSelected> gu = rPnPhaseSpacePanel.getSelectedAreas();
                
                for (AreaSelected areaSelected : gu) {

                    //Quadtree test 
                    Boundary boundary = rpnumerics.RPNUMERICS.boundary();
                    
                    BoxND box = new BoxND(boundary.getMinimums(), boundary.getMaximums());
                    
                    HyperOctree<RealSegment> h1 = new HyperOctree(box, 3, 100);
                    RpGeometry lastGeometry = UIController.instance().getActivePhaseSpace().getLastGeometry();
                    
                    RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) lastGeometry.geomFactory();
                    
                    RPnCurve curve = (RPnCurve) factory.geomSource();
                    
                    
                    List<RealSegment> segments = curve.segments();
                    
                    System.out.println("Quantidade de segmentos da curva: "+segments.size());
                    
                    for (int i = 0; i < segments.size(); i++) {
                        RealSegment realSegment = segments.get(i);
                        
                        h1.add(realSegment);
                        
                    }
                    
                    
                    List<RealVector> wcVertices = areaSelected.getWCVertices();
                    
                    for (RealVector realVector : wcVertices) {
                        
                        System.out.println(realVector);
                        
                    }
                    
                    
                    BoxND boxTest = new BoxND(wcVertices.get(1), wcVertices.get(3));
                    
                    
                    System.out.println("Box: " + boxTest.pmin + " " + boxTest.pmax);
                    
                    Vector<RealSegment> realSegmentsInside = new Vector();
                    h1.within_box(boxTest, realSegmentsInside);
                    InflectionCurve inflection = new InflectionCurve(realSegmentsInside);
                    
                    ContourParams params = new ContourParams();
                    InflectionCurveCalc calc = new InflectionCurveCalc(params, 0);
                    InflectionCurveGeomFactory inflectionFactory = new InflectionCurveGeomFactory(calc, inflection);
                    for (RealSegment realSegment : realSegmentsInside) {
                        
                        System.out.println(realSegment);
                        
                    }
                    UIController.instance().getActivePhaseSpace().join(inflectionFactory.geom());
                    
                    
                    
                }
                
                
            }
            
            
            
            
            
        }
    }
    
    public RpGeometry createRpGeometry(RealVector[] input) {
        
        DoubleContactGeomFactory factory = new DoubleContactGeomFactory(RPNUMERICS.createDoubleContactCurveCalc());
        return factory.geom();
        
    }
    
    @Override
    public void execute() {
        
        
        DoubleContactGeomFactory factory = new DoubleContactGeomFactory(RPNUMERICS.createDoubleContactCurveCalc());
        execute(factory);
    }
    
    static public OctreeTesteCommand instance() {
        if (instance_ == null) {
            instance_ = new OctreeTesteCommand();
        }
        return instance_;
    }
}
