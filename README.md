# Server-Sent Events (SSE)
SSE é uma tecnologia que permite que um servidor envie atualizações automáticas para um cliente através de uma conexão HTTP persistente. Diferente de WebSockets, SSE é unidirecional (apenas servidor → cliente) e mais simples de implementar.

## Fluxo básico:

- O cliente faz uma requisição HTTP para o servidor.

- O servidor mantém a conexão aberta e envia eventos de forma contínua.

- O cliente escuta e processa os eventos recebidos.

## Vantagens:

- Baseado em HTTP (não precisa de protocolo extra).

- Reconexão automática pelo navegador.

- Simples de implementar.

# Rest Controller Java
Seguindo a simplicidade que o SSE naturalmente entrega, como podemos ver no controller da aplicação, o controle da existenção de novas conexões, nessa implementação básica, 
é feita por um HashMap em memória que tem como chave o parâmetro passado no path da requisição e o valor uma lista de SSE Emitters.

- Clientes se inscreverem em um "canal" (range) para receber eventos.

- O servidor envia mensagens para todos os inscritos de um canal específico.

# Como funciona no fluxo real
1) Cliente se inscreve:

```bash
GET /sse/subscribe/1
``` 
A conexão permanece aberta e o servidor poderá enviar eventos para esse cliente.

2) Servidor envia evento:

```bash 
POST /sse/send/1
```
Todos os clientes conectados a ```1``` recebem a nova mensagem instantaneamente.

# Testando a implementação

1) Suba a aplicação da forma que prefirir, seja por usa IDE ou por linha de comando executando o **.jar** gerado.

2) Abra no navegador o ```index.html``` encontrado dentro de ```/resources/template```

Nesse momento, se tudo ocorreu bem, seu navegador já vai estar com uma conexão constante com o servidor

> **Atenção:** Por default o EventSource do código javascript disponibilizado se conecta no **"range" 1**

3) Execute um **POST** em ```http://localhost:10001/sse/send/1```

4) Aproveite suas mensagens em tempo real chegando na página do seu navegador :)
