/*
   Yeong Jin Park 1002691
   yeongjin@uoguelph.ca

   Ron Drik 1029745
   rdrik@uoguelph.ca

   File Name: CM.java
   java -classpath /usr/share/java/cup.jar:. CM 1.cm

*/
import java.io.*;
import absyn.*;
   
class CM {
    public final static boolean SHOW_TREE = true;
    static public void main(String argv[]) {    
        String FileName = "";
        String ParserType = "";
        int argc=0;
        for (String s: argv) {
            if(argc == 0){
                FileName = s;
            }
            else if(argc == 1){
                ParserType = s;
            }
            argc++;
        }
        /* Start the parser */
        try {
            parser p = new parser(new Lexer(new FileReader(FileName)));
            Absyn result = (Absyn)(p.parse().value);      
            // System.out.println("");
            if(ParserType.equals("-a")){
                if(SHOW_TREE && result != null){
                System.out.println("The abstract syntax tree is:");
                ShowTreeVisitor visitor = new ShowTreeVisitor();
                result.accept(visitor, 0,false,0); 
                }
            }
            else if(ParserType.equals("-s")){
                if(SHOW_TREE && result != null){
                    System.out.println("The abstract syntax tree is:\n");
                    System.out.println("Entering the global scope:");
                    SemanticAnalyzer visitor = new SemanticAnalyzer();
                    result.accept(visitor, 0,false,0); 
                    System.out.println("Leaving the global scope");
                }
            }
            else if(ParserType.equals("-c")){
                if(SHOW_TREE && result != null){
                    //System.out.println("The TM gen syntax tree is:\n");
                    //System.out.println("Beginning of code gen:");
                    TMgen visitor = new TMgen();
                    result.accept(visitor, 0,false,0); 
                    //System.out.println("End of code gen");
                }
            }
        } 
        catch (Exception e) {
            /* do cleanup here -- possibly rethrow e */
            e.printStackTrace();
        }
  }
}


