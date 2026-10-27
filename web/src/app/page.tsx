import ConsultaForm from "@/components/ConsultaForm";
import estilos from "./page.module.css";

// Pagina estatica: todo lo dinamico (captcha y consulta) ocurre en el componente de cliente.
export default function Home() {
  return (
    <main className={estilos.contenedor}>
      <header className={estilos.cabecera}>
        <p className={estilos.oficina}>Oficina Municipal de Agua Potable</p>
        <h1>Consulta pública de estado de cuenta</h1>
        <p>Escribe el código de tu contador para ver tu saldo, tus boletas y tus últimos pagos.</p>
      </header>

      <ConsultaForm />

      <footer className={estilos.pie}>
        <p>Servicio informativo. Tus datos personales no se muestran en esta consulta.</p>
      </footer>
    </main>
  );
}
