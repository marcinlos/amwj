grammar NPJ;

options {
  language = Java;
}

tokens {
    SEMI    = ';' ;
}

@header {
package mlos.amw.npj.parser;

import mlos.amw.npj.ast.*;
}

@lexer::header {
package mlos.amw.npj.parser;
}
 

// Lexer

INTEGER_LITERAL : (DIGIT)+ ;
 
WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+   { $channel = HIDDEN; } ;
 
STRING_LITERAL : '"' (~ ('\n' | '"' ))* '"' ;

ID : 'a'..'z' ('a'..'z' | '0'..'9')* ;

fragment DIGIT  : '0'..'9' ;


// Parser

program returns [List<Statement> prog] 
:                               { List<Statement> stms = new ArrayList<>(); } 
  (s = statement ';'            { stms.add($s.stm); } 
  )* EOF                        { $prog = stms; } 
;
        
statement returns [Statement stm]
: var_decl                        { $stm = $var_decl.decl; }
| assignment                      { $stm = $assignment.val; }
| 'Print' s = STRING_LITERAL      { $stm = Print.literal($s.text); }
| 'Print' ID                      { $stm = Print.var($ID.text); }
| 'HeapAnalyze'                   { $stm = HeapAnalyze.VALUE; }
| 'Collect'                       { $stm = Collect.VALUE; }
| /* epsilon */
;
          
var_decl returns [Decl decl]
: 'VarDeclT' ID                    { $decl = Decl.newTVar($ID.text); }
| 'VarDeclS' ID s = STRING_LITERAL { $decl = Decl.newSVar($ID.text, $s.text); }
| 'VarDeclS' ID 'NULL'             { $decl = Decl.newSVar($ID.text); }               
;
                     
assignment returns [Assignment val]
: deref '=' rvalue   { $val = new Assignment($deref.target, $rvalue.val); }
;

rvalue returns [RValue val]
: deref                  { $val = $deref.target; }
| 'NULL'                 { $val = Null.VALUE; }
| n = INTEGER_LITERAL    { $val = IntLiteral.from($n.text); }
| STRING_LITERAL         { $val = new StringLiteral($n.text); }
;
       
deref returns [Deref target]
: ID              { $target = new Deref($ID.text); }
| ID '.' deref2   { $target = new Deref($ID.text, $deref2.target); }
; 
        
deref2 returns [Deref target]
: /* epsilon */          { $target = null; }
| ID '.' d = deref2      { $target = new Deref($ID.text, $d.target); } 
;
 


