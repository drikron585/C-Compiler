package absyn;

public class VarExp extends Exp{
  
    public Var variable;

    public VarExp(int row, int col, Var variable) {
        this.col = col;
        this.row = row;
        this.variable = variable;
    }

    public void accept(AbsynVisitor visitor, int level, boolean isAddr, int offset) {
        visitor.visit(this, level, isAddr, offset);
    }
}
