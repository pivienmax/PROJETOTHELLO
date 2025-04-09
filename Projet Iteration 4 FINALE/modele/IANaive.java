package modele;

import java.util.List;
import java.util.Random;

/**
 * Implémente une stratégie d'IA naïve qui joue des coups aléatoires.
 */
public class IANaive implements ModeleIA {

    /**
     * Joue un coup aléatoire parmi les coups possibles.
     *
     * @param partie La partie d'Othello en cours.
     * @param couleur La couleur du joueur IA.
     * @return Le coup choisi par l'IA.
     */
    @Override
    public int[] jouerCoup(PartieOthello partie, char couleur) {
        List<int[]> coupsPossibles = partie.getCoupsPossibles(couleur);
        if (!coupsPossibles.isEmpty()) {
            Random rand = new Random();
            int indiceAleatoire = rand.nextInt(coupsPossibles.size());
            return coupsPossibles.get(indiceAleatoire);
        }
        return null;
    }

}
