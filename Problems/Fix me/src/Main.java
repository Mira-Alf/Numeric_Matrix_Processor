import java.util.*;     

/** Observable */
interface Observable {
    public void addObserver(Observer o);
    public void removeObserver(Observer o);

    public void notifyObservers();
    public void release(String text);
}

/** Concrete Observable */
class RockstarGames implements Observable {

    public String releaseGame;
    private List<Observer> observers = new ArrayList<>();

    public void release(String game) {
        this.releaseGame = game;
        notifyObservers();
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(o->o.update(releaseGame));
    }
}

/** Observer */
interface Observer {
    public void update(String text);
}

/** Concrete Observer */
class Gamer implements Observer {

    private String name;
    private String reaction;
    private Observable rockstarGames;
    private Set<String> games = new HashSet<>();

    public Gamer(String name,String reaction, Observable rockstarGames) {
        this.reaction = reaction;
        this.rockstarGames = rockstarGames;
        this.name = name;
    }

    public void buyGame(String game) {
        games.add(game);
        System.out.println(reaction);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public void update(String game) {
        System.out.println("Inform message to : "+name );
        if(!games.contains(game)) {
            System.out.print(name + " says: ");
            buyGame(game);
        }
    }
}

/** Main Class */

public class Main {
    public static void main(String[] args) {

        final Scanner scanner = new Scanner(System.in);

        String game = null;

        Observable rockstarGames = new RockstarGames();

        Gamer garry = new Gamer("Garry Rose", "I want to pre-order", rockstarGames);
        Gamer peter = new Gamer("Peter Johnston", "Pinch me...", rockstarGames);
        Gamer helen = new Gamer("Helen Jack", "Jesus, it's new game from Rockstar!", rockstarGames);

        rockstarGames.addObserver(garry);
        rockstarGames.addObserver(peter);
        rockstarGames.addObserver(helen);

        game = scanner.nextLine();

        System.out.println("It's happened! RockstarGames releases new game - " + game + "!");

        rockstarGames.release(game);

        scanner.close();
    }
}