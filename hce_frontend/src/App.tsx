import { useEffect } from 'react';
import AppRouter from './routes/AppRouter';
import { ToastProvider, useToast } from './contexts/ToastContext';
import { syncService } from './services/syncService';

function AppContent() {
  const { showToast } = useToast();

  useEffect(() => {
    // Conectar syncService con el sistema de toast
    syncService.setToastCallback((message, type) => {
      showToast(message, type);
    });
  }, [showToast]);

  return <AppRouter />;
}

function App() {
  return (
    <ToastProvider>
      <AppContent />
    </ToastProvider>
  );
}

export default App;
