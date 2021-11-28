package absyn;

public class SimpleDec extends VarDec{

	public SimpleDec(int row, int col, NameTy typ, String name){
		this.col = col;
        this.row = row;
		this.typ = typ;
		this.name = name;
	}

    public void accept(AbsynVisitor visitor, int level, boolean isAddr, int offset) {
        visitor.visit( this, level, isAddr, offset);
    }
}