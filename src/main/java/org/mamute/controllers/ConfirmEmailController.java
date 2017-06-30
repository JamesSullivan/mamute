package org.mamute.controllers;

import java.util.Arrays;

import javax.inject.Inject;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.mamute.dao.UserDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.User;
import org.mamute.vraptor.DefaultLinker;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;
import br.com.caelum.vraptor.validator.Validator;

@Routed
@Controller
public class ConfirmEmailController {

	@Inject private Mailer mailer;
	@Inject private TemplateMailer templates;
	@Inject private Result result;
	@Inject private UserDAO users;
	@Inject private DefaultLinker linker;
	@Inject private Validator validator;
	@Inject private MessageFactory messageFactory;
	@Inject private BundleFormatter bundle;


	@Get
	public void sendEmailConfirm(String id) {
		User user = users.loadByEmail(id);

		if (user == null) {
			validator.add(messageFactory.build("error", "confirm_email.invalid_email"));
			validator.onErrorRedirectTo(this).confirmEmailError();
			return;
		}
		
		System.out.println("user.getName(): " + user.getName());

		Email confirmEmail = emailWithTokenFor(user);
		try {
			mailer.send(confirmEmail);
			result.include("mamuteMessages", Arrays.asList(
						messageFactory.build("confirmation", "confirm_email.sent_mail", user.getEmail()),
						messageFactory.build("confirmation", "confirm_email.sent_mail.warn")
					));
			result.redirectTo(this).confirmEmailSent();
		} catch (EmailException e) {
			validator.add(messageFactory.build("error", "forgot_password.send_mail.error"));
			validator.onErrorRedirectTo(this).confirmEmailError();
		}
	}
	
	@Get
	public void confirmEmailError() {
	}
	
	@Get
	public void confirmEmailSent() {
	}
	
	@Get
	public void confirmEmailSuccess(Long id, String token) {
		validateTokenAndGetUser(id, token);
		result.include("id", id);
		result.include("token", token);
	}
	
	private String tokenUrlFor(User user) {
		String token = user.touchForgotPasswordToken();
		linker.linkTo(this).confirmEmailSuccess(user.getId(), token);
		return linker.get();
	}
	
	private Email emailWithTokenFor(User user) {
		String url = tokenUrlFor(user);
		return templates.template("confirm_email_mail")
				.with("bundle", bundle)
				.with("user_name", user.getName())
				.with("url", url)
				.to(user.getName(), user.getEmail());
	}
	
	private User validateTokenAndGetUser(Long id, String token) {
		User user = users.loadByIdAndToken(id, token);
		if (user == null) {
			validator.add(messageFactory.build("error", "forgot_password.invalid_token"));
			validator.onErrorRedirectTo(this).confirmEmailError();
		}
		user.setConfirmedEmail();
		return user;
	}
	


}
