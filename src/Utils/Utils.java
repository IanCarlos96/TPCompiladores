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
}
