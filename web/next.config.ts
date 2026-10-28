import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  cacheComponents: true,
  partialPrefetching: true,
  // Genera .next/standalone: un servidor Node minimo con solo lo necesario para ejecutar la web.
  // Es lo que se copia a la imagen final de Docker (sin node_modules completo ni codigo fuente).
  output: "standalone",
};

export default nextConfig;
