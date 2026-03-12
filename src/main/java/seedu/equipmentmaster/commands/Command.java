package seedu.equipmentmaster.commands;


import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

public abstract class Command {
    public abstract void execute(EquipmentList equipments, Ui ui, Storage storage)
            throws EquipmentMasterException;

    public boolean isExit() {
        return false;
    }
}
