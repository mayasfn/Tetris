package Modele;


public class IPiece extends Piece {
    public IPiece(SimpleGrid _grid) {
        super(_grid, new boolean[][]{
                        {true, true, true, true},
                        {false, false, false, false},
                        {false, false, false, false},
                        {false, false, false, false}},
                Couleur.MAGENTA);
    }
}
