public class MySet<X>
{
	MyLinkedList<X> set = new MyLinkedList<X>();
	
	void addElement(X element)
	{
		if(!set.contains(element))
			set.add(element);
	}
	
	MySet<X> Union(MySet<X> otherSet)
	{
		MySet<X> union = new MySet<X>();
		
		for(int i=0,n=set.size();i<n;i++)
			union.addElement(set.get(i));
		
		for(int i=0, n=otherSet.set.size();i<n;i++)
		{
			int flag = 0;
			for(int j=0;j<set.size();j++)
				if(otherSet.set.get(i).equals(set.get(j)))
					flag = 1;
			if(flag == 0)
				union.set.add(otherSet.set.get(i));
		}
		return union;
	}
	
	public MySet<X> Intersection(MySet<X> otherSet)
	{
		MySet<X> intersection = new MySet<X>();
		for(int i=0, n=otherSet.set.size();i<n;i++)
		{
			int flag = 0;
			for(int j=0, n2=otherSet.set.size();i<n2;j++)
			{
				if(otherSet.set.get(i).equals(otherSet.set.get(j)))
					flag = 1;
			}
			if(flag == 1)
				intersection.set.add(otherSet.set.get(i));
		}
		return intersection;
	}
}