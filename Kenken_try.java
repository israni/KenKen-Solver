import java.io.*;
import java.util.*;

public class Kenken_try {

    // ********************************************************** //
	// readfile - reads input.txt.
	// Creates a data structure called "problems".
	// "problems" is an ArrayList of hashmaps. Each "problem" is a hashmap.
	// Problem contains - "N", "conditions" (hashmap) and "group_id"
	// ********************************************************** //

	static List<HashMap> problems = new ArrayList<HashMap>();    
	static int size_kk = 0;
	static List<Integer> group_id = new ArrayList<Integer>(Collections.nCopies(16, 0));
	static long limit;

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
					//System.out.println(problem);
					problems.add(problem);
				}

				size_kk = Integer.valueOf(line);
				//System.out.println();
				//System.out.println(size_kk);
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
				//System.out.println(op);
				int result = Integer.valueOf(words[1]); 
				List condition = new ArrayList<>();
				
				// words contain index for conditions
				for (int i = 2; i < words.length; i++)
				{
					//System.out.println(words[i]);
					String[] x_y = words[i].split(",",2);
					int x = Integer.valueOf(x_y[0]);
					int y = Integer.valueOf(x_y[1]);
					int index = y + size_kk*(x-1) - 1;
					//System.out.println(index);
					condition.add(i-2,index);
					group_id.set(index, condition_num);
				}

				condition.add(0,result);
				condition.add(0,op);

				conditions.put(condition_num, condition);
				//System.out.println(op + " " + result + " " + indices);
				condition_num++;
				//op,result,indices
			}
			line_number++;
		}

		//System.out.println(Arrays.toString(group_id));
		problem.put("group_id", group_id);
		problem.put("conditions",conditions);
		//System.out.println(problem);
		problems.add(problem);
		//System.out.println(problems.size());

		br.close();
		System.out.println("File reading complete!");
	}


	private static boolean check_conditions(HashMap conditions)
	{
		Array 
		// inefficient check_conditions, checks all conditions in no intelligent order, can be improved a lot
		for(int condition_num =1; condition_num <= conditions.size(); condition_num++)
			{
				List condition = (ArrayList) conditions.get(condition_num);
				String op = (String) condition.get(0);
				int result = (Integer) condition.get(1);
				boolean result_check = true;

				if(op.equals("Constant"))
				{
					System.out.println("checking constant");	
					if (solution[(Integer)condition.get(2)] == result) result_check = result_check && true;
					else  result_check = result_check && false;
				}

				if (op.equals("Subtract"))
		    	{
		    		System.out.println("Subtracting");
		    	}

		    	if (op.equals("Multiply"))
		    	{
		    		System.out.println("Multiplying Values at indices : " );
		    		// int mult = 1;
		    		// for(int i=0;i<indices.size();i++)
		    		// 	mult *= Character.getNumericValue(num.charAt(indices.get(i)));       // we have the indices but need the number at these indices
		    		// System.out.println(mult);
		    		// if (mult == result) return true;
		    	}

		    	if (op.equals("Divide"))
		    	{
		    		System.out.println("Dividing");
		    	}

		    	if (op.equals("Add"))
		    	{
		    		System.out.println("Adding Values at indices : " );
		    		// int sum = 0;
		    		// for(int i=0;i<indices.size();i++)
		    		// 	sum += Character.getNumericValue(num.charAt(indices.get(i)));       // we have the indices but need the number at these indices
		    		// System.out.println(sum);
		    		// if (sum == result) return true;
		    	}
		    }

		return false;
	}



	private static void solve_using_approach_1(HashMap puzzle)
	{
		List group_id = (ArrayList) puzzle.get("group_id");
		int n = (Integer) puzzle.get("N"); 
		HashMap conditions = (HashMap) puzzle.get("conditions");
		boolean check = check_conditions(conditions);

		if (size_kk == 4) limit = 4294967296L;
		if (size_kk == 5) limit = 152587890625L;
		
		//**************** remove this!  Only for testing
		limit = 100L;
		// By now, I have read the input and saved the conditions.
		for ( long j = 0; j < limit; j++)   //4^16 combinations. can be 5^16 as well; size_kk^16;
		{
			// convert j to base 4/5
			// check condition
			// end if true
			
			String str_long = Long.toString(j,size_kk);
			str_long = String.format("%16s", str_long).replace(' ', '0');
			str_long = str_long.replace("4","5");
			str_long = str_long.replace("3","4");
			str_long = str_long.replace("2","3");
			str_long = str_long.replace("1","2");
			str_long = str_long.replace("0","1");
			//System.out.println(str_long); // converts to base 4 but 0,1,2,3 need to add 1
			
		}
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

		System.out.println(problems.size());
		
		// For each problem in problems,
		// Solve using approach 1,2,3,4,5

		for(int puzzle_num = 0; puzzle_num < problems.size(); puzzle_num++)
		{
			System.out.println("\n"+(puzzle_num+1));
			HashMap puzzle = problems.get(puzzle_num);
			
			// get required data from puzzle! Could be done in each approach
			// List group_id = (ArrayList) puzzle.get("group_id");
			int n = (Integer) puzzle.get("N"); 
			// HashMap conditions = (HashMap) puzzle.get("conditions");

			if (n==4)
				solve_using_approach_1(puzzle);
			//solve_using_approach_2(puzzle);
			//solve_using_approach_3(puzzle);
			//solve_using_approach_4(puzzle);
			//solve_using_approach_5(puzzle);
			

		}

    }
}