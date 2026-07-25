# КЛИЕНТ

## Обработчик канала
```java
54  public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
55  
56      private final WebSocketClientHandshaker handshaker;
57      private ChannelPromise handshakeFuture;
58  
59      public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
60          this.handshaker = handshaker;
61      }
62  
63      public ChannelFuture handshakeFuture() {
64          return handshakeFuture;
65      }
66  
67      @Override
68      public void handlerAdded(ChannelHandlerContext ctx) {
69          handshakeFuture = ctx.newPromise();
70      }
71  
72      @Override
73      public void channelActive(ChannelHandlerContext ctx) {
74          handshaker.handshake(ctx.channel());
75      }
76  
77      @Override
78      public void channelInactive(ChannelHandlerContext ctx) {
79          System.out.println("WebSocket Client disconnected!");
80      }
81  
82      @Override
83      public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
84          Channel ch = ctx.channel();
85          if (!handshaker.isHandshakeComplete()) {
86              try {
87                  handshaker.finishHandshake(ch, (FullHttpResponse) msg);
88                  System.out.println("WebSocket Client connected!");
89                  handshakeFuture.setSuccess();
90              } catch (WebSocketHandshakeException e) {
91                  System.out.println("WebSocket Client failed to connect");
92                  handshakeFuture.setFailure(e);
93              }
94              return;
95          }
96  
97          if (msg instanceof FullHttpResponse) {
98              FullHttpResponse response = (FullHttpResponse) msg;
99              throw new IllegalStateException(
100                     "Unexpected FullHttpResponse (getStatus=" + response.status() +
101                             ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
102         }
103 
104         WebSocketFrame frame = (WebSocketFrame) msg;
105         if (frame instanceof TextWebSocketFrame) {
106             TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
107             System.out.println("WebSocket Client received message: " + textFrame.text());
108         } else if (frame instanceof PongWebSocketFrame) {
109             System.out.println("WebSocket Client received pong");
110         } else if (frame instanceof CloseWebSocketFrame) {
111             System.out.println("WebSocket Client received closing");
112             ch.close();
113         }
114     }
115 
116     @Override
117     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
118         cause.printStackTrace();
119         if (!handshakeFuture.isDone()) {
120             handshakeFuture.setFailure(cause);
121         }
122         ctx.close();
123     }
124 }
```

## Бутстрапп клиента
```java
public final class WebSocketClient {
60  
61      static final String URL = System.getProperty("url", "ws://127.0.0.1:8080/websocket");
62      static final int MAX_CONTENT_LENGTH = 8192;
63  
64      public static void main(String[] args) throws Exception {
65          URI uri = new URI(URL);
66          String scheme = uri.getScheme() == null? "ws" : uri.getScheme();
67          final String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
68          final int port;
69          if (uri.getPort() == -1) {
70              if ("ws".equalsIgnoreCase(scheme)) {
71                  port = 80;
72              } else if ("wss".equalsIgnoreCase(scheme)) {
73                  port = 443;
74              } else {
75                  port = -1;
76              }
77          } else {
78              port = uri.getPort();
79          }
80  
81          if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
82              System.err.println("Only WS(S) is supported.");
83              return;
84          }
85  
86          final boolean ssl = "wss".equalsIgnoreCase(scheme);
87          final SslContext sslCtx;
88          if (ssl) {
89              sslCtx = SslContextBuilder.forClient()
90                  .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
91          } else {
92              sslCtx = null;
93          }
94  
95          EventLoopGroup group = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
96          try {
97              // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
98              // If you change it to V00, ping is not supported and remember to change
99              // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
100             final WebSocketClientHandler handler =
101                     new WebSocketClientHandler(
102                             WebSocketClientHandshakerFactory.newHandshaker(
103                                     uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));
104 
105             Bootstrap b = new Bootstrap();
106             b.group(group)
107              .channel(NioSocketChannel.class)
108              .handler(new ChannelInitializer<SocketChannel>() {
109                  @Override
110                  protected void initChannel(SocketChannel ch) {
111                      ChannelPipeline p = ch.pipeline();
112                      if (sslCtx != null) {
113                          p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
114                      }
115                      p.addLast(
116                              new HttpClientCodec(),
117                              new HttpObjectAggregator(MAX_CONTENT_LENGTH),
118                              new WebSocketClientCompressionHandler(MAX_CONTENT_LENGTH),
119                              handler);
120                  }
121              });
122 
123             Channel ch = b.connect(uri.getHost(), port).sync().channel();
124             handler.handshakeFuture().sync();
125 
126             BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
127             while (true) {
128                 String msg = console.readLine();
129                 if (msg == null) {
130                     break;
131                 } else if ("bye".equals(msg.toLowerCase())) {
132                     ch.writeAndFlush(new CloseWebSocketFrame());
133                     ch.closeFuture().sync();
134                     break;
135                 } else if ("ping".equals(msg.toLowerCase())) {
136                     WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
137                     ch.writeAndFlush(frame);
138                 } else {
139                     WebSocketFrame frame = new TextWebSocketFrame(msg);
140                     ch.writeAndFlush(frame);
141                 }
142             }
143         } finally {
144             group.shutdownGracefully();
145         }
146     }
147 }
```

---
# СЕРВЕР
## Обработчик канала
```java
26  /**
27   * Echoes uppercase content of text frames.
28   */
29  public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
30  
31      @Override
32      protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
33          // ping and pong frames already handled
34  
35          if (frame instanceof TextWebSocketFrame) {
36              // Send the uppercase string back.
37              String request = ((TextWebSocketFrame) frame).text();
38              ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
39          } else {
40              String message = "unsupported frame type: " + frame.getClass().getName();
41              throw new UnsupportedOperationException(message);
42          }
43      }
44  
45      @Override
46      public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
47          if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
48              //Channel upgrade to websocket, remove WebSocketIndexPageHandler.
49              ctx.pipeline().remove(WebSocketIndexPageHandler.class);
50          } else {
51              super.userEventTriggered(ctx, evt);
52          }
53      }
54  }
```

## Какой-то обработчик страницы
```java
39  /**
40   * Outputs index page content.
41   */
42  public class WebSocketIndexPageHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
43  
44      private final String websocketPath;
45  
46      public WebSocketIndexPageHandler(String websocketPath) {
47          this.websocketPath = websocketPath;
48      }
49  
50      @Override
51      protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
52          // Handle a bad request.
53          if (!req.decoderResult().isSuccess()) {
54              sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), BAD_REQUEST,
55                                                                     ctx.alloc().buffer(0)));
56              return;
57          }
58  
59          // Handle websocket upgrade request.
60          if (req.headers().contains(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET, true)) {
61              ctx.fireChannelRead(req.retain());
62              return;
63          }
64  
65          // Allow only GET methods.
66          if (!GET.equals(req.method())) {
67              sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), FORBIDDEN,
68                                                                     ctx.alloc().buffer(0)));
69              return;
70          }
71  
72          // Send the index page
73          if ("/".equals(req.uri()) || "/index.html".equals(req.uri())) {
74              String webSocketLocation = getWebSocketLocation(ctx.pipeline(), req, websocketPath);
75              ByteBuf content = WebSocketServerIndexPage.getContent(webSocketLocation);
76              FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), OK, content);
77  
78              res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
79              HttpUtil.setContentLength(res, content.readableBytes());
80  
81              sendHttpResponse(ctx, req, res);
82          } else {
83              sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), NOT_FOUND,
84                                                                     ctx.alloc().buffer(0)));
85          }
86      }
87  
88      @Override
89      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
90          cause.printStackTrace();
91          ctx.close();
92      }
93  
94      private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
95          // Generate an error page if response getStatus code is not OK (200).
96          HttpResponseStatus responseStatus = res.status();
97          if (responseStatus.code() != 200) {
98              ByteBufUtil.writeUtf8(res.content(), responseStatus.toString());
99              HttpUtil.setContentLength(res, res.content().readableBytes());
100         }
101         // Send the response and close the connection if necessary.
102         boolean keepAlive = HttpUtil.isKeepAlive(req) && responseStatus.code() == 200;
103         HttpUtil.setKeepAlive(res, keepAlive);
104         ChannelFuture future = ctx.writeAndFlush(res);
105         if (!keepAlive) {
106             future.addListener(ChannelFutureListener.CLOSE);
107         }
108     }
109 
110     private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
111         String protocol = "ws";
112         if (cp.get(SslHandler.class) != null) {
113             // SSL in use so use Secure WebSockets
114             protocol = "wss";
115         }
116         return protocol + "://" + req.headers().get(HttpHeaderNames.HOST) + path;
117     }
118 }
```

## Судя по всему сама страница:
```java
22  /**
23   * Generates the demo HTML page which is served at http://localhost:8080/
24   */
25  public final class WebSocketServerIndexPage {
26  
27      private static final String NEWLINE = "\r\n";
28  
29      public static ByteBuf getContent(String webSocketLocation) {
30          return Unpooled.copiedBuffer(
31                  "<html><head><title>Web Socket Test</title></head>" + NEWLINE +
32                  "<body>" + NEWLINE +
33                  "<script type=\"text/javascript\">" + NEWLINE +
34                  "var socket;" + NEWLINE +
35                  "if (!window.WebSocket) {" + NEWLINE +
36                  "  window.WebSocket = window.MozWebSocket;" + NEWLINE +
37                  '}' + NEWLINE +
38                  "if (window.WebSocket) {" + NEWLINE +
39                  "  socket = new WebSocket(\"" + webSocketLocation + "\");" + NEWLINE +
40                  "  socket.onmessage = function(event) {" + NEWLINE +
41                  "    var ta = document.getElementById('responseText');" + NEWLINE +
42                  "    ta.value = ta.value + '\\n' + event.data" + NEWLINE +
43                  "  };" + NEWLINE +
44                  "  socket.onopen = function(event) {" + NEWLINE +
45                  "    var ta = document.getElementById('responseText');" + NEWLINE +
46                  "    ta.value = \"Web Socket opened!\";" + NEWLINE +
47                  "  };" + NEWLINE +
48                  "  socket.onclose = function(event) {" + NEWLINE +
49                  "    var ta = document.getElementById('responseText');" + NEWLINE +
50                  "    ta.value = ta.value + \"Web Socket closed\"; " + NEWLINE +
51                  "  };" + NEWLINE +
52                  "} else {" + NEWLINE +
53                  "  alert(\"Your browser does not support Web Socket.\");" + NEWLINE +
54                  '}' + NEWLINE +
55                  NEWLINE +
56                  "function send(message) {" + NEWLINE +
57                  "  if (!window.WebSocket) { return; }" + NEWLINE +
58                  "  if (socket.readyState == WebSocket.OPEN) {" + NEWLINE +
59                  "    socket.send(message);" + NEWLINE +
60                  "  } else {" + NEWLINE +
61                  "    alert(\"The socket is not open.\");" + NEWLINE +
62                  "  }" + NEWLINE +
63                  '}' + NEWLINE +
64                  "</script>" + NEWLINE +
65                  "<form onsubmit=\"return false;\">" + NEWLINE +
66                  "<input type=\"text\" name=\"message\" value=\"Hello, World!\"/>" +
67                  "<input type=\"button\" value=\"Send Web Socket Data\"" + NEWLINE +
68                  "       onclick=\"send(this.form.message.value)\" />" + NEWLINE +
69                  "<h3>Output</h3>" + NEWLINE +
70                  "<textarea id=\"responseText\" style=\"width:500px;height:300px;\"></textarea>" + NEWLINE +
71                  "</form>" + NEWLINE +
72                  "</body>" + NEWLINE +
73                  "</html>" + NEWLINE, CharsetUtil.US_ASCII);
74      }
75  
76      private WebSocketServerIndexPage() {
77          // Unused
78      }
79  }
```

---
## НАСТРОЙКА ПАЙПЛАЙНА СЕРВЕРА
```JAVA
29  public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
30  
31      private static final String WEBSOCKET_PATH = "/websocket";
32  
33      private static final int MAX_CONTENT_LENGTH = 65536;
34  
35      private final SslContext sslCtx;
36  
37      public WebSocketServerInitializer(SslContext sslCtx) {
38          this.sslCtx = sslCtx;
39      }
40  
41      @Override
42      public void initChannel(SocketChannel ch) throws Exception {
43          ChannelPipeline pipeline = ch.pipeline();
44          if (sslCtx != null) {
45              pipeline.addLast(sslCtx.newHandler(ch.alloc()));
46          }
47          pipeline.addLast(new HttpServerCodec());
48          pipeline.addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH));
49          pipeline.addLast(new WebSocketIndexPageHandler(WEBSOCKET_PATH));
50          pipeline.addLast(new WebSocketServerCompressionHandler(MAX_CONTENT_LENGTH));
51          pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
52          pipeline.addLast(new WebSocketFrameHandler());
53      }
54  }
```

---
## БУТСТРАП СЕРВЕРА
```java
29  /**
30   * An HTTP server which serves Web Socket requests at:
31   *
32   * http://localhost:8080/websocket
33   *
34   * Open your browser at <a href="http://localhost:8080/">http://localhost:8080/</a>, then the demo page will be loaded
35   * and a Web Socket connection will be made automatically.
36   *
37   * This server illustrates support for the different web socket specification versions and will work with:
38   *
39   * <ul>
40   * <li>Safari 5+ (draft-ietf-hybi-thewebsocketprotocol-00)
41   * <li>Chrome 6-13 (draft-ietf-hybi-thewebsocketprotocol-00)
42   * <li>Chrome 14+ (draft-ietf-hybi-thewebsocketprotocol-10)
43   * <li>Chrome 16+ (RFC 6455 aka draft-ietf-hybi-thewebsocketprotocol-17)
44   * <li>Firefox 7+ (draft-ietf-hybi-thewebsocketprotocol-10)
45   * <li>Firefox 11+ (RFC 6455 aka draft-ietf-hybi-thewebsocketprotocol-17)
46   * </ul>
47   */
48  public final class WebSocketServer {
49  
50      static final boolean SSL = System.getProperty("ssl") != null;
51      static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8080"));
52  
53      public static void main(String[] args) throws Exception {
54          // Configure SSL.
55          final SslContext sslCtx = ServerUtil.buildSslContext();
56  
57          EventLoopGroup group = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
58          try {
59              ServerBootstrap b = new ServerBootstrap();
60              b.group(group)
61               .channel(NioServerSocketChannel.class)
62               .handler(new LoggingHandler(LogLevel.INFO))
63               .childHandler(new WebSocketServerInitializer(sslCtx));
64  
65              Channel ch = b.bind(PORT).sync().channel();
66  
67              System.out.println("Open your web browser and navigate to " +
68                      (SSL? "https" : "http") + "://127.0.0.1:" + PORT + '/');
69  
70              ch.closeFuture().sync();
71          } finally {
72              group.shutdownGracefully();
73          }
74      }
75  }
```