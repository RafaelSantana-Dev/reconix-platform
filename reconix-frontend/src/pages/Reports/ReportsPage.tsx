import { useState } from 'react'
import { useMutation, useQuery } from '@tanstack/react-query'
import { reportingService } from '@/services/reportingService'
import { useAuthStore } from '@/store/authStore'
import { useNotificationStore } from '@/store/notificationStore'
import { FileText, Download } from 'lucide-react'

export default function ReportsPage() {
  const { user } = useAuthStore()
  const { addNotification } = useNotificationStore()
  const [reportType, setReportType] = useState<'DAILY' | 'WEEKLY' | 'MONTHLY'>('DAILY')
  const [format, setFormat] = useState<'PDF' | 'EXCEL'>('PDF')

  const { data: reports, refetch } = useQuery({
    queryKey: ['reports', user?.tenantId],
    queryFn: () => reportingService.getReports(user!.tenantId),
    enabled: !!user?.tenantId,
  })

  const generateMutation = useMutation({
    mutationFn: () => reportingService.generateReport(user!.tenantId, reportType, format),
    onSuccess: () => {
      addNotification({
        type: 'success',
        message: 'Relatório gerado com sucesso!',
      })
      refetch()
    },
  })

  const downloadReport = async (reportId: string, fileName: string) => {
    try {
      const blob = await reportingService.downloadReport(reportId)
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = fileName
      document.body.appendChild(a)
      a.click()
      window.URL.revokeObjectURL(url)
      document.body.removeChild(a)
    } catch (error) {
      addNotification({
        type: 'error',
        message: 'Erro ao baixar relatório',
      })
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Relatórios</h1>
        <p className="mt-2 text-gray-600">Gere e baixe relatórios de conciliação</p>
      </div>

      {/* Generate Report */}
      <div className="card">
        <h2 className="text-xl font-semibold text-gray-900 mb-4">Gerar Novo Relatório</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="label">Tipo</label>
            <select
              value={reportType}
              onChange={e => setReportType(e.target.value as any)}
              className="input"
            >
              <option value="DAILY">Diário</option>
              <option value="WEEKLY">Semanal</option>
              <option value="MONTHLY">Mensal</option>
            </select>
          </div>
          <div>
            <label className="label">Formato</label>
            <select value={format} onChange={e => setFormat(e.target.value as any)} className="input">
              <option value="PDF">PDF</option>
              <option value="EXCEL">Excel</option>
            </select>
          </div>
          <div className="flex items-end">
            <button
              onClick={() => generateMutation.mutate()}
              disabled={generateMutation.isPending}
              className="btn btn-primary w-full"
            >
              {generateMutation.isPending ? 'Gerando...' : 'Gerar Relatório'}
            </button>
          </div>
        </div>
      </div>

      {/* Reports List */}
      <div className="card">
        <h2 className="text-xl font-semibold text-gray-900 mb-4">Relatórios Gerados</h2>
        <div className="space-y-3">
          {reports?.map((report: any) => (
            <div
              key={report.id}
              className="flex items-center justify-between p-4 bg-gray-50 rounded-lg"
            >
              <div className="flex items-center space-x-3">
                <FileText className="w-5 h-5 text-gray-600" />
                <div>
                  <p className="font-medium text-gray-900">
                    Relatório {report.type} - {report.format}
                  </p>
                  <p className="text-sm text-gray-600">
                    {new Date(report.createdAt).toLocaleString('pt-BR')}
                  </p>
                </div>
              </div>
              {report.status === 'READY' && (
                <button
                  onClick={() => downloadReport(report.id, `report-${report.id}.${report.format.toLowerCase()}`)}
                  className="btn btn-secondary flex items-center space-x-2"
                >
                  <Download className="w-4 h-4" />
                  <span>Baixar</span>
                </button>
              )}
              {report.status === 'GENERATING' && (
                <span className="text-sm text-yellow-600">Gerando...</span>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
