import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;

public class Greedy_decreasing {
    List<Integer> folder_list;
    MaxPQ disk_heap;
    double sum;


    public Greedy_decreasing(String filename) {
        sum = 0;

        folder_list = readFolders(filename);
        disk_heap = new MaxPQ(folder_list.size());

        algorithm(Sort.quickSort(folder_list), disk_heap);
    }

    public List<Integer> readFolders(String fileName) {
        folder_list = new List<>();
        String str;

        try{
            FileReader file = new FileReader(fileName);
            BufferedReader br = new BufferedReader(file);

            while ((str = br.readLine()) != null) {


                if (!(str.matches("^[0-9]$|^[0-9]{2}$|^[0-9]{3}$|^[0-9]{4}$|^[0-9]{5}$|^[0-9]{6}$") || str.equals("1000000"))) {
                    error(1);
                }

                sum += Integer.parseInt(str);
                folder_list.insertAtBack(Integer.parseInt(str));
            }
        }
        catch(IOException e){
            error(2);
        }
        return folder_list;
    }

    public void display() {

        System.out.println("Sum of all folders = "+ sum/1000000.0 +" TB");
        System.out.println("Total number of disks used = "+ disk_heap.size());

      if(folder_list.size() <= 100) {

          while(disk_heap.size()!=0) {
              System.out.println(disk_heap.getmax().toString());
          }
      }
    }

    public void algorithm(List<Integer> folder_list, MaxPQ disk_heap) {

      Node<Integer> folder = folder_list.getHead();
      int data;

      while(folder != null) {
          data = folder.getData();

          if(disk_heap.isEmpty()){
              Disk empty_disk = new Disk();

              empty_disk.used_space += data;
              empty_disk.folders.insertAtFront(data);

              disk_heap.insert(empty_disk);
          }
          else {
              Disk max_disk = disk_heap.getmax();

              if (data <= max_disk.getFreeSpace()) {
                max_disk.used_space += data;
                max_disk.folders.insertAtBack(data);
              }
              else {
                Disk empty_disk = new Disk();

                empty_disk.used_space += data;
                empty_disk.folders.insertAtBack(data);

                disk_heap.insert(empty_disk);
              }
              disk_heap.insert(max_disk);
            }

            try{
                folder = folder.getNext();
            }
            catch(NoSuchElementException e) {
                break;
            }
        }
    }

    public static void error(int code) {
        switch (code) {
            case 1: {
                System.out.println("Error: Wrong data given");
                break;
            }
            case 2: {
                System.out.println("Error: Cant Open .txt File");
                break;
            }
            case 3: {
                System.out.println("Error: No .txt File given");
                break;
            }
            default: {
                System.out.println("Error");
                break;
            }
        }
        System.exit(2);
    }

    public static void main(String[] args) {
        try {
            Greedy_decreasing greedy_decreasing = new Greedy_decreasing(args[0]);
            greedy_decreasing.display();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            error(3);
        }
    }
}
