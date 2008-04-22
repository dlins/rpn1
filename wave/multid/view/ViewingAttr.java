/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.multid.view;

import java.awt.Color;

public class ViewingAttr {
    //
    // Constants
    //
    static public class DefaultAttr extends ViewingAttr {
        public DefaultAttr() {
            super(Color.black);
        }
    }


    //
    // Members
    //
    private Color color_;
    private boolean visible_;
    private boolean selected_;

    //
    // Constructors
    //
    public ViewingAttr(Color color) {
        color_ = new Color(color.getRed(), color.getGreen(), color.getBlue());
        visible_ = true;
        selected_ = false;
    }

    public ViewingAttr(Color color, boolean visible) {
        this(color);
        visible_ = visible;
    }

    public ViewingAttr(Color color, boolean visible, boolean selected) {
        this(color, visible);
        selected_ = selected;
    }

    public ViewingAttr(ViewingAttr attr) {
        this(attr.getColor(), attr.isVisible());
    }

    //
    // Methods
    //
    public Color getColor() { return color_; }

    public void setColor(Color color) { color_ = color; }

    public boolean isVisible() { return visible_; }

    public void setVisible(boolean flag) { visible_ = flag; }

    public boolean isSelected() { return selected_; }

    public void setSelected(boolean flag) { selected_ = flag; }
}
