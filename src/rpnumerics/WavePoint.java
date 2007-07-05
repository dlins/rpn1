package rpnumerics;

import wave.util.RealVector;

public class WavePoint extends RealVector {

    private double sigma_;

   public WavePoint(int size, double sigma) {
       super(size);
       sigma_ = sigma;
   }

   public WavePoint (RealVector vector , double sigma){
       super (vector);
       sigma_=sigma;
   }

   public WavePoint(WavePoint copy) {
       super(copy);
       sigma_=copy.getSigma();
   }

   public WavePoint(double[] v, double sigma) {
       super(v);
       sigma_ = sigma;
   }

   public WavePoint(String data, String sigma) {
       super(data);
       sigma_ = new Double(sigma).doubleValue();
   }


   //Accessors/Mutators

   public double getSigma(){return sigma_;}


}
