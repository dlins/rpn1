/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.util;

import javax.vecmath.GVector;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class RealVector extends GVector {

    static public int XML_FORMAT_PRECISION_USED = 4;


    public RealVector(int size) {
        super(size);
    }

    public RealVector(RealVector copy) {
        super(copy);
    }

    public RealVector(double[] v) {
        super(v);
    }

    public RealVector(String data) {
        super(fromString(data));
    }

    //
    // Methods
    //
    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder(); 

        for (int i = 0; i < this.getSize(); i++) {

            //buffer.append(String.format("%3.6E",getElement(i)));
            buffer.append(String.format("%.4f",getElement(i)));
            //buffer.append(getElement(i));
            buffer.append(" ");

        }
        return buffer.toString().trim();
    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("<REALVECTOR dimension=\"" + getSize() + "\"" + " coords=\"" + toString() + "\"/>" + '\n');
        return buffer.toString();
    }

    private static double[] fromString(String data) {

    	DecimalFormat parseFormatter = new DecimalFormat();
	Locale usLocale = new Locale("en", "US");

	/*try {

		parseFormatter.applyPattern("0.######E0#");

	} catch (IllegalArgumentException ex) {

		System.out.println(ex);

	}*/

        String[] components = data.split(" ");
        double[] doubleList = new double[components.length];

        for (int j = 0; j < components.length; j++) 
	
          try {		
		
		//Number element = NumberFormat.getInstance(usLocale).parse(components[j]);
		
		//Number element = parseFormatter.parse(components[j],new ParsePosition(0));
            	//doubleList[j] = element.doubleValue();
                doubleList[j] = new Double(components[j]);


          }
          catch(NumberFormatException ex) {
          //catch (java.text.ParseException ex) {
               	System.out.println("Error parsing the Double string" + "\n" + ex);
          }

	//}

        return doubleList;
    }

    public void sort() {
        for (int i = getSize(); --i >= 0;) {
            for (int j = 0; j < i; j++) {
                if (getElement(j) > getElement(j + 1)) {
                    double swap = getElement(j);
                    setElement(j, getElement(j + 1));
                    setElement(j + 1, swap);
                }
            }
        }
    }

    public boolean equals(RealVector test) {

        if (test.getSize() != this.getSize()) {
            return false;
        }

        for (int i = 0; i < test.getSize(); i++) {

            if (this.getElement(i) != test.getElement(i)) {

                return false;
            }
        }

        return true;


    }

    static public void sortEigenData(int n, double[] eigenValR, double[] eigenValI, RealVector[] eigenVec) {
        int i = 0;
        int d1, d2, j;
        boolean bool = true;
        RealVector[] sortVec = new RealVector[4];
        for (i = 0; i < 4; i++) {
            sortVec[i] = new RealVector(n);
        }
        double[] sortSpaceR = {0, 0, 0, 0};
        double[] sortSpaceI = {0, 0, 0, 0};
        while (bool) {
            bool = false;
            i = 0;
            if (eigenValI[i] != 0) {
                d1 = 2;
            } else {
                d1 = 1;
            }
            while (i + d1 < n) {
                if (eigenValI[i + d1] != 0) {
                    d2 = 2;
                } else {
                    d2 = 1;
                }
                if (eigenValR[i] > eigenValR[i + d1]) {
                    bool = true;
                    for (j = 0; j < d2; j++) {
                        sortSpaceR[j] = eigenValR[i + d1 + j];
                        sortSpaceI[j] = eigenValI[i + d1 + j];
                        sortVec[j] = eigenVec[i + d1 + j];
                    }
                    for (j = 0; j < d1; j++) {
                        sortSpaceR[d2 + j] = eigenValR[i + j];
                        sortSpaceI[d2 + j] = eigenValI[i + j];
                        sortVec[d2 + j] = eigenVec[i + j];
                    }
                    for (j = 0; j < d1 + d2; j++) {
                        eigenValR[i + j] = sortSpaceR[j];
                        eigenValI[i + j] = sortSpaceI[j];
                        eigenVec[i + j] = sortVec[j];
                    }
                    i = i + d2;
                } else {
                    i = i + d1;
                    d1 = d2;
                }
            }
        }
    }

    public double[] toDouble() {

        int i, lenData;

        double data[];

        lenData = getSize();

        data = new double[lenData];

        for (i = 0; i < lenData; i++) {

            data[i] = getElement(i);
        }

        return data;

    }

    public float[] toFloat() {

        int i, lenData;

        float[] data;
        double[] doubleData;

        Double tempData;

        doubleData = toDouble();

        lenData = getSize();

        data = new float[lenData];

        for (i = 0; i < lenData; i++) {

            tempData = new Double(doubleData[i]);
            data[i] = tempData.floatValue();

        }


        return data;

    }


    public double distance(RealVector input) {
        RealVector p1 = new RealVector(this);
        p1.sub(input);
        return (p1.norm());
    }


}
