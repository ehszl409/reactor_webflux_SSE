package com.park.webfluxex;


import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

// 프로세서를 만들어서 사용해보기.
@CrossOrigin
@RestController
public class TestController2 {
	
	// 프로세서 만들기
	// Sinks = 프로세서
	// 역할 : 지속적인 응답.
	Sinks.Many<String> sink;
	
	public TestController2() {
		// multicast() : 새로 들어온 데이터만 응답 받음 Hot 시퀀스, Hot 스트림
		// 				채팅방에 사용되는 개념이다. 채팅방에 새로운 사람이 들어오면 
		//				새로운 사람에게 전 대화 내용을 전부 보여주지 않는 것과 같다.
		// Reply() : 기존 데이터 + 새로운 데이터 전부 응답 Cold 시퀀스
		this.sink = Sinks.many().multicast().onBackpressureBuffer();
	}
	
	@GetMapping("/")
	public Flux<Integer> test() {
		return Flux.just(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15).log();
	}

	// Sink에 데이터를 넣어주는 것.
	
	@GetMapping("/send")
	public void send() {
		// sink에 데이터를 집어 넣었다.
		sink.tryEmitNext("Hello World");
	}
	
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
}
