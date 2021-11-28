/*
   Yeong Jin Park 1002691
   yeongjin@uoguelph.ca

   Ron Drik 1029745
   rdrik@uoguelph.ca
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import absyn.*;

public class TMgen implements AbsynVisitor {

    class currFunction{
        String name;
        int type;
        int isReturned;
    }
    final static int SPACES = 4;
    final static String INLINE = " in line ";
    final static String INCOL = ", in col ";
    public currFunction FunctionScope;
    public HashMap<String, ArrayList<TableSymb>> table; 

    //TM global variables
    public static int emitLoc = 0;
    public static int highEmitLoc = 0;

    public static int globalOffset = 0;
    public static int frameOffset = -2;
  
    public static final int ac = 0;
    public static final int ac1 = 1;
    public static final int fp  = 5;
    public static final int gp = 6;
    public static final int pc = 7;

    public static int mainEntry;

    public static int ofpFO = 0;
    public static int tempf = 0;




    public static void emitRO(String code,int r, int s, int t, String comment){
        if(s < 100 && s > -10){
            System.out.printf("%3d: %5s %d,%d,%d \t\t\t\t\t\t\t%s\n",emitLoc,code, r, s, t,comment);
        }
        else{
            System.out.printf("%3d: %5s %d,%d,%d \t\t\t\t\t\t%s\n",emitLoc,code, r, s, t,comment);
        }
        emitLoc++;
        if(highEmitLoc < emitLoc){
            highEmitLoc= emitLoc;
        }
    }
    public static void emitRM(String code,int r, int s, int t, String comment){
        if(s < 100 && s > -10){
            System.out.printf("%3d: %5s %d,%d(%d) \t\t\t\t\t\t%s\n",emitLoc,code, r, s, t,comment);
        }
        else{
            System.out.printf("%3d: %5s %d,%d(%d) \t\t\t\t\t%s\n",emitLoc,code, r, s, t,comment);
        }
        emitLoc++;
        if(highEmitLoc < emitLoc){
            highEmitLoc= emitLoc;
        }
    }
    public static void emitRM_Abs(String code, int r, int a, String comment){
        if((a - (emitLoc + 1)) < 100 && (a - (emitLoc + 1)) > -10){
            System.out.printf("%3d: %5s %d,%d(%d) \t\t\t\t\t\t%s\n",emitLoc,code, r, (a - (emitLoc + 1)), pc, comment);
        }
        else{
            System.out.printf("%3d: %5s %d,%d(%d) \t\t\t\t\t%s\n",emitLoc,code, r, (a - (emitLoc + 1)), pc, comment);
        }
        emitLoc++;
		if (highEmitLoc < emitLoc){
			highEmitLoc = emitLoc;
        }
	} 
    public static void emitComment(String comment){
        System.out.printf("* %s\n",comment);
    }
    public static int emitSkip(int distance){
        int i = emitLoc;
        emitLoc += distance;
        if (highEmitLoc < emitLoc){
            highEmitLoc = emitLoc;
        }
        return i;
    }
    public static void emitBackup(int loc){
        if (loc > highEmitLoc){
            emitComment("BUG in emitBackup");
        }
        emitLoc = loc;
    }    
    public static void emitRestore(){
        emitLoc = highEmitLoc;
    }


    //Prelude headers
    public static void prelude(String fileNameTM){
		//Printing prelude
		emitComment("C-Minus Compilation to TM Code");
		emitComment("File: " + fileNameTM);
		emitComment("Standard prelude:");
		emitRM("LD", 6, 0, 0, "Load gp with maxaddr");
		emitRM("LDA", 5, 0, 6, "Copy gp to fp");
		emitRM("ST", 0, 0, 0, "Clear content at location");
		int savedLoc = emitSkip(1);
		emitComment("Jump around i/o routines here");
		emitComment("code for input routine");
		emitRM("ST", 0, -1, 5, "store return");
		emitRO("IN", 0, 0, 0, "input");
		emitRM("LD", 7, -1, 5, "return to caller");
		emitComment("code for output routine");
		emitRM("ST", 0, -1, 5, "store return");
		emitRM("LD", 0,-2,5, "Load output value");
		emitRO("OUT", 0, 0, 0, "output");
		emitRM("LD", 7, -1, 5, "return to caller");
		emitBackup(savedLoc);
		emitRM("LDA", 7, 7, 7, "Jump around i/o code");
		emitRestore();
		emitComment("End of standard prelude");
        System.out.println("");
        System.out.println("");
	}
    public static void finale(){
		//Printing finale
        emitRM( "ST", fp, globalOffset+ofpFO, fp, "Push ofp" );
        emitRM( "LDA", fp, globalOffset, fp, "Push frame" );
        emitRM( "LDA", ac, 1, pc, "Load ac with return line" );
        emitRM_Abs( "LDA", pc, mainEntry, "Jump to main location" );
        emitRM( "LD", fp, ofpFO, fp, "Pop frame" );
        emitRO( "HALT", 0, 0, 0, "End of program" );
	}


    public TMgen(){
        this.table = new HashMap<String, ArrayList<TableSymb>>();
        // add input function
        ArrayList<Integer> emptyStringlist = new ArrayList<Integer>();
        TableSymb temp = new TableSymb("input",0,-1,emptyStringlist,2);
        ArrayList<TableSymb> tableArray = new ArrayList<TableSymb>();
        tableArray.add(temp);
        table.put("input",tableArray);
        // add output function
        ArrayList<Integer> emptyStringlist2 = new ArrayList<Integer>();
        emptyStringlist2.add(0);
        TableSymb temp2 = new TableSymb("output",1,-1,emptyStringlist2,2);
        ArrayList<TableSymb> tableArray2 = new ArrayList<TableSymb>();
        tableArray.add(temp2);
        table.put("output",tableArray2);
    }

    ////indent function
    private void indent(int level){
        for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
    }


    //Search functions
    public void delete(int level){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.level == level){
                    SymbArray.remove();
                }
            }
        }
    }
    public void print(int level){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.level == level){
                    //indent(level);
                    if(currentSymb.decType == 0){
                        if(currentSymb.type == 0){
                            //System.out.println(currentSymb.name+" : int");
                        }
                        else if(currentSymb.type == 1){
                            //System.out.println(currentSymb.name+" : void");
                        }
                    }
                    if(currentSymb.decType == 1){
                        //System.out.println(currentSymb.name+"[] : int");
                    }
                }
            }
        }
    }
    public int redefinedVar(String varName, int level){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.level == level && currentSymb.name.equals(varName)){
                    return 1;
                }
            }
        }
        return 0;
    }
    public int undefinedVar(String varName){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.name.equals(varName) && currentSymb.type!=NameTy.VOID){
                    return 0;
                }
                else if(currentSymb.name.equals(varName) && currentSymb.type==NameTy.VOID){
                    return 2;
                }
            }
        }
        return 1;
    }
    public int getOffset(String varName){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.name.equals(varName)){
                    return currentSymb.offset;
                }
            }
        }
        return -1;
    }
    public int getNestLevel(String varName){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.name.equals(varName)){
                    return currentSymb.nestLevel;
                }
            }
        }
        return -1;
    }
    public int getArrayLength(String varName){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.name.equals(varName)){
                    return currentSymb.arraylength;
                }
            }
        }
        return -1;
    }
    public ArrayList<Integer> getFunction(String varName){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.name.equals(varName) && currentSymb.decType == 2){
                    return currentSymb.params;
                }
            }
        }
        return null;
    }
    public int find_type(String varName){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.name.equals(varName) && currentSymb.decType == 0){
                    return 0;
                }
                else if(currentSymb.name.equals(varName) && currentSymb.decType == 1){
                    return 1;
                }
                else if(currentSymb.name.equals(varName) && currentSymb.decType == 2){
                    return 2;
                }
            }
        }
        return 3;
    }
    public int searchForFunc(String varName){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.name.equals(varName) && currentSymb.decType == 2 && currentSymb.type == NameTy.INT){
                    return 0;
                }
                else if(currentSymb.name.equals(varName)&& currentSymb.decType == 2 && currentSymb.type == NameTy.VOID){
                    return 2;
                }
            }
        }
        return 1;
    }
    public int getFuncAddress(String varName){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.name.equals(varName) && currentSymb.decType == 2){
                    return currentSymb.funcAddr;
                }
            }
        }
        return -1;
    }
    public int getReturnType(String varName){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.name.equals(varName)){
                    return currentSymb.returnType;
                }
            }
        }
        return -1;
    }
    public int getfunctionarraycall(String varName){
        Iterator HashMap = table.entrySet().iterator();
        while(HashMap.hasNext()){
            Map.Entry CurrentKey = (Map.Entry)HashMap.next();
            ArrayList<TableSymb> SymTable = (ArrayList<TableSymb>)CurrentKey.getValue();
            Iterator SymbArray = SymTable.iterator();
            while(SymbArray.hasNext()){
                TableSymb currentSymb = (TableSymb)SymbArray.next();
                if(currentSymb.name.equals(varName)){
                    return currentSymb.functionarraycall;
                }
            }
        }
        return 0;
    }










    //Classes that extends Absyn
    
    //NameTy
    public void visit(NameTy exp, int level, boolean isAddr, int offset){
        // do nothing
    }


    //Classes that extends Var

    //SimpleVar
    public void visit(SimpleVar exp, int level, boolean isAddr, int offset){    
        // check if the name is in the symbol table, if not, print error (trying to use undefined var)
        emitComment("-> SimpleVar");
        emitComment("looking up simplevar id: "+exp.name);

        if(undefinedVar(exp.name) == 1){
            System.err.println("ERROR: Undefined simple variable: "+exp.name+INLINE+(exp.row+1)+INCOL+exp.col);
        }
        if(undefinedVar(exp.name) == 2){
            System.err.println("ERROR: Invalid(void) simple variable: "+exp.name+INLINE+(exp.row+1)+INCOL+exp.col);
        }
        if(isAddr == false){
            if(find_type(exp.name) == 0){
                emitRM("LD", 2,getOffset(exp.name), 5+((1+getNestLevel(exp.name))%2), "grab current value of "+exp.name);
                emitRM("ST", 2, frameOffset, 5, "get value of "+exp.name+" and add it to the end of the frameoffset"); 
                frameOffset--;
            }
            else if(find_type(exp.name) == 1){
                emitRM("LDA", 2, getOffset(exp.name), 5+((1+getNestLevel(exp.name))%2), "grab current address of "+exp.name);
                emitRM("ST", 2, frameOffset, 5, "get value of "+exp.name+" and add it to the end of the frameoffset"); 
                frameOffset--;
            }
        }
        else if(isAddr == true){
        frameOffset++;
        emitRM("LD", 2, frameOffset, 5, "Get the result of operation");
        emitRM("ST", 2,getOffset(exp.name), 5+((1+getNestLevel(exp.name))%2), "assign: calculated value to "+exp.name);
        }
        emitComment("<- SimpleVar");
    }
    //IndexVar
    public void visit(IndexVar exp, int level, boolean isAddr, int offset){
        exp.index.accept(this, level, false, 0);
        if(isAddr == false){
            emitRM("LD", 0, frameOffset+1, 5, "Get the result of index calculation (false)");

            if(getfunctionarraycall(exp.name) == 0){
                emitRM("LDC", 3, getOffset(exp.name)-1, 0, "Get starting point of the array");
                emitRO("SUB", 3, 3, 0, "Adding the start of the index with index number");
                emitRO("ADD", 3, 3, 5+((1+getNestLevel(exp.name))%2), "Current point + offset"); 
    
                //ERROR handling for out of range below
                emitRM("JGE", 0, 4, 7, "If array isn't out of index");
                emitRM("LDC", 2, -1000000, 0, "Load register 0 with out of range below");
                emitRO("OUT", 2, 0, 0, "Output out of range below");
                emitRM("ST", 4, frameOffset+1, 5, "ERROR RECOVERY: Send 0 as calculated value at "+exp.name);
                emitRM("LDA", 7, 9, 7, "ERROR RECOVERY: Ignore the rest of the calculation");
                //ERROR handling for out of range above
                emitRM("LD", 2, getOffset(exp.name), 5+((1+getNestLevel(exp.name))%2), "Load array length");
                emitRO("SUB", 0, 2, 0, "Adding array length to offset to see if out of bounds");
                emitRM("JGT", 0, 4, 7, "If array isn't out of index");
                emitRM("LDC", 2, -2000000, 0, "Load register 0 with out of range aboves");
                emitRO("OUT", 2, 0, 0, "Output out of range below");
                emitRM("ST", 4, frameOffset+1, 5, "ERROR RECOVERY: Send 0 as calculated value at "+exp.name);
                emitRM("LDA", 7, 2, 7, "ERROR RECOVERY: Ignore the rest of the calculation");
            }
            else{    
                emitRM("LD", 3, getOffset(exp.name), 5, "dqwdwqdqw (false)");
                emitRM("LDA", 3, 0, 3, "Get function pointer address");
                emitRM("LDC", 1, 1, 0, "Get starting point of the array");
                emitRO("SUB", 3, 3, 1, "Adding the start of the index with index number");
                emitRO("SUB", 3, 3, 0, "Adding the start of the index with index number");

            }
    
            emitRM("LD", 3, 0, 3, "Load the found index");
            emitRM("ST", 3, frameOffset+1, 5, "Assign: Calculated value to "+exp.name);
        }
        else if(isAddr == true){
            frameOffset++;
            emitRM("LD", 0, frameOffset, 5, "Get the result of index calculation (true)");
            if(getfunctionarraycall(exp.name) == 0){
                emitRM("LDC", 3, getOffset(exp.name)-1, 0, "Get starting point of the array");
                emitRO("SUB", 2, 3, 0, "Adding the start of the index with index number");
                emitRO("ADD", 3, 2, 5+((1+getNestLevel(exp.name))%2), "Current point + offset"); 
                frameOffset++;
    
                //ERROR handling for out of range below
                emitRM("JGE", 0, 3, 7, "If array isn't out of index");
                emitRM("LDC", 2, -1000000, 0, "Load register 0 with out of range below");
                emitRO("OUT", 2, 0, 0, "Output out of range below");
                emitRM("LDA", 7, 8, 7, "ERROR RECOVERY: Ignore the rest of the calculation");
                //ERROR handling for out of range above
                emitRM("LD", 2, getOffset(exp.name), 5+((1+getNestLevel(exp.name))%2), "Load array length");
                emitRO("SUB", 0, 2, 0, "Adding array length to offset to see if out of bounds");
                emitRM("JGT", 0, 3, 7, "If array isn't out of index");
                emitRM("LDC", 2, -2000000, 0, "Load register 0 with out of range aboves");
                emitRO("OUT", 2, 0, 0, "Output out of range below");
                emitRM("LDA", 7, 2, 7, "ERROR RECOVERY: Ignore the rest of the calculation");
            }
            else{
                emitRM("LD", 3, getOffset(exp.name), 5, "dqwdwqdqw (false)");
                emitRM("LDA", 3, 0, 3, "Get function pointer address");
                emitRM("LDC", 1, 1, 0, "Get starting point of the array");
                emitRO("SUB", 3, 3, 1, "Adding the start of the index with index number");
                emitRO("SUB", 3, 3, 0, "Adding the start of the index with index number");
                frameOffset++;
            }
            emitRM("LD", 2, frameOffset, 5, "Get the index value");
            emitRM("ST", 2, 0, 3, "Assign: Calculated value to "+exp.name);
        }
        if(exp.index instanceof CallExp){
            String functionName = ((CallExp)exp.index).func;
            if(searchForFunc(functionName) == 1){
                System.err.println("ERROR: Undeclared Index value: "+functionName+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            else if(searchForFunc(functionName) == 2){
                System.err.println("ERROR: void return function type at Index value: "+functionName+INLINE+(exp.row+1)+INCOL+exp.col);
            }
        }
        else if(exp.index instanceof VarExp){
            VarExp variableExp = (VarExp)exp.index;
            if(variableExp.variable instanceof SimpleVar){
                SimpleVar varaibleSim = (SimpleVar)variableExp.variable;
                if(undefinedVar(varaibleSim.name) == 1){
                    System.err.println("ERROR: Undeclared index value for "+exp.name+": "+varaibleSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                else if(undefinedVar(varaibleSim.name) == 2){
                    System.err.println("ERROR: Invalid(void) index value "+exp.name+": "+varaibleSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }

            }
        }
    }


    //Classes that extends Exp

    //NilExp
    public void visit(NilExp exp, int level, boolean isAddr, int offset){
        // do nothing
    }
    //VarExp
    public void visit(VarExp exp, int level, boolean isAddr, int offset){
        if (exp.variable != null){
            exp.variable.accept(this, level, false, 0);
        }
    }
    //IntExp
    public void visit(IntExp exp, int level, boolean isAddr, int offset){
        emitComment("-> Constant");
        emitComment("The constant value is "+exp.value);
        emitRM("LDC", 2, exp.value, 0, "Load Const");
        emitComment("<- Constant");
        emitRM("ST", 2, frameOffset, 5, "Add: Constant value to memory address");
        frameOffset--;
    }
    //CallExp
    public void visit(CallExp exp, int level, boolean isAddr, int offset){
        emitComment("-> Call of function: " + exp.func);
        if(exp.func.equals("input")){
            emitRM("ST", 5, frameOffset, 5, "Push ofp");
            emitRM("LDA", 5, frameOffset, 5, "Push frame");
            emitRM("LDA", 0, 1, 7, "Load ac with return line");
            emitRM("LDA", 7, 3-emitLoc, 7, "Jump to input func location");
            emitRM("LD", 5, 0, 5, "Pop frame");
            emitRM("ST",0,frameOffset,5,"Assign: Return value at current frameoffset");
            frameOffset--;
        }
        else if(exp.func.equals("output")){
            if(exp.args.tail == null){
                if(exp.args.head instanceof VarExp){
                    VarExp temp = (VarExp) exp.args.head;
                    if((temp.variable instanceof SimpleVar)){
                        SimpleVar temp2 = (SimpleVar) temp.variable;
                        emitRM("LD", 2,getOffset(temp2.name), 5+((1+getNestLevel(temp2.name))%2), "Get value of simple variable " +temp2.name);
                        emitRM("ST", 2,frameOffset - 2 , 5, "Store arg val");
                        emitRM("ST", 5, frameOffset, 5, "Push ofp");
                        emitRM("LDA", 5, frameOffset, 5, "Push frame");
                        emitRM("LDA", 0, 1, 7, "Load ac with return line");
                        emitRM("LDA", 7, 6-emitLoc, 7, "Jump to output func location");
                        emitRM("LD", 5, 0, 5, "Pop frame");
                    }                
                    if(temp.variable instanceof IndexVar){
                        IndexVar temp2 = (IndexVar) temp.variable;
                        exp.args.head.accept(this, level, false, 0);
                        frameOffset++;
                        emitRM("LD", 2, frameOffset, 5, "Get value of index variable " +temp2.name);
                        emitRM("ST", 2, frameOffset - 2 , 5, "Store arg val");
                        emitRM("ST", 5, frameOffset, 5, "Push ofp");
                        emitRM("LDA", 5, frameOffset, 5, "Push frame");
                        emitRM("LDA", 0, 1, 7, "Load ac with return line");
                        emitRM("LDA", 7, 6-emitLoc, 7, "Jump to output func location");
                        emitRM("LD", 5, 0, 5, "Pop frame");
                    }
                }
                else if(exp.args.head instanceof IntExp){
                    IntExp temp2 = (IntExp) exp.args.head;
                    emitRM("LDC", 2, temp2.value, 0, "Get value of int expression " +temp2.value);
                    emitRM("ST", 2,frameOffset - 2 , 5, "Store arg val");
                    emitRM("ST", 5, frameOffset, 5, "Push ofp");
                    emitRM("LDA", 5, frameOffset, 5, "Push frame");
                    emitRM("LDA", 0, 1, 7, "Load ac with return line");
                    emitRM("LDA", 7, 6-emitLoc, 7, "Jump to output func location");
                    emitRM("LD", 5, 0, 5, "Pop frame");
                }
                else{
                    ExpList args = exp.args;
                    args = exp.args;
                    while(args != null){
                        args.head.accept(this, level, false, 0);
                        args = args.tail;
                    }
                    frameOffset++;
                    emitRM("LD", 2, frameOffset, 5, "Get result of expression ");
                    emitRM("ST", 2,frameOffset - 2 , 5, "Store arg val");
                    emitRM("ST", 5, frameOffset, 5, "Push ofp");
                    emitRM("LDA", 5, frameOffset, 5, "Push frame");
                    emitRM("LDA", 0, 1, 7, "Load ac with return line");
                    emitRM("LDA", 7, 6-emitLoc, 7, "Jump to output func location");
                    emitRM("LD", 5, 0, 5, "Pop frame");
                }
            }
        }
        else{
            int counter=0;
            int paramLength=0;

            ExpList args = exp.args;

            while(args != null){
                paramLength++;
                args = args.tail;
            }


            ArrayList<Integer> arrayInteger = getFunction(exp.func);
            if(undefinedVar(exp.func) == 1){
                System.err.println("ERROR: Fucntion you have called is undefined: "+exp.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            else if(arrayInteger == null && paramLength !=0){
                System.err.println("ERROR: Fucntion you have called require less parameter: "+exp.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            else if(arrayInteger.size() > paramLength){
                System.err.println("ERROR: Fucntion you have called require more parameter: "+exp.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            else if(arrayInteger.size() < paramLength){
                System.err.println("ERROR: Fucntion you have called require less parameter: "+exp.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            else{
                args = exp.args;
                while(args != null){
                    
                    if(args.head instanceof VarExp){
                        VarExp temp = (VarExp)args.head;
                        if(temp.variable instanceof SimpleVar){
                            SimpleVar temp1 = (SimpleVar)temp.variable;
                            if(find_type(temp1.name) == 0){
                                System.out.println("********** simple ************* SimpleVar "+temp1.name);
                            }
                            else if(find_type(temp1.name) == 1){
                                System.out.println("********** simple ************* IndexVar "+temp1.name);
                            }
                            if(find_type(temp1.name) == 1){ 
                                if(arrayInteger.get(counter) != 2){
                                    System.err.println("ERROR: Function parameter you have referenced is invalid(integer index): "+temp1.name+INLINE+(temp1.row+1)+INCOL+temp1.col);
                                }
                            }
                            else{
                                if(arrayInteger.get(counter)>1){
                                    System.err.println("ERROR: Function parameter you have referenced should be a int index type: "+temp1.name+INLINE+(temp1.row+1)+INCOL+temp1.col);
                                }
                                else if(arrayInteger.get(counter) == 0){
                                    if(undefinedVar(temp1.name) == 2){
                                        System.err.println("ERROR: Function parameter you have referenced should be a int type: "+temp1.name+INLINE+(temp1.row+1)+INCOL+temp1.col);
                                    }
                                }
                                else if(arrayInteger.get(counter) == 1){
                                    if(undefinedVar(temp1.name) == 0){
                                        System.err.println("ERROR: Function parameter you have referenced should be a void type: "+temp1.name+INLINE+(temp1.row+1)+INCOL+temp1.col);
                                    }
                                }
                            }
                        }
                        else if(temp.variable instanceof IndexVar){
                            IndexVar temp1 = (IndexVar)temp.variable;
                            System.out.println("*********************** IndexVar "+temp1.name);
                            if(undefinedVar(temp1.name) == 1){
                                System.err.println("ERROR: Function parameter you have referenced is undefined: "+temp1.name+INLINE+(temp1.row+1)+INCOL+temp1.col);
                            }
                            else{
                                if(arrayInteger.get(counter) != 0){
                                    System.err.println("ERROR: Function parameter you have referenced is invalid(Interger): "+temp1.name+INLINE+(temp1.row+1)+INCOL+temp1.col);
                                }
                            }
                        }
                    }
                    else if(args.head instanceof IntExp){
                        IntExp temp1 = (IntExp)args.head;
                        System.out.println("*********************** IntExp "+temp1.value);
                    }
                    else if(args.head instanceof CallExp){
                        CallExp call = (CallExp) args.head;
                        System.out.println("*********************** CallExp "+call.func);
                        if(searchForFunc(call.func) == 1){
                            System.err.println("ERROR: Fucntion you have called is undefined: "+call.func+INLINE+(call.row+1)+INCOL+call.col);
                        }
                        else if(searchForFunc(call.func) == 2){
                            System.err.println("ERROR: Fucntion you have called has a void return type: "+call.func+INLINE+(call.row+1)+INCOL+call.col);
                        }
                        else{
                            if(arrayInteger.get(counter) != 0){
                                System.err.println("ERROR: Fucntion you have called returns a int type but the parameter requires a void type: "+call.func+INLINE+(call.row+1)+INCOL+call.col);
                            }
                        }
                    }
                    
                    counter++;

                    args.head.accept(this, level, false, 0);
                    
                    args = args.tail;
                }
            }
            emitRM("ST", 5, frameOffset, 5, "Push ofp");
            emitRM("LDA", 5, frameOffset, 5, "Push frame");
            emitRM("LDA", 0, 1, 7, "Load ac with return line");
            emitRM("LDA", 7, getFuncAddress(exp.func)-emitLoc-1, 7, "Jump to " + exp.func +" func location");
            emitRM("LD", 5, 0, 5, "Pop frame");
            if(getReturnType(exp.func) == 0){
                frameOffset = frameOffset+paramLength;
                emitRM("ST",0,frameOffset,5,"Assign: Return value at current frameoffset");
                frameOffset--;
            }
        }
        emitComment("<- Call");
    }   
    //OpExp
    public void visit(OpExp exp, int level, boolean isAddr, int offset){

        emitComment("-> Op");
        exp.left.accept(this, level, false, 0);
        exp.right.accept(this, level, false, 0);
        
        switch(exp.op){
            case OpExp.PLUS:
                emitRM("LD", 0, frameOffset+2, 5,"Load arithmetic value 1 to 0");
                emitRM("LD", 1, frameOffset+1, 5,"Load arithmetic value 2 to 1");
                emitRO("ADD", 0, ac, ac1, "ac(0) + ac1(1) OP");
                emitRM("ST", 0, frameOffset+2, 5, "assign: store value");
                frameOffset++;
                break;
            case OpExp.MINUS:
                emitRM("LD", 0, frameOffset+2, 5,"Load arithmetic value 1 to 0");
                emitRM("LD", 1, frameOffset+1, 5,"Load arithmetic value 2 to 1");
                emitRO("SUB", 0, ac, ac1, "ac(0) - ac1(1) OP");
                emitRM("ST", 0, frameOffset+2, 5, "assign: store value");
                frameOffset++;
                break;
            case OpExp.MUL:
                emitRM("LD", 0, frameOffset+2, 5,"Load arithmetic value 1 to 0");
                emitRM("LD", 1, frameOffset+1, 5,"Load arithmetic value 2 to 1");
                emitRO("MUL", 0, ac, ac1, "ac(0) * ac1(1) OP");
                emitRM("ST", 0, frameOffset+2, 5, "assign: store value");
                frameOffset++;
                break;
            case OpExp.DIV:
                emitRM("LD", 0, frameOffset+2, 5,"Load arithmetic value 1 to 0");
                emitRM("LD", 1, frameOffset+1, 5,"Load arithmetic value 2 to 1");
                emitRO("DIV", 0, ac, ac1, "ac(0) / ac1(1) OP");
                emitRM("ST", 0, frameOffset+2, 5, "assign: store value");
                frameOffset++;
                break;
            case OpExp.EQEQ:
                //System.out.println( " == " );
                frameOffset++;
                emitRM("LD", 0, frameOffset, 5, "Load ac");
                frameOffset++;
                emitRM("LD", 1, frameOffset, 5, "Load ac1");
                emitRO("SUB", 0, 1, 0, "make it zero");
                emitRM("JEQ", 0, 2, 7, "ac > ac1");
                emitRM("LDC", 0, 0, 0, "FALSE CASE");
                emitRM("LDA", 7, 1, 7, "UNCONDITIONAL JUMP");
                emitRM("LDC", 0, 1, 0, "TRUE CASE");
                break;
            case OpExp.NE:
                //System.out.println( " != " );
                frameOffset++;
                emitRM("LD", 0, frameOffset, 5, "Load ac");
                frameOffset++;
                emitRM("LD", 1, frameOffset, 5, "Load ac1");
                emitRO("SUB", 0, 1, 0, "make it zero");
                emitRM("JNE", 0, 2, 7, "ac > ac1");
                emitRM("LDC", 0, 0, 0, "FALSE CASE");
                emitRM("LDA", 7, 1, 7, "UNCONDITIONAL JUMP");
                emitRM("LDC", 0, 1, 0, "TRUE CASE");
                break;
            case OpExp.LT:
                //System.out.println( " < " );
                frameOffset++;
                emitRM("LD", 0, frameOffset, 5, "Load ac1");
                frameOffset++;
                emitRM("LD", 1, frameOffset, 5, "Load ac");
                emitRO("SUB", 0, 1, 0, "make it zero");
                emitRM("JLT", 0, 2, 7, "ac < ac1");
                emitRM("LDC", 0, 0, 0, "FALSE CASE");
                emitRM("LDA", 7, 1, 7, "UNCONDITIONAL JUMP");
                emitRM("LDC", 0, 1, 0, "TRUE CASE");
                break;
            case OpExp.LE:
                //System.out.println( " <= " );
                frameOffset++;
                emitRM("LD", 0, frameOffset, 5, "Load ac");
                frameOffset++;
                emitRM("LD", 1, frameOffset, 5, "Load ac1");
                emitRO("SUB", 0, 1, 0, "make it zero");
                emitRM("JLE", 0, 2, 7, "ac > ac1");
                emitRM("LDC", 0, 0, 0, "FALSE CASE");
                emitRM("LDA", 7, 1, 7, "UNCONDITIONAL JUMP");
                emitRM("LDC", 0, 1, 0, "TRUE CASE");
                break; 
            case OpExp.GT:
                //System.out.println( " > " );
                frameOffset++;
                emitRM("LD", 0, frameOffset, 5, "Load ac");
                frameOffset++;
                emitRM("LD", 1, frameOffset, 5, "Load ac1");
                emitRO("SUB", 0, 1, 0, "make it zero");
                emitRM("JGT", 0, 2, 7, "ac > ac1");
                emitRM("LDC", 0, 0, 0, "FALSE CASE");
                emitRM("LDA", 7, 1, 7, "UNCONDITIONAL JUMP");
                emitRM("LDC", 0, 1, 0, "TRUE CASE");
                break;
            case OpExp.GE:
                //System.out.println( " >= " );
                frameOffset++;
                emitRM("LD", 0, frameOffset, 5, "Load ac");
                frameOffset++;
                emitRM("LD", 1, frameOffset, 5, "Load ac1");
                emitRO("SUB", 0, 1, 0, "make it zero");
                emitRM("JGE", 0, 2, 7, "ac > ac1");
                emitRM("LDC", 0, 0, 0, "FALSE CASE");
                emitRM("LDA", 7, 1, 7, "UNCONDITIONAL JUMP");
                emitRM("LDC", 0, 1, 0, "TRUE CASE");
                break;
            case OpExp.EQ:
                //System.out.println( " = " );
                break;
            case OpExp.ERROROP:
                System.out.println( " ERROR " );
                break;
            default:
            System.out.println("Unrecognized operator at line " + exp.col);
        }
        
        emitComment("<- Op");

        if (exp.left instanceof CallExp){
            CallExp callExpName = (CallExp)exp.left;
            if(searchForFunc(callExpName.func) == 1){
                System.err.println("ERROR: Undeclared function: "+callExpName.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            else if(searchForFunc(callExpName.func) == 2){
                System.err.println("ERROR: void return function type for function: "+callExpName.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }
        }
        else if(exp.left instanceof VarExp){
            VarExp varExpName = (VarExp)exp.left;
            if(varExpName.variable instanceof SimpleVar){
                SimpleVar temp = (SimpleVar)varExpName.variable;
                if(undefinedVar(temp.name) == 1){
                    System.err.println("ERROR: Undefined simple variable: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                if(undefinedVar(temp.name) == 2){
                    System.err.println("ERROR: Invalid(void) simple variable cannot be assigned anything: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
            else if(varExpName.variable instanceof IndexVar){
                IndexVar temp = (IndexVar)varExpName.variable;
                if(undefinedVar(temp.name) == 1){
                    System.err.println("ERROR: Undefined simple variable: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                if(undefinedVar(temp.name) == 2){
                    System.err.println("ERROR: Invalid(void) simple variable cannot be a index variablesss: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
        }
        if (exp.right instanceof CallExp) {
            CallExp callExpName = (CallExp)exp.right;
            if(searchForFunc(callExpName.func) == 1){
                System.err.println("ERROR: Undeclared function: "+callExpName.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            else if(searchForFunc(callExpName.func) == 2){
                System.err.println("ERROR: void return function type for function: "+callExpName.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }
        }
        else if(exp.right instanceof VarExp){
            VarExp varExpName = (VarExp)exp.right;
            if(varExpName.variable instanceof SimpleVar){
                SimpleVar temp = (SimpleVar)varExpName.variable;
                if(undefinedVar(temp.name) == 1){
                    System.err.println("ERROR: Undefined simple variable: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                if(undefinedVar(temp.name) == 2){
                    System.err.println("ERROR: Invalid(void) simple variable cannot be assigned anything: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
            else if(varExpName.variable instanceof IndexVar){
                IndexVar temp = (IndexVar)varExpName.variable;
                if(undefinedVar(temp.name) == 1){
                    System.err.println("ERROR: Undefined simple variable: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                if(undefinedVar(temp.name) == 2){
                    System.err.println("ERROR: Invalid(void) simple variable cannot be assigned anything: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
        }
    }
    //AssignExp
    public void visit(AssignExp exp, int level, boolean isAddr, int offset){ 
        emitComment("-> Assign");
        exp.rhs.accept(this, level, false, 0);
        exp.lhs.accept(this, level, true, 0);

        //ERROR HANDLINGS 

        //Lhs is simple or index
        if(exp.lhs instanceof IndexVar){
            IndexVar templhs = (IndexVar) exp.lhs;
            if(undefinedVar(templhs.name) == 1){
                System.err.println("ERROR: Undefined index variable: "+templhs.name+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            if(undefinedVar(templhs.name) == 2){
                System.err.println("ERROR: Invalid(void) index variable: "+templhs.name+INLINE+(exp.row+1)+INCOL+exp.col);
            }
        }
        //RHS can be anything like functions or expressions
        if (exp.rhs instanceof CallExp){
            CallExp callExpName = (CallExp)exp.rhs;
            if(searchForFunc(callExpName.func) == 1){
                System.err.println("ERROR: Undeclared function: "+callExpName.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            else if(searchForFunc(callExpName.func) == 2){
                System.err.println("ERROR: void return function type for function: "+callExpName.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }
        }
        else if(exp.rhs instanceof VarExp){
            VarExp varExpName = (VarExp)exp.rhs;
            if(varExpName.variable instanceof SimpleVar){
                SimpleVar temp = (SimpleVar)varExpName.variable;
                if(undefinedVar(temp.name) == 1){
                    System.err.println("ERROR: Undefined simple variable: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                else if(undefinedVar(temp.name) == 2){
                    System.err.println("ERROR: Invalid(void) simple variable cannot be assigned to anything: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
            else if(varExpName.variable instanceof IndexVar){
                IndexVar temp = (IndexVar)varExpName.variable;
                if(undefinedVar(temp.name) == 1){
                    System.err.println("ERROR: Undefined simple variable: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                else if(undefinedVar(temp.name) == 2){
                    System.err.println("ERROR: Invalid(void) simple variable cannot be assigned to anything: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
        }
   
        emitComment("<- Assign");
    }
    //IfExp
    public void visit(IfExp exp, int level, boolean isAddr, int offset){
        level++;
        emitComment("-> If");
        int currentFrameoffset = frameOffset;

        emitComment("If: Jump after body comes back here");
        exp.test.accept(this, level, false, 0);
        if(exp.test instanceof IntExp || exp.test instanceof CallExp || exp.test instanceof VarExp){
            frameOffset++;
            emitRM("LD", 0, frameOffset, 5, "Load the register 0 with condition value");
        }
        emitComment("If: Jump to end belongs here");
        int savedLoc1 = emitSkip(1);
        
        if(exp.test instanceof VarExp){
            VarExp var = (VarExp)exp.test;
            if(var.variable instanceof SimpleVar){
                SimpleVar variableSim = (SimpleVar)var.variable;
                if(undefinedVar(variableSim.name) == 1){
                    System.err.println("ERROR: If statement condition value cannot be undefined: "+variableSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                if(undefinedVar(variableSim.name) == 2){
                    System.err.println("ERROR: If statement condition cannot be a void type: "+variableSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
            else if(var.variable instanceof IndexVar){
                IndexVar variableIndex = (IndexVar)var.variable;
                if(undefinedVar(variableIndex.name) == 1){
                    System.err.println("ERROR: If statement condition value cannot be undefined: "+variableIndex.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                if(undefinedVar(variableIndex.name) == 2){
                    System.err.println("ERROR: If statement condition cannot be a void type: "+variableIndex.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
        }

        exp.thenB.accept(this, level, false, 0);

        int savedLoc2 = emitSkip(0);
        if(exp.elseB != null && !(exp.elseB instanceof NilExp)){
            emitBackup(savedLoc1);
            emitRM_Abs("JEQ", 0, savedLoc2+1, "If: Jump to else part");
            emitRestore();
        }
        else{
            emitBackup(savedLoc1);
            emitRM_Abs("JEQ", 0, savedLoc2, "End of If");
            emitRestore();
        }

        if (exp.elseB != null && !(exp.elseB instanceof NilExp)){
            emitComment("If: Jump to else belongs here");
            savedLoc1 = emitSkip(1);


            //indent(level);
            //System.out.println("Entering a new else block: ");

            level++;
            exp.elseB.accept(this, level, false, 0);
            print(level);
            delete(level);
            level--;

            //indent(level);
            //System.out.println("Leaving the else block");

            savedLoc2 = emitSkip(0);
            emitBackup(savedLoc1);
            emitRM_Abs("LDA", 7, savedLoc2, "If: Leave else if");
            emitRestore();
        }

        print(level);
        delete(level);
        level--;

        
        frameOffset = currentFrameoffset;
        emitComment("<- If");
    }
    //WhilExp
    public void visit(WhileExp exp, int level, boolean isAddr, int offset){
        level++;
        emitComment("-> While");
        int currentFrameoffset = frameOffset;



        emitComment("While: Jump after body comes back here");
        int savedLoc1 = emitSkip(0);
        exp.test.accept(this, level, false, 0);
        if(exp.test instanceof IntExp || exp.test instanceof CallExp || exp.test instanceof VarExp){
            frameOffset++;
            emitRM("LD", 0, frameOffset, 5, "Load the register 0 with condition value");
        }
        emitComment("While: Jump to end belongs here");
        int savedLoc2 = emitSkip(1);

        if(exp.test instanceof VarExp){
            VarExp var = (VarExp)exp.test;
            if(var.variable instanceof SimpleVar){
                SimpleVar variableSim = (SimpleVar)var.variable;
                if(undefinedVar(variableSim.name) == 1){
                    System.err.println("ERROR: While statement condition value cannot be undefined: "+variableSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                if(undefinedVar(variableSim.name) == 2){
                    System.err.println("ERROR: While statement condition cannot be a simple void type: "+variableSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
            else if(var.variable instanceof IndexVar){
                IndexVar variableIndex = (IndexVar)var.variable;
                if(undefinedVar(variableIndex.name) == 1){
                    System.err.println("ERROR: While statement condition value cannot be undefined: "+variableIndex.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                if(undefinedVar(variableIndex.name) == 2){
                    System.err.println("ERROR: While statement condition cannot be a void type: "+variableIndex.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
        }
        
        exp.body.accept(this, level, false, 0);

        emitRM_Abs("LDA", 7, savedLoc1, "While: Absolute jump to test");
        int savedLoc3 = emitSkip(0);
        emitBackup(savedLoc2);
        emitRM_Abs("JEQ", 0, savedLoc3, "While: Jump to end");
        emitRestore();

        print(level);
        delete(level);
        level--;


        frameOffset = currentFrameoffset;
        emitComment("<- While");
    }
    //ReturnExp
    public void visit(ReturnExp exp, int level, boolean isAddr, int offset){
        // handles return;
        if(exp.exp != null){
            exp.exp.accept(this, level, false, 0);
        }
        else if(exp.exp == null && FunctionScope.type == 0){
            System.err.println("ERROR: Return value should match the function type(int): "+INLINE+(exp.row+1)+INCOL+exp.col);
        }
        // flag if the current function returns anything
        FunctionScope.isReturned = 1;
        if(exp.exp instanceof IntExp){
            IntExp temp = (IntExp)exp.exp;
            if(FunctionScope.type == 1){
                System.err.println("ERROR: Return value should match the function type(void): "+temp.value+INLINE+(exp.row+1)+INCOL+exp.col);
            }
        }   
        else if(exp.exp instanceof VarExp){
            VarExp var = (VarExp)exp.exp;
            if(var.variable instanceof SimpleVar){
                SimpleVar variableSim = (SimpleVar)var.variable;
                if(FunctionScope.type == 0){
                    if(undefinedVar(variableSim.name) == 1){
                        System.err.println("ERROR: Return value cannot be undefined: "+variableSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                    }
                    else if(undefinedVar(variableSim.name) == 2){
                        System.err.println("ERROR: Return value should match the function type(int): "+variableSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                    }
                    if(find_type(variableSim.name) == 1){
                        System.err.println("ERROR: Return value cannot be an index: "+variableSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                    }
                }
                else if(FunctionScope.type == 1){
                    if(undefinedVar(variableSim.name) == 1){
                        System.err.println("ERROR: Return value cannot be undefined: "+variableSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                    }
                    else if(undefinedVar(variableSim.name) == 0){
                        System.err.println("ERROR: Return value should match the function type(void): "+variableSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                    }
                }
            }
            else if(var.variable instanceof IndexVar){
                IndexVar variableSim = (IndexVar)var.variable;
                if(undefinedVar(variableSim.name) == 1){
                    System.err.println("ERROR: Return varaible you have referenced is undefined: "+variableSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                else{
                    if(FunctionScope.type == 1){
                        System.err.println("ERROR: Void function type cannot be returned by a interger/integers: "+variableSim.name+INLINE+(exp.row+1)+INCOL+exp.col);
                    }
                }
            }
        }
        else if(exp.exp instanceof CallExp){
            CallExp call = (CallExp)exp.exp;
            String functionName = call.func;
            if(searchForFunc(functionName) == 1){
                System.err.println("ERROR: Undeclared function: "+functionName+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            else if(searchForFunc(functionName) == 2){
                System.err.println("ERROR: Void return function type: "+functionName+INLINE+(exp.row+1)+INCOL+exp.col);
            }
        }
        frameOffset++;
        emitRM( "LD", 0, frameOffset, 5, "Load return into register" );
    }
    //CompoundExp
    public void visit(CompoundExp exp, int level, boolean isAddr, int offset){
        emitComment("-> Compound statement");
        if (exp.decs != null && exp.exps != null){
            //level++;
        }
        if (exp.decs != null){
            exp.decs.accept(this, level, false, 0);
        }
        if (exp.exps != null){
            exp.exps.accept(this, level, false, 0);
        }
        emitComment("<- Compound statement");
    }
    

    //Classes that extends Dec

    //FunctionDec
    public void visit(FunctionDec exp, int level, boolean isAddr, int offset){

        int savedLoc = emitSkip(1);

        emitComment("Processing function: "+exp.func);
        if(exp.func.equals("main")){
            mainEntry = emitLoc;
        }
        emitRM("ST",0,-1,5,"Store return");
        emitComment("Jump around function body here");
        exp.funaddr = emitLoc - 1;
        level++;


        if(redefinedVar(exp.func,level) == 0){
            ArrayList<Integer> emptyIntegerlist = new ArrayList<Integer>();

            VarDecList instanceParams = exp.params;
            while(instanceParams != null){
                if(instanceParams.head instanceof SimpleDec){ 
                    SimpleDec currParam = (SimpleDec)instanceParams.head;
                    emptyIntegerlist.add(currParam.typ.type);
                }
                else if(instanceParams.head instanceof ArrayDec){
                    ArrayDec currParam = (ArrayDec)instanceParams.head;
                    emptyIntegerlist.add(currParam.typ.type+2);
                }
                instanceParams = instanceParams.tail;
            }
            TableSymb temp = new TableSymb(exp.func,exp.result.type,level,emptyIntegerlist,2);
            temp.funcAddr = exp.funaddr;
            temp.returnType = exp.result.type;

            if(!table.containsKey(exp.func)){
                ArrayList<TableSymb> tableArray = new ArrayList<TableSymb>();
                tableArray.add(temp);
                table.put(exp.func,tableArray);
            }
            else{
                table.get(exp.func).add(temp);
            }

            //indent(level);
            //System.out.println("Entering scope for function " + exp.func + " : ");
            FunctionScope = new currFunction();
            FunctionScope.name = exp.func;
            FunctionScope.type = exp.result.type;
            FunctionScope.isReturned = 0;
            level++;
            int numParams = 0;

            VarDecList instanceParamsRun = exp.params;
            while(instanceParamsRun != null){
                numParams++;
                instanceParamsRun.head.accept(this, level, false, 0);
                instanceParamsRun = instanceParamsRun.tail;
            }
            
            for(int i = 1; i <= numParams; ++i){
                emitRM("LD", 1, numParams+1 - (i) , 5, "load arg value");
                emitRM("ST", 1, -i - 1 , 5, "store arg value");
            }



            exp.body.accept(this, level, false, 0);

            emitRM("LD", pc, -1, fp, "Return to caller");
            emitBackup(savedLoc);
            emitRM_Abs("LDA", pc, highEmitLoc, "Jump around function body");
            emitComment("<- Fundecl");
            emitRestore();

            print(level);
            delete(level);

            if(FunctionScope.isReturned == 0 && FunctionScope.type == 0){
                System.err.println("ERROR: No return statement in int type function declaration:"+exp.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }



            //indent(level);
            // System.out.print(exp.func+"(");
            // VarDecList CurrentParameter = exp.params;
            // while(CurrentParameter != null) {
            //     System.out.print("int "+CurrentParameter.head.name);
            //     CurrentParameter = CurrentParameter.tail;
            //     if(CurrentParameter != null){
            //         System.out.print(", ");
            //     }
            // }
            if(exp.result.type == NameTy.VOID){
                //System.out.println(") -> void");
            }
            else if(exp.result.type == NameTy.INT){
                //System.out.println(") -> int");
            }
            level--;

            //indent(level);
            //System.out.println("Leaving the function scope "+ exp.func);
            frameOffset = -2;
        }
        else{
            System.err.println("ERROR: Redefined function: "+exp.func+INLINE+(exp.row+1)+INCOL+exp.col);
        }
    }


    //Classes that extends VarDec

    //SimpleDec
    public void visit(SimpleDec exp, int level, boolean isAddr, int offset){
        if(redefinedVar(exp.name,level) == 0){
            if(level == 0){
                emitComment("Processing global simple variable: "+exp.name);
                exp.offset = globalOffset;
                exp.nestLevel =  0;
                globalOffset--;
            }
            else if(level != 0){
                emitComment("Processing local simple variable: "+exp.name);
                exp.offset = frameOffset;
                exp.nestLevel =  1;
                frameOffset--;
            }
            
            ArrayList<Integer> emptyIntegerlist = new ArrayList<Integer>();
            TableSymb temp = new TableSymb(exp.name,exp.typ.type,level,emptyIntegerlist,0);
            
            temp.offset = exp.offset;
            temp.nestLevel = exp.nestLevel; 
            
            if(!table.containsKey(exp.name)){
                ArrayList<TableSymb> tableArray = new ArrayList<TableSymb>();
                tableArray.add(temp);
                table.put(exp.name,tableArray);
            }
            else{
                table.get(exp.name).add(temp);
            }
        }
        else{
            System.err.println("ERROR: Redefined simple variable: "+exp.name+INLINE+(exp.row+1)+INCOL+exp.col);
        }

    }
    //ArrayDec
    public void visit(ArrayDec exp, int level, boolean isAddr, int offset){
        if(redefinedVar(exp.name,level) == 1){
            System.err.println("ERROR: "+exp.name+" has already been declared as a simple variable: "+INLINE+(exp.row+1)+INCOL+exp.col);
        }
        else if(exp.typ.type == NameTy.VOID){
            System.err.println("ERROR: Index variables cannot have a void type: "+exp.name+INLINE+(exp.row+1)+INCOL+exp.col);
        }
        else if(redefinedVar(exp.name,level) == 0){
            if(level == 0){
                emitComment("Processing global index variable: "+exp.name);
                exp.offset = globalOffset;
                globalOffset = globalOffset - (exp.size.value+1);
                exp.nestLevel =  0;
                emitRM("LDC", 2, exp.size.value, 0, "Get array length");
                emitRM("ST", 2, exp.offset, 6, "Assign: Array length to the beginning of array index");    
            }
            else if(level != 0){
                emitComment("Processing local index variable: "+exp.name);
                exp.offset = frameOffset;
                if(exp.size != null){
                    frameOffset = frameOffset - (exp.size.value+1);
                    exp.nestLevel =  1;
                    emitRM("LDC", 2, exp.size.value, 0, "Get array length");
                    emitRM("ST", 2, exp.offset, 5+((1+(getNestLevel(exp.name)))%2), "Assign: Array length to the beginning of array index");
                }
                else{
                    frameOffset--;
                    exp.nestLevel =  1;

                    // emitRM("LDC", 2, exp.size.value, 0, "Get array length");
                    // emitRM("ST", 2, exp.offset, 5+((1+(getNestLevel(exp.name)))%2), "Assign: Array length to the beginning of array index");
                }
            }

            ArrayList<Integer> emptyIntegerlist = new ArrayList<Integer>();
            TableSymb temp = new TableSymb(exp.name,exp.typ.type,level,emptyIntegerlist,1);
            temp.offset = exp.offset;
            temp.nestLevel = exp.nestLevel;
            if(exp.size != null){
                temp.arraylength = ((IntExp)exp.size).value;
            }
            else{
                temp.functionarraycall = 1;
            }

            if(!table.containsKey(exp.name)){
                ArrayList<TableSymb> tableArray = new ArrayList<TableSymb>();
                tableArray.add(temp);
                table.put(exp.name,tableArray);
            }
            else{
                table.get(exp.name).add(temp);
            }
        }
        else{
            System.err.println("ERROR: Redefined index variable: "+exp.name+INLINE+(exp.row+1)+INCOL+exp.col);
        }
    }


    //Miscellaneous classes || listclasses

    //DecList
    public void visit(DecList declist, int level, boolean isAddr, int offset){
        prelude("fileNameTM");
        while(declist != null) {
            if(declist.head != null){
                declist.head.accept(this, level, false, 0);
            }
            declist = declist.tail;
        }
        finale(); 
    }
    //VarDecList
    public void visit(VarDecList vardeclist, int level, boolean isAddr, int offset){
        while(vardeclist != null) {
            if(vardeclist.head != null){
                vardeclist.head.accept(this, level, false, 0);
            }
            vardeclist = vardeclist.tail;
        } 
    }
    //ExpList
    public void visit(ExpList expList, int level, boolean isAddr, int offset){
        while(expList != null) {
            if(expList.head != null){
                expList.head.accept(this, level, false, 0);
            }
            expList = expList.tail;
        } 
    }
}
