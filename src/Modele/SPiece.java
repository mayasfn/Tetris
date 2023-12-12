package Modele;



public class SPiece extends Piece {

    public SPiece(GrilleJeu _grid) {
        super(_grid, new boolean[][]{
                {false, true, false, false},
                {false, true, true, false},
                {false, false, true, false},
                {false, false, false, false}
        }, Couleur.PINK);
        // Modify other specific properties or behaviors if needed
    }

}
