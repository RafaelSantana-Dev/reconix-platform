import { X, CheckCircle, AlertCircle, AlertTriangle, Info } from 'lucide-react'
import { useNotificationStore } from '@/store/notificationStore'
import { clsx } from 'clsx'

interface ToastProps {
  id: string
  type: 'success' | 'error' | 'warning' | 'info'
  message: string
}

const icons = {
  success: CheckCircle,
  error: AlertCircle,
  warning: AlertTriangle,
  info: Info,
}

const colors = {
  success: 'bg-green-50 border-green-200 text-green-800',
  error: 'bg-red-50 border-red-200 text-red-800',
  warning: 'bg-yellow-50 border-yellow-200 text-yellow-800',
  info: 'bg-blue-50 border-blue-200 text-blue-800',
}

export default function Toast({ id, type, message }: ToastProps) {
  const { removeNotification } = useNotificationStore()
  const Icon = icons[type]

  return (
    <div
      className={clsx(
        'flex items-center p-4 rounded-lg border shadow-lg min-w-[300px] max-w-md',
        colors[type]
      )}
    >
      <Icon className="w-5 h-5 mr-3 flex-shrink-0" />
      <p className="flex-1 text-sm font-medium">{message}</p>
      <button
        onClick={() => removeNotification(id)}
        className="ml-3 flex-shrink-0 hover:opacity-70 transition-opacity"
      >
        <X className="w-4 h-4" />
      </button>
    </div>
  )
}
