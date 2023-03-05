package map.project.repository;

import map.project.domain.Client;

import java.sql.*;

public class ClientRepository implements Repository<String, Client> {
    private final String url;
    private final String username;
    private final String password;

    public ClientRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Iterable<Client> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client findOne(String userName) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM client WHERE username=?")
        ) {
            statement.setString(1, userName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String id = resultSet.getString("username");
                    String name = resultSet.getString("name");

                    Client client = new Client(name);
                    client.setId(id);
                    return client;
                }
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }

    @Override
    public Client save(Client e) {
        throw new UnsupportedOperationException();
    }
}
