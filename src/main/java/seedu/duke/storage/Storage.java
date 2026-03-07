package seedu.duke.storage;

import seedu.duke.ui.Ui;

public class Storage {
    private final String filepath;
    private final Ui ui = new Ui();

    public Storage(String filePath) {
        this.filepath = filePath;
    }
}
