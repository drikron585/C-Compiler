/*
   Yeong Jin Park 1002691
   yeongjin@uoguelph.ca

   Ron Drik 1029745
   rdrik@uoguelph.ca

*/
import absyn.*;

public class ShowTreeVisitor implements AbsynVisitor {

    final static int SPACES = 4;

    //Indent function
    private void indent( int level ) {
        for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
    }


    //Classes that extends Absyn
    
    //NameTy
    public void visit(NameTy exp,int level, boolean isAddr, int offset){
        indent(level);
    }


    //Classes that extends Var

    //SimpleVar
    public void visit(SimpleVar exp,int level, boolean isAddr, int offset){
        indent(level);
        System.out.println("SimpleVar: " + exp.name);
    }
    //IndexVar
    public void visit(IndexVar exp,int level, boolean isAddr, int offset){
        indent(level);
        System.out.println("IndexVar: " + exp.name);
        level++;
        exp.index.accept(this, level, false, 0);
    }


    //Classes that extends Exp

    //NilExp
    public void visit(NilExp exp,int level, boolean isAddr, int offset){
        indent(level);
        System.out.println("NilExp:");
    }
    //VarExp
    public void visit(VarExp exp,int level, boolean isAddr, int offset){
        indent(level);
        System.out.println("VarExp: ");
        level++;
        if (exp.variable != null){
            exp.variable.accept(this, level, false, 0);
        }
    }
    //IntExp
    public void visit(IntExp exp,int level, boolean isAddr, int offset){
        indent(level);
        System.out.println("IntExp: " + exp.value ); 
    }
    //CallExp
    public void visit(CallExp exp,int level, boolean isAddr, int offset){
        indent(level);
        System.out.println("CallExp: " + exp.func);
        level++;
        if (exp.args != null){
            exp.args.accept(this, level, false, 0);
        }
    }   
    //OpExp
    public void visit(OpExp exp,int level, boolean isAddr, int offset){
        indent(level);
        System.out.print("OpExp:"); 
        switch(exp.op) {
            case OpExp.PLUS:
                System.out.println( " + " );
                break;
            case OpExp.MINUS:
                System.out.println( " - " );
                break;
            case OpExp.MUL:
                System.out.println( " * " );
                break;
            case OpExp.DIV:
                System.out.println( " / " );
                break;
            case OpExp.EQ:
                System.out.println( " = " );
                break;
            case OpExp.EQEQ:
                System.out.println( " == " );
                break;
            case OpExp.NE:
                System.out.println( " != " );
                break;
            case OpExp.LT:
                System.out.println( " < " );
                break;
            case OpExp.LE:
                System.out.println( " <= " );
                break; 
            case OpExp.GT:
                System.out.println( " > " );
                break;
            case OpExp.GE:
                System.out.println( " <= " );
            case OpExp.ERROROP:
                System.out.println( " ERROR " );
                break;
            default:
            System.out.println("Unrecognized operator at line " + exp.col);
        }
        level++;
        exp.left.accept(this, level, false, 0);
        exp.right.accept(this, level, false, 0);
    }
    //AssignExp
    public void visit(AssignExp exp,int level, boolean isAddr, int offset){
        indent(level);
        System.out.println("AssignExp:");
        level++;
        exp.lhs.accept(this, level, false, 0);
        exp.rhs.accept(this, level, false, 0);
      }
    //IfExp
    public void visit(IfExp exp,int level, boolean isAddr, int offset){
        indent(level);
        if(exp.test == null){
            System.out.println("IfExp: ERROR");
        }
        else{
            System.out.println("IfExp:");
        }
        level++;
        if(exp.test != null){
            exp.test.accept(this, level, false, 0);
        }
        exp.thenB.accept(this, level, false, 0);
        if (exp.elseB != null){
            exp.elseB.accept(this, level, false, 0);
        }
    }
    //WhilExp
    public void visit(WhileExp exp,int level, boolean isAddr, int offset){
        indent(level);
        if(exp.test == null){
            System.out.println("WhileExp: ERROR");
        }
        else{
            System.out.println("WhileExp:");
        }
        level++;
        if (exp.test != null){
            exp.test.accept(this, level, false, 0);
        }
        if (exp.body != null){
            exp.body.accept(this, level, false, 0);
        }
    }
    //ReturnExp
    public void visit(ReturnExp exp,int level, boolean isAddr, int offset){
        indent(level);
        if(exp.error == 0){
            System.out.println("ReturnExp:"); 
        }
        else if(exp.error == 1){
            System.out.println("Error-ReturnExp:");
        } 
        level++;
        if (exp.exp != null){
            exp.exp.accept(this, level, false, 0);
        }
    }
    //CompoundExp
    public void visit(CompoundExp exp,int level, boolean isAddr, int offset){
        indent(level);
        System.out.println("CompoundExp:");
        if (exp.decs != null && exp.exps != null){
            level++;
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
        indent(level);
        if (exp.result.type == NameTy.VOID){
            System.out.println("FunctionDec: Void-"+exp.func);
        }
        else if (exp.result.type == NameTy.INT){
            System.out.println("FunctionDec: Int-"+exp.func); 
        }
        level++;
        if (exp.params != null){
            exp.params.accept(this, level, false, 0);
        }
        if (exp.body != null){
          exp.body.accept(this, level, false, 0);
        }
    }


    //Classes that extends VarDec

    //SimpleDec
    public void visit(SimpleDec exp,int level, boolean isAddr, int offset){
        indent(level);
        if (exp.typ.type == NameTy.VOID){
            System.out.println("SimpleDec: VOID-"+exp.name); 
        }
        else if (exp.typ.type == NameTy.INT){
            System.out.println("SimpleDec: INT-"+exp.name);
        }
        else if(exp.typ.type == NameTy.ERROR){
            System.out.println("SimpleDec: Invalid Variable - "+exp.name);
        }
    }
    //ArrayDec
    public void visit(ArrayDec exp,int level, boolean isAddr, int offset){
        indent(level);
        String ty = new String("");
        if (exp.typ.type == NameTy.INT){
            ty = "INT";
        }
        else if (exp.typ.type == NameTy.VOID){
            ty = "VOID";
        }
        else if (exp.typ.type == NameTy.ERROR){
            ty = "ERROR";
        }
        if (exp.size != null){
            if (exp.size != null){
                System.out.println("ArrayDec: " + ty + "-" + exp.name + "[" + exp.size.value + "]");
            }
        }
        else{
            System.out.println("ArrayDec: " + ty + "-" + exp.name + "[]");
        }
    }


    //Miscellaneous classes || listclasses

    //DecList
    public void visit( DecList declist,int level, boolean isAddr, int offset ) {
        while(declist != null) {
            if(declist.head != null){
                declist.head.accept(this, level, false, 0);
            }
            declist = declist.tail;
        } 
    }
    //VarDecList
    public void visit( VarDecList vardeclist,int level, boolean isAddr, int offset ) {
        while(vardeclist != null) {
            if(vardeclist.head != null){
                vardeclist.head.accept(this, level, false, 0);
            }
            vardeclist = vardeclist.tail;
        } 
    }
    //ExpList
    public void visit( ExpList expList,int level, boolean isAddr, int offset ) {
        while(expList != null) {
            if(expList.head != null){
                expList.head.accept(this, level, false, 0);
            }
            expList = expList.tail;
        } 
    }
}

