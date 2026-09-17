// Local dev / docker-compose: frontend and backend share a host, so the backend
// is always reachable on port 8080 on that same host.
// Deployed separately (e.g. Railway, where frontend and backend get different
// hostnames): env.js (generated at container startup, see fe-test/Dockerfile)
// sets window.__env.apiBase to the backend's actual URL before this runs.
declare global {
  interface Window {
    __env?: { apiBase?: string };
  }
}

export const API_BASE =
  window.__env?.apiBase || `${location.protocol}//${location.hostname}:8080`;
