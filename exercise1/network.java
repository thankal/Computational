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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        System.out.println("Hello World");
    }

}
