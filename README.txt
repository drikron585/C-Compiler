/*
    Yeong Jin Park 1002691
    yeongjin@uoguelph.ca

    Ron Drik 1029745
    rdrik@uoguelph.ca

    Milestone 2
*/

To build our program, first adjust the makefile accordingly for the directories in which your CUP, CLASSPATH and FLEX are located, before running "make". Run make clean to delete files created by make.

Run "make" to build the required files.


------------------------------------------------------------------------------------------------
To test the parser code and generate an AST for a program titled "fac.cm", input 
"java -classpath /usr/share/java/cup.jar:. CM fac.cm -a" 

The syntax tree and any detected errors will be printed on the screen.

To test the semantic analyzer code and generate the symbol table for a program titled "fac.cm", input 
"java -classpath /usr/share/java/cup.jar:. CM fac.cm -s" 

The symbol table and any detected semantic errors will be printed on the screen.


"java -classpath /usr/share/java/cup.jar:. CM test.cm -c > TM_Simulator/test_MINE.tm"


*If -a/-s are not specified, will default to -s
------------------------------------------------------------------------------------------------

5 test cm files are included, 1 is correct, 2-4 have 3 errors each and 5 has 7, as described
within the files.

!!! Our code is based upon Professor Fei Song's Tiny parser implementation!!!
