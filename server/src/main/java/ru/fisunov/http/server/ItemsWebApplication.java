package ru.fisunov.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;

public class ItemsWebApplication implements MyWebApplication {
    private String name;
    private List<Item> items;
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "IMM32167_";
    private static final String ITEMS = "SELECT id, item_names FROM public.items";

    public ItemsWebApplication() {
        this.name = "Items Web Application";
//        this.items = List.of(
//                new Item(1L, "Box"),
//                new Item(2L, "Phone"),
//                new Item(3L, "Table"),
//                new Item(4L, "Monitor"),
//                new Item(5L, "Chair")
//        );
        this.items = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, dbUser, dbPassword)) {
            try (PreparedStatement ps = connection.prepareStatement(ITEMS)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        long id = rs.getLong("id");
                        String item_names = rs.getString("item_names");
                        items.add(new Item(id, item_names));
                    }
                } catch (SQLException e) {
                }
            } catch (SQLException e) {
            }
        } catch (SQLException e) {
        }
    }

    @Override
    public void execute(Request request, OutputStream output) throws IOException {
//        StringBuilder builder = new StringBuilder("[ ");
//        for (int i = 0; i < items.size(); i++) {
//            builder.append("{ \"id\": ").append(items.get(i).getId()).append(", \"title\": \"").append(items.get(i).getTitle()).append("\" }");
//            if (i < items.size() - 1) {
//                builder.append(", ");
//            }
//        }
//        builder.append(" ]");
        Gson gson = new Gson();
        String jsonItems = gson.toJson(items);

        output.write(("" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Access-Control-Allow-Origin: *\r\n" +
                "\r\n" +
                // builder
                jsonItems
        ).getBytes(StandardCharsets.UTF_8));
    }
}
