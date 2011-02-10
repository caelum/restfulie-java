package br.com.caelum.restfulie.feature;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.http.error.BadRequestException;
import br.com.caelum.restfulie.http.error.ClientException;
import br.com.caelum.restfulie.http.error.ConflictException;
import br.com.caelum.restfulie.http.error.ForbiddenException;
import br.com.caelum.restfulie.http.error.GoneException;
import br.com.caelum.restfulie.http.error.MethodNotAllowedException;
import br.com.caelum.restfulie.http.error.NotFoundException;
import br.com.caelum.restfulie.http.error.NotImplementedException;
import br.com.caelum.restfulie.http.error.PreconditionFailedException;
import br.com.caelum.restfulie.http.error.ProxyAuthenticationRequiredException;
import br.com.caelum.restfulie.http.error.RedicetionException;
import br.com.caelum.restfulie.http.error.ServerException;
import br.com.caelum.restfulie.http.error.UnauthorizedException;
import br.com.caelum.restfulie.http.error.UnknowCodeException;
import br.com.caelum.restfulie.request.ResponseChain;

@RunWith(MockitoJUnitRunner.class)
public class ThrowErrorTest {


	@Mock
	private Request request;

	@Mock
	private ResponseChain chain;

	@Mock
	private Response response;

	@Test
	public void shouldNotThrowExceptionWhenCode100() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(100);

		//When
		new ThrowError().process(chain, response);

		//Then
		verify(chain).next(response);
	}

	@Test
	public void shouldNotThrowExceptionWhenCode200() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(200);

		//When
		new ThrowError().process(chain, response);

		//Then
		verify(chain).next(response);
	}

	@Test
	public void shouldNotThrowExceptionWhenCode299() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(299);

		//When
		new ThrowError().process(chain, response);

		//Then
		verify(chain).next(response);
	}

	@Test(expected=RedicetionException.class)
	public void shouldThrowRedirectionExceptionWhenCode300() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(300);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=RedicetionException.class)
	public void shouldThrowRedirectionExceptionWhenCode350() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(350);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=RedicetionException.class)
	public void shouldThrowRedirectionExceptionWhenCode399() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(399);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=BadRequestException.class)
	public void shouldThrowBadRequestWhenCode400() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(400);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=UnauthorizedException.class)
	public void shouldThrowUnauthorizedExceptionWhenCode401() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(401);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ForbiddenException.class)
	public void shouldThrowForbiddenExceptionWhenCode403() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(403);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=NotFoundException.class)
	public void shouldThrowNotFoundExceptionWhenCode404() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(404);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=MethodNotAllowedException.class)
	public void shouldThrowMethodNotAllowedExceptionWhenCode405() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(405);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ProxyAuthenticationRequiredException.class)
	public void shouldThrowProxyAuthenticationRequiredExceptionWhenCode407() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(407);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ConflictException.class)
	public void shouldThrowConflictExceptionWhenCode409() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(409);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=GoneException.class)
	public void shouldThrowGoneExceptionWhenCode410() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(410);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=PreconditionFailedException.class)
	public void shouldThrowPreconditionFailedExceptionWhenCode412() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(412);

		//When
		new ThrowError().process(chain, response);
	}


	@Test(expected=ClientException.class)
	public void shouldThrowClientExceptionWhenCode402() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(402);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ClientException.class)
	public void shouldThrowClientExceptionWhenCode406() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(406);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ClientException.class)
	public void shouldThrowClientExceptionWhenCode408() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(408);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ClientException.class)
	public void shouldThrowClientExceptionWhenCode411() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(411);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ClientException.class)
	public void shouldThrowClientExceptionWhenCode413() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(413);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ClientException.class)
	public void shouldThrowClientExceptionWhenCode450() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(450);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ClientException.class)
	public void shouldThrowClientExceptionWhenCode499() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(499);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=NotImplementedException.class)
	public void shouldThrowNotImplementedExceptionWhenCode501() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(501);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ServerException.class)
	public void shouldThrowServerExceptionWhenCode500() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(500);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ServerException.class)
	public void shouldThrowServerExceptionWhenCode502() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(502);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ServerException.class)
	public void shouldThrowServerExceptionWhenCode550() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(550);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=ServerException.class)
	public void shouldThrowServerExceptionWhenCode599() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(599);

		//When
		new ThrowError().process(chain, response);
	}

	@Test(expected=UnknowCodeException.class)
	public void shouldThrowUnkownCodeExceptionWhenUnknonwCode() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(600);

		//When
		new ThrowError().process(chain, response);
	}

}
