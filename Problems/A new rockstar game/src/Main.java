import java.util.*;     

/**
 * Observable interface
**/
interface Observable {
    public void addObserver(Observer o);
    public void removeObserver(Observer o);

    public void notifyObservers();

}

/**
 * Concrete Observable - Rockstar Games
**/
class RockstarGames implements Observable {

    private List<Observer> observers = new ArrayList<>();
    public String releaseGame;

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        observers
                .forEach(o -> {
                    o.update(releaseGame);
                });
    }

    public void release(String game) {
        this.releaseGame = game;
        notifyObservers();
    }
}

/**
 * Observer interface
**/
interface Observer {
    public void update(String game);
}

/**
 * Concrete observer - Gamer
**/
class Gamer implements Observer {

    private String name;
    private Observable observable;
    private Set<String> games = new HashSet<>();

    public Gamer(String name, Observable observable) {
        this.name = name;
        this.observable = observable;
    }

    public void update(String game) {
        System.out.println("Notification for gamer : "+name);

        if(games.contains(game))
            System.out.println("What? They've already released this game ... I don't understand");
        else
            buyGame(game);
    }

    public void buyGame(String game) {
        System.out.println(name + " says : \"Oh, Rockstar releases new game " + game + " !\"");
        games.add(game);
    }

    @Override
    public String toString() {
        return this.name;
    }    
}

/**
 * Main class
**/
public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        String game = null;

        RockstarGames rockstarGames = new RockstarGames();

        Gamer garry = new Gamer("Garry Rose", rockstarGames);
        Gamer peter = new Gamer("Peter Johnston", rockstarGames);
        Gamer helen = new Gamer("Helen Jack", rockstarGames);

        rockstarGames.addObserver(garry);
        rockstarGames.addObserver(peter);
        rockstarGames.addObserver(helen);

        for (int i = 0; i < 2; i++) {
            game = scanner.nextLine();
            rockstarGames.release(game);
        }

        scanner.close();
    }
}