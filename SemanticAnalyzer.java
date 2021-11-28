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

import absyn.*;

public class SemanticAnalyzer implements AbsynVisitor {

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

    public SemanticAnalyzer(){
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

    //Indent function
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
                    indent(level);
                    if(currentSymb.decType == 0){
                        if(currentSymb.type == 0){
                            System.out.println(currentSymb.name+" : int");
                        }
                        else if(currentSymb.type == 1){
                            System.out.println(currentSymb.name+" : void");
                        }
                    }
                    if(currentSymb.decType == 1){
                        System.out.println(currentSymb.name+"[] : int");
                    }
                }
            }
        }
    }

    public int redefinedVar(String varName,int level){
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


    //Classes that extends Absyn
    
    //NameTy
    public void visit(NameTy exp,int level, boolean isAddr, int offset){
        // do nothing
    }


    //Classes that extends Var

    //SimpleVar
    public void visit(SimpleVar exp,int level, boolean isAddr, int offset){    
        // check if the name is in the symbol table, if not, print error (trying to use undefined var)
    }
    //IndexVar
    public void visit(IndexVar exp,int level, boolean isAddr, int offset){
        exp.index.accept(this, level, false, 0);
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
    public void visit(NilExp exp,int level, boolean isAddr, int offset){
        // do nothing
    }
    //VarExp
    public void visit(VarExp exp,int level, boolean isAddr, int offset){
        if (exp.variable != null){
            exp.variable.accept(this, level, false, 0);
        }
    }
    //IntExp
    public void visit(IntExp exp,int level, boolean isAddr, int offset){
        // do nothing
    }
    //CallExp
    public void visit(CallExp exp,int level, boolean isAddr, int offset){
        // dont understand
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
                else if(args.head instanceof CallExp){
                    CallExp call = (CallExp) args.head;
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
    }   
    //OpExp
    public void visit(OpExp exp,int level, boolean isAddr, int offset){
        exp.left.accept(this, level, false, 0);
        exp.right.accept(this, level, false, 0);

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
    public void visit(AssignExp exp,int level, boolean isAddr, int offset){

        exp.lhs.accept(this, level, false, 0);
        exp.rhs.accept(this, level, false, 0);
        
        if(exp.lhs instanceof SimpleVar){
            SimpleVar templhs = (SimpleVar) exp.lhs;
            if(undefinedVar(templhs.name) == 1){
                System.err.println("ERROR: Undefined simple variable: "+templhs.name+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            if(undefinedVar(templhs.name) == 2){
                System.err.println("ERROR: Invalid(void) simple variable: "+templhs.name+INLINE+(exp.row+1)+INCOL+exp.col);
            }
        }
        else{
            IndexVar templhs = (IndexVar) exp.lhs;
            if(undefinedVar(templhs.name) == 1){
                System.err.println("ERROR: Undefined index variable: "+templhs.name+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            if(undefinedVar(templhs.name) == 2){
                System.err.println("ERROR: Invalid(void) index variable: "+templhs.name+INLINE+(exp.row+1)+INCOL+exp.col);
            }
        }
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
                if(undefinedVar(temp.name) == 2){
                    System.err.println("ERROR: Invalid(void) simple variable cannot be assigned to anything: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
            else if(varExpName.variable instanceof IndexVar){
                IndexVar temp = (IndexVar)varExpName.variable;
                if(undefinedVar(temp.name) == 1){
                    System.err.println("ERROR: Undefined simple variable: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
                if(undefinedVar(temp.name) == 2){
                    System.err.println("ERROR: Invalid(void) simple variable cannot be assigned to anything: "+temp.name+INLINE+(exp.row+1)+INCOL+exp.col);
                }
            }
        }
    }
    //IfExp
    public void visit(IfExp exp,int level, boolean isAddr, int offset){

        indent(level);
        System.out.println("Entering a new if block:");

        level++;

        exp.test.accept(this, level, false, 0);

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
        else if(exp.test instanceof CallExp){

        }
        exp.thenB.accept(this, level, false, 0);

        print(level);
        delete(level);

        level--;

        indent(level);
        System.out.println("Exiting the if block:");

        if (exp.elseB != null && !(exp.elseB instanceof NilExp)){

            indent(level);
            System.out.println("Entering a new else block: ");

            level++;

            exp.elseB.accept(this, level, false, 0);

            print(level);
            delete(level);

            level--;

            indent(level);
            System.out.println("Leaving the else block");
        }
    }
    //WhilExp
    public void visit(WhileExp exp,int level, boolean isAddr, int offset){

        indent(level);
        System.out.println("Entering a new while block: ");

        level++;

        exp.test.accept(this, level, false, 0);

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

        print(level);
        delete(level);

        level--;

        indent(level);
        System.out.println("Leaving the while block");
        
    }
    //ReturnExp
    public void visit(ReturnExp exp,int level, boolean isAddr, int offset){
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

    }
    //CompoundExp
    public void visit(CompoundExp exp,int level, boolean isAddr, int offset){
        if (exp.decs != null && exp.exps != null){
            //level++;
        }
        if (exp.decs != null){
            exp.decs.accept(this, level, false, 0);
        }
        if (exp.exps != null){
            exp.exps.accept(this, level, false, 0);
        }
    }
    

    //Classes that extends Dec

    //FunctionDec
    public void visit(FunctionDec exp,int level, boolean isAddr, int offset){
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



            if(!table.containsKey(exp.func)){
                ArrayList<TableSymb> tableArray = new ArrayList<TableSymb>();
                tableArray.add(temp);
                table.put(exp.func,tableArray);
            }
            else{
                table.get(exp.func).add(temp);
            }

            indent(level);
            System.out.println("Entering scope for function " + exp.func + " : ");
            FunctionScope = new currFunction();
            FunctionScope.name = exp.func;
            FunctionScope.type = exp.result.type;
            FunctionScope.isReturned = 0;
            level++;

            VarDecList instanceParamsRun = exp.params;
            while(instanceParamsRun != null){
                instanceParamsRun.head.accept(this, level, false, 0);
                instanceParamsRun = instanceParamsRun.tail;
            }
            
            exp.body.accept(this, level, false, 0);

            print(level);
            delete(level);
            if(FunctionScope.isReturned == 0 && FunctionScope.type == 0){
                System.err.println("ERROR: No return statement in int type function declaration:"+exp.func+INLINE+(exp.row+1)+INCOL+exp.col);
            }
            indent(level);
            System.out.print(exp.func+"(");
            VarDecList CurrentParameter = exp.params;
            while(CurrentParameter != null) {
                System.out.print("int "+CurrentParameter.head.name);
                CurrentParameter = CurrentParameter.tail;
                if(CurrentParameter != null){
                    System.out.print(", ");
                }
            }
            if(exp.result.type == NameTy.VOID){
                System.out.println(") -> void");
            }
            else if(exp.result.type == NameTy.INT){
                System.out.println(") -> int");
            }
            level--;

            indent(level);
            System.out.println("Leaving the function scope "+ exp.func);
        }
        else{
            System.err.println("ERROR: Redefined function: "+exp.func+INLINE+(exp.row+1)+INCOL+exp.col);
        }
    }


    //Classes that extends VarDec

    //SimpleDec
    public void visit(SimpleDec exp,int level, boolean isAddr, int offset){
        if(redefinedVar(exp.name,level) == 0){
            ArrayList<Integer> emptyIntegerlist = new ArrayList<Integer>();
            TableSymb temp = new TableSymb(exp.name,exp.typ.type,level,emptyIntegerlist,0);
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
    public void visit(ArrayDec exp,int level, boolean isAddr, int offset){
        if(redefinedVar(exp.name,level) == 1){
            System.err.println("ERROR: "+exp.name+" has already been declared as a simple variable: "+INLINE+(exp.row+1)+INCOL+exp.col);
        }
        else if(exp.typ.type == NameTy.VOID){
            System.err.println("ERROR: Index variables cannot have a void type: "+exp.name+INLINE+(exp.row+1)+INCOL+exp.col);
        }
        else if(redefinedVar(exp.name,level) == 0){
            ArrayList<Integer> emptyIntegerlist = new ArrayList<Integer>();
            TableSymb temp = new TableSymb(exp.name,exp.typ.type,level,emptyIntegerlist,1);
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
    public void visit(DecList declist,int level, boolean isAddr, int offset){
        while(declist != null) {
            if(declist.head != null){
                declist.head.accept(this, level, false, 0);
            }
            declist = declist.tail;
        } 
    }
    //VarDecList
    public void visit(VarDecList vardeclist,int level, boolean isAddr, int offset){
        while(vardeclist != null) {
            if(vardeclist.head != null){
                vardeclist.head.accept(this, level, false, 0);
            }
            vardeclist = vardeclist.tail;
        } 
    }
    //ExpList
    public void visit(ExpList expList,int level, boolean isAddr, int offset){
        while(expList != null) {
            if(expList.head != null){
                expList.head.accept(this, level, false, 0);
            }
            expList = expList.tail;
        } 
    }
}

