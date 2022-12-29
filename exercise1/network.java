package exercise1;

public class network {

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
