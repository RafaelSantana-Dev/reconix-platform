// Auth Types
export interface User {
  id: string
  username: string
  email: string
  tenantId: string
  roles: string[]
}

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  accessToken: string
  refreshToken: string
  expiresIn: number
  user: User
}

// Transaction Types
export interface Transaction {
  id: string
  tenantId: string
  externalId: string
  amount: number
  currency: string
  description: string
  transactionDate: string
  source: 'BANK' | 'ERP' | 'INVOICE' | 'PAYMENT_GATEWAY'
  status: 'PENDING' | 'MATCHED' | 'PARTIAL_MATCH' | 'UNMATCHED'
  createdAt: string
}

// Matching Types
export interface MatchResult {
  id: string
  tenantId: string
  transactionAId: string
  transactionBId: string
  score: number
  status: 'MATCHED' | 'PARTIAL_MATCH' | 'UNMATCHED'
  strategies: StrategyResult[]
  createdAt: string
}

export interface StrategyResult {
  strategyName: string
  score: number
  weight: number
  details: Record<string, unknown>
}

// Fraud Types
export interface FraudAlert {
  id: string
  tenantId: string
  transactionId: string
  riskLevel: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  status: 'PENDING' | 'UNDER_REVIEW' | 'CONFIRMED_FRAUD' | 'FALSE_POSITIVE'
  rulesTriggered: string[]
  score: number
  details: Record<string, unknown>
  createdAt: string
  reviewedAt?: string
  reviewedBy?: string
}

// Notification Types
export interface Notification {
  id: string
  tenantId: string
  type: 'FRAUD_ALERT' | 'MATCH_FOUND' | 'REPORT_READY' | 'SYSTEM'
  channel: 'EMAIL' | 'SLACK' | 'WEBHOOK' | 'WEBSOCKET'
  status: 'PENDING' | 'SENT' | 'FAILED'
  message: string
  createdAt: string
  sentAt?: string
}

// Report Types
export interface Report {
  id: string
  tenantId: string
  type: 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'CUSTOM'
  format: 'PDF' | 'EXCEL'
  status: 'GENERATING' | 'READY' | 'FAILED'
  fileUrl?: string
  createdAt: string
  generatedAt?: string
}

export interface DashboardKPIs {
  totalTransactions: number
  matchedTransactions: number
  unmatchedTransactions: number
  partialMatchTransactions: number
  reconciliationRate: number
  totalAmount: number
  matchedAmount: number
  unmatchedAmount: number
  fraudAlerts: number
  criticalAlerts: number
}

// File Upload Types
export interface FileUploadRequest {
  tenantId: string
  source: string
  file: File
}

export interface FileUploadResponse {
  fileId: string
  fileName: string
  fileSize: number
  status: 'UPLOADED' | 'PROCESSING' | 'COMPLETED' | 'FAILED'
  message: string
}

// Pagination Types
export interface PageRequest {
  page: number
  size: number
  sort?: string
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}
