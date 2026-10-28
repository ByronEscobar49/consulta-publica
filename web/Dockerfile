# ---------- Etapa 1: dependencias ----------
# "npm ci" instala EXACTAMENTE lo que dice package-lock.json (reproducible, a diferencia de "npm install").
FROM node:22-slim AS deps
WORKDIR /app
COPY package.json package-lock.json ./
RUN --mount=type=cache,target=/root/.npm npm ci

# ---------- Etapa 2: compilar ----------
FROM node:22-slim AS build
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
# La clave PUBLICA del captcha se incrusta en el JavaScript del navegador al compilar,
# por eso se recibe aqui como argumento de build. La clave SECRETA nunca pasa por esta etapa.
ARG NEXT_PUBLIC_RECAPTCHA_SITE_KEY
ENV NEXT_PUBLIC_RECAPTCHA_SITE_KEY=$NEXT_PUBLIC_RECAPTCHA_SITE_KEY
ENV NEXT_TELEMETRY_DISABLED=1
RUN npm run build

# ---------- Etapa 3: ejecutar ----------
# Imagen final: solo el servidor minimo (.next/standalone), sin codigo fuente ni node_modules completo.
FROM node:22-slim AS runner
WORKDIR /app
ENV NODE_ENV=production \
    NEXT_TELEMETRY_DISABLED=1 \
    HOSTNAME=0.0.0.0 \
    PORT=3000
COPY --from=build --chown=node:node /app/.next/standalone ./
COPY --from=build --chown=node:node /app/.next/static ./.next/static
COPY --from=build --chown=node:node /app/public ./public
# Usuario "node" (sin privilegios) que ya trae la imagen: la web no corre como root.
USER node
EXPOSE 3000
CMD ["node", "server.js"]
