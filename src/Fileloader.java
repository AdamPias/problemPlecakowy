import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Fileloader {
    public int capacity=0;
    public int length=0;
    public List<DataSet> dataSets = new ArrayList<DataSet>();

    public Fileloader(File file) throws FileNotFoundException {
        try (Scanner myReader = new Scanner(file)){
            while(myReader.hasNextLine()){
                String s = myReader.nextLine().trim();
                if(capacity == 0){
                    String replace = s.replace(",", "").replace("-", " ");
                    String[] split = replace.split("\\s+");
                    length = Integer.parseInt(split[1]);
                    capacity = Integer.parseInt(split[3]);
                }
                if(s.startsWith("sizes")){
                    dataSets.add(new DataSet());
                    String replace = s.replace("sizes = {", " ").replace("}", " ").replace(",", " ").trim();
                    String[] numbers = replace.split("\\s+");
                    int[] sizes = new int[numbers.length];
                    for (int i = 0; i < numbers.length; i++) {
                        sizes[i] = Integer.parseInt(numbers[i]);
                    }
                    dataSets.getLast().setSizes(sizes);
                }
                if(s.startsWith("vals")){
                    String replace = s.replace("{", " ").replace("}", " ").replace(",", " ").replace("vals", "").replace("="," ").trim();
                    String[] numbers = replace.split("\\s+");
                    int[] vals = new int[numbers.length];
                    for (int i = 0; i < numbers.length; i++) {
                        vals[i] = Integer.parseInt(numbers[i]);
                    }
                    dataSets.getLast().setVals(vals);
                }

            }
        }

    }
}
