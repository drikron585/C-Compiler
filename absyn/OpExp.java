package absyn;

public class OpExp extends Exp {
  
    public final static int PLUS        = 0;
    public final static int MINUS       = 1;
    public final static int MUL         = 2;
    public final static int DIV         = 3;
    public final static int EQ          = 4;
    public final static int EQEQ        = 5;
    public final static int NE          = 6;
    public final static int LT          = 7;
    public final static int LE          = 8;
    public final static int GT          = 9;
    public final static int GE         = 10;
    public final static int ERROROP    = 11;

    public Exp left;
    public Exp right;
    public int op;

    public OpExp(int row, int col, Exp left, int op, Exp right ) {
        this.col = col;
        this.row = row;
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public void accept( AbsynVisitor visitor, int level, boolean isAddr, int offset) {
        visitor.visit( this, level, isAddr, offset);
    }
}
