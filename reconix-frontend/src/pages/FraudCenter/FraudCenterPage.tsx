import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { fraudService } from '@/services/fraudService'
import { useAuthStore } from '@/store/authStore'
import { useNotificationStore } from '@/store/notificationStore'
import { AlertTriangle, Shield, Eye, CheckCircle, XCircle } from 'lucide-react'
import { clsx } from 'clsx'
import { FraudAlert } from '@/types'

const RISK_LEVELS = [
  { value: '', label: 'Todos os Níveis' },
  { value: 'LOW', label: 'Baixo' },
  { value: 'MEDIUM', label: 'Médio' },
  { value: 'HIGH', label: 'Alto' },
  { value: 'CRITICAL', label: 'Crítico' },
]

const STATUS_OPTIONS = [
  { value: '', label: 'Todos os Status' },
  { value: 'PENDING', label: 'Pendente' },
  { value: 'UNDER_REVIEW', label: 'Em Revisão' },
  { value: 'CONFIRMED_FRAUD', label: 'Fraude Confirmada' },
  { value: 'FALSE_POSITIVE', label: 'Falso Positivo' },
]

export default function FraudCenterPage() {
  const { user } = useAuthStore()
  const { addNotification } = useNotificationStore()
  const queryClient = useQueryClient()
  const [riskFilter, setRiskFilter] = useState('')
  const [statusFilter, setStatusFilter] = useState('PENDING')
  const [selectedAlert, setSelectedAlert] = useState<FraudAlert | null>(null)

  const { data: alerts } = useQuery({
    queryKey: ['fraud-alerts', user?.tenantId, riskFilter, statusFilter],
    queryFn: () =>
      fraudService.getAlerts(user!.tenantId, {
        riskLevel: riskFilter,
        status: statusFilter,
        page: 0,
        size: 50,
      }),
    enabled: !!user?.tenantId,
  })

  const reviewMutation = useMutation({
    mutationFn: ({
      alertId,
      status,
    }: {
      alertId: string
      status: 'UNDER_REVIEW' | 'CONFIRMED_FRAUD' | 'FALSE_POSITIVE'
    }) => fraudService.reviewAlert(alertId, status),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['fraud-alerts'] })
      setSelectedAlert(null)
      addNotification({
        type: 'success',
        message: 'Alerta revisado com sucesso!',
      })
    },
  })

  const getRiskColor = (level: string) => {
    switch (level) {
      case 'CRITICAL':
        return 'bg-red-100 text-red-800 border-red-200'
      case 'HIGH':
        return 'bg-orange-100 text-orange-800 border-orange-200'
      case 'MEDIUM':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200'
      case 'LOW':
        return 'bg-blue-100 text-blue-800 border-blue-200'
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200'
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Centro de Fraudes</h1>
        <p className="mt-2 text-gray-600">Monitoramento e análise de alertas de fraude</p>
      </div>

      {/* Filters */}
      <div className="card">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="label">Nível de Risco</label>
            <select
              value={riskFilter}
              onChange={e => setRiskFilter(e.target.value)}
              className="input"
            >
              {RISK_LEVELS.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="label">Status</label>
            <select
              value={statusFilter}
              onChange={e => setStatusFilter(e.target.value)}
              className="input"
            >
              {STATUS_OPTIONS.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Alerts Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {alerts?.content.map(alert => (
          <div
            key={alert.id}
            className={clsx('card border-2 cursor-pointer hover:shadow-md transition-shadow', getRiskColor(alert.riskLevel))}
            onClick={() => setSelectedAlert(alert)}
          >
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center space-x-2">
                <AlertTriangle className="w-6 h-6" />
                <span className="font-semibold text-lg">{alert.riskLevel}</span>
              </div>
              <span className="text-sm font-medium px-3 py-1 bg-white rounded-full">
                {alert.status}
              </span>
            </div>

            <div className="space-y-2">
              <div>
                <p className="text-sm font-medium text-gray-700">Transação ID:</p>
                <p className="text-sm text-gray-900">{alert.transactionId}</p>
              </div>
              <div>
                <p className="text-sm font-medium text-gray-700">Regras Acionadas:</p>
                <div className="flex flex-wrap gap-1 mt-1">
                  {alert.rulesTriggered.map(rule => (
                    <span
                      key={rule}
                      className="text-xs px-2 py-1 bg-white rounded-full"
                    >
                      {rule}
                    </span>
                  ))}
                </div>
              </div>
              <div>
                <p className="text-sm font-medium text-gray-700">Score:</p>
                <p className="text-sm text-gray-900">{(alert.score * 100).toFixed(0)}%</p>
              </div>
              <div>
                <p className="text-sm text-gray-600">
                  {new Date(alert.createdAt).toLocaleString('pt-BR')}
                </p>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Alert Detail Modal */}
      {selectedAlert && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <div className="p-6">
              <div className="flex items-center justify-between mb-6">
                <h2 className="text-2xl font-bold text-gray-900">Detalhes do Alerta</h2>
                <button
                  onClick={() => setSelectedAlert(null)}
                  className="text-gray-400 hover:text-gray-600"
                >
                  <XCircle className="w-6 h-6" />
                </button>
              </div>

              <div className="space-y-4">
                <div className={clsx('p-4 rounded-lg border-2', getRiskColor(selectedAlert.riskLevel))}>
                  <p className="font-semibold text-lg">{selectedAlert.riskLevel} RISK</p>
                </div>

                <div>
                  <p className="font-medium text-gray-700">Transação ID:</p>
                  <p className="text-gray-900">{selectedAlert.transactionId}</p>
                </div>

                <div>
                  <p className="font-medium text-gray-700">Regras Acionadas:</p>
                  <ul className="list-disc list-inside mt-2 space-y-1">
                    {selectedAlert.rulesTriggered.map(rule => (
                      <li key={rule} className="text-gray-900">
                        {rule}
                      </li>
                    ))}
                  </ul>
                </div>

                <div className="flex space-x-3 pt-4">
                  <button
                    onClick={() =>
                      reviewMutation.mutate({
                        alertId: selectedAlert.id,
                        status: 'UNDER_REVIEW',
                      })
                    }
                    className="btn btn-secondary flex items-center space-x-2"
                  >
                    <Eye className="w-4 h-4" />
                    <span>Em Revisão</span>
                  </button>
                  <button
                    onClick={() =>
                      reviewMutation.mutate({
                        alertId: selectedAlert.id,
                        status: 'CONFIRMED_FRAUD',
                      })
                    }
                    className="btn btn-danger flex items-center space-x-2"
                  >
                    <Shield className="w-4 h-4" />
                    <span>Confirmar Fraude</span>
                  </button>
                  <button
                    onClick={() =>
                      reviewMutation.mutate({
                        alertId: selectedAlert.id,
                        status: 'FALSE_POSITIVE',
                      })
                    }
                    className="btn btn-primary flex items-center space-x-2"
                  >
                    <CheckCircle className="w-4 h-4" />
                    <span>Falso Positivo</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
