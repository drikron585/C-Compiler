package absyn;

public class FunctionDec extends Dec {

	public NameTy result;
	public String func;
	public VarDecList params;
	public CompoundExp body;
	public int funaddr;

	public FunctionDec(int row, int col, NameTy result, String func, VarDecList params, CompoundExp body){
		this.col = col;
		this.row = row;
		this.result = result;
		this.func = func;
		this.params = params;
		this.body = body;
	}

    public void accept(AbsynVisitor visitor, int level,boolean isAddr, int offset) {
        visitor.visit(this, level, isAddr, offset);
    }
}