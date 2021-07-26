import java.util.LinkedList;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class BufMgr {
	
	//Fields
	LinkedList<Integer> LRU; //queue for LRU replacement.  Will only use addLast and removeFirst methods.  Contains queue integers representing frame numbers.
	BufHashTbl pageTable;
	int arraySize;
	Frame[] frameArray;
	int openIndex; //Keeps track of the next open index in frameArray.  if openIndex > frameArray.length - 1, then follow replacement policy
	Scanner kb;
	
	
	//Constructors
	public BufMgr()
	{
		this.LRU = new LinkedList<Integer>();
		this.pageTable = new BufHashTbl();
		this.arraySize = -1;
		this.frameArray = null;
		this.openIndex = 0;
		this.kb = new Scanner(System.in);
	}
	
	public BufMgr(int arraySize)
	{
		this.LRU = new LinkedList<Integer>();
		this.pageTable = new BufHashTbl();
		this.arraySize = arraySize;
		this.frameArray = new Frame[arraySize];
		this.openIndex = 0;
		this.kb = new Scanner(System.in);
	}
	
	public BufMgr(LinkedList<Integer> LRU, BufHashTbl pageTable, int arraySize)
	{
		this.LRU = LRU;
		this.pageTable = pageTable;
		this.frameArray = new Frame[arraySize];
		this.openIndex = 0;
		this.kb = new Scanner(System.in);
	}
	
	
	//Methods	
	//Create a new page/text file on the disk with page number "a" and contents "This is page a"
	public void createPage(int a) throws Exception
	{
		String aString = a + ".txt";
		File newFile = new File(aString);
		newFile.createNewFile();
		
		String content = "This is page " + a;
		FileWriter contentWriter = new FileWriter(newFile);
		contentWriter.write(content);
		contentWriter.close();
	}
	
	
	//I want page "pageNum" protocol.
	public Frame pin(int pageNum) throws Exception
	{
		int frameNum;
		Frame frame = null;
		
		//Check pageTable for page 'pageNum' and return frame if present.
		if(this.pageTable.contains(pageNum))
		{
			frameNum = this.pageTable.lookup(pageNum);
			frame = this.frameArray[frameNum];
			frame.incPin();
		}
		
		//If page not in buffer yet, read from disk into buffer.
		//LRU replacement, new frame creation, and frame insertion into frameArray and pageTable are managed by readPage() method.
		else
		{
			frameNum = readPage(pageNum);
			frame = this.frameArray[frameNum];
			frame.incPin();
		}
		
		return frame;
	}
		
	
	//Read from disk into buffer.  Pin incrementation and check for presence of page in buffer will happen in pin() method.
	//Need to make a frame, add it to an open buffer spot, and if no open slots, follow LRU replacement policy
	//Also need to add it to the pageTable.
	//returns index of the frame in frameArray the page has been added to.
	public int readPage(int pageNum) throws Exception
	{
		String fileName = pageNum + ".txt";
		File page = new File(fileName); //File to read into buffer.
		int frameNum; //index to insert frame into
		
		//check if buffer is full, if not, insert in first open slot and return index number.
		//If buffer not full - easier case, no replacement.
		if( this.openIndex < this.frameArray.length ) //i.e. if buffer is not full since open index is in of bounds of frameArray
		{
			//create new frame and insert into index this.openIndex in this.frameArray
			frameNum = this.openIndex;
			Frame frame = new Frame(0, false, pageNum, frameNum, page);
			this.frameArray[frameNum] = frame;
			this.pageTable.insert(pageNum, frameNum);
			this.openIndex++;
		}
		
		//If buffer full - execute replacement policy
		else
		{
			frameNum = this.LRU.removeFirst(); //first frame in LRU queue selected for replacement.
			this.pageTable.remove( this.frameArray[frameNum].pageNum ); //remove page to be replaced from pageTable.
			
			if(this.frameArray[frameNum].dirty)
				this.frameArray[frameNum].closePage(); //write contents back to disk prior to replacement if frame dirty.
			
			Frame frame = new Frame(0, false, pageNum, frameNum, page); //create new frame to insert
			this.frameArray[frameNum] = frame; //insert new frame
			this.pageTable.insert(pageNum, frameNum); //insert new frame into pageTable			
		}
		return frameNum;
	}
	
	
	//I am done with page PageNum protocol
	public void unpin(int pageNum)
	{
		int frameNum = this.pageTable.lookup(pageNum);
		Frame frame = this.frameArray[frameNum];
		frame.decPin();
		if(frame.getPin() == 0) //if pin == 0, add frame index to replacement queue
			this.LRU.addLast(frameNum);
	}
	
	//Writes contents of page 'a' from the corresponding frame to the disk.
	public void writePage(int a) throws Exception
	{
		int frameNum = this.pageTable.lookup(a);
		Frame frame = this.frameArray[frameNum];
		frame.closePage();
	}
	
	
	//Displays contents of page 'a' from the corresponding frame
	public void displayPage(int a) throws Exception
	{
		int frameNum = this.pageTable.lookup(a);
		Frame frame = this.frameArray[frameNum];
		frame.displayPage();
	}
	
	
	//Allows user to update page 'a' in its corresponding frame
	public void updatePage(int a) throws Exception
	{
		int frameNum = this.pageTable.lookup(a);
		Frame frame = this.frameArray[frameNum];
		frame.updatePage();
	}

}
