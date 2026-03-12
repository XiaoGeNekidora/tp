package seedu.equipmentmaster.commands;

import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

public class ByeCommand extends Command {

    @Override
    public void execute(EquipmentList equipments, Ui ui, Storage storage) {
        ui.showGoodByeMessage();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
