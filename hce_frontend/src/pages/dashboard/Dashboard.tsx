import { useEffect, useState } from 'react';

export default function Dashboard() {
  const [nombreUsuario, setNombreUsuario] = useState('Cargando...');
  const [inicial, setInicial] = useState('U');

  useEffect(() => {
    // 1. LEER LOS DATOS DESDE EL LOCALSTORAGE (LOS QUE PUSO EL LOGIN)
    const storedUser = localStorage.getItem('usuarioLogueado');
    if (storedUser) {
      const userData = JSON.parse(storedUser);
      setNombreUsuario(userData.nombre); // Ejemplo: "Dr. Jandry"
      // Sacamos la primera letra del nombre real (después del "Dr. ") para el círculo
      const letra = userData.nombre.split(" ")[1]?.charAt(0).toUpperCase() || "U";
      setInicial(letra);
    }
  }, []);

  return (
    <div className="container-fluid p-0">
      {/* --- ENCABEZADO CON FONDO TRANSPARENTE (IGUAL A LA PANTALLA) --- */}
      <div className="d-flex justify-content-between align-items-center mb-0 px-4 py-3 bg-transparent">
        <div className="d-flex align-items-center">
          <i className="bi bi-grid-1x2-fill text-primary fs-4 me-3"></i>
          {/* Título sin negrita (fw-normal) */}
          <h4 className="text-primary fw-normal m-0">Tablero Clínico</h4>
        </div>

        {/* Perfil dinámico desde la base de datos (LocalStorage) */}
        <div className="d-flex align-items-center border rounded p-2 bg-white shadow-sm" style={{ minWidth: "220px" }}>
          <div className="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2" style={{ width: "40px", height: "40px", fontWeight: "bold" }}>
            {inicial}
          </div>
          <div>
            <div className="text-muted" style={{ fontSize: "10px", lineHeight: "1" }}>Profesional</div>
            <div className="text-dark" style={{ fontSize: "14px", fontWeight: "500" }}>{nombreUsuario}</div>
          </div>
        </div>
      </div>
      
      <hr className="m-0 text-muted opacity-25" />

      {/* --- CONTENIDO DE LAS TARJETAS --- */}
      <div className="d-flex flex-column align-items-center justify-content-center" style={{ minHeight: "70vh" }}>
        <div className="row g-3 w-100 justify-content-center px-4" style={{ maxWidth: "1200px" }}>
          
          <div className="col-md-4">
            <div className="card text-center p-4 shadow-sm border-0">
              <i className="bi bi-people fs-1 text-primary mb-2"></i>
              <h6 className="text-muted fw-normal">Total de pacientes</h6>
              <strong className="fs-3 text-dark">0</strong>
            </div>
          </div>

          <div className="col-md-4">
            <div className="card text-center p-4 shadow-sm border-0">
              <i className="bi bi-calendar-check fs-1 text-success mb-2"></i>
              <h6 className="text-muted fw-normal">Consultas del día</h6>
              <strong className="fs-3 text-dark">0</strong>
            </div>
          </div>

          <div className="col-md-4">
            <div className="card text-center p-4 shadow-sm border-0">
              <i className="bi bi-exclamation-triangle fs-1 text-danger mb-2"></i>
              <h6 className="text-muted fw-normal">Alertas Clínicas</h6>
              <strong className="fs-3 text-dark">0</strong>
            </div>
          </div>

        </div>
      </div>
    </div>
  );
}
