FROM node:22-alpine AS build
WORKDIR /workspace
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npx ng build

FROM nginx:alpine
COPY --from=build /workspace/dist/fe-test/browser /usr/share/nginx/html
# Templated so the listen port can be overridden at startup (e.g. Railway assigns
# its own $PORT per service instead of always using 80).
COPY nginx-templates/default.conf.template /etc/nginx/templates/default.conf.template
# Writes env.js with the deployed backend's URL before nginx starts - see api-base.ts.
COPY generate-env.sh /docker-entrypoint.d/90-generate-env.sh
RUN chmod +x /docker-entrypoint.d/90-generate-env.sh
ENV PORT=80
EXPOSE 80
