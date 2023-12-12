package Modele;


public class OPiece extends Piece{
    public OPiece(GrilleJeu _grid) {
        super(_grid, new boolean[][]{
                        {false, true, true, false},
                        {false, true, true, false},
                        {false, false, false, false},
                        {false, false, false, false}},
                Couleur.CYAN);
    }
}
