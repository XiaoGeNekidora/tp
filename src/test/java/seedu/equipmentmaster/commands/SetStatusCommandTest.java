package seedu.equipmentmaster.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

public class SetStatusCommandTest {

    @Test
    public void execute_byName_loanQuantityPositive_updatesCorrectly() {
        // Arrange
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage("data/equipment.txt", ui);
        equipments.addEquipment(new Equipment("BasyS3 FPGA", 40, 40, 0));
        SetStatusCommand command = new SetStatusCommand("BasyS3 FPGA", 5, "loaned");

        // Act
        command.execute(equipments, ui, storage);

        // Assert
        Equipment eq = equipments.getEquipment(0);
        assertEquals(35, eq.getAvailable());
        assertEquals(5, eq.getLoaned());
    }

    @Test
    public void execute_byName_loanQuantityNegative_noChange() {
        // Arrange
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage("data/equipment.txt", ui);
        equipments.addEquipment(new Equipment("BasyS3 FPGA", 40, 40, 0));
        SetStatusCommand command = new SetStatusCommand("BasyS3 FPGA", -5, "loaned");

        // Act
        command.execute(equipments, ui, storage);

        // Assert
        Equipment eq = equipments.getEquipment(0);
        assertEquals(40, eq.getAvailable());
        assertEquals(0, eq.getLoaned());
    }

    @Test
    public void execute_byIndex_returnQuantityPositive_updatesCorrectly() {
        // Arrange
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage("data/equipment.txt", ui);
        equipments.addEquipment(new Equipment("BasyS3 FPGA", 40, 30, 10)); // 30 available, 10 loaned
        SetStatusCommand command = new SetStatusCommand(1, 3, "available");

        // Act
        command.execute(equipments, ui, storage);

        // Assert
        Equipment eq = equipments.getEquipment(0);
        assertEquals(33, eq.getAvailable()); // 30 + 3 = 33
        assertEquals(7, eq.getLoaned());     // 10 - 3 = 7
    }

    @Test
    public void execute_byIndex_returnQuantityNegative_noChange() {
        // Arrange
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage("data/equipment.txt", ui);
        equipments.addEquipment(new Equipment("BasyS3 FPGA", 40, 30, 10)); // 30 available, 10 loaned
        SetStatusCommand command = new SetStatusCommand(1, -3, "available");

        // Act
        command.execute(equipments, ui, storage);

        // Assert
        Equipment eq = equipments.getEquipment(0);
        assertEquals(30, eq.getAvailable()); // unchanged
        assertEquals(10, eq.getLoaned());     // unchanged
    }
}