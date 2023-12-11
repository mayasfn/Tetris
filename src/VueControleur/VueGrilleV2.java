package VueControleur;

import Modele.Couleur;
import Modele.SimpleGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;

class VueGrilleV2 extends JPanel implements Observer {

    private final static int TAILLE = 16;
    private SimpleGrid grid;
    Canvas c;

    public VueGrilleV2(SimpleGrid _modele) {

        grid = _modele;
        setLayout(new BorderLayout());
        int width = TAILLE * grid.TAILLE ;
        int height = TAILLE * grid.TAILLE ;
        Dimension dim = new Dimension(width,height);
        this.setPreferredSize(dim);



        JPanel AdroiteGrille = new JPanel(new BorderLayout());
        AdroiteGrille.setBackground(Color.darkGray);

        //setBackground(Color.black);

        c = new Canvas() {


            public void paint(Graphics g) {

                //clear my window
                g.setColor(Color.darkGray);
                g.fillRect(0, 0, getWidth(), getHeight());

                //add space before the grid
                int extra_x = 4;
                int extra_y = 4;


                for (int i = 0; i < grid.TAILLE; i++) {
                    for (int j = 0; j < grid.TAILLE; j++) {

                        if (grid.getGrille_couleur(i,j) == Couleur.VIDE) g.setColor(Color.darkGray);
                        if (grid.getGrille_couleur(i,j)== Couleur.RED) g.setColor(Color.RED);
                        if (grid.getGrille_couleur(i,j)== Couleur.BLUE) g.setColor(Color.BLUE);
                        if (grid.getGrille_couleur(i,j)== Couleur.ORANGE) g.setColor(Color.ORANGE);
                        if (grid.getGrille_couleur(i,j)== Couleur.MAGENTA) g.setColor(Color.MAGENTA);
                        if (grid.getGrille_couleur(i,j)== Couleur.CYAN) g.setColor(Color.CYAN);
                        if (grid.getGrille_couleur(i,j)== Couleur.PINK) g.setColor(Color.PINK);
                        if (grid.getGrille_couleur(i,j)== Couleur.YELLOW) g.setColor(Color.YELLOW);
                        g.fillRect((i+extra_x) * TAILLE, (j+extra_y) * TAILLE, TAILLE, TAILLE);
                        g.setColor(Color.white);
                        g.drawRoundRect((i+extra_x) * TAILLE, (j+extra_y) * TAILLE, TAILLE, TAILLE, 1, 1);

                    }

                }


                if (grid.getcurrentPiece().getCouleur()== Couleur.RED) g.setColor(Color.RED);
                if (grid.getcurrentPiece().getCouleur()== Couleur.BLUE) g.setColor(Color.BLUE);
                if (grid.getcurrentPiece().getCouleur()== Couleur.ORANGE) g.setColor(Color.ORANGE);
                if (grid.getcurrentPiece().getCouleur()== Couleur.MAGENTA) g.setColor(Color.MAGENTA);
                if (grid.getcurrentPiece().getCouleur()== Couleur.CYAN) g.setColor(Color.CYAN);
                if (grid.getcurrentPiece().getCouleur()== Couleur.PINK) g.setColor(Color.PINK);
                if (grid.getcurrentPiece().getCouleur()== Couleur.YELLOW) g.setColor(Color.YELLOW);
                for (int i=0; i<4; i++) {
                    for (int j=0; j<4; j++) {
                        if (grid.getcurrentPiece().getShape()[i][j])

                            g.fillRect((grid.getcurrentPiece().getx() +i + extra_x) * TAILLE, (grid.getcurrentPiece().gety()+j+ extra_y) * TAILLE, TAILLE, TAILLE);
                    }
                }




            }
        };

        c.setPreferredSize(dim);
        add(c, BorderLayout.CENTER);
    }


    @Override
    public void update(Observable o, Object arg) {
        BufferStrategy bs = c.getBufferStrategy(); // bs + dispose + show : double buffering pour éviter les scintillements
        if(bs == null) {
            c.createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        c.paint(g); // appel de la fonction pour dessiner
        g.dispose();
        bs.show();
    }




}
