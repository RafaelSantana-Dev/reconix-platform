import { useQuery } from '@tanstack/react-query'
import { reportingService } from '@/services/reportingService'
import { useAuthStore } from '@/store/authStore'
import { TrendingUp, TrendingDown, DollarSign, AlertTriangle } from 'lucide-react'
import { useWebSocket } from '@/hooks/useWebSocket'
import { useNotificationStore } from '@/store/notificationStore'
import { FraudAlert } from '@/types'

export default function DashboardPage() {
  const { user } = useAuthStore()
  const { addNotification } = useNotificationStore()

  const { data: kpis, refetch } = useQuery({
    queryKey: ['dashboard-kpis', user?.tenantId],
    queryFn: () => reportingService.getDashboardKPIs(user!.tenantId),
    enabled: !!user?.tenantId,
    refetchInterval: 30000, // Refetch every 30 seconds
  })

  // WebSocket para alertas em tempo real
  useWebSocket<FraudAlert>('fraud-alerts', alert => {
    addNotification({
      type: 'warning',
      message: `Novo alerta de fraude: ${alert.riskLevel}`,
    })
    refetch()
  })

  if (!kpis) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Carregando dashboard...</p>
        </div>
      </div>
    )
  }

  const stats = [
    {
      name: 'Total de Transações',
      value: kpis.totalTransactions.toLocaleString('pt-BR'),
      icon: TrendingUp,
      color: 'text-blue-600',
      bgColor: 'bg-blue-100',
    },
    {
      name: 'Taxa de Conciliação',
      value: `${kpis.reconciliationRate.toFixed(1)}%`,
      icon: TrendingUp,
      color: 'text-green-600',
      bgColor: 'bg-green-100',
    },
    {
      name: 'Valor Total',
      value: `R$ ${kpis.totalAmount.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`,
      icon: DollarSign,
      color: 'text-purple-600',
      bgColor: 'bg-purple-100',
    },
    {
      name: 'Alertas de Fraude',
      value: kpis.fraudAlerts.toLocaleString('pt-BR'),
      icon: AlertTriangle,
      color: 'text-red-600',
      bgColor: 'bg-red-100',
    },
  ]

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="mt-2 text-gray-600">Visão geral da reconciliação financeira</p>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map(stat => (
          <div key={stat.name} className="card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">{stat.name}</p>
                <p className="mt-2 text-3xl font-bold text-gray-900">{stat.value}</p>
              </div>
              <div className={`p-3 rounded-full ${stat.bgColor}`}>
                <stat.icon className={`w-6 h-6 ${stat.color}`} />
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Status Cards */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Transações Conciliadas</h3>
          <div className="space-y-2">
            <div className="flex justify-between items-center">
              <span className="text-sm text-gray-600">Matched</span>
              <span className="text-sm font-semibold text-green-600">
                {kpis.matchedTransactions.toLocaleString('pt-BR')}
              </span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div
                className="bg-green-600 h-2 rounded-full"
                style={{
                  width: `${(kpis.matchedTransactions / kpis.totalTransactions) * 100}%`,
                }}
              ></div>
            </div>
          </div>
        </div>

        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Parcialmente Conciliadas</h3>
          <div className="space-y-2">
            <div className="flex justify-between items-center">
              <span className="text-sm text-gray-600">Partial Match</span>
              <span className="text-sm font-semibold text-yellow-600">
                {kpis.partialMatchTransactions.toLocaleString('pt-BR')}
              </span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div
                className="bg-yellow-600 h-2 rounded-full"
                style={{
                  width: `${(kpis.partialMatchTransactions / kpis.totalTransactions) * 100}%`,
                }}
              ></div>
            </div>
          </div>
        </div>

        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Não Conciliadas</h3>
          <div className="space-y-2">
            <div className="flex justify-between items-center">
              <span className="text-sm text-gray-600">Unmatched</span>
              <span className="text-sm font-semibold text-red-600">
                {kpis.unmatchedTransactions.toLocaleString('pt-BR')}
              </span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div
                className="bg-red-600 h-2 rounded-full"
                style={{
                  width: `${(kpis.unmatchedTransactions / kpis.totalTransactions) * 100}%`,
                }}
              ></div>
            </div>
          </div>
        </div>
      </div>

      {/* Alertas Críticos */}
      {kpis.criticalAlerts > 0 && (
        <div className="card bg-red-50 border-red-200">
          <div className="flex items-center space-x-3">
            <AlertTriangle className="w-6 h-6 text-red-600" />
            <div>
              <h3 className="text-lg font-semibold text-red-900">Alertas Críticos</h3>
              <p className="text-sm text-red-700">
                Existem {kpis.criticalAlerts} alertas críticos que requerem atenção imediata.
              </p>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
