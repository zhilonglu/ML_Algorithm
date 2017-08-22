package NN;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.*;
/**
 *
 * @author dhruv
 */
public class Neuron {
 static int neuronCount = 0;
 
 public int id;
 
 Connection biasConnection;
 double bias = -1;
 double output;
 
 ArrayList<Connection> InConnections = new ArrayList<Connection>();
 HashMap<Integer,Connection>  connLookup = new HashMap<Integer,Connection>();
 
 public Neuron()
 {
     id = neuronCount;
     neuronCount++;
 }
 
 public void computeOutput()
 {
     double s = 0;
     
     for(Connection c : InConnections)
     {
         Neuron left = c.getFromNeuron();
         double weight = c.getWeight();
         double a = left.getOutput();
         
         s += weight*a;
     }
     s+=biasConnection.getWeight()*bias;
     
     output = g(s);
 }
 
 double g(double x)
 {
     return x>0?x:0;
//     return sigm(x);
 }
 
 double sigm(double x) {
        return 1.0 / (1.0 +  (Math.exp(-x)));
    }
 
 public double getBias()
 {
     return bias;
 }
 
 public double getOutput()
 {
     return output;
 }
 
 public void setOutput(double o)
 {
     output = o;
     
 }
 
 public void addInConnections(ArrayList<Neuron> inNeurons)
 {
     for (Neuron n : inNeurons)
     {
         Connection con =  new Connection(n,this);
         InConnections.add(con);
        connLookup.put(n.id, con);
         
     }
 }
 
 public Connection getConnection(int index)
 {
     return connLookup.get(index);
 }
 
 public void addInConnection(Connection con)
 {
     InConnections.add(con);
 }
 
 public void addBiasConnection(Neuron n)
 {
     Connection con = new Connection(n, this);
     biasConnection = con;
     InConnections.add(con);
 }
 public ArrayList<Connection> getAllInConnections(){
        return InConnections;
    }
 
 }
 
 
