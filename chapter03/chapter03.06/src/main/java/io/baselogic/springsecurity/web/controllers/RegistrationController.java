package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.domain.User;
import io.baselogic.springsecurity.service.EventService;
import io.baselogic.springsecurity.service.UserContext;
import io.baselogic.springsecurity.web.model.RegistrationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * User Registration Controller
 *
 * @author mickknutson
 *
 * @since chapter03.01 Does not create a User in the database.
 * @since chapter03.02 Creates a new User object with the eventService and sets it in the userContext
 */
@Controller
@RequestMapping("registration")
@Slf4j
public class RegistrationController {

    private final UserContext userContext;
    private final EventService eventService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //DelegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(PasswordEncoder)

    private static final String REG_FORM_VIEW = "registration/register";


    @Autowired
    public RegistrationController(final @NotNull UserContext userContext,
                                  final @NotNull EventService eventService) {
        this.userContext = userContext;
        this.eventService = eventService;
    }

    @GetMapping("/form")
    public String registration(final @ModelAttribute RegistrationDto registrationDto) {
        return REG_FORM_VIEW;
    }

    @PostMapping("/new")
    public String registration(final @Valid RegistrationDto registrationDto,
                               final BindingResult result,
                               final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach( e -> log.info("error: {}", e) );
            return REG_FORM_VIEW;
        }

        String email = registrationDto.getEmail();
        if(eventService.findUserByEmail(email) != null) {
            result.rejectValue("email", "errors.registration.email",
                    "Email address is already in use.");
            return REG_FORM_VIEW;
        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());

        user.setPassword(
                registrationDto.getPassword()
        );

//        user.setPassword(
//                passwordEncoder.encode(registrationDto.getPassword())
//        );

        int id = eventService.createUser(user);
        log.info("Created user ID {}.", id);
        user.setId(id);
        userContext.setCurrentUser(user);

        StringBuilder sb = new StringBuilder("Registration Successful.");
        sb.append(" Account created for '").append(email).append("' and automatically logged-in.");

        redirectAttributes.addFlashAttribute("message",
                sb.toString());
        return "redirect:/";
    }

} // The End...