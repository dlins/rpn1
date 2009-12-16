/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpnumerics;


public class Area {

    private int xResolution_;
    private int yResolution_;

    private int xUpLeft_;
    private int yUpLeft_;

    private int xDownRight_;
    private int yDownRight_;

    public Area(int xResolution_, int yResolution_, int xUpLeft_, int yUpLeft_, int xDownRight_, int yDownRight_) {
        this.xResolution_ = xResolution_;
        this.yResolution_ = yResolution_;
        this.xUpLeft_ = xUpLeft_;
        this.yUpLeft_ = yUpLeft_;
        this.xDownRight_ = xDownRight_;
        this.yDownRight_ = yDownRight_;
    }

    public int getxDownRight() {
        return xDownRight_;
    }

    public void setxResolution(int xResolution_) {
        this.xResolution_ = xResolution_;
    }

    public void setyResolution(int yResolution_) {
        this.yResolution_ = yResolution_;
    }

    public int getxResolution() {
        return xResolution_;
    }

    public int getxUpLeft() {
        return xUpLeft_;
    }

    public int getyDownRight() {
        return yDownRight_;
    }

    public int getyResolution() {
        return yResolution_;
    }

    public int getyUpLeft() {
        return yUpLeft_;
    }








}
