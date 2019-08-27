import java.util.*;
public class RoutingMapTree
{
	Exchange root = new Exchange(0);
	RoutingMapTree()
	{
		root.identifier = 0;
		root.parent = null;
	}
	RoutingMapTree(Exchange r)
	{
		root = r;
	}
	
	public Boolean containsNode(Exchange a)
	{
		boolean preret = false;
		Exchange temp = root;
		if(a.identifier == temp.identifier)
			return true;
		if(temp.numChildren() == 0)
			return false;
		for(int i=0;i<temp.numChildren();i++)
		{
			preret = preret || temp.subtree(i).containsNode(a);
		}
		return preret;
	}
	
	public Exchange getExchange(int i)
	{
		Exchange a = null,temp = root;
		if(temp.identifier == i)
			return temp;
		for(int x=0;x<temp.numChildren();x++)
		{
			a = temp.subtree(x).getExchange(i);
			if(a!= null)
				return a;
		}
		return null;
	}
	
	public void switchon(MobilePhone a, Exchange b)
	{
		if(containsNode(b))
		{
			Exchange temp = getExchange(b.identifier);
			if(temp.ismember(a.number()))
			{
				while(temp!=null)
				{
					for(int i=0;i<temp.mps.mys.list.size();i++)
					{
						MobilePhone x = (MobilePhone) temp.mps.mys.list.get(i);
						if(x.number() == a.number())
							x.switchon();
					}
					temp = temp.parent;
				}
			}
			else
			{
				a.base = temp;
				a.switchon();
				if(!temp.mps.IsMember(a.number()))
				{
					temp.mps.Insert(a);
					while(temp!= null)
					{
						temp.mps = temp.residentSet();
						temp = temp.Parent();
					}
				}
			}
		}
		else
			System.out.println("Exchange does not exist.");
	}
	
	public void switchoff(MobilePhone a)
	{
		if(root.ismember(a.number()))
		{
			for(int j=0;j<root.mps.mys.list.size();j++)
			{
				MobilePhone x = (MobilePhone) root.mps.mys.list.get(j);	
				if(x.number() == a.number())
				{
					try 
					{
						Exchange temp;
						temp = x.location();
						while(temp!=null)
						{
							for(int i=0;i<temp.mps.mys.list.size();i++)
							{
								MobilePhone x1 = (MobilePhone) temp.mps.mys.list.get(i);
								if(x1.number() == a.number())
								{
									x1.switchoff();
								}	
							}
							temp = temp.parent;
						}
					}
					catch (CustomException e)
					{}
				}	
			}
		}
			
		else 
			System.out.println("switchOffMobile " + a.number() + " : " + "Mobile Phone does not exist.");
	}
	
	public Exchange findPhone(MobilePhone m)
	{
		if(root.ismember(m.number()))
		{
			for(int j=0;j<root.mps.mys.list.size();j++)
			{
				MobilePhone x = (MobilePhone) root.mps.mys.list.get(j);	
				if(x!=null && x.Status() == true && x.number() == m.number())
				{
					try
					{
						return x.location();
					}
					catch (CustomException e)
					{}
				}/*
				else if(x.Status() == false && x.number() == m.number())
				{
					System.out.println("Mobile Phone " + m.number() + " is switched off.");
				}*/
			}
		}	
		return null;
	}
	
	public Exchange lowestRouter(Exchange a, Exchange b)
	{
		try
		{
			Exchange temp = a;
			if(temp.identifier == b.identifier)
				return temp;
			else
			{	
				while(temp!=null)
					{
					boolean check = belongs(temp,b);
					if(check == true)
						return temp;
					temp = temp.parent;
				}
			}
		}
		catch(Exception e){}
		return null;	
	}
	
	public boolean belongs(Exchange temp, Exchange b)
	{
		Exchange temp2 = temp;
		if(temp2.identifier == b.identifier)
			return true;
		for(int i=0,n=temp2.numChildren();i<n;i++)
		{
			boolean check = temp2.subtree(i).belongs(temp2.Child(i), b);
			if(check!=false)
				return true;
		}
		return false;
	}
	
	public ExchangeList routeCall(MobilePhone a,MobilePhone b) throws CustomException
	{
		ExchangeList temp = new ExchangeList ();
		Exchange ap = findPhone(a);
		if(ap!=null)
		{
			Exchange bp = findPhone(b);
			Exchange temp2 = ap;
			if(bp!=null)
			{	
				Exchange lr = lowestRouter(ap,bp);
				if(lr!= null)
				{
					while(temp2.identifier!=lr.identifier)
					{
						temp.children.add(temp2);
						temp2 = temp2.parent;
					}
					temp.children.add(temp2);
					int in = temp.children.indexOf(temp2);
					temp2 = bp;
					while(temp2.identifier!=lr.identifier)
					{
						temp.children.add(in+1, temp2);
						temp2 = temp2.parent;
					}
					return temp;
				}
			}
			else
			{
				System.out.println("Second Mobile Phone not available");
			}
		}
		else
		{
			System.out.println(" First Mobile Phone not available");
		}
		return null;
	}
	
	public void movePhone(MobilePhone a, Exchange b)
	{
		Exchange aa = findPhone(a);
		Exchange bb = getExchange(b.identifier);
		if(aa == null)
			System.out.println("movePhone " + a.number() + " " + b.identifier + " : " + "Mobile Phone not available");
		else
		{
			if(bb == null)
				System.out.println("movePhone " + a.number() + " " + b.identifier + " : " + "Exchange does not exist");
			else
			{
				for(int i=0;i<aa.mps.mys.list.size();i++)
				{
					MobilePhone temp = (MobilePhone) aa.mps.mys.list.get(i);
					if(temp.number() == a.number())
					{	
						if(temp.Status())
						{
							removeMobilePhone(temp);
							temp.base = bb;
							bb.mps.Insert(temp);
						}
						else
						{
							System.out.println("movePhone " + a.number() + " " + b.identifier + " : " + "MobilePhone is switched off");
						}
					}
				}
				while(bb!= null)
				{
					bb.mps = bb.residentSet();
					bb = bb.Parent();
				}
			}
		}
	}
	
	public void performaction(String actionMessage) throws CustomException
	{
		Scanner s = new Scanner (actionMessage);
		String input = s.next();
		if(input.equals("addExchange"))
		{
			try
			{
				int a = Integer.parseInt(s.next());
				int b = Integer.parseInt(s.next());
				addExchange(a,b);
			}
			catch (Exception e)
			{
				System.out.println(actionMessage + " : " + "Wrong Input");
			}
		}
		else if(input.equals("switchOnMobile"))
		{
			try
			{	
				int a = Integer.parseInt(s.next());
				int b = Integer.parseInt(s.next());
				MobilePhone x = new MobilePhone(a);
				Exchange y = new Exchange (b);
				switchon(x,y);
			}
			catch (Exception e)
			{
				System.out.println(actionMessage + " : " + "Wrong Input");
			}
		}
		else if(input.equals("switchOffMobile"))
		{
			int a = Integer.parseInt(s.next());
			MobilePhone x = new MobilePhone(a);
			switchoff(x);
		}
		else if(input.equals("queryNthChild"))
		{
			int a = Integer.parseInt(s.next());
			int b = Integer.parseInt(s.next());
			Exchange temp = getExchange(a);
			if(temp!=null)
			{
				if(temp.numChildren()<=b)
					System.out.println(actionMessage + " : " + "Wrong Input");
				else
				{
					Exchange x = temp.Child(b);
					System.out.println(actionMessage + " : " + x.identifier);
				}
			}
			else
				System.out.println(actionMessage + " : " + "Wrong Input");
		}
		else if(input.equals("queryMobilePhoneSet"))
		{
			int a = Integer.parseInt(s.next());
			Exchange temp = getExchange(a);
			if(temp!=null)
			{
				MobilePhoneset x = temp.residentSet();
				System.out.print(actionMessage + " : ");
				for(int i=0;i<x.mys.list.size();i++)
				{
					if(i==x.mys.list.size()-1)
					{
						MobilePhone t = (MobilePhone) x.mys.list.get(i);
						if(t.Status())
							System.out.print(t.number() + "\n");
					}
					else
					{
						MobilePhone t = (MobilePhone) x.mys.list.get(i);
						if(t.Status())
							System.out.print(t.number() + ", ");
					}	
				}
			}
		}
		else if(input.equals("lowestRouter"))
		{
			int x = Integer.parseInt(s.next());
			int y = Integer.parseInt(s.next());
			Exchange a = getExchange(x);
			if(a == null)
				System.out.println(actionMessage + " : " + "First Exchange do not exist.");
			else
			{
				Exchange b = getExchange(y);
				if(b == null)
					System.out.println(actionMessage + " : " + "Second Exchange do not exist.");
				else
				{
					Exchange c = lowestRouter(a,b);
					if(c == null)
						System.out.println("No such Exchange exist.");
					else
						System.out.println(actionMessage + " : " + c.identifier);
				}
			}
		}	
		else if(input.equals("findPhone"))
		{
			int y = Integer.parseInt(s.next());
			MobilePhone m = new MobilePhone(y);
			Exchange f = findPhone(m);
			if(f!=null)
				System.out.println(actionMessage + " : " + f.identifier);
			else
				System.out.println(actionMessage + " : " + "Mobile Phone " + m.number() + " not available");
		}
		else if(input.equals("findCallPath"))
		{
			System.out.print(actionMessage + " : ");
			int x = Integer.parseInt(s.next());
			int y = Integer.parseInt(s.next());
			MobilePhone a = new MobilePhone(x);
			MobilePhone b = new MobilePhone(y);
			ExchangeList l = routeCall(a,b);
			if(l!=null)
			{
				for(int i=0;i<l.children.size();i++)
				{
					if(i == l.children.size()-1)
						System.out.print(l.children.get(i).identifier + "\n");
					else
						System.out.print(l.children.get(i).identifier + ", ");
				}
			}
		}	
		else if(input.equals("movePhone"))
		{
			int x = Integer.parseInt(s.next());
			int y = Integer.parseInt(s.next());
			MobilePhone a = new MobilePhone(x);
			Exchange b = new Exchange(y);
			movePhone(a,b);
		}
		s.close();
	}
	
	public Exchange getroot()
	{
		return root;
	}
	
	public void addExchange(int a, int b) throws CustomException
	{
		Exchange temp = getExchange(a);
		Exchange temp2 = new Exchange(b);
		if(!containsNode(temp2))
		{
			if(temp != null)
			{
				temp.e.children.add(temp2);
				temp2.parent = temp;
			}
			else
			{
				System.out.println("addExchange " + a + " " + b + " Given Exchange does not exist");
			}
		}
		else
			System.out.println("addExchange " + a + " " + b + " Given Exchange already exist");
	}

	public void removeMobilePhone(MobilePhone m)
	{
		Exchange temp = root;
		temp.removemobile(m.number());
		if(temp.numChildren()==0)
			return;
		for(int i=0;i<temp.numChildren();i++)
		{
			if(temp.Child(i).ismember(m.number()))
				temp.subtree(i).removeMobilePhone(m);
		}	
	}
}