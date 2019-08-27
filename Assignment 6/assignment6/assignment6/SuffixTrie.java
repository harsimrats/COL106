import java.io.*;
import java.util.*;

public class SuffixTrie
{
	SuffixTree tree = new SuffixTree();
	int check_trie_built = 0;
	Vector <Integer> index;
	
	class SuffixTree
	{
		node root = new node();
		
		class node
		{
			String text;
			node parent;
			leafnode ln;
			LinkedList<node> children = new LinkedList<node>();
			
			node()
			{
				parent = null;
				ln = null;
			}
			
			node(String str)
			{
				text = str;
				parent = null;
				ln = null;
			}
		
			int numchildren()
			{
				return children.size();
			}
			
			node getchild(int i)
			{
				return children.get(i);
			}
		}
		
		class leafnode
		{
			int index;
			node parent;
		}

		void insert(String str, int index, node main_node)
		{
			int flag = 0;
			for(int i=0;i<main_node.numchildren();i++)
			{
				if(!str.equals(main_node.getchild(i).text))
				{
					String com = common(str,main_node.getchild(i).text);
					if(com!=null)
					{
						flag = 1;
						if(com.equals(main_node.getchild(i).text))
						{
							String rem = remaining(str, com);
							if(rem!=null)
								insert(rem,index ,main_node.getchild(i));
							else
							{
								node newnode = new node();
								newnode.text = remaining(str, com);
								newnode.parent = main_node.getchild(i);
								leafnode t = new leafnode();
								t.index = index;
								t.parent = newnode;
								newnode.ln = t;
								main_node.getchild(i).children.add(newnode);
								break;
							}
						}	
						else if(com.equals(str))
						{
							node temp = main_node.getchild(i);
							node newnode = new node(com);
							leafnode t = new leafnode();
							t.index = index;
							t.parent = newnode;
							newnode.ln = t;
							newnode.parent = main_node;
							main_node.children.remove(temp);
							temp.text = remaining(temp.text, com);
							temp.parent = newnode;
							main_node.children.add(temp);
							break;
						}
						else
						{
							node temp = main_node.getchild(i);
							String remain_w = remaining(str,com);
							String remain_t = remaining(temp.text,com);
							node newnode = new node(com);
							newnode.parent = main_node;
							main_node.children.remove(temp);
							temp.parent = newnode;
							temp.text = remain_t;
							newnode.children.add(temp);
						
							node newnode1 = new node(remain_w);
							newnode1.parent = newnode;
							leafnode t = new leafnode();
							t.index = index;
							t.parent = newnode1;
							newnode1.ln = t;
							newnode.children.add(newnode1);
							
							main_node.children.add(newnode);
							break;
						}
					}	
				}
				else
				{
					flag = 1;
					if(main_node.getchild(i).ln==null)
					{
						leafnode t = new leafnode();
						t.index = index;
						t.parent = main_node.getchild(i);
						main_node.getchild(i).ln = t;
					}
					break;
				}
			}
			if(flag == 0)
			{
				node temp = new node();
				temp.text = str;
				temp.parent = main_node;
				leafnode temp2 = new leafnode(); 
				temp2.index = index;
				temp2.parent = temp;
				temp.ln = temp2;
				main_node.children.add(temp);
			}
		}
		
		String remaining(String str1, String str2)
		{
			String str = null;
			for(int i=str2.length(); i<str1.length(); i++)
			{
				if(i==str2.length())
					str = String.valueOf(str1.charAt(i));
				else
					str = str + String.valueOf(str1.charAt(i));
			}
			return str;
		}
		
		String common(String str1, String str2)
		{
			String str = null;
			int first = 0;
			for(int i=0; i<Math.min(str1.length(),str2.length()); i++)
			{
				if(str1.charAt(i)==str2.charAt(i))
				{
					if(first == 0)
					{
						first = 1;
						str = String.valueOf(str1.charAt(i));
					}
					else
						str = str + String.valueOf(str1.charAt(i));
				}
				else
					break;
			}
			return str;
		}
	
		boolean substring(String str, node no)
		{
			for(int i=0,n=no.numchildren(); i<n; i++)
			{
				String temp = common(no.getchild(i).text,str);
				if(temp != null)
				{
					String rem = remaining(str,temp);
					if(rem == null)
						return true;
					else
					{
						boolean x = substring(rem, no.getchild(i));
						if(x==true)
							return x;	
					}
				}
			}
			return false;
		}
		
		void possubstrings(String str)
		{
			node temp = nodesubstring(str, root);
			if(temp!=null)
				recurssive_sub(temp);
		}
		
		void recurssive_sub(node temp)
		{
			if(temp.ln!=null)
				index.add(temp.ln.index);	
			for(int i=0, n=temp.numchildren(); i<n; i++)
			{
				recurssive_sub(temp.getchild(i));
			}
		}
		
		node nodesubstring(String str, node no)
		{
			for(int i=0,n=no.numchildren(); i<n; i++)
			{
				String temp = common(no.getchild(i).text,str);
				if(temp != null)
				{
					if(temp.equals(str))
						return no.getchild(i);
					else if(temp.equals(no.getchild(i).text))
						return nodesubstring(remaining(str,temp), no.getchild(i));
				}
			}
			return null;
		}
	
		void fuzzysubstring(String str, int num, node no)
		{
			if(num <= 0)
				possubstrings(str);
			
			else
			{
				for(int i=0 ; i< no.numchildren(); i++)
				{
					String com = common(str, no.getchild(i).text);
					
				}
			}
		}
	}
	
	public void performAction(String actionString)
	{
		Scanner s = new Scanner(actionString);
		String input = s.next();
		
		if(input.equals("makeSuffixTrie"))
		{
			String temp = s.next();
			FileInputStream fstream;
			try
			{
				fstream = new FileInputStream(temp);
				Scanner ss = new Scanner(fstream);
				String inp = ss.next();
				String[] words;
				while(ss.hasNext())
					inp += ss.nextLine();
				inp = inp.toLowerCase();
				words = new String[inp.length()];
				words[0] = String.valueOf(inp.charAt(inp.length()-1));
				for(int i=1,n=inp.length();i<n;i++)
					words[i] = inp.charAt(inp.length()-1-i) + words[i-1];
				for(int i=0,n=inp.length();i<n;i++)
					tree.insert(words[i],n-i-1,tree.root);
				ss.close();
				check_trie_built = 1;
			}
			catch (FileNotFoundException e)
			{
				check_trie_built = 0;
				System.out.println(actionString + " : File does not exist");
			}
			catch(NullPointerException n)
			{
				check_trie_built = 0;
				System.out.println(actionString + " : File is empty");
			}
		}
		
		else if(input.equals("isSubstring"))
		{
			if(check_trie_built == 0)
				System.out.println(actionString + " : Exception - Trie has not been built yet");
			else
			{
				String str = s.next();
				str = str.toLowerCase();
				if(tree.substring(str, tree.root))
					System.out.println(actionString + " : 1");
				else
					System.out.println(actionString + " : 0");
			}
		}
		
		else if(input.equals("numSubstrings"))
		{
			index = new Vector<Integer>();
			if(check_trie_built == 0)
				System.out.println(actionString + " : Exception - Trie has not been built yet");
			else
			{
				String str = s.next();
				str = str.toLowerCase();
				int overlap = Integer.parseInt(s.next());
				tree.possubstrings(str);
				if(overlap == 1)
					System.out.println(actionString + " : " + index.size());
				else if(overlap == 0)
				{
					Vector<Integer> no_overlap = new Vector<Integer>();
					int i=index.size()-2;
					no_overlap.add(index.elementAt(index.size()-1));
					while(i>=0)
					{
						if(index.elementAt(i)-index.elementAt(i+1)>str.length())
							no_overlap.add(index.elementAt(i));
						i--;
					}
					System.out.println(actionString + " : " + no_overlap.size());
				}
				else
					System.out.println(actionString + " : Wrong input");
			}
			index.clear();
		}
		
		else if(input.equals("posSubstrings"))
		{
			index = new Vector<Integer>();
			if(check_trie_built == 0)
				System.out.println(actionString + " : Exception - Trie has not been built yet");
			else
			{
				String str = s.next();
				str = str.toLowerCase();
				int overlap = Integer.parseInt(s.next());
				tree.possubstrings(str);
				if(overlap == 1)
				{
					System.out.print(actionString + " : ");
					for(int i=index.size()-1; i>0; i--)
							System.out.print(index.elementAt(i) + ", ");
					System.out.println(index.elementAt(0));
				}
				else if(overlap == 0)
				{
					Vector<Integer> no_overlap = new Vector<Integer>();
					int i=index.size()-2;
					no_overlap.add(index.elementAt(index.size()-1));
					while(i>=0)
					{
						if(index.elementAt(i)-index.elementAt(i+1)>str.length())
							no_overlap.add(index.elementAt(i));
						i--;
					}
					System.out.print(actionString + " : ");
					for(int j=no_overlap.size()-1; j>0; j--)
							System.out.print(no_overlap.elementAt(j) + ", ");
					System.out.println(no_overlap.elementAt(0));
				}
				else
					System.out.println(actionString + " : Wrong input");
			}
			index.clear();
		}
		
		else if(input.equals("numFuzzySubstrings"))
		{
			if(check_trie_built == 0)
				System.out.println(actionString + " : Exception - Trie has not been built yet");
			else
			{
				String str = s.next();
				int num = Integer.parseInt(s.next());
				int overflag = Integer.parseInt(s.next());
				if(num<str.length())
				{
					index = new Vector<Integer>();
					tree.fuzzysubstring(str,num,tree.root);
					if(index.size()>0)
					{
						if(overflag==0)
						{
							System.out.println(actionString + " : " + index.size());
						}
						else if(overflag==1)
						{
							Collections.sort(index);
							Vector<Integer> no_overlap = new Vector<Integer>();
							int i=1;
							no_overlap.add(index.elementAt(0));
							while(i<index.size())
							{
								if(index.elementAt(i)-index.elementAt(i-1)>str.length())
									no_overlap.add(index.elementAt(i));
								i++;
							}
							System.out.println(actionString + " : " + no_overlap.size());
						}
					}
					else
						System.out.println(actionString + " : No such string exist");
					index.clear();
				}	
				else
					System.out.println(actionString + " : Exception - 'num' is greater than size of string");
			}
		}
		
		else if(input.equals("posFuzzySubstrings"))
		{
			if(check_trie_built == 0)
				System.out.println(actionString + " : Exception - Trie has not been built yet");
			else
			{
				String str = s.next();
				int num = Integer.parseInt(s.next());
				int overflag = Integer.parseInt(s.next());
				if(num<str.length())
				{
					index = new Vector<Integer>();
					tree.fuzzysubstring(str,num,tree.root);
					if(index.size() != 0)
					{
						if(overflag==0)
						{
							Collections.sort(index);
							Vector<Integer> no_overlap = new Vector<Integer>();
							int i=index.size()-2;
							no_overlap.add(index.elementAt(index.size()-1));
							while(i>=0)
							{
								if(index.elementAt(i+1)-index.elementAt(i)>=str.length())
									no_overlap.add(index.elementAt(i));
								i--;
							}
							System.out.print(actionString + " : ");
							for(int j=no_overlap.size()-1; j>0; j--)
								System.out.print(no_overlap.elementAt(j) + ", ");
							System.out.println(no_overlap.elementAt(0));
						}
						else if(overflag==1)
						{
							System.out.print(actionString + " : ");
							for(int i=index.size()-1; i>0; i--)
								System.out.print(index.elementAt(i) + ", ");
							System.out.println(index.elementAt(0));
						}
					}
					else
						System.out.println(actionString + " : No such string exist");
					index.clear();
				}
				else
					System.out.println(actionString + " : Exception - 'num' is greater than size of string");
			}
		}
		s.close();
	}
}