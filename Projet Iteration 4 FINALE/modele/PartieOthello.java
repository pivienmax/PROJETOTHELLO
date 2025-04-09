package modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une partie du jeu Othello.
 */
public class PartieOthello {
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueurCourant;
    private char[][] plateau;

    /**
     * Construit une nouvelle partie d'Othello avec les joueurs spécifiés.
     *
     * @param joueur1 Le premier joueur.
     * @param joueur2 Le second joueur.
     */
    public PartieOthello(Joueur joueur1, Joueur joueur2) {
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
     *
     * @return true si la partie est terminée, false sinon.
     */
    public boolean estTerminee() {
        return estPlein() || (!coupPossible(joueur1.getCouleur()) && !coupPossible(joueur2.getCouleur()));
    }

    /**
     * Joue un coup sur le plateau.
     *
     * @param coup Le coup à jouer, sous forme de chaîne (ex: "3 D").
     * @return true si le coup est valide, false sinon.
     */
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
     *
     * @param ligne         La ligne du coup.
     * @param colonne       La colonne du coup.
     * @param dLigne        La direction en ligne.
     * @param dColonne      La direction en colonne.
     * @param couleurJoueur La couleur du joueur courant.
     * @return La liste des pions à retourner si le coup est valide, sinon une liste vide.
     */

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
     *
     * @return Le joueur vainqueur, ou null en cas de match nul.
     */
    public Joueur getVainqueur() {
        if (!estTerminee()) {
            return null;
        }

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
     *
     * @param couleur La couleur des pions à compter.
     * @return Le nombre de pions de la couleur spécifiée.
     */
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
     *
     * @param couleurJoueur La couleur du joueur.
     * @return true si un coup est possible, false sinon.
     */
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
     *
     * @param ligne         La ligne du coup.
     * @param colonne       La colonne du coup.
     * @param dLigne        La direction en ligne.
     * @param dColonne      La direction en colonne.
     * @param couleurJoueur La couleur du joueur courant.
     * @return true si un coup est possible dans la direction spécifiée, false sinon.
     */
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
     *
     * @return true si le plateau est plein, false sinon.
     */
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

    /**
     * Retourne la liste des coups possibles pour une couleur spécifique.
     *
     * @param couleurJoueur La couleur du joueur.
     * @return La liste des coups possibles.
     */
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
     *
     * @param ligne         La ligne du coup.
     * @param colonne       La colonne du coup.
     * @param couleurJoueur La couleur du joueur courant.
     * @return true si le coup est valide, false sinon.
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

    /**
     * Crée une copie de la partie en cours.
     *
     * @return Une copie de la partie.
     */
    public PartieOthello copier() {
        PartieOthello copie = new PartieOthello(joueur1, joueur2);
        for (int i = 0; i < 8; i++) {
            System.arraycopy(this.plateau[i], 0, copie.plateau[i], 0, 8);
        }
        copie.joueurCourant = this.joueurCourant;
        return copie;
    }

    /**
     * Retourne le plateau de jeu.
     *
     * @return Le plateau de jeu.
     */
    public char[][] getPlateau() {
        return plateau;
    }

    /**
     * Retourne le premier joueur.
     *
     * @return Le premier joueur.
     */
    public Joueur getJoueur1() {
        return joueur1;
    }

    /**
     * Retourne le second joueur.
     *
     * @return Le second joueur.
     */
    public Joueur getJoueur2() {
        return joueur2;
    }

    /**
     * Retourne le joueur courant.
     *
     * @return Le joueur courant.
     */
    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    /**
     * Retourne le nombre de parties gagnées par un joueur.
     *
     * @param joueur Le joueur dont on veut connaître le nombre de parties gagnées.
     * @return Le nombre de parties gagnées par le joueur.
     */
    public int getPartiesGagnees(Joueur joueur) {
        return joueur.getNbPartiesGagnees();
    }
}