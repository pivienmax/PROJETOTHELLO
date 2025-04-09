package modele;

import java.util.List;

/**
 * Implémente une stratégie d'IA utilisant l'algorithme Minimax pour jouer à Othello.
 */

public class IAMinimax implements ModeleIA {
    private int profondeurMax = 3; // Profondeur de recherche ajustable

    /**
     * Joue un coup en utilisant l'algorithme Minimax.
     *
     * @param partieOthello La partie d'Othello en cours.
     * @param couleur       La couleur du joueur IA.
     * @return Le coup choisi par l'IA.
     */
    @Override
    public int[] jouerCoup(PartieOthello partieOthello, char couleur) {
        if (partieOthello.estTerminee()) {
            return null; // Plus de coup à joué
        }

        List<int[]> coupsPossibles = partieOthello.getCoupsPossibles(couleur);
        if (coupsPossibles.isEmpty()) {
            return null; // Aucun coups possible, on passe le tour
        }

        int[] meilleurCoup = null;
        int meilleureValeur = Integer.MIN_VALUE;

        for (int[] coup : coupsPossibles) {
            PartieOthello copie = partieOthello.copier();
            String coupStr = (coup[0] + 1) + " " + (char) ('A' + coup[1]);
            copie.jouerCoup(coupStr);
            copie.changerTour(); // Important: changer le tour pour simuler le prochain joueur

            int valeur = minimax(copie, profondeurMax - 1, false, couleur);
            if (valeur > meilleureValeur) {
                meilleureValeur = valeur;
                meilleurCoup = coup;
            }
        }
        return meilleurCoup;
    }

    /**
     * Algorithme Minimax pour évaluer les coups possibles.
     *
     * @param partieOthello La partie d'Othello en cours.
     * @param profondeur    La profondeur de recherche restante.
     * @param estMax        Indique si le joueur courant est le maximiseur.
     * @param couleurIA     La couleur du joueur IA.
     * @return La valeur évaluée du plateau.
     */
    private int minimax(PartieOthello partieOthello, int profondeur, boolean estMax, char couleurIA) {
        // Couleur du joueur actuel dans la simulation
        char couleurActuelle = estMax ? couleurIA : (couleurIA == 'N' ? 'B' : 'N');

        if (profondeur == 0 || partieOthello.estTerminee()) {
            return evaluerPlateau(partieOthello, couleurIA);
        }

        List<int[]> coupsPossibles = partieOthello.getCoupsPossibles(couleurActuelle);

        // Si aucun coup possible, on passe son tour
        if (coupsPossibles.isEmpty()) {
            PartieOthello copiePasse = partieOthello.copier();
            copiePasse.changerTour();
            return minimax(copiePasse, profondeur - 1, !estMax, couleurIA);
        }

        if (estMax) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] coup : coupsPossibles) {
                PartieOthello copie = partieOthello.copier();
                String coupStr = (coup[0] + 1) + " " + (char) ('A' + coup[1]);
                copie.jouerCoup(coupStr);
                copie.changerTour();

                int eval = minimax(copie, profondeur - 1, false, couleurIA);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[] coup : coupsPossibles) {
                PartieOthello copie = partieOthello.copier();
                String coupStr = (coup[0] + 1) + " " + (char) ('A' + coup[1]);
                copie.jouerCoup(coupStr);
                copie.changerTour();

                int eval = minimax(copie, profondeur - 1, true, couleurIA);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    /**
     * Évalue la valeur stratégique du plateau pour l'IA.
     *
     * @param partieOthello La partie d'Othello en cours.
     * @param couleur       La couleur du joueur IA.
     * @return La valeur évaluée du plateau.
     */
    private int evaluerPlateau(PartieOthello partieOthello, char couleur) {
        // Si la partieOthello est terminée, attribuer une valeur élevée selon le vainqueur
        if (partieOthello.estTerminee()) {
            Joueur vainqueur = partieOthello.getVainqueur();
            if (vainqueur == null) {
                return 0; // Match nul
            }
            return (vainqueur.getCouleur() == couleur) ? 1000 : -1000;
        }

        char[][] plateau = partieOthello.getPlateau();
        int score = 0;
        char couleurAdversaire = (couleur == 'N') ? 'B' : 'N';

        // Évaluation en fonction de la position stratégique des pions
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (plateau[i][j] == couleur) {
                    score += getValeurPosition(i, j);
                } else if (plateau[i][j] == couleurAdversaire) {
                    score -= getValeurPosition(i, j);
                }
            }
        }

        return score;
    }

    /**
     * Retourne la valeur stratégique d'une position sur le plateau.
     *
     * @param ligne   La ligne de la position.
     * @param colonne La colonne de la position.
     * @return La valeur stratégique de la position.
     */
    private int getValeurPosition(int ligne, int colonne) {
        // Coins
        if ((ligne == 0 || ligne == 7) && (colonne == 0 || colonne == 7)) {
            return 11;
        }

        // Bords
        if (ligne == 0 || ligne == 7 || colonne == 0 || colonne == 7) {
            return 6;
        }

        // Positions centrales
        return 1;
    }
}