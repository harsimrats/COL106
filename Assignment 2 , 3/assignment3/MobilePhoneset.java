public class MobilePhoneset
{
	Myset mys = new Myset();
	
	public boolean IsEmpty()
	{
		return mys.IsEmpty();
	}
	
	public boolean IsMember(int id)
	{
		for(int i=0;i<mys.list.size();i++)
		{
			MobilePhone m = (MobilePhone) mys.list.get(i);
			if(m.number() == id)
		  		return true;
		}
		return false;
	}
	
	public void Insert(MobilePhone mp)
	{
		mys.Insert(mp);
	}
	
	public void delete(int id)
	{
		for(int i=0;i<mys.list.size();i++)
		{
			MobilePhone temp = (MobilePhone) mys.list.get(i);
			if(temp.number()==id)
				mys.delete(mys.list.get(i));
		}		
	}
}