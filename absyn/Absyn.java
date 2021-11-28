package absyn;

public abstract class Absyn {
    public int col;
    public int row;

    public abstract void accept( AbsynVisitor visitor, int level,boolean isAddr, int offset);
}
