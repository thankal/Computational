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

    // Number of neurons per layer
    private int[] NUM_OF_NEURONS = {d, NUM_OF_H_NEURONS[0], NUM_OF_H_NEURONS[1], NUM_OF_H_NEURONS[2], K};
    
    // Type of activation function (for hidden layers only)
    private int ACTIVATION_FUNCTION_TYPE = 1;

    private  int MAX_EPOCHS = 700;

    private  double ERROR_THRESHOLD = 0.0; // TODO: fine-tune
    
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
    

    // constructor set the constants
    public Network(int d, int K, double LEARNING_RATE, int[] NUM_OF_H_NEURONS, int ACTIVATION_FUNCTION_TYPE, int MAX_EPOCHS, double ERROR_THRESHOLD, int B) {
        this.d = d;
        this.K = K;
        this.LEARNING_RATE = LEARNING_RATE;
        this.NUM_OF_H_NEURONS = NUM_OF_H_NEURONS;
        this.ACTIVATION_FUNCTION_TYPE = ACTIVATION_FUNCTION_TYPE;
        this.MAX_EPOCHS = MAX_EPOCHS;
        this.ERROR_THRESHOLD = ERROR_THRESHOLD;
        this.B = B;


        // Initialize lists with zeros
        activations.add(new ArrayList<Double>(d));
        for (int j = 0; j < d; j++) {
            activations.get(0).add(0.0);
        }

        for (int i = 1; i < NUM_OF_NEURONS.length; i++) {
            biases.add(new ArrayList<Double>(NUM_OF_NEURONS[i]));
            weights.add(new ArrayList<Double>(NUM_OF_NEURONS[i]*NUM_OF_NEURONS[i-1]));
            totalInputs.add(new ArrayList<Double>(NUM_OF_NEURONS[i]));
            activations.add(new ArrayList<Double>(NUM_OF_NEURONS[i]));
            deltas.add(new ArrayList<Double>(NUM_OF_NEURONS[i]));
            partialDerivativesBiases.add(new ArrayList<Double>(NUM_OF_NEURONS[i]));
            partialDerivativesWeights.add(new ArrayList<Double>(NUM_OF_NEURONS[i]*NUM_OF_NEURONS[i-1]));
            for (int j = 0; j < NUM_OF_NEURONS[i]; j++) {
                biases.get(i-1).add(0.0);
                totalInputs.get(i-1).add(0.0);
                activations.get(i).add(0.0);
                deltas.get(i-1).add(0.0);
                partialDerivativesBiases.get(i-1).add(0.0);
            }
            for (int j = 0; j < NUM_OF_NEURONS[i]*NUM_OF_NEURONS[i-1]; j++) {
                weights.get(i-1).add(0.0);
                partialDerivativesWeights.get(i-1).add(0.0);
            }
        }
    }


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


    private void loadInputs(String filename) {
        final ArrayList<ArrayList<Double>> temp = Reader.readFile(filename);

        // initialize the input lists
        x1List = temp.get(0);
        x2List = temp.get(1);

        // categorize each point to a category (C1, C2, or C3)
        expectedOutputVectors.clear(); // reset the list
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


    public void gradientDescentAlgorithm() {
        ArrayList<Double> input = new ArrayList<Double>(d);
        ArrayList<ArrayList<Double>> desiredOutputVectors = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> actualOutputVectors = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> totalErrors = new ArrayList<Double>();

        // randomize weights
        initRandomWeights();

        // Serial update
        if (B == 1) {
            // for each input
            int t = 0; // epoch counter
            do {
                System.out.println("Epoch: " + t+1);
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

                t++;
            } while (t < x1List.size() || (t < MAX_EPOCHS && totalErrors.get(t-2) - totalErrors.get(t-1) < ERROR_THRESHOLD));

        }

        // Group update
        else {
            // calculate the number of mini-batches
            final int miniBatchNum = x1List.size() / B;
            final int remainingInputs = x1List.size() % B;
            
            int t = 0; // epoch counter
            do {
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
            } while (t < miniBatchNum || (t < MAX_EPOCHS && totalErrors.get(t-2) - totalErrors.get(t-1) < ERROR_THRESHOLD));


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
                final ArrayList<Double> desiredOutputVector = expectedOutputVectors.get(miniBatchNum*B + n);
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
            for (int j = 0; j < d; j++) { // TODO: erased '<='
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
            for (int j = 0; j < NUM_OF_H_NEURONS[0]; j++) {
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
            for (int j = 0; j < NUM_OF_H_NEURONS[1]; j++) {
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
            for (int j = 0; j < NUM_OF_H_NEURONS[2]; j++) {
                // calculate the activation
                z += activations.get(3).get(j) * weights.get(3).get(j);
            }
            // add the bias to the total input after all input (activations*weights) have been taken into account
            z += biases.get(3).get(i); 
            totalInputs.get(3).set(i, z); // update the table

            // TODO: maybe softmax?
            double new_activation = activationFunction(1, z); // for output use sigmoid always
            activations.get(4).set(i, new_activation); // update the table
        }

        outputs = activations.get(4); // the activations of the output neurons
        return outputs;
    }

    private void backPropagation(ArrayList<Double> input, int d, ArrayList<Double> desiredOutputVector, int K) {

        // calculate deltas for output layer
        for (int i = 0; i < K; i++) {
            double delta = (desiredOutputVector.get(i) - activations.get(4).get(i)) * activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(3).get(i));
            deltas.get(3).set(i, delta);
        }

        // calculate deltas for third hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[2]; i++) {
            double delta = 0;
            for (int j = 0; j < K; j++) {
                delta += deltas.get(3).get(j) * weights.get(3).get(j+1);
            }
            delta *= activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(2).get(i));
            deltas.get(2).set(i, delta);
        }

        // calculate deltas for second hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[1]; i++) {
            double delta = 0;
            for (int j = 0; j < NUM_OF_H_NEURONS[2]; j++) {
                delta += deltas.get(2).get(j) * weights.get(2).get(j+1);
            }
            delta *= activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(1).get(i));
            deltas.get(1).set(i, delta);
        }

        // calculate deltas for first hidden layer
        for (int i = 0; i < NUM_OF_H_NEURONS[0]; i++) {
            double delta = 0;
            for (int j = 0; j < NUM_OF_H_NEURONS[1]; j++) {
                delta += deltas.get(1).get(j) * weights.get(1).get(j+1);
            }
            delta *= activationFunctionPrime(ACTIVATION_FUNCTION_TYPE, totalInputs.get(0).get(i));
            deltas.get(0).set(i, delta);
        }


        // * calculating partial derivatives (biases and weights)*

        // calculate partial derivatives for Hidden layers 
        for (int h=0; h<3 ; h++) {
            for (int j=0; j<NUM_OF_H_NEURONS[h]; j++) {
                double delta = deltas.get(h).get(j);
                
                partialDerivativesWeights.get(h).set(j, activations.get(h).get(j%NUM_OF_NEURONS[h]) * delta); // TODO: modulo correct?
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
        for(int i=0; i<partialDerivativesWeights.size(); i++){
            for(int j=0; j<partialDerivativesWeights.get(i).size(); j++){
                temp = weights.get(i).get(j) - LEARNING_RATE * partialDerivativesWeights.get(i).get(j);
                weights.get(i).set(j,temp);
            }  

        }   

    }

    // update biases 
    private void updateBiases(ArrayList<ArrayList<Double>> partialDerivativesBiases){
        double newValue = 0;
        for(int i=0; i<partialDerivativesBiases.size(); i++){
            for(int j=0; j<partialDerivativesBiases.get(i).size(); j++){
                newValue = biases.get(i).get(j) - LEARNING_RATE * partialDerivativesBiases.get(i).get(j);
                biases.get(i).set(j,newValue);
        }
    }
        
    }

    private double calculateTotalError(ArrayList<ArrayList<Double>>actualOutputVectors, ArrayList<ArrayList<Double>>desiredOutputVectors){
        double OverallError=0;
        for(int i=0; i<actualOutputVectors.size();i++){
            for(int j=0; j<K; j++){
                 OverallError = OverallError + 0.5*(Math.pow(desiredOutputVectors.get(i).get(j)-actualOutputVectors.get(i).get(j),2));
            }
        }
        return OverallError;    
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



    //find the max index of the outputs 
    public int findMax(ArrayList<Double> outputs){
        int maxIndex = -1;

        for (int i = 0; i < 3; i++) {
            if (outputs.get(i) > outputs.get(maxIndex)) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }


    public double run(){
        // results stores the final list of the outputs of the network
        ArrayList<ArrayList<Double>> results = new ArrayList<ArrayList<Double>>();
        // outputs temporarily stores the outputs of the network
        ArrayList<Double> outputs = new ArrayList<Double>(); 

        // load the inputs from the file
        loadInputs("experiment_data.txt");

        // array to store the inputs from loadInputs()
        ArrayList<Double> inputs = new ArrayList<Double>(2);
        
        // arrays for (1.0, 0.0, 0.0), (0.0, 1.0, 0.0), (0.0, 0.0, 1.0)
        ArrayList<Double> first = new ArrayList<Double>(3);
        ArrayList<Double> second = new ArrayList<Double>(3);
        ArrayList<Double> third = new ArrayList<Double>(3);

        first.add(1.0);
        first.add(0.0);
        first.add(0.0);

        second.add(0.0);
        second.add(1.0);
        second.add(0.0);

        third.add(0.0);
        third.add(0.0);
        third.add(1.0);


        // initialize input vector
        inputs.add(0.0);
        inputs.add(0.0);

        for(int i = 0; i<4000; i++){

            //run feed forward and get outputs
            //use findMax to get the index of the output with the highest probability
            //add the corresponding label to the results arraylist

            inputs.set(0, x1List.get(i));
            inputs.set(1, x2List.get(i));

            outputs = forwardPass(inputs, d, K);
            int x = findMax(outputs);
            if(x == 0){
                results.add(first);
            }
            else if(x == 1){
                results.add(second);
            }
            else if(x == 2){
                results.add(third);
            }
        }
        
        System.out.println("Results: " + results);
        System.out.println("-----------------------------");
        System.out.println("Expected: " + expectedOutputVectors);

        int correct = 0;
        for(int i = 0; i<4000; i++){
            for(int j = 0; j<3; j++){
                if(results.get(i).get(j) == expectedOutputVectors.get(i).get(j)){
                    correct++;
                }
            }
        }

        //calculate the generalization percentage
        Double generalizationPercentage = (double)(correct/4000)*100;
        return generalizationPercentage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // test runs

        /* 
        // Learning rate
        private double LEARNING_RATE = 0.1;
        
        // Number of neurons for each hidden layer
        private int[] NUM_OF_H_NEURONS = {10, 10, 10};
        
        // Type of activation function (for hidden layers only)
        private int ACTIVATION_FUNCTION_TYPE = 1;

        private static int MAX_EPOCHS = 700;

        private static double ERROR_THRESHOLD = 0.0; // TODO: fine-tune
        */


        int[] H = {10, 10, 10};
        Network mlp = new Network(2, 3, 0.1, H, 1, 700, 0.0, 1);

        // load the inputs from the file
        mlp.loadInputs("training_data.txt");

        // train the network
        mlp.gradientDescentAlgorithm();

        double x = mlp.run();
        System.out.println("Generalization percentage: " + x + "%");
    }

}
