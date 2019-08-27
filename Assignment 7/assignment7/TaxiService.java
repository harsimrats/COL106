import java.util.*;
public class TaxiService
{
	graph gr = new graph();;
	public TaxiService()
	{

	}

	public void performAction(String actionMessage)
	{
		Scanner s = new Scanner(actionMessage);
		String input = s.next();
		if(input.equals("edge"))
		{
			System.out.print(actionMessage + " : ");
			String src = s.next();
			String dst = s.next();
			int t = Integer.parseInt(s.next());
			gr.add_edge(src, dst, t);
		}
		
		else if(input.equals("taxi"))
		{
			System.out.print(actionMessage + " : ");
			String name = s.next();
			String pos = s.next();
			gr.add_taxi(name, pos);
		}
		
		else if(input.equals("customer"))
		{
			
		}
		
		else if(input.equals("printTaxiPosition"))
		{
			
		}
		s.close();
	}
}

class graph
{
	LinkedList<edge> edges = new LinkedList<edge>();
	LinkedList<taxi> cab = new LinkedList<taxi>();
	
	Map<String, Integer> temp;
	LinkedList<String> path;
	
	class edge
	{
		String from = null, to = null;
		private int time = 0;
		
		edge(String str1, String str2, int tim)
		{
			from = str1;
			to = str2;
			time = tim;
		}
		
		int time_taken()
		{
			return time;
		}
		
		String opposite(String temp)
		{
			if(from!=null && to!=null)
			{
				if(temp.equals(from))
					return to;
				else if(temp.equals(to))
					return from;
			}
			return null;
		}
	}
	
	class taxi
	{
		String name;
		String position;
		int availability = 0;
		
		taxi(String str)
		{
			name = str;
		}
	}
	
	void add_edge(String str1, String str2, int time)
	{
		int flag = 0;
		for(int i=0;i<edges.size();i++)
		{
			if(edges.get(i).from.equals(str1) && edges.get(i).to.equals(str2) && time==edges.get(i).time_taken())
			{
				flag = 1;
				break;
			}
			else if(edges.get(i).to.equals(str1) && edges.get(i).from.equals(str2) && time==edges.get(i).time_taken())
			{
				flag = 1;
				break;
			}
		}
		if(flag == 1)
		{
			System.out.println("Same edge already exist");
		}
		else
		{
			edge temp = new edge(str1,str2,time);
			edges.add(temp);
			System.out.println("Edge added successfully");
		}
	}

	public void add_taxi(String name, String pos)
	{
		int flag = 0;
		for(int i=0;i<cab.size();i++)
			if(cab.get(i).name.equals(name))
				flag = 1;
		if(flag == 0)
		{
			taxi temp = new taxi(name);
			temp.position = pos;
			cab.add(temp);
			System.out.println("Taxi added successfully");
		}
		else
			System.out.println("Taxi already exist.");
	}

	void short_path(String src, String dst)
	{
		temp = new HashMap<String, Integer>();
		path = new LinkedList<String>();
		temp.put(src, 0);
		give_value_(src);
		find_path(dst,src);
	}
	
	private void find_path(String dst, String src)
	{
		if(dst.equals(src))
			path.add(dst);
		else
		{
			for(int i=0,n=edges.size();i<n;i++)
			{
				if(edges.get(i).from.equals(dst) || edges.get(i).to.equals(dst))
				{
					int diff = temp.get(dst) - temp.get(edges.get(i).opposite(dst));
					if(edges.get(i).time_taken() <= diff)
					{
						find_path(edges.get(i).opposite(dst),src);
						path.add(dst);
					}
				}
			}
		}
	}

	private void give_value_(String str)
	{
		int min = Integer.MAX_VALUE;
		String str2 = null;
		for(int i=0,n=edges.size();i<n;i++)
		{
			if(edges.get(i).from.equals(str) || edges.get(i).to.equals(str))
				if(min>edges.get(i).time_taken())
				{
					min = edges.get(i).time_taken();
					str2 = edges.get(i).opposite(str);
				}
		}
		temp.put(str2, min);
		give_value_(str2);
	}
}