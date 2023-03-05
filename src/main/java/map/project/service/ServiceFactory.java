package map.project.service;

import map.project.repository.ClientRepository;
import map.project.repository.FlightRepository;
import map.project.repository.TicketRepository;
import map.project.utils.Constants;

public class ServiceFactory {
    public static Service getService(String url, String username, String password) {
        return new Service(
                new ClientRepository(url, username, password),
                new FlightRepository(url, username, password),
                new TicketRepository(url, username, password)
        );
    }

    public static Service getService() {
        return getService(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
    }
}
