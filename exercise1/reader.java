package exercise1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {
    
    // final list of 2 lists, each with 4000 elements
    private ArrayList<ArrayList<Double>> xList = new ArrayList<ArrayList<Double>>();
    
    public static ArrayList<ArrayList<Double>> readFile(String type){

        int c;
        String st;
        Double d;
        String line;

        // counter for the number of times the loop runs
        int counter = 0;
    
        ArrayList<Double> x1List = new ArrayList<Double>();
        ArrayList<Double> x2List = new ArrayList<Double>();
        ArrayList<Double> categoryList = new ArrayList<Double>();
        ArrayList<ArrayList<Double>> xList = new ArrayList<ArrayList<Double>>();

        try{
            File file = new File(type);

            BufferedReader br = new BufferedReader(new FileReader(file));  

            while ((line = br.readLine()) != null) {
                c = br.read();
                counter ++;
                System.out.println(counter);

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

            }
            br.close();
            xList.add(x1List);
            xList.add(x2List);
            System.out.println(xList);

        }
        catch (IOException e){
            e.printStackTrace();
        }

        try{
            File file2 = new File("training_data.txt");
            BufferedReader br2 = new BufferedReader(new FileReader(file2));
            Double d2 = 0.0;

            line = br2.readLine();
            while ((line = br2.readLine()) != null){
                //read last 2 characters of the line and add them to the category list
                String category = line.substring(line.length() - 2);
                if (category.equals("1 ")){
                    d2 = 1.0;
                }
                else if (category.equals("2 ")){
                    d2 = 2.0;
                }
                else if (category.equals("3 ")){
                    d2 = 3.0;
                }
                System.out.println(category);
                categoryList.add(d2);
            }
            br2.close();
            
        }
        catch (Exception e){
            e.printStackTrace();
        }

        xList.add(categoryList);
        System.out.println(xList);
        return xList;

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please choose a file to read(training.txt, experiment.txt): ");
        String type = sc.nextLine();
        readFile(type);

    }
}
