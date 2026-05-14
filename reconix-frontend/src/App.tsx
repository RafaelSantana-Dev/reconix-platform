import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useAuthStore } from './store/authStore'
import Layout from './components/Layout/Layout'
import LoginPage from './pages/Login/LoginPage'
import DashboardPage from './pages/Dashboard/DashboardPage'
import UploadPage from './pages/Upload/UploadPage'
import ReconciliationPage from './pages/Reconciliation/ReconciliationPage'
import FraudCenterPage from './pages/FraudCenter/FraudCenterPage'
import ReportsPage from './pages/Reports/ReportsPage'
import NotificationsPage from './pages/Notifications/NotificationsPage'
import SettingsPage from './pages/Settings/SettingsPage'
import ToastContainer from './components/Toast/ToastContainer'

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuthStore()
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />
}

function App() {
  return (
    <BrowserRouter>
      <ToastContainer />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/"
          element={
            <PrivateRoute>
              <Layout />
            </PrivateRoute>
          }
        >
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="upload" element={<UploadPage />} />
          <Route path="reconciliation" element={<ReconciliationPage />} />
          <Route path="fraud-center" element={<FraudCenterPage />} />
          <Route path="reports" element={<ReportsPage />} />
          <Route path="notifications" element={<NotificationsPage />} />
          <Route path="settings" element={<SettingsPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
