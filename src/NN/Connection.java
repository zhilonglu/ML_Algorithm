package NN;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dhruv
 */
public class Connection {
       double weight;
    double prevdeltaweight;
    double deltaweight;
    
    final Neuron left;
    final Neuron right;
    static int connCount = 0;
    
    public int connID ;
    
    public Connection(Neuron _left, Neuron _right)
    {
        left = _left;
        right = _right;
        
    }
    
      public double getWeight() {
        return weight;
    }
      
        public void setWeight(double w) {
        weight = w;
    }
 
           public void setDeltaWeight(double w) {
        prevdeltaweight = deltaweight;
        deltaweight = w;
    }
   public double getPrevDeltaWeight() {
        return prevdeltaweight;
    }
 
    public Neuron getFromNeuron() {
        return left;
    }
 
    public Neuron getToNeuron() {
        return right;
    }
}