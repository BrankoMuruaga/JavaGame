package game;

public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position right(int n) {
        return new Position(x + n, y);
    }
    
    public Position left(int n) {
        return new Position(x - n, y);
    }
    
    public Position up(int n) {
        return new Position(x, y - n);
    }
    
    public Position down(int n) {
        return new Position(x, y + n);
    }

    @Override
    public String toString() {
        return x+","+y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position other = (Position) obj;
        return x == other.x && y == other.y;
    }
    
    
}
