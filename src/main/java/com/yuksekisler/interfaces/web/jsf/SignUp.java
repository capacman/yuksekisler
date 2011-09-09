package com.yuksekisler.interfaces.web.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

public class SignUp {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignUp.class);
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;

	// private String userName;
	private String password;
	private String confirmPassword;
	private MessageSource messageSource;
	private SessionInfo sessionInfo;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}


	public void confirmPasswordCheck(FacesContext context,
			UIComponent component, Object value) {
		UIInput passwordInput = (UIInput) component.findComponent("password");
		String localPassword = (String) passwordInput.getLocalValue();
		LOGGER.debug("passwords are password: " + localPassword
				+ " confirmPassword: " + value);
		if (!localPassword.equals(value)) {
			LOGGER.debug("password confirmation failed!");
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, messageSource.getMessage(
							"confirmPasswordFailed", null,
							sessionInfo.getLocale()), messageSource.getMessage(
							"confirmPasswordFailedDetail", null,
							sessionInfo.getLocale()));
			throw new ValidatorException(message);
		}
	}

//	public String signUp() {
//		try {
//			CrewMember crewMember = crewMemberService.createCrewMember(
//					firstName, lastName, email, password, phone, address, type);
//			Utils.addToFlashScopeMessages(new FacesMessage(
//					FacesMessage.SEVERITY_INFO, messageSource.getMessage(
//							"accountCreated",
//							new Object[] { crewMember.getName(),
//									crewMember.getSurname(),
//									crewMember.getUsername() },
//							sessionInfo.getLocale()), ""));
//			return "login";
//		} catch (Exception e) {
//			LOGGER.info("crewMember cannot be created", e);
//			// TODO: catch exception in a more specific manner get messages and
//			// navigate either to login or to same page
//			return "signup";
//		}
//
//	}
//
//	public void setCrewMemberService(EmployeeRepository crewMemberService) {
//		this.crewMemberService = crewMemberService;
//	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}
}
