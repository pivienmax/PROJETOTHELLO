package modele;

/**
 * Représente un coup joué sur le plateau.
 */
public class Coup {
    private int ligne;
    private int colonne;

    /**
     * Construit un objet Coup à partir d'une représentation sous forme de chaîne de caractère.
     *
     * @param coup La représentation du coup sous forme de chaîne (ex: "3 D").
     */
    public Coup(String coup) {
        String[] parts = coup.split(" ");
        this.ligne = Integer.parseInt(parts[0]) - 1;
        this.colonne = parts[1].charAt(0) - 'A';
    }

    /**
     * Construit un objet Coup avec une ligne et une colonne spécifiées.
     *
     * @param ligne   La ligne du coup.
     * @param colonne La colonne du coup.
     */
    public Coup(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    /**
     * Retourne la ligne du coup.
     *
     * @return La ligne du coup.
     */
    public int getLigne() {
        return ligne;
    }

    /**
     * Retourne la colonne du coup.
     *
     * @return La colonne du coup.
     */
    public int getColonne() {
        return colonne;
    }
}