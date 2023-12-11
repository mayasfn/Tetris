package Modele;

import java.awt.Color;

public class Piece implements Runnable {

    protected boolean [][] shape;
    private int x = 8;
    private int y = -5;
    private int dY = 1;
    private Couleur couleur;
    private SimpleGrid grid;

    public Piece(SimpleGrid _grid, boolean [][] _shape,Couleur _color) {
        grid= _grid;
        shape = _shape;
        couleur = _color;
    }

    public void action() {
        dY *= -1;
    }

    public void run() {
        System.out.println("pos" + x + " "+ y);
        int nextY = this.y;
        int nextX = this.x;

        nextY += dY;

        if (grid.validationPosition(nextX, nextY)) {
            this.y = nextY;
            this.x = nextX;

            System.out.println("pos x " + x + " y "+ y);
        } else {
            grid.fige_piece();
            grid.ligne_complete();

        }

    }


    public void move_right() {

        int nextX = this.x;
        nextX += 1;
        if (grid.validationPosition(nextX, this.y)) {
            this.x = nextX;

        }
    }

    public void move_left() {

        int nextX = this.x;
        nextX -= 1;
        if (grid.validationPosition(nextX, y)) {

            this.x = nextX;

        }
    }


    public void move_down(){
        int nextY= this.y;
        nextY +=2;
        if(grid.validationPosition(this.x, nextY)){
            this.y=nextY;
        }


    }
    public void rotation() {
        boolean nouvelle_piece[][]= new boolean [4][4];
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                nouvelle_piece[i][j]=shape[4 - 1 -j][i];
            }
        }
        if (grid.validationPositionRotation(this.x, this.y, nouvelle_piece)) shape = nouvelle_piece;

    }




    public Couleur getCouleur() {
        return couleur;
    }


    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }

    public void setX(int _x) {
       this.x = _x;
    }

    public void setY(int _y) {
        this.y = _y;
    }


    public boolean[][] getShape() {
        return shape;
    }

}
