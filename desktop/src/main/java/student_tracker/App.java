package student_tracker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ExecutorService ioPool = Executors.newCachedThreadPool();
    private final BackendApi api = new BackendApi(mapper);
    private final Session session = new Session();
    private Stage stage;
    private Scene scene;

    private final BorderPane shell = new BorderPane();
    private final VBox contentRoot = new VBox(16);
    private final Label userNameLabel = new Label();
    private final Label userUsernameLabel = new Label();

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        scene = new Scene(buildLoginView(), 1180, 760);
        scene.getStylesheets().add(getClass().getResource("/css/desktop.css").toExternalForm());
        stage.setTitle("Student Tracker Desktop");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        ioPool.shutdownNow();
    }

    private VBox buildAuthCard(String title, String subtitle) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("auth-title");
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("auth-subtitle");
        VBox card = new VBox(14, titleLabel, subtitleLabel);
        card.getStyleClass().add("auth-card");
        card.setMaxWidth(440);
        card.setPadding(new Insets(28));
        return card;
    }

    private BorderPane wrapAuth(VBox card) {
        BorderPane root = new BorderPane(card);
        root.getStyleClass().add("auth-root");
        BorderPane.setAlignment(card, Pos.CENTER);
        return root;
    }

    private BorderPane buildLoginView() {
        VBox card = buildAuthCard("Добро пожаловать обратно", "Войдите, чтобы управлять материалами группы");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Имя пользователя");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");

        Button loginButton = new Button("Войти");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(e -> doLogin(usernameField.getText(), passwordField.getText(), loginButton));

        Button signupLink = new Button("Нет аккаунта? Зарегистрироваться");
        signupLink.getStyleClass().add("ghost-link");
        signupLink.setOnAction(e -> scene.setRoot(buildSignupView()));

        Button forgotLink = new Button("Забыли пароль?");
        forgotLink.getStyleClass().add("ghost-link");
        forgotLink.setOnAction(e -> scene.setRoot(buildForgotPasswordView()));

        card.getChildren().addAll(usernameField, passwordField, loginButton, forgotLink, signupLink);
        return wrapAuth(card);
    }

    private BorderPane buildSignupView() {
        VBox card = buildAuthCard("Создать аккаунт", "Присоединяйтесь к Student Tracker");
        TextField nameField = new TextField();
        nameField.setPromptText("Полное имя");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Имя пользователя");
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Студент", "Староста");
        roleBox.setPromptText("Выберите роль");
        roleBox.setMaxWidth(Double.MAX_VALUE);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");

        Button signupButton = new Button("Зарегистрироваться");
        signupButton.getStyleClass().add("primary-button");
        signupButton.setMaxWidth(Double.MAX_VALUE);
        signupButton.setOnAction(e -> doSignup(nameField.getText(), usernameField.getText(), roleBox.getValue(), passwordField.getText(), signupButton));

        Button loginLink = new Button("Уже есть аккаунт? Войти");
        loginLink.getStyleClass().add("ghost-link");
        loginLink.setOnAction(e -> scene.setRoot(buildLoginView()));
        card.getChildren().addAll(nameField, usernameField, roleBox, passwordField, signupButton, loginLink);
        return wrapAuth(card);
    }

    private BorderPane buildForgotPasswordView() {
        VBox card = buildAuthCard("Восстановление пароля", "Введите email для получения ссылки");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        Button sendButton = new Button("Отправить ссылку");
        sendButton.getStyleClass().add("primary-button");
        sendButton.setMaxWidth(Double.MAX_VALUE);
        sendButton.setOnAction(e -> doForgotPassword(emailField.getText(), sendButton));

        Button backLink = new Button("Назад ко входу");
        backLink.getStyleClass().add("ghost-link");
        backLink.setOnAction(e -> scene.setRoot(buildLoginView()));
        card.getChildren().addAll(emailField, sendButton, backLink);
        return wrapAuth(card);
    }

    private void doLogin(String username, String password, Button actionButton) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            error("Заполните логин и пароль.");
            return;
        }
        actionButton.setDisable(true);
        runAsync(() -> {
            LoginResponse login = api.login(username.trim(), password);
            session.token = login.token;
            session.user = api.getProfile(session.token);
            session.hasGroup = api.hasGroup(session.token);
            return null;
        }).whenComplete((ok, ex) -> Platform.runLater(() -> {
            actionButton.setDisable(false);
            if (ex != null) {
                error("Не удалось войти: " + rootMessage(ex));
                return;
            }
            showMainLayout();
            routeAfterLogin();
        }));
    }

    private void doSignup(String name, String username, String roleLabel, String password, Button actionButton) {
        if (name.isBlank() || username.isBlank() || roleLabel == null || roleLabel.isBlank() || password.isBlank()) {
            error("Заполните все поля.");
            return;
        }
        int role = "Староста".equals(roleLabel) ? 1 : 0;
        if (password.length() < 6) {
            error("Пароль должен содержать минимум 6 символов.");
            return;
        }
        actionButton.setDisable(true);
        runAsync(() -> api.signup(name.trim(), username.trim(), role, password))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                actionButton.setDisable(false);
                if (ex != null) {
                    error("Ошибка регистрации: " + rootMessage(ex));
                    return;
                }
                info("Регистрация успешна. Выполните вход.");
                scene.setRoot(buildLoginView());
            }));
    }

    private void doForgotPassword(String email, Button actionButton) {
        if (email.isBlank()) {
            error("Введите email.");
            return;
        }
        actionButton.setDisable(true);
        runAsync(() -> api.forgotPassword(email.trim()))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                actionButton.setDisable(false);
                if (ex != null) {
                    error("Не удалось отправить письмо: " + rootMessage(ex));
                    return;
                }
                info("Ссылка для сброса отправлена.");
            }));
    }

    private void showMainLayout() {
        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPadding(new Insets(16));
        sidebar.setPrefWidth(260);

        Label sidebarTitle = new Label("Список групп");
        sidebarTitle.getStyleClass().add("sidebar-title");
        userNameLabel.getStyleClass().add("sidebar-user");
        userUsernameLabel.getStyleClass().add("sidebar-user-secondary");

        Button profileBtn = navButton("Профиль", this::loadProfilePage);
        Button subjectsBtn = navButton("Предметы", this::loadSubjectsPage);
        Button groupsBtn = navButton("Группа", this::loadGroupPage);
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button logoutBtn = new Button("Выход");
        logoutBtn.getStyleClass().add("logout-button");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> {
            session.token = null;
            session.user = null;
            scene.setRoot(buildLoginView());
        });

        sidebar.getChildren().addAll(sidebarTitle, profileBtn, subjectsBtn, groupsBtn, spacer, userNameLabel, userUsernameLabel, logoutBtn);

        contentRoot.getStyleClass().add("content-root");
        ScrollPane scroll = new ScrollPane(contentRoot);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("content-scroll");

        Label appTitle = new Label("Student Tracker");
        appTitle.getStyleClass().add("app-title");
        HBox topBar = new HBox(appTitle);
        topBar.getStyleClass().add("top-bar");
        topBar.setPadding(new Insets(14, 20, 14, 20));

        BorderPane center = new BorderPane(scroll);
        center.setTop(topBar);
        shell.setLeft(sidebar);
        shell.setCenter(center);
        shell.getStyleClass().add("shell-root");
        scene.setRoot(shell);
        refreshSidebarUser();
    }

    private Button navButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> action.run());
        return button;
    }

    private void refreshSidebarUser() {
        if (session.user == null) {
            return;
        }
        userNameLabel.setText(session.user.name);
        userUsernameLabel.setText("@" + session.user.username);
    }

    private void routeAfterLogin() {
        if (isHeadman() && !hasGroup()) {
            loadGroupPage(true);
            return;
        }
        loadProfilePage();
    }

    private void loadProfilePage() {
        contentRoot.getChildren().clear();
        Label title = pageTitle("Профиль аккаунта");
        VBox card = card();
        Label name = new Label(session.user.name);
        name.getStyleClass().add("profile-name");
        Label username = new Label("@" + session.user.username);
        Label email = new Label(session.user.email == null ? "Email не указан" : session.user.email);
        Label role = new Label("Роль: " + (isHeadman() ? "Староста" : "Студент"));

        Button editNameBtn = new Button("Редактировать профиль");
        Button changePasswordBtn = new Button("Сменить пароль");
        Button createGroupBtn = new Button("Создать группу");
        editNameBtn.getStyleClass().add("secondary-button");
        changePasswordBtn.getStyleClass().add("secondary-button");
        createGroupBtn.getStyleClass().add("secondary-button");
        editNameBtn.setOnAction(e -> showEditProfileDialog());
        changePasswordBtn.setOnAction(e -> showChangePasswordDialog());
        createGroupBtn.setOnAction(e -> showCreateGroupDialog());
        createGroupBtn.setDisable(!isHeadman());

        VBox actions = new VBox(8, editNameBtn, changePasswordBtn, createGroupBtn);
        HBox row = new HBox(20, new VBox(6, name, username, email, role), actions);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(row.getChildren().get(0), Priority.ALWAYS);
        card.getChildren().add(row);

        VBox emailSection = card();
        TextField newEmailField = new TextField();
        newEmailField.setPromptText("Введите новый email");
        Button emailBtn = new Button("Запросить смену email");
        emailBtn.getStyleClass().add("primary-button");
        emailBtn.setOnAction(e -> requestEmailChange(newEmailField.getText(), emailBtn));
        emailSection.getChildren().addAll(new Label("Сменить email"), newEmailField, emailBtn);

        VBox danger = card();
        danger.getStyleClass().add("danger-card");
        Label dangerTitle = new Label("Опасная зона");
        dangerTitle.getStyleClass().add("danger-title");
        Button deleteBtn = new Button("Удалить аккаунт");
        deleteBtn.getStyleClass().add("danger-button");
        deleteBtn.setOnAction(e -> deleteAccount());
        danger.getChildren().addAll(dangerTitle, new Label("После удаления аккаунта восстановление невозможно."), deleteBtn);

        contentRoot.getChildren().add(title);
        if (!hasGroup()) {
            contentRoot.getChildren().add(noGroupBanner());
        }
        contentRoot.getChildren().addAll(card, emailSection, danger);
    }

    private void loadSubjectsPage() {
        contentRoot.getChildren().clear();
        contentRoot.getChildren().add(pageTitle("Предметы"));
        if (!hasGroup()) {
            contentRoot.getChildren().add(noGroupBanner());
            return;
        }
        
        FlowPane grid = new FlowPane();
        grid.setStyle("-fx-hgap: 20; -fx-vgap: 20;");
        grid.setPrefWrapLength(1000);
        grid.setAlignment(Pos.TOP_LEFT);
        
        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(420);
        scroll.getStyleClass().add("content-scroll");
        
        Button createBtn = new Button("Создать предмет");
        createBtn.getStyleClass().add("secondary-button");
        createBtn.setDisable(!isHeadman());
        createBtn.setOnAction(e -> showCreateSubjectDialog());
        
        HBox actions = new HBox(10, createBtn);
        
        contentRoot.getChildren().addAll(scroll, actions);
        
        runAsync(() -> api.getSubjects(session.token))
            .whenComplete((subjects, ex) -> Platform.runLater(() -> {
                if (ex != null) {
                    error("Не удалось загрузить предметы: " + rootMessage(ex));
                } else {
                    grid.getChildren().clear();
                    for (Subject subject : subjects) {
                        grid.getChildren().add(createSubjectCard(subject));
                    }
                }
            }));
    }
    
    private StackPane createSubjectCard(Subject subject) {
        StackPane card = new StackPane();
        card.setPrefSize(200, 150);
        card.getStyleClass().add("subject-card");

        VBox placeholder = new VBox(8);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.getStyleClass().add("subject-placeholder");
        placeholder.setPrefSize(200, 150);

        Label icon = new Label("📘");
        icon.getStyleClass().add("subject-placeholder-icon");
        Label titleLabel = new Label(subject.title);
        titleLabel.getStyleClass().add("subject-card-title");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(170);
        titleLabel.setAlignment(Pos.CENTER);

        placeholder.getChildren().addAll(icon, titleLabel);
        card.getChildren().add(placeholder);

        if (isHeadman()) {
            Button deleteBtn = new Button("Удалить");
            deleteBtn.getStyleClass().add("danger-button");
            deleteBtn.setStyle("-fx-opacity: 0;");
            deleteBtn.setPrefSize(70, 24);
            deleteBtn.setOnAction(e -> {
                e.consume();
                deleteSubject(subject);
            });
            StackPane.setAlignment(deleteBtn, Pos.TOP_RIGHT);
            StackPane.setMargin(deleteBtn, new Insets(6));
            card.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-opacity: 1;"));
            card.setOnMouseExited(e -> deleteBtn.setStyle("-fx-opacity: 0;"));
            card.getChildren().add(deleteBtn);
        }

        card.setOnMouseClicked(e -> loadSubjectMaterialsPage(subject));
        return card;
    }

    private void loadSubjectMaterialsPage(Subject subject) {
        contentRoot.getChildren().clear();
        contentRoot.getChildren().add(pageTitle("Материалы: " + subject.title));
        if (!hasGroup()) {
            contentRoot.getChildren().add(noGroupBanner());
            return;
        }
        javafx.scene.control.Accordion sections = new javafx.scene.control.Accordion();
        sections.setPrefHeight(460);

        Button backBtn = new Button("Назад к предметам");
        backBtn.getStyleClass().add("secondary-button");
        backBtn.setOnAction(e -> loadSubjectsPage());
        Button addBtn = new Button("Добавить материал");
        addBtn.getStyleClass().add("secondary-button");
        addBtn.setDisable(!isHeadman());
        addBtn.setOnAction(e -> showCreateMaterialDialog(subject));
        contentRoot.getChildren().addAll(sections, new HBox(10, backBtn, addBtn));

        runAsync(() -> api.getMaterials(session.token, subject.id))
            .whenComplete((materials, ex) -> Platform.runLater(() -> {
                if (ex != null) {
                    error("Не удалось загрузить материалы: " + rootMessage(ex));
                } else {
                    sections.getPanes().clear();
                    for (Material material : materials) {
                        VBox body = new VBox(10);
                        body.getStyleClass().add("content-card");
                        Label description = new Label(
                            material.description == null || material.description.isBlank()
                                ? "Описание отсутствует"
                                : material.description
                        );
                        description.setWrapText(true);
                        Label file = new Label("Файл: " + (material.originalFileName == null ? "Без файла" : material.originalFileName));

                        HBox actions = new HBox(8);
                        Button downloadBtn = new Button("Скачать");
                        downloadBtn.getStyleClass().add("primary-button");
                        downloadBtn.setOnAction(e -> downloadMaterial(material));
                        actions.getChildren().add(downloadBtn);

                        if (isHeadman()) {
                            Button editBtn = new Button("Изменить");
                            editBtn.getStyleClass().add("secondary-button");
                            editBtn.setOnAction(e -> showEditMaterialDialog(subject, material));
                            Button deleteBtn = new Button("Удалить");
                            deleteBtn.getStyleClass().add("danger-button");
                            deleteBtn.setOnAction(e -> deleteMaterial(subject, material));
                            actions.getChildren().addAll(editBtn, deleteBtn);
                        }

                        body.getChildren().addAll(description, file, actions);
                        javafx.scene.control.TitledPane pane = new javafx.scene.control.TitledPane(material.title, body);
                        pane.getStyleClass().add("material-section");
                        sections.getPanes().add(pane);
                    }
                    if (!sections.getPanes().isEmpty()) {
                        sections.setExpandedPane(sections.getPanes().get(0));
                    }
                }
            }));
    }

    private void loadGroupPage() {
        loadGroupPage(false);
    }

    private void loadGroupPage(boolean promptCreateIfNoGroup) {
        contentRoot.getChildren().clear();
        contentRoot.getChildren().add(pageTitle("Группа"));
        Label groupTitle = new Label("Загрузка...");
        ListView<GroupMember> members = new ListView<>();
        members.setPrefHeight(430);
        members.setCellFactory(v -> new GroupMemberCell());
        Button addBtn = new Button("Добавить участника");
        addBtn.getStyleClass().add("secondary-button");
        addBtn.setDisable(!isHeadman());
        addBtn.setOnAction(e -> showAddMemberDialog());
        Button removeBtn = new Button("Удалить участника");
        removeBtn.getStyleClass().add("danger-button");
        removeBtn.setDisable(!isHeadman());
        removeBtn.setOnAction(e -> {
            GroupMember member = members.getSelectionModel().getSelectedItem();
            if (member != null) {
                removeMember(member.id);
            }
        });
        Button deleteGroupBtn = new Button("Удалить группу");
        deleteGroupBtn.getStyleClass().add("danger-button");
        deleteGroupBtn.setDisable(!isHeadman());
        deleteGroupBtn.setOnAction(e -> deleteGroup());
        contentRoot.getChildren().addAll(groupTitle, members, new HBox(10, addBtn, removeBtn, deleteGroupBtn));
        if (!hasGroup()) {
            groupTitle.setText("Вы не состоите в группе");
            contentRoot.getChildren().add(noGroupBanner());
            if (isHeadman()) {
                Button createGroupBtn = new Button("Создать группу");
                createGroupBtn.getStyleClass().add("primary-button");
                createGroupBtn.setOnAction(e -> showCreateGroupDialog());
                contentRoot.getChildren().add(createGroupBtn);
                if (promptCreateIfNoGroup) {
                    showCreateGroupDialog();
                }
            }
            return;
        }
        runAsync(() -> api.getMyGroup(session.token))
            .whenComplete((group, ex) -> Platform.runLater(() -> {
                if (ex != null) {
                    updateGroupState(false);
                    groupTitle.setText("Вы не состоите в группе");
                    members.getItems().clear();
                    contentRoot.getChildren().add(noGroupBanner());
                } else {
                    updateGroupState(true);
                    groupTitle.setText("Ваша группа: " + group.name);
                    members.getItems().setAll(group.members);
                }
            }));
    }

    private Label pageTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("page-title");
        return label;
    }

    private VBox card() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(14));
        box.getStyleClass().add("content-card");
        return box;
    }

    private VBox noGroupBanner() {
        VBox banner = new VBox(8);
        banner.setPadding(new Insets(14));
        banner.getStyleClass().add("content-card");
        Label title = new Label("Вы пока не состоите в группе");
        title.getStyleClass().add("danger-title");
        Label text = new Label(isHeadman()
            ? "Создайте группу и добавьте участников, чтобы открыть предметы и материалы."
            : "Попросите старосту добавить вас в группу. До этого разделы будут недоступны.");
        banner.getChildren().addAll(title, text);
        return banner;
    }

    private boolean isHeadman() {
        return session.user != null && "HEADMAN".equals(session.user.studentType);
    }

    private boolean hasGroup() {
        return session.hasGroup;
    }

    private void refreshGroupStateFromProfile() {
        updateGroupState(session.user != null && session.user.groupId != null);
    }

    private void updateGroupState(boolean hasGroup) {
        session.hasGroup = hasGroup;
    }

    private void showEditProfileDialog() {
        TextInputDialogEx dialog = new TextInputDialogEx("Новое имя", session.user.name);
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> runAsync(() -> {
            api.updateProfile(session.token, newName);
            session.user = api.getProfile(session.token);
            return null;
        }).whenComplete((ok, ex) -> Platform.runLater(() -> {
            if (ex != null) error("Не удалось обновить профиль: " + rootMessage(ex));
            else {
                refreshSidebarUser();
                loadProfilePage();
            }
        })));
    }

    private void showChangePasswordDialog() {
        PasswordDialog dialog = new PasswordDialog();
        Optional<PasswordChange> result = dialog.showAndWait();
        result.ifPresent(data -> runAsync(() -> api.changePassword(session.token, data.currentPassword(), data.newPassword()))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) error("Не удалось изменить пароль: " + rootMessage(ex));
                else info("Пароль обновлён.");
            })));
    }

    private void showCreateGroupDialog() {
        TextInputDialogEx dialog = new TextInputDialogEx("Название группы", "");
        dialog.showAndWait().ifPresent(name -> runAsync(() -> {
                api.createGroup(session.token, name);
                session.user = api.getProfile(session.token);
                session.hasGroup = api.hasGroup(session.token);
            })
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) {
                    String message = rootMessage(ex);
                    if (message.contains("HEADMAN already has a group")) {
                        session.hasGroup = true;
                        info("У вас уже есть группа. Открываю страницу группы.");
                        loadGroupPage();
                        return;
                    }
                    error("Не удалось создать группу: " + message);
                }
                else {
                    info("Группа создана.");
                    loadGroupPage();
                }
            })));
    }

    private void requestEmailChange(String email, Button button) {
        if (email.isBlank()) return;
        button.setDisable(true);
        runAsync(() -> api.requestEmailChange(session.token, email))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                button.setDisable(false);
                if (ex != null) error("Не удалось запросить смену email: " + rootMessage(ex));
                else info("Ссылка подтверждения отправлена.");
            }));
    }

    private void deleteAccount() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Удалить аккаунт?", ButtonType.OK, ButtonType.CANCEL);
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;
        runAsync(() -> api.deleteAccount(session.token, ""))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) error("Не удалось удалить аккаунт: " + rootMessage(ex));
                else {
                    info("Аккаунт удалён.");
                    scene.setRoot(buildLoginView());
                }
            }));
    }

    private void showCreateSubjectDialog() {
        SubjectDialog dialog = new SubjectDialog(stage, false, null);
        dialog.showAndWait().ifPresent(data -> {
            if (data.photoPath() == null || data.title().isBlank() || data.description().isBlank()) {
                error("Пожалуйста, заполните все поля и выберите файл.");
                return;
            }
            try {
                long fileSize = Files.size(data.photoPath());
                if (fileSize > (1 * 1024 * 1024)) {
                    error("Размер файла не должен превышать 1 МБ.");
                    return;
                }
                String fileName = data.photoPath().getFileName().toString().toLowerCase();
                if (!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png"))) {
                    error("Недопустимый формат файла. Разрешены JPEG и PNG.");
                    return;
                }
            } catch (IOException ex) {
                error("Не удалось прочитать выбранный файл.");
                return;
            }
            runAsync(() -> api.createSubject(session.token, data))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) error("Не удалось создать предмет: " + rootMessage(ex));
                else loadSubjectsPage();
            }));
        });
    }

    private void deleteSubject(Subject subject) {
        runAsync(() -> api.deleteSubject(session.token, subject.id))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) error("Не удалось удалить предмет: " + rootMessage(ex));
                else loadSubjectsPage();
            }));
    }

    private void showCreateMaterialDialog(Subject subject) {
        MaterialDialog dialog = new MaterialDialog(stage, false, null);
        dialog.showAndWait().ifPresent(data -> {
            if (data.filePath() == null || data.title().isBlank() || data.description().isBlank()) {
                error("Пожалуйста, заполните все поля и выберите файл.");
                return;
            }
            try {
                long fileSize = Files.size(data.filePath());
                if (fileSize > (10 * 1024 * 1024)) {
                    error("Размер файла не должен превышать 10 МБ.");
                    return;
                }
            } catch (IOException ex) {
                error("Не удалось прочитать выбранный файл.");
                return;
            }
            runAsync(() -> api.createMaterial(session.token, subject.id, data))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) error("Не удалось создать материал: " + rootMessage(ex));
                else loadSubjectMaterialsPage(subject);
            }));
        });
    }

    private void showEditMaterialDialog(Subject subject, Material material) {
        MaterialDialog dialog = new MaterialDialog(stage, true, material);
        dialog.showAndWait().ifPresent(data -> {
            if (data.title().isBlank() || data.description().isBlank()) {
                error("Пожалуйста, заполните название и описание.");
                return;
            }
            if (data.filePath() != null) {
                try {
                    long fileSize = Files.size(data.filePath());
                    if (fileSize > (1 * 1024 * 1024)) {
                        error("Размер файла не должен превышать 1 МБ.");
                        return;
                    }
                } catch (IOException ex) {
                    error("Не удалось прочитать выбранный файл.");
                    return;
                }
            }
            runAsync(() -> api.updateMaterial(session.token, material.id, subject.id, data))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) error("Не удалось изменить материал: " + rootMessage(ex));
                else loadSubjectMaterialsPage(subject);
            }));
        });
    }

    private void deleteMaterial(Subject subject, Material material) {
        runAsync(() -> api.deleteMaterial(session.token, material.id))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) error("Не удалось удалить материал: " + rootMessage(ex));
                else loadSubjectMaterialsPage(subject);
            }));
    }

    private void downloadMaterial(Material material) {
        if (material == null) return;
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName(material.originalFileName == null ? "material.bin" : material.originalFileName);
        Path target = Optional.ofNullable(chooser.showSaveDialog(stage)).map(java.io.File::toPath).orElse(null);
        if (target == null) return;
        runAsync(() -> api.downloadMaterial(session.token, material.filePath, target))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) error("Ошибка скачивания: " + rootMessage(ex));
                else info("Файл сохранён: " + target);
            }));
    }

    private void showAddMemberDialog() {
        TextInputDialogEx dialog = new TextInputDialogEx("Имя пользователя", "");
        dialog.showAndWait().ifPresent(username -> runAsync(() -> api.addUserToGroup(session.token, username))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) error("Не удалось добавить пользователя: " + rootMessage(ex));
                else loadGroupPage();
            })));
    }

    private void removeMember(long id) {
        runAsync(() -> api.deleteUserFromGroup(session.token, id))
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) error("Не удалось удалить пользователя: " + rootMessage(ex));
                else loadGroupPage();
            }));
    }

    private void deleteGroup() {
        runAsync(() -> {
                api.deleteGroup(session.token);
                session.user = api.getProfile(session.token);
                session.hasGroup = api.hasGroup(session.token);
            })
            .whenComplete((ok, ex) -> Platform.runLater(() -> {
                if (ex != null) error("Не удалось удалить группу: " + rootMessage(ex));
                else loadGroupPage();
            }));
    }

    private void info(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void error(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        DialogPane pane = alert.getDialogPane();
        pane.setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private <T> CompletableFuture<T> runAsync(CheckedSupplier<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.get();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }, ioPool);
    }

    private CompletableFuture<Void> runAsync(CheckedRunnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }, ioPool);
    }

    private String rootMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage() == null ? current.toString() : current.getMessage();
    }

    public static void main(String[] args) {
        launch();
    }

    @FunctionalInterface
    private interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    private interface CheckedRunnable {
        void run() throws Exception;
    }

    private static class Session {
        String token;
        UserProfile user;
        boolean hasGroup;
    }

    private static class SubjectCell extends javafx.scene.control.ListCell<Subject> {
        @Override
        protected void updateItem(Subject item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.title + " — " + item.description);
        }
    }

    private static class MaterialCell extends javafx.scene.control.ListCell<Material> {
        @Override
        protected void updateItem(Material item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.title + " (" + item.originalFileName + ")");
        }
    }

    private static class GroupMemberCell extends javafx.scene.control.ListCell<GroupMember> {
        @Override
        protected void updateItem(GroupMember item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.name + " @" + item.username + " [" + item.studentType + "]");
        }
    }

    private record LoginResponse(String token) {}
    private record UserProfile(long id, String name, String username, String email, String studentType, Long groupId) {}
    private record Subject(long id, String title, String description, String photoUrl) {}
    private record Material(long id, String title, String description, String originalFileName, String filePath) {}
    private record Group(String name, List<GroupMember> members) {}
    private record GroupMember(long id, String name, String username, String studentType) {}
    private record SubjectPayload(String title, String description, Path photoPath) {}
    private record MaterialPayload(String title, String description, Path filePath) {}
    private record PasswordChange(String currentPassword, String newPassword) {}

    private static class TextInputDialogEx extends javafx.scene.control.TextInputDialog {
        TextInputDialogEx(String prompt, String initialValue) {
            super(initialValue);
            setTitle(prompt);
            setHeaderText(prompt);
        }
    }

    private static class PasswordDialog extends javafx.scene.control.Dialog<PasswordChange> {
        PasswordDialog() {
            setTitle("Сменить пароль");
            ButtonType submit = new ButtonType("Обновить", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(submit, ButtonType.CANCEL);

            PasswordField current = new PasswordField();
            PasswordField next = new PasswordField();
            PasswordField confirm = new PasswordField();
            current.setPromptText("Текущий пароль");
            next.setPromptText("Новый пароль");
            confirm.setPromptText("Подтвердите пароль");
            VBox content = new VBox(8, current, next, confirm);
            getDialogPane().setContent(content);
            setResultConverter(bt -> {
                if (bt != submit) return null;
                if (!next.getText().equals(confirm.getText())) return null;
                return new PasswordChange(current.getText(), next.getText());
            });
        }
    }

    private static class SubjectDialog extends javafx.scene.control.Dialog<SubjectPayload> {
        SubjectDialog(Stage stage, boolean editMode, Subject subject) {
            setTitle(editMode ? "Изменить предмет" : "Создать предмет");
            ButtonType submit = new ButtonType(editMode ? "Сохранить" : "Создать", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(submit, ButtonType.CANCEL);
            TextField title = new TextField(subject == null ? "" : subject.title);
            TextArea description = new TextArea(subject == null ? "" : subject.description);
            Button pickFile = new Button("Выбрать фото");
            Label fileLabel = new Label("Файл не выбран");
            final Path[] selected = {null};
            pickFile.setOnAction(e -> {
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
                java.io.File file = chooser.showOpenDialog(stage);
                if (file != null) {
                    selected[0] = file.toPath();
                    fileLabel.setText(file.getName());
                }
            });
            getDialogPane().setContent(new VBox(8, title, description, pickFile, fileLabel));
            setResultConverter(bt -> bt == submit ? new SubjectPayload(title.getText(), description.getText(), selected[0]) : null);
        }
    }

    private static class MaterialDialog extends javafx.scene.control.Dialog<MaterialPayload> {
        MaterialDialog(Stage stage, boolean editMode, Material material) {
            setTitle(editMode ? "Изменить материал" : "Создать материал");
            ButtonType submit = new ButtonType(editMode ? "Сохранить" : "Создать", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(submit, ButtonType.CANCEL);
            TextField title = new TextField(material == null ? "" : material.title);
            TextArea description = new TextArea(material == null ? "" : material.description);
            Button pickFile = new Button("Выбрать файл");
            Label fileLabel = new Label("Файл не выбран");
            final Path[] selected = {null};
            pickFile.setOnAction(e -> {
                FileChooser chooser = new FileChooser();
                java.io.File file = chooser.showOpenDialog(stage);
                if (file != null) {
                    selected[0] = file.toPath();
                    fileLabel.setText(file.getName());
                }
            });
            getDialogPane().setContent(new VBox(8, title, description, pickFile, fileLabel));
            setResultConverter(bt -> bt == submit ? new MaterialPayload(title.getText(), description.getText(), selected[0]) : null);
        }
    }

    private static class BackendApi {
        private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build();
        private final ObjectMapper mapper;
        private final String base = System.getProperty("student.tracker.baseUrl", "http://localhost:8080");
        private final String apiBase = base + "/api";
        private final String authBase = base + "/auth";

        BackendApi(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        LoginResponse login(String username, String password) throws Exception {
            JsonNode body = mapper.createObjectNode().put("username", username).put("password", password);
            JsonNode node = sendJson(authBase + "/login", "POST", body, null);
            return new LoginResponse(node.path("accessToken").asText());
        }

        void signup(String name, String username, int studentType, String password) throws Exception {
            JsonNode body = mapper.createObjectNode()
                .put("name", name).put("username", username).put("studentType", studentType).put("password", password);
            sendJson(authBase + "/signup", "POST", body, null);
        }

        UserProfile getProfile(String token) throws Exception {
            JsonNode node = sendJson(apiBase + "/users/profile", "GET", null, token);
            Long groupId = node.path("groupId").isNull() || node.path("groupId").isMissingNode() ? null : node.path("groupId").asLong();
            return new UserProfile(
                node.path("id").asLong(),
                node.path("name").asText(""),
                node.path("username").asText(""),
                node.path("email").asText(""),
                node.path("studentType").asText(""),
                groupId
            );
        }

        void updateProfile(String token, String name) throws Exception {
            JsonNode body = mapper.createObjectNode().put("name", name);
            sendJson(apiBase + "/users/profile", "PUT", body, token);
        }

        void requestEmailChange(String token, String email) throws Exception {
            sendText(apiBase + "/users/request-email-change?newEmail=" + enc(email), "PUT", null, token);
        }

        void changePassword(String token, String currentPassword, String newPassword) throws Exception {
            JsonNode body = mapper.createObjectNode()
                .put("currentPassword", currentPassword)
                .put("newPassword", newPassword)
                .put("confirmPassword", newPassword);
            sendJson(apiBase + "/users/change-password", "PUT", body, token);
        }

        void forgotPassword(String email) throws Exception {
            sendText(apiBase + "/users/forgot-password?email=" + enc(email), "POST", "", null);
        }

        void deleteAccount(String token, String password) throws Exception {
            String url = apiBase + "/users/delete";
            if (password != null && !password.isBlank()) {
                url += "?password=" + enc(password);
            }
            sendText(url, "DELETE", null, token);
        }

        List<Subject> getSubjects(String token) throws Exception {
            JsonNode node = sendJson(apiBase + "/training-subjects", "GET", null, token);
            List<Subject> out = new ArrayList<>();
            if (node.isArray()) {
                for (JsonNode s : node) {
                    out.add(new Subject(s.path("id").asLong(), s.path("title").asText(""), s.path("description").asText(""), s.path("photoUrl").asText("")));
                }
            }
            return out;
        }

        void createSubject(String token, SubjectPayload payload) throws Exception {
            JsonNode dto = mapper.createObjectNode().put("title", payload.title()).put("description", payload.description());
            sendMultipart(apiBase + "/training-subjects", token, Map.of("data", mapper.writeValueAsString(dto)), "photo", payload.photoPath());
        }

        void deleteSubject(String token, long subjectId) throws Exception {
            sendText(apiBase + "/training-subjects/" + subjectId, "DELETE", null, token);
        }

        List<Material> getMaterials(String token, long subjectId) throws Exception {
            JsonNode node = sendJson(apiBase + "/educational-materials/subject/" + subjectId, "GET", null, token);
            List<Material> out = new ArrayList<>();
            if (node.isArray()) {
                for (JsonNode s : node) {
                    out.add(new Material(
                        s.path("id").asLong(),
                        s.path("title").asText(""),
                        s.path("description").asText(""),
                        s.path("originalFileName").asText(""),
                        s.path("filePath").asText("")
                    ));
                }
            }
            return out;
        }

        void createMaterial(String token, long subjectId, MaterialPayload payload) throws Exception {
            JsonNode dto = mapper.createObjectNode()
                .put("trainingSubjectId", subjectId)
                .put("title", payload.title())
                .put("description", payload.description());
            sendMultipart(apiBase + "/educational-materials", token, Map.of("data", mapper.writeValueAsString(dto)), "file", payload.filePath());
        }

        void updateMaterial(String token, long materialId, long subjectId, MaterialPayload payload) throws Exception {
            JsonNode dto = mapper.createObjectNode()
                .put("trainingSubjectId", subjectId)
                .put("title", payload.title())
                .put("description", payload.description());
            sendMultipart(apiBase + "/educational-materials/" + materialId, token, Map.of("data", mapper.writeValueAsString(dto)), "file", payload.filePath());
        }

        void deleteMaterial(String token, long materialId) throws Exception {
            sendText(apiBase + "/educational-materials/" + materialId, "DELETE", null, token);
        }

        Group getMyGroup(String token) throws Exception {
            JsonNode node = sendJson(apiBase + "/groups/my-group", "GET", null, token);
            List<GroupMember> members = new ArrayList<>();
            JsonNode membersNode = node.path("members");
            if (membersNode.isArray()) {
                for (JsonNode m : membersNode) {
                    members.add(new GroupMember(m.path("id").asLong(), m.path("name").asText(""), m.path("username").asText(""), m.path("studentType").asText("")));
                }
            }
            return new Group(node.path("name").asText(""), members);
        }

        boolean hasGroup(String token) throws Exception {
            try {
                Group group = getMyGroup(token);
                return group != null && group.name() != null && !group.name().isBlank();
            } catch (Exception ex) {
                String message = ex.getMessage() == null ? "" : ex.getMessage();
                if (message.contains("HTTP 404")) {
                    return false;
                }
                throw ex;
            }
        }

        void createGroup(String token, String groupName) throws Exception {
            JsonNode body = mapper.createObjectNode().put("name", groupName);
            sendText(apiBase + "/groups", "POST", mapper.writeValueAsString(body), token, "application/json");
        }

        void addUserToGroup(String token, String username) throws Exception {
            sendText(apiBase + "/groups/add-student/" + enc(username), "PUT", null, token);
        }

        void deleteUserFromGroup(String token, long userId) throws Exception {
            sendText(apiBase + "/groups/delete-student/" + userId, "DELETE", null, token);
        }

        void deleteGroup(String token) throws Exception {
            sendText(apiBase + "/groups", "DELETE", null, token);
        }

        void downloadMaterial(String token, String url, Path target) throws Exception {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("HTTP " + response.statusCode());
            }
            Files.write(target, response.body());
        }

        private JsonNode sendJson(String url, String method, JsonNode body, String token) throws Exception {
            HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url)).header("Accept", "application/json");
            if (token != null && !token.isBlank()) builder.header("Authorization", "Bearer " + token);
            switch (method) {
                case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : mapper.writeValueAsString(body)))
                    .header("Content-Type", "application/json");
                case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body == null ? "" : mapper.writeValueAsString(body)))
                    .header("Content-Type", "application/json");
                case "DELETE" -> builder.DELETE();
                default -> builder.GET();
            }
            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
            }
            if (response.body() == null || response.body().isBlank()) return mapper.createObjectNode();
            return mapper.readTree(response.body());
        }

        private void sendText(String url, String method, String body, String token) throws Exception {
            HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url)).header("Accept", "application/json");
            if (token != null && !token.isBlank()) builder.header("Authorization", "Bearer " + token);
            switch (method) {
                case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
                case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
                case "DELETE" -> builder.DELETE();
                default -> builder.GET();
            }
            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
            }
        }

        private void sendText(String url, String method, String body, String token, String contentType) throws Exception {
            HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url)).header("Accept", "application/json");
            if (token != null && !token.isBlank()) builder.header("Authorization", "Bearer " + token);
            if (contentType != null && !contentType.isBlank()) builder.header("Content-Type", contentType);
            switch (method) {
                case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
                case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
                case "DELETE" -> builder.DELETE();
                default -> builder.GET();
            }
            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
            }
        }

        private void sendMultipart(String url, String token, Map<String, String> textParts, String filePartName, Path filePath) throws Exception {
            String boundary = "----JavaFxBoundary" + UUID.randomUUID();
            List<byte[]> chunks = new ArrayList<>();
            for (Map.Entry<String, String> entry : textParts.entrySet()) {
                chunks.add(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
                chunks.add(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                chunks.add((entry.getValue() + "\r\n").getBytes(StandardCharsets.UTF_8));
            }
            if (filePath != null) {
                chunks.add(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
                chunks.add(("Content-Disposition: form-data; name=\"" + filePartName + "\"; filename=\"" + filePath.getFileName() + "\"\r\n").getBytes(StandardCharsets.UTF_8));
                chunks.add(("Content-Type: application/octet-stream\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                chunks.add(Files.readAllBytes(filePath));
                chunks.add("\r\n".getBytes(StandardCharsets.UTF_8));
            }
            chunks.add(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

            int total = chunks.stream().mapToInt(a -> a.length).sum();
            byte[] body = new byte[total];
            int offset = 0;
            for (byte[] chunk : chunks) {
                System.arraycopy(chunk, 0, body, offset, chunk.length);
                offset += chunk.length;
            }

            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
            }
        }

        private String enc(String value) {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        }
    }
}