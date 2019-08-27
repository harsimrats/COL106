public class Exchange
{
	MobilePhoneset mps = new MobilePhoneset();
	int identifier;
	Exchange parent;
	ExchangeList e = new ExchangeList();
	
	Exchange(int number)
	{
		identifier = number;
	}
	
	public Exchange Parent ()
	{
		return parent;
	}
	
	public int numChildren()
	{
		return e.children.size();
	}
	
	public Exchange Child(int i)
	{
		if(i>e.children.size())
		{
			System.out.println("ERROR!!!Number of children is less.");
			return null;
		}	
		else
		{
			Exchange child = e.children.get(i);
			return child;
		}
	}
	
	public boolean isroot()
	{
		if(identifier == 0)
			return true;
		return false;
	}
	
	public RoutingMapTree subtree(int i)
	{
		if(e.children.size() == 0)
			return null;
		else
		{
			RoutingMapTree sbt = new RoutingMapTree(Child(i));
			return sbt;
		}
	}
	
	public void removemobile(int id)
	{
		mps.delete(id);
	}
	
	public boolean ismember(int id)
	{
		return mps.IsMember(id);
	}
	
	public MobilePhoneset residentSet()
	{
		if(e.children.size() != 0)
		{
			for(int i=0;i<e.children.size();i++)
			{
				mps.mys = mps.mys.union(this.subtree(i).root.residentSet().mys);
			}
			return mps;
		}
		return mps;
	}
}