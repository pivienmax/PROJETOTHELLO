package modele;

/**
 * Représente une partie du jeu Awalé.
 */
public class PartieAwale {
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueurCourant;
    private int[][] plateau; // 2x6 trous
    private int grenierJ1;
    private int grenierJ2;

    /**
     * Construit une nouvelle partie d'Awalé avec les joueurs spécifiés.
     *
     * @param joueur1 Le premier joueur.
     * @param joueur2 Le second joueur.
     */
    public PartieAwale(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.joueurCourant = joueur1;
        this.plateau = new int[2][6];
        initialiserPlateau();
        this.grenierJ1 = 0;
        this.grenierJ2 = 0;
    }

    /**
     * Initialise le plateau avec les graines de départ.
     */
    private void initialiserPlateau() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                plateau[i][j] = 4;
            }
        }
    }

    /**
     * Joue un coup sur le plateau.
     *
     * @param coup Le coup à jouer, sous forme de chaîne (1 à 6).
     * @return true si le coup est valide, false sinon.
     */
    public boolean jouerCoup(String coup) {
        if(coup.equalsIgnoreCase("P")) {
            passerTour();
            return true;
        }
        int col;
        try {
            col = Integer.parseInt(coup) - 1;
        } catch (NumberFormatException e) {
            return false;
        }
        int rangee = (joueurCourant == joueur1) ? 1 : 0;
        if(col < 0 || col >= 6 || plateau[rangee][col] == 0) {
            return false;
        }
        int graines = plateau[rangee][col];
        plateau[rangee][col] = 0;

        // Distribution des graines dans le sens anti-horaire.
        int r = rangee, c = col;
        boolean premierTour = true;
        while (graines > 0) {
            int[] next = getTrouSuivant(r, c, rangee, col, premierTour);
            r = next[0];
            c = next[1];
            // Si le nombre de graines initial est > 11, la case de départ est toujours sautée
            if (!(r == rangee && c == col && !premierTour)) {
                plateau[r][c]++;
                graines--;
            }
            if(r == rangee && c == col) {
                premierTour = false;
            }
        }

        // Capture : si la dernière graine est déposée dans un trou adverse comportant 1 ou 2 graines
        if ((joueurCourant == joueur1 && r == 0) || (joueurCourant == joueur2 && r == 1)) {
            int captures = 0;
            int rangeeCourante = r, colonneCourante = c;
            while (((joueurCourant == joueur1 && rangeeCourante == 0) || (joueurCourant == joueur2 && rangeeCourante == 1))
                    && (plateau[rangeeCourante][colonneCourante] == 2 || plateau[rangeeCourante][colonneCourante] == 3)) {
                captures += plateau[rangeeCourante][colonneCourante];
                plateau[rangeeCourante][colonneCourante] = 0;
                int[] prec = getTrouPrecedent(rangeeCourante, colonneCourante);
                rangeeCourante = prec[0];
                colonneCourante = prec[1];
            }
            // Vérification de la règle d'interdiction d'affamer l'adversaire
            int rangeeAdversaire = (joueurCourant == joueur1) ? 0 : 1;
            if (captures == totalGrainesRangee(rangeeAdversaire)) {
                return false; // Coup invalide : affamer l'adversaire
            }
            if (joueurCourant == joueur1) {
                grenierJ1 += captures;
            } else {
                grenierJ2 += captures;
            }
        }
        return true;
    }

    /**
     * Renvoie le trou suivant dans l'ordre anti-horaire.
     *
     * @param r         La rangée actuelle.
     * @param c         La colonne actuelle.
     * @param rangeeDep La rangée de départ.
     * @param colDep    La colonne de départ.
     * @param premierTour Indique si c'est le premier tour.
     * @return Le trou suivant sous forme de tableau [rangée, colonne].
     */
    private int[] getTrouSuivant(int r, int c, int rangeeDep, int colDep, boolean premierTour) {
        if (r == 1) { // Rangée du joueur 1 (bas)
            if (c > 0) {
                return new int[]{1, c - 1};
            } else {
                return new int[]{0, 0};
            }
        } else { // Rangée du joueur 2 (haut)
            if (c < 5) {
                return new int[]{0, c + 1};
            } else {
                return new int[]{1, 5};
            }
        }
    }

    /**
     * Renvoie le trou précédent dans l'ordre horaire.
     *
     * @param r La rangée actuelle.
     * @param c La colonne actuelle.
     * @return Le trou précédent sous forme de tableau [rangée, colonne].
     */
    private int[] getTrouPrecedent(int r, int c) {
        if (r == 1) {
            if (c < 5) {
                return new int[]{1, c + 1};
            } else {
                return new int[]{0, 5};
            }
        } else {
            if (c > 0) {
                return new int[]{0, c - 1};
            } else {
                return new int[]{1, 0};
            }
        }
    }

    /**
     * Calcule le total des graines sur une rangée.
     *
     * @param rangee La rangée à évaluer.
     * @return Le total des graines sur la rangée.
     */
    private int totalGrainesRangee(int rangee) {
        int total = 0;
        for (int j = 0; j < 6; j++) {
            total += plateau[rangee][j];
        }
        return total;
    }

    /**
     * Vérifie si la partie est terminée.
     *
     * @return true si la partie est terminée, false sinon.
     */
    public boolean estTerminee() {
        int rangee = (joueurCourant == joueur1) ? 1 : 0;
        for (int j = 0; j < 6; j++) {
            if (plateau[rangee][j] > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Passe le tour du joueur courant.
     */
    public void passerTour() {
        changerTour();
        if (estTerminee()) {
            int rangeeAdversaire = (joueurCourant == joueur1) ? 0 : 1;
            int restantes = totalGrainesRangee(rangeeAdversaire);
            if (joueurCourant == joueur1) {
                grenierJ1 += restantes;
            } else {
                grenierJ2 += restantes;
            }
            for (int j = 0; j < 6; j++) {
                plateau[rangeeAdversaire][j] = 0;
            }
        }
    }

    /**
     * Change le tour du joueur courant.
     */
    public void changerTour() {
        joueurCourant = (joueurCourant == joueur1) ? joueur2 : joueur1;
    }

    /**
     * Retourne le plateau de jeu.
     *
     * @return Le plateau de jeu.
     */
    public int[][] getPlateau() {
        return plateau;
    }

    /**
     * Retourne le nombre de graines dans le grenier d'un joueur.
     *
     * @param joueur Le joueur dont on veut connaître le grenier.
     * @return Le nombre de graines dans le grenier du joueur.
     */
    public int getGrenier(Joueur joueur) {
        return (joueur == joueur1) ? grenierJ1 : grenierJ2;
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
     * Retourne le joueur 1.
     *
     * @return Le joueur 1.
     */
    public Joueur getJoueur1() {
        return joueur1;
    }

    /**
     * Retourne le joueur 2.
     *
     * @return Le joueur 2.
     */
    public Joueur getJoueur2() {
        return joueur2;
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

    /**
     * Détermine le vainqueur de la partie.
     *
     * @return Le joueur vainqueur, ou null en cas de match nul.
     */
    public Joueur getVainqueur() {
        if (grenierJ1 > grenierJ2) {
            joueur1.incrementerPartiesGagnees();
            return joueur1;
        } else if (grenierJ2 > grenierJ1) {
            joueur2.incrementerPartiesGagnees();
            return joueur2;
        } else {
            return null;
        }
    }

    /**
     * Crée une copie de la partie en cours.
     *
     * @return Une copie de la partie.
     */
    public PartieAwale copier() {
        PartieAwale copie = new PartieAwale(joueur1, joueur2);
        for (int i = 0; i < 2; i++) {
            System.arraycopy(this.plateau[i], 0, copie.plateau[i], 0, 6);
        }
        copie.grenierJ1 = this.grenierJ1;
        copie.grenierJ2 = this.grenierJ2;
        copie.joueurCourant = this.joueurCourant;
        return copie;
    }
}
