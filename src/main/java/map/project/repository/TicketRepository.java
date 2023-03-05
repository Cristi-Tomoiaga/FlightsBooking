package map.project.repository;

import map.project.domain.Ticket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class TicketRepository implements Repository<Long, Ticket> {
    private final String url;
    private final String username;
    private final String password;

    public TicketRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Iterable<Ticket> findAll() {
        Set<Ticket> ticketSet = new TreeSet<>(Comparator.comparing(Ticket::getId));

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ticket");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                long ticketId = resultSet.getLong("ticket_id");
                String userName = resultSet.getString("username");
                long flightId = resultSet.getLong("flight_id");
                LocalDateTime purchaseTime = resultSet.getTimestamp("purchase_time").toLocalDateTime();

                Ticket ticket = new Ticket(userName, flightId, purchaseTime);
                ticket.setId(ticketId);
                ticketSet.add(ticket);
            }

            return ticketSet;
        } catch (SQLException ex) {
            return ticketSet;
        }
    }

    @Override
    public Ticket findOne(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Ticket save(Ticket e) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO ticket(username, flight_id, purchase_time) values (?, ?, ?)")
        ) {
            statement.setString(1, e.getUsername());
            statement.setLong(2, e.getFlightId());
            statement.setTimestamp(3, Timestamp.valueOf(e.getPurchaseTime()));
            statement.executeUpdate();

            return null;
        } catch (SQLException ex) {
            return e;
        }
    }
}
