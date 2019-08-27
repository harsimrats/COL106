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
	
	public void switchon(MobilePhone a, Exchange b) throws CustomException
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
				else 
					throw new CustomException("Mobile Phone already exist.");
			}
		}
		else
			throw new CustomException("Exchange does not exist.");
	}
	
	public void switchoff(MobilePhone a) throws CustomException
	{
		if(root.ismember(a.number()))
		{
			for(int j=0;j<root.mps.mys.list.size();j++)
			{
				MobilePhone x = (MobilePhone) root.mps.mys.list.get(j);	
				if(x.number() == a.number())
				{
					Exchange temp = x.location();
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
			}
		}
			
		else 
			throw new CustomException("Mobile Phone does not exist.");
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
			try
			{	
				int a = Integer.parseInt(s.next());
				MobilePhone x = new MobilePhone(a);
				switchoff(x);
			}
			catch (CustomException e)
			{
				System.out.println(actionMessage + " : " + "Wrong Input");
			}
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
						System.out.print(t.number() + "\n");
					}
					else
					{
						MobilePhone t = (MobilePhone) x.mys.list.get(i);
						System.out.print(t.number() + ", ");
					}	
				}
			}
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
				throw new CustomException ("Given Exchange does not exist");
			}
		}
		else
			throw new CustomException ("Given Exchange already exist");
	}

	public void removeMobilePhone(int num)
	{
		Exchange temp = root;
		temp.removemobile(num);
		if(temp.numChildren()==0)
			return;
		for(int i=0;i<temp.numChildren();i++)
		{
			if(temp.Child(i).ismember(num))
				temp.subtree(i).removeMobilePhone(num);
		}	
	}
}