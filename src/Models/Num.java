/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Ian Silva
 */
public class Num extends Token{
    
    public final int value;
    public final int tag;

    public Num(int value) {
        super(Tag.INT);
        this.value = value;
        tag = super.tag;
    }
    
    public Num(int value, int line){
        super(Tag.INT, line);
        this.value = value;
        tag = super.tag;
    }
    
    @Override
    public String toString() {
        return "Num{" + "value=" + value + '}';
    }
    
    
}
