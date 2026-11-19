module co.edu.poli.Examen2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.poli.Examen2.infrastructure.ui to javafx.fxml;
    opens co.edu.poli.Examen2.aplication.domain.model to java.base;

    exports co.edu.poli.Examen2.vista;
}
