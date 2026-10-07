package br.com.estacionamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.estacionamento.model.Estacionamento;
import br.com.estacionamento.repository.EstacionamentoRepository;
import br.com.estacionamento.service.EstacionamentoService;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private LocalDateTime horarioSaidaCalculado;
    private BigDecimal valorCalculadoSaida;

    private final EstacionamentoService service =
            new EstacionamentoService(new EstacionamentoRepository());

    private static final String PRIMARY = "#2563eb";
    private static final String PRIMARY_DARK = "#1d4ed8";
    private static final String TEXT = "#172033";
    private static final String MUTED = "#64748b";
    private static final String BORDER = "#e2e8f0";
    private static final String BACKGROUND = "#f5f7fb";
    private static final String CARD = "#ffffff";
    private static final String SUCCESS = "#15803d";
    private static final String DANGER = "#b91c1c";

    @Override
    public void start(Stage stage) {

        // =========================
        // CABEÇALHO
        // =========================

        Label titulo = new Label("Estacionamento");
        titulo.getStyleClass().add("app-title");

        Label subtitulo = new Label("Controle de entradas, saídas e veículos ativos");
        subtitulo.getStyleClass().add("app-subtitle");

        VBox cabecalho = new VBox(4, titulo, subtitulo);
        cabecalho.getStyleClass().add("header");

        // =========================
        // ENTRADA
        // =========================

        Label tituloEntrada = new Label("Registrar entrada");
        tituloEntrada.getStyleClass().add("card-title");

        Label descricaoEntrada = new Label("Informe a placa para registrar um novo veículo.");
        descricaoEntrada.getStyleClass().add("card-description");

        TextField placaEntrada = new TextField();
        placaEntrada.setPromptText("Ex.: ABC1D23");
        placaEntrada.getStyleClass().add("input");

        Button botaoEntrada = new Button("Registrar entrada");
        botaoEntrada.getStyleClass().add("primary-button");

        Label mensagemEntrada = new Label();
        mensagemEntrada.getStyleClass().add("feedback");

        TableView<Estacionamento> tabelaAtivos = new TableView<>();

        HBox.setHgrow(placaEntrada, Priority.ALWAYS);

        HBox camposEntrada = new HBox(12, placaEntrada, botaoEntrada);
        camposEntrada.setAlignment(Pos.CENTER_LEFT);

        VBox cardEntrada = criarCard(
                tituloEntrada,
                descricaoEntrada,
                camposEntrada,
                mensagemEntrada
        );

        // =========================
        // SAÍDA
        // =========================

        Label tituloSaida = new Label("Registrar saída");
        tituloSaida.getStyleClass().add("card-title");

        Label descricaoSaida = new Label(
                "Calcule o valor, ajuste o valor final se necessário e finalize."
        );
        descricaoSaida.getStyleClass().add("card-description");

        TextField placaSaida = new TextField();
        placaSaida.setPromptText("Placa");
        placaSaida.getStyleClass().add("input");

        TextField valorCalculado = new TextField();
        valorCalculado.setPromptText("Valor calculado");
        valorCalculado.setEditable(false);
        valorCalculado.getStyleClass().addAll("input", "readonly-input");

        TextField valorFinal = new TextField();
        valorFinal.setPromptText("Valor final");
        valorFinal.getStyleClass().add("input");

        Button botaoCalcular = new Button("Calcular");
        botaoCalcular.getStyleClass().add("secondary-button");

        Button botaoFinalizar = new Button("Finalizar saída");
        botaoFinalizar.getStyleClass().add("success-button");

        Label mensagemSaida = new Label();
        mensagemSaida.getStyleClass().add("feedback");

        GridPane camposSaida = new GridPane();
        camposSaida.setHgap(12);
        camposSaida.setVgap(10);

        camposSaida.add(placaSaida, 0, 0);
        camposSaida.add(valorCalculado, 1, 0);

        camposSaida.add(valorFinal, 0, 1);
        camposSaida.add(botaoCalcular, 1, 1);
        camposSaida.add(botaoFinalizar, 2, 1);

        GridPane.setHgrow(placaSaida, Priority.ALWAYS);
        GridPane.setHgrow(valorCalculado, Priority.ALWAYS);
        GridPane.setHgrow(valorFinal, Priority.ALWAYS);

        VBox cardSaida = criarCard(
                tituloSaida,
                descricaoSaida,
                camposSaida,
                mensagemSaida
        );

        // =========================
        // CALCULAR SAÍDA
        // =========================

        botaoCalcular.setOnAction(event -> {

            String placa = placaSaida.getText();

            horarioSaidaCalculado = LocalDateTime.now();

            BigDecimal valor = service.calcularValorSaida(
                    placa,
                    horarioSaidaCalculado
            );

            if (valor != null) {

                valorCalculadoSaida = valor;

                valorCalculado.setText("R$ " + valor);
                valorFinal.setText(valor.toString());

                mensagemSaida.getStyleClass().removeAll("feedback-error");
                mensagemSaida.getStyleClass().add("feedback-success");

                mensagemSaida.setText(
                        "Valor calculado com sucesso. Confira o valor final."
                );

            } else {

                valorCalculadoSaida = null;
                horarioSaidaCalculado = null;

                valorCalculado.clear();
                valorFinal.clear();

                mensagemSaida.getStyleClass().removeAll("feedback-success");
                mensagemSaida.getStyleClass().add("feedback-error");

                mensagemSaida.setText(
                        "Não foi possível calcular. Verifique a placa."
                );
            }
        });

        // =========================
        // FINALIZAR SAÍDA
        // =========================

        botaoFinalizar.setOnAction(event -> {

            String placa = placaSaida.getText();

            if (horarioSaidaCalculado == null || valorCalculadoSaida == null) {

                mensagemSaida.getStyleClass().removeAll("feedback-success");
                mensagemSaida.getStyleClass().add("feedback-error");

                mensagemSaida.setText(
                        "Calcule a saída antes de finalizar."
                );
                return;
            }

            BigDecimal valorFinalInformado;

            try {
                valorFinalInformado =
                        new BigDecimal(valorFinal.getText().replace(",", "."));

            } catch (NumberFormatException e) {

                mensagemSaida.getStyleClass().removeAll("feedback-success");
                mensagemSaida.getStyleClass().add("feedback-error");

                mensagemSaida.setText(
                        "Informe um valor final válido."
                );

                return;
            }

            boolean sucesso = service.registrarSaida(
                    placa,
                    horarioSaidaCalculado,
                    valorFinalInformado
            );

            if (sucesso) {

                mensagemSaida.getStyleClass().removeAll("feedback-error");
                mensagemSaida.getStyleClass().add("feedback-success");

                mensagemSaida.setText(
                        "Saída registrada com sucesso."
                );

                tabelaAtivos.getItems().setAll(
                        service.buscarTodosAtivos()
                );

                placaSaida.clear();
                valorCalculado.clear();
                valorFinal.clear();

                horarioSaidaCalculado = null;
                valorCalculadoSaida = null;

            } else {

                mensagemSaida.getStyleClass().removeAll("feedback-success");
                mensagemSaida.getStyleClass().add("feedback-error");

                mensagemSaida.setText(
                        "Não foi possível registrar a saída."
                );
            }
        });

        // =========================
        // VEÍCULOS ATIVOS
        // =========================

        Label tituloAtivos = new Label("Veículos ativos");
        tituloAtivos.getStyleClass().add("card-title");

        Label descricaoAtivos = new Label(
                "Veículos atualmente registrados no estacionamento."
        );
        descricaoAtivos.getStyleClass().add("card-description");

        DateTimeFormatter formatoDataHora =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        TableColumn<Estacionamento, String> colunaPlaca =
                new TableColumn<>("Placa");

        colunaPlaca.setCellValueFactory(
                new PropertyValueFactory<>("placa")
        );

        TableColumn<Estacionamento, LocalDateTime> colunaEntrada =
                new TableColumn<>("Entrada");

        colunaEntrada.setCellValueFactory(
                new PropertyValueFactory<>("entrada")
        );

        colunaEntrada.setCellFactory(coluna -> new TableCell<>() {

            @Override
            protected void updateItem(
                    LocalDateTime entrada,
                    boolean empty) {

                super.updateItem(entrada, empty);

                if (empty || entrada == null) {
                    setText(null);
                } else {
                    setText(entrada.format(formatoDataHora));
                }
            }
        });

        TableColumn<Estacionamento, Estacionamento.Status> colunaStatus =
                new TableColumn<>("Status");

        colunaStatus.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        colunaStatus.setCellFactory(coluna -> new TableCell<>() {

            @Override
            protected void updateItem(
                    Estacionamento.Status status,
                    boolean empty) {

                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    getStyleClass().removeAll("status-active");
                } else {
                    setText("ATIVO");
                    getStyleClass().add("status-active");
                }
            }
        });

        tabelaAtivos.getColumns().addAll(
                colunaPlaca,
                colunaEntrada,
                colunaStatus
        );

        colunaPlaca.prefWidthProperty().bind(
                tabelaAtivos.widthProperty().multiply(0.30)
        );

        colunaEntrada.prefWidthProperty().bind(
                tabelaAtivos.widthProperty().multiply(0.45)
        );

        colunaStatus.prefWidthProperty().bind(
                tabelaAtivos.widthProperty().multiply(0.25)
        );

        tabelaAtivos.getItems().setAll(
                service.buscarTodosAtivos()
        );

        tabelaAtivos.setPlaceholder(
                new Label("Nenhum veículo estacionado no momento.")
        );

        tabelaAtivos.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        VBox.setVgrow(tabelaAtivos, Priority.ALWAYS);

        VBox cardAtivos = criarCard(
                tituloAtivos,
                descricaoAtivos,
                tabelaAtivos
        );

        // =========================
        // RODAPÉ
        // =========================

        Button botaoHistorico = new Button("Ver histórico");
        botaoHistorico.getStyleClass().add("outline-button");

        HBox rodape = new HBox(botaoHistorico);
        rodape.setAlignment(Pos.CENTER_RIGHT);

        // =========================
        // CONTEÚDO
        // =========================

        VBox conteudo = new VBox(
                18,
                cabecalho,
                cardEntrada,
                cardSaida,
                cardAtivos,
                rodape
        );

        conteudo.setPadding(new Insets(28));
        conteudo.setMaxWidth(1100);

        BorderPane root = new BorderPane();
        root.setCenter(conteudo);
        root.getStyleClass().add("root");

        Scene scene = new Scene(root, 1100, 820);

        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Estacionamento");
        stage.setMinWidth(950);
        stage.setMinHeight(700);
        stage.setScene(scene);
        stage.show();
    }

    private VBox criarCard(javafx.scene.Node... elementos) {

        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.getChildren().addAll(elementos);

        return card;
    }

    public static void main(String[] args) {
        launch();
    }
}
