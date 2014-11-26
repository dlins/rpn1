/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rpnumerics;

/**
 *
 * @author edsonlan
 */
public interface RpDiagramCalc {
    
    
    public RpSolution createDiagramSource() throws RpException;

    public RpSolution updateDiagramSource() throws RpException;
    
}
