package map.project.service;

import map.project.domain.Client;
import map.project.domain.Flight;
import map.project.domain.Ticket;
import map.project.domain.dto.FlightDto;
import map.project.repository.ClientRepository;
import map.project.repository.FlightRepository;
import map.project.repository.TicketRepository;
import map.project.utils.observer.Observable;
import map.project.utils.observer.Observer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service implements Observable {
    private final ClientRepository clientRepository;
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    private final List<Observer> observerList = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void notifyObservers(Long flightId) {
        observerList.forEach(observer -> observer.update(flightId));
    }

    public Service(ClientRepository clientRepository, FlightRepository flightRepository, TicketRepository ticketRepository) {
        this.clientRepository = clientRepository;
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
    }

    public Client login(String username) {
        return clientRepository.findOne(username);
    }

    private int getNumTicketsForFlight(long flightId) {
        return (int) StreamSupport.stream(ticketRepository.findAll().spliterator(), false)
                .filter(x -> x.getFlightId() == flightId)
                .count();
    }

    public List<FlightDto> getFilteredFlightsDto(String from, String to, LocalDate departureDate) {
        return StreamSupport.stream(flightRepository.findAll().spliterator(), false)
                .filter(x -> x.getFrom().equals(from)
                        && x.getTo().equals(to)
                        && x.getDepartureTime().toLocalDate().isEqual(departureDate))
                .map(x -> new FlightDto(x.getId(),
                                        x.getFrom(),
                                        x.getTo(),
                                        x.getDepartureTime(),
                                        x.getLandingTime(),
                                        x.getSeats(),
                                        x.getSeats() - getNumTicketsForFlight(x.getId()))
                )
                .collect(Collectors.toList());
    }

    public List<String> getFromLocations() {
        return StreamSupport.stream(flightRepository.findAll().spliterator(), false)
                .map(Flight::getFrom)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getToLocations() {
        return StreamSupport.stream(flightRepository.findAll().spliterator(), false)
                .map(Flight::getTo)
                .distinct()
                .collect(Collectors.toList());
    }

    public void buyTicket(String username, Long flightId, LocalDateTime purchaseTime) {
        Ticket ticket = new Ticket(username, flightId, purchaseTime);
        if (ticketRepository.save(ticket) != null) {
            throw new ServiceException("Could not add purchase");
        }

        notifyObservers(flightId);
    }
}
