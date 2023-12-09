/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author Ian Silva
 */
public class SemanticError extends Exception {
    private final String error;
    
    public SemanticError(String error){
        super();
        this.error = error;
    }

    @Override
    public String getMessage() {
        return "Semantic Error: " + this.error;
    }
}
