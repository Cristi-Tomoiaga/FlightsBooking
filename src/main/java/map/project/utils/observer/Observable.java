package map.project.utils.observer;

public interface Observable {
    void addObserver(Observer observer);

    void notifyObservers(Long flightId);
}
