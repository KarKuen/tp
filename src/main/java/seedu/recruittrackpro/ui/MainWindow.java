package seedu.recruittrackpro.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.recruittrackpro.commons.core.GuiSettings;
import seedu.recruittrackpro.commons.core.LogsCenter;
import seedu.recruittrackpro.logic.Logic;
import seedu.recruittrackpro.logic.commands.CommandResult;
import seedu.recruittrackpro.logic.commands.SwitchSortCommand;
import seedu.recruittrackpro.logic.commands.exceptions.CommandException;
import seedu.recruittrackpro.logic.parser.exceptions.ParseException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";
    private static final String LIGHT_THEME = "/view/LightTheme.css";
    private static final String DARK_THEME = "/view/DarkTheme.css";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;
    private boolean isLightMode = false;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private ResultDisplay resultDisplay;
    private UserGuideWindow userGuideWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem userGuideMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusBarPlaceholder;

    @FXML
    private Button sortButton;

    @FXML
    private Button themeToggle;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        userGuideWindow = new UserGuideWindow();

        toggleTheme(); // Sets initial theme to dark
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @FXML
    private void initialize() {
        themeToggle.setOnAction(e -> toggleTheme());
    }

    private void toggleTheme() {
        Scene scene = getRoot().getScene();
        ObservableList<String> stylesheets = scene.getStylesheets();
        String newTheme;

        stylesheets.removeIf(sheet ->
                sheet.contains("DarkTheme.css") || sheet.contains("LightTheme.css")
        );

        if (isLightMode) {
            newTheme = getClass().getResource(LIGHT_THEME).toExternalForm();
            themeToggle.setText(new String(Character.toChars(0x2600)));
        } else {
            newTheme = getClass().getResource(DARK_THEME).toExternalForm();
            themeToggle.setText(new String(Character.toChars(0x263D)));
        }

        stylesheets.add(newTheme);
        userGuideWindow.updateTheme(newTheme);

        isLightMode = !isLightMode;
    }

    private void setAccelerators() {
        setAccelerator(userGuideMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * user guide window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getRecruitTrackProFilePath());
        statusBarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the user guide window or focuses on it if it's already opened.
     */
    @FXML
    public void handleUserGuide() {
        if (!userGuideWindow.isShowing()) {
            userGuideWindow.show();
        } else {
            userGuideWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        userGuideWindow.hide();
        primaryStage.hide();
    }

    @FXML
    private void handleSortSwitch() throws CommandException, ParseException {
        executeCommand(SwitchSortCommand.COMMAND_WORD);
    }

    private void updateSortButton() {
        sortButton.setText(logic.isAscending() ? "A → Z" : "Z → A");
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.recruittrackpro.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isExit()) {
                handleExit();
            }

            updateSortButton();
            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
