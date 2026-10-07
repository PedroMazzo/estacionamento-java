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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private LocalDateTime horarioSaidaCalculado;
    private BigDecimal valorCalculadoSaida;

    private final EstacionamentoService service = new EstacionamentoService(new EstacionamentoRepository());

    @Override
    public void start(Stage stage) {

        // =========================
        // TÍTULO
        // =========================

        Label titulo = new Label("ESTACIONAMENTO");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        // =========================
        // ENTRADA
        // =========================

        Label tituloEntrada = new Label("Registrar entrada");
        tituloEntrada.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField placaEntrada = new TextField();
        placaEntrada.setPromptText("Placa");

        Button botaoEntrada = new Button("ENTRAR");

        Label mensagemEntrada = new Label();

        TableView<Estacionamento> tabelaAtivos = new TableView<>();

        botaoEntrada.setOnAction(event -> {

            String placa = placaEntrada.getText();

            LocalDateTime horario = LocalDateTime.now();

            boolean entradaSucesso = service.registrarEntrada(placa, horario);

            if (entradaSucesso) {

                mensagemEntrada.setText(
                        "Entrada registrada com sucesso!");

                tabelaAtivos.getItems().setAll(
                        service.buscarTodosAtivos());

                placaEntrada.clear();

            } else {

                mensagemEntrada.setText(
                        "Não foi possível registrar a entrada.");
            }

            System.out.println("Placa: " + placa);
            System.out.println("Horário: " + horario);
        });

        HBox camposEntrada = new HBox(10);

        camposEntrada.getChildren().addAll(
                placaEntrada,
                botaoEntrada);

        VBox cardEntrada = new VBox(10);

        cardEntrada.setPadding(new Insets(15));

        cardEntrada.setStyle(
                "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;");

        cardEntrada.getChildren().addAll(
                tituloEntrada,
                camposEntrada,
                mensagemEntrada);

        // =========================
        // SAÍDA
        // =========================

        Label tituloSaida = new Label("Registrar saída");
        tituloSaida.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField placaSaida = new TextField();
        placaSaida.setPromptText("Placa");

        TextField valorCalculado = new TextField();
        valorCalculado.setPromptText("Valor calculado");
        valorCalculado.setEditable(false);

        TextField valorFinal = new TextField();
        valorFinal.setPromptText("Valor final");

        Button botaoCalcular = new Button("CALCULAR");

        Button botaoFinalizar = new Button("FINALIZAR SAÍDA");

        Label mensagemSaida = new Label();

        // -------------------------
        // CALCULAR SAÍDA
        // -------------------------

        botaoCalcular.setOnAction(event -> {

            String placa = placaSaida.getText();

            horarioSaidaCalculado = LocalDateTime.now();

            BigDecimal valor = service.calcularValorSaida(
                    placa,
                    horarioSaidaCalculado);

            if (valor != null) {

                valorCalculadoSaida = valor;

                valorCalculado.setText(valor.toString());
                valorFinal.setText(valor.toString());

                mensagemSaida.setText(
                        "Valor calculado com sucesso. Confira o valor final.");

            } else {

                valorCalculadoSaida = null;
                horarioSaidaCalculado = null;

                valorCalculado.clear();
                valorFinal.clear();

                mensagemSaida.setText(
                        "Não foi possível calcular a saída. Verifique a placa.");
            }
        });

        botaoFinalizar.setOnAction(event -> {

            String placa = placaSaida.getText();

            if (horarioSaidaCalculado == null || valorCalculadoSaida == null) {
                mensagemSaida.setText(
                        "Calcule a saída antes de finalizar.");
                return;
            }

            BigDecimal valorFinalInformado;

            try {
                valorFinalInformado = new BigDecimal(valorFinal.getText());

            } catch (NumberFormatException e) {

                mensagemSaida.setText(
                        "Informe um valor final válido.");

                return;
            }

            boolean sucesso = service.registrarSaida(
                    placa,
                    horarioSaidaCalculado,
                    valorFinalInformado);

            if (sucesso) {

                mensagemSaida.setText(
                        "Saída registrada com sucesso!");

                tabelaAtivos.getItems().setAll(
                        service.buscarTodosAtivos());

                placaSaida.clear();
                valorCalculado.clear();
                valorFinal.clear();

                horarioSaidaCalculado = null;
                valorCalculadoSaida = null;

            } else {

                mensagemSaida.setText(
                        "Não foi possível registrar a saída.");
            }
        });

        // -------------------------
        // CAMPOS DA SAÍDA
        // -------------------------

        GridPane camposSaida = new GridPane();

        camposSaida.setHgap(10);
        camposSaida.setVgap(10);

        camposSaida.add(placaSaida, 0, 0);
        camposSaida.add(valorCalculado, 1, 0);

        camposSaida.add(valorFinal, 0, 1);
        camposSaida.add(botaoCalcular, 1, 1);
        camposSaida.add(botaoFinalizar, 2, 1);

        VBox cardSaida = new VBox(10);

        cardSaida.setPadding(new Insets(15));

        cardSaida.setStyle(
                "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;");

        cardSaida.getChildren().addAll(
                tituloSaida,
                camposSaida,
                mensagemSaida);

        // =========================
        // VEÍCULOS ATIVOS
        // =========================

        Label tituloAtivos = new Label("Veículos ativos");
        tituloAtivos.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        DateTimeFormatter formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // -------------------------
        // COLUNA PLACA
        // -------------------------

        TableColumn<Estacionamento, String> colunaPlaca = new TableColumn<>("Placa");

        colunaPlaca.setCellValueFactory(
                new PropertyValueFactory<>("placa"));

        // -------------------------
        // COLUNA ENTRADA
        // -------------------------

        TableColumn<Estacionamento, LocalDateTime> colunaEntrada = new TableColumn<>("Entrada");

        colunaEntrada.setCellValueFactory(
                new PropertyValueFactory<>("entrada"));

        colunaEntrada.setCellFactory(coluna -> new TableCell<>() {

            @Override
            protected void updateItem(
                    LocalDateTime entrada,
                    boolean empty) {
                super.updateItem(entrada, empty);

                if (empty || entrada == null) {

                    setText(null);

                } else {

                    setText(
                            entrada.format(formatoDataHora));
                }
            }
        });

        // -------------------------
        // COLUNA STATUS
        // -------------------------

        TableColumn<Estacionamento, Estacionamento.Status> colunaStatus = new TableColumn<>("Status");

        colunaStatus.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        // -------------------------
        // CARREGAR ATIVOS
        // -------------------------

        tabelaAtivos.getItems().addAll(
                service.buscarTodosAtivos());

        tabelaAtivos.getColumns().addAll(
                colunaPlaca,
                colunaEntrada,
                colunaStatus);

        tabelaAtivos.setPlaceholder(
                new Label("Nenhum veículo estacionado"));

        VBox cardAtivos = new VBox(10);

        cardAtivos.setPadding(new Insets(15));

        cardAtivos.getChildren().addAll(
                tituloAtivos,
                tabelaAtivos);

        // =========================
        // RODAPÉ
        // =========================

        Button botaoHistorico = new Button("VER HISTÓRICO");

        HBox rodape = new HBox(botaoHistorico);

        rodape.setAlignment(Pos.CENTER_RIGHT);

        // =========================
        // CONTEÚDO PRINCIPAL
        // =========================

        VBox conteudo = new VBox(20);

        conteudo.setPadding(new Insets(20));

        conteudo.getChildren().addAll(
                titulo,
                cardEntrada,
                cardSaida,
                cardAtivos,
                rodape);

        // =========================
        // JANELA
        // =========================

        BorderPane root = new BorderPane();

        root.setCenter(conteudo);

        Scene scene = new Scene(root, 1000, 700);

        stage.setTitle("Estacionamento");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}