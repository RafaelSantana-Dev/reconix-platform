import { useState } from 'react'
import { useDropzone } from 'react-dropzone'
import { useMutation, useQuery } from '@tanstack/react-query'
import { ingestionService } from '@/services/ingestionService'
import { useAuthStore } from '@/store/authStore'
import { useNotificationStore } from '@/store/notificationStore'
import { Upload, FileText, CheckCircle, XCircle, Clock } from 'lucide-react'
import { clsx } from 'clsx'

const SOURCE_OPTIONS = [
  { value: 'BANK', label: 'Extrato Bancário' },
  { value: 'ERP', label: 'Sistema ERP' },
  { value: 'INVOICE', label: 'Notas Fiscais' },
  { value: 'PAYMENT_GATEWAY', label: 'Gateway de Pagamento' },
]

export default function UploadPage() {
  const { user } = useAuthStore()
  const { addNotification } = useNotificationStore()
  const [selectedSource, setSelectedSource] = useState('BANK')

  const { data: uploadHistory, refetch } = useQuery({
    queryKey: ['upload-history', user?.tenantId],
    queryFn: () => ingestionService.getUploadHistory(user!.tenantId),
    enabled: !!user?.tenantId,
  })

  const uploadMutation = useMutation({
    mutationFn: (file: File) =>
      ingestionService.uploadFile(user!.tenantId, selectedSource, file),
    onSuccess: () => {
      addNotification({
        type: 'success',
        message: 'Arquivo enviado com sucesso!',
      })
      refetch()
    },
    onError: () => {
      addNotification({
        type: 'error',
        message: 'Erro ao enviar arquivo. Tente novamente.',
      })
    },
  })

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop: acceptedFiles => {
      if (acceptedFiles.length > 0) {
        uploadMutation.mutate(acceptedFiles[0])
      }
    },
    accept: {
      'text/csv': ['.csv'],
      'application/vnd.ms-excel': ['.xls'],
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': ['.xlsx'],
      'application/xml': ['.xml'],
      'application/json': ['.json'],
      'application/x-ofx': ['.ofx'],
    },
    maxFiles: 1,
    disabled: uploadMutation.isPending,
  })

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Upload de Arquivos</h1>
        <p className="mt-2 text-gray-600">
          Envie extratos bancários, notas fiscais e registros do ERP
        </p>
      </div>

      {/* Source Selection */}
      <div className="card">
        <label className="label">Origem dos Dados</label>
        <select
          value={selectedSource}
          onChange={e => setSelectedSource(e.target.value)}
          className="input"
          disabled={uploadMutation.isPending}
        >
          {SOURCE_OPTIONS.map(option => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
      </div>

      {/* Dropzone */}
      <div
        {...getRootProps()}
        className={clsx(
          'card border-2 border-dashed cursor-pointer transition-all duration-200',
          isDragActive && 'border-primary-500 bg-primary-50',
          !isDragActive && 'border-gray-300 hover:border-primary-400',
          uploadMutation.isPending && 'opacity-50 cursor-not-allowed'
        )}
      >
        <input {...getInputProps()} />
        <div className="text-center py-12">
          <Upload
            className={clsx(
              'w-16 h-16 mx-auto mb-4',
              isDragActive ? 'text-primary-600' : 'text-gray-400'
            )}
          />
          {uploadMutation.isPending ? (
            <div>
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600 mx-auto mb-4"></div>
              <p className="text-lg font-medium text-gray-900">Enviando arquivo...</p>
            </div>
          ) : isDragActive ? (
            <p className="text-lg font-medium text-primary-600">Solte o arquivo aqui</p>
          ) : (
            <div>
              <p className="text-lg font-medium text-gray-900 mb-2">
                Arraste e solte um arquivo aqui
              </p>
              <p className="text-sm text-gray-600">
                ou clique para selecionar (CSV, XLSX, XML, JSON, OFX)
              </p>
            </div>
          )}
        </div>
      </div>

      {/* Upload History */}
      <div className="card">
        <h2 className="text-xl font-semibold text-gray-900 mb-4">Histórico de Uploads</h2>
        {uploadHistory && uploadHistory.length > 0 ? (
          <div className="space-y-3">
            {uploadHistory.map((upload: any) => (
              <div
                key={upload.id}
                className="flex items-center justify-between p-4 bg-gray-50 rounded-lg"
              >
                <div className="flex items-center space-x-3">
                  <FileText className="w-5 h-5 text-gray-600" />
                  <div>
                    <p className="font-medium text-gray-900">{upload.fileName}</p>
                    <p className="text-sm text-gray-600">
                      {new Date(upload.createdAt).toLocaleString('pt-BR')}
                    </p>
                  </div>
                </div>
                <div className="flex items-center space-x-2">
                  {upload.status === 'COMPLETED' && (
                    <span className="flex items-center text-green-600">
                      <CheckCircle className="w-5 h-5 mr-1" />
                      Concluído
                    </span>
                  )}
                  {upload.status === 'PROCESSING' && (
                    <span className="flex items-center text-yellow-600">
                      <Clock className="w-5 h-5 mr-1" />
                      Processando
                    </span>
                  )}
                  {upload.status === 'FAILED' && (
                    <span className="flex items-center text-red-600">
                      <XCircle className="w-5 h-5 mr-1" />
                      Falhou
                    </span>
                  )}
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p className="text-center text-gray-600 py-8">Nenhum arquivo enviado ainda</p>
        )}
      </div>
    </div>
  )
}
