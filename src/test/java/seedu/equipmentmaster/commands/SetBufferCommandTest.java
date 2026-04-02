package seedu.equipmentmaster.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;
import static seedu.equipmentmaster.common.Messages.MESSAGE_INVALID_SETBUFFER_FORMAT;

import java.util.ArrayList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SetBufferCommandTest {
    private static final String TEST_FILE_PATH = "test_equipment.txt";
    private static final String TEST_SETTING_FILE_PATH = "test_setting.txt";
    private static final String TEST_MODULE_FILE_PATH = "test_module.txt";

    @Test
    public void execute_validBuffer_setsBuffer() throws EquipmentMasterException {
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();

        AddCommand addCommand = new AddCommand("STM32", 10);
        AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
        Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);
        addCommand.execute(context);

        SetBufferCommand command = new SetBufferCommand("STM32", 15.0);
        command.execute(context);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(15.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void execute_equipmentNotFound_showsErrorMessage() {
        EquipmentList equipments = new EquipmentList();
        ModuleList moduleList = new ModuleList();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(System.in, new PrintStream(outputStream));
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);

        SetBufferCommand command = new SetBufferCommand("NonExistent", 10.0);
        try {
            AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
            Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);
            command.execute(context);

            String output = outputStream.toString();
            assertTrue(output.contains("Equipment 'NonExistent' not found."));
        } catch (EquipmentMasterException e) {
            ui.showMessage(e.getMessage());
        }
    }

    @Test
    public void parse_inputWithPercentSymbol_createsCommand() throws EquipmentMasterException {
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();

        AddCommand addCommand = new AddCommand("STM32", 10);
        AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
        Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);
        addCommand.execute(context);

        SetBufferCommand command = SetBufferCommand.parse("setbuffer n/STM32 b/10%");
        command.execute(context);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(10.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void parseEquipment_withBuffer_setsCorrectBuffer() throws EquipmentMasterException {
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();

        AddCommand addCommand = new AddCommand("STM32", 10);
        AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
        Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);
        addCommand.execute(context);

        SetBufferCommand command = new SetBufferCommand("STM32", 15.5);
        command.execute(context);

        storage.save(equipments.getAllEquipments());
        ArrayList<Equipment> loadedList = storage.load();

        assertEquals(1, loadedList.size());
        Equipment loadedEquipment = loadedList.get(0);
        assertEquals(15.5, loadedEquipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void parseEquipment_withoutBuffer_setsDefaultBuffer() throws EquipmentMasterException {
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();

        AddCommand addCommand = new AddCommand("STM32", 10);
        AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
        Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);
        addCommand.execute(context);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(0.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void execute_validIndex_setsBuffer() throws EquipmentMasterException {
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();

        // Add multiple equipment to test index
        AddCommand addCommand1 = new AddCommand("STM32", 10);
        AddCommand addCommand2 = new AddCommand("Arduino", 5);
        AddCommand addCommand3 = new AddCommand("RaspberryPi", 3);

        AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
        Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);
        addCommand1.execute(context);
        addCommand2.execute(context);
        addCommand3.execute(context);

        // Set buffer for equipment at index 2 (Arduino)
        SetBufferCommand command = new SetBufferCommand(2, 20.0);
        command.execute(context);

        Equipment equipment = equipments.getEquipment(1); // index 1 is 0-based for Arduino
        assertEquals(20.0, equipment.getBufferPercentage(), 0.0001);
        assertEquals("Arduino", equipment.getName());
    }

    @Test
    public void execute_invalidIndex_showsErrorMessage() {
        EquipmentList equipments = new EquipmentList();
        ModuleList moduleList = new ModuleList();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(System.in, new PrintStream(outputStream));
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);

        // Add one equipment
        AddCommand addCommand = new AddCommand("STM32", 10);
        try {
            AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
            Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);
            addCommand.execute(context);

            // Try to set buffer for index 99 (out of bounds)
            SetBufferCommand command = new SetBufferCommand(99, 10.0);
            command.execute(context);

            String output = outputStream.toString();
            assertTrue(output.contains("Equipment at index 99 not found. (Total: 1 equipment(s))"));
        } catch (EquipmentMasterException e) {
            ui.showMessage(e.getMessage());
        }
    }

    @Test
    public void parse_inputWithIndex_createsCommand() throws EquipmentMasterException {
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();

        // Add equipment
        AddCommand addCommand = new AddCommand("STM32", 10);
        AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
        Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);
        addCommand.execute(context);

        // Parse command with index format
        SetBufferCommand command = SetBufferCommand.parse("setbuffer i/1 b/25");
        command.execute(context);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(25.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void parse_bothNameAndIndexFlags_throwsException() {
        try {
            SetBufferCommand.parse("setbuffer n/STM32 i/1 b/10");
            // Should not reach here
            assertTrue(false, "Expected EquipmentMasterException was not thrown");
        } catch (EquipmentMasterException e) {
            assertTrue(e.getMessage().contains("Please specify either name (n/) OR index (i/), not both"));
        }
    }

    @Test
    public void parse_negativeBufferPercentage_throwsException() {
        try {
            SetBufferCommand.parse("setbuffer n/STM32 b/-10");
            assertTrue(false, "Expected EquipmentMasterException was not thrown");
        } catch (EquipmentMasterException e) {
            assertEquals("Buffer percentage cannot be negative.", e.getMessage());
        }
    }

    @Test
    public void parse_missingBFlag_throwsException() {
        try {
            SetBufferCommand.parse("setbuffer n/STM32");
            assertTrue(false, "Expected EquipmentMasterException was not thrown");
        } catch (EquipmentMasterException e) {
            assertEquals(MESSAGE_INVALID_SETBUFFER_FORMAT, e.getMessage());
        }
    }

    @Test
    public void parse_missingNameAndIndexFlags_throwsException() {
        try {
            SetBufferCommand.parse("setbuffer b/10");
            assertTrue(false, "Expected EquipmentMasterException was not thrown");
        } catch (EquipmentMasterException e) {
            assertEquals(MESSAGE_INVALID_SETBUFFER_FORMAT, e.getMessage());
        }
    }

    @Test
    public void parse_emptyName_throwsException() {
        try {
            SetBufferCommand.parse("setbuffer n/ b/10");
            assertTrue(false, "Expected EquipmentMasterException was not thrown");
        } catch (EquipmentMasterException e) {
            assertEquals("Equipment name cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void parse_emptyIndex_throwsException() {
        try {
            SetBufferCommand.parse("setbuffer i/ b/10");
            assertTrue(false, "Expected EquipmentMasterException was not thrown");
        } catch (EquipmentMasterException e) {
            assertEquals("Equipment index cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void parse_zeroIndex_throwsException() {
        try {
            SetBufferCommand.parse("setbuffer i/0 b/10");
            assertTrue(false, "Expected EquipmentMasterException was not thrown");
        } catch (EquipmentMasterException e) {
            assertEquals("Index must be a positive number.", e.getMessage());
        }
    }

    @Test
    public void parse_nonNumericIndex_throwsException() {
        try {
            SetBufferCommand.parse("setbuffer i/abc b/10");
            assertTrue(false, "Expected EquipmentMasterException was not thrown");
        } catch (EquipmentMasterException e) {
            assertEquals("Please enter a valid positive integer for index.", e.getMessage());
        }
    }

    @Test
    public void parse_nonNumericBuffer_throwsException() {
        try {
            SetBufferCommand.parse("setbuffer n/STM32 b/abc");
            assertTrue(false, "Expected EquipmentMasterException was not thrown");
        } catch (EquipmentMasterException e) {
            assertEquals("Please enter a valid number for buffer percentage.", e.getMessage());
        }
    }

    @Test
    public void parse_emptyBufferValue_throwsException() {
        try {
            SetBufferCommand.parse("setbuffer n/STM32 b/");
            assertTrue(false, "Expected EquipmentMasterException was not thrown");
        } catch (EquipmentMasterException e) {
            assertEquals(MESSAGE_INVALID_SETBUFFER_FORMAT, e.getMessage());
        }
    }

    @Test
    public void parse_bufferValueAtEnd_extractsCorrectly() throws EquipmentMasterException {
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();

        AddCommand addCommand = new AddCommand("TestDevice", 10);
        AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
        Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);
        addCommand.execute(context);

        SetBufferCommand command = SetBufferCommand.parse("setbuffer n/TestDevice b/30");
        command.execute(context);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(30.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void constructor_negativeIndex_throwsIllegalArgumentException() {
        try {
            SetBufferCommand command = new SetBufferCommand(0, 10.0);
            assertTrue(false, "Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Index must be positive"));
        }
    }

    @Test
    public void constructor_negativePercentage_throwsIllegalArgumentException() {
        try {
            SetBufferCommand command = new SetBufferCommand(1, -5.0);
            assertTrue(false, "Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Buffer percentage cannot be negative.", e.getMessage());
        }
    }
}
