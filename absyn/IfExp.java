package absyn;

public class IfExp extends Exp {
    
    public Exp test;
    public Exp thenB;
    public Exp elseB;

    public IfExp(int row, int col, Exp test, Exp thenB, Exp elseB){
        this.col = col;
		this.row = row;
        this.test = test;
        this.thenB = thenB;
        this.elseB = elseB;
    }

    public void accept( AbsynVisitor visitor, int level, boolean isAddr, int offset) {
        visitor.visit( this, level, isAddr, offset);
    }
}

