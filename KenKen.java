import java.io.*;
import java.util.*;

public class KenKen {

    // ********************************************************** //
	// readfile - reads input.txt and obtains relevant size of ken ken 
	// and adds conditions 
    // ********************************************************** //
	static Vector ops = new Vector();
	static Vector<Integer> results = new Vector<Integer>();
	static Vector<Vector<Integer>> indices_vec = new Vector<Vector<Integer>>(); 
	static int size_kk = 0;
	static long limit;

    private static void readFile(File fin) throws IOException {
	FileInputStream fis = new FileInputStream(fin);
 	BufferedReader br = new BufferedReader(new InputStreamReader(fis));
 	
	int line_number = 1;
	String line = null;
	boolean solved = false;

	while ((line = br.readLine()) != null) {

		// System.out.println(line);	
		// First line contains information about the size of Ken Ken
		if (line_number == 1) {
			size_kk = Integer.valueOf(line);
			System.out.println("Size of ken ken = " + size_kk);
		}

		// For each line other than the first line - condition!
		else{ 
			String delims = "[ ]+";
			String[] words = line.split(delims);
			String op = words[0];
			//System.out.println(op);
			int result = Integer.valueOf(words[1]); 
			Vector<Integer> indices = new Vector<Integer>();

			// words contain index for conditions
			for (int i = 2; i < words.length; i++)
			{
				//System.out.println(words[i]);
				String[] x_y = words[i].split(",",2);
				int x = Integer.valueOf(x_y[0]);
				int y = Integer.valueOf(x_y[1]);
				int index = x + size_kk*(y-1) - 1;
				//System.out.println(index);
				indices.add(i-2,index);
			}
			//System.out.println(op + " " + result + " " + indices);
			add_condition(op,result,indices);
		}
		line_number++;
	}
 
	br.close();
	System.out.println("File reading complete!");
	print_conditions();
    }


	// ********************************************************** //
    // saves conditions in vectors ops, results and vec<vec<indices>> 
    // ********************************************************** //
    private static void add_condition(String op,int result,Vector<Integer> indices)
    {
    	ops.add(op);
    	results.add(result);
    	indices_vec.add(indices);
    	//System.out.println(indices);	
    }

    private static void print_conditions()
    {
    	System.out.println(ops);
    	System.out.println(results);
    	for(int i=0;i<indices_vec.size();i++)
    	{
    		System.out.print(indices_vec.get(i));
    	}
    		
    }


    // ********************************************************** //
    // checks condition and returns true only if it is true! 
    // ********************************************************** //

    private static boolean check_condition(long num, String op,int result,Vector<Integer> indices)
    {

    	//System.out.println(op.length());
    	
    	if (op.equals("Subtract"))
    	{
    		System.out.println("Subtracting");
    	}
    	if (op.equals("Multiply"))
    	{
    		System.out.println("Multiplying");
    	}
    	if (op.equals("Divide"))
    	{
    		System.out.println("Dividing");
    	}
    	if (op.equals("Add"))
    	{
    		System.out.println("Adding");
    		int sum = 0;
    		for(int i=0;i<indices.size();i++)
    			sum += (indices.get(i));       // we have the indices but need the number at these indices
    		if (sum == result) return true;
    	}

    	return false;
    }

    // ********************************************************** //
    // everythings starts here - calls readline to read input.txt
    // ********************************************************** //

    public static void main(String[] args) 
    {
		File dir = new File(".");
		try{
			File fin = new File(dir.getCanonicalPath()+File.separator +"input.txt");
			readFile(fin);
		}catch(IOException e) {
			e.printStackTrace();
		}

		if (size_kk == 4) limit = 4294967296L;
		if (size_kk == 5) limit = 152587890625L;
		
		// By now, I have read the input and saved the conditions.
		for ( long j = 0; j < limit; j++)   //4^16 combinations. can be 5^16 as well; size_kk^16;
		{
			//convert j to base 4/5
			//check condition
			// end if true
			
			// does'nt work - we need to replace selective values 
			String str_long = Long.toString(j,size_kk);
			str_long = String.format("%16s", str_long).replace(' ', '0');
			str_long = str_long.replace("4","5");
			str_long = str_long.replace("3","4");
			str_long = str_long.replace("2","3");
			str_long = str_long.replace("1","2");
			str_long = str_long.replace("0","1");
			//System.out.println(str_long); // converts to base 4 but 0,1,2,3 need to add 1
		}

		// Write the required details in the output file
   }
}