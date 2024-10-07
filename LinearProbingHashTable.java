
import java.util.Set;
import java.util.Scanner;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class LinearProbingHashTable<K, V>{
	private Entry<K, V>[] table;
	private int size;
	private int capacity;
	private Set set;
	private boolean rehashing = false;
	//Class for each node, key-value pair
	private static class Entry<K, V>{
		K key;
		V value;
		boolean deleted;
		
		public Entry(K key, V value){
			this.key = key;
			this.value= value;
			this.deleted = false;
		}
		
	}

	//default constructor 
	public LinearProbingHashTable() {
		this(7);
	}
	//Constructor for specified table capacity
	public LinearProbingHashTable(int capacity) {
		this.capacity = capacity;
		this.table= new Entry[capacity];
	}
	
	public boolean put(K key, V value) {
		//index generated based on key
		int index = key.hashCode() % capacity;
		
		//Find the open spot in the hash table using linear probing, if necessary
		while(table[index] != null && !table[index].deleted) {
			//Duplicate case
			if(table[index].value == value) return false;
			
			index++;
			if(index >= capacity) index = 0;
		}
		
		//Inserting the new entry
		if(table[index] == null) {
			table[index] = new Entry(key, value);
		}else {
			table[index].key = key;
			table[index].value = value;
			table[index].deleted = false;
		}
		if(!rehashing) size++;
		//Rehashing if necissary
		
		if(size - 1 >= capacity/2)
			rehash();
		
		return true;
	}
	
	//Delete function that makes the value for the key passed as deleted
	public boolean delete(K key) {
		int index = 0;
		boolean found = false;
		//As soon as the first instance of the key is found it will marked deleted
		while(index < capacity) {
			if(table[index] != null && table[index].key == key) {
				table[index].deleted = true;
				found = true;
				size--;
				break;
			}
			index++;
		}
		
		
		return found;
	}
	//Is empty helper method
	public boolean isEmpty() {
		return (size == 0);
	}
	//KeySet method returning the set of keys for the hash table
	public HashSet<K> keySet(){
		set = new HashSet<>(size);
		for(int i = 0; i < capacity; i++) {
			if(table[i] != null && !table[i].deleted) {
				set.add(table[i].key);
			}
		}
		return (HashSet<K>) set;
	}
	//A print key set method that automatically calls the key set method to construct the key set and then prints the values
	public void printKeySet() {
		this.keySet();
		if(set == null) return;
		
		Iterator<K> j = set.iterator();
		while(j.hasNext()) {
			System.out.print(j.next() + " ");
		}
		System.out.println();
	}
	
	//To string method that uses the StringBuilder class to output the hash table as a String
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < capacity; i ++) {
				str.append(i);
				if(table[i] != null) {
					str.append(" ");
					str.append(table[i].key.toString());
					str.append(", ");
					str.append(table[i].value.toString());
				}
				
				if(table[i] != null && table[i].deleted)
					str.append("   deleted");
				if(i != capacity - 1)
					str.append("\n");

			}
		
		return str.toString();
	}
	//Print method to print the string form of the hash table
	public void print() {
		System.out.println(toString());
	}
	
	//Rehash method for when the table becomes half full, it will rehash to the text prime number that is double the current capacity
	private void rehash() {
		rehashing = true;
		int oldCapacity = capacity;
		 
		this.capacity = newCapacity(capacity);
		
		int index;
		Entry<K, V> oldTable[] = table;
		table = new Entry[capacity];
		for(int i = 0; i < oldCapacity; i++) {
			if(oldTable[i] != null) {

				K key = oldTable[i].key;
				V value = oldTable[i].value;
				
				this.put(key, value);
			}
			
		}
		rehashing = false;
		
	}
	//Helper for finding a new capacity size that is a prime number that is at least double the size of the current capacity
	private static int newCapacity(int capacity) {
		int newCapacity = capacity * 2;
		while(!isPrime(newCapacity)) {
			newCapacity++;
		}
		return newCapacity;
	}
	
	//Helper for checking if a number above 7 is prime
	private static boolean isPrime(int x) {
		if(x % 2 == 0 || x % 3 == 0) 
			return false;
		for(int i = 5; i * i <= x; i += 6) {
			if(x % i == 0 || x % (i + 2) == 0)
				return false;
		}
		return true;
		
	}
	//Get method that simply returns the value where there is a key
	public V get(K key) {
		return table[key.hashCode() % capacity].value;
	}
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		LinearProbingHashTable<Integer, String> table = new LinearProbingHashTable<>();
		boolean quit = false;
		int key = 0;
		String value = "";
		char method;
		while (scanner.hasNextLine() && !quit) {
			String[] in = scanner.nextLine().trim().split(" ");
			if(in[0].isEmpty()) continue;
			method = in[0].charAt(0);
			if(in.length > 1) key = Integer.parseInt(in[1]);
			if(in.length > 2) value = in[2];
			
			switch(method) {
			case 'P':
				table.put(key, value);
				break;
			case 'G':
				System.out.println(table.get(key));
				break;
			case 'D':
				table.delete(key);
				break;
			case 'K':
				table.printKeySet();
				break;
			case 'R':
				System.out.print("Size " + table.capacity);
				table.rehash();
				System.out.println(" " + table.capacity);
				break;
			case 'S':
				table.print();
				break;
			case 'Q':
				quit = true;
				break;
			}
			
		}
		scanner.close();

	}
}
