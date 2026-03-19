package seedu.equipmentmaster.storage;

import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.ui.Ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that handles the storage of the equipment list.
 * It reads data from and writes data to a specified .txt file.
 */
public class Storage {
    private String filePath;
    private Ui ui;
    private String settingsPath = "data/settings.txt";

    /**
     * Constructor.
     * @param filePath The relative path to the .txt storage file.
     */
    public Storage(String filePath, Ui ui) {
        this.filePath = filePath;
        this.ui = ui;
    }

    /**
     * Saves the current list of equipment to the .txt file.
     * @param equipments The current list of equipment.
     */
    public void save(ArrayList<Equipment> equipments){
        try {
            File file = new File(filePath);
            File directory = file.getParentFile();
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }

            try (FileWriter writer = new FileWriter(filePath)) {
                for (Equipment equipment : equipments) {
                    writer.write(equipment.toFileString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            ui.showMessage("Error saving equipment data: " + e.getMessage());
        }
    }

    /**
     * Loads the equipment list stored in the .txt file.
     * @return The list of equipment from the file. Returns an empty list if the file is not found.
     */
    public ArrayList<Equipment> load() {
        ArrayList<Equipment> equipments = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return equipments;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Equipment eq = parseEquipment(line);
                if (eq != null) {
                    equipments.add(eq);
                }
            }
        } catch (Exception e) {
            ui.showMessage("Error loading equipment data: " + e.getMessage());
        }
        return equipments;
    }

    /**
     * Converts a formatted string from the .txt file into an Equipment object.
     * @param line A single line of text from the save file.
     * @return An Equipment object, or null if the string format is corrupted.
     */
    private Equipment parseEquipment(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        try {
            String[] parts = line.split(" \\| ", -1);
            int totalParts = parts.length;

            // Peel from back: Modules(1), Life(2), Sem(3), Min(4), Loan(5), Avail(6), Qty(7)
            String modulesStr = parts[totalParts - 1].trim();
            String lifeStr = parts[totalParts - 2].trim();
            double life = lifeStr.isEmpty() ? 0.0 : Double.parseDouble(lifeStr);
            String semStr = parts[totalParts - 3].trim();
            AcademicSemester sem = semStr.isEmpty() ? null : new AcademicSemester(semStr);

            // Checkstyle fix: Separate declarations
            int min = Integer.parseInt(parts[totalParts - 4].trim());
            int l = Integer.parseInt(parts[totalParts - 5].trim());
            int a = Integer.parseInt(parts[totalParts - 6].trim());
            int q = Integer.parseInt(parts[totalParts - 7].trim());

            // Everything before the 7 metadata fields is the Name
            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 0; i <= (totalParts - 8); i++) {
                if (i > 0) {
                    nameBuilder.append(" | ");
                }
                nameBuilder.append(parts[i]);
            }
            String name = nameBuilder.toString().trim();

            ArrayList<String> modules = new ArrayList<>();
            if (!modulesStr.isEmpty()) {
                for (String m : modulesStr.split(",")) {
                    modules.add(m.trim());
                }
            }

            return new Equipment(name, q, a, l, sem, life, modules, min);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Saves the current system semester to the settings file.
     * @param currentSem The semester to be saved.
     */
    public void saveSettings(AcademicSemester currentSem) {
        Logger storageLogger = Logger.getLogger(Storage.class.getName());
        try {
            File file = new File(settingsPath);
            File directory = file.getParentFile();
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }

            try (FileWriter writer = new FileWriter(settingsPath)) {
                writer.write(currentSem.toString());
            }
            storageLogger.log(Level.INFO, "Successfully saved semester settings to file.");
        } catch (IOException e) {
            ui.showMessage("Error saving settings: " + e.getMessage());
            storageLogger.log(Level.SEVERE, "Failed to save settings: " + e.getMessage());
        }
    }

    /**
     * Loads the system semester from the settings file.
     * @return The saved semester as a String, or a default value if not found.
     */
    public String loadSettings() {
        File file = new File(settingsPath);
        if (!file.exists()) {
            return "AY2024/25 Sem1"; // Default value
        }

        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                return scanner.nextLine().trim();
            }
        } catch (FileNotFoundException e) {
            // Fallback to default
        }
        return "AY2024/25 Sem1";
    }
}
