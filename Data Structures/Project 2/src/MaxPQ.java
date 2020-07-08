public class MaxPQ {
  private Disk[] pq;
  private int N;

  public boolean less(int i, int j) {
    return pq[i].compareTo(pq[j])<=0;
  }

  public MaxPQ(int maxN) {
    pq = new Disk[maxN+1];
    N = 0;
  }

  public void insert(Disk disk){
      pq[++N] = disk;
      swim(N);
  }

  public Disk getmax() {
    swap(1,N);
    sink(1,N-1);
    return pq[N--];
  }

  public void swap(int i, int j) {
    Disk t = pq[i];
    pq[i] = pq[j];
    pq[j] = t;
  }

  private void sink(int k, int N) {
    while(2*k <= N) {
      int j = 2*k;
      if(j<N && less(j, j+1)) {
        j++;
      }
      if(!less(k, j)) {
        break;
      }
      swap(k, j);
      k = j;
    }
  }

  private void swim(int k){
      while (k > 1 && less(k/2,k)){
        swap(k,k/2);
        k = k/2;
      }

  }

  public boolean isEmpty() {
    return N == 0;
  }

  public int size() {
    return N;
  }
}
