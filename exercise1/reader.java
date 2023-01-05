package exercise1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class reader {
    
    // final list of 2 lists, each with 4000 elements
    private static ArrayList<ArrayList> xList = new ArrayList<ArrayList>(2);
    
    //Constructor
    public reader(ArrayList<ArrayList> xList){
        this.xList = xList;
    }

    // Getter for xList
    public static ArrayList<ArrayList> getList(){
        return xList;
    }

    public static void main(String[] args){

        int c;
        String st;
        float f;
        String line;

        // counter for the number of times the loop runs
        int counter = 0;
    
        ArrayList<Float> x1List = new ArrayList<Float>();
        ArrayList<Float> x2List = new ArrayList<Float>();
        
        try{
            File file = new File("data.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {
                c = br.read();
                counter ++;
                System.out.println(counter);

                st = Character.toString((char)c);
                //System.out.println(st);
                if(st.equals("[")){

                    ArrayList<String> number_list1 = new ArrayList<String>();
                    ArrayList<String> number_list2 = new ArrayList<String>();
                    c = br.read();
                    st = Character.toString((char)c);

                    while(c != 44){
                        number_list1.add(st);
                        c = br.read();
                        st = Character.toString((char)c);
                    }

                    // join a list of strings into a single string and parse it to float
                    String number1 = String.join("", number_list1);
                    f = Float.parseFloat(number1.toString());
            
                    x1List.add(f);

                    // read the next character because it is a ","
                    c = br.read();
                    st = Character.toString((char)c);

                    while(c != 93){
                        number_list2.add(st);
                        c = br.read();
                        st = Character.toString((char)c);
                    }
                    
                    // join a list of strings into a single string and parse it to float
                    String number2 = String.join("", number_list2);
                    f = Float.parseFloat(number2.toString());

                    x2List.add(f);

                }

            }
            br.close();
            reader.getList().add(x1List);
            reader.getList().add(x2List);
            System.out.println(reader.getList());

        }
        catch (IOException e){
            e.printStackTrace();
        }

        

    }
}
