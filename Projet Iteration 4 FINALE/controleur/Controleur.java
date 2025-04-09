package controleur;

import modele.*;
import vue.Ihm;
import java.util.ArrayList;

/**
 * La classe Controleur gère le déroulement des parties d'Othello et d'Awalé.
 */
public class Controleur {
    private Ihm ihm;
    private Object partie; // Partie peut être Partie (Othello) ou PartieAwale
    private int toursPassesConsecutivement = 0;

    /**
     * Constructeur de la classe Controleur.
     *
     * @param ihm L'interface utilisateur pour interagir avec le joueur.
     */
    public Controleur(Ihm ihm) {
        this.ihm = ihm;
    }

    /**
     * Lance une partie de jeu en fonction du jeu choisi.
     */
    public void jouer() {
        String choixJeu = ihm.demanderChoixJeu();
        boolean rejouer = true;
        while (rejouer) {
            if (choixJeu.equals("O")) {
                // Othello
                String nomJoueur1 = ihm.demanderNomJoueur(1);
                boolean contreIA = ihm.demanderSiContreIA();
                Joueur joueur1 = new Joueur(nomJoueur1, 'N');
                Joueur joueur2;
                if (contreIA) {
                    int choixIA = ihm.demanderTypeIA();
                    ModeleIA strategieIA = (choixIA == 1) ? new IAMinimax() : new IANaive();
                    joueur2 = new JoueurIA("IA", 'B', strategieIA);
                } else {
                    joueur2 = new Joueur(ihm.demanderNomJoueur(2), 'B');
                }
                partie = new PartieOthello(joueur1, joueur2);
                toursPassesConsecutivement = 0;
                jouerPartieOthello();
            } else {
                // Awalé
                String nomJoueur1 = ihm.demanderNomJoueur(1);
                // Pour Awalé, on ne gère pas l'IA : on demande toujours le nom du joueur 2
                String nomJoueur2 = ihm.demanderNomJoueur(2);
                Joueur joueur1 = new Joueur(nomJoueur1, '1'); // La "couleur" n'a pas d'impact ici
                Joueur joueur2 = new Joueur(nomJoueur2, '2');
                partie = new PartieAwale(joueur1, joueur2);
                jouerPartieAwale();
            }
            rejouer = ihm.demanderRejouer();
        }
        afficherResultatsFinaux();
    }

    /**
     * Gère le déroulement d'une partie d'Othello.
     */
    private void jouerPartieOthello() {
        PartieOthello partieOthello = (PartieOthello) partie;
        while (!partieOthello.estTerminee() && toursPassesConsecutivement < 2) {
            ihm.afficherPlateauOthello(partieOthello.getPlateau());
            Joueur joueurCourant = partieOthello.getJoueurCourant();
            ArrayList<int[]> coupsPossibles = (ArrayList<int[]>) partieOthello.getCoupsPossibles(joueurCourant.getCouleur());
            boolean tourDejaChange = false;
            String coup;
            boolean coupValide = false;

            if (joueurCourant instanceof JoueurIA) {
                JoueurIA ia = (JoueurIA) joueurCourant;
                int[] coupIA = ia.choisirCoup(partieOthello);
                if (coupIA == null) {
                    ihm.afficherMessage(joueurCourant.getNom() + " ne peut pas jouer et passe son tour.");
                    partieOthello.passerTour();
                    tourDejaChange = true;
                    toursPassesConsecutivement++;
                    continue;
                }
                coup = (coupIA[0] + 1) + " " + (char) ('A' + coupIA[1]);
                ihm.afficherMessage("L'IA joue : " + coup);
                coupValide = partieOthello.jouerCoup(coup);
            } else {
                do {
                    coup = ihm.demanderCoupOthello(joueurCourant.getNom());
                    if (coup.equals("P")) {
                        if (coupsPossibles.isEmpty()) {
                            ihm.afficherMessage(joueurCourant.getNom() + " ne peut pas jouer et passe son tour.");
                            partieOthello.passerTour();
                            tourDejaChange = true;
                            coupValide = true;
                            toursPassesConsecutivement++;
                        } else {
                            ihm.afficherMessage("Vous avez des coups possibles. Vous ne pouvez pas passer votre tour.");
                        }
                    } else if (!coup.matches("[1-8] [A-H]")) {
                        ihm.afficherMessage("Format invalide. Exemple : 3 D.");
                        continue;
                    } else {
                        coupValide = partieOthello.jouerCoup(coup);
                        if (!coupValide) {
                            ihm.afficherMessage("Coup invalide. Réessayez.");
                        }
                    }
                } while (!coupValide);
            }
            if (coupValide && !tourDejaChange) {
                toursPassesConsecutivement = 0;
            }
            if (!tourDejaChange) {
                partieOthello.changerTour();
            }
        }
        ihm.afficherPlateauOthello(partieOthello.getPlateau());
        afficherResultatsPartieOthello(partieOthello);
    }

    /**
     * Gère le déroulement d'une partie d'Awalé.
     */
    private void jouerPartieAwale() {
        PartieAwale partieAwale = (PartieAwale) partie;
        while (!partieAwale.estTerminee()) {
            ihm.afficherPlateauAwale(partieAwale.getPlateau(),
                    partieAwale.getGrenier(partieAwale.getJoueur1()),
                    partieAwale.getGrenier(partieAwale.getJoueur2()));
            Joueur joueurCourant = partieAwale.getJoueurCourant();
            String coup;
            boolean coupValide = false;
            do {
                coup = ihm.demanderCoupAwale(joueurCourant.getNom());
                if (coup.equalsIgnoreCase("P")) {
                    partieAwale.passerTour();
                    coupValide = true;
                } else {
                    coupValide = partieAwale.jouerCoup(coup);
                    if (!coupValide) {
                        ihm.afficherMessage("Coup invalide. Réessayez.");
                    }
                }
            } while (!coupValide);
            partieAwale.changerTour();
        }
        ihm.afficherPlateauAwale(partieAwale.getPlateau(),
                partieAwale.getGrenier(partieAwale.getJoueur1()),
                partieAwale.getGrenier(partieAwale.getJoueur2()));
        afficherResultatsPartieAwale(partieAwale);
    }

    /**
     * Affiche les résultats d'une partie d'Othello.
     *
     * @param partieOthello La partie d'Othello terminée.
     */
    private void afficherResultatsPartieOthello(PartieOthello partieOthello) {
        Joueur vainqueur = partieOthello.getVainqueur();
        if (vainqueur != null) {
            ihm.afficherMessage("Le vainqueur est " + vainqueur.getNom() + " avec "
                    + partieOthello.compterPions(vainqueur.getCouleur()) + " pions.");
        } else {
            ihm.afficherMessage("Match nul !");
        }
    }

    /**
     * Affiche les résultats d'une partie d'Awalé.
     *
     * @param partieAwale La partie d'Awalé terminée.
     */
    private void afficherResultatsPartieAwale(PartieAwale partieAwale) {
        Joueur vainqueur = partieAwale.getVainqueur();
        if (vainqueur != null) {
            int score = (vainqueur == partieAwale.getJoueur1()) ? partieAwale.getGrenier(partieAwale.getJoueur1()) : partieAwale.getGrenier(partieAwale.getJoueur2());
            ihm.afficherMessage("Le vainqueur est " + vainqueur.getNom() + " avec " + score + " graines.");
        } else {
            ihm.afficherMessage("Match nul !");
        }
    }

    /**
     * Affiche les résultats finaux après une ou plusieurs parties.
     */
    private void afficherResultatsFinaux() {
        if (partie instanceof PartieOthello) {
            PartieOthello partieOthello = (PartieOthello) partie;
            int partiesGagneesJoueur1 = partieOthello.getPartiesGagnees(partieOthello.getJoueur1());
            int partiesGagneesJoueur2 = partieOthello.getPartiesGagnees(partieOthello.getJoueur2());
            if (partiesGagneesJoueur1 > partiesGagneesJoueur2) {
                ihm.afficherMessage("Le vainqueur final est " + partieOthello.getJoueur1().getNom() + " avec " + partiesGagneesJoueur1 + " partie(s) gagnée(s).");
            } else if (partiesGagneesJoueur2 > partiesGagneesJoueur1) {
                ihm.afficherMessage("Le vainqueur final est " + partieOthello.getJoueur2().getNom() + " avec " + partiesGagneesJoueur2 + " partie(s) gagnée(s).");
            } else {
                ihm.afficherMessage("Égalité parfaite !");
            }
        } else if (partie instanceof PartieAwale) {
            // Pour Awalé, on pourrait afficher le score total (greniers)
            PartieAwale partieAwale = (PartieAwale) partie;
            int scoreJ1 = partieAwale.getGrenier(partieAwale.getJoueur1());
            int scoreJ2 = partieAwale.getGrenier(partieAwale.getJoueur2());
            if (scoreJ1 > scoreJ2) {
                ihm.afficherMessage("Le vainqueur final est " + partieAwale.getJoueur1().getNom() + " avec " + scoreJ1 + " graines.");
            } else if (scoreJ2 > scoreJ1) {
                ihm.afficherMessage("Le vainqueur final est " + partieAwale.getJoueur2().getNom() + " avec " + scoreJ2 + " graines.");
            } else {
                ihm.afficherMessage("Égalité parfaite !");
            }
        }
    }
}
