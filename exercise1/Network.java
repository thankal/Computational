package exercise1;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;

public class Network {

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

    // The weights of the network (for each layer)
    private ArrayList<ArrayList<Double>> weights = new ArrayList<ArrayList<Double>>();

    // The biases of the network (for each layer)
    private ArrayList<ArrayList<Double>> biases = new ArrayList<ArrayList<Double>>();

    // The total input of each neuron
    private ArrayList<ArrayList<Double>> totalInputs = new ArrayList<ArrayList<Double>>();

    /* reduntant? TODO: remove
    // The desired outputs
    private ArrayList<Double> desiredOutputs = new ArrayList<Double>();
    */

    // The activation of each neuron
    private ArrayList<ArrayList<Double>> activations = new ArrayList<ArrayList<Double>>();


    // The deltas of each neuron
    private ArrayList<ArrayList<Double>> deltas = new ArrayList<ArrayList<Double>>();

    // Partial derivatives for each weight
    private ArrayList<ArrayList<Double>> partialDerivativesWeights = new ArrayList<ArrayList<Double>>();

    // Partial derivatives for each bias
    private ArrayList<ArrayList<Double>> partialDerivativesBiases = new ArrayList<ArrayList<Double>>();

    // The size of the mini-batch
    private int B = 10;

    // the lists for the x1, x2 and Category values from the file load
    private ArrayList<Double> x1List = new ArrayList<Double>();
    private ArrayList<Double> x2List = new ArrayList<Double>();
    private ArrayList<ArrayList<Double>> expectedOutputVectors = new ArrayList<ArrayList<Double>>();
    




    // initialize the weights with random values in the range [-1,1]
    private void initRandomWeights() {
        // add a random value for each weight of the first hidden layer
        ArrayList<Double> temp = new ArrayList<Double>(K);
        for (int i = 0; i < NUM_OF_H_NEURONS[0]; i++) {
            for (int j = 0; j < d; j++) {
                // generate a random double between -1 and 1
                temp.add((Math.random()*2-1));
            }
            biases.get(0).add(Math.random()*2-1);
        }
        weights.add(temp);

        // add a random value for each weight of the second hidden layer
        temp = new ArrayList<Double>(K);
        for (int i = 0; i < NUM_OF_H_NEURONS[1]; i++) {
            for (int j = 0; j < NUM_OF_H_NEURONS[0]; j++) {
                temp.add((Math.random()*2-1));
            }
            biases.get(1).add(Math.random()*2-1);
        }
        weights.add(temp);

        // add a random value for each weight of the third hidden layer
        temp = new ArrayList<Double>(K);
        for (int i = 0; i < NUM_OF_H_NEURONS[2]; i++) {
            for (int j = 0; j < NUM_OF_H_NEURONS[1]; j++) {
                temp.add((Math.random()*2-1));
            }
            biases.get(2).add(Math.random()*2-1);
        }
        weights.add(temp);

        // add a random value for each weight of the output layer
        temp = new ArrayList<Double>(K);
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < NUM_OF_H_NEURONS[2]; j++) {
                temp.add((Math.random()*2-1));
            }
            biases.get(3).add(Math.random()*2-1);
        }
        weights.add(temp);
        
    }


    private void loadInputs() {
        final ArrayList<ArrayList<Double>> temp = Reader.readFile("data.txt");

        // initialize the input lists
        x1List = temp.get(0);
        x2List = temp.get(1);

        // categorize each point to a category (C1, C2, or C3)
        ArrayList<Double> expectedOutputVector;
        int category = 0;
        for (int i = 0; i < x1List.size(); i++) {
            // get the x1 and x2 values
            double x1 = x1List.get(i);
            double x2 = x2List.get(i);

            // determine category
            if (Math.pow((x1-0.5),2) + Math.pow((x2-0.5),2) < 0.2 && x2>0.5){
                category = 1;
            }
            else if(Math.pow((x1-0.5),2) + Math.pow((x2-0.5),2) < 0.2 && x2<0.5){
                category = 2;
            }
            else if(Math.pow((x1+0.5),2) + Math.pow((x2+0.5),2) < 0.2 && x2>-0.5){
                category = 1;
            }
            else if(Math.pow((x1+0.5),2) + Math.pow((x2+0.5),2) < 0.2 && x2<-0.5){
                category = 2;
            }
            else if(Math.pow((x1-0.5),2) + Math.pow(x2+0.5,2) <0.2 && x2>-0.5){
                category = 1;
            }
            else if(Math.pow((x1-0.5),2) + Math.pow((x2+0.5),2) <0.2 && x2<-0.5){
                category = 2;
            }
            else if(Math.pow((x1+0.5),2) + Math.pow((x2-0.5),2) < 0.2 && x2>0.5){
                category = 1;
            }
            else if(Math.pow((x1+0.5),2) + Math.pow((x2-0.5),2) < 0.2 && x2<0.5){
                category = 2;
            }
            else{
                category = 3;
            }

            // encode the category as a vector
            if (category == 1){
                expectedOutputVector = new ArrayList<Double>(Arrays.asList(1.0, 0.0, 0.0));
            }
            else if (category == 2){
                expectedOutputVector = new ArrayList<Double>(Arrays.asList(0.0, 1.0, 0.0));
            }
            else{
                expectedOutputVector = new ArrayList<Double>(Arrays.asList(0.0, 0.0, 1.0));
            }

            // add the expected output vector to the list of desired output vectors
            expectedOutputVectors.add(expectedOutputVector);
        }
    }


    public void gradientDecentAlgorithm() {
        ArrayList<Double> input = new ArrayList<Double>(d);
        ArrayList<ArrayList<Double>> desiredOutputVectors = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> actualOutputVectors = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> totalErrors = new ArrayList<Double>();

        // Serial update
        if (B == 1) {
            // for each input
            for (int t = 0; t < x1List.size(); t++) {
                // initialize the input vector and the desired output 
                input.clear();
                input.add(x1List.get(t));
                input.add(x2List.get(t));
                desiredOutputVectors.clear();

                final ArrayList<Double> desiredOutputVector = expectedOutputVectors.get(t);
                desiredOutputVectors.add(desiredOutputVector);
                
                // forward pass
                actualOutputVectors.clear();
                final ArrayList<Double> outputVector;
                outputVector = forwardPass(input, d, K);
                actualOutputVectors.add(outputVector);

                // backward pass
                backPropagation(input, d, desiredOutputVector, K);

                // .. now that we have calculated the partial derivatives update the weights and biases
                updateWeights(partialDerivativesWeights);
                updateBiases(partialDerivativesBiases);

                // calculate the total error for the epoch
                totalErrors.add(calculateTotalError(desiredOutputVectors, actualOutputVectors));
            }

        }

        // Group update
        else {
            // calculate the number of mini-batches
            final int miniBatchNum = x1List.size() / B;
            final int remainingInputs = x1List.size() % B;
            
            // for each mini-batch
            for (int t = 0; t < miniBatchNum; t++) {
                // initialize sums of partial derivatives (for the epoch)
                ArrayList<ArrayList<Double>> sumOfPartialDerivativesWeights = new ArrayList<ArrayList<Double>>();
                ArrayList<ArrayList<Double>> sumOfPartialDerivativesBiases = new ArrayList<ArrayList<Double>>();
                
                
                // for each input in the mini-batch
                for (int n = 0; n < B; n++) {
                    // prepare the input vector and the desired output 
                    input.clear();
                    input.add(x1List.get(t*B + n));
                    input.add(x2List.get(t*B + n));
                    desiredOutputVectors.clear();
                    final ArrayList<Double> desiredOutputVector = expectedOutputVectors.get(t*B + n);
                    desiredOutputVectors.add(desiredOutputVector);
                    
                    // forward pass
                    actualOutputVectors.clear();
                    final ArrayList<Double> outputVector;
                    outputVector = forwardPass(input, d, K);
                    actualOutputVectors.add(outputVector);

                    // backward pass
                    backPropagation(input, d, desiredOutputVector, K);

                    // .. now that we have calculated the partial derivatives add them to the sums
                    // for weights
                    for (int i = 0; i < partialDerivativesWeights.size(); i++) {
                        for (int j = 0; j < partialDerivativesWeights.get(i).size(); j++) {
                            sumOfPartialDerivativesWeights.get(i).set(j, sumOfPartialDerivativesWeights.get(i).get(j) + partialDerivativesWeights.get(i).get(j));
                        }
                    }

                    // for biases
                    for (int i = 0; i < partialDerivativesBiases.size(); i++) {
                        for (int j = 0; j < partialDerivativesBiases.get(i).size(); j++) {
                            sumOfPartialDerivativesBiases.get(i).set(j, sumOfPartialDerivativesBiases.get(i).get(j) + partialDerivativesBiases.get(i).get(j));
                        }
                    }

                }
                    
                // the epoch has ended, now update the weights and biases
                updateWeights(sumOfPartialDerivativesWeights);
                updateBiases(sumOfPartialDerivativesBiases);

                // calculate the total error for the epoch
                totalErrors.add(calculateTotalError(desiredOutputVectors, actualOutputVectors));
            }


            // for the last batch of remaining inputs (if any)
            // initialize sums of partial derivatives (for the epoch)
            ArrayList<ArrayList<Double>> sumOfPartialDerivativesWeights = new ArrayList<ArrayList<Double>>();
            ArrayList<ArrayList<Double>> sumOfPartialDerivativesBiases = new ArrayList<ArrayList<Double>>();
            for (int n = 0; n < remainingInputs; n++) {

                // prepare the input vector and the desired output 
                input.clear();
                input.add(x1List.get(miniBatchNum*B + n));
                input.add(x2List.get(miniBatchNum*B + n));
                desiredOutputVectors.clear();
                desiredOutputVectors.add(expectedOutputVectors.get(t*B + n));
                
                // forward pass
                actualOutputVectors.clear();
                final ArrayList<Double> outputVector;
                outputVector = forwardPass(input, d, K);
                actualOutputVectors.add(outputVector);

                // backward pass
                backPropagation(input, d, outputVector, K);

                // .. now that we have calculated the partial derivatives add them to the sums
                // for weights
                for (int i = 0; i < partialDerivativesWeights.size(); i++) {
                    for (int j = 0; j < partialDerivativesWeights.get(i).size(); j++) {
                        sumOfPartialDerivativesWeights.get(i).set(j, sumOfPartialDerivativesWeights.get(i).get(j) + partialDerivativesWeights.get(i).get(j));
                    }
                }

                // for biases
                for (int i = 0; i < partialDerivativesBiases.size(); i++) {
                    for (int j = 0; j < partialDerivativesBiases.get(i).size(); j++) {
                        sumOfPartialDerivativesBiases.get(i).set(j, sumOfPartialDerivativesBiases.get(i).get(j) + partialDerivativesBiases.get(i).get(j));
                    }
                }

            }

            // the epoch has ended, now update the weights and biases
            updateWeights(sumOfPartialDerivativesWeights);
            updateBiases(sumOfPartialDerivativesBiases);

            // calculate the total error for the epoch
            totalErrors.add(calculateTotalError(desiredOutputVectors, actualOutputVectors));
        }

    }

    private ArrayList<Double> forwardPass(ArrayList<Double> input, int d, int K) {
        ArrayList<Double> outputs = new ArrayList<Double>(K);

        // set the input activations
        for (int i = 0; i < d; i++) {
            activations.get(0).set(i, input.get(i));
        }


        // calculate the activations of the first hidden layer (H1)
        for (int i = 0; i < NUM_OF_H_NEURONS[0]; i++) {
            // for each neuron
            // iterate through its inputs
            double z = 0; // initialize temp for calculating total input of one neuron
            for (int j = 0; j <= d; j++) {
                // calculate the activation
                z += activations.get(0).get(j) * weights.get(0).get(j);
            }
            // add the bias to the total input after all input (activations*weights) have been taken into account
            z += biases.get(0).get(i); 
            totalInputs.get(0).set(i, z); // update the table

            double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
            activations.get(1).set(i, new_activation); // update the table
        }

        // calculate the activations of the second hidden layer (H2)
        for (int i = 0; i < NUM_OF_H_NEURONS[1]; i++) {
            // for each neuron
            // iterate through its inputs
            double z = 0; // initialize temp for calculating total input of one neuron
            for (int j = 0; j <= NUM_OF_H_NEURONS[0]; j++) {
                // calculate the activation
                z += activations.get(1).get(j) * weights.get(1).get(j);
            }
            // add the bias to the total input after all input (activations*weights) have been taken into account
            z += biases.get(1).get(i); 
            totalInputs.get(1).set(i, z); // update the table

            double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
            activations.get(2).set(i, new_activation); // update the table
        }

        // calculate the activations of the third hidden layer (H3)
        for (int i = 0; i < NUM_OF_H_NEURONS[2]; i++) {
            // for each neuron
            // iterate through its inputs
            double z = 0; // initialize temp for calculating total input of one neuron
            for (int j = 0; j <= NUM_OF_H_NEURONS[1]; j++) {
                // calculate the activation
                z += activations.get(2).get(j) * weights.get(2).get(j);
            }
            // add the bias to the total input after all input (activations*weights) have been taken into account
            z += biases.get(2).get(i); 
            totalInputs.get(2).set(i, z); // update the table

            double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
            activations.get(3).set(i, new_activation); // update the table
        }

        // calculate the activations of the output layer
        for (int i = 0; i < K; i++) {
            // for each neuron
            // iterate through its inputs
            double z = 0; // initialize temp for calculating total input of one neuron
            for (int j = 0; j <= NUM_OF_H_NEURONS[2]; j++) {
                // calculate the activation
                z += activations.get(3).get(j) * weights.get(3).get(j);
            }
            // add the bias to the total input after all input (activations*weights) have been taken into account
            z += biases.get(3).get(i); 
            totalInputs.get(3).set(i, z); // update the table

            double new_activation = activationFunction(ACTIVATION_FUNCTION_TYPE, z);
            activations.get(4).set(i, new_activation); // update the table
        }

        outputs = activations.get(4); // the activations of the output neurons
        return outputs;
    }

    private void backPropagation(ArrayList<Double> input, int d, ArrayList<Double> desiredOutputVector, int K) {

        // calculate deltas for output layer
        for (int i = 0; i < K; i++) {
            double delta = (desiredOutputVector.get(i) - activations.get(4).get(i)) * activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(4).get(i));
            deltas.get(3).set(i, delta);
        }

        // calculate deltas for third hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[2]; i++) {
            double delta = 0;
            for (int j = 0; j < K; j++) {
                delta += deltas.get(3).get(j) * weights.get(3).get(j+1);
            }
            delta *= activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(3).get(i));
            deltas.get(2).set(i, delta);
        }

        // calculate deltas for second hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[1]; i++) {
            double delta = 0;
            for (int j = 0; j < NUM_OF_H_NEURONS[2]; j++) {
                delta += deltas.get(2).get(j) * weights.get(2).get(j+1);
            }
            delta *= activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(2).get(i));
            deltas.get(1).set(i, delta);
        }

        // calculate deltas for first hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[0]; i++) {
            double delta = 0;
            for (int j = 0; j < NUM_OF_H_NEURONS[1]; j++) {
                delta += deltas.get(1).get(j) * weights.get(1).get(j+1);
            }
            delta *= activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(1).get(i));
            deltas.get(0).set(i, delta);
        }


        // * calculating partial derivatives (biases and weights)*

        // calculate partial derivatives for Hidden layers 
        for (int h=0; h<3 ; h++) {
            for (int j=0; j<NUM_OF_H_NEURONS[h]; j++) {
                double delta = deltas.get(h).get(j);

                partialDerivativesWeights.get(h).set(j, activations.get(h).get(j) * delta);
                partialDerivativesBiases.get(h).set(j, delta);
            }
        }

       // calculate partial derivatives for output layer
        for (int j=0; j<K; j++) {
            double delta = deltas.get(3).get(j);

            partialDerivativesWeights.get(3).set(j, activations.get(3).get(j) * delta);
            partialDerivativesBiases.get(3).set(j, delta);
        }
        
        

    }

    // update weights 
    private void updateWeights(ArrayList<ArrayList<Double>> partialDerivativesWeights){
        double temp = 0;
        for(int i=0; i<weights.size(); i++){
            for(int j=0; j<weights.get(i).size(); j++){
                temp = weights.get(i).get(j) - LEARNING_RATE *  partialDerivativesWeights.get(i).get(j);
                weights.get(i).set(j,temp);
            }  

        }   

    }

    // update biases 
    private void updateBiases(ArrayList<ArrayList<Double>> partialDerivativesBiases){
        double newValue = 0;
        for(int i=0; i<biases.size(); i++){
            for(int j=0; j<biases.get(i).size(); j++){
                newValue = biases.get(i).get(j) - LEARNING_RATE * partialDerivativesBiases.get(i).get(j);
                biases.get(i).set(j,newValue);
        }
    }
        
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