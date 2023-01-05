package exercise1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
    
    // final list of 2 lists, each with 4000 elements
    private ArrayList<ArrayList<Float>> xList = new ArrayList<ArrayList<Float>>();
    
    public static ArrayList<ArrayList<Float>> readFile(){

        int c;
        String st;
        float f;
        String line;

        // counter for the number of times the loop runs
        int counter = 0;
    
        ArrayList<Float> x1List = new ArrayList<Float>();
        ArrayList<Float> x2List = new ArrayList<Float>();
        ArrayList<Float> categoryList = new ArrayList<Float>();
        ArrayList<ArrayList<Float>> xList = new ArrayList<ArrayList<Float>>();

        try{
            File file = new File("training_data.txt");
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
            float f2 = 0;

            line = br2.readLine();
            while ((line = br2.readLine()) != null){
                //read last 2 characters of the line and add them to the category list
                String category = line.substring(line.length() - 2);
                if (category.equals("1 ")){
                    f2 = 1.0f;
                }
                else if (category.equals("2 ")){
                    f2 = 2.0f;
                }
                else if (category.equals("3 ")){
                    f2 = 3.0f;
                }
                System.out.println(category);
                categoryList.add(f2);
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
        readFile();
    }
}
