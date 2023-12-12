package Modele;


public class ZPiece extends Piece {

    public ZPiece(GrilleJeu _grid) {
        super(_grid, new boolean[][]{
                        {false, false, true, false},
                        {false, true, true, false},
                        {false, true, false, false},
                        {false, false, false, false}
                }, Couleur.RED);
        // Modify other specific properties or behaviors if needed
    }

}
