package ec.gob.salud.hce.backend.service; // <--- Esta es la ruta del paquete

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReporteService {

    @Autowired
    private DataSource dataSource;

    /**
     * Genera el PDF de la Historia Clínica compilando el reporte maestro y el subreporte.
     */
    public byte[] generarHistoriaClinicaPdf(Integer idPaciente) throws Exception {
        Connection connection = null;
        try {
            // 1. Obtenemos la conexión a la base de datos actual (MySQL)
            connection = dataSource.getConnection();

            // 2. Cargar y Compilar el Subreporte (Detalles Consultas)
            // Asegúrate de que este archivo esté en: src/main/resources/reports/detalles_consultas.jrxml
            InputStream subreportStream = new ClassPathResource("reports/detalles_consultas.jrxml").getInputStream();
            JasperReport subReport = JasperCompileManager.compileReport(subreportStream);

            // 3. Cargar y Compilar el Reporte Maestro (Historia Clínica)
            // Asegúrate de que este archivo esté en: src/main/resources/reports/historia_clinica.jrxml
            InputStream masterStream = new ClassPathResource("reports/historia_clinica.jrxml").getInputStream();
            JasperReport masterReport = JasperCompileManager.compileReport(masterStream);

            // 4. Preparar Parámetros
            Map<String, Object> parameters = new HashMap<>();
            
            // Este parámetro 'id_paciente' es el que pide tu SQL en Jasper
            parameters.put("id_paciente", idPaciente);
            
            // CRUCIAL: Pasamos el objeto del subreporte compilado al maestro.
            // En tu XML 'historia_clinica.jrxml' tienes un parámetro llamado "SUBREPORT_OBJECT".
            parameters.put("SUBREPORT_OBJECT", subReport); 

            // 5. Llenar el reporte con datos y conexión
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parameters, connection);

            // 6. Exportar a PDF (bytes)
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } finally {
            // Siempre cerrar la conexión para no saturar el pool
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }
}