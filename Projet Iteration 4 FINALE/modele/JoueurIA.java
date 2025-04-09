package modele;

/**
 * Représente un joueur contrôlé par une IA.
 */
public class JoueurIA extends Joueur {
    private ModeleIA strategie;

    /**
     * Construit un objet JoueurIA avec une stratégie d'IA spécifiée.
     *
     * @param nom       Le nom du joueur IA.
     * @param couleur   La couleur du joueur IA.
     * @param strategie La stratégie d'IA utilisée.
     */
    public JoueurIA(String nom, char couleur, ModeleIA strategie) {
        super(nom, couleur);
        this.strategie = strategie;
    }

    /**
     * Choisit un coup en utilisant la stratégie d'IA.
     *
     * @param partie La partie d'Othello en cours.
     * @return Le coup choisi par l'IA.
     */
    public int[] choisirCoup(PartieOthello partie) {
        return strategie.jouerCoup(partie, getCouleur());
    }
}
