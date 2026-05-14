import { useState } from 'react'
import { useAuthStore } from '@/store/authStore'
import { useNotificationStore } from '@/store/notificationStore'
import { Settings, User, Bell, Shield } from 'lucide-react'

export default function SettingsPage() {
  const { user } = useAuthStore()
  const { addNotification } = useNotificationStore()
  const [emailNotifications, setEmailNotifications] = useState(true)
  const [slackNotifications, setSlackNotifications] = useState(false)
  const [slackWebhook, setSlackWebhook] = useState('')

  const handleSave = () => {
    addNotification({
      type: 'success',
      message: 'Configurações salvas com sucesso!',
    })
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Configurações</h1>
        <p className="mt-2 text-gray-600">Gerencie suas preferências e configurações</p>
      </div>

      {/* Profile Section */}
      <div className="card">
        <div className="flex items-center space-x-3 mb-6">
          <User className="w-6 h-6 text-primary-600" />
          <h2 className="text-xl font-semibold text-gray-900">Perfil</h2>
        </div>
        <div className="space-y-4">
          <div>
            <label className="label">Nome de Usuário</label>
            <input type="text" value={user?.username || ''} disabled className="input bg-gray-50" />
          </div>
          <div>
            <label className="label">Email</label>
            <input type="email" value={user?.email || ''} disabled className="input bg-gray-50" />
          </div>
          <div>
            <label className="label">Tenant ID</label>
            <input type="text" value={user?.tenantId || ''} disabled className="input bg-gray-50" />
          </div>
          <div>
            <label className="label">Roles</label>
            <div className="flex flex-wrap gap-2 mt-2">
              {user?.roles.map(role => (
                <span
                  key={role}
                  className="px-3 py-1 bg-primary-100 text-primary-700 rounded-full text-sm font-medium"
                >
                  {role}
                </span>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Notification Settings */}
      <div className="card">
        <div className="flex items-center space-x-3 mb-6">
          <Bell className="w-6 h-6 text-primary-600" />
          <h2 className="text-xl font-semibold text-gray-900">Notificações</h2>
        </div>
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="font-medium text-gray-900">Notificações por Email</p>
              <p className="text-sm text-gray-600">Receba alertas de fraude por email</p>
            </div>
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={emailNotifications}
                onChange={e => setEmailNotifications(e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </div>

          <div className="flex items-center justify-between">
            <div>
              <p className="font-medium text-gray-900">Notificações no Slack</p>
              <p className="text-sm text-gray-600">Receba alertas no Slack</p>
            </div>
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={slackNotifications}
                onChange={e => setSlackNotifications(e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </div>

          {slackNotifications && (
            <div>
              <label className="label">Slack Webhook URL</label>
              <input
                type="url"
                value={slackWebhook}
                onChange={e => setSlackWebhook(e.target.value)}
                placeholder="https://hooks.slack.com/services/..."
                className="input"
              />
            </div>
          )}
        </div>
      </div>

      {/* Security Settings */}
      <div className="card">
        <div className="flex items-center space-x-3 mb-6">
          <Shield className="w-6 h-6 text-primary-600" />
          <h2 className="text-xl font-semibold text-gray-900">Segurança</h2>
        </div>
        <div className="space-y-4">
          <button className="btn btn-secondary">Alterar Senha</button>
          <button className="btn btn-secondary">Gerenciar API Keys</button>
        </div>
      </div>

      {/* Save Button */}
      <div className="flex justify-end">
        <button onClick={handleSave} className="btn btn-primary">
          Salvar Configurações
        </button>
      </div>
    </div>
  )
}
