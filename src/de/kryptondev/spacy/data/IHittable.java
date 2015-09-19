/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.kryptondev.spacy.data;

/**
 *
 * @author cssand
 */
public interface IHittable {
    public void hit (Projectile hitting); 
    
    public int hp = 100; //hatten wir glatt vergessen
    public int maxHp = 100;
    /*
    life und maxLife könnten eventuell auch in die Entity-Klasse gepackt werden,
    aber Projektile und Hindernisse haben diese Atribute nicht.
    Bester Ort wäre vermutlich das Interface IHittable.
    */
}
