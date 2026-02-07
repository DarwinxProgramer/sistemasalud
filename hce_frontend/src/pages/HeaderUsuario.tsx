import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

// Prop opcional para mostrar el título de la página actual a la izquierda
export default function HeaderUsuario({ titulo }: { titulo?: string }) {
  const navigate = useNavigate();
  const [nombreDoctor, setNombreDoctor] = useState("Cargando...");
  const [inicial, setInicial] = useState("?");

  useEffect(() => {
    // Leemos el dato que guardó el Login
    const sesion = localStorage.getItem('usuarioLogueado');
    
    if (sesion) {
      const data = JSON.parse(sesion);
      const nombre = data.nombre || "Usuario";
      setNombreDoctor(nombre);
      
      // Obtenemos la inicial del nombre (ej: "D" de Dr. Juan)
      // Si quieres la inicial del nombre real, ajusta el slice
      // Aquí tomamos la primera letra del string completo
      setInicial(nombre.charAt(0).toUpperCase()); 
    } else {
        // Si no hay sesión, redirigir al login (opcional)
        // navigate('/'); 
        setNombreDoctor("Invitado");
    }
  }, []);

  const cerrarSesion = () => {
      localStorage.removeItem('usuarioLogueado');
      navigate('/'); // Vuelve al Login
  };

  return (
    <div className="bg-white border-bottom py-2 px-4 d-flex justify-content-between align-items-center shadow-sm flex-shrink-0" style={{height: '70px'}}>
      
      {/* Título de la página (Izquierda) */}
      <div>
          {titulo && <h5 className="fw-bold text-primary m-0"><i className="bi bi-folder2-open me-2"></i>{titulo}</h5>}
      </div>

      {/* Área de Usuario (Derecha) */}
      <div className="d-flex align-items-center gap-3">
          {/* Nombre y Rol */}
          <div className="text-end lh-1">
              <div className="fw-bold text-dark" style={{fontSize: '0.9rem'}}>{nombreDoctor}</div>
              <small className="text-muted" style={{fontSize: '0.75rem'}}>Médico Tratante</small>
          </div>

          {/* Círculo con Inicial */}
          <div className="rounded-circle bg-primary text-white d-flex justify-content-center align-items-center fw-bold shadow-sm" 
               style={{width: '40px', height: '40px', fontSize: '1.2rem', border: '2px solid #e9ecef'}}>
              {inicial}
          </div>

          {/* Botón Salir */}
          <button onClick={cerrarSesion} className="btn btn-outline-danger btn-sm ms-2" title="Salir">
              <i className="bi bi-box-arrow-right"></i>
          </button>
      </div>
    </div>
  );
}