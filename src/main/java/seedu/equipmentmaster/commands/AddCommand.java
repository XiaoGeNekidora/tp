package seedu.equipmentmaster.commands;


import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

public class AddCommand extends Command{
    private final String name;
    private final int quantity;

    public AddCommand(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public void execute(EquipmentList equipments, Ui ui, Storage storage) {
        Equipment equipment = new Equipment(name, quantity);
        equipments.addEquipment(equipment);
        storage.save(equipments.getAllEquipments());
        ui.showMessage("Added " + quantity + " of " + name + ". (Total Available: " + equipment.getAvailable() + ")" );
    }
}
