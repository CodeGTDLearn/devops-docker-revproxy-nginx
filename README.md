# Nginx Configuration

## Focus

1. Reverse Proxy
2. Load Balancing

## Main Configurations

1. Configurações Gerais do Nginx
2. Configuração do Server

## Índice

1. [Configurações Gerais do Nginx](#configurações-gerais-do-nginx)
* [worker_processes](#worker_processes)
* [error_log](#error_log)
* [events](#events)
* [worker_connections](#worker_connections)
* [http](#http)
  * [include](#include)
  * [sendfile](#sendfile)
  * [server_tokens](#server_tokens)
  * [client_max_body_size](#client_max_body_size)
  * [gzip](#gzip)
    * [gzip_proxied](#gzip_proxied)
    * [gzip_types](#gzip_types)
    * [gzip_comp_level](#gzip_comp_level)
  * [upstream](#upstream)
  * [server](#server)

2. [Configuração do Server](#configuração-do-server)
* [listen](#listen)
* [server_name](#server_name)
* [location](#location)
  * [proxy_redirect](#proxy_redirect)
  * [proxy_set_header](#proxy_set_header)
  * [proxy_hide_header](#proxy_hide_header)
  * [proxy_pass](#proxy_pass)

3. [Vantagens do uso do Nginx](#vantagens-do-uso-do-nginx)
* [Security](#security)
* [SSL](#ssl)
* [Scalability](#scalability)
* [Caching](#caching)
* [Nesting](#nesting)
* [Data Compression️](#data-compression)
* [Horizontal Scaling (Resilience)](#horizontal-scaling-resilience)
* [Proxy / Redirection](#proxy--redirection)
* [Important Tutorials️](#important-tutorials)

## Configurações Gerais do Nginx

### worker_processes

- Define quantos processos de trabalho/request serão criados.
    - Ex: 01 Core-CPU p/ cada 'worker_process'

- Opcoes:
    - `number`: Deve acompanhar o número de 'cores' da 'CPU do 'server de deploy'.
    - `auto`: Autodetecta o total de cores disponíveis na 'CPU do 'server de deploy'.

```nginx configuration
worker_processes auto;
```

### error_log

- Define a localização do log de erros.

```nginx configuration
error_log /var/log/nginx/error.log warn;
```

### events

- Define como o Nginx gerencia as conexões de rede.

```nginx configuration
events {
    worker_connections 1024;
}
```

### worker_connections

- Quantidade de 'conexões simultâneas' em cada 'worker_processes'.
    - Ex: 1024 'work_connections': suporta até 1024 requests/second.

```nginx configuration
worker_connections 1024;
```

### http

- Configuracao de trafego
- Comportamento geral das conexões HTTP rodadas nos 'worker_processes'.

```nginx configuration
http {

        include /etc/nginx/nginx_mime.types;
        
        sendfile on;
        server_tokens off;
        client_max_body_size 1m;
        
        gzip on;
        gzip_proxied any;
        gzip_comp_level 6;
        gzip_types text/plain text/xml text/css text/html text/javascript application/xml+rss application/javascript
        application/json application/xml application/x-javascript;
        
        upstream loadbalancing-servers-crew {
            least_conn;
            
            server webapp_1:8080;
            server webapp_2:8080;
        }
    
    include /etc/nginx/conf.d/02 - server_config.conf;
    
}
```

### include

- Inclui arquivos de configuração adicionais/externos.

```nginx configuration
    include /etc/nginx/conf.d/02 - server_config.conf;
```

### sendfile

- Usa o 'OS' para transferir 'files' para 'rede/client'
- sem passar pelo 'buffer do Nginx'.
- Economiza CPU e Memória.

```nginx configuration
    sendfile on;
```

### server_tokens

- Suprime a versão do Nginx na request (SEGURANÇA).

```nginx configuration
    server_tokens off;
```

### client_max_body_size

- Limita o tamanho máximo do 'body' da request (SEGURANÇA).

```nginx configuration
    client_max_body_size 1m;
```

### gzip

- Comprime 'files' nas 'responses' antes de enviá-los p/ o 'client'.

```nginx configuration
    gzip on;
```

#### gzip_proxied

- Comprime os 'payloads dos requests' nos 'servers especificados no UpStream'.
    - **Default:** Comprime 'requests' **_"> de 20Bytes"_** **_(quase todos)_**, pois:
        - O 'Compress-Header'(5bytes) + 'Request_15 Bytes' = já resultaria em 20Bytes.
            - Exemplos de tamanho (JSONS):
                - {"age": 30} = 15Bytes
                - {"nome": "John Doe"} = 20Bytes.

```nginx configuration
    gzip_proxied any;
```

### gzip_types

- Especifica os conteúdos para compressão.
- Videos/imagens/pdf tem pouca compressao, logo não compensam a CPU usada na compressa
- EXCECAO: SVG pois, de fato este é um 'txt vetorizado' logo ele é 'gzipável'.

```nginx configuration
    gzip_types text/plain text/xml text/css text/html text/javascript application/xml+rss application/javascript application/json application/xml application/x-javascript;
```

### gzip_comp_level

- Melhora a taxa de compressão
    - **Default:** 5

```nginx configuration
    gzip_comp_level 6;
```

### upstream

- Define o:
    - Algoritmo de Balanceamento de Carga
    - 'Grupo de servers' do 'balanceamento de carga'.

```nginx configuration
    upstream loadbalancing-servers-crew {

        least_conn;
    
        server webapp_1:8080;
        server webapp_2:8080;
        
    }
```

- Algoritmo de Balanceamento de Carga:
    - **_least_conn_**: Envia 'requests' para o 'server' menos 'carregado' no momento.

```nginx configuration
    least_conn;
```

- **_round-robin_**:
    - Distribuicao sequencial dos 'requests'

```nginx configuration
    round_robin;
```

- **_ip-hash_**:
    - Permite 'requests' de 'um mesmo client' cairem 'num mesmo server' exceto se o server estiver indisponível.

```nginx configuration
    ip_hash;
```

### server

- Define os 'servers' para o **_LoadBalancing_**.
- Condicao: Os services do 'App' e 'Nginx' devem estar na mesma 'network'
- Nomenclatura:
    - **_"webapp"_**: nome do "Service"(container) da WebApp no Docker-Compose.
    - **_"1"_** : número da "replica escalada" do container.
    - **_":8080"_**: Porta escutada nos 'Services Replicados'.

```nginx configuration
        server webapp_1:8080;
        server webapp_2:8080;
```

## Configuração do Server

### listen

- Define a **_'Porta de escuta/recepção'_** dos requests.
  - **_Requests recebidos aqui_**, serao encaminhados p/ **_'location'_**(sintaxe)
- Porta 80 / HTTP:
    - Conexão não criptografada
    - não é SSL - não recomendável expor para WEB
    - RODAR DENTRO DA EC2 "PROTEGIDA POR BORDA"
        - BORDA-BLINDADA = CLOUDFLARE+SSL.
    - AWS Security Group:
        - Bloquear acesso direto à Porta 80.

```nginx configuration
listen 80;
```

### server_name

- 'Dominio ROOT' "antecedente" ao 'PROXY-URL' definido no 'location'.

```nginx configuration
server_name meu-ip.com.br;
```

### location

- Regras de redirecionamento (PROXY-REVERSO).
    - PROXY-URL **_(Adicional ao ROOT)_**.
        - EX.: "/" Todos 'Requests' do domínio, cairão em "/" e serão redirecionados para onde 'for definido no' '
          proxy_pass'.

```nginx configuration
location / {

    proxy_redirect off;
    
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_hide_header X-Powered-By;
    
    proxy_pass http://loadbalancing-servers-crew;
    
}
```

### proxy_redirect

- Bloqueia (SEGURANCA) os 'Redirect-Headers' impedindo redirecionamentos dos 'servers' do 'grupo de servers' do
  'LoadBalancing' (
  Upstream).

```nginx configuration
proxy_redirect off;
```

### proxy_set_header

- Garante que os **_'servers'_** do **_'grupo LoadBalancing' (Http|Upstream)_** recebam um:
    - 'Header de Host' identico ao 'Header de
      Host' do client.
    - 'Header de X-Real-IP' com o 'IP real do
      client'.
    - 'Header de X-Forwarded-For' com a 'lista
      cumulativa dos IP's trafegados' pela request.

```nginx configuration
proxy_set_header Host $host;
proxy_set_header X-Real-IP $remote_addr;
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
```

### proxy_hide_header

- Garante que os 'servers' do grupo 'LoadBalancing' (Http|Upstream), escondam o Header "X-Powered-By" trafegados pela
  request.
    - Checar 'NetWork' no Browser para selecionar o 'Headers' a serem escondidos.

```nginx configuration
proxy_hide_header X-Powered-By;
```

### proxy_pass

- Redireciona os 'requests' para o **_'Poll de Instancias'_**.
- **_Pool de Instancias:_**
    - 'Grupo de servers' do 'LoadBalancing' (Http|Upstream).
    - "RequestMapping do Controller" da Spring API: '/users'(exemplo).
- OBS: Não usar porta, pois já estão mapeadas nos 'servers' da **_'Poll de Instancias'_**.

```nginx configuration
proxy_pass http://loadbalancing-servers-crew;
```

## Vantagens do uso do Nginx

### Security

| Feature             | Description                                                            |
|---------------------|------------------------------------------------------------------------|
| **Reverse Proxy**   | Prevents basic attacks/vectors                                         |
| **DDoS Mitigation** | Improves request management, enhancing protection against DDoS attacks |

### SSL

### Scalability

- **Load Balancing**
    - **Local Load Balancing**: Distributes traffic within the same location.
    - **Distributed Load Balancing**: Distributes traffic across multiple locations.

### Caching

- **Caches URLs**: Improves performance by caching frequently accessed UR

### Nesting

- **Proxy Nesting**: Allows for complex configurations by nesting proxies.

### Data Compression

- **Faster Traffic**: Improves performance by compressing data.
- **Reduced Costs**: Lowers costs by reducing data traffic.
- **Faster Thread Release**: Mitigates DDoS by releasing threads more quickly.
- **Avoids API Compression**: Saves CPU by avoiding compression within the API.

### Horizontal Scaling (Resilience)

- Replica do container (2 formas) - Horizontal Scaling:
    * [Dynamic | docker-scale](https://youtu.be/9aOpRhm33oM)
    * [Static | Compose Duplicity](https://youtu.be/bFZurhL14LA)

### Proxy / Redirection

- ["Redirectinal Proxy"](https://youtu.be/bFZurhL14LA)

### Important Tutorials

- **Cezar milan**:
    * [Nginx include Servers](https://youtu.be/WeoZ4Ego1vs)
    * [Nginx Features - Video 1](https://youtu.be/E51dIa0ZcGs)
    * [Nginx Features - Video 2](https://youtu.be/Sa74-4ExZ4Q)
    * [GitHub](https://github.com/wesleymilan/nginx-for-nodejs/blob/main/config/nginx/nginx.conf)

---