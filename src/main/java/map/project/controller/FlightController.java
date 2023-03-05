package map.project.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import map.project.domain.Client;
import map.project.domain.dto.FlightDto;
import map.project.service.Service;
import map.project.service.ServiceException;
import map.project.utils.Constants;
import map.project.utils.MessageAlert;
import map.project.utils.observer.Observer;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FlightController implements Observer {
    public ComboBox<String> fromComboBox;
    public ComboBox<String> toComboBox;
    public DatePicker departureDatePicker;
    public TableView<FlightDto> flightsTable;
    public TableColumn<FlightDto, String> fromFlightsColumn;
    public TableColumn<FlightDto, String> toFlightsColumn;
    public TableColumn<FlightDto, String> departureFlightsColumn;
    public TableColumn<FlightDto, String> landingFlightsColumn;
    public TableColumn<FlightDto, Integer> seatsFlightsColumn;
    public TableColumn<FlightDto, Integer> availableFlightsColumn;
    public Pagination flightsPagination;

    private Service service;
    private Client client;
    private final ObservableList<FlightDto> flightModel = FXCollections.observableArrayList();
    private final ObservableList<String> fromLocationModel = FXCollections.observableArrayList();
    private final ObservableList<String> toLocationModel = FXCollections.observableArrayList();

    private final int ROWS_PER_PAGE = 2;
    private final ObservableList<FlightDto> flightPageModel = FXCollections.observableArrayList();

    public void setServiceClient(Service service, Client client) {
        this.service = service;
        this.client = client;
        service.addObserver(this);
        refreshData();
    }

    @FXML
    public void initialize() {
        departureDatePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate object) {
                if (object == null)
                    return "";

                return object.format(Constants.DATE_FORMATTER);
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.equals(""))
                    return null;

                return LocalDate.parse(string, Constants.DATE_FORMATTER);
            }
        });

        fromComboBox.setItems(fromLocationModel);
        toComboBox.setItems(toLocationModel);

        fromFlightsColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        toFlightsColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        departureFlightsColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getDepartureTime().format(Constants.DATE_TIME_FORMATTER)));
        landingFlightsColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getLandingTime().format(Constants.DATE_TIME_FORMATTER)));
        seatsFlightsColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));
        availableFlightsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        flightsTable.setItems(flightPageModel);

        flightsPagination.setPageCount(1);
        flightsPagination.setPageFactory(page -> {
            refreshPageData(page);

            return flightsTable;
        });
    }

    private void refreshData() {
        fromLocationModel.setAll(service.getFromLocations());
        toLocationModel.setAll(service.getToLocations());
    }

    private void refreshPagination() {
        int numPages = 1;

        if (!flightModel.isEmpty() && flightModel.size() % ROWS_PER_PAGE == 0) {
            numPages = flightModel.size() / ROWS_PER_PAGE;
        } else if (flightModel.size() > ROWS_PER_PAGE) {
            numPages = flightModel.size() / ROWS_PER_PAGE + 1;
        }
        flightsPagination.setPageCount(numPages);

        refreshPageData(flightsPagination.getCurrentPageIndex());
    }

    private void refreshPageData(int page) {
        int fromIndex = page * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, flightModel.size());
        flightPageModel.setAll(flightModel.subList(fromIndex, toIndex));
    }

    public void handleSearch() {
        String from = fromComboBox.getSelectionModel().getSelectedItem();
        String to = toComboBox.getSelectionModel().getSelectedItem();
        LocalDate departureDate = departureDatePicker.getValue();

        if (from == null || from.equals("") || to == null || to.equals("") || departureDate == null) {
            MessageAlert.showErrorMessage(null, "Provide valid data for from, to or departure date");
            return;
        }

        flightModel.setAll(service.getFilteredFlightsDto(from, to, departureDate));
        refreshPagination();
    }

    public void handleBuyTicket() {
        FlightDto flight = flightsTable.getSelectionModel().getSelectedItem();
        if (flight == null) {
            MessageAlert.showErrorMessage(null, "Please select a flight");
            return;
        }

        try {
            service.buyTicket(client.getId(), flight.getFlightId(), LocalDateTime.now());
        } catch (ServiceException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
            return;
        }

        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Ticket bought successfully", "");
    }

    @Override
    public void update(Long flightId) {
        if (flightModel.stream().anyMatch(x -> x.getFlightId() == flightId))
            handleSearch();
    }
}
