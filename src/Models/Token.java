/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Ian Silva
 */
public class Token {
    
    public final int tag;
    public int line;
    public int type;

    public Token(int tag) {
        this.tag = tag;
    }

    public Token(int tag, int line) {
        this.tag = tag;
        this.line = line;
    }

    public Token(int tag, int line, int type) {
        this.tag = tag;
        this.line = line;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Token{" + "tag=" + tag + ", line=" + line + '}';
    }

    
    
    
    
}
