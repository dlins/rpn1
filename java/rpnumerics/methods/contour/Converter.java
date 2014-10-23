package rpnumerics.methods.contour;

import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunctionDecorator;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class Converter {

	public Converter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static CubeFunction[] fromVectorialToCube(MDVectorFunction function){	
		
		int m = function.getResultComponentNumber();
		CubeFunction[] arrayfunctions = new CubeFunction[m];
		
		for (int pont_function = 0; pont_function < m; pont_function++) {
			arrayfunctions[pont_function] = new MDVectorFunctionDecorator(function, pont_function);
		}
		
		return arrayfunctions;
	}
	
	public static CubeFunction[] fromVectorialToCube(MDVectorFunction functionsGroup1, MDVectorFunction functionsGroup2){
		
		int m1 = functionsGroup1.getResultComponentNumber();
		int m2 = functionsGroup2.getResultComponentNumber();
		
		CubeFunction[] arrayfunctions = new CubeFunction[m1 + m2];
		
		for (int pont_function = 0; pont_function < m1; pont_function++) {
			arrayfunctions[pont_function] = new MDVectorFunctionDecorator(functionsGroup1, pont_function);
		}
		
		for (int pont_function = m1; pont_function < m2; pont_function++) {
			arrayfunctions[pont_function] = new MDVectorFunctionDecorator(functionsGroup2, pont_function);
		}
		
		return arrayfunctions;
	}
		
}

