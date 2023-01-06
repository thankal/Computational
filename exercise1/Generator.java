package exercise1;
import java.util.Random;
import java.io.FileWriter;
import java.lang.Math;
import java.io.IOException;

public class Generator {
    public static void main(String[] args){
        Random rand = new Random();
        
        try{
            FileWriter fw = new FileWriter("data.txt");

            for (int i = 0; i < 4000; i++){

                Float x1 = rand.nextFloat(2) - 1;
                Float x2 = rand.nextFloat(2) - 1;
                
                
                if (Math.pow((x1-0.5),2) + Math.pow((x2-0.5),2) < 0.2 && x2>0.5){
                        fw.write("[" + x1 + "," + x2 + "]" + " category: C1 \n");
                }
                else if(Math.pow((x1-0.5),2) + Math.pow((x2-0.5),2) < 0.2 && x2<0.5){
                   
                        fw.write("[" + x1 + "," + x2 + "]" + " category: C2 \n");
                }
                else if(Math.pow((x1+0.5),2) + Math.pow((x2+0.5),2) < 0.2 && x2>-0.5){
                        fw.write("[" + x1 + "," + x2 + "]" + " category: C1 \n");
                }
                else if(Math.pow((x1+0.5),2) + Math.pow((x2+0.5),2) < 0.2 && x2<-0.5){
                        fw.write("[" + x1 + "," + x2 + "]" + " category: C2 \n");
                }
                else if(Math.pow((x1-0.5),2) + Math.pow(x2+0.5,2) <0.2 && x2>-0.5){
                        fw.write("[" + x1 + "," + x2 + "]" + " category: C1 \n");
                }
                else if(Math.pow((x1-0.5),2) + Math.pow((x2+0.5),2) <0.2 && x2<-0.5){
                        fw.write("[" + x1 + "," + x2 + "]" + " category: C2 \n");
                }
                else if(Math.pow((x1+0.5),2) + Math.pow((x2-0.5),2) < 0.2 && x2>0.5){
                        fw.write("[" + x1 + "," + x2 + "]" + " category: C1 \n");
                }
                else if(Math.pow((x1+0.5),2) + Math.pow((x2-0.5),2) < 0.2 && x2<0.5){
                        fw.write("[" + x1 + "," + x2 + "]" + " category: C2 \n");
                }
                else{
                        fw.write("[" + x1 + "," + x2 + "]" + " category: C3 \n");
                }
        
            }

        fw.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
