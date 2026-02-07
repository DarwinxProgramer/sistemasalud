package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "datos_gestacionales")
@Getter
@Setter
public class DatoGestacional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dato_gestacional")
    private Integer idDatoGestacional;

    @Column(name = "producto_gestacion", length = 100)
    private String productoGestacion;

    @Column(name = "edad_gestacional", length = 100)
    private String edadGestacional;

    @Column(name = "via_parto", length = 150)
    private String viaParto;

    // --- CORRECCIÓN DEL ERROR ---
    // Eliminamos precision y scale para evitar el error con Double
    @Column(name = "peso_nacer")
    private Double pesoNacer;

    @Column(name = "talla_nacer")
    private Double tallaNacer;
    // ----------------------------

    @Column(name = "apgar_minuto")
    private Integer apgarMinuto;

    @Column(name = "apgar_cinco_minutos")
    private Integer apgarCincoMinutos;

    @Column(columnDefinition = "TEXT")
    private String complicacionesPerinatales;

    // --- UNIÓN (RELACIÓN) ---
    // Reemplazamos el Integer suelto por la entidad real
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_antecedente_perinatal")
    private AntecedentePerinatal antecedentePerinatal;

    @Column(length = 50)
    private String usuario;

    @Column(name = "id_personal")
    private Integer idPersonal;

    @Column(name = "uuid_offline", length = 36)
    private String uuidOffline;

    @Column(name = "sync_status", length = 20)
    private String syncStatus;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(length = 20)
    private String origin;

    @PrePersist
    @PreUpdate
    protected void updateAudit() {
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null) this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null) this.syncStatus = "PENDING";
    }
}