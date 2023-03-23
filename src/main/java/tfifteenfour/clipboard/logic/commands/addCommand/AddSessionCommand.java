package tfifteenfour.clipboard.logic.commands.addCommand;

import tfifteenfour.clipboard.logic.CurrentSelected;
import tfifteenfour.clipboard.logic.commands.CommandResult;
import tfifteenfour.clipboard.logic.commands.exceptions.CommandException;
import tfifteenfour.clipboard.model.Model;

public class AddSessionCommand extends AddCommand {

	public CommandResult execute(Model model, CurrentSelected currentSelected) throws CommandException {
		return new CommandResult(this, "test", willModifyState);
	}

}
