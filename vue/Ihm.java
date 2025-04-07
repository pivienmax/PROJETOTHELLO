package vue;

import java.util.Scanner;

public class Ihm {
    private Scanner scanner;

    public Ihm() {
        this.scanner = new Scanner(System.in);
    }

    // --- Méthodes existantes ---

    public String demanderNomJoueur(int joueur) {
        System.out.println("Entrez le nom du joueur " + joueur + " : ");
        return scanner.nextLine();
    }

    public void afficherPlateau(char[][] plateau) {
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

    public String demanderCoup(String nomJoueur) {
        System.out.println(nomJoueur + ", à vous de jouer. Saisir un coup (ex: 3 D) ou P pour passer.");
        return scanner.nextLine().toUpperCase().trim();
    }

    public boolean demanderRejouer() {
        System.out.println("Voulez-vous rejouer une partie ? (O/N)");
        String reponse = scanner.nextLine().toUpperCase();
        return reponse.equals("O");
    }

    public boolean demanderSiContreIA() {
        System.out.println("Voulez-vous jouer contre l'ordinateur (IA) ? (O/N)");
        String reponse = scanner.nextLine().toUpperCase().trim();
        return reponse.equals("O");
    }

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

    // --- Méthodes spécifiques à la sélection de jeu ---

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

    // Méthodes pour Awalé
    public String demanderCoupAwale(String nomJoueur) {
        System.out.println(nomJoueur + ", choisissez un trou (1 à 6) sur votre rangée ou P pour passer.");
        return scanner.nextLine().toUpperCase().trim();
    }

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

    public void afficherMessage(String message) {
        System.out.println(message);
    }
}
