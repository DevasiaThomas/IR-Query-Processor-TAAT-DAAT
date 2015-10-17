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
	public static LinkedList<Terms> copy(LinkedList<Terms> T1){ //This Function Deep Copies an Index
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
	public static HashMap<String,Integer> buildMap(LinkedList<Terms> T1){ // This function builds a Hashmap <Term,positionOfTermInIndex>
		HashMap<String,Integer> h1 = new HashMap<String,Integer>();
		for(Terms t1:T1){
			h1.put(t1.term,T1.indexOf(t1));
		}
		return h1;
		}
		
	public static void display(LinkedList<Terms> T1){//This Function displays the Index. Was Used for debugging.
		for(Terms t:T1){
			System.out.println(t.term+" "+t.size);
			for(Postings p:t.list){
				System.out.print(" "+p.dId+" "+p.tF+",");
			}
			System.out.print("\n");
		}
	}
	public static void displayString(String a[]){//This function displays the query. (One line in the input file)
		for (int i=0;i<a.length;i++){
			System.out.print(" "+a[i]);
			if(i!=a.length-1)
				System.out.print(",");
		}
		System.out.print("\n");
	}
	public static void displayList(ArrayList<Integer> a){//This function displays the Result List for DAAT and TAAT--AND and OR
		Collections.sort(a);
		System.out.print("Result:");
		for(int i=0;i<a.size();i++){
			System.out.print(" "+a.get(i));
			if(i!=a.size()-1)
				System.out.print(",");
		}
		System.out.print("\n");
	}
	public static void sortDec(LinkedList<String> a,HashMap<String,Integer> h1,LinkedList<Terms> I1){//This Function uses a Custom Comparator that sorts the entire Query(One Line) based on the postings size (Increasing Order).
		Comparator<String> c = (a1,a2) -> (h1.get(a1)==null)?-1:((h1.get(a2)==null)?1:((I1.get(h1.get(a1)).size>I1.get(h1.get(a2)).size)?1:((I1.get(h1.get(a1)).size<I1.get(h1.get(a2)).size)?-1:0)));
		a.sort(c);
	}
	
	public static void getTopK(LinkedList<Terms> I1,int n){
		Comparator<Terms> c = (p1, p2) -> (p1.size<p2.size)?1:((p1.size>p2.size)?-1:0);//Comparator that Sorts entire list based on Postings Size in Decreasing Order.
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
		for(Terms t:I1){								 //Traverses Index
			if(t.term.equals(Term)){
				int i = I1.indexOf(t);
				System.out.print("Ordered by doc IDs:"); // Prints based on Increasing DocID
				for(int j=0;j<t.size;j++){
					System.out.print(" "+t.list.get(j).dId);
					if(j!=t.size-1)
						System.out.print(",");
				}
				System.out.print("\n");
				System.out.print("Ordered by TF:");		 // Prints based on Decreasing TF
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
		if(h1.get(s)!=null){							// This checks if term exists in Index
			int index = h1.get(s);
			if(flag==0){								// If function is called for the first time just add Incoming DocIds to Result List.
				for(Postings p:I1.get(index).list){
					a2.add(p.dId);
					
				}
			}
			else{
				for(int a:a1){                          // a1 is the Old Result List
					for(Postings p:I1.get(index).list){
						ctr[0]++;
						if(a==p.dId){					// Compares docID with old list and query term's posting list
							a2.add(a);					//a2 is the new Result List
							I1.get(index).list.remove(p);//Removing compared elements from temporary Index so that less comparisons are made.
							break;
						}
					}
				}
			}
		}
		return a2;										// Return Intermediate or Final Result List
	}
	public static ArrayList<Integer> TaaTOr(LinkedList<Terms> I1,HashMap<String,Integer>  h1,ArrayList<Integer> a1,String s, int flag, long ctr[]){
		ArrayList<Integer> a2 = new ArrayList<Integer>();
		if(h1.get(s)!=null){							//Checking if query term exists in Index
			int index = h1.get(s);
			if(flag==0){
				for(Postings p:I1.get(index).list){		//Adds all docIDs if called for the first time.
					a2.add(p.dId);
					
				}
			}
			else{
				for(int a:a1){	  						//a1 is the old Result List
					for(Postings p:I1.get(index).list){
						ctr[0]++;
						if(a==p.dId){					//Compares docID with old list and query term's posting list
							I1.get(index).list.remove(p);// Removes duplicate elements from temporary Index 
							break;
						}
					}
				}
				for(Postings p:I1.get(index).list)		//adds non-duplicate docIds from Postings list to new Result List
					a2.add(p.dId);
			}
		}
		if(a1!=null)									// Checks if old Result Set had terms. Adds them to new Result Set.
			a2.addAll(a1);
		return a2;										// Return Intermediate or Final Result List
	}
	
	public static ArrayList<Integer> DaaTAnd(LinkedList<Terms> I1,HashMap<String,Integer>  h1,LinkedList<String> query, long ctr[]){
		ArrayList<Integer> a2 = new ArrayList<Integer>();
		int flag=1;
		for(String s:query){							//Checks for existence of terms in Index
			if(h1.get(s)==null){
				flag=0;
				break;
			}
		}
		if(flag!=0){
			int index = query.size();
			int check = 1;
			LinkedList<Postings>[] p = new LinkedList[index];// Array of Postings List of size = 'n' query terms
			Iterator<Postings>[] iter = new Iterator[index];//Iterator Array containing Iterators for each Postings list above
			Postings[] posts = new Postings[index];		// Array Of Postings to hold each Iterated Posting
			for(int i=0;i<index;i++){					//Assigning each index of the above to each term in the query list respectively
				p[i]=I1.get(h1.get(query.get(i))).list;
				iter[i]=p[i].iterator();
				posts[i]=(Postings) iter[i].next();
			}
			while(check==1){							//This checks all of the postings list have items to be traversed. 
				int max=-1,i;
				for (i=0;i<index-1;i++){				//Finds the max docID of all terms in Postings being currently pointed to
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
				if(max==-1){							// This check determines if all docIDs were equal
					a2.add(posts[i].dId);				// adds term to Result List
					for(i=0;i<index;i++){
						if(iter[i].hasNext())
							posts[i]=(Postings) iter[i].next();// iterate postings on all pointers
						else{
							check=0;					// One postings list has no more postings.
							break;
						}
					}
				}	
				else{									// Case for docIDs not equal
					for(i=0;i<index;i++){
						while((posts[i].dId<max)&&(check==1)){
							ctr[0]++;
							if(iter[i].hasNext())		// Iterate each postings list till docID is >= max
								posts[i]=(Postings) iter[i].next();
							else
								check=0;				// One postings list has no more postings.
						}
						if(check==0)
							break;
					}
				}
				// Repeat the above till we completely traverse one postings list.	
			}
		}	
		return a2;										// Return Final Result List	
	}
	
	public static ArrayList<Integer> DaaTOr(LinkedList<Terms> I1,HashMap<String,Integer>  h1,LinkedList<String> query, long ctr[]){
		ArrayList<Integer> a2 = new ArrayList<Integer>();
		ArrayList<Integer> a1 =new ArrayList<Integer>();
		LinkedList<String> query1 = new LinkedList<String>();
		int flag=0,index;
		for(String s:query){							// This block checks for existence of query terms in the Index. If it exists add to arraylist query1.
			if(h1.get(s)==null)
				flag++;
			else{
				query1.add(s);
				a1.add(query1.indexOf(s));				// This list maintains positions of query1 terms and the same is used to uniquely identify pointers.
			}
		}
		index=query1.size();
		if(flag!=query.size()){							// Check to see if all terms did not exist in Index.
			LinkedList<Postings>[] p = new LinkedList[a1.size()]; //Creates array of Postings List of size of 'n' existing terms.
			Iterator<Postings>[] iter = new Iterator[a1.size()];  //Iterator array for each of the above lists
			Postings[] posts = new Postings[a1.size()];	// Postings array to hold each of the iterated elements
			for(int i=0;i<a1.size();i++){				// Assigning postings list of n query terms to n pointers respectively
				p[a1.get(i)]=I1.get(h1.get(query1.get(a1.get(i)))).list;
				iter[a1.get(i)]=p[i].iterator();
				posts[a1.get(i)]=(Postings) iter[a1.get(i)].next();
			}
			while(a1.size()>1){							// This will work till only one postings list remains, Since all elements in that one list are unique and can be added to Result List directly
				int min=posts[a1.get(0)].dId;			//Initializing min of docIDs with the first docId in the first postings list
				int mindex=0;
				for(int i=1;i<a1.size();i++){			//Finds min docIDs amongst 2 to n postings lists and records it position
					ctr[0]++;
					if(posts[a1.get(i)].dId==min){		//If docId = min
						iter[a1.get(i)].remove();		//Removes duplicate docIDs
						if(iter[a1.get(i)].hasNext())	//Iterates after removing duplicate docID
							posts[a1.get(i)]=iter[a1.get(i)].next();
						else
						{
							a1.remove(a1.indexOf(a1.get(i))); //If next posting does not exist, remove the current pointer from comparison list
							i--;						//  We reduce i (points to index of a1) since after removal of element from array list the remaining elements are left shifted.
						}
					}
					else{
						if(posts[a1.get(i)].dId<min)	//if docID < min, use this posting as min
						{
							min=posts[a1.get(i)].dId;
							mindex=i;
						}
					}
				}
				if(iter[a1.get(mindex)].hasNext()){ 	// After we through n pointers we iterate once on the min postings and repeat the above
					posts[a1.get(mindex)]=iter[a1.get(mindex)].next();
				}
				else{
					a1.remove(a1.indexOf(a1.get(mindex))); // If min posting cannot be iterated anymore we remove it from the comparison list
				}
			}
			for(int i=0;i<index;i++){					//Once we are down to only one list to be traversed we know all lists contain postings that are unique
				for(Postings k: p[i])					//We add all postings to the Result List
					a2.add(k.dId);
			}
		}	
		return a2;										// Return Final Result List
	}
	
	public static void main(String[] args)throws IOException {
		String line="";
		LinkedList<Terms> I1 = new LinkedList<Terms>();
		FileReader f1 = new FileReader(args[0]);
		BufferedReader b1 = new BufferedReader(f1);
		while((line=b1.readLine())!=null){				//Creating Index from Input File
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
			t1.sortDoc();								//Sorting Index1 in increasing order of DocID
		}
		for(Terms t1:I2){
			t1.sortTF();								//Sorting Index2 in decreasing order of term frequency
		}
		//display(I2);--debugging
		File outfile = new File(args[1]);
		FileOutputStream fos = new FileOutputStream(outfile);
		PrintStream print = new PrintStream(fos);
		System.setOut(print);							//Redirecting print to a file.
		HashMap <String,Integer> h1=buildMap(I1);
		getTopK(copy(I1),Integer.parseInt(args[2]));	//Top K Terms.
		
		f1=new FileReader(args[3]);
		b1=new BufferedReader(f1);
		line="";
		while((line=b1.readLine())!=null){				// Loop runs for each line in the query file 
			String a[]=line.split(" ");
			ArrayList<Integer> taatAnd = null,taatOr = null,daatAnd=null,daatOr=null;
			LinkedList<String> qterms = new LinkedList<String>();
			int flag =0;long ctr1[]={0,0},ctr2[]={0,0},ctr3[]={0,0},ctr4[]={0,0};long stime,etime;
			for(String s:a){
				getPostings(I1, I2, s);					//getting postings for each term
				
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
			sortDec(qterms, h1, I2);					//Optimizing Query Terms
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

		} // Repeat above for next line in Query File
		b1.close();f1.close();
		
		
		
	}

}
