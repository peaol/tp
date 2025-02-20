package tfifteenfour.clipboard.ui.attendancepage;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import tfifteenfour.clipboard.MainApp;
import tfifteenfour.clipboard.commons.core.LogsCenter;
import tfifteenfour.clipboard.model.student.StudentWithAttendance;
import tfifteenfour.clipboard.ui.UiPart;

/**
 * An UI component that displays the attendance information of a {@code Student}.
 */
public class PresentAttendanceListCard extends UiPart<Region> {

    private static final String FXML = "PresentAttendanceListCard.fxml";
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final StudentWithAttendance student;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label studentId;

    /**
     * Creates an PresentAttendanceListCard with the given StudentWithAttendance and index to display.
     */
    public PresentAttendanceListCard(StudentWithAttendance student, int displayedIndex) {
        super(FXML);
        this.student = student;
        id.setText(displayedIndex + ". ");
        name.setText(student.getName().fullName);
        studentId.setText(student.getStudentId().value);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PresentAttendanceListCard)) {
            return false;
        }

        // state check
        PresentAttendanceListCard card = (PresentAttendanceListCard) other;
        return id.getText().equals(card.id.getText())
                && student.equals(card.student);
    }
}
