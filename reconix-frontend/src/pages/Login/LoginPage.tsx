import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useAuth } from '@/hooks/useAuth'
import { Navigate } from 'react-router-dom'
import { LogIn } from 'lucide-react'

const loginSchema = z.object({
  username: z.string().min(3, 'Usuário deve ter no mínimo 3 caracteres'),
  password: z.string().min(6, 'Senha deve ter no mínimo 6 caracteres'),
})

type LoginFormData = z.infer<typeof loginSchema>

export default function LoginPage() {
  const { login, isLoggingIn, isAuthenticated } = useAuth()
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  })

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />
  }

  const onSubmit = (data: LoginFormData) => {
    login(data)
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary-500 to-primary-700">
      <div className="bg-white p-8 rounded-2xl shadow-2xl w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-primary-600 mb-2">Reconix</h1>
          <p className="text-gray-600">Plataforma de Reconciliação Financeira</p>
        </div>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
          <div>
            <label htmlFor="username" className="label">
              Usuário
            </label>
            <input
              id="username"
              type="text"
              {...register('username')}
              className="input"
              placeholder="Digite seu usuário"
            />
            {errors.username && (
              <p className="mt-1 text-sm text-red-600">{errors.username.message}</p>
            )}
          </div>

          <div>
            <label htmlFor="password" className="label">
              Senha
            </label>
            <input
              id="password"
              type="password"
              {...register('password')}
              className="input"
              placeholder="Digite sua senha"
            />
            {errors.password && (
              <p className="mt-1 text-sm text-red-600">{errors.password.message}</p>
            )}
          </div>

          <button
            type="submit"
            disabled={isLoggingIn}
            className="w-full btn btn-primary flex items-center justify-center space-x-2"
          >
            {isLoggingIn ? (
              <span>Entrando...</span>
            ) : (
              <>
                <LogIn className="w-5 h-5" />
                <span>Entrar</span>
              </>
            )}
          </button>
        </form>

        <div className="mt-6 text-center text-sm text-gray-600">
          <p>Usuário demo: admin / senha: admin123</p>
        </div>
      </div>
    </div>
  )
}
