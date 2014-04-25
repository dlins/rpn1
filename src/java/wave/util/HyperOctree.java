/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wave.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 *
 * @author edsonlan
 */
public class HyperOctree<T extends RealSegment> {

    protected int level_, maxlevel_, maxsize_;
    protected HyperOctree<T> father_;
    protected Vector<HyperOctree<T>> sons_;
    protected Set<T> data_;
    protected BoxND box_;

    public void subdivide_box(BoxND b, Vector<BoxND> vb) {
        vb.clear();

        int n = b.pmin.getSize();

        RealVector ppmin = new RealVector(n);
        RealVector ppmax = new RealVector(n);
        double mid = .5 * (b.pmin.getElement(0) + b.pmax.getElement(0));

        if (n == 1) {
            ppmin.setElement(0, b.pmin.getElement(0));
            ppmax.setElement(0, mid);
            vb.add(new BoxND(ppmin, ppmax));

            ppmin.setElement(0, mid);
            ppmax.setElement(0, b.pmax.getElement(0));
            vb.add(new BoxND(ppmin, ppmax));
        } else {
            RealVector tpmin = new RealVector(n - 1);
            RealVector tpmax = new RealVector(n - 1);
            for (int i = 1; i < n; i++) {
                tpmin.setElement(i - 1, b.pmin.getElement(i));
                tpmax.setElement(i - 1, b.pmax.getElement(i));
            }

            BoxND tb = new BoxND(tpmin, tpmax);
            Vector<BoxND> tvb = new Vector();
            subdivide_box(tb, tvb);

            for (int i = 0; i < tvb.size(); i++) {
                for (int j = 1; j < n; j++) {
                    ppmin.setElement(j, tvb.get(i).pmin.getElement(j - 1));
                    ppmax.setElement(j, tvb.get(i).pmax.getElement(j - 1));
                }

                ppmin.setElement(0, b.pmin.getElement(0));
                ppmax.setElement(0, mid);
                vb.add(new BoxND(ppmin, ppmax));

                ppmin.setElement(0, mid);
                ppmax.setElement(0, b.pmax.getElement(0));
                vb.add(new BoxND(ppmin, ppmax));
            }
        }


    }

// Recover the data stored on the current node's sons.
// Returns true if the list has up to maxsize_ elements,
// false otherwise. If true, the sons can be deleted and
// the current node can become a leaf.
//
// By returning a boolean, it is not necessary to recover
// all the data stored in the sons, thus shortening the process.
//
    public boolean recover_list(Set<T> list) {
        // If leaf
        if (sons_.isEmpty()) {
            if (data_.size() + list.size() > maxsize_) {
                return false;
            } else {
                Iterator<T> it = data_.iterator();
                while (it.hasNext()) {
                    T t = it.next();
                    list.add(t);

                }

                return true;
            }
        } // If node
        else {
            boolean valid = true;
            int i = 0;

            while (valid && i < sons_.size()) {
                valid = sons_.get(i).recover_list(list);
                i++;
            }

            return valid;
        }
    }

    public HyperOctree(BoxND b, int maxlevel, int maxsize) {

        box_ = b;

        maxlevel_ = maxlevel;
        maxsize_ = maxsize;
        sons_ = new Vector<HyperOctree<T>>();
        data_= new HashSet();
// level_=0;
// father_=0;
    }

    final void copy(HyperOctree<T> orig) {
        if (orig.sons_.isEmpty()) {

            Iterator<T> it = data_.iterator();
            while (it.hasNext()) {
                T t = it.next();
                data_.add(t);

            }

        } else {
            sons_.setSize(orig.sons_.size());
            for (int i = 0; i < orig.sons_.size(); i++) {
                HyperOctree<T> temp = new HyperOctree<T>(orig.sons_.get(i));
                temp.father_ = this;
                temp. level_ = level_ + 1;
                sons_.set(i, temp);
            }
        }
    }

    HyperOctree(HyperOctree<T> orig) {
        box_ = orig.box_;
        maxlevel_ = orig.maxlevel_;
        maxsize_ = orig.maxsize_;

        copy(orig);
    }

    public void add(T obj) {
        if (obj.intersect(box_)) {
            // If leaf
            if (sons_.isEmpty()) {
                data_.add(obj);

                // Subdivide if need be.
                //
                if (data_.size() > maxsize_ && level_ < maxlevel_) {
                    Vector<BoxND> vb = new Vector();
                    subdivide_box(box_, vb);

                    // Create the sons... 
                    for (int i = 0; i < vb.size(); i++) {
                        HyperOctree temp = new HyperOctree(vb.get(i), maxlevel_, maxsize_);
                        temp.level_ = level_ + 1;
                        temp.father_ = this;

                        // ...redistribute the data...
                        Iterator<T> it = data_.iterator();
                        while (it.hasNext()) {
                            T t = it.next();
//                            data_.add(t);
                            temp.add(t);

                        }


                        sons_.add(temp);
                    }

                    // ...and become a node.
                    data_.clear();
                }
            } // If node
            else {
                for (int i = 0; i < sons_.size(); i++) {
                    sons_.get(i).add(obj);
                }
            }
        }
    }

    void remove(T obj) {
        if (obj.intersect(box_)) {
            // If leaf
            if (sons_.isEmpty()) {
                data_.remove(obj);
            } // If node
            else {
                for (int i = 0; i < sons_.size(); i++) {
                    sons_.get(i).remove(obj);
                }

                if (recover_list(data_)) {

                    sons_.clear();
                } else {
                    data_.clear();
                }
            }
        }
    }

    public void clear() {
        data_.clear();

        sons_.clear();
    }

    public void within_box(BoxND b, Set<T> list) {
        if (father_ == null) {
            list.clear();
        }

        if (box_.intersect(b)) {
            // If leaf:
            if (sons_.isEmpty()) {
                
                 Iterator<T> it = data_.iterator();
                        while (it.hasNext()) {
                            T t = it.next();
                            if (t.intersect(b)){
                                list.add(t);
                            }

                        }
            } // If node:
            else {
                for (int i = 0; i < sons_.size(); i++) {
                    sons_.get(i).within_box(b, list);
                }
            }
        }
    }
    
    
    
   public  void within_box(  BoxND b, Vector<T>list){
        Set<T> list_set = new HashSet<T>();
        within_box(b, list_set);

//        list.setSize(list_set.size());
        list.clear();
        
        Iterator<T> it = list_set.iterator();
        while (it.hasNext()) {
            T t = it.next();
            list.add(t);
        }

    }
    public int number_of_objects() {
        int n = 0;

        // If leaf:
        if (sons_.isEmpty()) {
            n = data_.size();
        } // If node:
        else {
            for (int i = 0; i < sons_.size(); i++) {
                n += sons_.get(i).number_of_objects();
            }
        }

        return n;
    }
// Protected, engine.
//
//public String structure(String path,String s){
//    // Update the path
//    String this_path = path;
//       if(father_ != null) {
//            int i = 0;
//            boolean found = false;
//            while (this != father_.sons_.get(i)) {
//                i++;
//            }
//
////            std::stringstream sspath;
////            sspath << i;
//
//            this_path += "." + i;
//        }
//
//       String str =s;
//    
//    if (father_ == null) {
//            str += "Root. ";
//        } else {
//            str += "--";
//        }
//
//        str += (!sons_.isEmpty()) ? "Node." : "Leaf.";
//        str += " Path: " + this_path + ". Box: ";
//
//        String ss = box_.pmin + "-" + box_.pmax;
//
//        str += ss;
//
//        if (!sons_.isEmpty()) {
//            str += "\n "; // str += "\n>";
//        }
//       String next = s;
//
//    if ((father_ != null) && (this == father_.sons_.get(father_.sons_.size() - 1))) {
//            next.replace(next.rfind("|"), 1, " ");
//        }
//
//        // If leaf:
//        if (sons_.isEmpty()) {
//            
//              Iterator<T> it = data_.iterator();
//            while (it.hasNext()) {
//                T t = it.next();
//                str += "\n ";
//                str += next + "  |-";
//                str += "Pointer: "+t;
//
//            }
//            str += "\n " + next;
//        } // If node
//        else {
//            //str += "\n@";
//            next += "  |";
//            for (int i = 0; i < sons_.size(); i++) {
//                str += sons_.get(i).structure(this_path, next);
//            }
//        }
//
//        if ((father_ != null) && (this != father_.sons_.get(father_.sons_.size() - 1))) {
//            str += "\n "; //str += "\n" + next + "\n*";
//        }
//        return str;
//    }
// Public interface.
//
//    template<typename T
//    >
//std
//    ::string HyperOctree
//    <T
//
//    >::structure() {
//        std::string path
//        ("0");
//    std::string s
//        ("");
//
//    return structure(path, s);
//    }
//    template<typename T
//    >
public void boxes(Vector<BoxND> boxes_of_nodes, Vector<BoxND> boxes_of_leaves){
    if (father_ == null) {
            boxes_of_nodes.clear();
            boxes_of_leaves.clear();
        }

        if (sons_.isEmpty()) {
            boxes_of_leaves.add(box_);
        } else {
            boxes_of_nodes.add(box_);
            for (int i = 0; i < sons_.size(); i++) {
                sons_.get(i).boxes(boxes_of_nodes, boxes_of_leaves);
            }
        }

    }
}
