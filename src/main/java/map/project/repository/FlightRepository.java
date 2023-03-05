package map.project.repository;

import map.project.domain.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class FlightRepository implements Repository<Long, Flight> {
    private final String url;
    private final String username;
    private final String password;

    public FlightRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Iterable<Flight> findAll() {
        Set<Flight> flightSet = new TreeSet<>(Comparator.comparing(Flight::getId));

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM flight");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("flight_id");
                String from = resultSet.getString("from");
                String to = resultSet.getString("to");
                LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
                LocalDateTime landingTime = resultSet.getTimestamp("landing_time").toLocalDateTime();
                int seats = resultSet.getInt("seats");

                Flight flight = new Flight(from, to, departureTime, landingTime, seats);
                flight.setId(id);
                flightSet.add(flight);
            }
            return flightSet;
        } catch (SQLException ex) {
            return flightSet;
        }
    }

    @Override
    public Flight findOne(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Flight save(Flight e) {
        throw new UnsupportedOperationException();
    }
}
