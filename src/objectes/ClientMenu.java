package objectes;

import java.util.Scanner;

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
                    System.out.println("Funcionalitat de modificar client en desenvolupament.");
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
