/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import Models.Env;
import Models.Tag;
import Models.Token;
import java.util.Map;

/**
 *
 * @author Ian Silva
 */
public class Utils {
    public static void printTabelaSimbolos(Env top){
        for(Object entry: top.getTable().entrySet()){
            System.out.println(entry.toString());
        }
    
    }
    
    public static boolean typeNumeric(int type){
    
        switch(type){
            case Tag.INT:
            case Tag.INTEGER_CONSTANT:
            case Tag.FLOAT:
            case Tag.REAL_CONSTANT:
                return true;
        }
        return false;
    }
    
    public static boolean typeString(int type){
        switch(type){
            case Tag.STRING:
            case Tag.LITERAL:
                return true;
        }
        return false;
    }
}
