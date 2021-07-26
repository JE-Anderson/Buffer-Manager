import java.util.LinkedList;

public class BufHashTbl {
	
	//Nested record class
	private class Record
	{
		//Nested Fields	
		private Integer key;
		private Integer value;
		
		//Nested Constructors
		public Record()
		{
			this.key = null;
			this.value = null;
		}
		
		public Record(Integer key, Integer value)
		{
			this.key = key;
			this.value = value;
		}
		
		//Record Equality method based only on the key
		public boolean equals(Object obj)
		{
			if(obj instanceof Record)
			{
				Record node = (Record)obj;
				return this.key.equals(node.key);
			}
			else
				return false;
		}
		
		public String toString() {
			return "Key: ["+this.key+"] Value: ["+this.value+"]";
		}
		
	}
	
	//Fields
	private int tableSize = 101;
	private int numElements;
	private LinkedList<Record>[] table;
	
	
	//Constructors
	public BufHashTbl(int realSize)
	{
		this.table = new LinkedList[realSize];
		this.numElements = 0;
	}
	
	public BufHashTbl()
	{
		this.table = new LinkedList[this.tableSize];
		this.numElements = 0;
	}
	
	
	//Methods
	private int hash(Integer key)
	{
		int result = 42;
		String inputString = key.toString().toLowerCase();
		char[] characters = inputString.toCharArray();
		for(int i = 0; i < characters.length; i++)
		{
			char cur = characters[i];
			int j = (int)cur;
			result += j;
		}		
		return result % this.table.length;
	}
	
	public void insert(Integer key, Integer value)
	{
		//If inserting a duplicate key, update the already existing key-value pair, so delete the old one
		if(this.contains(key))
			remove(key);
				
		//Find where new record should be inserted.
		int pos = this.hash(key);
		
		//Check if hash table entry is empty, if so, make new linked list, otherwise add new value to linked list that already exists
		if(this.table[pos] == null)
			this.table[pos] = new LinkedList<Record>();
		
		this.table[pos].add(new Record(key, value));
		this.numElements++;
	}
	
	public Integer lookup(Integer key)
	{
		Integer res = null;
		int pos = this.hash(key);
		int innerPos;
		
		Record node = new Record();
		node.key = key;	
		
		if(this.contains(node.key))
		{
			innerPos = this.table[pos].indexOf(node);
			res = this.table[pos].get(innerPos).value;
		}
		return res;
	}
	
	public boolean contains(Integer key)
	{
		boolean result = false;
		int hash = this.hash(key);
		
		if(this.table[hash] != null)
		{
			Record node = new Record();
			node.key = key;
			
			if(this.table[hash].indexOf(node) > -1)
				result = true;
		}
		return result;
	}
	
	public void remove(Integer key)
	{
		int pos = this.hash(key);
		
		if(this.table[pos] != null)
		{
			Record node = new Record();
			node.key = key;
			
			boolean removed = this.table[pos].remove(node);
			
			if(this.table[pos].size() == 0)
				this.table[pos] = null;
			if(removed)
				this.numElements--;
		}
	}
	
	//toString class for testing
	@Override
	public String toString() {
		String buffer = "";

		buffer += "{\n";
		for (int i = 0; i < this.table.length; i++) {
			if (this.table[i] != null) {
				buffer  = buffer +  "\t" + this.table[i] + "\n" ;
			}
		}
		buffer += "}";
		return buffer;
	}

}
