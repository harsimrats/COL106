import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class SearchEngine
{	
	InvertedPageIndex inverted;
	SearchEngine()
	{
		inverted = new InvertedPageIndex();
	}

	public void performAction(String actionMessage)
	{
		Scanner s = new Scanner (actionMessage);
		String input = s.next();
		if(input.equals("addPage"))
		{
			int flag = 0;
			String name = s.next();
			for(int i=0,n=inverted.page_entry.set.size();i<n;i++)
			{
				if(inverted.page_entry.set.get(i).equals(name))
					flag = 1;
			}
			if(flag == 0)
			{
				PageEntry temp = new PageEntry(name);
				if(temp.notfound == 0)
				{
					inverted.addPage(temp);
					for(int i=0;i<inverted.page_entry.set.size();i++)
					{
						if(inverted.page_entry.set.get(i).getpagename().equals(name))
						{
							for(int j=0;j<inverted.page_entry.set.get(i).getPageIndex().getWordEntries().size();j++)
								inverted.add(inverted.page_entry.set.get(i).getPageIndex().getWordEntries().get(j));
							break;
						}
					}
				}	
				else
					System.out.println(actionMessage + " : " + "Page does not exist");
			}
			else
				System.out.println(actionMessage + " : " + "Page already exist");
		}
		else if(input.equals("queryFindPagesWhichContainWord"))
		{
			String word = s.next();
			word = word.toLowerCase();
			if(word.equals("stacks") || word.equals("applications") ||word.equals("structures"))
				word = word.substring(0,word.length()-1);
			MySet<PageEntry> temp = inverted.getPagesWhichContainWord(word);
			if(temp!=null)
			{
				if(temp.set.size()>0)
				{
					System.out.print(actionMessage + " : " );
					for(int i=0,n=temp.set.size()-1 ; i<n ; i++)
					{
						PageEntry p = temp.set.get(i);
						System.out.print(p.getpagename() + " , ");
					}
					System.out.println(temp.set.get(temp.set.size()-1).getpagename());
				}
				else
					System.out.println(actionMessage + " : " + "No such page exist!!!");
			}
		}
		else if(input.equals("queryFindPositionsOfWordInAPage"))
		{
			String x = s.next();
			String y = s.next();
			MyLinkedList<Position> temp = inverted.FindPositionsOfWordInAPage(x,y);
			if(temp.size()>0)
			{
				System.out.print(actionMessage + " : ");
				for(int i=0,n=temp.size()-1;i<n;i++)
				{
					System.out.print(temp.get(i).getWordIndex() + " , ");
				}
				System.out.println(temp.get(temp.size()-1).getWordIndex());
			}
			else
				System.out.println(actionMessage + " : " + "Webpage " + y + " does not contain word " + x);
		}
		s.close();
	}
}

class Position
{
	private PageEntry p;
	private int wordIndex;
	
	Position(PageEntry pe, int wi)
	{
		p = pe;
		wordIndex = wi;
	}
	
	PageEntry getPageEntry()
	{
		return p;
	}
	
	int getWordIndex()
	{
		return wordIndex;
	}
}

class WordEntry
{
	private MyLinkedList<Position> position = new MyLinkedList<Position>();
	private String word;
	
	WordEntry(String new_word)
	{
		word = new_word;
	}
	
	void addPosition(Position p)
	{
		position.add(p);
	}
	
	void addPositions(MyLinkedList<Position> p)
	{
		for(int i=0,n=p.size();i<n;i++)
		{
			position.add(p.get(i));
		}	
	}
	
	MyLinkedList<Position> getAllPositionsForThisWord()
	{
		return position;	
	}
	
	String getword()
	{
		return word;
	}
}

class PageIndex
{
	private MyLinkedList<WordEntry> wordentry = new MyLinkedList<WordEntry>();
	
	void addPositionForWord(String str, Position p)
	{
		int flag = 0;
		for(int i=0, n=wordentry.size(); i<n; i++)
		{
			WordEntry temp = wordentry.get(i);
			if(temp.getword().equals(str))
			{
				flag = 1;
				temp.addPosition(p);
			}
		}
		if(flag == 0)
		{
			WordEntry temp = new WordEntry(str);
			temp.addPosition(p);
			wordentry.add(temp);
		}
	}
	
	MyLinkedList<WordEntry> getWordEntries()
	{
		return wordentry;
	}
}

class PageEntry
{
	private String pagename;
	private PageIndex pageindex = new PageIndex();
	int notfound = 0;
	
	PageEntry(String newpageName)
	{
		pagename = newpageName;
		readfile();
	}
	
	private void readfile()
	{
		try
		{
			int count = 0;
			FileInputStream fstream = new FileInputStream(pagename);
			Scanner s = new Scanner (fstream);
			while(s.hasNext())
			{
				count++;
				String temp = s.next();
				temp = temp.replace('{', ' ');
				temp = temp.replace('}', ' ');
				temp = temp.replace('[', ' ');
				temp = temp.replace(']', ' ');
				temp = temp.replace('<', ' ');
				temp = temp.replace('>', ' ');
				temp = temp.replace('=', ' ');
				temp = temp.replace('(', ' ');
				temp = temp.replace(')', ' ');
				temp = temp.replace('.', ' ');
				temp = temp.replace(',', ' ');
				temp = temp.replace(';', ' ');
				temp = temp.replace("'", " ");
				temp = temp.replace('"', ' ');
				temp = temp.replace('?', ' ');
				temp = temp.replace('#', ' ');
				temp = temp.replace('!', ' ');
				temp = temp.replace('-', ' ');
				temp = temp.replace(':', ' ');
				temp = temp.toLowerCase();
				Scanner scn = new Scanner(temp);
				while(scn.hasNext())
				{	
					temp = scn.next();
					if(temp.equals("stacks") || temp.equals("applications") ||temp.equals("structures"))
						temp = temp.substring(0,temp.length()-1);
					if(!temp.equals("a") && !temp.equals("an") && !temp.equals("the") && !temp.equals("they") && !temp.equals("these") && !temp.equals("this") && !temp.equals("for") && !temp.equals("is") && !temp.equals("are") && !temp.equals("was") && !temp.equals("of") && !temp.equals("or") && !temp.equals("and") && !temp.equals("does") && !temp.equals("will") && !temp.equals("whose"))
					{
						Position p = new Position(this,count);
						pageindex.addPositionForWord(temp, p);
					}
				}
				scn.close();
			}
			s.close();
		}
		catch (FileNotFoundException e)
		{
			notfound = 1;
		}
	}
	
	PageIndex getPageIndex()
	{
		return pageindex;
	}
	
	String getpagename()
	{
		return pagename;
	}
}

class InvertedPageIndex extends MyHashTable
{
	MySet<PageEntry> page_entry = new MySet<PageEntry>();
	
	void addPage(PageEntry p)
	{
		page_entry.addElement(p);
	}
	
	MySet<PageEntry> getPagesWhichContainWord(String str)
	{
		MySet<PageEntry> temp = new MySet<PageEntry>();
		if(table[getHashIndex(str)]!= null)
		{
			int key = getHashIndex(str),flag;
			LinkedHashEntry entry = table[key];
			for(int i=0;i<entry.ll.size();i++)
			{
				if(entry.ll.get(i).getword().equals(str))
				{
					MyLinkedList<Position> temp2 = entry.ll.get(i).getAllPositionsForThisWord();
					for(int j=0, n=temp2.size();j<n;j++)
					{
						flag = 0;
						for(int k = 0 ; k < j ; k++)
						{
							if(temp2.get(k).getPageEntry().getpagename().equals(temp2.get(j).getPageEntry().getpagename()))
								flag = 1;
						}
						if(flag == 0)
							temp.addElement(temp2.get(j).getPageEntry());
					}
					break;
				}	
			}
			return temp;
		}
		return null;
		
	}

	MyLinkedList<Position> FindPositionsOfWordInAPage(String x , String y)
	{
		MyLinkedList<Position> temp = new MyLinkedList<Position>();
		int key = getHashIndex(x);
		LinkedHashEntry entry = table[key];
		for(int i=0;i<entry.ll.size();i++)
		{
			if(entry.ll.get(i).getword().equals(x))
			{
				MyLinkedList<Position> temp2 = entry.ll.get(i).getAllPositionsForThisWord();
				for(int j=0 ,n=temp2.size();j<n;j++)
				{
					if(temp2.get(j).getPageEntry().getpagename().equals(y))
					{
						temp.add(temp2.get(j));
					}
				}
			}
		}
		return temp;
	}
}

class MyHashTable
{
	LinkedHashEntry[] table = new LinkedHashEntry[500];
	
	class LinkedHashEntry
	{
	    MyLinkedList<WordEntry> ll = new MyLinkedList<WordEntry>();
	} 
	
	MyHashTable()
	{
		for (int i = 0; i < 500; i++)
			table[i] = new LinkedHashEntry();
	}	
		    
	public void add(WordEntry w)
	{
		int key = getHashIndex(w.getword());
		int flag = 0, i;
		for(i=0;i<table[key].ll.size();i++)
		{
			if(table[key].ll.get(i).getword().equals(w.getword()))
			{
				flag=1;
				break;
			}
		}
		if(flag==1)
			table[key].ll.get(i).addPositions(w.getAllPositionsForThisWord());
		else
			table[key].ll.add(w);
	}
	
	int getHashIndex(String str)
	{
		int a;
		if(str.hashCode()>0)
			a = str.hashCode()%500;
		else
			a= -str.hashCode()%500;
		return a;
	}
	
	void addPositionsForWord(WordEntry w)
	{
		add(w);
	}
}