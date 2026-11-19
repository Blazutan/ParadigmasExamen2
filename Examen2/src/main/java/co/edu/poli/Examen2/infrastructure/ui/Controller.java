package co.edu.poli.Examen2.infrastructure.ui;

import co.edu.poli.Examen2.aplication.facade.ExamenesFacade;
import co.edu.poli.Examen2.aplication.port.out.FormatoSerializacion;
import co.edu.poli.Examen2.infrastructure.factory.ExamenesFactoryPorDefecto;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/** Controlador JavaFX que conecta la interfaz con {@link ExamenesFacade}. */
public class Controller {
    @FXML
    private TextField txtCodigoExamen;
    @FXML
    private TextField txtNombrePaciente;
    @FXML
    private TextField txtCosto;
    @FXML
    private ComboBox<String> cmbTipoSangre;
    @FXML
    private ComboBox<FormatoSerializacion> cmbFormato;
    @FXML
    private RadioButton rbPositivo;
    @FXML
    private RadioButton rbNegativo;
    @FXML
    private TextArea txtAreaMensajes;

    private final ExamenesFacade facade =
            new ExamenesFacade(new ExamenesFactoryPorDefecto());

    @FXML
    private void initialize() {
        cmbFormato.getItems().setAll(facade.formatosDisponibles());
        cmbFormato.getSelectionModel().select(FormatoSerializacion.JSON);
        cmbFormato.valueProperty().addListener((observable, anterior, formato) -> {
            if (formato != null) {
                facade.seleccionarFormato(formato);
            }
        });
    }

    @FXML
    private void handleGuardar() {
        String factorRh = rbNegativo.isSelected() ? "NEGATIVO" : "POSITIVO";
        mostrarMensaje(facade.registrarExamen(
                txtCodigoExamen.getText(),
                txtNombrePaciente.getText(),
                txtCosto.getText(),
                cmbTipoSangre.getValue(),
                factorRh));
    }

    @FXML
    private void handleLimpiar() {
        txtCodigoExamen.clear();
        txtNombrePaciente.clear();
        txtCosto.clear();
        cmbTipoSangre.getSelectionModel().clearSelection();
        rbPositivo.setSelected(true);
        txtAreaMensajes.clear();
    }

    @FXML
    private void handleSerializar() {
        mostrarMensaje(facade.serializarExamenes());
    }

    @FXML
    private void handleDeserializar() {
        mostrarMensaje(facade.deserializarExamenes());
    }

    @FXML
    private void handleListar() {
        mostrarMensaje(facade.listarExamenes());
    }

    private void mostrarMensaje(String mensaje) {
        txtAreaMensajes.setText(mensaje == null ? "Ocurrió un error sin descripción." : mensaje);
    }
}
