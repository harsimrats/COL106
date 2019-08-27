import java.io.FileOutputStream;
import java.io.PrintStream;

public class Exchange
{
	queue temp = new queue();
	static node buyf = null;
	static node buyr = null;
	static node sellf = null;
	static node sellr = null;
	
	public synchronized void makefile(String fname,String data)
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
	
	void tofile(node x)
	{
		String data;
		data = String.valueOf(System.currentTimeMillis()-stock.tstart) + " " + String.valueOf(x.to) + " " + String.valueOf(x.name) + " " + String.valueOf(x.texp)+ " " + String.valueOf(x.type)+ " " + String.valueOf(x.qty1)+ " " + String.valueOf(x.stck)+ " " + String.valueOf(x.price) + " ";
		if(x.partial == true)
			data = data + "T";
		if(x.partial == false)
			data = data + "F";
		makefile("Cleanup.txt", data);
	}
	
	node algo(node fr, node rr, node ch)
	{
		node match = null;
		node ptr = fr;
		if(ch.partial == true)
		{
			while(ptr!=null)
			{	
				if(ptr.stck.equals(ch.stck) && ptr.price*ptr.qty>ch.price*ch.qty)
				{
					int maxprice=0,minto=0;
					if((ptr.qty <= ch.qty) || (ptr.qty > ch.qty && ptr.partial == true))
					{
						if(maxprice < ptr.price*ptr.qty)
						{
							maxprice = ptr.price*ptr.qty;
							match = ptr;
							minto = ptr.to;
						}
						if(maxprice == ptr.price*ptr.qty && minto > ptr.to)
						{
								minto = ptr.to;
								match = ptr;
						}	
					}
				}
				ptr = ptr.next;
			}
		}
		if(ch.partial != true)
		{
			ptr = fr;
			while(ptr!=null)
			{
				if(ptr.stck.equals(ch.stck) && ptr.price*ptr.qty>ch.price*ch.qty && ptr.qty == ch.qty)
				{
					match = ptr;
				}
				if(match!= null && ptr!=null && ch!=null && ptr.stck.equals(ch.stck) && match.price*match.qty == ptr.price*match.qty && match.to>ptr.to)
				{
					match = ptr;
				}
			}
		}
		return match;
	}
	
	public void match_orders()
	{
		node f = temp.getfront();
		node r = temp.getrear();
		node match;
		node t;
		int  flag1 =0;
		String data;
		while(f!=null && r!=null )
		{
			t = temp.dequeue();
			if(t != null)
			{
				if(t.texp*1000 > System.currentTimeMillis() - stock.tstart)
				{
					if(t.type.equals("buy"))
					{
					if(buyf == null && buyr == null && sellr == null && sellf == null)
					{
						buyf = t;
						buyr = t;
						data = "P " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " 0 " + " " + String.valueOf(t.to) + " " + String.valueOf(t.name) + " " + String.valueOf(t.texp)+ " " + String.valueOf(t.type)+ " " + String.valueOf(t.qty1)+ " " + String.valueOf(t.stck)+ " " + String.valueOf(t.price) + " " ;
						if(t.partial == true)
							data = data + "T";
						if(t.partial == false)
							data = data + "F";
						makefile("Exchange.txt",data);
					}
					else if(sellr == null && sellf == null)
					{
						buyr.next = t;
						buyr = t;
						data = "P " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " 0 " + " " + String.valueOf(t.to) + " " + String.valueOf(t.name) + " " + String.valueOf(t.texp)+ " " + String.valueOf(t.type)+ " " + String.valueOf(t.qty1)+ " " + String.valueOf(t.stck)+ " " + String.valueOf(t.price) + " " ;
						if(t.partial == true)
							data = data + "T";
						if(t.partial == false)
							data = data + "F";
						makefile("Exchange.txt",data);
					}
					
					else
					{
						match = algo(sellf,sellr,t);
						if(match!= null)
						{
							flag1 = 1;
							if(t.qty<match.qty)
							{
								checker.profit = checker.profit + match.price*match.qty - t.price*t.qty;
								match.qty -= t.qty;
								t.qty = 0;
							}
							if(t.qty > match.qty)
							{
								checker.profit = checker.profit - match.price*match.qty + t.price*t.qty;
								t.qty -= match.qty;
								match.qty = 0;
							}
							if(t.qty == match.qty)
							{
								checker.profit = checker.profit + match.price*match.qty - t.price*t.qty;
								t.qty = 0;
								match.qty = 0;
							}
						}	
						
						if(match != null)
						{
							if(match.qty<match.qty1)
							{
								data = "T " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " " + String.valueOf(match.qty-t.qty>0 ? match.qty-t.qty : t.qty-match.qty)+ String.valueOf(match.to) + " " + String.valueOf(match.name) + " " + String.valueOf(match.texp)+ " " + String.valueOf(match.type)+ " " + String.valueOf(match.qty1)+ " " + String.valueOf(match.stck)+ " " + String.valueOf(match.price) + " ";
								if(match.partial == true)
									data = data + "T";
								else if(match.partial == false)
									data = data + "F";
							}	
						}

						if(match == null || t.qty>0)
						{
							if(buyr!=null && buyf!=null)
							{
								buyr.next = t;
								buyr = t;
							}	
							else
							{
								buyr= t;
								buyf=t;
							}
								
							if(flag1 == 0)
							{
								data = "P " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " 0 " + " " + String.valueOf(t.to) + " " + String.valueOf(t.name) + " " + String.valueOf(t.texp)+ " " + String.valueOf(t.type)+ " " + String.valueOf(t.qty1)+ " " + String.valueOf(t.stck)+ " " + String.valueOf(t.price) + " ";
								if(t.partial == true)
									data = data + "T";
								else if(t.partial == false)
									data = data + "F";
								makefile("Exchange.txt",data);
							}
							if(flag1 == 1)
							{
								flag1 = 0;
								data = "T " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " " + String.valueOf(match.qty-t.qty>0 ? match.qty-t.qty : t.qty-match.qty)+ String.valueOf(t.to) + " " + String.valueOf(t.name) + " " + String.valueOf(t.texp)+ " " + String.valueOf(t.type)+ " " + String.valueOf(t.qty1)+ " " + String.valueOf(t.stck)+ " " + String.valueOf(t.price) + " ";
								if(t.partial == true)
									data = data + "T";
								else if(t.partial == false)
									data = data + "F";
								makefile("Exchange.txt",data);
							}
						}
						if(t.qty == 0 )
						{
							data = "T " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " " + String.valueOf(match.qty-t.qty>0 ? match.qty-t.qty : t.qty-match.qty)+ String.valueOf(t.to) + " " + String.valueOf(t.name) + " " + String.valueOf(t.texp)+ " " + String.valueOf(t.type)+ " " + String.valueOf(t.qty1)+ " " + String.valueOf(t.stck)+ " " + String.valueOf(t.price) + " ";
							if(t.partial == true)
							data = data + "T";
							else if(t.partial == false)
								data = data + "F";
							makefile("Exchange.txt",data);
						}
					}	
				}
				if(t.type.equals("sell"))
				{
					if(buyf == null && buyr == null && sellr == null && sellf == null)
					{
						sellf = t;
						sellr = t;
						data = "S " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " 0 " + " " + String.valueOf(t.to) + " " + String.valueOf(t.name) + " " + String.valueOf(t.texp)+ " " + String.valueOf(t.type)+ " " + String.valueOf(t.qty1)+ " " + String.valueOf(t.stck)+ " " + String.valueOf(t.price) + " " ;
						if(t.partial == true)
							data = data + "T";
						if(t.partial == false)
							data = data + "F";
						makefile("Exchange.txt",data);
					}
					
					else if(buyf == null && buyr == null)
					{
						sellr.next = t;
						sellr = t;
						data = "S " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " 0 " + " " + String.valueOf(t.to) + " " + String.valueOf(t.name) + " " + String.valueOf(t.texp)+ " " + String.valueOf(t.type)+ " " + String.valueOf(t.qty1)+ " " + String.valueOf(t.stck)+ " " + String.valueOf(t.price) + " " ;
						if(t.partial == true)
							data = data + "T";
						if(t.partial == false)
							data = data + "F";
						makefile("Exchange.txt",data);
					}
					
					else
					{
						match = algo(buyf,buyr,t);
						if(match!= null)
						{	
							flag1 = 1;
							if(t.qty<match.qty)
							{
								checker.profit = checker.profit + match.price*match.qty - t.price*t.qty;
								match.qty -= t.qty;
								t.qty = 0;
							}
							if(t.qty > match.qty)
							{
								checker.profit = checker.profit - match.price*match.qty + t.price*t.qty;
								t.qty -= match.qty;
								match.qty = 0;
							}
							if(t.qty == match.qty)
							{
								checker.profit = checker.profit + match.price*match.qty - t.price*t.qty;
								t.qty = 0;
								match.qty = 0;
							}
						}	
						if(match != null)
						{
							if(match.qty<match.qty1)
							{
								data = "T " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " " + String.valueOf(match.qty-t.qty>0 ? match.qty-t.qty : t.qty-match.qty)+ String.valueOf(match.to) + " " + String.valueOf(match.name) + " " + String.valueOf(match.texp)+ " " + String.valueOf(match.type)+ " " + String.valueOf(match.qty1)+ " " + String.valueOf(match.stck)+ " " + String.valueOf(match.price) + " ";
								if(match.partial == true)
									data = data + "T";
								else if(match.partial == false)
									data = data + "F";
							}
						}
						if(match == null || t.qty>0)
						{
							if(sellr!=null && sellf!=null)
							{
								sellr.next = t;
								sellr = t;
							}	
							else
							{
								sellr = t;
								sellf = t;
							}
							
							if(flag1 == 0)
							{
								data = "S " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " 0 " + " " + String.valueOf(t.to) + " " + String.valueOf(t.name) + " " + String.valueOf(t.texp)+ " " + String.valueOf(t.type)+ " " + String.valueOf(t.qty1)+ " " + String.valueOf(t.stck)+ " " + String.valueOf(t.price) + " " ;
								if(t.partial == true)
									data = data + "T";
								if(t.partial == false)
									data = data + "F";
								makefile("Exchange.txt",data);
							}
							if(flag1 == 1)
							{
								flag1 = 0;
								data = "T " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " " + String.valueOf(match.qty-t.qty>0 ? match.qty-t.qty : t.qty-match.qty)+ String.valueOf(t.to) + " " + String.valueOf(t.name) + " " + String.valueOf(t.texp)+ " " + String.valueOf(t.type)+ " " + String.valueOf(t.qty1)+ " " + String.valueOf(t.stck)+ " " + String.valueOf(t.price) + " ";
								if(t.partial == true)
									data = data + "T";
								if(t.partial == false)
									data = data + "F";
								data = "S " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " " + String.valueOf(match.qty-t.qty>0 ? match.qty-t.qty : t.qty-match.qty)+ String.valueOf(t.to) + " " + String.valueOf(t.name) + " " + String.valueOf(t.texp)+ " " + String.valueOf(t.type)+ " " + String.valueOf(t.qty1)+ " " + String.valueOf(t.stck)+ " " + String.valueOf(t.price) + " ";
								if(t.partial == true)
									data = data + "T";
								if(t.partial == false)
									data = data + "F";
								makefile("Exchange.txt",data);
							}
						}
					
						if(t.qty == 0 )
						{
							data = "T " + String.valueOf(System.currentTimeMillis()-stock.tstart) + " " + String.valueOf(match.qty-t.qty>0 ? match.qty-t.qty : t.qty-match.qty)+ String.valueOf(t.to) + " " + String.valueOf(t.name) + " " + String.valueOf(t.texp)+ " " + String.valueOf(t.type)+ " " + String.valueOf(t.qty1)+ " " + String.valueOf(t.stck)+ " " + String.valueOf(t.price) + " ";
							if(t.partial == true)
							data = data + "T";
							if(t.partial == false)
								data = data + "F";
							makefile("Exchange.txt",data);
						}
					}
				}
				}
			}
			f = temp.getfront();
			r = temp.getrear();
		}
		
		cleanup();
		
		if(test.count == checker.count && queue.front ==null)
		{
			String dat;
			dat = "Profit = " + String.valueOf(checker.profit); 
			makefile("Exchange.txt", dat);
		}
	}
	
	public void cleanup()
	{
		node ptr = buyf;
		if(ptr!= null)
		{
			if(ptr.texp*1000>System.currentTimeMillis() || ptr.qty<=0)
			{
				buyf = buyf.next;
				tofile(buyf);
			}
			if(ptr.next != null)
			{
				while(ptr.next!= null)
				{
					if(ptr.next.texp*1000>System.currentTimeMillis() || ptr.qty<=0)
					{
						ptr.next = ptr.next.next;
						tofile(ptr.next);
					}
				}
				if(ptr.texp*1000>System.currentTimeMillis() || ptr.qty<=0)
				{
					buyr = buyr.next;
					tofile(buyr);
				}
			}	
		}
		
		//SELL CLEAN
		node ptr1 = sellf;
		if(ptr1!=null)
		{
			if(ptr1.texp*1000>System.currentTimeMillis() || ptr1.qty<=0)
			{
				sellf = sellf.next;
				tofile(sellf);
			}
			if(ptr1.next != null)
			{
				while(ptr1.next!= null)
				{
					if(ptr1.next.texp*1000>System.currentTimeMillis() || ptr1.qty<=0)
					{
						ptr1.next = ptr1.next.next;
						tofile(ptr1.next);
					}
				}
				if(ptr1.texp*1000>System.currentTimeMillis() || ptr1.qty<=0)
				{
					sellr = sellr.next;
					tofile(sellr);
				}
			}	
		}
	}
}