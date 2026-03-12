package seedu.equipmentmaster.commands;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

public class AddCommandTest {

    @Test
    public void execute_validEquipment_addsToList() {
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = null;

        AddCommand command = new AddCommand("STM32", 5);

        command.execute(equipments, ui, storage);

        assertEquals(1, equipments.getSize());

        Equipment added = equipments.getEquipment(0);
        assertEquals("STM32", added.getName());
        assertEquals(5, added.getQuantity());
    }
}