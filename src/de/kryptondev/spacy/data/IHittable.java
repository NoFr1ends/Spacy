package de.kryptondev.spacy.data;


public interface IHittable {
    public void hit (Projectile hitting); 
    
    
    /*
    life und maxLife könnten eventuell auch in die Entity-Klasse gepackt werden,
    aber Projektile und Hindernisse haben diese Atribute nicht.
    Bester Ort wäre vermutlich das Interface IHittable.
    */
}
