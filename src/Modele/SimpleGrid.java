package Modele;

import java.util.Observable;
import java.util.Random;

import VueControleur.Sound;


public class SimpleGrid extends Observable implements Runnable {
    private Sound deletelineSound;
    public final int TAILLE = 20;
    private Piece currentPiece;
    private Piece prochainePiece;
    private int score = 0;
    private Piece [] piece_types = new Piece [7];
    private Couleur grille_couleur[][] = new Couleur[20][20];

    public boolean paused = false;

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused){
        this.paused=paused;
    }

    public SimpleGrid() {
        String projectRoot = System.getProperty("user.dir");
        deletelineSound = new Sound(projectRoot+ "/src/VueControleur/clear.wav");

        //Fill in our Piece array with each type of piece we have
        piece_types [0] = new IPiece(this);
        piece_types [1] = new JPiece(this);
        piece_types [2] = new LPiece(this);
        piece_types [3] = new OPiece(this);
        piece_types [4] = new SPiece(this);
        piece_types [5] = new TPiece(this);
        piece_types [6] = new ZPiece(this);
        Random random = new Random();
        int randompiece = random.nextInt(7);
        this.prochainePiece = piece_types[randompiece];

        remplir_Grille();
        //attribute_piecetype();
        new OrdonnanceurSimple(this).start();
    }

    public void remplir_Grille() {
        for (int i = 0; i<TAILLE; i++) {
            for (int j=0; j<TAILLE; j++)

                grille_couleur[i][j]=Couleur.VIDE;
        }
    }


    //Function that randomly picks a piece type to be our current piece
    public void attribute_piecetype() {

        this.currentPiece = this.prochainePiece;
        this.currentPiece.setX(8);
        this.currentPiece.setY(-5);
        //Pick a random number between 0 and 6 (inclusive) to pick a random piece type

        Random random = new Random();
        int randompiece = random.nextInt(7);
        this.prochainePiece = piece_types[randompiece];

        //Set the current piece as the shape chosen

    }

    public void action() {
        //currentPiece.action();


    }

    public boolean checkCollision(int _nextX, int _nextY, boolean tab[][]) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tab[i][j]) {
                    int gridX = _nextX + i;
                    int gridY = _nextY + j;

                    // Check if the cell is already occupied by another piece
                    if (gridY >= 0 && gridY < TAILLE && gridX >= 0 && gridX < TAILLE) {
                        if (grille_couleur[gridX][gridY] != Couleur.VIDE) {
                            return true; // Collision with an existing piece
                        }
                    }

                }
            }
        }
        return false; // No collision detected
    }

    public boolean validationPosition(int _nextX, int _nextY) {
        boolean position_valideY = true;
        boolean position_valideX = true;
        int positionY_absolu;
        int positionX_absolu;

        for (int i = 0; i < 4 ; i++) {
            if (!position_valideX || !position_valideY) break;
            for (int j = 0; j < 4; j++) {
                if (currentPiece.shape[i][j]) {
                    positionX_absolu = _nextX +i;
                    positionY_absolu = _nextY + j;
                    if (positionY_absolu >= TAILLE) position_valideY = false;
                    if (positionX_absolu >= TAILLE || positionX_absolu < 0) position_valideX = false;

                }
            }
        }

        return (position_valideX && position_valideY && !checkCollision(_nextX,_nextY, currentPiece.getShape()))  ;
    }


    public boolean validationPositionRotation(int _nextX, int _nextY, boolean tab[][]) {
        boolean position_valideY = true;
        boolean position_valideX = true;
        int positionY_absolu;
        int positionX_absolu;

        for (int i = 0; i < 4 ; i++) {
            if (!position_valideX || !position_valideY) break;
            for (int j = 0; j < 4; j++) {
                if (tab[i][j]) {
                    positionX_absolu = _nextX +i;
                    positionY_absolu = _nextY + j;

                    if (positionY_absolu >= TAILLE) position_valideY = false;
                    if (positionX_absolu >= TAILLE || positionX_absolu < 0) position_valideX = false;

                }
            }
        }
        if (checkCollision(_nextX,_nextY, tab)) return  false;
        return (position_valideX && position_valideY);
    }




    public void run() {
        if (!paused) {
            currentPiece.run();
            setChanged(); // setChanged() + notifyObservers() : notification de la vue pour le rafraichissement
            notifyObservers();

        }
    }

    public Piece getcurrentPiece() {
        return currentPiece;
    }

    public Piece getProchainePiecePiece() {
        return prochainePiece;
    }

    public Couleur getGrille_couleur(int i, int j) {
        return this.grille_couleur[i][j];
    }


    public void fige_piece() {
        for (int i=0; i<4;i++) {
            for (int j=0; j<4; j++) {
                int Xabsolu = currentPiece.getx() +i;
                int Yabsolu = currentPiece.gety() + j;
                 if (Xabsolu >=0 && Xabsolu <= TAILLE && Yabsolu <= TAILLE && currentPiece.shape[i][j]) grille_couleur[Xabsolu][Yabsolu] = currentPiece.getCouleur();
            }
        }

        //this.currentPiece = this.prochainePiece;
        //this.currentPiece.setX(8);
        //this.currentPiece.setY(-5);
        attribute_piecetype();



    }

    public void  ligne_complete() {
        // int nombreligne = 0;
        int complete = 0;
        boolean ligneComplete = false;
        do {
            ligneComplete = false;
            for (int i = TAILLE - 1; i >= 0; i--) {
                boolean verifieCase = true;
                // Check if the current row is complete
                for (int j = 0; j < TAILLE; j++) {
                    if (this.grille_couleur[j][i] == Couleur.VIDE) {
                        verifieCase = false;
                        break;
                    }
                }

                if (verifieCase) {
                    //nombreligne++;
                    ligneComplete = true;
                    complete++;

                    // Clear the completed line
                    for (int j = 0; j < TAILLE; j++) {
                        this.grille_couleur[j][i] = Couleur.VIDE;
                    }


                    // Shift down the pieces above the completed line
                    for (int k = i; k > 0; k--) {
                        for (int j = 0; j < TAILLE; j++) {
                            this.grille_couleur[j][k] = this.grille_couleur[j][k - 1];
                        }
                    }


                }
            }

        } while (ligneComplete);



        if(complete > 0){
            deletelineSound.play();
        }

        incremente_score(complete);
    }

    public void incremente_score(int ligne) {
        switch (ligne) {
            case 0:
                break;
            case 1:
                score +=40;
                break;
            case 2:
                score +=100;
                break;
            case 3:
                score +=300;
                break;
            default:
                score+=1200;
        }

    }


    public int get_score() {
        return this.score;
    }


    public void resetgrid() {
        Random random = new Random();
        int randompiece = random.nextInt(7);
        this.prochainePiece = piece_types[randompiece];
        //this.prochainePiece.setX(8);
        //this.prochainePiece.setY(-5);
        attribute_piecetype();
    }
    public void setCurrentPiece(Piece _currentPiece) {
        this.currentPiece = _currentPiece;
    }
}
