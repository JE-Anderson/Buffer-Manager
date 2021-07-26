

public class BufMgrTester {

	//menu method - returns integer associated with selected option and second input integer
	public static int menu(BufMgr buffer)
	{
		int res = 0;
		
		System.out.println("Select from the following options, please enter a single integer only:");
		System.out.println("1. Create new pages.");
		System.out.println("2. Request a page.");
		System.out.println("3. Update a page.");
		System.out.println("4. Relinquish a page.");
		System.out.println("-1. Quit program.");
		
		res = Integer.parseInt(buffer.kb.nextLine());

		
		return res;
	}
	
	public static void createPages(BufMgr buffer) throws Exception
	{
		System.out.println("How many pages to create?");
		int num = Integer.parseInt(buffer.kb.nextLine());

		
		for(int i = 0; i < num; i++)
			buffer.createPage(i);

	}
	
	public static void requestPage(BufMgr buffer) throws Exception
	{
		System.out.println("What page number is requested?");
		int num = Integer.parseInt(buffer.kb.nextLine());

		
		buffer.pin(num);
		buffer.displayPage(num);

	}
	
	public static void updatePage(BufMgr buffer) throws Exception
	{
		System.out.println("What page number is to be updated?");
		int num = Integer.parseInt(buffer.kb.nextLine());
		
		buffer.updatePage(num);

	}
	
	public static void relinquishPage(BufMgr buffer)
	{
		System.out.println("What page is being relinquished?");
		int num = Integer.parseInt(buffer.kb.nextLine());
		
		buffer.unpin(num);

	}
	
	public static void main(String[] args) throws Exception {
		
		int arraySize = Integer.parseInt(args[0]);
		BufMgr buffer = new BufMgr(arraySize);
		int menu;
		
		do
		{
			menu = menu(buffer);
			
			if(menu == 1)
				createPages(buffer);
			else if(menu == 2)
				requestPage(buffer);
			else if(menu == 3)
				updatePage(buffer);
			else if(menu == 4)
				relinquishPage(buffer);
			
			
		}while(menu != -1);
		buffer.kb.close();
		

	}

	
	////////////Early Tests ///////////////
	//Testing of Frame class
	//Frame testFrame = new Frame(0, false, 1, 0, new File("test.txt"));
	//testFrame.displayPage();
	//testFrame.updatePage();
	//testFrame.displayPage();
	//testFrame.closePage();
	//PASS - frame class works as expected.
	
	//Testing of BufHshTbl class
	//BufHashTbl testTable = new BufHashTbl(10);
	//System.out.print(testTable.toString());
	
	//System.out.println("Inital hash table is: \n" + testTable);
	//testTable.insert(1, 2);
	//testTable.insert(2, 3);
	//System.out.println("After two adds, hashtalbe is: \n" + testTable);
	//testTable.insert(3, 4);
	//testTable.insert(4, 5);
	//System.out.println("After four adds, hashtalbe is: \n" + testTable);
	//so far so good.
	
	//System.out.println("The value associated with key 1 is:");
	//System.out.println(testTable.lookup(1));
	//PASS - BufHshTbl works as expected.
}
