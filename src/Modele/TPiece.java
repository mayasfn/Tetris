package Modele;


public class TPiece extends Piece {

    public TPiece(GrilleJeu _grid) {
        super(_grid, new boolean[][]{
                {false, true, true, true},
                {false, false, true, false},
                {false, false, false, false},
                {false, false, false, false}
        }, Couleur.ORANGE);
        // Modify other specific properties or behaviors if needed
    }

}
