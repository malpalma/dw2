package dw2;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mp.dw.DWService;

@Configuration
public class MockInjectionConfiguration {

	@Bean
	public DWService dwService() {
		DWService mockDWService = Mockito.mock(DWService.class);
		return mockDWService;
	}
}
