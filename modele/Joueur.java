package modele;

public class Joueur {
    private final String nom;
    private final char couleur;
    private int nbPartiesGagnees;

    /**
     * Construit un objet Joueur avec le nom et la couleur spécifiés.
     **/
    public Joueur(String nom, char couleur) {
        this.nom = nom;
        this.couleur = couleur;
        this.nbPartiesGagnees = 0;
    }

    /**
     * Retourne le nom du joueur.
     **/
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la couleur du joueur.
     **/
    public char getCouleur() {
        return couleur;
    }

    /**
     * Retourne le nombre de parties gagnées par le joueur.
     **/
    public int getNbPartiesGagnees() {
        return nbPartiesGagnees;
    }

    /**
     * Incrémente le nombre de parties gagnées par le joueur.
     */
    public void incrementerPartiesGagnees() {
        nbPartiesGagnees++;
    }
}