package Modele;

public class JPiece extends Piece{
    public JPiece(SimpleGrid _grid) {
        super(_grid, new boolean[][]{
                        {false, false, true, false},
                        {false, false, true, false},
                        {false, true, true, false},
                        {false, false, false, false}},
                Couleur.BLUE);
    }
}
