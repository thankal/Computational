package exercise1;
import java.util.Random;
import java.io.FileWriter;
import java.lang.Math;
import java.io.IOException;

public class Generator {
    public static void main(String[] args){
        Random rand = new Random();
        
        try{
            FileWriter fw = new FileWriter("training_data.txt");
            // fw.write(" \n");
            for (int i = 0; i < 4000; i++){

                Float x1 = 2*rand.nextFloat() - 1;
                Float x2 = 2*rand.nextFloat() - 1;
        
                fw.write("[" + x1 + "," + x2 + "]\n");
            }

        fw.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        try{
                FileWriter fw = new FileWriter("experiment_data.txt");
                // fw.write(" \n");
    
                for (int i = 0; i < 4000; i++){
    
                    Float x1 = 2*rand.nextFloat() - 1;
                    Float x2 = 2*rand.nextFloat() - 1;

                    fw.write("[" + x1 + "," + x2 + "]\n");
                }
    
            fw.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
    }
}
