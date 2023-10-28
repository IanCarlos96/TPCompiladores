/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Ian Silva
 */
public class Float extends Token {

    public final float value;

    public Float(float value) {
        super(Tag.FLOAT);
        this.value = value;
    }
    
    public Float(float value, int line){
        super(Tag.FLOAT, line);
        this.value = value;
    }

    public String toString() {
        return "" + value;
    }
}
