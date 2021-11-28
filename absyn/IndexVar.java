package absyn;

public class IndexVar extends Var{
	
	public Exp index;

	public IndexVar (int row, int col, String name, Exp index){
		this.col = col;
		this.row = row;
		this.name = name;
		this.index = index;
	}

  	public void accept( AbsynVisitor visitor, int level, boolean isAddr, int offset) {
    	visitor.visit( this, level, isAddr, offset);
  	}
}