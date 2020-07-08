import java.io.PrintStream;
import java.util.NoSuchElementException;

public class StringQueueWithOnePointer<T> implements StringQueue<T>{

	MyNode<T> pseudo = new MyNode("0", null);
	
	private MyNode<T> head;
	
	private int queuesize;
	
  public StringQueueWithOnePointer() {
		queuesize = 0;
	  
		head = pseudo;
		head.next = head;
	}
	
	public void put(T item){
		queuesize++;
		
		MyNode<T> dot = new MyNode(item, null);
		
		if(isEmpty()){
			head = dot;
			dot.next = dot;
		}
		else
		{
			dot.next = head.next;
			head.next = dot;
			head = dot;
		}
	}
	
	public T get() throws NoSuchElementException{
		if(isEmpty()){
			throw new NoSuchElementException();
		}	
		queuesize--;
		
		T v = head.next.getItem();
		head.next = head.next.next;
		return v;

	}
	
	public T peek() throws NoSuchElementException{
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		return head.next.getItem();
	}
	
	public boolean isEmpty(){
		return head == pseudo;
	}
	
	public void printQueue(PrintStream stream){
		if(isEmpty()){
        throw new NoSuchElementException();
		}
		
		MyNode<T> first = head.next;
		T item;
		
		while(first.next != head.next){
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