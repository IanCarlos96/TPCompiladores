/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Ian Silva
 */
public class Symbol {
    
    private String type;
    
    public static final Symbol classSymbol = new Symbol("CLASS");
    public static final Symbol intSymbol = new Symbol("INT");
    public static final Symbol stringSymbol = new Symbol("STRING");
    public static final Symbol floatSymbol = new Symbol("FLOAT");

    public Symbol() {
    }

    public Symbol(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Symbol{" + "type=" + type + '}';
    }
    
    
}
