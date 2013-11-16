grammar NPJ;

options {
  language = Java;
}

tokens {
    SEMI    = ';' ;
}

@header {
package mlos.amw.npj.parser;

import java.io.IOException;
import java.io.InputStream;
import mlos.amw.npj.ast.*;
}

@lexer::header {
package mlos.amw.npj.parser;
}
 
 
@members {
    public static List<Statement> parse(InputStream input) throws IOException, 
            RecognitionException {
        NPJLexer lex = new NPJLexer(new ANTLRInputStream(input));
        CommonTokenStream tokens = new CommonTokenStream(lex);
 
        NPJParser parser = new NPJParser(tokens);
        return parser.program();
    }
    
    private static String trim(String s) {
        return s.substring(1, s.length() - 1);
    }
}

// Lexer

INTEGER_LITERAL : (DIGIT)+ ;
 
WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+   { $channel = HIDDEN; } ;

COMMENT : '//' ~('\n'|'\r')* '\r'? '\n' { $channel = HIDDEN; } ;

BLOCK_COMMENT : '/*' .* '*/' { $channel = HIDDEN; } ;
 
STRING_LITERAL : '"' (~ ('\n' | '"' ))* '"';

ID : 'a'..'z' ('a'..'z' | '0'..'9')* ;

fragment DIGIT  : '0'..'9' ;


// Parser

program returns [List<Statement> prog] 
:                               { List<Statement> stms = new ArrayList<Statement>(); } 
  (s = statement ';'            { stms.add($s.stm); } 
  )* EOF                        { $prog = stms; } 
;
        
statement returns [Statement stm]
: var_decl                        { $stm = $var_decl.decl; }
| assignment                      { $stm = $assignment.val; }
| 'Print' s = string_literal      { $stm = Print.literal($s.str); }
| 'Print' ID                      { $stm = Print.var($ID.text); }
| 'HeapAnalyze'                   { $stm = HeapAnalyze.VALUE; }
| 'Collect'                       { $stm = Collect.VALUE; }
| /* epsilon */
;
          
var_decl returns [Decl decl]
: 'VarDeclT' ID                    { $decl = Decl.newTVar($ID.text); }
| 'VarDeclS' ID s = string_literal { $decl = Decl.newSVar($ID.text, $s.str); }
| 'VarDeclS' ID 'NULL'             { $decl = Decl.newSVar($ID.text); }               
;
                     
assignment returns [Assignment val]
: deref '=' rvalue   { $val = new Assignment($deref.target, $rvalue.val); }
;

rvalue returns [RValue val]
: deref                  { $val = $deref.target; }
| 'NULL'                 { $val = Null.VALUE; }
| n = INTEGER_LITERAL    { $val = IntLiteral.from($n.text); }
| s = string_literal     { $val = new StringLiteral($s.str); }
;
       
deref returns [Deref target]
: ID deref2   { $target = new Deref($ID.text, $deref2.target); }
; 
        
deref2 returns [Deref target] 
: /* epsilon */          { $target = null; }
| '.' ID d = deref2      { $target = new Deref($ID.text, $d.target); } 
;

string_literal returns [String str]
: s = STRING_LITERAL         { $str = trim($s.text); }
;


