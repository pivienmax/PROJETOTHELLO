package main;

import controleur.Controleur;
import vue.Ihm;

/**
 * La classe principale pour lancer le jeu.
 */
public class Main {
    /**
     * Point d'entrée principal pour lancer le jeu.
     *
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        Ihm ihm = new Ihm();
        Controleur controleur = new Controleur(ihm);
        controleur.jouer();
    }
}
