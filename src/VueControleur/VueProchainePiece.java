package VueControleur;

import Modele.Couleur;
import Modele.GrilleJeu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;

class VueProchainePiece extends JPanel implements Observer {

    private final static int TAILLE = 16;
    private GrilleJeu grille;
    Canvas c;

    public VueProchainePiece(GrilleJeu _modele) {

        grille = _modele;
        setLayout(new BorderLayout());
        Dimension dim = new Dimension( TAILLE * 4, TAILLE * 4);
        this.setPreferredSize(dim);


        //setBackground(Color.black);

        c = new Canvas() {


            public void paint(Graphics g) {

                //clear my window
                g.setColor(Color.darkGray);
                g.fillRect(0, 0, getWidth(), getHeight());




                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {

                        g.setColor(Color.darkGray);
                        g.fillRect((i) * TAILLE, (j) * TAILLE, TAILLE, TAILLE);
                        g.setColor(Color.white);
                        g.drawRoundRect((i) * TAILLE, (j) * TAILLE, TAILLE, TAILLE, 1, 1);

                    }

                }


                if (grille.getProchainePiecePiece().getCouleur()== Couleur.RED) g.setColor(Color.RED);
                if (grille.getProchainePiecePiece().getCouleur()== Couleur.BLUE) g.setColor(Color.BLUE);
                if (grille.getProchainePiecePiece().getCouleur()== Couleur.ORANGE) g.setColor(Color.ORANGE);
                if (grille.getProchainePiecePiece().getCouleur()== Couleur.MAGENTA) g.setColor(Color.MAGENTA);
                if (grille.getProchainePiecePiece().getCouleur()== Couleur.CYAN) g.setColor(Color.CYAN);
                if (grille.getProchainePiecePiece().getCouleur()== Couleur.PINK) g.setColor(Color.PINK);
                if (grille.getProchainePiecePiece().getCouleur()== Couleur.YELLOW) g.setColor(Color.YELLOW);
                for (int i=0; i<4; i++) {
                    for (int j=0; j<4; j++) {
                        if (grille.getProchainePiecePiece().getShape()[i][j])

                            g.fillRect((i) * TAILLE, (j) * TAILLE, TAILLE, TAILLE);
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
