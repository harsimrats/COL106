import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class checker
{
	static int count=0;
	static int profit = 0;
	public static void main ( String args [])
	{
		BufferedReader br = null;
		BufferedReader x = null;
		stock r = new stock();
		
		try {
			x= new BufferedReader(new FileReader("input.txt"));

			while (x.readLine() != null)
			{
				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			String actionString;
			br = new BufferedReader(new FileReader("input.txt"));

			while ((actionString = br.readLine()) != null) {
				r.performAction(actionString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
