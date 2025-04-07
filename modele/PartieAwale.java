package modele;

public class PartieAwale {
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueurCourant;
    private int[][] plateau; // Plateau de 2x6 trous
    private int grenierJ1;
    private int grenierJ2;

    public PartieAwale(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.joueurCourant = joueur1;
        this.plateau = new int[2][6];
        initialiserPlateau();
        this.grenierJ1 = 0;
        this.grenierJ2 = 0;
    }

    private void initialiserPlateau() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                plateau[i][j] = 4;
            }
        }
    }

    /**
     * Le coup est saisi sous forme d'un chiffre (1 à 6) indiquant le trou choisi.
     * La méthode retourne false si le coup est invalide.
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
        int row = (joueurCourant == joueur1) ? 1 : 0;
        if(col < 0 || col >= 6 || plateau[row][col] == 0) {
            return false;
        }
        int seeds = plateau[row][col];
        plateau[row][col] = 0;

        // Distribution des graines dans le sens anti-horaire.
        int r = row, c = col;
        boolean premierTour = true;
        while (seeds > 0) {
            int[] next = getNextPit(r, c, row, col, premierTour);
            r = next[0];
            c = next[1];
            // Si le nombre de graines initial est > 11, la case de départ est toujours sautée
            if (!(r == row && c == col && !premierTour)) {
                plateau[r][c]++;
                seeds--;
            }
            if(r == row && c == col) {
                premierTour = false;
            }
        }

        // Capture : si la dernière graine est déposée dans un trou adverse comportant 1 ou 2 graines
        if ((joueurCourant == joueur1 && r == 0) || (joueurCourant == joueur2 && r == 1)) {
            int captured = 0;
            int currRow = r, currCol = c;
            while (((joueurCourant == joueur1 && currRow == 0) || (joueurCourant == joueur2 && currRow == 1))
                    && (plateau[currRow][currCol] == 2 || plateau[currRow][currCol] == 3)) {
                captured += plateau[currRow][currCol];
                plateau[currRow][currCol] = 0;
                int[] prev = getPreviousPit(currRow, currCol);
                currRow = prev[0];
                currCol = prev[1];
            }
            // Vérification de la règle d'interdiction d'affamer l'adversaire
            int opponentRow = (joueurCourant == joueur1) ? 0 : 1;
            if (captured == totalSeedsInRow(opponentRow)) {
                return false; // Coup invalide : affamer l'adversaire
            }
            if (joueurCourant == joueur1) {
                grenierJ1 += captured;
            } else {
                grenierJ2 += captured;
            }
        }
        return true;
    }

    /**
     * Renvoie le trou suivant dans l'ordre anti-horaire.
     * Ici, l'ordre choisi est : sur la rangée du joueur 1 (bas), on se déplace de droite à gauche,
     * puis la rangée adverse de gauche à droite.
     */
    private int[] getNextPit(int r, int c, int startRow, int startCol, boolean premierTour) {
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
     * Renvoie le trou précédent dans l'ordre inverse de la distribution.
     */
    private int[] getPreviousPit(int r, int c) {
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

    private int totalSeedsInRow(int row) {
        int total = 0;
        for (int j = 0; j < 6; j++) {
            total += plateau[row][j];
        }
        return total;
    }

    /**
     * Le jeu se termine lorsqu'un joueur n'a plus de graines sur sa rangée.
     */
    public boolean estTerminee() {
        int row = (joueurCourant == joueur1) ? 1 : 0;
        for (int j = 0; j < 6; j++) {
            if (plateau[row][j] > 0) {
                return false;
            }
        }
        return true;
    }

    public void passerTour() {
        changerTour();
        if (estTerminee()) {
            int opponentRow = (joueurCourant == joueur1) ? 0 : 1;
            int remaining = totalSeedsInRow(opponentRow);
            if (joueurCourant == joueur1) {
                grenierJ1 += remaining;
            } else {
                grenierJ2 += remaining;
            }
            for (int j = 0; j < 6; j++) {
                plateau[opponentRow][j] = 0;
            }
        }
    }

    public void changerTour() {
        joueurCourant = (joueurCourant == joueur1) ? joueur2 : joueur1;
    }

    public int[][] getPlateau() {
        return plateau;
    }

    public int getGrenier(Joueur joueur) {
        return (joueur == joueur1) ? grenierJ1 : grenierJ2;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public Joueur getJoueur1() {
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }

    public int getPartiesGagnees(Joueur joueur) {
        return joueur.getNbPartiesGagnees();
    }

    /**
     * Le vainqueur est celui qui a le plus de graines dans son grenier.
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
