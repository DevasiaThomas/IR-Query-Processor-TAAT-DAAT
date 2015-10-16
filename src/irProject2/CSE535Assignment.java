package irProject2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class CSE535Assignment {
	public static LinkedList<Terms> copy(LinkedList<Terms> T1){
		LinkedList<Terms> T2=new LinkedList<Terms>();
		for(Terms t1:T1){
			Terms t2 = new Terms();
			t2.term=t1.term;
			t2.size=t1.size;
			for(Postings p1:t1.list){
				Postings p2 = new Postings();
				p2.dId=p1.dId;
				p2.tF=p1.tF;
				t2.list.add(p2);
			}
			T2.add(t2);
		}
		return T2;
	}
	public static HashMap<String,Integer> buildMap(LinkedList<Terms> T1){
		HashMap<String,Integer> h1 = new HashMap<String,Integer>();
		for(Terms t1:T1){
			h1.put(t1.term,T1.indexOf(t1));
		}
		return h1;
		}
		
	public static void display(LinkedList<Terms> T1){
		for(Terms t:T1){
			System.out.println(t.term+" "+t.size);
			for(Postings p:t.list){
				System.out.print(" "+p.dId+" "+p.tF+",");
			}
			System.out.print("\n");
		}
	}
	public static void displayString(String a[]){
		for (int i=0;i<a.length;i++){
			System.out.print(" "+a[i]);
			if(i!=a.length-1)
				System.out.print(",");
		}
		System.out.print("\n");
	}
	public static void displayList(ArrayList<Integer> a){
		Collections.sort(a);
		System.out.print("Result:");
		for(int i=0;i<a.size();i++){
			System.out.print(" "+a.get(i));
			if(i!=a.size()-1)
				System.out.print(",");
		}
		System.out.print("\n");
	}
	public static void sortDec(LinkedList<String> a,HashMap<String,Integer> h1,LinkedList<Terms> I1){
		Comparator<String> c = (a1,a2) -> (h1.get(a1)==null)?-1:((h1.get(a2)==null)?1:((I1.get(h1.get(a1)).size>I1.get(h1.get(a2)).size)?1:((I1.get(h1.get(a1)).size<I1.get(h1.get(a2)).size)?-1:0)));
		a.sort(c);
	}
	
	public static void getTopK(LinkedList<Terms> I1,int n){
		Comparator<Terms> c = (p1, p2) -> (p1.size<p2.size)?1:((p1.size>p2.size)?-1:0);
		I1.sort(c);
		System.out.println("FUNCTION: getTopK "+n);
		System.out.print("Result:");
		for(int i =0;i<10;i++){
			System.out.print(" "+I1.get(i).term);
			if(i!=9)
				System.out.print(",");
		}
		System.out.print("\n");
	}
	public static void getPostings(LinkedList<Terms> I1,LinkedList<Terms> I2, String Term){
		System.out.println("FUNCTION: getPostings "+Term);
		for(Terms t:I1){
			if(t.term.equals(Term)){
				int i = I1.indexOf(t);
				System.out.print("Ordered by doc IDs:");
				for(int j=0;j<t.size;j++){
					System.out.print(" "+t.list.get(j).dId);
					if(j!=t.size-1)
						System.out.print(",");
				}
				System.out.print("\n");
				System.out.print("Ordered by TF:");
				for(int j=0;j<I2.get(i).size;j++){
					System.out.print(" "+I2.get(i).list.get(j).dId);
					if(j!=t.size-1)
						System.out.print(",");
				}
				System.out.print("\n");
				return;
			}
				
		}
		System.out.println("term not found");
	}
	public static ArrayList<Integer> TaaTAnd(LinkedList<Terms> I1,HashMap<String,Integer>  h1,ArrayList<Integer> a1,String s, int flag, long ctr[]){
		ArrayList<Integer> a2 = new ArrayList<Integer>();
		if(h1.get(s)!=null){
			int index = h1.get(s);
			if(flag==0){
				for(Postings p:I1.get(index).list){
					a2.add(p.dId);
					
				}
			}
			else{
				for(int a:a1){
					for(Postings p:I1.get(index).list){
						ctr[0]++;
						if(a==p.dId){
							a2.add(a);
							I1.get(index).list.remove(p);
							break;
						}
					}
				}
			}
		}
		return a2;	
	}
	public static ArrayList<Integer> TaaTOr(LinkedList<Terms> I1,HashMap<String,Integer>  h1,ArrayList<Integer> a1,String s, int flag, long ctr[]){
		ArrayList<Integer> a2 = new ArrayList<Integer>();
		if(h1.get(s)!=null){
			int index = h1.get(s);
			if(flag==0){
				for(Postings p:I1.get(index).list){
					a2.add(p.dId);
					
				}
			}
			else{
				for(int a:a1){
					for(Postings p:I1.get(index).list){
						ctr[0]++;
						if(a==p.dId){
							I1.get(index).list.remove(p);
							break;
						}
					}
				}
				for(Postings p:I1.get(index).list)
					a2.add(p.dId);
			}
		}
		if(a1!=null)
			a2.addAll(a1);
		return a2;	
	}
	
	public static ArrayList<Integer> DaaTAnd(LinkedList<Terms> I1,HashMap<String,Integer>  h1,LinkedList<String> query, long ctr[]){
		ArrayList<Integer> a2 = new ArrayList<Integer>();
		int flag=1;
		for(String s:query){
			if(h1.get(s)==null){
				flag=0;
				break;
			}
		}
		if(flag!=0){
			int index = query.size();
			int check = 1;
			LinkedList<Postings>[] p = new LinkedList[index];
			Iterator<Postings>[] iter = new Iterator[index];
			Postings[] posts = new Postings[index];
			for(int i=0;i<index;i++){
				p[i]=I1.get(h1.get(query.get(i))).list;
				iter[i]=p[i].iterator();
				posts[i]=(Postings) iter[i].next();
			}
			while(check==1){
				int max=-1,i;
				for (i=0;i<index-1;i++){
					ctr[0]++;
					if(posts[i].dId==posts[i+1].dId)
						continue;
					else{
						if(posts[i].dId>posts[i+1].dId)
							max=posts[i].dId;
						else
							max=posts[i+1].dId;
					}
						
				}
				if(max==-1){
					a2.add(posts[i].dId);
					for(i=0;i<index;i++){
						if(iter[i].hasNext())
							posts[i]=(Postings) iter[i].next();
						else{
							check=0;
							break;
						}
					}
				}	
				else{
					for(i=0;i<index;i++){
						while((posts[i].dId<max)&&(check==1)){
							ctr[0]++;
							if(iter[i].hasNext())
								posts[i]=(Postings) iter[i].next();
							else
								check=0;
						}
						if(check==0)
							break;
					}
				}
					
			}
		}	
		return a2;	
	}
	
	public static ArrayList<Integer> DaaTOr(LinkedList<Terms> I1,HashMap<String,Integer>  h1,LinkedList<String> query, long ctr[]){
		ArrayList<Integer> a2 = new ArrayList<Integer>();
		ArrayList<Integer> a1 =new ArrayList<Integer>();
		LinkedList<String> query1 = new LinkedList<String>();
		int flag=0,index;
		for(String s:query){
			if(h1.get(s)==null)
				flag++;
			else{
				query1.add(s);
				a1.add(query1.indexOf(s));
			}
		}
		index=query1.size();
		if(flag!=query.size()){
			LinkedList<Postings>[] p = new LinkedList[a1.size()];
			Iterator<Postings>[] iter = new Iterator[a1.size()];
			Postings[] posts = new Postings[a1.size()];
			for(int i=0;i<a1.size();i++){
				p[a1.get(i)]=I1.get(h1.get(query1.get(a1.get(i)))).list;
				iter[a1.get(i)]=p[i].iterator();
				posts[a1.get(i)]=(Postings) iter[a1.get(i)].next();
			}
			while(a1.size()>1){
				int min=posts[a1.get(0)].dId;
				int mindex=0;
				for(int i=1;i<a1.size();i++){
					ctr[0]++;
					if(posts[a1.get(i)].dId==min){
						iter[a1.get(i)].remove();
						if(iter[a1.get(i)].hasNext())
							posts[a1.get(i)]=iter[a1.get(i)].next();
						else
						{
							a1.remove(a1.indexOf(a1.get(i)));
							i--;
						}
					}
					else{
						if(posts[a1.get(i)].dId<min)
						{
							min=posts[a1.get(i)].dId;
							mindex=i;
						}
					}
				}
				if(iter[a1.get(mindex)].hasNext()){
					posts[a1.get(mindex)]=iter[a1.get(mindex)].next();
				}
				else{
					a1.remove(a1.indexOf(a1.get(mindex)));
				}
			}
			for(int i=0;i<index;i++){
				for(Postings k: p[i])
					a2.add(k.dId);
			}
		}	
		return a2;	
	}
	
	public static void main(String[] args)throws IOException {
		String line="";
		LinkedList<Terms> I1 = new LinkedList<Terms>();
		FileReader f1 = new FileReader(args[0]);
		BufferedReader b1 = new BufferedReader(f1);
		while((line=b1.readLine())!=null){
			String a[] = line.split("\\\\c");
			String b[] = a[1].split("\\\\m");
			b[1]=b[1].substring(1, b[1].length()-1);
			String c[] = b[1].split(", ");
			Terms temp = new Terms(a[0],Integer.parseInt(b[0]));
			for (String s: c){
				String d[]=s.split("/");
				temp.addList(Integer.parseInt(d[0]),Integer.parseInt(d[1]));
			}
			I1.add(temp);
		}
		
		b1.close();f1.close();
		LinkedList<Terms> I2 = copy(I1);
		
		for(Terms t1:I1){
			t1.sortDoc();//Sorting in increasing order of DocID
		}
		for(Terms t1:I2){
			t1.sortTF();//Sorting in decreasing order of term frequency
		}
		//display(I2);
		File outfile = new File(args[1]);
		FileOutputStream fos = new FileOutputStream(outfile);
		PrintStream print = new PrintStream(fos);
		System.setOut(print);
		HashMap <String,Integer> h1=buildMap(I1);
		getTopK(copy(I1),Integer.parseInt(args[2]));//Top K Terms.
		
		f1=new FileReader(args[3]);
		b1=new BufferedReader(f1);
		line="";
		while((line=b1.readLine())!=null){
			String a[]=line.split(" ");
			ArrayList<Integer> taatAnd = null,taatOr = null,daatAnd=null,daatOr=null;
			LinkedList<String> qterms = new LinkedList<String>();
			int flag =0;long ctr1[]={0,0},ctr2[]={0,0},ctr3[]={0,0},ctr4[]={0,0};long stime,etime;
			for(String s:a){
				getPostings(I1, I2, s);//getting postings for each term
				
			}
			/*			
			TaaT AND Starts here
			*/
			System.out.print("FUNCTION: termAtATimeQueryAnd");
			displayString(a);	
			for(String s:a){
				qterms.add(s);
				stime=System.currentTimeMillis();
				taatAnd=TaaTAnd(copy(I2), h1, taatAnd, s, flag, ctr1);//TaaTAnd-Not Optimized
				etime=System.currentTimeMillis();
				ctr1[1]+=etime-stime;
				flag=1;
				if(taatAnd.isEmpty())
					break;
			}
			System.out.println(taatAnd.size()+" documents are found");
			System.out.println(ctr1[0]+" comparisons are made");
			System.out.println(((double)ctr1[1]/1000)+" seconds are used");
			sortDec(qterms, h1, I2);
			flag=0;ctr1[0]=0;taatAnd=null;
			for(String s:qterms){
				taatAnd=TaaTAnd(copy(I2), h1, taatAnd, s, flag, ctr1);//TaaTAnd-Optimized
				flag=1;
				if(taatAnd.isEmpty())
					break;
			}
			System.out.println(ctr1[0]+" comparisons are made with optimization");
			if(taatAnd.isEmpty())
				System.out.println("Result: terms not found");
			else
				displayList(taatAnd);
			/*			
			TaaT OR Starts here
			*/
			flag=0;
			System.out.print("FUNCTION: termAtATimeQueryOr");
			displayString(a);	
			for(String s:a){
				stime=System.currentTimeMillis();
				taatOr=TaaTOr(copy(I2), h1, taatOr, s, flag, ctr2);//TaaTOr-Not Optimized
				etime=System.currentTimeMillis();
				ctr2[1]+=etime-stime;
				flag=1;
			}
			System.out.println(taatOr.size()+" documents are found");
			System.out.println(ctr2[0]+" comparisons are made");
			System.out.println(((double)ctr2[1]/1000)+" seconds are used");
			flag=0;ctr2[0]=0;taatOr=null;
			for(String s:qterms){
				taatOr=TaaTOr(copy(I2), h1, taatOr, s, flag, ctr2);//TaaTOr-Optimized
				flag=1;
			}
			System.out.println(ctr2[0]+" comparisons are made with optimization");
			if(taatOr.isEmpty())
				System.out.println("Result: terms not found");
			else
				displayList(taatOr);
			/*			
			DaaT AND Starts here
			*/
			System.out.print("FUNCTION: docAtATimeQueryAnd");
			displayString(a);			
			stime=System.currentTimeMillis();
			daatAnd=DaaTAnd(I1, h1, qterms, ctr3);//DaaTAnd
			etime=System.currentTimeMillis();
			ctr3[1]+=etime-stime;
			System.out.println(daatAnd.size()+" documents are found");
			System.out.println(ctr3[0]+" comparisons are made");
			System.out.println(((double)ctr3[1]/1000)+" seconds are used");
			
			if(daatAnd.isEmpty())
				System.out.println("Result: terms not found");
			else
				displayList(daatAnd);
			/*			
			DaaT Or Starts here
			*/
			System.out.print("FUNCTION: docAtATimeQueryOr");
			displayString(a);			
			stime=System.currentTimeMillis();
			daatOr=DaaTOr(copy(I1), h1, qterms, ctr4);//DaaTAnd
			etime=System.currentTimeMillis();
			ctr4[1]+=etime-stime;
			System.out.println(daatOr.size()+" documents are found");
			System.out.println(ctr4[0]+" comparisons are made");
			System.out.println(((double)ctr4[1]/1000)+" seconds are used");
			
			if(daatOr.isEmpty())
				System.out.println("Result: terms not found");
			else
				displayList(daatOr);

		}
		b1.close();f1.close();
		
		
		
	}

}
