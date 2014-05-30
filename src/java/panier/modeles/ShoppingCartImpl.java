/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package panier.modeles;

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import musique.modeles.Musique;

/**
 *
 * @author julien
 */
@Stateful (mappedName = "ejb/myStatefulBean")
public class ShoppingCartImpl implements ShoppingCart{
    
    
    private Integer uid;
    @PersistenceContext(unitName = "TP_2_GitPU")
    private EntityManager em;
    private ArrayList<Musique> products;


   public void create(){
     products = new ArrayList<>();
   }

   public void init(Integer id){
     if(id==null){
       uid = id;
     }
  }

  public void addToCart(Musique product){
     if(product!=null){
        products.add(product);
     }

 }

    public void persist(Object object) throws RuntimeException{
        em.persist(object);
    }
    
    public Musique getItem(int index){
        return products.get(index);
    }

}
