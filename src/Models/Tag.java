/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Ian Silva
 */
public class Tag {

    private int tag;

    public final static int //Palavras reservadas
        CLASS = 283,
        PRG = 256,
        BEG = 257,
        END = 258,
        TYPE = 259,
        INT = 260,
        CHAR = 261,
        BOOL = 262,
        IF = 263,
        ELSE = 264,
        STRING = 265,
        FLOAT = 266,
        DO = 267,
        WHILE = 268,
        READ = 269,
        WRITE = 270,
        //Operadores e pontuação
        EQ = 288, // ==
        GE = 289, // >=
        LE = 290, // <=
        NE = 291, // !=
        LT = 60, // <
        GT = 62, // >
        ASSIGN = 61, // =
        NUM = 278,
        ID = 279,
        AND = 280,
        OR = 281,
        TRUE = 1,
        FALSE = 0,
        MAIS = 43,
        MENOS = 45,
        MULT = 42,
        DIV = 47,
        NOT = 33,
        
        COMMENT = 282,
        COMMENTSTART = 283,
        COMMENTEND = 284,
        COMMENTBLOCK = 285,
        PARABRE = 40,
        PARFECHA = 41,
        CHAVEABRE = 123,
        CHAVEFECHA = 125,
        PONTOVIRGULA = 59,
        VIRGULA = 44,
        ASPAS = 34,
            
        INTEGER_CONSTANT = 286,
        LITERAL = 287,
        REAL_CONSTANT = 300,
    
        ERROR = -99;
            
        
            
    public Tag(int tag) {
        this.tag = tag;
    }

    public Tag() {
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

}
