package be.pirlewiet.digitaal.domain.people;

import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Person;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Set;

@Component
public class SpokesPerson {
    protected final TemplateEngine templateEngine;

    public SpokesPerson(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String formatIntakeMessageOrganisation(Application application, List<Person> participants, Set<Holiday> holidays, Person recipient ) {
        final String template = "email/to-organisation/intake";
        final Context ctx = new Context();
        ctx.setVariable("holidays", holidays);
        ctx.setVariable("participants",participants);
        return this.templateEngine.process(template, ctx);
    }

    protected String formatIntakeMessagePirlewiet( Application application, List<Person> participants, Set<Holiday> holidays, Person contact, Organisation organisation ) {
        // TODO!
        return "";
    }

    public String formatCodeRequestMessages(String code ) {
        final String template = "email/to-organisation/code-request";
        final Context ctx = new Context();
        ctx.setVariable("code", code);
        return this.templateEngine.process(template, ctx);
    }

}
