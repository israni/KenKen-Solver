import java.io.*;
import java.util.*;

public class KenKen {

    // ********************************************************** //
	// readfile - reads input.txt and obtains relevant size of ken ken 
	// and conditions 
    // ********************************************************** //

    private static boolean readFile(File fin) throws IOException {
	FileInputStream fis = new FileInputStream(fin);
 	BufferedReader br = new BufferedReader(new InputStreamReader(fis));
 	
	int line_number = 1;
	String line = null;
	int size_kk = 0;
	boolean solved = false;

	while ((line = br.readLine()) != null) {

		// System.out.println(line);	
		// First line contains information about the size of Ken Ken
		if (line_number == 1) {
			size_kk = Integer.valueOf(line);
			System.out.println("Size of ken ken = " + size_kk);
		}

		// For each line other than the first line - condition
		else{ 
			String delims = "[ ]+";
			String[] words = line.split(delims);
			String op = words[0];
			//System.out.println(op);
			int result = Integer.valueOf(words[1]); 
			Vector indices = new Vector();

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
			System.out.println(op + " " + result + " " + indices);
			boolean check = check_condition(op,result,indices);
			solved = solved && check;
			indices.clear();
		}
		line_number++;
	}
 
	br.close();
    return solved;
    }

    // ********************************************************** //
    // checks condition and returns true only if it is true! 
    // ********************************************************** //

    private static boolean check_condition(String op,int result,Vector indices)
    {

    	System.out.println(op.length());
    	
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
    	}

    	return false;
    }

    // ********************************************************** //
    // everythings starts here - calls readline to read input.txt
    // ********************************************************** //

    public static void main(String[] args) {
	File dir = new File(".");
	boolean problem_solved;
	try{
		File fin = new File(dir.getCanonicalPath()+File.separator +"input.txt");
		problem_solved = readFile(fin);
		System.out.println(problem_solved);	
	}catch(IOException e) {
		e.printStackTrace();
	}

    
   }
}