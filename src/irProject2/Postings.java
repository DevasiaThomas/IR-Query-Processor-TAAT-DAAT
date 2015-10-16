package irProject2;

public class Postings {
	
	int dId,tF;

	public Postings() {
		this.dId = 0;
		this.tF = 0;
	}
	public Postings(int dId, int tF) {
		this.dId = dId;
		this.tF = tF;
	}
	public Postings(Postings p) {
		this.dId = p.dId;
		this.tF = p.tF;
	}


}
