package aseds_snake;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
//classe serialisable et comparable pour representer un joueur
public class Player implements Serializable,Comparable<Object> {

	private static final long serialVersionUID = -3059492723917507165L;
	public static ArrayList<Player> highScore = (ArrayList<Player>) getHighscore(); //variable static pour l'ensemble des highscores
    private String name;
    private int score;

    public Player() {
    }

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    static private List<Player> getHighscore() { //lecture dse highscore depuis un fichier
        ObjectInputStream ois = null;
        ArrayList<Player> highScores = new ArrayList<Player>();
        try {
            File f = new File("highscoredata");
            if (!f.exists()) f.createNewFile();  //on cree le fichier si il existe pas
            final FileInputStream fichier = new FileInputStream(f); 
            if (fichier.available() > 0) {
                ois = new ObjectInputStream(fichier);
                highScores = (ArrayList<Player>) ois.readObject(); //ecriture de l'objet
                
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }

            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
        return highScores;
    }
    public boolean checkIfHighScore() { //on verifie si il s'agit d'un highscore
        if(highScore.isEmpty())return true;
        for (Player p : highScore) {
            if (p.score < this.score)
                return true;
        }
        return false;
    }

    public static void addToHighScore(Player player) throws CloneNotSupportedException {
        highScore.add(new Player(player.getName(),player.getScore()));//On ajoute le score
        highScore.sort(Player::compareTo);//on va mettre l'arraylist on ordre

        highScore = new ArrayList<>(  highScore.subList(0, (highScore.size()<5? highScore.size():4 ))); //on supprime le plus pour qu'on laisse que 5 entrées
        ObjectOutputStream oos = null;

        try {
            final FileOutputStream fichier = new FileOutputStream("highscoredata");
            oos = new ObjectOutputStream(fichier);
            oos.writeObject(highScore);//ecriture du nouveau objet dans le fichier
            // ...
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }

            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //implimentation de la methode de comparaison  
    @Override
    public int compareTo(Object o) {
        Player p2 = (Player)o;
        return -(score-p2.score);
    }
}
