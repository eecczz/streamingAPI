import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
      '/initiate-upload': 'http://localhost:8080',
      '/upload-signed-url': 'http://localhost:8080',
      '/complete-upload': 'http://localhost:8080',
      '/abort-upload': 'http://localhost:8080',
    },
  },
})
