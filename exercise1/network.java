package exercise1;

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

    private float logistic(x){
        return 1.0/(1+exp(-x));
    }

    private float logistic_prime(x){
        return logistic(x)*(1-logistic(x));
    }

    private float tangent(x){
        return (exp(x)-exp(-x))/(exp(x)+exp(-x));
    }

    private float tangent_prime(x){
        return 1-(tangent(x)*tangent(x));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        System.out.println("Hello World");
    }

}
