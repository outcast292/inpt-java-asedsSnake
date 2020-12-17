package aseds_snake;

//class pour representer un point sur l'ecrant
public class Point {
    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
 
    
    //redefinition de la methode de comparaison entre objets
    @Override
    public boolean equals(Object o) {
        if(o==null) return false;
        if (this == o) return true;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

}
