public class test implements Runnable
{
	stock sto = new stock();
	Exchange ex = new Exchange();
	public static int count = 0;
	public void run()
	{
		long time = 100000000000000000l;
		long time1 = time;
		if(Thread.currentThread().getName().equals("Order Thread"))
		{
			while(true)
			{
				if(count <= checker.count)
				{
					if(stock.flag2 == 1)
					{
						sto.input_queue();
						stock.flag2 = 0;
					}	
				}
				if(count > checker.count)
				{
					time = System.currentTimeMillis();
					break;
				}	
			}
		}
		
		if(Thread.currentThread().getName().equals("Exchange Thread"))
		{
			while(System.currentTimeMillis()<= time + 5000)
			{
				if(queue.front != null && queue.rear != null)
					ex.match_orders();
			}
			time1 = System.currentTimeMillis();
		}
		
		if(Thread.currentThread().getName().equals("Clean-up Thread"))
		{
			while(System.currentTimeMillis()<= time1 + 5000)
			{
					ex.cleanup();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {}
			}
		}
	}
}