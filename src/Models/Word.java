/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Ian Silva
 */
public class Word extends Token{
    
    public final String lexema;
    public final int tag;

    //Palavras Reservadas
    public static final Word and = new Word ("&&", Tag.AND);
    public static final Word or = new Word ("||", Tag.OR);
    public static final Word eq = new Word ("==", Tag.EQ);
    public static final Word ne = new Word ("!=", Tag.NE);
    public static final Word le = new Word ("<=", Tag.LE);
    public static final Word ge = new Word (">=", Tag.GE);
    public static final Word True = new Word ("true", Tag.TRUE);
    public static final Word False = new Word ("false", Tag.FALSE);
    public static final Word mais = new Word ("+", Tag.MAIS);
    public static final Word menos = new Word ("-", Tag.MENOS);
    public static final Word mult = new Word ("*", Tag.MULT);
    public static final Word div = new Word ("/", Tag.DIV);
    public static final Word comment = new Word ("//", Tag.COMMENT);
    
    public static final Word commentStart = new Word ("/*", Tag.COMMENTSTART);
    public static final Word commentEnd = new Word ("*/", Tag.COMMENTEND);
    public static final Word parAbre = new Word ("(", Tag.PARABRE);
    public static final Word parFecha = new Word (")", Tag.PARFECHA);
    public static final Word chaveAbre = new Word ("{", Tag.CHAVEABRE);
    public static final Word chaveFecha = new Word ("}", Tag.CHAVEFECHA);
    public static final Word pontoVirgula = new Word (";", Tag.CHAVEFECHA);
    
    @Override
    public String toString() {
        return "Word{" + "lexema=" + lexema + '}';
    }

    public Word(String lexema, int tag) {
        super(tag);
        this.lexema = lexema;
        this.tag = super.tag;
    }
    
    /*
    public Word(int tag) {
        super(tag);
    }
    */
    
}
