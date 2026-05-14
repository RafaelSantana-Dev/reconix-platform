import { useQuery } from '@tanstack/react-query'
import { useAuthStore } from '@/store/authStore'
import { Bell, Mail, MessageSquare, Webhook, CheckCircle, XCircle } from 'lucide-react'
import { clsx } from 'clsx'

const CHANNEL_ICONS = {
  EMAIL: Mail,
  SLACK: MessageSquare,
  WEBHOOK: Webhook,
  WEBSOCKET: Bell,
}

export default function NotificationsPage() {
  const { user } = useAuthStore()

  // Mock data - substituir pela chamada real da API
  const { data: notifications } = useQuery({
    queryKey: ['notifications', user?.tenantId],
    queryFn: async () => {
      // Simulação - substituir por: notificationService.getNotifications(user!.tenantId)
      return []
    },
    enabled: !!user?.tenantId,
  })

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Notificações</h1>
        <p className="mt-2 text-gray-600">Histórico de notificações enviadas</p>
      </div>

      {/* Notifications List */}
      <div className="card">
        <div className="space-y-3">
          {notifications && notifications.length > 0 ? (
            notifications.map((notification: any) => {
              const Icon = CHANNEL_ICONS[notification.channel as keyof typeof CHANNEL_ICONS]
              return (
                <div
                  key={notification.id}
                  className={clsx(
                    'flex items-start justify-between p-4 rounded-lg border-2',
                    notification.status === 'SENT'
                      ? 'bg-green-50 border-green-200'
                      : notification.status === 'FAILED'
                      ? 'bg-red-50 border-red-200'
                      : 'bg-yellow-50 border-yellow-200'
                  )}
                >
                  <div className="flex items-start space-x-3 flex-1">
                    <Icon className="w-5 h-5 text-gray-600 mt-1" />
                    <div className="flex-1">
                      <div className="flex items-center space-x-2 mb-1">
                        <span className="font-medium text-gray-900">{notification.type}</span>
                        <span className="text-xs px-2 py-1 bg-white rounded-full">
                          {notification.channel}
                        </span>
                      </div>
                      <p className="text-sm text-gray-700">{notification.message}</p>
                      <p className="text-xs text-gray-500 mt-1">
                        {new Date(notification.createdAt).toLocaleString('pt-BR')}
                      </p>
                    </div>
                  </div>
                  <div className="ml-4">
                    {notification.status === 'SENT' ? (
                      <CheckCircle className="w-5 h-5 text-green-600" />
                    ) : notification.status === 'FAILED' ? (
                      <XCircle className="w-5 h-5 text-red-600" />
                    ) : (
                      <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-yellow-600"></div>
                    )}
                  </div>
                </div>
              )
            })
          ) : (
            <div className="text-center py-12">
              <Bell className="w-16 h-16 text-gray-300 mx-auto mb-4" />
              <p className="text-gray-600">Nenhuma notificação encontrada</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
