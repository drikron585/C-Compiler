import java.util.ArrayList;

public class TableSymb{
    public String name;
    public int type;
    public int level;
    public ArrayList<Integer> params;
    //Simple = 0 Index = 1 Function = 2
    public int decType;
    public int offset;
    public int nestLevel;
    public int arraylength;
    public int funcAddr;
    public int returnType;
    public int functionarraycall = 0;

    public TableSymb(String name, int type, int level, ArrayList<Integer> params, int decType) {
		this.name = name;
		this.type = type;
		this.level = level;
    this.params = params;
		this.decType = decType;
	}
}
