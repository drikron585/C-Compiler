package absyn;

public class ArrayDec extends VarDec {

	public IntExp size;

	public ArrayDec(int row, int col, NameTy typ, String name, IntExp size){
		this.col = col;
		this.row = row;
		this.typ = typ;
		this.name = name;
		this.size = size;
	}

    public void accept( AbsynVisitor visitor, int level,boolean isAddr, int offset) {
        visitor.visit( this, level, isAddr, offset );
    }
}