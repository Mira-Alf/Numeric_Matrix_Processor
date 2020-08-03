import java.util.ArrayList;
import java.util.List;

interface Movable {
    int getX();
    int getY();
    void setX(int newX);
    void setY(int newY);
}

interface Storable {
    int getInventoryLength();
    String getInventoryItem(int index);
    void setInventoryItem(int index, String item);
}

interface Command {
    void execute();
    void undo();
}

class CommandMove implements Command {
    Movable entity;
    int xMovement;
    int yMovement;
    private List<Point> points = new ArrayList<>();

    public CommandMove() {

    }

    public void setxMovement(int xMovement) {
        this.xMovement = xMovement;
    }

    public void setyMovement(int yMovement) {
        this.yMovement = yMovement;
    }

    public void execute() {
        Point currentPosition = new Point(entity.getX(), entity.getY());
        Point newPosition = new Point( entity.getX()+xMovement, entity.getY()+yMovement);
        entity.setX(newPosition.getX());
        entity.setY(newPosition.getY());
        points.add(currentPosition);
    }

    public void undo() {
        if(points.size()>0) {
            Point lastPosition = points.get(points.size() - 1);
            entity.setX(lastPosition.getX());
            entity.setY(lastPosition.getY());
            points.remove(points.size() - 1);
        }
    }
}

class Point {
    private int xPoint, yPoint;

    public Point(int xPoint, int yPoint) {
        this.xPoint = xPoint;
        this.yPoint = yPoint;
    }

    public int getX() {
        return this.xPoint;
    }

    public int getY() {
        return this.yPoint;
    }
}

class CommandPutItem implements Command {
    Storable entity;
    String item;
    private List<Integer> inventoryIndices = new ArrayList<>();

    @Override
    public void execute() {
        for(int i = 0; i < entity.getInventoryLength(); i++) {
            if(entity.getInventoryItem(i) == null) {
                entity.setInventoryItem(i, item);
                inventoryIndices.add(i);
                break;
            }
        }
    }

    @Override
    public void undo() {
        if(inventoryIndices.size() > 0) {
            Integer lastAddedIndex = inventoryIndices.get(inventoryIndices.size() - 1);
            entity.setInventoryItem(lastAddedIndex, null);
            inventoryIndices.remove(inventoryIndices.size() - 1);
        }
    }
}