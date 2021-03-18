### Reactor Webflux 예제 
##### 03.18 최신화

```text
	환경
	- Spring Reactive web
	- Springboot DevTool
	- Lombok
```

```java
	// 프로세서 만들기
	// Sinks = 프로세서
	// 역할 : 지속적인 응답.
	Sinks.Many<String> sink;
	
	// SSE Protocol
	// 목적 : 
	// SSE가 Sink를 바라보게 만든다.
	// "/send"를 하게되면 SSE를 구독하고 있는 모든 구독자에게
	// 데이터를 응답하게 만든다.
	// TEXT_EVENT_STREAM_VALUE : SSE 전용 MINE 타입이다. data: 실제값\n\n
	// ServerSentEvent는 기본적으로 produces = MediaType.TEXT_EVENT_STREAM_VALUE 포함한다.
	@GetMapping(value = "/sse")
	public Flux<ServerSentEvent<String>> sse() { // ServerSentEvent<String>
		 // 쉽게 말해서 SSE는 어떤 하나의 Sink를 구독하고 있는 코드.
		return sink.asFlux().map(e -> ServerSentEvent.builder(e).build());
	}
	
```
