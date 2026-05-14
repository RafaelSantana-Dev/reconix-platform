import api from './api'
import { LoginRequest, LoginResponse, User } from '@/types'

export const authService = {
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await api.post<LoginResponse>('/auth/login', credentials)
    return response.data
  },

  async logout(): Promise<void> {
    await api.post('/auth/logout')
  },

  async refreshToken(refreshToken: string): Promise<LoginResponse> {
    const response = await api.post<LoginResponse>('/auth/refresh', { refreshToken })
    return response.data
  },

  async getCurrentUser(): Promise<User> {
    const response = await api.get<User>('/auth/me')
    return response.data
  },

  async register(data: {
    username: string
    email: string
    password: string
    tenantName: string
  }): Promise<void> {
    await api.post('/auth/register', data)
  },
}
