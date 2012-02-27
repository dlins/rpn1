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

    public void setIntType(int type_) {
        intType_ = type_;
    }

    public HugoniotSegment(RealVector leftPoint, double leftSigma, RealVector rightPoint, double rightSigma,
            HugoniotPointType type) {
        super(leftPoint, rightPoint);
        leftPoint_ = leftPoint;
        leftSigma_ = leftSigma;
        rightPoint_ = rightPoint;
        rightSigma_ = rightSigma;
        type_ = type;

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

    @Override
    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        PhasePoint leftPoint = new PhasePoint(leftPoint_);
        PhasePoint rightPoint = new PhasePoint(rightPoint_);

        StringBuilder leftLambda = new StringBuilder();

        for (int i = 0; i < leftLambdaArray_.length; i++) {
            double d = leftLambdaArray_[i];

            leftLambda.append(d);
            leftLambda.append(" ");

        }

        StringBuilder rightLambda = new StringBuilder();

        for (int i = 0; i < rightLambdaArray_.length; i++) {
            double d = rightLambdaArray_[i];
            rightLambda.append(d);
            rightLambda.append(" ");
        }

        buffer.append("<HUGONIOTSEGMENT ");

        buffer.append("leftsigma=\"" + leftSigma() + "\"" + " ");
        buffer.append("rightsigma=\"" + rightSigma() + "\"" + " ");
        buffer.append("leftlambda=\"" + leftLambda.toString().trim() + "\"" + " ");
        buffer.append("rightlambda=\"" + rightLambda.toString().trim() + "\"" + " ");
        buffer.append("type=\"" + intType_ + "\"");

        buffer.append(">\n");

        buffer.append(leftPoint.toXML());
        buffer.append(rightPoint.toXML());

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
