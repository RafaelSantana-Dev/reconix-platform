import api from './api'
import { DashboardKPIs, Report } from '@/types'

export const reportingService = {
  async getDashboardKPIs(tenantId: string): Promise<DashboardKPIs> {
    const response = await api.get<DashboardKPIs>('/dashboard/kpis', {
      params: { tenantId },
    })
    return response.data
  },

  async generateReport(
    tenantId: string,
    type: 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'CUSTOM',
    format: 'PDF' | 'EXCEL',
    startDate?: string,
    endDate?: string
  ): Promise<Report> {
    const response = await api.post<Report>(`/reports/generate/${format.toLowerCase()}`, {
      tenantId,
      type,
      startDate,
      endDate,
    })
    return response.data
  },

  async getReports(tenantId: string): Promise<Report[]> {
    const response = await api.get<Report[]>('/reports', {
      params: { tenantId },
    })
    return response.data
  },

  async downloadReport(reportId: string): Promise<Blob> {
    const response = await api.get(`/reports/${reportId}/download`, {
      responseType: 'blob',
    })
    return response.data
  },

  async searchTransactions(tenantId: string, query: string) {
    const response = await api.get('/search/transactions', {
      params: { tenantId, query },
    })
    return response.data
  },
}
