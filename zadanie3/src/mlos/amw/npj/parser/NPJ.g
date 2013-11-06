grammar NPJ;

options {
  language = Java;
}

tokens {
    SEMI    = ';' ;
}

@header {
package mlos.amw.npj.parser;
}

@lexer::header {
package mlos.amw.npj.parser;
}
 

// Lexer

INTEGER_LITERAL : (DIGIT)+ ;
 
WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+   { $channel = HIDDEN; } ;
 
STRING_LITERAL : '"' (~ ('\n' | '"' ))* '"' ;

STRING_CONSTANT : 'a'..'z' ('a'..'z' | '0'..'9')* ;

fragment DIGIT  : '0'..'9' ;


// Parser

program : (statement ';')* EOF ;
        
statement : var_decl
          | assignment
          | 'Print' STRING_LITERAL
          | 'Print' STRING_CONSTANT
          | 'HeapAnalyze'
          | 'Collect' 
          | /* epsilon */
          ;
          
var_decl : 'VarDeclT' STRING_CONSTANT
         | 'VarDeclS' STRING_CONSTANT STRING_LITERAL
         | 'VarDeclS' STRING_CONSTANT 'NULL'
         ;
                     
assignment : deref '=' rvalue ;

rvalue : deref
       | 'NULL'
       | INTEGER_LITERAL
       | STRING_LITERAL
       ;
       
deref : STRING_CONSTANT ('.' STRING_CONSTANT)* ; 
 


