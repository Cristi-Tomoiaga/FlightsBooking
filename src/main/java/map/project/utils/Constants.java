package map.project.utils;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String URL = "jdbc:postgresql://localhost:5432/zboruri";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "postgres";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
