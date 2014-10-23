/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util;

public class MultiGrid {
    //
    // Members
    //
    private GridProfile[] profiles_;
    private int dim_;

    //
    // Constructors
    //
    public MultiGrid(GridProfile[] profiles,int dim) {
        profiles_ = profiles;
        dim_=dim;
    }

    //
    // Accessors/Mutators
    //
    public GridProfile[] profiles() { return profiles_; }

    //
    // Methods
    //
    public GridIterator iterator() { return new GridIterator(this,dim_); }
}
