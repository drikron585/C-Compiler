package absyn;

public class CompoundExp extends Exp{
    
	public VarDecList decs;
	public ExpList exps;

	public CompoundExp(int row, int col, VarDecList decs, ExpList exps){
		this.col = col;
		this.row = row;
		this.decs = decs;
		this.exps = exps;
	}

    public void accept( AbsynVisitor visitor, int level, boolean isAddr, int offset) {
        visitor.visit( this, level, isAddr, offset );
    }
}