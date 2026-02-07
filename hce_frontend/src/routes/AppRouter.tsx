import { BrowserRouter, Routes, Route, useParams } from 'react-router-dom';
import Login from '../pages/auth/Login';
import MainLayout from '../components/layout/MainLayout';
import Dashboard from '../pages/dashboard/Dashboard';
import RegistroPaciente from '../pages/pacientes/RegistroPaciente';
import ConsultaPacientes from '../pages/pacientes/ConsultaPacientes';
import ActualizarPaciente from '../pages/pacientes/ActualizarPaciente';
import HistorialConsultas from '../pages/historial/historialConsultasCorre';
import HistorialIndex from '../pages/historial/HistorialIndex';
import VerHistorialCompleto from '../pages/historial/VerHistorialCompleto';
import HistorialUsuarios from '../pages/dashboard/HistorialUsuarios';
import { ReporteCompletoHCE } from '../pages/historial/components/ReporteCompletoHCE';
import { obtenerPacientes } from '../services/dbPacienteService';
import { useState, useEffect } from 'react';

// Wrapper para cargar paciente por cédula para el reporte
const ReporteHCEWrapper = () => {
  const { cedula } = useParams();
  const [paciente, setPaciente] = useState<any>(null);

  useEffect(() => {
    const cargar = async () => {
      const lista = await obtenerPacientes();
      const encontrado = lista.find(p => String(p.cedula) === String(cedula));
      setPaciente(encontrado);
    };
    cargar();
  }, [cedula]);

  return <ReporteCompletoHCE paciente={paciente} />;
};

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route element={<MainLayout />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/pacientes/registro" element={<RegistroPaciente />} />
          <Route path="/pacientes/consulta" element={<ConsultaPacientes />} />
          <Route path="/pacientes/actualizar" element={<ActualizarPaciente />} />

          {/* Gestión de Historial */}
          <Route path="/historial/:cedula" element={<HistorialConsultas />} />
          {/* Gestión de Historial - Index (Busqueda) y Detalle */}
          <Route path="/historial-clinico" element={<HistorialIndex />} />
          <Route path="/historial-completo/:cedula" element={<VerHistorialCompleto />} />

          {/* ✅ VISTA PREVIA REPORTE HCE (SOLO LECTURA) */}
          <Route path="/reporte-hce/:cedula" element={<ReporteHCEWrapper />} />

          <Route path="/historial-usuarios" element={<HistorialUsuarios />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
