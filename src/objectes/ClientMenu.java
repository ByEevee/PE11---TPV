package objectes;

import java.util.Scanner;

import DAO.ClientDAO;

/**
 * Menú interactiu per a la gestió de clients.
 * Segueix el mateix patró que ArticleMenu.
 */
public class ClientMenu {

    private Scanner sc;
    private ClientService service;

    public ClientMenu(ClientService service) {
        this.service = service;
        this.sc = new Scanner(System.in);
    }

    /**
     * Mostra el menú principal de gestió de clients.
     */
    public void mostraMenu() {
        boolean tornar = false;

        while (!tornar) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           GESTIÓ DE CLIENTS");
            System.out.println("=".repeat(50));
            System.out.println("1. Afegir un client");
            System.out.println("2. Eliminar un client");
            System.out.println("3. Modificar un client");
            System.out.println("4. Veure tots els clients");
            System.out.println("5. Cercar client per DNI");
            System.out.println("0. Tornar enrere");
            System.out.println("=".repeat(50));
            System.out.print("Selecciona una opció: ");

            int opcio = llegirEnter();

            switch (opcio) {
                case 1:
                    altaClient();
                    break;
                case 2:
                    baixaClient();
                    break;
                case 3:
                    modificarClient();
                    break;
                case 4:
                    veureTots();
                    break;
                case 5:
                    cercarPerDni();
                    break;
                case 0:
                    tornar = true;
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        }
    }

    // =========================================================
    // ALTA DE CLIENTS
    // =========================================================

    /**
     * Recull les dades per pantalla i crida el servei d'alta.
     * El DNI és obligatori; email i telèfon són opcionals (Enter per deixar-los buits).
     */
    private void altaClient() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("ALTA DE CLIENT");
        System.out.println("-".repeat(50));

        System.out.print("DNI (màx. 10 caràcters): ");
        String dni = sc.nextLine().trim();

        System.out.print("Nom complet: ");
        String nom = sc.nextLine().trim();

        System.out.print("Email (opcional, Enter per ometre): ");
        String email = sc.nextLine().trim();

        System.out.print("Telèfon (opcional, Enter per ometre): ");
        String telefon = sc.nextLine().trim();

        boolean resultat = service.altaClient(dni, nom, email, telefon);

        if (!resultat) {
            System.out.println("\nIntenta-ho novament amb dades vàlides.");
        }
    }

    // =========================================================
    // BAIXA DE CLIENTS
    // =========================================================

    /**
     * Demana el DNI per pantalla, mostra les dades del client trobat,
     * sol·licita confirmació i crida el servei d'eliminació.
     */
    private void baixaClient() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("BAIXA DE CLIENT");
        System.out.println("-".repeat(50));

        System.out.print("DNI del client a eliminar: ");
        String dni = sc.nextLine().trim();

        if (dni.isEmpty()) {
            System.out.println("El DNI no pot estar buit.");
            return;
        }

        // Mostrar les dades del client abans de confirmar
        objectes.Client client = service.getClientDAO().getByDni(dni);
        if (client == null) {
            System.err.println("Error: No existeix cap client amb el DNI '" + dni + "'.");
            return;
        }

        System.out.println("\nClient trobat:");
        System.out.println("  " + client);
        System.out.print("\nEstàs segur que vols eliminar aquest client? (s/n): ");
        String confirmacio = sc.nextLine().trim().toLowerCase();

        if (!confirmacio.equals("s")) {
            System.out.println("Operació cancel·lada.");
            return;
        }

        service.baixaClient(dni);
    }

    // =========================================================
    // MODIFICACIÓ DE CLIENTS
    // =========================================================

    /**
     * Mostra les dades actuals del client i permet modificar
     * nom, email i telèfon (Enter per deixar el valor sense canvis).
     */
    private void modificarClient() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("MODIFICACIÓ DE CLIENT");
        System.out.println("-".repeat(50));

        System.out.print("DNI del client a modificar: ");
        String dni = sc.nextLine().trim();

        if (dni.isEmpty()) {
            System.out.println("El DNI no pot estar buit.");
            return;
        }

        // Mostrar les dades actuals
        Client client = service.getClientDAO().getByDni(dni);
        if (client == null) {
            System.err.println("Error: No existeix cap client amb el DNI '" + dni + "'.");
            return;
        }

        System.out.println("\nDades actuals:");
        System.out.println("  " + client);
        System.out.println("\nIntrodueix els nous valors (Enter per mantenir l'actual):");

        System.out.print("Nom [" + client.getNom() + "]: ");
        String nouNom = sc.nextLine().trim();

        String emailActual = (client.getEmail() != null && !client.getEmail().isEmpty()) ? client.getEmail() : "—";
        System.out.print("Email [" + emailActual + "]: ");
        String nouEmail = sc.nextLine().trim();

        String telefonActual = (client.getTelefon() != null && !client.getTelefon().isEmpty()) ? client.getTelefon() : "—";
        System.out.print("Telèfon [" + telefonActual + "]: ");
        String nouTelefon = sc.nextLine().trim();

        service.modificarClient(dni, nouNom, nouEmail, nouTelefon);
    }
    // =========================================================
    // CONSULTES
    // =========================================================

    private void veureTots() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("TOTS ELS CLIENTS");
        System.out.println("-".repeat(50));

        var clients = service.getClientDAO().getAll();

        if (clients.isEmpty()) {
            System.out.println("No hi ha clients registrats.");
        } else {

            for (int i = 0; i < clients.size(); i++) {
                System.out.println((i + 1) + ". " + clients.get(i));
            }

            System.out.println("\nTotal: " + clients.size() + " clients");
        }
    }

    private void cercarPerDni() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("CERCA PER DNI");
        System.out.println("-".repeat(50));

        System.out.print("Introdueix el DNI: ");
        String dni = sc.nextLine().trim();

        var client = service.getClientDAO().getByDni(dni);

        if (client == null) {
            System.out.println("No s'ha trobat cap client amb el DNI '" + dni + "'.");
        } else {
            System.out.println(client);
        }
    }
    // =========================================================
    // UTILITATS
    // =========================================================

    private int llegirEnter() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.print("Valor invàlid. Intenta-ho novament: ");
            return llegirEnter();
        }
    }
}
