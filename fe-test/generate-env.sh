#!/bin/sh
# Runs automatically at container startup (nginx's official image executes every
# executable script in /docker-entrypoint.d/ before starting nginx).
# Overwrites the local-dev stub (public/env.js) with the real backend URL, if one
# was provided - see fe-test/src/app/api-base.ts for how it's consumed.
set -e

cat > /usr/share/nginx/html/env.js <<EOF
window.__env = { apiBase: "${API_BASE_URL:-}" };
EOF
