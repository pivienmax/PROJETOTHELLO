package vue;

import java.util.Scanner;

/**
 * Gère l'interface utilisateur pour interagir avec les joueurs.
 */
public class Ihm {
    private Scanner scanner;

    /**
     * Constructeur de la classe Ihm.
     */
    public Ihm() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Demande le nom d'un joueur.
     *
     * @param joueur Le numéro du joueur (1 ou 2).
     * @return Le nom du joueur.
     */
    public String demanderNomJoueur(int joueur) {
        System.out.println("Entrez le nom du joueur " + joueur + " : ");
        return scanner.nextLine();
    }

    /**
     * Affiche le plateau d'Othello.
     *
     * @param plateau Le plateau de jeu à afficher.
     */
    public void afficherPlateauOthello(char[][] plateau) {
        System.out.println("     A    B    C    D    E    F    G    H");
        for (int i = 0; i < plateau.length; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < plateau[i].length; j++) {
                switch (plateau[i][j]) {
                    case 'N':
                        System.out.print("\u26AB");
                        break;
                    case 'B':
                        System.out.print("\u26AA");
                        break;
                    default:
                        System.out.print("\uD83D\uDFE9");
                        break;
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * Demande un coup à un joueur pour Othello.
     *
     * @param nomJoueur Le nom du joueur.
     * @return Le coup saisi par le joueur.
     */
    public String demanderCoupOthello(String nomJoueur) {
        System.out.println(nomJoueur + ", à vous de jouer. Saisir un coup (ex: 3 D) ou P pour passer.");
        return scanner.nextLine().toUpperCase().trim();
    }

    /**
     * Demande si les joueurs veulent rejouer une partie.
     *
     * @return true si les joueurs veulent rejouer, false sinon.
     */
    public boolean demanderRejouer() {
        System.out.println("Voulez-vous rejouer une partie ? (O/N)");
        String reponse = scanner.nextLine().toUpperCase();
        return reponse.equals("O");
    }

    /**
     * Demande si un joueur veut jouer contre l'IA.
     *
     * @return true si le joueur veut jouer contre l'IA, false sinon.
     */
    public boolean demanderSiContreIA() {
        System.out.println("Voulez-vous jouer contre l'ordinateur (IA) ? (O/N)");
        String reponse = scanner.nextLine().toUpperCase().trim();
        return reponse.equals("O");
    }

    /**
     * Demande le type d'IA contre laquelle jouer.
     *
     * @return Le choix de l'IA (1 pour Minimax, 2 pour Naïve).
     */
    public int demanderTypeIA() {
        afficherMessage("Choisissez l'IA contre laquelle jouer : ");
        afficherMessage("1 - IA Minimax (stratégique)");
        afficherMessage("2 - IA Naïve (prend le premier coup valide)");

        int choix = -1;
        while (true) {
            System.out.print("Votre choix : ");
            if (scanner.hasNextInt()) {
                choix = scanner.nextInt();
                scanner.nextLine();
                if (choix == 1 || choix == 2) {
                    break;
                } else {
                    afficherMessage("Choix invalide, veuillez entrer 1 ou 2.");
                }
            } else {
                afficherMessage("Entrée invalide, veuillez entrer un nombre.");
                scanner.nextLine();
            }
        }
        return choix;
    }

    /**
     * Demande à quel jeu jouer.
     *
     * @return Le choix du jeu ("O" pour Othello, "A" pour Awalé).
     */
    public String demanderChoixJeu() {
        System.out.println("Quel jeu souhaitez-vous jouer ?");
        System.out.println("O - Othello");
        System.out.println("A - Awalé");
        String choix = scanner.nextLine().toUpperCase().trim();
        while (!choix.equals("O") && !choix.equals("A")) {
            System.out.println("Choix invalide. Veuillez entrer O ou A.");
            choix = scanner.nextLine().toUpperCase().trim();
        }
        return choix;
    }

    // Awalé

    /**
     * Demande un coup à un joueur pour Awalé.
     *
     * @param nomJoueur Le nom du joueur.
     * @return Le coup saisi par le joueur.
     */
    public String demanderCoupAwale(String nomJoueur) {
        System.out.println(nomJoueur + ", choisissez un trou (1 à 6) sur votre rangée ou P pour passer.");
        return scanner.nextLine().toUpperCase().trim();
    }

    /**
     * Affiche le plateau d'Awalé.
     *
     * @param plateau    Le plateau de jeu à afficher.
     * @param grenierJ1  Le grenier du joueur 1.
     * @param grenierJ2  Le grenier du joueur 2.
     */
    public void afficherPlateauAwale(int[][] plateau, int grenierJ1, int grenierJ2) {
        System.out.println("---- Plateau Awalé ----");
        System.out.println("Grenier Joueur 2 : " + grenierJ2);
        System.out.print("   ");
        for (int j = 1; j <= 6; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        System.out.print("   ");
        for (int j = 0; j < 6; j++) {
            System.out.print(plateau[0][j] + " ");
        }
        System.out.println("\n   -----------------");
        System.out.print("   ");
        for (int j = 0; j < 6; j++) {
            System.out.print(plateau[1][j] + " ");
        }
        System.out.println();
        System.out.println("Grenier Joueur 1 : " + grenierJ1);
    }

    /**
     * Affiche un message à l'utilisateur.
     *
     * @param message Le message à afficher.
     */
    public void afficherMessage(String message) {
        System.out.println(message);
    }
}
