package org.mamute.controllers;

import static java.util.Arrays.asList;

import javax.inject.Inject;

import org.mamute.auth.FacebookAuthService;
import org.mamute.auth.GoogleAuthService;
import org.mamute.dao.LoginMethodDAO;
import org.mamute.dao.UserDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.LoginMethod;
import org.mamute.model.SanitizedText;
import org.mamute.model.User;
import org.mamute.validators.SignupValidator;
import org.mamute.vraptor.Linker;

import br.com.caelum.brutauth.auth.annotations.Public;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Public
@Routed
@Controller
public class ValidEmailController {

	@Inject private SignupValidator validator;
	@Inject private UserDAO users;
	@Inject private Result result;
	@Inject private MessageFactory messageFactory;
	@Inject private LoginMethodDAO loginMethods;
	@Inject private FacebookAuthService facebook;
	@Inject private GoogleAuthService google;
	@Inject private Linker linker;
	@Inject private Environment env;

	@Get
	public void check() {

		result.include("facebookUrl", facebook.getOauthUrl(null));
		result.include("googleUrl", google.getOauthUrl(null));
	}

}
