import { useNotificationStore } from '@/store/notificationStore'
import Toast from './Toast'

export default function ToastContainer() {
  const { notifications } = useNotificationStore()

  return (
    <div className="fixed top-4 right-4 z-50 space-y-2">
      {notifications.map(notification => (
        <Toast key={notification.id} {...notification} />
      ))}
    </div>
  )
}
