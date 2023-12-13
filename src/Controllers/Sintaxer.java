/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Exceptions.SemanticError;
import Exceptions.SintaticError;
import Models.Tag;
import Models.Token;
import Models.Word;
import Utils.Utils;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Ian Silva
 */
public class Sintaxer {
    
    private ArrayList<Token> tokenList;
    private Token current;
    private int tokenIndex = 0;
    Hashtable<String, Word> words;
    
    private int offset;

    public Sintaxer(ArrayList<Token> tokenList, Hashtable<String, Word> words) {
        this.tokenList = tokenList;
        this.words = words;
    }

    private void getNextToken(int expected) throws SintaticError {
        if (this.current.tag == Tag.COMMENTBLOCK){
            this.tokenIndex ++;
            if(this.tokenIndex < tokenList.size()){
                this.current = tokenList.get(tokenIndex);
            }
        }
        if (this.current.tag == expected) {
            this.tokenIndex ++;
            if(this.tokenIndex < tokenList.size()){
                this.current = tokenList.get(tokenIndex);
            }
        }
        else {
            throw new SintaticError("Expected tag " + expected + ", got " + Character.toString((char)this.current.tag) + " at line "+this.current.line);
        }
    }
    
    
    // program ::= class identifier [decl-list] body
    public void program() throws SintaticError, SemanticError {
        //Inicia o offset como 0, para geração de código
        this.offset = 0;
        //Little gambiarra, já que o início do arquivo estava gerando um token em branco às vezes
        boolean finded = false;
        for(int index = 0; index < this.tokenList.size() && finded == false; index++){
            this.current = this.tokenList.get(index);
            if(this.current.tag == 0){
            //    this.current = this.tokenList.get(index);
            } else {
                this.tokenIndex = index;
                this.current = this.tokenList.get(index);
                finded = true;
            }
        }
        //Fim da gambiarra
        
        
        getNextToken(Tag.CLASS);
        getNextToken(Tag.ID);
        
        decl_list();

        body();
    }

    // decl-list ::= decl ";" { decl ";"}
    private void decl_list() throws SintaticError {
        decl();
        //getNextToken(Tag.PONTOVIRGULA);
        while (current.tag == Tag.PONTOVIRGULA) {
            getNextToken(Tag.PONTOVIRGULA);
            if(current.tag == Tag.INT || current.tag == Tag.FLOAT || current.tag == Tag.STRING){
                decl();
            }
        }
        //getNextToken(Tag.PONTOVIRGULA);
        
        
    }

    // decl ::= type ident-list
    private void decl() throws SintaticError {
        int type = type();
        ArrayList<Token> identifiers = ident_list(type);
    }

    // ident-list ::= identifier {"," identifier}
    private ArrayList<Token> ident_list(int type) throws SintaticError {
        ArrayList<Token> identifiers = new ArrayList<Token> ();
        if (this.current.tag == Tag.ID){
            this.current.type = type;
            identifiers.add(this.current);
        }
        getNextToken(Tag.ID);
        //getNextToken(Tag.VIRGULA);
        while (current.tag == Tag.VIRGULA) {
            getNextToken(Tag.VIRGULA);
            if (this.current.tag == Tag.ID) {
                this.current.type = type;
                identifiers.add(this.current);
            }
            getNextToken(Tag.ID);
        }
        return identifiers;
    }

    // type ::= int | string | float
    private int type() throws SintaticError {
        switch (current.tag) {
            case Tag.INT:
                getNextToken(Tag.INT);
                return Tag.INT;
            case Tag.FLOAT:
                getNextToken(Tag.FLOAT);
                return Tag.FLOAT;
            case Tag.STRING:
                getNextToken(Tag.STRING);
                return Tag.STRING;
            default:
                throw new SintaticError("Expected Type (int, float or string), got " + this.current + " at line "+this.current.line);
        }
    }

    // body ::= "{" stmt-list "}"
    private void body() throws SintaticError, SemanticError {
        getNextToken(Tag.CHAVEABRE);
        stmt_list();
        getNextToken(Tag.CHAVEFECHA);
    }

    // stmt-list ::= stmt ";" { stmt ";" }
    private void stmt_list() throws SintaticError, SemanticError {
        stmt();
        while (current.tag == Tag.PONTOVIRGULA) {
            getNextToken(Tag.PONTOVIRGULA);
            stmt();
        }
    }

    // stmt ::= assign-stmt | if-stmt | do-stmt | read-stmt | write-stmt
    private void stmt() throws SintaticError, SemanticError {
        switch (current.tag) {
            case Tag.ID :
                assign_stmt();
                break;
            case Tag.IF :
                if_stmt();
                break;
            case Tag.DO:
                do_stmt();
                break;
            case Tag.READ:
                read_stmt();
                break;
            case Tag.WRITE:
                write_stmt();
                break;
            case Tag.CHAVEFECHA: //teste
                break;
            default:
                throw new SintaticError(this.current + " is an Invalid Statement Start" + " at line "+this.current.line);
        }
    }

    // assign-stmt ::= identifier "=" simple_expr
    private void assign_stmt() throws SintaticError, SemanticError {
        int typeId = words.get(((Word)current).lexema).type;
        if(words.get(((Word)current).lexema) == null || words.get(((Word)current).lexema).type == 0){
            throw new SemanticError("Identifier ["+current.toString() +"] not declared at line " + current.line);
        }
        getNextToken(Tag.ID);
        getNextToken(Tag.ASSIGN);
        int typeExp = simple_expr();
        if(typeId != typeExp){
            throw new SemanticError("Invalid type operators at line " + current.line + "[Expected "+typeId+" got "+typeExp);
        }
    }

    // if-stmt ::= if "(" condition ")" "{" stmt-list "}" [else "{" stmt-list "}"]
    private void if_stmt() throws SintaticError, SemanticError {
        getNextToken(Tag.IF);
        getNextToken(Tag.PARABRE);
        int type = condition();
        if(type != Tag.BOOL){
            throw new SemanticError("Boolean expression required at line " + current.line);
        }
        getNextToken(Tag.PARFECHA);
        getNextToken(Tag.CHAVEABRE);
        stmt_list();
        getNextToken(Tag.CHAVEFECHA);
        if (current.tag == Tag.ELSE) {
            getNextToken(Tag.ELSE);
            getNextToken(Tag.CHAVEABRE);
            stmt_list();
            getNextToken(Tag.CHAVEFECHA);
        }
    }

    // condition ::= expression
    private int condition() throws SintaticError, SemanticError {
        int type = expression();
        return type;
    }

    // do-stmt ::= do "{" stmt-list "}" do-suffix
    private void do_stmt() throws SintaticError, SemanticError {
        getNextToken(Tag.DO);
        getNextToken(Tag.CHAVEABRE);
        stmt_list();
        getNextToken(Tag.CHAVEFECHA);
        do_suffix();
    }

    // do-suffix ::= while "(" condition ")"
    private void do_suffix() throws SintaticError, SemanticError {
        getNextToken(Tag.WHILE);
        getNextToken(Tag.PARABRE);
        int type = condition();
        if(type != Tag.BOOL){
            throw new SemanticError("Boolean expression required at line " + current.line);
        }
        getNextToken(Tag.PARFECHA);
    }

    // read-stmt ::= read "(" identifier ")"
    private void read_stmt() throws SintaticError, SemanticError {
        getNextToken(Tag.READ);
        getNextToken(Tag.PARABRE);
        if(words.get(((Word)current).lexema) == null || words.get(((Word)current).lexema).type == 0){
            throw new SemanticError("Identifier ["+current.toString() +"] not declared at line " + current.line);
        }
        getNextToken(Tag.ID);
        getNextToken(Tag.PARFECHA);
    }

    // write-stmt ::= write "(" writable ")"
    private void write_stmt() throws SintaticError, SemanticError {
        getNextToken(Tag.WRITE);
        getNextToken(Tag.PARABRE);
        writable();
        getNextToken(Tag.PARFECHA);
    }

    // writable ::=  simple-expr | literal
    private void writable() throws SintaticError, SemanticError {
        if(current.tag == Tag.LITERAL){
            getNextToken(Tag.LITERAL);
        } else {
            simple_expr();
        }
        
    }

    // expression ::= simple-expr | simple-expr relop simple-expr
    private int expression() throws SintaticError, SemanticError {
        int type = simple_expr();
        switch (current.tag) {
            case Tag.EQ, Tag.GT, Tag.GE, Tag.LT, Tag.LE, Tag.NE:
                relop();
                int type2 = simple_expr();
                if(type == type2){
                    //caso simple-expr relop simple-expr
                    return Tag.BOOL;
                } else {
                    throw new SemanticError("Invalid type operators at line " + current.line + "[Expected "+type+" got "+type2);
                }
            default: 
            //nothing
        }
        //caso simple-expr
        return type;
    }

    // simple-expr ::= term | simple-expr addop term
    private int simple_expr() throws SintaticError, SemanticError {
        int type = term();
        type = recursive_simple_expr(type);
        return type;
    }
    
    private int recursive_simple_expr(int typeAnt) throws SintaticError, SemanticError {
        int type = typeAnt;
        switch (current.tag) {
            case Tag.MAIS, Tag.MENOS, Tag.OR:
                int tagAux = current.tag;
                addop();
                type = term();
//                if(type == typeAnt) {
//                    recursive_simple_expr(type);
//                    break;
//                } else if(tagAux == Tag.MAIS){
                if(tagAux == Tag.MAIS){
                    if(Utils.typeString(typeAnt) || Utils.typeString(type)){
                        type = Tag.STRING;
                        type = recursive_simple_expr(type);
                    } else if (Utils.typeNumeric(type) && Utils.typeNumeric(typeAnt) && type == typeAnt){
                        type = recursive_simple_expr(type);
                        break;
                    } else if(Utils.typeNumeric(type) && Utils.typeNumeric(typeAnt) && type != typeAnt) {
                        throw new SemanticError("Invalid type operators at line " + current.line + "[Expected "+typeAnt+" got "+type);
                    }else {
                        throw new SemanticError("Invalid type operators at line " + current.line + "[Expected numeric values");
                    }
                } else if (tagAux == Tag.MENOS || tagAux == Tag.OR){
                    if((!Utils.typeNumeric(typeAnt) || !Utils.typeNumeric(type)) || type != typeAnt ){
                        throw new SemanticError("Invalid type operators at line " + current.line + "[Expected numeric values");
                    } else {
                        type = recursive_simple_expr(type);
                        break;
                    }
                }
                else {
                    throw new SemanticError("Invalid type operators at line " + current.line + "[Expected "+typeAnt+" got "+type);
                }
        }
        return type;
    }

    // term ::= factor-a | term mulop factor-a
    private int term() throws SintaticError, SemanticError {
        int type = factor_a();
        type = recursive_term(type);

        return type;
    }
    
    private int recursive_term(int typeAnt) throws SintaticError, SemanticError{
        int type = typeAnt;
        boolean mark = false;
        switch(current.tag){
            case Tag.MULT, Tag.DIV, Tag.AND:
                int tagAux = current.tag;
                mulop();
                type = factor_a();
                if(type == typeAnt && !(tagAux == Tag.DIV && type == Tag.INT && typeAnt == Tag.INT)){
                    recursive_term(type);
                    break;
                } else if (tagAux == Tag.DIV && type == Tag.INT && typeAnt == Tag.INT){
                    //type = Tag.FLOAT;
                    mark = true;
                    recursive_term(type);
                    break;
                } else if (!Utils.typeNumeric(typeAnt) || !Utils.typeNumeric(type)){
                    throw new SemanticError("Invalid type operators at line " + current.line + "[Expected numeric values");
                } 
                else {
                    throw new SemanticError("Invalid type operators at line " + current.line + "[Expected "+typeAnt+" got "+type);
                }
        }
        if(mark == true){
            return Tag.FLOAT;
        }
        return type;
    }

    // factor-a ::= factor | "!" factor | "-" factor
    private int factor_a() throws SintaticError, SemanticError {
        int type = Tag.ERROR;
        if (current.tag == Tag.NOT || current.tag == Tag.MENOS) {
            getNextToken(current.tag);
        }
        type = factor();
        return type;
    }

    // factor ::= identifier | constant | literal | "(" expression ")"
    private int factor() throws SintaticError, SemanticError {
        int type = Tag.ERROR;
        switch(current.tag){
            case Tag.ID:
                type = words.get(((Word)current).lexema).type;
                if(type == 0) {
                    throw new SemanticError("Identifier ["+current.toString() +"] not declared at line " + current.line);
                } else{
                    current.offset = this.offset;
                    switch(type){
                        case Tag.INT:
                            this.offset += Utils.sizeInt;
                            break;
                        case Tag.FLOAT:
                            this.offset += Utils.sizeFloat;
                            break;
                        case Tag.STRING:
                            this.offset += Utils.sizeString;
                            break;
                    }
                }
                getNextToken(Tag.ID);
                break;
            case Tag.INT, Tag.FLOAT, Tag.STRING, Tag.LITERAL, Tag.REAL_CONSTANT:
                type = constant();
                break;
            case Tag.PARABRE:
                getNextToken(Tag.PARABRE);
                type = expression();
                getNextToken(Tag.PARFECHA);
                break;
            default: 
                throw new SintaticError(this.current + " is an Invalid Factor" + " at line "+this.current.line);
        }
        return type;
    }

    // relop ::= ">" | ">=" | "<" | "<=" | "!=" | "=="
    private void relop() throws SintaticError {
        switch (current.tag) {
            case Tag.EQ: 
                getNextToken(Tag.EQ);
                break;
            case Tag.GT:
                getNextToken(Tag.GT);
                break;
            case Tag.GE:
                getNextToken(Tag.GE);
                break;
            case Tag.LT:
                getNextToken(Tag.LT);
                break;
            case Tag.LE:
                getNextToken(Tag.LE);
                break;
            case Tag.NE:
                getNextToken(Tag.NE);
                break;
            default:
                throw new SintaticError(this.current + " is an Invalid Relational Operator" + " at line "+this.current.line);
        }
    }

    // addop ::= "+" | "-" | "||"
    private void addop() throws SintaticError {
        switch (current.tag) {
            case Tag.MAIS: 
                getNextToken(Tag.MAIS);
                break;
            case Tag.MENOS:
                getNextToken(Tag.MENOS);
                break;
            case Tag.OR: 
                getNextToken(Tag.OR);
                break;
            default: 
                throw new SintaticError(this.current + " is an Invalid Add Operator" + " at line "+this.current.line);
        }
    }

    // mulop ::= "*" | "/" | "&&"
    private void mulop() throws SintaticError {
        switch (current.tag) {
            case Tag.MULT:
                getNextToken(Tag.MULT);
                break;
            case Tag.DIV:
                getNextToken(Tag.DIV);
                break;
            case Tag.AND:
                getNextToken(Tag.AND);
                break;
            default: 
                throw new SintaticError(this.current + " is an Invalid Mul Operator" + " at line "+this.current.line);
        }
    }

    // constant ::= integer_const | literal | real_const
    private int constant() throws SintaticError {
        // Implemente a lógica para verificar e consumir constantes.
        int type = Tag.ERROR;
        switch (current.tag) {
            case Tag.INTEGER_CONSTANT, Tag.INT:
                current.type = Tag.INT;
                type = Tag.INT;
                
                //offset p/ geração de código
                current.offset = this.offset;
                this.offset += Utils.sizeInt;
                
                getNextToken(Tag.INT);
                break;
            case Tag.LITERAL:
                current.type = Tag.LITERAL;
                type = Tag.LITERAL;
                
                //offset p/ geração de código
                current.offset = this.offset;
                this.offset+= Utils.sizeString;
                
                getNextToken(Tag.LITERAL);
                break;
            case Tag.REAL_CONSTANT:
                current.type = Tag.FLOAT;
                type = Tag.FLOAT;
                
                //offset p/ geração de código
                current.offset = this.offset;
                this.offset+= Utils.sizeFloat;
                
                getNextToken(Tag.REAL_CONSTANT);
                break;
            default:
                throw new SintaticError(this.current + " is an Invalid Constant");
        }
        return type;
    }

}
