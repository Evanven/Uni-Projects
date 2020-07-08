import java.io.PrintStream;
import java.util.NoSuchElementException;

public class StringQueueImpl<T> implements StringQueue<T> {

  MyNode<T> pseudo = new MyNode("0", null);

  private MyNode<T> head;
  private MyNode<T> tail;

  private int queuesize;

  public StringQueueImpl() {
      queuesize = 0;
      head = pseudo;
      tail = head;
  }

  public void put(T item){
    queuesize++;

    MyNode<T> dot = new MyNode(item, null);
    tail.next = dot;
    tail = dot;
  }

  public T get() throws NoSuchElementException{
    if(isEmpty()){
        throw new NoSuchElementException();
    }
    queuesize--;

    T v = head.next.getItem();
    head = head.next;
    return v;
  }

  public T peek() throws NoSuchElementException{
    if(isEmpty()){
        throw new NoSuchElementException();
    }
    return head.next.getItem();
  }

  public boolean isEmpty(){
    return head == tail;
  }

  public void printQueue(PrintStream stream){
    if(isEmpty()){
        throw new NoSuchElementException();
    }
    MyNode<T> first = head.next;
    T item;

    while(first.next != null){
      item = first.getItem();
      stream.print(item + ", ");

      first = first.next;
    }
    item = first.getItem();
    stream.println(item);

  }

  public int size(){
    return queuesize;
  }
}
