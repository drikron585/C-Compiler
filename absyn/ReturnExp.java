package absyn;

public class ReturnExp extends Exp {
	
	public Exp exp;
	public int error;

	public ReturnExp(int row, int col, Exp exp, int error){
		this.col = col;
        this.row = row;
		this.exp = exp;
		this.error = error;
	}

    public void accept( AbsynVisitor visitor, int level, boolean isAddr, int offset) {
        visitor.visit( this, level,isAddr, offset);
    }
}