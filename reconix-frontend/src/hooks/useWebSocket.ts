import { useEffect, useRef, useState } from 'react'
import { Client, IMessage } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { useAuthStore } from '@/store/authStore'

export function useWebSocket<T>(topic: string, onMessage: (message: T) => void) {
  const [isConnected, setIsConnected] = useState(false)
  const clientRef = useRef<Client | null>(null)
  const { token, user } = useAuthStore()

  useEffect(() => {
    if (!token || !user) return

    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8086/ws'),
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      debug: (str: string) => {
        console.log('STOMP:', str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    })

    client.onConnect = () => {
      console.log('WebSocket connected')
      setIsConnected(true)

      client.subscribe(`/topic/${topic}/${user.tenantId}`, (message: IMessage) => {
        try {
          const data = JSON.parse(message.body) as T
          onMessage(data)
        } catch (error) {
          console.error('Error parsing WebSocket message:', error)
        }
      })
    }

    client.onDisconnect = () => {
      console.log('WebSocket disconnected')
      setIsConnected(false)
    }

    client.onStompError = frame => {
      console.error('STOMP error:', frame)
      setIsConnected(false)
    }

    client.activate()
    clientRef.current = client

    return () => {
      if (clientRef.current) {
        clientRef.current.deactivate()
      }
    }
  }, [token, user, topic, onMessage])

  return { isConnected }
}
