package absyn;

public class WhileExp extends Exp {

	public Exp test;
	public Exp body;

	public WhileExp (int row, int col, Exp test, Exp body){
		this.col = col;
        this.row = row;
		this.test = test;
		this.body = body;
	}

    public void accept( AbsynVisitor visitor, int level, boolean isAddr, int offset) {
        visitor.visit( this, level,isAddr, offset);
    }
}