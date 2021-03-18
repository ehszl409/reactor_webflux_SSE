package com.park.webfluxex;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Netty 서버는 비동기 서버 Tomcat 서버는 스레드 서버. (Tomcat도 8.5부터 비동기서버를 지원 한다.)
// Flux는 N개 이상의 데이터를 응답할 때
// Mono는 0~1개의 데이터를 응답할 때 


@RestController
public class TestController {

	@GetMapping("/flux1")
	public Flux<Integer> flux1() {
		// (Back Pressure를 설정하지 않으면 디폴트 unbounded로 설정됩니다.)
		// 내부적으로 onNext가 4번 실행됬지만 http통신에 의해 한번에 응답한다.
		return Flux.just(1,2,3,4).log();
	}
	
	// 맵핑 설정에 하나만 넣으면 value생략가능
	// produces : MINE타입을 설정해 줄 수 있다.
	@GetMapping(value = "/flux2", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Integer> flux2() {
		// (Back Pressure를 설정하지 않으면 디폴트 unbounded로 설정됩니다.)
		// 1초마다 값이 전달되도록 딜레이를 걸어줬다.
		// 데이터가 전부 날아오지 않았는데 첫번째 데이터를 받으면 http200을 응답한다.
		// 이유는 브라우저도 첫 데이터를 받고 Stream으로 전달하는 것을 인식하고
		// response를 끊지 않기 때문이다.
		return Flux.just(1,2,3,4).delayElements(Duration.ofSeconds(1)).log();
	}
	
		@GetMapping(value = "/flux3", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
		public Flux<Long> flux3() {
			// interval : 데이터가 없어도 계속해서 OnNext()를 호출한다.
			return Flux.interval(Duration.ofSeconds(1)).log();
		}
	
	@GetMapping(value = "/mono1")
	public Mono<Integer> mono1() {
		// (Back Pressure를 설정하지 않으면 디폴트 unbounded로 설정됩니다.)
		// Mono는 하나의 오브젝트를 전달할 때 사용함으로 딜레이를 사용할 수 없다.
		return Mono.just(1).log();
	}
	
	
	
	
}
