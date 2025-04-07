package modele;

import java.util.List;

public class IAMinimax implements ModeIA {
    private int profondeurMax = 3; // Profondeur de recherche ajustable

    @Override
    public int[] jouerCoup(Partie partie, char couleur) {
        if (partie.estTerminee()) {
            return null; // Plus de coup à joué
        }

        List<int[]> coupsPossibles = partie.getCoupsPossibles(couleur);
        if (coupsPossibles.isEmpty()) {
            return null; // Aucun coups possible, on passe le tour
        }

        int[] meilleurCoup = null;
        int meilleureValeur = Integer.MIN_VALUE;

        for (int[] coup : coupsPossibles) {
            Partie copie = partie.copier();
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


    private int minimax(Partie partie, int profondeur, boolean estMax, char couleurIA) {
        // Couleur du joueur actuel dans la simulation
        char couleurActuelle = estMax ? couleurIA : (couleurIA == 'N' ? 'B' : 'N');

        if (profondeur == 0 || partie.estTerminee()) {
            return evaluerPlateau(partie, couleurIA);
        }

        List<int[]> coupsPossibles = partie.getCoupsPossibles(couleurActuelle);

        // Si aucun coup possible, on passe son tour
        if (coupsPossibles.isEmpty()) {
            Partie copiePasse = partie.copier();
            copiePasse.changerTour();
            return minimax(copiePasse, profondeur - 1, !estMax, couleurIA);
        }

        if (estMax) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] coup : coupsPossibles) {
                Partie copie = partie.copier();
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
                Partie copie = partie.copier();
                String coupStr = (coup[0] + 1) + " " + (char) ('A' + coup[1]);
                copie.jouerCoup(coupStr);
                copie.changerTour();

                int eval = minimax(copie, profondeur - 1, true, couleurIA);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    private int evaluerPlateau(Partie partie, char couleur) {
        // Si la partie est terminée, attribuer une valeur élevée selon le vainqueur
        if (partie.estTerminee()) {
            Joueur vainqueur = partie.getVainqueur();
            if (vainqueur == null) {
                return 0; // Match nul
            }
            return (vainqueur.getCouleur() == couleur) ? 1000 : -1000;
        }

        char[][] plateau = partie.getPlateau();
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
     * Retourne la valeur stratégique d'une position sur le plateau
     * - 11 points pour un coin
     * - 6 points pour un bord
     * - 1 point ailleurs
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