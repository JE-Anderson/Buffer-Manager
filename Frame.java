import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Frame {
	
	//Fields
	int pin;
	boolean dirty;
	File page; //This is just used to set up the filewriter object and scanner objects when reading file to a string.
	String fileContents; //Used to store and update file contents while still in buffer, without disk I/O's.
	int pageNum;
	int frameNum;
	
	//Constructors
	public Frame()
	{
		this.pin = 0;
		this.dirty = false;
	}
	
	public Frame(int pin, boolean dirty, int pageNum, int frameNum, File file) throws Exception
	{
		this.pin = pin;
		this.dirty = dirty;
		this.pageNum = pageNum;
		this.frameNum = frameNum;
		this.page = file;
		this.fileContents = this.fileToString();
	}
	
	//Methods
	public int getPin()
	{
		return this.pin;
	}
	
	public void incPin()
	{
		this.pin++;
	}
	
	public void decPin()
	{
		this.pin--;
	}
	
	public boolean isDirty()
	{
		return this.dirty;
	}
	
	public void setDirty()
	{
		this.dirty = true;
	}
	
	//Displays text file (declared variable page) stored in the Frame object one line at a time.  Pulls from the buffered content, not the disk.
	public void displayPage() throws Exception
	{
		System.out.print(this.fileContents + "\n");
	}
	
	//Append a new line to the end of the text file (declared variable page) stored in the Frame object.  Only updates in buffer, does not write to disk.
	public void updatePage() throws Exception
	{
		this.displayPage();
		Scanner kb2 = new Scanner(System.in);
		System.out.println("Type in text to append to file:");
		String append = "\n" + kb2.nextLine();

		
		this.fileContents += append;
		this.setDirty();
	}
	
	//Helper for writing page to disk in BufMgr.
	public void closePage() throws Exception
	{
		FileWriter pageWriter = new FileWriter(this.page);
		pageWriter.write(this.fileContents);
		pageWriter.close();
	}
	
	//Extracts contents of .txt file as a string.
	public String fileToString() throws Exception
	{
		String res = "";
		Scanner fin = new Scanner(this.page);
		while(fin.hasNext())
			res += fin.nextLine() + "\n";
		fin.close();
		return res;
	}

}
