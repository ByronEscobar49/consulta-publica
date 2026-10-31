import type { Metadata } from "next";
import type { ReactNode } from "react";
import "./globals.css";
import ChatWidget from "@/components/chat/ChatWidget";

export const metadata: Metadata = {
  title: "Consulta pública | Oficina Municipal de Agua Potable",
  description: "Consulta tu saldo, tus boletas y tus pagos de agua potable con el código de tu contador.",
};

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="es">
      <body>{children}<ChatWidget /></body>
    </html>
  );
}
