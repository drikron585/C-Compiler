package absyn;

public class CallExp extends Exp{

    public String func;
    public ExpList args;

    public CallExp (int row, int col, String func, ExpList args){
        this.col = col;
		this.row = row;
        this.func = func;
        this.args = args;
    }

    public void accept( AbsynVisitor visitor, int level, boolean isAddr, int offset) {
        visitor.visit( this, level, isAddr, offset);
    }
}