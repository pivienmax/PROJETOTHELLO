package modele;

import java.util.ArrayList;
import java.util.List;

public class Partie {
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueurCourant;
    private char[][] plateau;

    /**
     * Construit les objets Joueurs.
     **/
    public Partie(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.joueurCourant = joueur1;
        this.plateau = new char[8][8];
        initialiserPlateau();
    }

    /**
     * Initialise le plateau de jeu avec les positions de départ.
     */
    private void initialiserPlateau() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                plateau[i][j] = ' ';
            }
        }
        plateau[3][3] = 'B';
        plateau[4][4] = 'B';
        plateau[3][4] = 'N';
        plateau[4][3] = 'N';
    }

    /**
     * Vérifie si la partie est terminée.
     **/
    public boolean estTerminee() {
        return estPlein() || (!coupPossible(joueur1.getCouleur()) && !coupPossible(joueur2.getCouleur()));
    }

    /**
     * Joue un coup sur le plateau.
     **/
    public boolean jouerCoup(String coup) {
        Coup coupObj = new Coup(coup);
        int ligne = coupObj.getLigne();
        int colonne = coupObj.getColonne();

        if (plateau[ligne][colonne] != ' ') {
            return false;
        }

        boolean coupValide = false;
        List<int[]> pionsARetournerTotal = new ArrayList<>();

        for (int dLigne = -1; dLigne <= 1; dLigne++) {
            for (int dColonne = -1; dColonne <= 1; dColonne++) {
                if (dLigne == 0 && dColonne == 0) {
                    continue;
                }
                List<int[]> pionsARetourner = verifierCoupDirection(ligne, colonne, dLigne, dColonne, joueurCourant.getCouleur());
                if (!pionsARetourner.isEmpty()) {
                    coupValide = true;
                    pionsARetournerTotal.addAll(pionsARetourner);
                }
            }
        }

        if (coupValide) {
            plateau[ligne][colonne] = joueurCourant.getCouleur();
            for (int[] pion : pionsARetournerTotal) {
                plateau[pion[0]][pion[1]] = joueurCourant.getCouleur();
            }
        }
        return coupValide;
    }

    /**
     * Vérifie si un coup dans une direction spécifique est valide.
     **/
    private List<int[]> verifierCoupDirection(int ligne, int colonne, int dLigne, int dColonne, char couleurJoueur) {
        List<int[]> pionsARetourner = new ArrayList<>();
        int l = ligne + dLigne;
        int c = colonne + dColonne;

        while (l >= 0 && l < 8 && c >= 0 && c < 8 && plateau[l][c] != ' ' && plateau[l][c] != couleurJoueur) {
            pionsARetourner.add(new int[]{l, c});
            l += dLigne;
            c += dColonne;
        }

        if (l < 0 || l >= 8 || c < 0 || c >= 8 || plateau[l][c] != couleurJoueur) {
            return new ArrayList<>(); // Coup invalide, ne retourne rien
        }

        return pionsARetourner;
    }

    /**
     * Change le tour du joueur courant.
     */
    public void changerTour() {
        joueurCourant = (joueurCourant == joueur1) ? joueur2 : joueur1;
    }

    /**
     * Passe le tour du joueur courant.
     */
    public void passerTour() {
        changerTour();
        if (estTerminee()) {
            getVainqueur();
        }
    }

    /**
     * Détermine le vainqueur du jeu.
     **/
    public Joueur getVainqueur() {
        int pionsJoueur1 = compterPions(joueur1.getCouleur());
        int pionsJoueur2 = compterPions(joueur2.getCouleur());

        if (pionsJoueur1 > pionsJoueur2) {
            joueur1.incrementerPartiesGagnees();
            return joueur1;
        } else if (pionsJoueur2 > pionsJoueur1) {
            joueur2.incrementerPartiesGagnees();
            return joueur2;
        } else {
            return null;
        }
    }

    /**
     * Compte le nombre de pions d'une couleur spécifique sur le plateau.
     **/
    public int compterPions(char couleur) {
        int compte = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (plateau[i][j] == couleur) {
                    compte++;
                }
            }
        }
        return compte;
    }

    /**
     * Vérifie si un coup est possible pour une couleur spécifique.
     **/
    public boolean coupPossible(char couleurJoueur) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (plateau[i][j] == ' ') {
                    for (int dLigne = -1; dLigne <= 1; dLigne++) {
                        for (int dColonne = -1; dColonne <= 1; dColonne++) {
                            if (dLigne == 0 && dColonne == 0) {
                                continue;
                            }
                            if (coupPossibleDirection(i, j, dLigne, dColonne, couleurJoueur)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si un coup est possible dans une direction spécifique.
     **/
    private boolean coupPossibleDirection(int ligne, int colonne, int dLigne, int dColonne, char couleurJoueur) {
        int l = ligne + dLigne;
        int c = colonne + dColonne;
        // Vérifier que la case adjacente est dans le plateau et contient un pion adverse.
        if (l < 0 || l >= 8 || c < 0 || c >= 8 || plateau[l][c] == ' ' || plateau[l][c] == couleurJoueur) {
            return false;
        }
        // Avancer dans la direction pour trouver un pion de la même couleur.
        l += dLigne;
        c += dColonne;
        while (l >= 0 && l < 8 && c >= 0 && c < 8) {
            if (plateau[l][c] == ' ') {
                return false;
            }
            if (plateau[l][c] == couleurJoueur) {
                return true;
            }
            l += dLigne;
            c += dColonne;
        }
        return false;
    }


    /**
     * Vérifie si le plateau est plein.
     **/
    public boolean estPlein() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (plateau[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public List<int[]> getCoupsPossibles(char couleurJoueur) {
        List<int[]> coupsPossibles = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (plateau[i][j] == ' ' && estCoupValide(i, j, couleurJoueur)) {
                    coupsPossibles.add(new int[]{i, j});
                }
            }
        }
        return coupsPossibles;
    }

    /**
     * Vérifie si un coup à une position donnée est valide.
     */
    private boolean estCoupValide(int ligne, int colonne, char couleurJoueur) {
        for (int dLigne = -1; dLigne <= 1; dLigne++) {
            for (int dColonne = -1; dColonne <= 1; dColonne++) {
                if (dLigne == 0 && dColonne == 0) continue;
                if (!verifierCoupDirection(ligne, colonne, dLigne, dColonne, couleurJoueur).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Partie copier() {
        Partie copie = new Partie(joueur1, joueur2);
        for (int i = 0; i < 8; i++) {
            System.arraycopy(this.plateau[i], 0, copie.plateau[i], 0, 8);
        }
        copie.joueurCourant = this.joueurCourant;
        return copie;
    }

    /**
     * Retourne le plateau de jeu.
     **/
    public char[][] getPlateau() {
        return plateau;
    }

    /**
     * Retourne le premier joueur.
     **/
    public Joueur getJoueur1() {
        return joueur1;
    }

    /**
     * Retourne le second joueur.
     **/
    public Joueur getJoueur2() {
        return joueur2;
    }

    /**
     Retourne le joueur courant.
     **/
    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    /**
     * Retourne le nombre de parties gagnées par un joueur.
     **/
    public int getPartiesGagnees(Joueur joueur) {
        return joueur.getNbPartiesGagnees();
    }
}