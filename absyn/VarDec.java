package absyn;

public abstract class VarDec extends Dec {

	public NameTy typ;
	public String name;
	public int nestLevel;
	public int offset;
}