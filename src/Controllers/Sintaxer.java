/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Exceptions.SintaticError;
import Models.Tag;
import Models.Token;
import Models.Word;
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

    public Sintaxer(ArrayList<Token> tokenList, Hashtable<String, Word> words) {
        this.tokenList = tokenList;
        this.words = words;
    }

    private void getNextToken(int expected) throws SintaticError {
        if (this.current.tag == expected) {
            this.tokenIndex ++;
            this.current = tokenList.get(tokenIndex);
        }
        else {
            throw new SintaticError("Expected tag " + expected + ", got " + Character.toString((char)this.current.tag));
        }
    }
    
    
    // program ::= class identifier [decl-list] body
    public void program() throws SintaticError {
        
        //Little gambiarra, já que o início do arquivo estava gerando um token em branco às vezes
        boolean finded = false;
        for(int index = 0; index < this.tokenList.size() && finded == false; index++){
            this.current = this.tokenList.get(index);
            if(this.current.tag == 0){
                this.current = this.tokenList.get(index);
            } else {
                this.tokenIndex = index;
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
            identifiers.add(this.current);
        }
        getNextToken(Tag.ID);
        //getNextToken(Tag.VIRGULA);
        while (current.tag == Tag.VIRGULA) {
            getNextToken(Tag.VIRGULA);
            if (this.current.tag == Tag.ID) {
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
                throw new SintaticError("Expected Type (int, float or string), got " + this.current);
        }
    }

    // body ::= "{" stmt-list "}"
    private void body() throws SintaticError {
        getNextToken(Tag.CHAVEABRE);
        stmt_list();
        getNextToken(Tag.CHAVEFECHA);
    }

    // stmt-list ::= stmt ";" { stmt ";" }
    private void stmt_list() throws SintaticError {
        stmt();
        while (current.tag == Tag.PONTOVIRGULA) {
            getNextToken(Tag.PONTOVIRGULA);
            stmt();
        }
    }

    // stmt ::= assign-stmt | if-stmt | do-stmt | read-stmt | write-stmt
    private void stmt() throws SintaticError {
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
            default:
                throw new SintaticError(this.current + " is an Invalid Statement Start");
        }
    }

    // assign-stmt ::= identifier "=" simple_expr
    private void assign_stmt() throws SintaticError {
        getNextToken(Tag.ID);
        getNextToken(Tag.ASSIGN);
        simple_expr();
    }

    // if-stmt ::= if "(" condition ")" "{" stmt-list "}" [else "{" stmt-list "}"]
    private void if_stmt() throws SintaticError {
        getNextToken(Tag.IF);
        getNextToken(Tag.PARABRE);
        condition();
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
    private void condition() throws SintaticError {
        expression();
    }

    // do-stmt ::= do "{" stmt-list "}" do-suffix
    private void do_stmt() throws SintaticError {
        getNextToken(Tag.DO);
        getNextToken(Tag.CHAVEABRE);
        stmt_list();
        getNextToken(Tag.CHAVEFECHA);
        do_suffix();
    }

    // do-suffix ::= while "(" condition ")"
    private void do_suffix() throws SintaticError {
        getNextToken(Tag.WHILE);
        getNextToken(Tag.PARABRE);
        condition();
        getNextToken(Tag.PARFECHA);
    }

    // read-stmt ::= read "(" identifier ")"
    private void read_stmt() throws SintaticError {
        getNextToken(Tag.READ);
        getNextToken(Tag.PARABRE);
        getNextToken(Tag.ID);
        getNextToken(Tag.PARFECHA);
    }

    // write-stmt ::= write "(" writable ")"
    private void write_stmt() throws SintaticError {
        getNextToken(Tag.WRITE);
        getNextToken(Tag.PARABRE);
        writable();
        getNextToken(Tag.PARFECHA);
    }

    // writable ::=  simple-expr | literal
    private void writable() throws SintaticError {
        if(current.tag == Tag.LITERAL){
            getNextToken(Tag.LITERAL);
        } else {
            simple_expr();
        }
        
    }

    // expression ::= simple-expr | simple-expr relop simple-expr
    private void expression() throws SintaticError {
        simple_expr();
        switch (current.tag) {
            case Tag.EQ, Tag.GT, Tag.GE, Tag.LT, Tag.LE, Tag.NE:
                relop();
                simple_expr();
                break;
            default: 
                throw new SintaticError(this.current + " is an Invalid Expression Operator");
        }
    }

    // simple-expr ::= term | simple-expr addop term
    private void simple_expr() throws SintaticError {
        term();
        recursive_simple_expr();
    }
    
    private void recursive_simple_expr() throws SintaticError {
        switch (current.tag) {
            case Tag.MAIS, Tag.MENOS, Tag.OR:
                addop();
                term();
                recursive_simple_expr();
                break;
        }
    }

    // term ::= factor-a | term mulop factor-a
    private void term() throws SintaticError {
        factor_a();
        recursive_term();
//        while (current.tag == Tag.MUL_OP) {
//            mulop();
//            factor_a();
//        };
    }
    
    private void recursive_term() throws SintaticError{
        switch(current.tag){
            case Tag.MULT, Tag.DIV, Tag.OR:
                mulop();
                factor_a();
                recursive_term();
                break;
        }
    }

    // factor-a ::= factor | "!" factor | "-" factor
    private void factor_a() throws SintaticError {
        if (current.tag == Tag.NOT || current.tag == Tag.MENOS) {
            getNextToken(current.tag);
        }
        factor();
    }

    // factor ::= identifier | constant | "(" expression ")"
    private void factor() throws SintaticError {
        switch(current.tag){
            case Tag.ID:
                getNextToken(Tag.ID);
                break;
            case Tag.INT, Tag.FLOAT, Tag.STRING:
                constant();
                break;
            case Tag.PARABRE:
                getNextToken(Tag.PARABRE);
                expression();
                getNextToken(Tag.PARFECHA);
                break;
            default: 
                throw new SintaticError(this.current + " is an Invalid Factor");
        }
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
                throw new SintaticError(this.current + " is an Invalid Relational Operator");
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
                throw new SintaticError(this.current + " is an Invalid Add Operator");
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
                throw new SintaticError(this.current + " is an Invalid Mul Operator");
        }
    }

    // constant ::= integer_const | literal | real_const
    private void constant() throws SintaticError {
        // Implemente a lógica para verificar e consumir constantes.
        switch (current.tag) {
            case Tag.INTEGER_CONSTANT, Tag.INT:
                getNextToken(Tag.INT);
                break;
            case Tag.LITERAL:
                getNextToken(Tag.LITERAL);
                break;
            case Tag.REAL_CONSTANT:
                getNextToken(Tag.REAL_CONSTANT);
                break;
            default:
            //    throw new SintaticError(this.current + " is an Invalid Constant");
        }
    }

}
