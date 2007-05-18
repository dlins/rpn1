package wave.multid;

import wave.multid.model.*;
import wave.multid.*;
import wave.multid.view.*;
import java.awt.Color;
import wave.multid.map.*;
import wave.util.*;
import wave.util.graphs.*;
import java.awt.geom.*;
import wave.util.*;
import java.awt.*;
import javax.swing.*;


class    ScenePanel extends JPanel {

    private Scene scene;

    ScenePanel (Scene s){

	scene=s;
	setPreferredSize(new Dimension(400,400));
	setBackground(Color.gray);
	setVisible(true);
    }

    protected void paintComponent(Graphics g){
	super.paintComponent(g);
        scene.draw((Graphics2D) g);
    }
}

public class MultidExample{

    public static void main (String args []){

	double coords1 []={0.5,-0.5,0};  // each array define the polyline vertices
	double coords2 []={0.6,-0.3,0.2};
	double coords3 []={0.7,-0.2,0.3};
	double coords4 []={0.8,-0.1,0.4};
	double coords5 []={1.0,0.0,0.5};


	CoordsArray vertice1 = new CoordsArray (coords1); // creation of  CoordsArrays with double arrays
	CoordsArray vertice2 = new CoordsArray (coords2);
	CoordsArray vertice3 = new CoordsArray (coords3);
	CoordsArray vertice4 = new CoordsArray (coords4);
	CoordsArray vertice5 = new CoordsArray (coords5);

	CoordsArray vertices []={vertice1,vertice2,vertice3,vertice4,vertice5}; // vertices array will contain all  vertices

	ViewingAttr viewAttr = new ViewingAttr (Color.red,true); // definition of visual attributes (Red and visible)

	MultiPolyLine poly=  new MultiPolyLine (vertices,viewAttr); // making a polyline with the array of vertices and the attributes

	Space d2  = new Space ("Codomain",2);
	int  axis [] = {0,1}; // axis 1 and 2 of multidimensional object will be visible
	Space d3  = new Space ("Domain",3);

	ProjectionMap map= new ProjectionMap (d3,d2,axis); // construction of a  ProjectionMap


	RealVector2 vec []= {new RealVector2(-1,1),new RealVector2 (-1,-1),new RealVector2(1,-1),new RealVector2(1,1)};
	ViewPlane vPlane= new ViewPlane(new dcViewport(400,400),new wcWindow(vec,new java.awt.geom.Point2D.Double(-1,-1)));

	Viewing2DTransform transf = new Viewing2DTransform (map,vPlane);

	AbstractScene abscene = new AbstractScene("Scene",d3);
	abscene.join(poly);
	try {

	    JFrame frame = new JFrame();
	    frame.setBackground(Color.orange);
	    frame.getContentPane().setLayout(new BorderLayout());
	    frame.getContentPane().add(new ScenePanel(abscene.createScene(transf,viewAttr)),BorderLayout.CENTER);

	    frame.pack();
	    frame.setVisible(true);


	}


	catch (DimMismatchEx e){

	    System.out.println ("Error in transformation");
	}


    }

}











