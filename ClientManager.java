// ClientManager.java - Task 2: Manage Clients
// This class handles adding, updating, and removing clients
// Data is saved to a text file (clients.txt) so it stays after closing

import java.util.*;
import java.io.*;

public class ClientManager {
    private List<Client> clients = new ArrayList<>();
    private int nextId = 1;
    private static final String FILE = "clients.txt";

    public ClientManager() {
        loadFromFile();
        if (clients.isEmpty()) {
            // Add sample clients so the app doesn't look empty
            addClient("Saman Stores", "saman@gmail.com", "0771234567", "Colombo 03");
            addClient("Perera Mart", "perera@gmail.com", "0769876543", "Kandy");
            addClient("Lanka Retail", "lanka@gmail.com", "0751112233", "Galle");
        }
    }

    // Add a new client
    public Client addClient(String name, String email, String phone, String address) {
        String id = "C" + String.format("%03d", nextId++);
        Client client = new Client(id, name, email, phone, address);
        clients.add(client);
        saveToFile();
        return client;
    }

    // Update an existing client
    public void updateClient(Client client, String name, String email, String phone, String address) {
        client.setName(name);
        client.setEmail(email);
        client.setPhone(phone);
        client.setAddress(address);
        saveToFile();
    }

    // Remove a client
    public void removeClient(Client client) {
        clients.remove(client);
        saveToFile();
    }

    // Get all clients
    public List<Client> getAllClients() {
        return new ArrayList<>(clients);
    }

    // Search clients by name
    public List<Client> searchClients(String keyword) {
        List<Client> result = new ArrayList<>();
        for (Client c : clients) {
            if (c.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(c);
            }
        }
        return result;
    }

    // Get total number of clients
    public int getTotalClients() {
        return clients.size();
    }

    // Save data to file
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE))) {
            writer.println(nextId);
            for (Client c : clients) {
                writer.println(c.getId() + "|" + c.getName() + "|" + c.getEmail() + "|" + c.getPhone() + "|" + c.getAddress());
            }
        } catch (IOException e) {
            System.out.println("Error saving clients: " + e.getMessage());
        }
    }

    // Load data from file
    private void loadFromFile() {
        File file = new File(FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            nextId = Integer.parseInt(reader.readLine().trim());
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    clients.add(new Client(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading clients: " + e.getMessage());
        }
    }
}
