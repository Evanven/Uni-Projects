import java.io.*;
import java.nio.file.*;
import java.util.NoSuchElementException;
import java.util.Random;

public class comparison {

    static int[] stages = {100, 500, 1000};
    static Random rand = new Random();
    static String filename;
    static String content;

    static List<int[]> result_list = new List<>();
    static int[] temp_array;

    static int nor_sum_1 = 0;
    static int dec_sum_1 = 0;
    static int nor_sum_2 = 0;
    static int dec_sum_2 = 0;
    static int nor_sum_3 = 0;
    static int dec_sum_3 = 0;

    public static void compare() {
        Path path = null;
        try {
            path = Paths.get(System.getProperty("user.dir") + "\\data");
            Files.createDirectories(path);
        }
        catch (IOException e) {
            System.exit(2);
        }

        for(int stage: stages) {
            for (int rep = 0; rep < 10; rep++) {
                filename = path.normalize().toString() + "\\" + stage +"_"+ rep +".txt";

                try (FileWriter writer = new FileWriter(filename);
                     BufferedWriter bw = new BufferedWriter(writer)) {

                    for (int i = 1; i < stage; i++) {

                        content = String.valueOf(rand.nextInt(1000000));
                        bw.write(content + "\n");
                    }

                    content = String.valueOf(rand.nextInt(1000000));
                    bw.write(content);

                } catch (IOException e) {
                    System.err.format("IOException: %s%n", e);
                }

                File file = new File(filename);
                Greedy greedy = new Greedy(file.getAbsolutePath());
                Greedy_decreasing greedy_decreasing = new Greedy_decreasing(file.getAbsolutePath());

                switch(stage) {
                    case 100:
                        nor_sum_1 +=greedy.disk_heap.size();
                        dec_sum_1 +=greedy_decreasing.disk_heap.size();
                        break;
                    case 500:
                        nor_sum_2 +=greedy.disk_heap.size();
                        dec_sum_2 +=greedy_decreasing.disk_heap.size();
                        break;
                    case 1000:
                        nor_sum_3 +=greedy.disk_heap.size();
                        dec_sum_3 +=greedy_decreasing.disk_heap.size();
                        break;
                }
            }
        }

        temp_array = new int[2];
        temp_array[0] = nor_sum_1/10;
        temp_array[1] = dec_sum_1/10;
        result_list.insertAtBack(temp_array);

        temp_array = new int[2];
        temp_array[0] = nor_sum_2/10;
        temp_array[1] = dec_sum_2/10;
        result_list.insertAtBack(temp_array);

        temp_array = new int[2];
        temp_array[0] = nor_sum_3/10;
        temp_array[1] = dec_sum_3/10;
        result_list.insertAtBack(temp_array);
    }

	public static void Display() {
		Node<int[]> index = result_list.getHead();
		int i = 0;
        while(index != null) {
            System.out.println("Number of Folders: " + stages[i] + " - " + "Greedy: " + index.getData()[0] + ", " + "Greedy Decreasing: " + index.getData()[1]);
			      i += 1;

            try {
                index = index.getNext();
            }
            catch(NoSuchElementException e) {
                break;
            }
        }
	}

    public static void main(String[] args) {
      compare();
      Display();
    }
}
