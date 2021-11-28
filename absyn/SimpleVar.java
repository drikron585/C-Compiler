package absyn;

public class SimpleVar extends Var {

	public String name;

	public SimpleVar(int row, int col, String name){
		this.col = col;
        this.row = row;
		this.name = name;
	}

	public void accept(AbsynVisitor visitor, int level, boolean isAddr, int offset) {
		visitor.visit(this, level, isAddr, offset);
	}
}