package irProject2;

import java.util.Comparator;
import java.util.LinkedList;

public class Terms {
	String term;
	int size;
	LinkedList<Postings> list = new LinkedList<Postings>();
	public Terms() {
		this.term = "";
		this.size = 0;
	}
	public Terms(String term, int size) {
		this.term = term;
		this.size = size;
	}
	public Terms(Terms t) {
		this.term = t.term;
		this.size = t.size;
		this.list = t.list;
	}
	public void addList(int dID, int dF)
	{
		Postings p1 = new Postings(dID,dF);
		this.list.add(p1);
	}
	public void sortDoc(){	// Sorts using Postings List using custom comparator in Increasing order of docID
		Comparator<Postings> c = (p1, p2) -> (p1.dId>p2.dId)?1:((p1.dId<p2.dId)?-1:0);
		this.list.sort(c);
			
	}
	public void sortTF(){  // Sorts using Postings List using custom comparator in Decreasing order of term frequency
		Comparator<Postings> c = (p1, p2) -> (p1.tF<p2.tF)?1:((p1.tF>p2.tF)?-1:0);
		this.list.sort(c);
			
	}
	
	

}
