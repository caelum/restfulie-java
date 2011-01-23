package br.com.caelum.restfulie.feature;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
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
import br.com.caelum.restfulie.request.ResponseChain;
import br.com.caelum.restfulie.request.ResponseFeature;

public class ThrowError implements ResponseFeature {

	public ThrowError(RestClient client) {
	}

	public Response process(ResponseChain responseChain, Response response) {
		int code = response.getCode();

		
		//300 range
		if(code >= 300 && code <= 399) {
			throw new RedicetionException("Http erro when invoking blah");
		}
		
		if(code == 400) {
			throw new BadRequestException("Http erro when invoking blah");
		}
		
		if(code == 401) {
			throw new UnauthorizedException("Http erro when invoking blah");
		}

		if(code == 403) {
			throw new ForbiddenException("Http erro when invoking blah");
		}
		
		if(code == 404) {
			throw new NotFoundException("Http erro when invoking blah");
		}
		
		if(code == 405) {
			throw new MethodNotAllowedException("Http erro when invoking blah");
		}
		
		if(code == 407) {
			throw new ProxyAuthenticationRequiredException("Http erro when invoking blah");
		}
		
		if(code == 409) {
			throw new ConflictException("Http erro when invoking blah");
		}
		
		if(code == 410) {
			throw new GoneException("Http erro when invoking blah");
		}
		
		if(code == 412) {
			throw new PreconditionFailedException("Http erro when invoking blah");
		}
		
		if(code == 402 || code == 406 || code == 408 || code == 411 || (code >= 413 && code <= 499)) {
			throw new ClientException("Http erro when invoking blah");
		}
		
		if(code == 501) {
			throw new NotImplementedException("Http erro when invoking blah");
		}
		
		if (code == 500 || (code >= 502 && code <= 599)) {
			throw new ServerException("Http erro when invoking blah");
		}
		
		return null;
	}

}
