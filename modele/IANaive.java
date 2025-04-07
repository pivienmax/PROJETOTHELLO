package modele;

import java.util.List;

public class IANaive implements ModeIA {
    @Override
    public int[] jouerCoup(Partie partie, char couleur) {
        List<int[]> coupsPossibles = partie.getCoupsPossibles(couleur);
        if (!coupsPossibles.isEmpty()) {
            return coupsPossibles.get(0); // Prend le premier coup valide
        }
        return null;
    }
}
