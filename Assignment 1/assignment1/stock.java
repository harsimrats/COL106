import java.util.*;
import java.io.*;

public class stock
{
	static int signal = 0 , flag2 = 0;
	static test ob1 = new test();
	Exchange e = new Exchange();
	queue temp2 = new queue();
	public int flag=0;
	public String input;
	public static long tstart = System.currentTimeMillis();

	public void performAction(String actionString)
	{
		while(true)
		{
			if(flag2 == 0)
			{
				flag2 = 1;
				input = actionString;
				break;
			}
		}
	}
	
	void threadini()
	{
		Thread order = new Thread (ob1);
		order.setName("Order Thread");
		order.start();
		Thread exchange = new Thread (ob1);
		exchange.setName("Exchange Thread");
		exchange.start();
		Thread cleanup = new Thread (ob1);
		cleanup.setName("Clean-up Thread");
		cleanup.start();
	}
	
	public void makefile(String fname,String data)
	{
		try
		{
			FileOutputStream fs = new FileOutputStream(fname,true);
			PrintStream p = new PrintStream(fs);
			p.println(data);
			p.close();
		}
		catch(Exception e){}
	}
	
	public void input_queue()
	{
		test.count++;
		node temp = new node();
		if(input != null)
		{
			Scanner s = new Scanner (input);
			long tend = System.currentTimeMillis();
			try
			{
				temp.to = Integer.parseInt(s.next());
				if(temp.to*1000>(-tstart+tend))
					Thread.sleep(temp.to*1000-(-tstart+tend));
				tend = System.currentTimeMillis();
				try
				{
					temp.name = s.next();
					temp.texp = Integer.parseInt(s.next());
					temp.type = s.next();
					temp.qty = Integer.parseInt(s.next());
					temp.stck = s.next();
					temp.price = Integer.parseInt(s.next());
					String sb = s.next();
					if(sb.equals("T"))
						temp.partial = true;
					if(sb.equals("F"))
						temp.partial = false;
					if(temp.texp<=0 || temp.qty<=0 || temp.price<=0)
					{
						String wt = "EXCEPTION " + String.valueOf((-tstart+tend)) + " " + input;
						makefile("Order.txt",wt);
					}
					if(temp.texp>0 && temp.qty>0 && temp.price>0)
					{
						temp.qty1 = temp.qty;
						temp2.enqueue(temp);
						String wt = String.valueOf((-tstart+tend)) + " " + input;
						makefile("Order.txt",wt);
					}
				}	
				catch(Exception e)
				{
					String wtex = "EXCEPTION " + String.valueOf((-tstart+tend)) + " " + input;
					makefile("Order.txt",wtex);
				}	
			}
			catch(Exception e)
			{
				String wtex = "EXCEPTION " + String.valueOf((-tstart+tend)) + " " + input;
				makefile("order.txt",wtex);
			}
			s.close();
		}
	}
}

class queue
{
	static node front = null;
	static node rear = null;
	
	public void enqueue(node temporary)
	{
		if(rear== null || front==null)
		{
			front = temporary;
			rear = front;
		}
		else
		{
			rear.next = temporary;
			rear = temporary;
		}
	}
	
	public node dequeue()
	{
		node temp = null;
		if(front!=null)
		{
			temp = front;
			front = front.next;
		}
		return temp;
	}
	
	public int isempty()
	{
		if(rear!= null && front!=null)
			return -1;
		return 0;
	}
	
	public node getfront()
	{
		return front; 
	}
	
	public node getrear()
	{
		return rear; 
	}
}

class node
{
	public int to;
	public String name;
	public int texp;
	public String type;
	public int qty;
	public String stck;
	public int price;
	public boolean partial = true;
	public int qty1 = qty;
	node next;
}