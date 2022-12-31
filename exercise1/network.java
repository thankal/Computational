package exercise1;
import java.lang.Math;

public class network {

    // Number of inputs
    private int d = 2;

    // Number of categories
    private int K = 3;
    
    // Number of neurons in the first hidden layer
    private int numOfH1Neurons;

    // Number of neurons in the second hidden layer
    private int numOfH2Neurons;

    // Number of neurons in the third hidden layer
    private int numOfH3Neurons;

    // Type of activation function
    private int activationFunctionType;

    
    // The inputs of the network
    private double[][] inputs;

    // The weights of the network (for each of the 3 layers)
    private double[][] weights;



    private void feedforward() {
        System.out.println("Feedforward");
    }

    private void backpropagation() {
        System.out.println("Backpropagation");
    }

    private double logistic(double x){
        return 1.0/(1+Math.exp(-x));
    }

    private double logistic_prime(double x){
        return logistic(x)*(1-logistic(x));
    }

    private double tangent(double x){
        return (Math.exp(x)-Math.exp(-x))/(Math.exp(x)+Math.exp(-x));
    }

    private double tangent_prime(double x){
        return 1-(tangent(x)*tangent(x));
    }

    private double relu(double x){
        if(x>0){
            return x;
        }else{
            return 0;
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        System.out.println("Hello World");
    }

}
