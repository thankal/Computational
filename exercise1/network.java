package exercise1;
import java.lang.Math;
import java.util.ArrayList;

public class network {

    // Number of inputs
    private int d = 2;

    // Number of categories
    private int K = 3;
    
    // Learning rate
    private double LEARNING_RATE = 0.1;

    // Number of neurons for each hidden layer
    private int[] NUM_OF_H_NEURONS = {10, 10, 10};

    // Type of activation function
    private int ACTIVATION_FUNCTION_TYPE = 1;

    
    // The input vector
    private ArrayList<Double> inputs = new ArrayList<Double>(d);

    // The output vector
    private ArrayList<Double> outputs = new ArrayList<Double>(K);



    // The weights of the network (for each of the 3 layers)
    private ArrayList<ArrayList<Double>> weights = new ArrayList<ArrayList<Double>>();

    // The total input of each neuron
    private ArrayList<ArrayList<Double>> totalInputs = new ArrayList<ArrayList<Double>>();

    // The desired outputs
    private ArrayList<Double> desiredOutputs = new ArrayList<Double>();

    // The activation of each neuron
    private ArrayList<ArrayList<Double>> activations = new ArrayList<ArrayList<Double>>();


    // The deltas of each neuron
    private ArrayList<ArrayList<Double>> deltas = new ArrayList<ArrayList<Double>>();

    // Partial derivatives for each weight
    private ArrayList<ArrayList<Double>> partialDerivativesWeights = new ArrayList<ArrayList<Double>>();

    // Partial derivatives for each bias
    private ArrayList<ArrayList<Double>> partialDerivativesBiases = new ArrayList<ArrayList<Double>>();






    // initialize the weights with random values in the range [-1,1]
    private void initRandomWeights() {
        // add a random value for each weight of the first hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[0]; i++) {
            ArrayList<Double> temp = new ArrayList<Double>(K);
            for (int j = 0; j <= d; j++) {
                // generate a random double between -1 and 1
                temp.add((Math.random()*2-1));
            }
            weights.add(temp);
        }

        // add a random value for each weight of the second hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[1]; i++) {
            ArrayList<Double> temp = new ArrayList<Double>(K);
            for (int j = 0; j <= NUM_OF_H_NEURONS[0]; j++) {
                temp.add((Math.random()*2-1));
            }
            weights.add(temp);
        }

        // add a random value for each weight of the third hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[2]; i++) {
            ArrayList<Double> temp = new ArrayList<Double>(K);
            for (int j = 0; j <= NUM_OF_H_NEURONS[1]; j++) {
                temp.add((Math.random()*2-1));
            }
            weights.add(temp);
        }

        // add a random value for each weight of the output layer
        for (int i = 0; i < K; i++) {
            ArrayList<Double> temp = new ArrayList<Double>(K);
            for (int j = 0; j <= NUM_OF_H_NEURONS[2]; j++) {
                temp.add((Math.random()*2-1));
            }
            weights.add(temp);
        }
        
    }

    private ArrayList<Double> feedforward(ArrayList<Double> inputs, int d, int K) {
        ArrayList<Double> outputs = new ArrayList<Double>(K);

        // add the inputs to the input layer activations
        activations.add(inputs);

        // calculate the activations of the first hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[0]; i++) {
            // iterate through the inputs
            for (int j = 1; j <= d; j++) {
                // calculate the activation
                double z = activations.get(0).get(j-1) * weights.get(0).get(j) + weights.get(0).get(0);
                double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
                activations.get(1).set(i, new_activation);
            }
        }

        // calculate the activations of the second hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[1]; i++) {
            // iterate through the inputs
            for (int j = 1; j <= NUM_OF_H_NEURONS[0]; j++) {
                // calculate the activation
                double z = activations.get(1).get(j-1) * weights.get(1).get(j) + weights.get(1).get(0);
                double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
                activations.get(2).set(i, new_activation);
            }
        }

        // calculate the activations of the third hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[2]; i++) {
            // iterate through the inputs
            for (int j = 1; j <= NUM_OF_H_NEURONS[1]; j++) {
                // calculate the activation
                double z = activations.get(2).get(j-1) * weights.get(2).get(j) + weights.get(2).get(0);
                double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
                activations.get(3).set(i, new_activation);
            }
        }

        // calculate the activations of the output layer
        for (int i = 0; i < K; i++) {
            // iterate through the inputs
            for (int j = 1; j <= NUM_OF_H_NEURONS[2]; j++) {
                // calculate the activation
                double z = activations.get(3).get(j-1) * weights.get(3).get(j) + weights.get(3).get(0);
                double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
                activations.get(4).set(i, new_activation);
            }
        }

        outputs = activations.get(4); // the activations of the output neurons
        return outputs;
    }

    private void backpropagation(ArrayList<Double> inputs, int d, ArrayList<Double> desiredOutputs, int K) {

        // calculate deltas for output layer
        for (int i = 0; i < K; i++) {
            double delta = (desiredOutputs.get(i) - activations.get(4).get(i)) * activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(4).get(i));
            deltas.get(5).set(i, delta);
        }

        // calculate deltas for third hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[2]; i++) {
            double delta = 0;
            for (int j = 0; j < K; j++) {
                delta += deltas.get(4).get(j) * weights.get(3).get(j+1);
            }
            delta *= activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(3).get(i));
            deltas.get(4).set(i, delta);
        }

        // calculate deltas for second hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[1]; i++) {
            double delta = 0;
            for (int j = 0; j < NUM_OF_H_NEURONS[2]; j++) {
                delta += deltas.get(3).get(j) * weights.get(2).get(j+1);
            }
            delta *= activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(2).get(i));
            deltas.get(3).set(i, delta);
        }

        // calculate deltas for first hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[0]; i++) {
            double delta = 0;
            for (int j = 0; j < NUM_OF_H_NEURONS[1]; j++) {
                delta += deltas.get(2).get(j) * weights.get(1).get(j+1);
            }
            delta *= activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(1).get(i));
            deltas.get(2).set(i, delta);
        }


        // * calculating partial derivatives *


        // TODO: 
        // calculate partial derivatives for Hidden layers 
        for (int i=0; i<3 ; i++) {
            for (int j=0; j<NUM_OF_H_NEURONS[i]; j++) {
                for (int k=0; k<NUM_OF_H_NEURONS[i+1]; k++) {
                    double delta = deltas.get(i+2).get(k) * activations.get(i+1).get(j);
                    partialDerivativesWeights.get(i).set(j+1, partialDerivativesWeights.get(i).get(j+1) + delta);
                    partialDerivativesBiases.get(i).set(j+1, partialDerivativesBiases.get(i).get(j+1) + delta);
                }
            }
        }

       // TODO: calculate partial derivatives for output layer
       
       // TODO: calculate totalInputs in forwardpass




        /*
        // update weights for output layer
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < NUM_OF_H_NEURONS[2]; j++) {
                double delta = deltas.get(4).get(i) * activations.get(3).get(j);
                weights.get(4).set(j+1, weights.get(4).get(j+1) + LEARNING_RATE * delta);
            }
        }

        // update weights for third hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[2]; i++) {
            for (int j = 0; j < NUM_OF_H_NEURONS[1]; j++) {
                double delta = deltas.get(3).get(i) * activations.get(2).get(j);
                weights.get(3).set(j+1, weights.get(3).get(j+1) + LEARNING_RATE * delta);
            }
        }

        // update weights for second hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[1]; i++) {
            for (int j = 0; j < NUM_OF_H_NEURONS[0]; j++) {
                double delta = deltas.get(2).get(i) * activations.get(1).get(j);
                weights.get(2).set(j+1, weights.get(2).get(j+1) + LEARNING_RATE * delta);
            }
        }

        // update weights for first hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[0]; i++) {
            for (int j = 0; j < inputs.size(); j++) {
                double delta = deltas.get(1).get(i) * inputs.get(j);
                weights.get(1).set(j+1, weights.get(1).get(j+1) + LEARNING_RATE * delta);
            }
        }
        */

    }


    // run the user specified activation function
    private double activationFunction(int type, double x) {
        switch (type) {
            case 1:
                return logistic(x);
            case 2:
                return tangent(x);
            default:
                return relu(x);
        }
    }

    // run the user specified activation function prime
    private double activationFunctionPrime(int type, double x) {
        switch (type) {
            case 1:
                return logistic_prime(x);
            case 2:
                return tangent_prime(x);
            default:
                return relu_prime(x);
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

    private double relu(double x){
        // max (0, x)
        if(x>0){
            return x;
        }else{
            return 0;
        }

    }

    private double relu_prime(double x){
        if(x>0){
            return 1;
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
