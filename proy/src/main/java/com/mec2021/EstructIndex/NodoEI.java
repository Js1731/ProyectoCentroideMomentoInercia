 package com.mec2021.EstructIndex;
 
 /**
  * Representa un nodo dentro de una estructua indexada.
  * Cada nodo tiene un nombre.
  */
 public abstract class NodoEI {
 
     String Nombre = "Sin Nombre";

     public NodoEI(String Nom){
         Nombre = Nom;
     }
 }