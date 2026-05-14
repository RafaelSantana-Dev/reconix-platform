import { create } from 'zustand'

interface ToastNotification {
  id: string
  type: 'success' | 'error' | 'warning' | 'info'
  message: string
  duration?: number
}

interface NotificationState {
  notifications: ToastNotification[]
  addNotification: (notification: Omit<ToastNotification, 'id'>) => void
  removeNotification: (id: string) => void
}

export const useNotificationStore = create<NotificationState>(set => ({
  notifications: [],
  addNotification: notification => {
    const id = Math.random().toString(36).substring(7)
    const newNotification = { ...notification, id }

    set(state => ({
      notifications: [...state.notifications, newNotification],
    }))

    // Auto remove after duration
    const duration = notification.duration || 5000
    setTimeout(() => {
      set(state => ({
        notifications: state.notifications.filter(n => n.id !== id),
      }))
    }, duration)
  },
  removeNotification: id =>
    set(state => ({
      notifications: state.notifications.filter(n => n.id !== id),
    })),
}))
