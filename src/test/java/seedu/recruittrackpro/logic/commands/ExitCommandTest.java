package seedu.recruittrackpro.logic.commands;

import static seedu.recruittrackpro.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.recruittrackpro.logic.commands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;

import org.junit.jupiter.api.Test;

import seedu.recruittrackpro.model.Model;
import seedu.recruittrackpro.model.ModelManager;

public class ExitCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_exit_success() {
        CommandResult expectedCommandResult = new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, true);
        assertCommandSuccess(new ExitCommand(), model, expectedCommandResult, expectedModel);
    }
}
