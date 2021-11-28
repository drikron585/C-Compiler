package absyn;

public class AssignExp extends Exp {
    
    public Var lhs;
    public Exp rhs;

    public AssignExp(int row, int col, Var lhs, Exp rhs) {
        this.col = col;
		this.row = row;
        this.lhs = lhs;
        this.rhs = rhs;
    }
    
	public void accept(AbsynVisitor visitor, int level, boolean isAddr, int offset) {
		visitor.visit(this, level, isAddr, offset);
	}
}
