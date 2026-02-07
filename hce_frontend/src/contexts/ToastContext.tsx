// src/contexts/ToastContext.tsx
// Context y Provider para gestionar notificaciones toast globalmente

import { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';
import { ToastNotification } from '../components/Toast/ToastNotification';
import type { Toast, ToastType } from '../components/Toast/ToastNotification';

interface ToastContextValue {
    showToast: (message: string, type?: ToastType, duration?: number) => void;
    showSyncToast: (message: string) => void;
    showSuccessToast: (message: string) => void;
    showErrorToast: (message: string) => void;
    showWarningToast: (message: string) => void;
}

const ToastContext = createContext<ToastContextValue | undefined>(undefined);

export function ToastProvider({ children }: { children: ReactNode }) {
    const [toasts, setToasts] = useState<Toast[]>([]);

    const showToast = (message: string, type: ToastType = 'info', duration = 4000) => {
        const id = `toast-${Date.now()}-${Math.random()}`;
        const newToast: Toast = { id, message, type, duration };

        setToasts(prev => [...prev, newToast]);
    };

    const dismissToast = (id: string) => {
        setToasts(prev => prev.filter(t => t.id !== id));
    };

    const showSyncToast = (message: string) => showToast(message, 'info', 3000);
    const showSuccessToast = (message: string) => showToast(message, 'success', 3000);
    const showErrorToast = (message: string) => showToast(message, 'error', 5000);
    const showWarningToast = (message: string) => showToast(message, 'warning', 4000);

    return (
        <ToastContext.Provider value={{
            showToast,
            showSyncToast,
            showSuccessToast,
            showErrorToast,
            showWarningToast
        }}>
            {children}
            <div className="toast-container">
                {toasts.map(toast => (
                    <ToastNotification
                        key={toast.id}
                        toast={toast}
                        onDismiss={dismissToast}
                    />
                ))}
            </div>
        </ToastContext.Provider>
    );
}

export function useToast() {
    const context = useContext(ToastContext);
    if (!context) {
        throw new Error('useToast must be used within ToastProvider');
    }
    return context;
}
