import { useMutation, useQuery } from '@tanstack/react-query'
import { authService } from '@/services/authService'
import { useAuthStore } from '@/store/authStore'
import { useNotificationStore } from '@/store/notificationStore'
import { LoginRequest } from '@/types'

export function useAuth() {
  const { setAuth, logout: logoutStore, isAuthenticated, user } = useAuthStore()
  const { addNotification } = useNotificationStore()

  const loginMutation = useMutation({
    mutationFn: (credentials: LoginRequest) => authService.login(credentials),
    onSuccess: data => {
      setAuth(data.user, data.accessToken, data.refreshToken)
      addNotification({
        type: 'success',
        message: 'Login realizado com sucesso!',
      })
    },
    onError: () => {
      addNotification({
        type: 'error',
        message: 'Erro ao fazer login. Verifique suas credenciais.',
      })
    },
  })

  const logoutMutation = useMutation({
    mutationFn: () => authService.logout(),
    onSuccess: () => {
      logoutStore()
      addNotification({
        type: 'success',
        message: 'Logout realizado com sucesso!',
      })
    },
  })

  const { data: currentUser } = useQuery({
    queryKey: ['currentUser'],
    queryFn: () => authService.getCurrentUser(),
    enabled: isAuthenticated,
  })

  return {
    login: loginMutation.mutate,
    logout: logoutMutation.mutate,
    isLoggingIn: loginMutation.isPending,
    isAuthenticated,
    user: currentUser || user,
  }
}
