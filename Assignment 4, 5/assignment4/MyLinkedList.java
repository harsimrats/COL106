public class MyLinkedList<X>
{
	private int counter;
	private Node head;
	
	public boolean isEmpty()
	{
		if(head == null)
			return true;
		return false;
	}
	
 	public void add(X data)
	{
		if (head == null)
			head = new Node(data);
	 
		Node Temp = new Node(data);
		Node Current = head;
	 
		if (Current != null)
		{
			while (Current.getNext() != null)
				 Current =  Current.getNext();
	 
			Current.setNext( Temp);
		}
		incrementCounter();
	}
	 
	private int getCounter()
	{
		return counter;
	}
	 
	private void incrementCounter()
	{
		counter++;
	}
	 
	private void decrementCounter()
	{
		counter--;
	}
	 
	public void add(X data, int index)
	{
		Node Temp = new Node(data);
		Node Current = head;
	 
		if (Current != null)
			for (int i = 0; i < index &&  Current.getNext() != null; i++)
				 Current =  Current.getNext();
	 
		Temp.setNext(Current.getNext());
		Current.setNext( Temp);
		incrementCounter();
	}
	 
	public X get(int index)
	{
		if (index < 0)
			return null;
		
		Node Current = null;
		
		if (head != null)
		{
			Current = head.getNext();
			for (int i = 0; i < index; i++)
			{
				if (Current.getNext() == null)
					return null;
				Current = Current.getNext();
			}
			return Current.getData();
		}
		return null;
	}
	 
	public boolean remove(int index)
	{
		if (index < 1 || index > size())
			return false;
	 
		Node Current = head;
		if (head != null)
		{
			for (int i = 0; i < index; i++)
			{
				if (Current.getNext() == null)
					return false;
				Current = Current.getNext();
			}
			Current.setNext(Current.getNext().getNext());
			decrementCounter();
			return true;
	 
		}
		return false;
	}
	 
	public int size()
	{
		return getCounter();
	}
	 
	public boolean contains(X data)
	{
		Node Current = head;
		while(Current != null)
		{
			if(Current.getData().equals(data))
				return true;
			Current = Current.getNext();
		}
		return false;
	}
	
	private class Node
	{
		private Node next;
		private X data;
	 
		public Node(X dataValue)
		{
			next = null;
			data = dataValue;
		}
	 
		@SuppressWarnings("unused")
		public Node(X dataValue, Node nextValue)
		{
			next = nextValue;
			data = dataValue;
		}
	 
		public X getData()
		{
			return data;
		}
	 
		@SuppressWarnings("unused")
		public void setData(X dataValue)
		{
			data = dataValue;
		}
	 
		public Node getNext()
		{
			return next;
		}
	 
		public void setNext(Node nextValue)
		{
			next = nextValue;
		}
	 
	}
}