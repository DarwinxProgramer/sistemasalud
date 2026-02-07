export default function Header() {
  return (
    <nav className="navbar navbar-light bg-white shadow-sm px-4">
      <span className="navbar-brand fw-bold">
        🏥 Historia Clínica Electrónica
      </span>
      <button className="btn btn-outline-danger btn-sm">
        <i className="bi bi-box-arrow-right"></i> Cerrar sesión
      </button>
    </nav>
  )
}