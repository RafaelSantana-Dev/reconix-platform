import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { matchingService } from '@/services/matchingService'
import { useAuthStore } from '@/store/authStore'
import { CheckCircle, AlertCircle, XCircle } from 'lucide-react'
import { clsx } from 'clsx'

const STATUS_OPTIONS = [
  { value: '', label: 'Todos' },
  { value: 'MATCHED', label: 'Conciliados' },
  { value: 'PARTIAL_MATCH', label: 'Parcialmente Conciliados' },
  { value: 'UNMATCHED', label: 'Não Conciliados' },
]

export default function ReconciliationPage() {
  const { user } = useAuthStore()
  const [statusFilter, setStatusFilter] = useState('')
  const [page, setPage] = useState(0)

  const { data: matchResults } = useQuery({
    queryKey: ['match-results', user?.tenantId, statusFilter, page],
    queryFn: () =>
      matchingService.getMatchResults(user!.tenantId, {
        status: statusFilter,
        page,
        size: 20,
      }),
    enabled: !!user?.tenantId,
  })

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'MATCHED':
        return <CheckCircle className="w-5 h-5 text-green-600" />
      case 'PARTIAL_MATCH':
        return <AlertCircle className="w-5 h-5 text-yellow-600" />
      case 'UNMATCHED':
        return <XCircle className="w-5 h-5 text-red-600" />
      default:
        return null
    }
  }

  const getScoreColor = (score: number) => {
    if (score >= 0.85) return 'text-green-600 bg-green-100'
    if (score >= 0.5) return 'text-yellow-600 bg-yellow-100'
    return 'text-red-600 bg-red-100'
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Conciliação</h1>
        <p className="mt-2 text-gray-600">Resultados do matching de transações</p>
      </div>

      {/* Filters */}
      <div className="card">
        <label className="label">Filtrar por Status</label>
        <select
          value={statusFilter}
          onChange={e => setStatusFilter(e.target.value)}
          className="input max-w-xs"
        >
          {STATUS_OPTIONS.map(option => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
      </div>

      {/* Results Table */}
      <div className="card overflow-hidden p-0">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Score
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Transação A
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Transação B
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Data
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {matchResults?.content.map(match => (
                <tr key={match.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center space-x-2">
                      {getStatusIcon(match.status)}
                      <span className="text-sm font-medium text-gray-900">{match.status}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span
                      className={clsx(
                        'px-3 py-1 rounded-full text-sm font-semibold',
                        getScoreColor(match.score)
                      )}
                    >
                      {(match.score * 100).toFixed(0)}%
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {match.transactionAId}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {match.transactionBId}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                    {new Date(match.createdAt).toLocaleDateString('pt-BR')}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Pagination */}
        {matchResults && matchResults.totalPages > 1 && (
          <div className="px-6 py-4 flex items-center justify-between border-t border-gray-200">
            <button
              onClick={() => setPage(p => Math.max(0, p - 1))}
              disabled={page === 0}
              className="btn btn-secondary disabled:opacity-50"
            >
              Anterior
            </button>
            <span className="text-sm text-gray-700">
              Página {page + 1} de {matchResults.totalPages}
            </span>
            <button
              onClick={() => setPage(p => p + 1)}
              disabled={page >= matchResults.totalPages - 1}
              className="btn btn-secondary disabled:opacity-50"
            >
              Próxima
            </button>
          </div>
        )}
      </div>
    </div>
  )
}
