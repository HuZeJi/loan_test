// Local dev / docker-compose stub: no override, api-base.ts falls back to
// deriving the backend URL from the current hostname. When deployed
// separately (e.g. Railway), the container entrypoint overwrites this file
// with the real backend URL before nginx starts - see fe-test/Dockerfile.
window.__env = {};
