package seedu.equipmentmaster.commands;

import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.module.Module;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;
import seedu.equipmentmaster.commands.Command;

/**
 * Represents a command to add a new course module to the system.
 */
public class AddModCommand extends Command {
    private final String moduleName;
    private final int pax;

    /**
     * Constructs an {@code AddModCommand} with the extracted module name and pax.
     *
     * @param moduleName The name of the new module.
     * @param pax        The student enrollment number.
     * @throws EquipmentMasterException If the pax is a negative number.
     */
    public AddModCommand(String moduleName, int pax) throws EquipmentMasterException {
        if (pax < 0) {
            throw new EquipmentMasterException("Pax cannot be a negative number.");
        }
        this.moduleName = moduleName;
        this.pax = pax;
    }

    /**
     * Executes the add module command.
     * Adds the module to the provided ModuleList and displays a success message via the Ui.
     *
     * @param moduleList The list of modules to be updated.
     * @param ui         The user interface to display output.
     */
    @Override
    public void execute(EquipmentList equipments, ModuleList moduleList, Ui ui, Storage staorage) {
        Module newModule = new Module(moduleName, pax);
        moduleList.addModule(newModule);
        ui.showMessage("Successfully added module: " + newModule);
    }
}
