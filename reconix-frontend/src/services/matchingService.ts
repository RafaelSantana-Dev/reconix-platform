import api from './api'
import { MatchResult, PageRequest, PageResponse } from '@/types'

export const matchingService = {
  async getMatchResults(
    tenantId: string,
    params?: PageRequest & { status?: string }
  ): Promise<PageResponse<MatchResult>> {
    const response = await api.get<PageResponse<MatchResult>>('/matching/results', {
      params: { tenantId, ...params },
    })
    return response.data
  },

  async getMatchById(matchId: string): Promise<MatchResult> {
    const response = await api.get<MatchResult>(`/matching/results/${matchId}`)
    return response.data
  },

  async triggerMatching(tenantId: string): Promise<void> {
    await api.post('/matching/trigger', { tenantId })
  },
}
