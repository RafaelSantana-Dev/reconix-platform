import api from './api'
import { FraudAlert, PageRequest, PageResponse } from '@/types'

export const fraudService = {
  async getAlerts(
    tenantId: string,
    params?: PageRequest & { status?: string; riskLevel?: string }
  ): Promise<PageResponse<FraudAlert>> {
    const response = await api.get<PageResponse<FraudAlert>>('/fraud/alerts', {
      params: { tenantId, ...params },
    })
    return response.data
  },

  async getAlertById(alertId: string): Promise<FraudAlert> {
    const response = await api.get<FraudAlert>(`/fraud/alerts/${alertId}`)
    return response.data
  },

  async reviewAlert(
    alertId: string,
    status: 'UNDER_REVIEW' | 'CONFIRMED_FRAUD' | 'FALSE_POSITIVE',
    notes?: string
  ): Promise<void> {
    await api.put(`/fraud/alerts/${alertId}/review`, { status, notes })
  },

  async getStatistics(tenantId: string) {
    const response = await api.get('/fraud/statistics', {
      params: { tenantId },
    })
    return response.data
  },
}
