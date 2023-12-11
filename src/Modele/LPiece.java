package Modele;


public class LPiece extends Piece{
    public LPiece(SimpleGrid _grid) {
        super(_grid, new boolean[][]{
                        {false, true, false, false},
                        {false, true, false, false},
                        {false, true, true, false},
                        {false, false, false, false}},
                Couleur.YELLOW);
    }
}
