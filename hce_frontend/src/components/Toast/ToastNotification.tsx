// src/components/Toast/ToastNotification.tsx
// Sistema de notificaciones toast para feedback del usuario

import { useEffect, useState } from 'react';
import './ToastNotification.css';

export type ToastType = 'info' | 'success' | 'warning' | 'error';

export interface Toast {
    id: string;
    type: ToastType;
    message: string;
    duration?: number; // ms, undefined = no auto-dismiss
}

interface ToastNotificationProps {
    toast: Toast;
    onDismiss: (id: string) => void;
}

export function ToastNotification({ toast, onDismiss }: ToastNotificationProps) {
    const [isExiting, setIsExiting] = useState(false);

    useEffect(() => {
        if (toast.duration) {
            const timer = setTimeout(() => {
                handleDismiss();
            }, toast.duration);
            return () => clearTimeout(timer);
        }
    }, [toast]);

    const handleDismiss = () => {
        setIsExiting(true);
        setTimeout(() => onDismiss(toast.id), 300);
    };

    const iconMap = {
        info: 'bi-info-circle-fill',
        success: 'bi-check-circle-fill',
        warning: 'bi-exclamation-triangle-fill',
        error: 'bi-x-circle-fill'
    };

    return (
        <div className={`toast-notification toast-${toast.type} ${isExiting ? 'toast-exit' : 'toast-enter'}`}>
            <div className="toast-content">
                <i className={`bi ${iconMap[toast.type]} toast-icon`}></i>
                <span className="toast-message">{toast.message}</span>
            </div>
            <button className="toast-close" onClick={handleDismiss}>
                <i className="bi bi-x"></i>
            </button>
        </div>
    );
}
