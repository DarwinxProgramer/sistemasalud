import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { VitePWA } from 'vite-plugin-pwa'

// https://vite.dev/config/
export default defineConfig({
  base: '/',
  plugins: [
    react(),
    VitePWA({
      registerType: 'autoUpdate',
    includeAssets: ['favicon.ico', 'robots.txt'],
    manifest: {
      name: 'HCE - Historia Clínica Electrónica',
      short_name: 'HCE',
      description: 'Sistema de Historia Clínica Electrónica con soporte offline',
      theme_color: '#0d6efd',
      background_color: '#ffffff',
      display: 'standalone',
      icons: [
        {
          src: 'pwa-192x192.png', // <-- Quita la "/" inicial
          sizes: '192x192',
          type: 'image/png'
        },
        {
          src: 'pwa-512x512.png', // <-- Quita la "/" inicial
          sizes: '512x512',
          type: 'image/png'
        }
        ]
      },
      workbox: {
        // Estrategia de caché para assets estáticos
        runtimeCaching: [
          {
            urlPattern: /^https:\/\/fonts\.googleapis\.com\/.*/i,
            handler: 'CacheFirst',
            options: {
              cacheName: 'google-fonts-cache',
              expiration: {
                maxEntries: 10,
                maxAgeSeconds: 60 * 60 * 24 * 365 // 1 año
              }
            }
          },
          {
            urlPattern: /^https:\/\/cdn\.jsdelivr\.net\/.*/i,
            handler: 'StaleWhileRevalidate',
            options: {
              cacheName: 'cdn-cache'
            }
          }
        ]
      }
    })
  ],
})
