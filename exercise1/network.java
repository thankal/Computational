package exercise1;
import java.lang.Math;
import java.util.ArrayList;

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
    private int ACTIVATION_FUNCTION_TYPE = 1;

    
    // The input vector
    private ArrayList<Double> inputs = new ArrayList<Double>(d);

    // The output vector
    private ArrayList<Double> outputs = new ArrayList<Double>(K);



    // The weights of the network (for each of the 3 layers)
    private ArrayList<ArrayList<Double>> weights = new ArrayList<ArrayList<Double>>();








    // initialize the weights with random values in the range [-1,1]
    private void initRandomWeights() {
        // add a random value for each weight of the first hidden layer
        for (int i = 0; i < numOfH1Neurons; i++) {
            ArrayList<Double> temp = new ArrayList<Double>(K);
            for (int j = 0; j <= d; j++) {
                // generate a random double between -1 and 1
                temp.add((Math.random()*2-1));
            }
            weights.add(temp);
        }

        // add a random value for each weight of the second hidden layer
        for (int i = 0; i < numOfH2Neurons; i++) {
            ArrayList<Double> temp = new ArrayList<Double>(K);
            for (int j = 0; j <= numOfH1Neurons; j++) {
                temp.add((Math.random()*2-1));
            }
            weights.add(temp);
        }

        // add a random value for each weight of the third hidden layer
        for (int i = 0; i < numOfH3Neurons; i++) {
            ArrayList<Double> temp = new ArrayList<Double>(K);
            for (int j = 0; j <= numOfH2Neurons; j++) {
                temp.add((Math.random()*2-1));
            }
            weights.add(temp);
        }

        // add a random value for each weight of the output layer
        for (int i = 0; i < K; i++) {
            ArrayList<Double> temp = new ArrayList<Double>(K);
            for (int j = 0; j <= numOfH3Neurons; j++) {
                temp.add((Math.random()*2-1));
            }
            weights.add(temp);
        }
        
    }

    private ArrayList<Double> feedforward(ArrayList<Double> inputs, int d, int K) {
        ArrayList<Double> outputs = new ArrayList<Double>(K);

        // The activations of the neurons (for each of the 5 layers)
        ArrayList<ArrayList<Double>> activations = new ArrayList<ArrayList<Double>>();

        // add the inputs to the input layer activations
        activations.add(inputs);

        // calculate the activations of the first hidden layer
        for (int i = 0; i < numOfH1Neurons; i++) {
            // iterate through the inputs
            for (int j = 1; j <= d; j++) {
                // calculate the activation
                double z = activations.get(0).get(j-1) * weights.get(0).get(j) + weights.get(0).get(0);
                double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
                activations.get(1).set(i, new_activation);
            }
        }

        // calculate the activations of the second hidden layer
        for (int i = 0; i < numOfH2Neurons; i++) {
            // iterate through the inputs
            for (int j = 1; j <= numOfH1Neurons; j++) {
                // calculate the activation
                double z = activations.get(1).get(j-1) * weights.get(1).get(j) + weights.get(1).get(0);
                double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
                activations.get(2).set(i, new_activation);
            }
        }

        // calculate the activations of the third hidden layer
        for (int i = 0; i < numOfH3Neurons; i++) {
            // iterate through the inputs
            for (int j = 1; j <= numOfH2Neurons; j++) {
                // calculate the activation
                double z = activations.get(2).get(j-1) * weights.get(2).get(j) + weights.get(2).get(0);
                double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
                activations.get(3).set(i, new_activation);
            }
        }

        // calculate the activations of the output layer
        for (int i = 0; i < K; i++) {
            // iterate through the inputs
            for (int j = 1; j <= numOfH3Neurons; j++) {
                // calculate the activation
                double z = activations.get(3).get(j-1) * weights.get(3).get(j) + weights.get(3).get(0);
                double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
                activations.get(4).set(i, new_activation);
            }
        }

        outputs = activations.get(4);
        return outputs;
    }

    private void backpropagation() {
        System.out.println("Backpropagation");
    }


    // run the user specified activation function
    private double activationFunction(int type, double x) {
        switch (type) {
            case 1:
                return logistic(x);
            case 2:
                return tangent(x);
            default:
                return logistic(x);
        }
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        System.out.println("Hello World");
    }

}
