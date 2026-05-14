import api from './api'
import { FileUploadResponse } from '@/types'

export const ingestionService = {
  async uploadFile(tenantId: string, source: string, file: File): Promise<FileUploadResponse> {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('tenantId', tenantId)
    formData.append('source', source)

    const response = await api.post<FileUploadResponse>('/ingestion/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return response.data
  },

  async getUploadHistory(tenantId: string) {
    const response = await api.get(`/ingestion/history`, {
      params: { tenantId },
    })
    return response.data
  },
}
