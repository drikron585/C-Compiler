package absyn;

public class NameTy extends Absyn{
  
    public final static int INT  = 0;
    public final static int VOID = 1;
    public final static int ERROR = 2;

    public int type;

    public NameTy(int row, int col, int type){
        this.col = col;
		this.row = row;
        this.type = type;
    }

    public void accept( AbsynVisitor visitor, int level, boolean isAddr, int offset) {
        visitor.visit( this, level, isAddr, offset);
    }
}