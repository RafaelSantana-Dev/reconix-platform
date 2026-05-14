/**
 * Constantes da aplicação
 */

export const API_BASE_URL = import.meta.env.VITE_API_URL || '/api'
export const WS_BASE_URL = import.meta.env.VITE_WS_URL || '/ws'

export const TRANSACTION_STATUS = {
  PENDING: 'Pendente',
  MATCHED: 'Conciliado',
  PARTIAL_MATCH: 'Parcialmente Conciliado',
  UNMATCHED: 'Não Conciliado',
} as const

export const FRAUD_RISK_LEVELS = {
  LOW: 'Baixo',
  MEDIUM: 'Médio',
  HIGH: 'Alto',
  CRITICAL: 'Crítico',
} as const

export const FRAUD_ALERT_STATUS = {
  PENDING: 'Pendente',
  UNDER_REVIEW: 'Em Revisão',
  CONFIRMED_FRAUD: 'Fraude Confirmada',
  FALSE_POSITIVE: 'Falso Positivo',
} as const

export const NOTIFICATION_CHANNELS = {
  EMAIL: 'Email',
  SLACK: 'Slack',
  WEBHOOK: 'Webhook',
  WEBSOCKET: 'WebSocket',
} as const

export const REPORT_TYPES = {
  DAILY: 'Diário',
  WEEKLY: 'Semanal',
  MONTHLY: 'Mensal',
  CUSTOM: 'Personalizado',
} as const

export const REPORT_FORMATS = {
  PDF: 'PDF',
  EXCEL: 'Excel',
} as const

export const FILE_SOURCES = {
  BANK: 'Extrato Bancário',
  ERP: 'Sistema ERP',
  INVOICE: 'Notas Fiscais',
  PAYMENT_GATEWAY: 'Gateway de Pagamento',
} as const

export const ALLOWED_FILE_EXTENSIONS = ['csv', 'xlsx', 'xls', 'xml', 'json', 'ofx']
export const MAX_FILE_SIZE_MB = 50

export const PAGINATION_DEFAULT_SIZE = 20
export const PAGINATION_SIZE_OPTIONS = [10, 20, 50, 100]

export const QUERY_STALE_TIME = 5 * 60 * 1000 // 5 minutes
export const QUERY_CACHE_TIME = 10 * 60 * 1000 // 10 minutes
