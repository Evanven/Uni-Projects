public class Sort {
  static int[] folder_array;

  public static int partition(int[] array, int start, int end) {
      int pivot = array[end];
      int i = (start-1);

      for (int j=start; j<end; j++) {
        if (array[j] <= pivot) {
          int temp = array[++i];
          array[i] = array[j];
          array[j] = temp;
        }
      }

      int temp = array[i+1];
      array[i+1] = array[end];
      array[end] = temp;

      return i+1;
  }

  public static void sort(int[] array, int start, int end) {
    if (start < end) {
      int p = partition(array, start, end);

      sort(array, start, p - 1);
      sort(array, p + 1, end);
    }
  }

  public static List<Integer> quickSort(List<Integer> folder_list) {
    int size = folder_list.size();
    folder_array = new int[size];
    for (int i = 0; i < size; i++) {
      folder_array[i] = folder_list.removeFromBack();
    }

    sort(folder_array, 0, size-1);

    for (int i = 0; i < size; i++) {
      folder_list.insertAtFront(folder_array[i]);
    }
    return folder_list;
  }
}
