#!/bin/sh

envsubst '$$API_BASE_URL' < /app/nginx.conf > /etc/nginx/conf.d/default.conf

exec nginx -g 'daemon off;'
