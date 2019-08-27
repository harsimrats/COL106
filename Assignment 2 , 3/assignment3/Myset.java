import java.util.*;
public class Myset 
{
	LinkedList <Object> list = new LinkedList <Object> ();
	public boolean IsEmpty()
	{
		return list.isEmpty();
	}
	
	public boolean IsMember(Object o)
	{
		return list.contains(o);
	}
	
	public void Insert(Object o)
	{
		list.add(o);
	}
	
	public void delete(Object o)
	{
		list.remove(o);
	}
	
	public Myset union (Myset a)
	{
		Myset u = new Myset();
		for(int i=0;i<list.size();i++)
		{
			u.list.add(list.get(i));
		}
		for(int i=0;i<a.list.size();i++)
		{
			int flag = 0;
			for(int j=0;j<list.size();j++)
			{
				if(a.list.get(i).equals(list.get(j)))
					flag = 1;
			}
			if(flag == 0)
				u.list.add(a.list.get(i));
		}
		return u;
	}
	
	public Myset intersection(Myset a)
	{
		Myset i = new Myset();
		for(int x=0;x<a.list.size();x++)
		{
			int flag = 0;
			for(int j=0;j<list.size();j++)
			{
				if(a.list.get(x).equals(a.list.get(j)))
					flag = 1;
			}
			if(flag == 1)
				i.list.add(a.list.get(x));
		}
		return i;
	}
}