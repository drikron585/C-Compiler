package absyn;

public class IntExp extends Exp {
  
    public int value;

    public IntExp(int row, int col, int value ) {
        this.col = col;
		this.row = row;
        this.value = value;
    }

    public void accept( AbsynVisitor visitor, int level, boolean isAddr, int offset) {
        visitor.visit( this, level,isAddr,offset);
    }
}
