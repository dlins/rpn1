/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealSegment;
import wave.util.RealVector;

public class HugoniotSegment extends RealSegment {
    //
    // Constants
    //

    static public final int WHITE_SEG = 0;
    static public final int LIGHT_BLUE_SEG = 1;
    static public final int RED_SEG = 2;
    static public final int YELLOW_SEG = 3;
    static public final int PINK_SEG = 4;
    static public final int GREEN_SEG = 5;
    static public final int DARK_BLUE_SEG = 6;
    //
    // Members
    //
    private RealVector leftPoint_;
    private double leftSigma_;
    private RealVector rightPoint_;
    private double rightSigma_;
    private HugoniotPointType type_;
    private int intType_;
    private double[] rightLambdaArray_;
    private double[] leftLambdaArray_;

    //
    // Constructors
    //
    public HugoniotSegment(RealVector leftPoint, double leftSigma, RealVector rightPoint, double rightSigma, double leftLambda1, double leftLambda2, double rightLambda1, double rightLambda2,
            int type) {
        super(leftPoint, rightPoint);
        leftPoint_ = leftPoint;
        leftSigma_ = leftSigma;
        rightPoint_ = rightPoint;
        rightSigma_ = rightSigma;

        leftLambdaArray_ = new double[2];//TODO Hardcoded para fisica do Helmut

        leftLambdaArray_[0] = leftLambda1;
        leftLambdaArray_[1] = leftLambda2;



        rightLambdaArray_ = new double[2];//TODO Hardcoded para fisica do Helmut

        rightLambdaArray_[0] = rightLambda1;
        rightLambdaArray_[1] = rightLambda2;

        intType_ = type;
    }

    public HugoniotSegment(RealVector leftPoint, double leftSigma, RealVector rightPoint, double rightSigma,
            HugoniotPointType type) {
        super(leftPoint, rightPoint);
        leftPoint_ = leftPoint;
        leftSigma_ = leftSigma;
        rightPoint_ = rightPoint;
        rightSigma_ = rightSigma;
        type_ = type;

        /*        switch (type_.positiveRealPartNo()) {

        case 2 :
        switch (p2.type().positiveRealPartNo()) {

        case 2 :
        type_ = WHITE_SEG;
        break;
        case 1 :
        type_ = PINK_SEG;
        break;
        case 0 :
        type_ = WHITE_SEG;
        break;
        }
        break;
        case 1 :
        switch (p2.type().positiveRealPartNo()) {
        case 2 :
        type_ = RED_SEG;
        break;
        case 1 :
        type_ = GREEN_SEG;
        break;
        case 0 :
        type_ = LIGHT_BLUE_SEG;
        break;
        }
        break;
        case 0 :
        switch (p2.type().positiveRealPartNo()) {
        switch (p1.type().positiveRealPartNo()) {
        case 2 :
        type_ = YELLOW_SEG;
        break;
        case 1 :
        type_ = DARK_BLUE_SEG;
        break;
        case 0 :
        type_ = WHITE_SEG;
        break;
        }
        case 2 :
        switch (p2.type().positiveRealPartNo()) {

        case 2 :
        type_ = WHITE_SEG;
        break;
        case 1 :
        type_ = PINK_SEG;
        break;
        case 0 :
        type_ = WHITE_SEG;
        break;
        }
         */
    }

    public HugoniotSegment(RealVector leftPoint, double leftSigma, RealVector rightPoint, double rightSigma,
            int type) {

        super(leftPoint, rightPoint);
        leftPoint_ = leftPoint;
        leftSigma_ = leftSigma;
        rightPoint_ = rightPoint;
        rightSigma_ = rightSigma;


          leftLambdaArray_ = new double[2];//TODO Hardcoded para fisica do Helmut

        leftLambdaArray_[0] = 0;
        leftLambdaArray_[1] = 0;



        rightLambdaArray_ = new double[2];//TODO Hardcoded para fisica do Helmut

        rightLambdaArray_[0] = 0;
        rightLambdaArray_[1] = 0;

        intType_ = type;

    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<HUGONIOTSEGMENT leftpoint=\"" + leftPoint().toString() + "\"" + " ");

        buffer.append("rightpoint=\"" + rightPoint().toString() + "\"" + " ");
        buffer.append("leftsigma=\"" + leftSigma() + "\"" + " ");
        buffer.append("rightsigma=\"" + rightSigma() + "\"" + " ");
//      buffer.append("hugoniotpointtype=\""+type().toString()+"\"");
        buffer.append(">");
        buffer.append("</HUGONIOTSEGMENT>\n");

        return buffer.toString();


    }

    //
    // Accessors/Mutators
    //
    public RealVector leftPoint() {
        return leftPoint_;
    }

    public double leftSigma() {
        return leftSigma_;
    }

    public RealVector rightPoint() {
        return rightPoint_;
    }

    public double rightSigma() {
        return rightSigma_;
    }

//    public HugoniotPointType type() { return type_; }
    public int getType() {
        return intType_;
    }

    public double[] getLeftLambdaArray() {
        return leftLambdaArray_;
    }

    public double[] getRightLambdaArray() {
        return rightLambdaArray_;
    }
}
