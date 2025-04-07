package modele;

public class JoueurIA extends Joueur {
    private ModeIA strategie;

    public JoueurIA(String nom, char couleur, ModeIA strategie) {
        super(nom, couleur);
        this.strategie = strategie;
    }

    public int[] choisirCoup(Partie partie) {
        return strategie.jouerCoup(partie, getCouleur());
    }
}
