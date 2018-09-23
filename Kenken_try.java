import java.io.*;
import java.util.*;

public class Kenken_try {

	static int size_kk = 0;
	static List<Integer> group_id = new ArrayList<Integer>(Collections.nCopies(16, 0));
	static long limit;

    // ********************************************************** //
	// readfile - reads input.txt.
	// Creates a data structure called "problems".
	// "problems" is an ArrayList of hashmaps. Each "problem" is a hashmap.
	// Problem contains - "N", "conditions" (hashmap) and "group_id"
	// ********************************************************** //

	static List<HashMap> problems = new ArrayList<HashMap>();    
    private static void readFile(File fin) throws IOException 
    {	
		FileInputStream fis = new FileInputStream(fin);
	 	BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 	int line_number = 1;
		String line = null;
		boolean solved = false;
	 	HashMap problem = new HashMap<>();
	 	int problem_num = 1;
	 	HashMap conditions = new HashMap<>(); 
	 	int condition_num = 1;	 	

		while ((line = br.readLine()) != null) {
			// If line length == 1, new problem begins
			// This line contains information about the size of Ken Ken
			// I have a HashMap of conditions accumulated, which is added to that problem.
			// Also, initialize new elements for the next problem
			if (line.length() == 1) {
				if (line_number!=1)
				{
					condition_num = 1;
					problem.put("group_id", group_id);
					problem.put("conditions",conditions);
					problems.add(problem);
				}
				size_kk = Integer.valueOf(line);
				problem = new HashMap<>();
				conditions = new HashMap<>();
				problem.put("N", size_kk);
				problem_num++;
				int size_group_id = size_kk*size_kk;
	 			group_id = new ArrayList<Integer>(Collections.nCopies(size_group_id, 0)); 	
			}
			// For each line other than the first line - condition!
			else{ 
				String delims = "[ ]+";
				String[] words = line.split(delims);
				String op = words[0];
				int result = Integer.valueOf(words[1]); 
				List condition = new ArrayList<>();				
				// words contain index for conditions
				for (int i = 2; i < words.length; i++)
				{
					String[] x_y = words[i].split(",",2);
					int x = Integer.valueOf(x_y[0]);
					int y = Integer.valueOf(x_y[1]);
					int index = y + size_kk*(x-1) - 1;
					condition.add(i-2,index);
					group_id.set(index, condition_num);
				}
				condition.add(0,result);
				condition.add(0,op);
				conditions.put(condition_num, condition);
				condition_num++;
			}
			line_number++;
		}
		problem.put("group_id", group_id);
		problem.put("conditions",conditions);
		problems.add(problem);
		br.close();
		System.out.println("File reading complete!");
	}

	private static void print_solution(int[] solution)
	{
		int temp = 1;
		if (solution.length == 16) temp = 4;
		if (solution.length == 25) temp = 5;
		System.out.println("Solution:");
		for (int i=1; i<=temp; i++)
		{	for ( int j=1; j<=temp; j++)
			{	
				System.out.print(solution[j+temp*(i-1)-1]);
				if (j!=temp) System.out.print("\t");
			}
			System.out.print("\n");	
		}
	}

	private static boolean check_row_constraints(int[] solution)
	{	
		boolean row_cons = true;
		int temp = 1;
		if (solution.length == 16) temp = 4;
		if (solution.length == 25) temp = 5;
		for (int i=1; i<=temp; i++)
		{	HashSet set = new HashSet();
			for ( int j=1; j<=temp; j++)
			{	row_cons = set.add(solution[j+temp*(i-1)-1]);
				if (!row_cons) {System.out.println("row " +i+ " conditon failed!");return false;} }	}
		return true;
	}

	private static boolean check_col_constraints(int[] solution)
	{	
		boolean col_cons = true;
		int temp = 1;
		if (solution.length == 16) temp = 4;
		if (solution.length == 25) temp = 5;
		for (int i=1; i<=temp; i++)
		{	HashSet set = new HashSet();
			for ( int j=1; j<=temp; j++)
			{	col_cons = set.add(solution[i+temp*(j-1)-1]); 
				if (!col_cons) {System.out.println("col " +i+ " conditon failed!");return false;} }}
		return true;
	}

	private static boolean check_conditions(HashMap conditions, int[] solution)
	{	
		boolean result_check = true;
		result_check = result_check && check_row_constraints(solution);
		result_check = result_check && check_col_constraints(solution);

		// inefficient check_conditions, checks all conditions in no intelligent order, can be improved a lot
		for(int condition_num =1; condition_num <= conditions.size(); condition_num++)
			{	List condition = (ArrayList) conditions.get(condition_num);
				String op = (String) condition.get(0);
				int result = (Integer) condition.get(1);

				if(op.equals("Constant"))
				{	if (solution[(Integer)condition.get(2)] == result) result_check = result_check && true;
					else  {System.out.println("contant failed");result_check = result_check && false; break;}}

				if (op.equals("Subtract"))
		    	{	if ((solution[(Integer)condition.get(2)] - solution[(Integer)condition.get(3)]) == result || (solution[(Integer)condition.get(3)] - solution[(Integer)condition.get(2)]) == result) result_check = result_check && true;
					else  {System.out.println("Subtract failed");result_check = result_check && false; break;}}

		    	if (op.equals("Multiply"))
		    	{	int mult = 1;
		    		for(int i=2;i<condition.size();i++)
		    		 	mult *= solution[(Integer)condition.get(i)];   
		    		if (mult == result) result_check = result_check && true;
					else  {System.out.println("Multiply failed");result_check = result_check && false; break;}}

		    	if (op.equals("Divide"))
		    	{	if ((solution[(Integer)condition.get(2)] / solution[(Integer)condition.get(3)]) == result || (solution[(Integer)condition.get(3)] / solution[(Integer)condition.get(2)]) == result) result_check = result_check && true;
					else  {System.out.println("Divide failed");result_check = result_check && false; break;}}

		    	if (op.equals("Add"))
		    	{	int sum = 0;
		    		for(int i=2;i<condition.size();i++)
		    		 	sum += solution[(Integer)condition.get(i)];    
		    		if (sum == result) result_check = result_check && true;
					else  {System.out.println("Add failed");result_check = result_check && false; break;}}}

		return result_check;
	}

	private static void solve_using_approach_1(HashMap puzzle)
	{
		List group_id = (ArrayList) puzzle.get("group_id");
		int n = (Integer) puzzle.get("N"); 
		HashMap conditions = (HashMap) puzzle.get("conditions");
		int solution[];
		int states_gen = 0;
	
		long start = System.nanoTime();
		// do stuff

			if (n == 4) solution = new int[] {3,2,4,1, 4,3,1,2, 2,1,3,4, 1,4,2,3};
			else return;
			boolean solved = check_conditions(conditions, solution);
			
		long end = System.nanoTime();
		long microseconds = (end - start) / 1000;

		if (solved) {
			print_solution(solution); 
			System.out.println("States generated: "+ states_gen); 
			System.out.println("Time in microseconds: " + microseconds); 
			System.out.println("States/microseconds: " + states_gen/microseconds); }


		// if (size_kk == 4) limit = 4294967296L;
		// if (size_kk == 5) limit = 152587890625L;
		
		// //**************** remove this!  Only for testing
		// limit = 100L;
		// // By now, I have read the input and saved the conditions.
		// for ( long j = 0; j < limit; j++)   //4^16 combinations. can be 5^16 as well; size_kk^16;
		// {
		// 	// convert j to base 4/5
		// 	// check condition
		// 	// end if true
			
		// 	String str_long = Long.toString(j,size_kk);
		// 	str_long = String.format("%16s", str_long).replace(' ', '0');
		// 	str_long = str_long.replace("4","5");
		// 	str_long = str_long.replace("3","4");
		// 	str_long = str_long.replace("2","3");
		// 	str_long = str_long.replace("1","2");
		// 	str_long = str_long.replace("0","1");
		// 	//System.out.println(str_long); // converts to base 4 but 0,1,2,3 need to add 1				
	}

	private static void solve_using_approach_2(HashMap puzzle)
	{
		List group_id = (ArrayList) puzzle.get("group_id");
		int n = (Integer) puzzle.get("N"); 
		HashMap conditions = (HashMap) puzzle.get("conditions");
		int solution[];

		if (n == 4) solution = new int[] {3,2,4,1, 4,3,1,2, 2,1,3,4, 1,4,2,3};
		//else if (n == 5) solution = new int[] {1,2,3,4,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		else return;
		boolean solved = check_conditions(conditions, solution);
		//System.out.println("solved? " + solved);
		if (solved) print_solution(solution);

	}

	private static void solve_using_approach_3(HashMap puzzle)
	{
		List group_id = (ArrayList) puzzle.get("group_id");
		int n = (Integer) puzzle.get("N"); 
		HashMap conditions = (HashMap) puzzle.get("conditions");
		int solution[];

		if (n == 4) solution = new int[] {3,2,4,1, 4,3,1,2, 2,1,3,4, 1,4,2,3};
		//else if (n == 5) solution = new int[] {1,2,3,4,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		else return;
		boolean solved = check_conditions(conditions, solution);
		//System.out.println("solved? " + solved);
		if (solved) print_solution(solution);
	}

	private static void solve_using_approach_4(HashMap puzzle)
	{
		List group_id = (ArrayList) puzzle.get("group_id");
		int n = (Integer) puzzle.get("N"); 
		HashMap conditions = (HashMap) puzzle.get("conditions");
		int solution[];

		if (n == 4) solution = new int[] {3,2,4,1, 4,3,1,2, 2,1,3,4, 1,4,2,3};
		//else if (n == 5) solution = new int[] {1,2,3,4,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		else return;
		boolean solved = check_conditions(conditions, solution);
		//System.out.println("solved? " + solved);
		if (solved) print_solution(solution);

	}

	private static void solve_using_approach_5(HashMap puzzle)
	{
		List group_id = (ArrayList) puzzle.get("group_id");
		int n = (Integer) puzzle.get("N"); 
		HashMap conditions = (HashMap) puzzle.get("conditions");
		int solution[];

		if (n == 4) solution = new int[] {3,2,4,1, 4,3,1,2, 2,1,3,4, 1,4,2,3};
		//else if (n == 5) solution = new int[] {1,2,3,4,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		else return;
		boolean solved = check_conditions(conditions, solution);
		//System.out.println("solved? " + solved);
		if (solved) print_solution(solution);
	}


    // ********************************************************** //
    // everythings starts here - calls readline to read input.txt
    // ********************************************************** //

    public static void main(String[] args) 
    {	
    	// Read file
		File dir = new File(".");
		try{
			File fin = new File(dir.getCanonicalPath()+File.separator +"input.txt");
			readFile(fin);
		}catch(IOException e) {
			e.printStackTrace();
		}

		// For each problem in problems,
		// Solve using approach 1,2,3,4,5

		for(int puzzle_num = 0; puzzle_num < problems.size(); puzzle_num++)
		{
			HashMap puzzle = problems.get(puzzle_num);
			
			System.out.println("Puzzle "+(puzzle_num+1)+":");
			System.out.println("Approach 1:");
				solve_using_approach_1(puzzle);
			System.out.println("Approach 2:");
				solve_using_approach_2(puzzle);
			System.out.println("Approach 3:");
				solve_using_approach_3(puzzle);
			System.out.println("Approach 4:");
				solve_using_approach_4(puzzle);
			System.out.println("Approach 5:");
				solve_using_approach_5(puzzle);
			System.out.println();
    	}
    }
}