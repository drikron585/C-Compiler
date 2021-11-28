package absyn;

public class NilExp extends Exp {

    public NilExp(int row, int col){
        this.col = col;
        this.row = row;
    }
    
    public void accept( AbsynVisitor visitor, int level, boolean isAddr, int offset) {
		visitor.visit( this, level, isAddr, offset);
	}
}