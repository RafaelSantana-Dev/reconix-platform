import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard,
  Upload,
  GitCompare,
  Shield,
  FileText,
  Bell,
  Settings,
} from 'lucide-react'
import { clsx } from 'clsx'

const navigation = [
  { name: 'Dashboard', href: '/dashboard', icon: LayoutDashboard },
  { name: 'Upload', href: '/upload', icon: Upload },
  { name: 'Conciliação', href: '/reconciliation', icon: GitCompare },
  { name: 'Centro de Fraudes', href: '/fraud-center', icon: Shield },
  { name: 'Relatórios', href: '/reports', icon: FileText },
  { name: 'Notificações', href: '/notifications', icon: Bell },
  { name: 'Configurações', href: '/settings', icon: Settings },
]

export default function Sidebar() {
  return (
    <div className="w-64 bg-white border-r border-gray-200">
      <div className="flex items-center justify-center h-16 border-b border-gray-200">
        <h1 className="text-2xl font-bold text-primary-600">Reconix</h1>
      </div>
      <nav className="mt-6 px-3">
        {navigation.map(item => (
          <NavLink
            key={item.name}
            to={item.href}
            className={({ isActive }) =>
              clsx(
                'flex items-center px-4 py-3 mb-2 rounded-lg transition-colors duration-200',
                isActive
                  ? 'bg-primary-50 text-primary-700 font-medium'
                  : 'text-gray-700 hover:bg-gray-100'
              )
            }
          >
            <item.icon className="w-5 h-5 mr-3" />
            {item.name}
          </NavLink>
        ))}
      </nav>
    </div>
  )
}
