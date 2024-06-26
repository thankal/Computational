package exercise1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Reader {
    
    
    public static ArrayList<ArrayList<Double>> readFile(String filename){

        int c;
        String st;
        Double d;
        String line;

        // counter for the number of times the loop runs
        int counter = 0;
    
        ArrayList<Double> x1List = new ArrayList<Double>();
        ArrayList<Double> x2List = new ArrayList<Double>();
        ArrayList<ArrayList<Double>> xList = new ArrayList<ArrayList<Double>>();

        try{
            File file = new File(filename);

            BufferedReader br = new BufferedReader(new FileReader(file));  

            do {
                c = br.read();
                counter ++;
                // System.out.println(counter);

                st = Character.toString((char)c);
                
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

                    // join a list of strings into a single string and parse it to Double
                    String number1 = String.join("", number_list1);
                    d = Double.parseDouble(number1.toString());
            
                    x1List.add(d);

                    // read the next character because it is a ","
                    c = br.read();
                    st = Character.toString((char)c);

                    while(c != 93){
                        number_list2.add(st);
                        c = br.read();
                        st = Character.toString((char)c);
                    }
                    
                    // join a list of strings into a single string and parse it to Double
                    String number2 = String.join("", number_list2);
                    d = Double.parseDouble(number2.toString());

                    x2List.add(d);

                }

            } while (((line = br.readLine()) != null));
            br.close();
            xList.add(x1List);
            xList.add(x2List);
            // System.out.println(xList);

        }
        catch (IOException e){
            e.printStackTrace();
        }

        // System.out.println(xList);
        return xList;

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please choose a file to read(training_data.txt, experiment_data.txt): ");
        String type = sc.nextLine();
        readFile(type);

    }
}
