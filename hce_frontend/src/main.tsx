import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'

// Bootstrap
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-icons/font/bootstrap-icons.css'
import './index.css'

// Initialize sync service
import { syncService } from './services/syncService'
syncService.initAutoSync()

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
)
