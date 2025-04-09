package modele;

/**
 * Interface pour les stratégies d'IA dans le jeu d'Othello.
 */
public interface ModeleIA {
    /**
     * Joue un coup en fonction de la stratégie d'IA.
     *
     * @param partie La partie d'Othello en cours.
     * @param couleur La couleur du joueur IA.
     * @return Le coup choisi par l'IA.
     */
    int[] jouerCoup(PartieOthello partie, char couleur);
}
